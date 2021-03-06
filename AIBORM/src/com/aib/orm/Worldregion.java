// Generated by com.xlend.orm.tools.dbgen.DbGenerator.class at Sun Feb 07 10:15:05 CET 2016
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class Worldregion extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer worldregionId = null;
    private String descr = null;
    private Double postPrice = null;
    private Integer postStatus = null;
    private Integer postNumber = null;

    public Worldregion(Connection connection) {
        super(connection, "worldregion", "worldregion_id");
        setColumnNames(new String[]{"worldregion_id", "descr", "post_price", "post_status", "post_number"});
    }

    public Worldregion(Connection connection, Integer worldregionId, String descr, Double postPrice, Integer postStatus, Integer postNumber) {
        super(connection, "worldregion", "worldregion_id");
        setNew(worldregionId.intValue() <= 0);
//        if (worldregionId.intValue() != 0) {
            this.worldregionId = worldregionId;
//        }
        this.descr = descr;
        this.postPrice = postPrice;
        this.postStatus = postStatus;
        this.postNumber = postNumber;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        Worldregion worldregion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT worldregion_id,descr,post_price,post_status,post_number FROM worldregion WHERE worldregion_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                worldregion = new Worldregion(getConnection());
                worldregion.setWorldregionId(new Integer(rs.getInt(1)));
                worldregion.setDescr(rs.getString(2));
                worldregion.setPostPrice(rs.getDouble(3));
                worldregion.setPostStatus(new Integer(rs.getInt(4)));
                worldregion.setPostNumber(new Integer(rs.getInt(5)));
                worldregion.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return worldregion;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO worldregion ("+(getWorldregionId().intValue()!=0?"worldregion_id,":"")+"descr,post_price,post_status,post_number) values("+(getWorldregionId().intValue()!=0?"?,":"")+"?,?,?,?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getWorldregionId().intValue()!=0) {
                 ps.setObject(++n, getWorldregionId());
             }
             ps.setObject(++n, getDescr());
             ps.setObject(++n, getPostPrice());
             ps.setObject(++n, getPostStatus());
             ps.setObject(++n, getPostNumber());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getWorldregionId().intValue()==0) {
             stmt = "SELECT max(worldregion_id) FROM worldregion";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setWorldregionId(new Integer(rs.getInt(1)));
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
                    "UPDATE worldregion " +
                    "SET descr = ?, post_price = ?, post_status = ?, post_number = ?" + 
                    " WHERE worldregion_id = " + getWorldregionId();
            try {
                ps = getConnection().prepareStatement(stmt);
                ps.setObject(1, getDescr());
                ps.setObject(2, getPostPrice());
                ps.setObject(3, getPostStatus());
                ps.setObject(4, getPostNumber());
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
        if (Country.exists(getConnection(),"worldregion_id = " + getWorldregionId())) {
            throw new ForeignKeyViolationException("Can't delete, foreign key violation: country_worldregion_fk");
        }
        if (getTriggers() != null) {
            getTriggers().beforeDelete(this);
        }
        PreparedStatement ps = null;
        String stmt =
                "DELETE FROM worldregion " +
                "WHERE worldregion_id = " + getWorldregionId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setWorldregionId(new Integer(-getWorldregionId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getWorldregionId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT worldregion_id,descr,post_price,post_status,post_number FROM worldregion " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new Worldregion(con,new Integer(rs.getInt(1)),rs.getString(2),rs.getDouble(3),new Integer(rs.getInt(4)),new Integer(rs.getInt(5))));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        Worldregion[] objects = new Worldregion[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            Worldregion worldregion = (Worldregion) lst.get(i);
            objects[i] = worldregion;
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
        String stmt = "SELECT worldregion_id FROM worldregion " +
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
    //    return getWorldregionId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return worldregionId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setWorldregionId(id);
        setNew(prevIsNew);
    }

    public Integer getWorldregionId() {
        return worldregionId;
    }

    public void setWorldregionId(Integer worldregionId) throws ForeignKeyViolationException {
        setWasChanged(this.worldregionId != null && this.worldregionId != worldregionId);
        this.worldregionId = worldregionId;
        setNew(worldregionId.intValue() == 0);
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.descr != null && !this.descr.equals(descr));
        this.descr = descr;
    }

    public Double getPostPrice() {
        return postPrice;
    }

    public void setPostPrice(Double postPrice) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.postPrice != null && !this.postPrice.equals(postPrice));
        this.postPrice = postPrice;
    }

    public Integer getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(Integer postStatus) throws SQLException, ForeignKeyViolationException {
        if (null != postStatus)
            postStatus = postStatus == 0 ? null : postStatus;
        setWasChanged(this.postStatus != null && !this.postStatus.equals(postStatus));
        this.postStatus = postStatus;
    }

    public Integer getPostNumber() {
        return postNumber;
    }

    public void setPostNumber(Integer postNumber) throws SQLException, ForeignKeyViolationException {
        if (null != postNumber)
            postNumber = postNumber == 0 ? null : postNumber;
        setWasChanged(this.postNumber != null && !this.postNumber.equals(postNumber));
        this.postNumber = postNumber;
    }
    public Object[] getAsRow() {
        Object[] columnValues = new Object[5];
        columnValues[0] = getWorldregionId();
        columnValues[1] = getDescr();
        columnValues[2] = getPostPrice();
        columnValues[3] = getPostStatus();
        columnValues[4] = getPostNumber();
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
            setWorldregionId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setWorldregionId(null);
        }
        setDescr(flds[1]);
        try {
            setPostPrice(Double.parseDouble(flds[2]));
        } catch(NumberFormatException ne) {
            setPostPrice(null);
        }
        try {
            setPostStatus(Integer.parseInt(flds[3]));
        } catch(NumberFormatException ne) {
            setPostStatus(null);
        }
        try {
            setPostNumber(Integer.parseInt(flds[4]));
        } catch(NumberFormatException ne) {
            setPostNumber(null);
        }
    }
}
