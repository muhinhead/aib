// Generated by com.xlend.orm.tools.dbgen.DbGenerator.class at Sun Feb 07 10:15:04 CET 2016
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class Locindustry extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer locindustryId = null;
    private Integer locationId = null;
    private Integer industryId = null;

    public Locindustry(Connection connection) {
        super(connection, "locindustry", "locindustry_id");
        setColumnNames(new String[]{"locindustry_id", "location_id", "industry_id"});
    }

    public Locindustry(Connection connection, Integer locindustryId, Integer locationId, Integer industryId) {
        super(connection, "locindustry", "locindustry_id");
        setNew(locindustryId.intValue() <= 0);
//        if (locindustryId.intValue() != 0) {
            this.locindustryId = locindustryId;
//        }
        this.locationId = locationId;
        this.industryId = industryId;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        Locindustry locindustry = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT locindustry_id,location_id,industry_id FROM locindustry WHERE locindustry_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                locindustry = new Locindustry(getConnection());
                locindustry.setLocindustryId(new Integer(rs.getInt(1)));
                locindustry.setLocationId(new Integer(rs.getInt(2)));
                locindustry.setIndustryId(new Integer(rs.getInt(3)));
                locindustry.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return locindustry;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO locindustry ("+(getLocindustryId().intValue()!=0?"locindustry_id,":"")+"location_id,industry_id) values("+(getLocindustryId().intValue()!=0?"?,":"")+"?,?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getLocindustryId().intValue()!=0) {
                 ps.setObject(++n, getLocindustryId());
             }
             ps.setObject(++n, getLocationId());
             ps.setObject(++n, getIndustryId());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getLocindustryId().intValue()==0) {
             stmt = "SELECT max(locindustry_id) FROM locindustry";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setLocindustryId(new Integer(rs.getInt(1)));
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
                    "UPDATE locindustry " +
                    "SET location_id = ?, industry_id = ?" + 
                    " WHERE locindustry_id = " + getLocindustryId();
            try {
                ps = getConnection().prepareStatement(stmt);
                ps.setObject(1, getLocationId());
                ps.setObject(2, getIndustryId());
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
                "DELETE FROM locindustry " +
                "WHERE locindustry_id = " + getLocindustryId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setLocindustryId(new Integer(-getLocindustryId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getLocindustryId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT locindustry_id,location_id,industry_id FROM locindustry " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new Locindustry(con,new Integer(rs.getInt(1)),new Integer(rs.getInt(2)),new Integer(rs.getInt(3))));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        Locindustry[] objects = new Locindustry[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            Locindustry locindustry = (Locindustry) lst.get(i);
            objects[i] = locindustry;
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
        String stmt = "SELECT locindustry_id FROM locindustry " +
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
    //    return getLocindustryId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return locindustryId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setLocindustryId(id);
        setNew(prevIsNew);
    }

    public Integer getLocindustryId() {
        return locindustryId;
    }

    public void setLocindustryId(Integer locindustryId) throws ForeignKeyViolationException {
        setWasChanged(this.locindustryId != null && this.locindustryId != locindustryId);
        this.locindustryId = locindustryId;
        setNew(locindustryId.intValue() == 0);
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) throws SQLException, ForeignKeyViolationException {
        if (locationId!=null && !Location.exists(getConnection(),"location_id = " + locationId)) {
            throw new ForeignKeyViolationException("Can't set location_id, foreign key violation: locindustry_location_fk");
        }
        setWasChanged(this.locationId != null && !this.locationId.equals(locationId));
        this.locationId = locationId;
    }

    public Integer getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Integer industryId) throws SQLException, ForeignKeyViolationException {
        if (industryId!=null && !Industry.exists(getConnection(),"industry_id = " + industryId)) {
            throw new ForeignKeyViolationException("Can't set industry_id, foreign key violation: locindustry_industry_fk");
        }
        setWasChanged(this.industryId != null && !this.industryId.equals(industryId));
        this.industryId = industryId;
    }
    public Object[] getAsRow() {
        Object[] columnValues = new Object[3];
        columnValues[0] = getLocindustryId();
        columnValues[1] = getLocationId();
        columnValues[2] = getIndustryId();
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
            setLocindustryId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setLocindustryId(null);
        }
        try {
            setLocationId(Integer.parseInt(flds[1]));
        } catch(NumberFormatException ne) {
            setLocationId(null);
        }
        try {
            setIndustryId(Integer.parseInt(flds[2]));
        } catch(NumberFormatException ne) {
            setIndustryId(null);
        }
    }
}
