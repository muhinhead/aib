// Generated by com.xlend.orm.tools.dbgen.DbGenerator.class at Thu May 30 09:20:42 EEST 2013
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class Peopleproduct extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer peopleproductId = null;
    private Date purchaseDate = null;
    private Integer peopleId = null;
    private Integer productId = null;

    public Peopleproduct(Connection connection) {
        super(connection, "peopleproduct", "peopleproduct_id");
        setColumnNames(new String[]{"peopleproduct_id", "purchase_date", "people_id", "product_id"});
    }

    public Peopleproduct(Connection connection, Integer peopleproductId, Date purchaseDate, Integer peopleId, Integer productId) {
        super(connection, "peopleproduct", "peopleproduct_id");
        setNew(peopleproductId.intValue() <= 0);
//        if (peopleproductId.intValue() != 0) {
            this.peopleproductId = peopleproductId;
//        }
        this.purchaseDate = purchaseDate;
        this.peopleId = peopleId;
        this.productId = productId;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        Peopleproduct peopleproduct = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT peopleproduct_id,purchase_date,people_id,product_id FROM peopleproduct WHERE peopleproduct_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                peopleproduct = new Peopleproduct(getConnection());
                peopleproduct.setPeopleproductId(new Integer(rs.getInt(1)));
                peopleproduct.setPurchaseDate(rs.getDate(2));
                peopleproduct.setPeopleId(new Integer(rs.getInt(3)));
                peopleproduct.setProductId(new Integer(rs.getInt(4)));
                peopleproduct.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return peopleproduct;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO peopleproduct ("+(getPeopleproductId().intValue()!=0?"peopleproduct_id,":"")+"purchase_date,people_id,product_id) values("+(getPeopleproductId().intValue()!=0?"?,":"")+"?,?,?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getPeopleproductId().intValue()!=0) {
                 ps.setObject(++n, getPeopleproductId());
             }
             ps.setObject(++n, getPurchaseDate());
             ps.setObject(++n, getPeopleId());
             ps.setObject(++n, getProductId());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getPeopleproductId().intValue()==0) {
             stmt = "SELECT max(peopleproduct_id) FROM peopleproduct";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setPeopleproductId(new Integer(rs.getInt(1)));
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
                    "UPDATE peopleproduct " +
                    "SET purchase_date = ?, people_id = ?, product_id = ?" + 
                    " WHERE peopleproduct_id = " + getPeopleproductId();
            try {
                ps = getConnection().prepareStatement(stmt);
                ps.setObject(1, getPurchaseDate());
                ps.setObject(2, getPeopleId());
                ps.setObject(3, getProductId());
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
                "DELETE FROM peopleproduct " +
                "WHERE peopleproduct_id = " + getPeopleproductId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setPeopleproductId(new Integer(-getPeopleproductId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getPeopleproductId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT peopleproduct_id,purchase_date,people_id,product_id FROM peopleproduct " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new Peopleproduct(con,new Integer(rs.getInt(1)),rs.getDate(2),new Integer(rs.getInt(3)),new Integer(rs.getInt(4))));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        Peopleproduct[] objects = new Peopleproduct[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            Peopleproduct peopleproduct = (Peopleproduct) lst.get(i);
            objects[i] = peopleproduct;
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
        String stmt = "SELECT peopleproduct_id FROM peopleproduct " +
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
    //    return getPeopleproductId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return peopleproductId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setPeopleproductId(id);
        setNew(prevIsNew);
    }

    public Integer getPeopleproductId() {
        return peopleproductId;
    }

    public void setPeopleproductId(Integer peopleproductId) throws ForeignKeyViolationException {
        setWasChanged(this.peopleproductId != null && this.peopleproductId != peopleproductId);
        this.peopleproductId = peopleproductId;
        setNew(peopleproductId.intValue() == 0);
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
            throw new ForeignKeyViolationException("Can't set people_id, foreign key violation: peopleproduct_people_fk");
        }
        setWasChanged(this.peopleId != null && !this.peopleId.equals(peopleId));
        this.peopleId = peopleId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) throws SQLException, ForeignKeyViolationException {
        if (productId!=null && !Product.exists(getConnection(),"product_id = " + productId)) {
            throw new ForeignKeyViolationException("Can't set product_id, foreign key violation: peopleproduct_product_fk");
        }
        setWasChanged(this.productId != null && !this.productId.equals(productId));
        this.productId = productId;
    }
    public Object[] getAsRow() {
        Object[] columnValues = new Object[4];
        columnValues[0] = getPeopleproductId();
        columnValues[1] = getPurchaseDate();
        columnValues[2] = getPeopleId();
        columnValues[3] = getProductId();
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
            setPeopleproductId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setPeopleproductId(null);
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
    }
}
