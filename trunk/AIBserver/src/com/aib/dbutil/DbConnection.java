package com.aib.dbutil;

//import com.xlend.AIBserver;
//import com.xlend.orm.Xopmachassing;
import com.aib.AIBserver;
import com.aib.rmi.RmiMessageSender;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;

/**
 *
 * @author Nick Mukhin
 */
public class DbConnection {

    private static class ConnectionWithFlag {

        boolean isBusy = false;
        Connection connection = null;

        ConnectionWithFlag(Connection c) {
            connection = c;
            isBusy = true;
        }

        private void freeConnection() {
            isBusy = false;
        }
    }
//    private static Connection logDBconnection = null;
    private static final int DB_VERSION_ID = 1;
    public static final String DB_VERSION = "0.1";
    private static boolean isFirstTime = true;
    private static Properties props = new Properties();
    private static String[] createLocalDBsqls = loadDDLscript("crebas.sql", ";");
    private static ArrayList<ConnectionWithFlag> connections = new ArrayList<ConnectionWithFlag>();

    private static String[] fixLocalDBsqls = new String[]{
        "update dbversion set version_id = " + DB_VERSION_ID + ",version = '" + DB_VERSION + "'",
        "delete from link where not exists("
            + "select complink_id from complink where link_id=link.link_id) and not exists("
            + "select peoplelink_id from peoplelink where link_id=link.link_id)",        
        "delete from industry where not exists("
            + "select compindustry_id from compindustry where industry_id=industry.industry_id) and not exists("
            + "select peopleindustry_id from peopleindustry where industry_id=industry.industry_id)",
        "delete from aibpublic where not exists(select comppublic_id from comppublic where aibpublic_id=aibpublic.aibpublic_id)"
    };

    public static String getLogin() {
        return props.getProperty("dbUser", "aib");
    }

    public static String getPassword() {
        return props.getProperty("dbPassword", "qwerty");
    }

    public static String getBackupCommand() {
        return props.getProperty("dbDump", "mysqldump");
    }

    public static String getFtpURL() {
        return props.getProperty("ftpURL", "ec2-54-226-3-180.compute-1.amazonaws.com");
    }

    public static String getFtpPath() {
        return props.getProperty("ftpPath", "/root/backups/");
    }

    public static String getFtpLogin() {
        return props.getProperty("ftpLogin", "alison");
    }

    public static String getFtpPassword() {
        return props.getProperty("ftpLogin", "dainton");
    }

    public static Connection getConnection() throws RemoteException {
        for (ConnectionWithFlag con : connections) {
            if (!con.isBusy) {
                con.isBusy = true;
//                System.out.println("!! connection FOUND");
                return con.connection;
            }
        }
        Connection connection = null;
        try {
            Locale.setDefault(Locale.ENGLISH);
            DriverManager.registerDriver(
                    (java.sql.Driver) Class.forName(
                    props.getProperty("dbDriverName",
                    "com.mysql.jdbc.Driver")).newInstance());
            connection = DriverManager.getConnection(
                    props.getProperty("dbConnection",
                    "jdbc:mysql://ec2-54-226-3-180.compute-1.amazonaws.com/aibcontact1?characterEncoding=UTF8"),
                    getLogin(), getPassword());
            connection.setAutoCommit(true);
            RmiMessageSender.isMySQL = (connection.getClass().getCanonicalName().indexOf("mysql") > -1);
            
        } catch (Exception e) {
            AIBserver.log(e);
        }
        if (isFirstTime) {
            initLocalDB(connection);
            fixLocalDB(connection);
            isFirstTime = false;
        }
        if (connection != null) {
            connections.add(new ConnectionWithFlag(connection));
//            System.out.println("!! connection CREATED");
            return checkVersion(connection);
        } else {
            return null;
        }
    }

    public static void initLocalDB(Connection connection) {
        sqlBatch(createLocalDBsqls, connection, false);
    }

    public static void fixLocalDB(Connection connection) {
        sqlBatch(fixLocalDBsqls, connection, props.getProperty("LogDbFixes", "false").equalsIgnoreCase("true"));
//        fixWrongAssignments(connection);
    }

    public static void sqlBatch(String[] sqls, Connection connection, boolean tolog) {
        PreparedStatement ps = null;
        for (int i = 0; i < sqls.length; i++) {
            try {
//                System.out.println(""+i+") "+sqls[i]);
                ps = connection.prepareStatement(sqls[i]);
                ps.execute();
                if (tolog) {
                    AIBserver.log("STATEMENT [" + sqls[i].substring(0,
                            sqls[i].length() > 60 ? 60 : sqls[i].length()) + "]... processed");
                }
            } catch (SQLException e) {
                if (tolog) {
                    AIBserver.log(e);
                }
            } finally {
                try {
                    ps.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    public static void setProps(Properties props) {
        DbConnection.props = props;
    }

    public static Properties getProps() {
        return props;
    }

    public static void closeConnection(Connection connection) throws SQLException {
        for (ConnectionWithFlag cf : connections) {
            if (cf.connection == connection) {
                cf.isBusy = false;
//                System.out.println("!! connection FREED");
                return;
            }
        }
//        connection.close();
        connection = null;
    }

    public static void closeAllConnections() throws SQLException {
        for (ConnectionWithFlag cf : connections) {
            cf.connection.close();
//            System.out.println("!! connection CLOSED");
            cf.connection = null;
        }
        connections.clear();
    }

    public static String getCurDir() {
        File curdir = new File("./");
        return curdir.getAbsolutePath();
    }

    private static Connection checkVersion(Connection connection) throws RemoteException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String err;
        String stmt = "SELECT version_id,version FROM dbversion WHERE dbversion_id=1";
        int curversion_id = 0;
        String curversion = "0.0";
        try {
            ps = connection.prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                curversion_id = rs.getInt(1);
                curversion = rs.getString(2);
            }
            if (DB_VERSION_ID > curversion_id || !curversion.equals(DB_VERSION)) {
                err = "Invalid database version! " + "expected:" + DB_VERSION + "(" + DB_VERSION_ID + ") "
                        + "found:" + curversion + "(" + curversion_id + ")";
                AIBserver.log(err);
                throw new RemoteException(err);
            }
        } catch (SQLException ex) {
            AIBserver.log(ex);
            try {
                closeConnection(connection);
            } catch (SQLException ex1) {
            }
        } finally {
            try {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                    }
                }
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException ex) {
                    }
                }
            }
        }
        return connection;
    }

//    public static void shutDownDatabase() {
//        Connection connection = null;
//        try {
//            connection = getConnection();
//            sqlBatch(new String[]{"sрutdown"}, connection, true);
//        } catch (RemoteException ex) {
//            AIBserver.log(ex);
//        } finally {
//            try {
//                closeConnection(connection);
//            } catch (SQLException ex1) {
//            }
//        }
//    }
    public static String[] loadDDLscript(String fname, String splitter) {
        String[] ans = new String[0];
        File sqlFile = new File(fname);
        boolean toclean = true;
        if (!sqlFile.exists()) {
            fname = "../sql/" + fname;
            sqlFile = new File(fname);
            toclean = false;
        }
        if (sqlFile.exists()) {
            StringBuffer contents = new StringBuffer();
            java.io.BufferedReader reader = null;
            try {
                reader = new java.io.BufferedReader(new FileReader(sqlFile));
                String line = null;
                int lineNum = 0;
                while ((line = reader.readLine()) != null) {
                    int cut = line.indexOf("--");
                    if (cut >= 0) {
                        line = line.substring(0, cut);
                    }
                    contents.append(line).append(System.getProperty("line.separator"));
                }
                ans = contents.toString().split(splitter);
            } catch (Exception e) {
                AIBserver.log(e);
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException ie) {
                }
            }
            if (toclean) {
                sqlFile.delete();
            }
        } else {
            AIBserver.log("File " + fname + " not found");
        }
        return ans;
    }

    private static ArrayList<Integer> getAnomalies(Connection connection, String stmt) {
        PreparedStatement ps = null;
        ResultSet rs = null;
//        System.out.println("!![" + stmt + "]");
        ArrayList<Integer> anomalies = new ArrayList<Integer>();
        try {
            ps = connection.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                anomalies.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            AIBserver.log(ex);
        } finally {
            try {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                    }
                }
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException ex) {
                    }
                }
            }
        }
        return anomalies;
    }
//    private static void fixWrongAssignments(Connection connection) {
//        AIBserver.log("Checking assignments...");
//        String stmt = "select xemployee_id from xopmachassing "
//                + "where date_end is null and not xemployee_id is null group by xemployee_id having count(*)>1";
//        fixOperatorsAssignments(connection, getAnomalies(connection, stmt));
//        stmt = "select xmachine_id from xopmachassing "
//                + "where date_end is null and not xmachine_id is null group by xmachine_id having count(*)>1";
//        fixMachineAssignments(connection, getAnomalies(connection, stmt));
//        stmt = "select xemployee_id from xopmachassing o "
//                + "where not exists(select * from xopmachassing "
//                + " where xemployee_id=o.xemployee_id and date_end is null)"
//                + "and not xemployee_id is null order by xemployee_id,xopmachassing_id";
//        addCurrentAssignment(connection, getAnomalies(connection, stmt), "xemployee_id");
//        stmt = "select xmachine_id from xopmachassing o "
//                + "where not exists(select * from xopmachassing "
//                + " where xmachine_id=o.xmachine_id and date_end is null)"
//                + "and not xmachine_id is null order by xmachine_id,xopmachassing_id";
//        addCurrentAssignment(connection, getAnomalies(connection, stmt), "xmachine_id");
//    }
//
//    private static void fixOperatorsAssignments(Connection connection, ArrayList<Integer> operatorAnomalies) {
//        fixAssignment(connection, operatorAnomalies, "xemployee_id");
//    }
//
//    private static void fixMachineAssignments(Connection connection, ArrayList<Integer> machineAnomalies) {
//        fixAssignment(connection, machineAnomalies, "xmachine_id");
//    }
//
//    private static void fixAssignment(Connection connection, ArrayList<Integer> anomalies, String fld) {
//        for (Integer itemID : anomalies) {
//            try {
//                Xopmachassing cur = null;
//                Xopmachassing next = null;
//                DbObject[] assigns = Xopmachassing.load(connection, fld + "=" + itemID, "xopmachassing_id");
//                for (int i = 0; i < assigns.length; i++) {
//                    cur = (Xopmachassing) assigns[i];
//                    if (cur.getDateEnd() == null && i < assigns.length - 1) {
//                        next = (Xopmachassing) assigns[i + 1];
//                        if (cur.getXmachineId() == 0) {
//                            cur.setXmachineId(null);
//                        }
//                        if (cur.getXemployeeId() == 0) {
//                            cur.setXemployeeId(null);
//                        }
//                        cur.setDateEnd(next.getDateStart());
//                        cur.save();
//                    }
//                }
//                if (cur.getDateEnd() != null) {
//                    addAssignment(cur, fld);
//                }
//            } catch (Exception ex) {
//                AIBserver.log(ex);
//            }
//        }
//    }
//
//    private static void addCurrentAssignment(Connection connection, ArrayList<Integer> anomalies, String fld) {
//        for (Integer itemID : anomalies) {
//            try {
//                DbObject[] assigns = Xopmachassing.load(connection, fld + "=" + itemID, "xopmachassing_id desc");
//                Xopmachassing cur = (Xopmachassing) assigns[0];
//                addAssignment(cur, fld);
//            } catch (Exception ex) {
//                AIBserver.log(ex);
//            }
//        }
//    }
//
//    private static void addAssignment(Xopmachassing cur, String fld) throws SQLException, ForeignKeyViolationException {
//        Date dt = cur.getDateEnd();
//        cur.setXopmachassingId(0);
//        cur.setNew(true);
//        cur.setDateStart(dt);
//        cur.setDateEnd(null);
//        if (fld.equals("xemployee_id")) {
//            cur.setXmachineId(null);
//        } else if (fld.equals("xmachine_id")) {
//            cur.setXemployeeId(null);
//        }
//        cur.save();
//    }
}