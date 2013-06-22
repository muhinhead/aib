// Generated by com.xlend.orm.tools.dbgen.DbGenerator.class at Fri Jun 21 15:59:30 EEST 2013
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class Filter extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer filterId = null;
    private String tablename = null;
    private String name = null;
    private String descr = null;
    private Integer ownerId = null;
    private String query = null;
    private Integer isComplex = null;

    public Filter(Connection connection) {
        super(connection, "filter", "filter_id");
        setColumnNames(new String[]{"filter_id", "tablename", "name", "descr", "owner_id", "query", "is_complex"});
    }

    public Filter(Connection connection, Integer filterId, String tablename, String name, String descr, Integer ownerId, String query, Integer isComplex) {
        super(connection, "filter", "filter_id");
        setNew(filterId.intValue() <= 0);
//        if (filterId.intValue() != 0) {
            this.filterId = filterId;
//        }
        this.tablename = tablename;
        this.name = name;
        this.descr = descr;
        this.ownerId = ownerId;
        this.query = query;
        this.isComplex = isComplex;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        Filter filter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT filter_id,tablename,name,descr,owner_id,query,is_complex FROM filter WHERE filter_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                filter = new Filter(getConnection());
                filter.setFilterId(new Integer(rs.getInt(1)));
                filter.setTablename(rs.getString(2));
                filter.setName(rs.getString(3));
                filter.setDescr(rs.getString(4));
                filter.setOwnerId(new Integer(rs.getInt(5)));
                filter.setQuery(rs.getString(6));
                filter.setIsComplex(new Integer(rs.getInt(7)));
                filter.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return filter;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO filter ("+(getFilterId().intValue()!=0?"filter_id,":"")+"tablename,name,descr,owner_id,query,is_complex) values("+(getFilterId().intValue()!=0?"?,":"")+"?,?,?,?,?,?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getFilterId().intValue()!=0) {
                 ps.setObject(++n, getFilterId());
             }
             ps.setObject(++n, getTablename());
             ps.setObject(++n, getName());
             ps.setObject(++n, getDescr());
             ps.setObject(++n, getOwnerId());
             ps.setObject(++n, getQuery());
             ps.setObject(++n, getIsComplex());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getFilterId().intValue()==0) {
             stmt = "SELECT max(filter_id) FROM filter";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setFilterId(new Integer(rs.getInt(1)));
                 }
             } finally {
                 try {
                     if (rs != null) rs.close();
                 } finally {
                     if (ps != null) ps.close();
                 }
             }
         }
         setNew(false);
         setWasChanged(false);
         if (getTriggers() != null) {
             getTriggers().afterInsert(this);
         }
    }

    public void save() throws SQLException, ForeignKeyViolationException {
        if (isNew()) {
            insert();
        } else {
            if (getTriggers() != null) {
                getTriggers().beforeUpdate(this);
            }
            PreparedStatement ps = null;
            String stmt =
                    "UPDATE filter " +
                    "SET tablename = ?, name = ?, descr = ?, owner_id = ?, query = ?, is_complex = ?" + 
                    " WHERE filter_id = " + getFilterId();
            try {
                ps = getConnection().prepareStatement(stmt);
                ps.setObject(1, getTablename());
                ps.setObject(2, getName());
                ps.setObject(3, getDescr());
                ps.setObject(4, getOwnerId());
                ps.setObject(5, getQuery());
                ps.setObject(6, getIsComplex());
                ps.execute();
            } finally {
                if (ps != null) ps.close();
            }
            setWasChanged(false);
            if (getTriggers() != null) {
                getTriggers().afterUpdate(this);
            }
        }
    }

    public void delete() throws SQLException, ForeignKeyViolationException {
        if (getTriggers() != null) {
            getTriggers().beforeDelete(this);
        }
        PreparedStatement ps = null;
        String stmt =
                "DELETE FROM filter " +
                "WHERE filter_id = " + getFilterId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setFilterId(new Integer(-getFilterId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getFilterId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT filter_id,tablename,name,descr,owner_id,query,is_complex FROM filter " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new Filter(con,new Integer(rs.getInt(1)),rs.getString(2),rs.getString(3),rs.getString(4),new Integer(rs.getInt(5)),rs.getString(6),new Integer(rs.getInt(7))));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        Filter[] objects = new Filter[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            Filter filter = (Filter) lst.get(i);
            objects[i] = filter;
        }
        return objects;
    }

    public static boolean exists(Connection con, String whereCondition) throws SQLException {
        if (con == null) {
            return true;
        }
        boolean ok = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT filter_id FROM filter " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                "WHERE " + whereCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            ok = rs.next();
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return ok;
    }

    //public String toString() {
    //    return getFilterId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return filterId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setFilterId(id);
        setNew(prevIsNew);
    }

    public Integer getFilterId() {
        return filterId;
    }

    public void setFilterId(Integer filterId) throws ForeignKeyViolationException {
        setWasChanged(this.filterId != null && this.filterId != filterId);
        this.filterId = filterId;
        setNew(filterId.intValue() == 0);
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.tablename != null && !this.tablename.equals(tablename));
        this.tablename = tablename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.name != null && !this.name.equals(name));
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.descr != null && !this.descr.equals(descr));
        this.descr = descr;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) throws SQLException, ForeignKeyViolationException {
        if (ownerId!=null && !User.exists(getConnection(),"user_id = " + ownerId)) {
            throw new ForeignKeyViolationException("Can't set owner_id, foreign key violation: filter_user_fk");
        }
        setWasChanged(this.ownerId != null && !this.ownerId.equals(ownerId));
        this.ownerId = ownerId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.query != null && !this.query.equals(query));
        this.query = query;
    }

    public Integer getIsComplex() {
        return isComplex;
    }

    public void setIsComplex(Integer isComplex) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.isComplex != null && !this.isComplex.equals(isComplex));
        this.isComplex = isComplex;
    }
    public Object[] getAsRow() {
        Object[] columnValues = new Object[7];
        columnValues[0] = getFilterId();
        columnValues[1] = getTablename();
        columnValues[2] = getName();
        columnValues[3] = getDescr();
        columnValues[4] = getOwnerId();
        columnValues[5] = getQuery();
        columnValues[6] = getIsComplex();
        return columnValues;
    }

    public static void setTriggers(Triggers triggers) {
        activeTriggers = triggers;
    }

    public static Triggers getTriggers() {
        return activeTriggers;
    }

    //for SOAP exhange
    @Override
    public void fillFromString(String row) throws ForeignKeyViolationException, SQLException {
        String[] flds = splitStr(row, delimiter);
        try {
            setFilterId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setFilterId(null);
        }
        setTablename(flds[1]);
        setName(flds[2]);
        setDescr(flds[3]);
        try {
            setOwnerId(Integer.parseInt(flds[4]));
        } catch(NumberFormatException ne) {
            setOwnerId(null);
        }
        setQuery(flds[5]);
        try {
            setIsComplex(Integer.parseInt(flds[6]));
        } catch(NumberFormatException ne) {
            setIsComplex(null);
        }
    }
}
