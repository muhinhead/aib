// Generated by com.xlend.orm.tools.dbgen.DbGenerator.class at Sun Jun 02 13:58:13 EEST 2013
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class Peoplelink extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer peoplelinkId = null;
    private Integer peopleId = null;
    private Integer linkId = null;

    public Peoplelink(Connection connection) {
        super(connection, "peoplelink", "peoplelink_id");
        setColumnNames(new String[]{"peoplelink_id", "people_id", "link_id"});
    }

    public Peoplelink(Connection connection, Integer peoplelinkId, Integer peopleId, Integer linkId) {
        super(connection, "peoplelink", "peoplelink_id");
        setNew(peoplelinkId.intValue() <= 0);
//        if (peoplelinkId.intValue() != 0) {
            this.peoplelinkId = peoplelinkId;
//        }
        this.peopleId = peopleId;
        this.linkId = linkId;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        Peoplelink peoplelink = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT peoplelink_id,people_id,link_id FROM peoplelink WHERE peoplelink_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                peoplelink = new Peoplelink(getConnection());
                peoplelink.setPeoplelinkId(new Integer(rs.getInt(1)));
                peoplelink.setPeopleId(new Integer(rs.getInt(2)));
                peoplelink.setLinkId(new Integer(rs.getInt(3)));
                peoplelink.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return peoplelink;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO peoplelink ("+(getPeoplelinkId().intValue()!=0?"peoplelink_id,":"")+"people_id,link_id) values("+(getPeoplelinkId().intValue()!=0?"?,":"")+"?,?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getPeoplelinkId().intValue()!=0) {
                 ps.setObject(++n, getPeoplelinkId());
             }
             ps.setObject(++n, getPeopleId());
             ps.setObject(++n, getLinkId());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getPeoplelinkId().intValue()==0) {
             stmt = "SELECT max(peoplelink_id) FROM peoplelink";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setPeoplelinkId(new Integer(rs.getInt(1)));
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
                    "UPDATE peoplelink " +
                    "SET people_id = ?, link_id = ?" + 
                    " WHERE peoplelink_id = " + getPeoplelinkId();
            try {
                ps = getConnection().prepareStatement(stmt);
                ps.setObject(1, getPeopleId());
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
                "DELETE FROM peoplelink " +
                "WHERE peoplelink_id = " + getPeoplelinkId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setPeoplelinkId(new Integer(-getPeoplelinkId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getPeoplelinkId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT peoplelink_id,people_id,link_id FROM peoplelink " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new Peoplelink(con,new Integer(rs.getInt(1)),new Integer(rs.getInt(2)),new Integer(rs.getInt(3))));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        Peoplelink[] objects = new Peoplelink[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            Peoplelink peoplelink = (Peoplelink) lst.get(i);
            objects[i] = peoplelink;
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
        String stmt = "SELECT peoplelink_id FROM peoplelink " +
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
    //    return getPeoplelinkId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return peoplelinkId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setPeoplelinkId(id);
        setNew(prevIsNew);
    }

    public Integer getPeoplelinkId() {
        return peoplelinkId;
    }

    public void setPeoplelinkId(Integer peoplelinkId) throws ForeignKeyViolationException {
        setWasChanged(this.peoplelinkId != null && this.peoplelinkId != peoplelinkId);
        this.peoplelinkId = peoplelinkId;
        setNew(peoplelinkId.intValue() == 0);
    }

    public Integer getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(Integer peopleId) throws SQLException, ForeignKeyViolationException {
        if (peopleId!=null && !People.exists(getConnection(),"people_id = " + peopleId)) {
            throw new ForeignKeyViolationException("Can't set people_id, foreign key violation: peoplelink_people_fk");
        }
        setWasChanged(this.peopleId != null && !this.peopleId.equals(peopleId));
        this.peopleId = peopleId;
    }

    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) throws SQLException, ForeignKeyViolationException {
        if (linkId!=null && !Link.exists(getConnection(),"link_id = " + linkId)) {
            throw new ForeignKeyViolationException("Can't set link_id, foreign key violation: peoplelink_link_fk");
        }
        setWasChanged(this.linkId != null && !this.linkId.equals(linkId));
        this.linkId = linkId;
    }
    public Object[] getAsRow() {
        Object[] columnValues = new Object[3];
        columnValues[0] = getPeoplelinkId();
        columnValues[1] = getPeopleId();
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
            setPeoplelinkId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setPeoplelinkId(null);
        }
        try {
            setPeopleId(Integer.parseInt(flds[1]));
        } catch(NumberFormatException ne) {
            setPeopleId(null);
        }
        try {
            setLinkId(Integer.parseInt(flds[2]));
        } catch(NumberFormatException ne) {
            setLinkId(null);
        }
    }
}
