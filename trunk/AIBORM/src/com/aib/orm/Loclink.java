// Generated by com.xlend.orm.tools.dbgen.DbGenerator.class at Mon May 06 19:30:31 EEST 2013
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class Loclink extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer loclinkId = null;
    private Integer locationId = null;
    private Integer linkId = null;

    public Loclink(Connection connection) {
        super(connection, "loclink", "loclink_id");
        setColumnNames(new String[]{"loclink_id", "location_id", "link_id"});
    }

    public Loclink(Connection connection, Integer loclinkId, Integer locationId, Integer linkId) {
        super(connection, "loclink", "loclink_id");
        setNew(loclinkId.intValue() <= 0);
//        if (loclinkId.intValue() != 0) {
            this.loclinkId = loclinkId;
//        }
        this.locationId = locationId;
        this.linkId = linkId;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        Loclink loclink = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT loclink_id,location_id,link_id FROM loclink WHERE loclink_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                loclink = new Loclink(getConnection());
                loclink.setLoclinkId(new Integer(rs.getInt(1)));
                loclink.setLocationId(new Integer(rs.getInt(2)));
                loclink.setLinkId(new Integer(rs.getInt(3)));
                loclink.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return loclink;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO loclink ("+(getLoclinkId().intValue()!=0?"loclink_id,":"")+"location_id,link_id) values("+(getLoclinkId().intValue()!=0?"?,":"")+"?,?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getLoclinkId().intValue()!=0) {
                 ps.setObject(++n, getLoclinkId());
             }
             ps.setObject(++n, getLocationId());
             ps.setObject(++n, getLinkId());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getLoclinkId().intValue()==0) {
             stmt = "SELECT max(loclink_id) FROM loclink";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setLoclinkId(new Integer(rs.getInt(1)));
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
                    "UPDATE loclink " +
                    "SET location_id = ?, link_id = ?" + 
                    " WHERE loclink_id = " + getLoclinkId();
            try {
                ps = getConnection().prepareStatement(stmt);
                ps.setObject(1, getLocationId());
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
                "DELETE FROM loclink " +
                "WHERE loclink_id = " + getLoclinkId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setLoclinkId(new Integer(-getLoclinkId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getLoclinkId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT loclink_id,location_id,link_id FROM loclink " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new Loclink(con,new Integer(rs.getInt(1)),new Integer(rs.getInt(2)),new Integer(rs.getInt(3))));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        Loclink[] objects = new Loclink[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            Loclink loclink = (Loclink) lst.get(i);
            objects[i] = loclink;
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
        String stmt = "SELECT loclink_id FROM loclink " +
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
    //    return getLoclinkId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return loclinkId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setLoclinkId(id);
        setNew(prevIsNew);
    }

    public Integer getLoclinkId() {
        return loclinkId;
    }

    public void setLoclinkId(Integer loclinkId) throws ForeignKeyViolationException {
        setWasChanged(this.loclinkId != null && this.loclinkId != loclinkId);
        this.loclinkId = loclinkId;
        setNew(loclinkId.intValue() == 0);
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) throws SQLException, ForeignKeyViolationException {
        if (locationId!=null && !Location.exists(getConnection(),"location_id = " + locationId)) {
            throw new ForeignKeyViolationException("Can't set location_id, foreign key violation: loclink_location_fk");
        }
        setWasChanged(this.locationId != null && !this.locationId.equals(locationId));
        this.locationId = locationId;
    }

    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) throws SQLException, ForeignKeyViolationException {
        if (linkId!=null && !Link.exists(getConnection(),"link_id = " + linkId)) {
            throw new ForeignKeyViolationException("Can't set link_id, foreign key violation: loclink_link_fk");
        }
        setWasChanged(this.linkId != null && !this.linkId.equals(linkId));
        this.linkId = linkId;
    }
    public Object[] getAsRow() {
        Object[] columnValues = new Object[3];
        columnValues[0] = getLoclinkId();
        columnValues[1] = getLocationId();
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
            setLoclinkId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setLoclinkId(null);
        }
        try {
            setLocationId(Integer.parseInt(flds[1]));
        } catch(NumberFormatException ne) {
            setLocationId(null);
        }
        try {
            setLinkId(Integer.parseInt(flds[2]));
        } catch(NumberFormatException ne) {
            setLinkId(null);
        }
    }
}
