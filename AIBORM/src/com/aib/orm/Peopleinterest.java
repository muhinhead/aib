// Generated by com.xlend.orm.tools.dbgen.DbGenerator.class at Sun Feb 07 10:15:05 CET 2016
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class Peopleinterest extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer peopleinterestId = null;
    private Date purchaseDate = null;
    private Integer peopleId = null;
    private Integer productId = null;
    private String prospectingLevel = null;

    public Peopleinterest(Connection connection) {
        super(connection, "peopleinterest", "peopleinterest_id");
        setColumnNames(new String[]{"peopleinterest_id", "purchase_date", "people_id", "product_id", "prospecting_level"});
    }

    public Peopleinterest(Connection connection, Integer peopleinterestId, Date purchaseDate, Integer peopleId, Integer productId, String prospectingLevel) {
        super(connection, "peopleinterest", "peopleinterest_id");
        setNew(peopleinterestId.intValue() <= 0);
//        if (peopleinterestId.intValue() != 0) {
            this.peopleinterestId = peopleinterestId;
//        }
        this.purchaseDate = purchaseDate;
        this.peopleId = peopleId;
        this.productId = productId;
        this.prospectingLevel = prospectingLevel;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        Peopleinterest peopleinterest = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT peopleinterest_id,purchase_date,people_id,product_id,prospecting_level FROM peopleinterest WHERE peopleinterest_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                peopleinterest = new Peopleinterest(getConnection());
                peopleinterest.setPeopleinterestId(new Integer(rs.getInt(1)));
                peopleinterest.setPurchaseDate(rs.getDate(2));
                peopleinterest.setPeopleId(new Integer(rs.getInt(3)));
                peopleinterest.setProductId(new Integer(rs.getInt(4)));
                peopleinterest.setProspectingLevel(rs.getString(5));
                peopleinterest.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return peopleinterest;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO peopleinterest ("+(getPeopleinterestId().intValue()!=0?"peopleinterest_id,":"")+"purchase_date,people_id,product_id,prospecting_level) values("+(getPeopleinterestId().intValue()!=0?"?,":"")+"?,?,?,?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getPeopleinterestId().intValue()!=0) {
                 ps.setObject(++n, getPeopleinterestId());
             }
             ps.setObject(++n, getPurchaseDate());
             ps.setObject(++n, getPeopleId());
             ps.setObject(++n, getProductId());
             ps.setObject(++n, getProspectingLevel());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getPeopleinterestId().intValue()==0) {
             stmt = "SELECT max(peopleinterest_id) FROM peopleinterest";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setPeopleinterestId(new Integer(rs.getInt(1)));
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
                    "UPDATE peopleinterest " +
                    "SET purchase_date = ?, people_id = ?, product_id = ?, prospecting_level = ?" + 
                    " WHERE peopleinterest_id = " + getPeopleinterestId();
            try {
                ps = getConnection().prepareStatement(stmt);
                ps.setObject(1, getPurchaseDate());
                ps.setObject(2, getPeopleId());
                ps.setObject(3, getProductId());
                ps.setObject(4, getProspectingLevel());
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
                "DELETE FROM peopleinterest " +
                "WHERE peopleinterest_id = " + getPeopleinterestId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setPeopleinterestId(new Integer(-getPeopleinterestId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getPeopleinterestId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT peopleinterest_id,purchase_date,people_id,product_id,prospecting_level FROM peopleinterest " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new Peopleinterest(con,new Integer(rs.getInt(1)),rs.getDate(2),new Integer(rs.getInt(3)),new Integer(rs.getInt(4)),rs.getString(5)));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        Peopleinterest[] objects = new Peopleinterest[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            Peopleinterest peopleinterest = (Peopleinterest) lst.get(i);
            objects[i] = peopleinterest;
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
        String stmt = "SELECT peopleinterest_id FROM peopleinterest " +
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
    //    return getPeopleinterestId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return peopleinterestId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setPeopleinterestId(id);
        setNew(prevIsNew);
    }

    public Integer getPeopleinterestId() {
        return peopleinterestId;
    }

    public void setPeopleinterestId(Integer peopleinterestId) throws ForeignKeyViolationException {
        setWasChanged(this.peopleinterestId != null && this.peopleinterestId != peopleinterestId);
        this.peopleinterestId = peopleinterestId;
        setNew(peopleinterestId.intValue() == 0);
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.purchaseDate != null && !this.purchaseDate.equals(purchaseDate));
        this.purchaseDate = purchaseDate;
    }

    public Integer getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(Integer peopleId) throws SQLException, ForeignKeyViolationException {
        if (peopleId!=null && !People.exists(getConnection(),"people_id = " + peopleId)) {
            throw new ForeignKeyViolationException("Can't set people_id, foreign key violation: peoplinterest_people_fk");
        }
        setWasChanged(this.peopleId != null && !this.peopleId.equals(peopleId));
        this.peopleId = peopleId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) throws SQLException, ForeignKeyViolationException {
        if (productId!=null && !Product.exists(getConnection(),"product_id = " + productId)) {
            throw new ForeignKeyViolationException("Can't set product_id, foreign key violation: peoplinterest_product_fk");
        }
        setWasChanged(this.productId != null && !this.productId.equals(productId));
        this.productId = productId;
    }

    public String getProspectingLevel() {
        return prospectingLevel;
    }

    public void setProspectingLevel(String prospectingLevel) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.prospectingLevel != null && !this.prospectingLevel.equals(prospectingLevel));
        this.prospectingLevel = prospectingLevel;
    }
    public Object[] getAsRow() {
        Object[] columnValues = new Object[5];
        columnValues[0] = getPeopleinterestId();
        columnValues[1] = getPurchaseDate();
        columnValues[2] = getPeopleId();
        columnValues[3] = getProductId();
        columnValues[4] = getProspectingLevel();
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
            setPeopleinterestId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setPeopleinterestId(null);
        }
        setPurchaseDate(toDate(flds[1]));
        try {
            setPeopleId(Integer.parseInt(flds[2]));
        } catch(NumberFormatException ne) {
            setPeopleId(null);
        }
        try {
            setProductId(Integer.parseInt(flds[3]));
        } catch(NumberFormatException ne) {
            setProductId(null);
        }
        setProspectingLevel(flds[4]);
    }
}
