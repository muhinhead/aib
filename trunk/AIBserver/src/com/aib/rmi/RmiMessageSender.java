package com.aib.rmi;

import com.aib.AIBserver;
import com.aib.dbutil.DbConnection;
import com.aib.orm.dbobject.DbObject;
import com.aib.remote.IMessageSender;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author Nick Mukhin
 */
public class RmiMessageSender extends java.rmi.server.UnicastRemoteObject implements IMessageSender {

    public static Boolean isMySQL = null;
//    private Connection connection;

    public RmiMessageSender() throws java.rmi.RemoteException {
        //        connection = DbConnection.getConnection();
        //        if (null == connection) {
        //            throw new java.rmi.RemoteException("Can't establish database connection");
        //        }
    }

//    @Override
    public DbObject[] getDbObjects(Class dbobClass, String whereCondition,
            String orderCondition) throws RemoteException {
        DbObject[] rows = null;
        Connection connection = DbConnection.getConnection();
        try {
            Method method = dbobClass.getDeclaredMethod("load", Connection.class, String.class, String.class);
            rows = (DbObject[]) method.invoke(null, connection, whereCondition, orderCondition);
        } catch (Exception ex) {
            AIBserver.log(ex);
            throw new java.rmi.RemoteException(ex.getMessage());
        } finally {
            try {
                DbConnection.closeConnection(connection);
            } catch (SQLException ex) {
                AIBserver.log(ex);
                throw new java.rmi.RemoteException(ex.getMessage());
            }
        }
        return rows;
    }

//    @Override
    public DbObject saveDbObject(DbObject dbob) throws RemoteException {
        if (dbob != null) {
            Connection connection = DbConnection.getConnection();
            try {
                boolean wasNew = dbob.isNew();
                dbob.setConnection(connection);
                dbob.save();
//                Connection logConnection = DbConnection.getLogDBconnection();
//                Object[] asRow = dbob.getAsRow();
//                logDbOperation(logConnection, dbob.getClass().getCanonicalName(), (Integer) asRow[0], wasNew ? 1 : 0);
            } catch (Exception ex) {
                AIBserver.log(ex);
                throw new java.rmi.RemoteException("Can't save DB object:", ex);
            } finally {
                try {
                    DbConnection.closeConnection(connection);
                } catch (SQLException ex) {
                    AIBserver.log(ex);
                    throw new java.rmi.RemoteException(ex.getMessage());
                }
            }
        }
        return dbob;
    }

//    @Override
    public DbObject loadDbObjectOnID(Class dbobClass, int id) throws RemoteException {
        Connection connection = DbConnection.getConnection();
        DbObject dbob = null;
        try {
            Constructor constructor = dbobClass.getConstructor(Connection.class);
            dbob = (DbObject) constructor.newInstance(connection);//DbConnection.getConnection());
            return dbob.loadOnId(id);
        } catch (Exception ex) {
            AIBserver.log(ex);
            throw new java.rmi.RemoteException(ex.getMessage());
        } finally {
            try {
                DbConnection.closeConnection(connection);
            } catch (SQLException ex) {
                AIBserver.log(ex);
                throw new java.rmi.RemoteException(ex.getMessage());
            }
        }
    }

////    @Override
//    public Vector[] getTableBody(String select) throws RemoteException {
//        Vector headers = getColNames(select);
//        return new Vector[]{headers, getRows(select, headers.size())};
//    }
    @Override
    public Vector[] getTableBody(String select) throws RemoteException {
        return getTableBody(select, 0, 0);
    }

    @Override
    public Vector[] getTableBody(String select, int page, int pagesize) throws java.rmi.RemoteException {
        Vector headers = getColNames(select);
        int startrow = 0, endrow = 0;
        if (page > 0 || pagesize > 0) {
            startrow = page * pagesize + 1; //int page starts from 0, int startrow starts from 1
            endrow = (page + 1) * pagesize; //last row of page
        }
        return new Vector[]{headers, getRows(select, headers.size(), startrow, endrow)};
    }

    private Vector getColNames(String select) throws RemoteException {
        Connection connection = DbConnection.getConnection();
        Vector colNames = new Vector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(select);
            rs = ps.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            for (int i = 0; i < md.getColumnCount(); i++) {
                colNames.add(md.getColumnLabel(i + 1));
            }
            return colNames;
        } catch (SQLException ex) {
            AIBserver.log(ex);
            throw new java.rmi.RemoteException(ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se1) {
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (SQLException se2) {
                }
            }
            try {
                DbConnection.closeConnection(connection);
            } catch (SQLException ex) {
                AIBserver.log(ex);
                throw new java.rmi.RemoteException(ex.getMessage());
            }
        }
    }

    private Vector getRows(String select, int cols, int startrow, int endrow) throws RemoteException {
        Connection connection = DbConnection.getConnection();
        Vector rows = new Vector();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String pagedSelect;
            if (startrow == 0 && endrow == 0) {
                pagedSelect = select;
            } else {
                //pagedSelect = "Select * from (select subq.*,rownum rn from (" + select + ") subq) where rn between " + startrow + " and " + endrow;
                pagedSelect = select.replaceFirst("select", "SELECT").replaceAll("Select", "SELECT");
                if (isMySQL) //pagedSelect = pagedSelect.replaceFirst("SELECT", "SELECT LIMIT " + (startrow-1) + " " + (endrow - startrow + 1));
                {
                    pagedSelect += (" LIMIT " + (startrow - 1) + "," + (endrow - startrow + 1));
                } else {
                    pagedSelect = pagedSelect.replaceFirst("SELECT", "SELECT LIMIT " + (startrow - 1) + " " + (endrow - startrow + 1));
                }
                //System.out.println("!!PAGESELECT="+pagedSelect);
            }
            Vector line;
            ps = connection.prepareStatement(pagedSelect);
            rs = ps.executeQuery();
            while (rs.next()) {
                line = new Vector();
                for (int c = 0; c < cols; c++) {
                    String ceil = rs.getString(c + 1);
                    ceil = ceil == null ? "" : ceil;
                    line.add(ceil);
                }
                rows.add(line);
            }
            return rows;
        } catch (SQLException ex) {
            AIBserver.log(ex);
            throw new java.rmi.RemoteException(ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se1) {
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (SQLException se2) {
                }
            }
            try {
                DbConnection.closeConnection(connection);
            } catch (SQLException ex) {
                AIBserver.log(ex);
                throw new java.rmi.RemoteException(ex.getMessage());
            }
        }
    }

//    private Vector getRows(String select, int cols) throws RemoteException {
//        Connection connection = DbConnection.getConnection();
//        Vector rows = new Vector();
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        try {
//            Vector line;
//            ps = connection.prepareStatement(select);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                line = new Vector();
//                for (int c = 0; c < cols; c++) {
//                    String ceil = rs.getString(c + 1);
//                    ceil = ceil == null ? "" : ceil;
//                    line.add(ceil);
//                }
//                rows.add(line);
//            }
//            return rows;
//        } catch (SQLException ex) {
//            AIBserver.log(ex);
//            throw new java.rmi.RemoteException(ex.getMessage());
//        } finally {
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//            } catch (SQLException se1) {
//            } finally {
//                try {
//                    if (ps != null) {
//                        ps.close();
//                    }
//                } catch (SQLException se2) {
//                }
//            }
//            try {
//                DbConnection.closeConnection(connection);
//            } catch (SQLException ex) {
//                AIBserver.log(ex);
//                throw new java.rmi.RemoteException(ex.getMessage());
//            }
//        }
//    }
//    @Override
    public void deleteObject(DbObject dbob) throws RemoteException {
        if (dbob != null) {
            Connection connection = DbConnection.getConnection();
            try {
                dbob.setConnection(connection);
                dbob.delete();
//                Connection logConnection = DbConnection.getLogDBconnection();
//                Object[] asRow = dbob.getAsRow();
//                int id = ((Integer) asRow[0]).intValue();
//                logDbOperation(logConnection, dbob.getClass().getCanonicalName(), id < 0 ? -id : id, -1);
            } catch (Exception ex) {
                AIBserver.log(ex);
                throw new java.rmi.RemoteException(ex.getMessage());
            } finally {
                try {
                    DbConnection.closeConnection(connection);
                } catch (SQLException ex) {
                    AIBserver.log(ex);
                    throw new java.rmi.RemoteException(ex.getMessage());
                }
            }
        }
    }

    private void execute(String stmt) throws RemoteException {
        PreparedStatement ps = null;
        Connection connection = DbConnection.getConnection();
        try {
            ps = connection.prepareStatement(stmt);
            ps.execute();
        } catch (SQLException ex) {
            AIBserver.log(ex);
            throw new java.rmi.RemoteException(ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException se2) {
            }
            try {
                DbConnection.closeConnection(connection);
            } catch (SQLException ex) {
                AIBserver.log(ex);
                throw new java.rmi.RemoteException(ex.getMessage());
            }
        }
    }

//    @Override
    public void startTransaction(String transactionName) throws RemoteException {
        //execute("SAVEPOINT " + transactionName);
    }

//    @Override
    public void commitTransaction() throws RemoteException {
        //execute("COMMIT");
    }

//    @Override
    public void rollbackTransaction(String transactionName) throws RemoteException {
        //execute("ROLLBACK to " + transactionName);
    }

    public int getCount(String select) throws RemoteException {
        StringBuffer slct;
        int count = 0;
        int p = select.toLowerCase().lastIndexOf("order by");
        slct = new StringBuffer("select count(*) from (" + select.substring(0, p > 0 ? p : select.length()) + ") intab");
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = DbConnection.getConnection();
        try {
            ps = connection.prepareStatement(slct.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException ex) {
            AIBserver.log(ex);
            throw new java.rmi.RemoteException(ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se1) {
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (SQLException se2) {
                }
            }
            try {
                DbConnection.closeConnection(connection);
            } catch (SQLException ex) {
                AIBserver.log(ex);
                throw new java.rmi.RemoteException(ex.getMessage());
            }
        }
        return count;
    }

//    private void logDbOperation(Connection logConnection, String className, Integer id, int mode) throws RemoteException {
//        PreparedStatement ps = null;
//        try {
//            ps = logConnection.prepareStatement(
//                    "insert into updatelog(classname,operation,id) "
//                    + "values('" + className + "'," + mode + "," + id.toString() + ")");
//            ps.execute();
//        } catch (SQLException ex) {
//            AIBserver.log(ex);
//            throw new java.rmi.RemoteException(ex.getMessage());
//        } finally {
//            try {
//                if (ps != null) {
//                    ps.close();
//                }
//            } catch (SQLException se2) {
//            }
//        }
//    }
    public String getServerVersion() throws RemoteException {
        return AIBserver.getVersion();
    }

    @Override
    public boolean truncateTable(String tableName) throws RemoteException {
        boolean ok = false;
        PreparedStatement ps = null;
        Connection connection = DbConnection.getConnection();
        try {
            ps = connection.prepareStatement("delete from " + tableName);
            ok = ps.execute();
        } catch (SQLException ex) {
            AIBserver.log(ex);
            throw new java.rmi.RemoteException(ex.getMessage());
        } finally {
            try {
                ps.close();
            } catch(Exception e){}
            try {
                DbConnection.closeConnection(connection);
            } catch (SQLException ex) {
                AIBserver.log(ex);
                throw new java.rmi.RemoteException(ex.getMessage());
            }
        }
        return ok;
    }
}
