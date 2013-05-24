// Generated by com.xlend.orm.tools.dbgen.DbGenerator.class at Fri May 24 20:05:19 EEST 2013
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class Complink extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer complinkId = null;
    private Integer companyId = null;
    private Integer linkId = null;

    public Complink(Connection connection) {
        super(connection, "complink", "complink_id");
        setColumnNames(new String[]{"complink_id", "company_id", "link_id"});
    }

    public Complink(Connection connection, Integer complinkId, Integer companyId, Integer linkId) {
        super(connection, "complink", "complink_id");
        setNew(complinkId.intValue() <= 0);
//        if (complinkId.intValue() != 0) {
            this.complinkId = complinkId;
//        }
        this.companyId = companyId;
        this.linkId = linkId;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        Complink complink = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT complink_id,company_id,link_id FROM complink WHERE complink_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                complink = new Complink(getConnection());
                complink.setComplinkId(new Integer(rs.getInt(1)));
                complink.setCompanyId(new Integer(rs.getInt(2)));
                complink.setLinkId(new Integer(rs.getInt(3)));
                complink.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return complink;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO complink ("+(getComplinkId().intValue()!=0?"complink_id,":"")+"company_id,link_id) values("+(getComplinkId().intValue()!=0?"?,":"")+"?,?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getComplinkId().intValue()!=0) {
                 ps.setObject(++n, getComplinkId());
             }
             ps.setObject(++n, getCompanyId());
             ps.setObject(++n, getLinkId());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getComplinkId().intValue()==0) {
             stmt = "SELECT max(complink_id) FROM complink";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setComplinkId(new Integer(rs.getInt(1)));
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
                    "UPDATE complink " +
                    "SET company_id = ?, link_id = ?" + 
                    " WHERE complink_id = " + getComplinkId();
            try {
                ps = getConnection().prepareStatement(stmt);
                ps.setObject(1, getCompanyId());
                ps.setObject(2, getLinkId());
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
                "DELETE FROM complink " +
                "WHERE complink_id = " + getComplinkId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setComplinkId(new Integer(-getComplinkId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getComplinkId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT complink_id,company_id,link_id FROM complink " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new Complink(con,new Integer(rs.getInt(1)),new Integer(rs.getInt(2)),new Integer(rs.getInt(3))));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        Complink[] objects = new Complink[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            Complink complink = (Complink) lst.get(i);
            objects[i] = complink;
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
        String stmt = "SELECT complink_id FROM complink " +
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
    //    return getComplinkId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return complinkId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setComplinkId(id);
        setNew(prevIsNew);
    }

    public Integer getComplinkId() {
        return complinkId;
    }

    public void setComplinkId(Integer complinkId) throws ForeignKeyViolationException {
        setWasChanged(this.complinkId != null && this.complinkId != complinkId);
        this.complinkId = complinkId;
        setNew(complinkId.intValue() == 0);
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) throws SQLException, ForeignKeyViolationException {
        if (companyId!=null && !Company.exists(getConnection(),"company_id = " + companyId)) {
            throw new ForeignKeyViolationException("Can't set company_id, foreign key violation: complink_company_fk");
        }
        setWasChanged(this.companyId != null && !this.companyId.equals(companyId));
        this.companyId = companyId;
    }

    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) throws SQLException, ForeignKeyViolationException {
        if (linkId!=null && !Link.exists(getConnection(),"link_id = " + linkId)) {
            throw new ForeignKeyViolationException("Can't set link_id, foreign key violation: complink_link_fk");
        }
        setWasChanged(this.linkId != null && !this.linkId.equals(linkId));
        this.linkId = linkId;
    }
    public Object[] getAsRow() {
        Object[] columnValues = new Object[3];
        columnValues[0] = getComplinkId();
        columnValues[1] = getCompanyId();
        columnValues[2] = getLinkId();
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
            setComplinkId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setComplinkId(null);
        }
        try {
            setCompanyId(Integer.parseInt(flds[1]));
        } catch(NumberFormatException ne) {
            setCompanyId(null);
        }
        try {
            setLinkId(Integer.parseInt(flds[2]));
        } catch(NumberFormatException ne) {
            setLinkId(null);
        }
    }
}
