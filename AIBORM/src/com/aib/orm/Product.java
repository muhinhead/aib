// Generated by com.xlend.orm.tools.dbgen.DbGenerator.class at Mon May 06 19:30:31 EEST 2013
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class Product extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer productId = null;
    private String descr = null;

    public Product(Connection connection) {
        super(connection, "product", "product_id");
        setColumnNames(new String[]{"product_id", "descr"});
    }

    public Product(Connection connection, Integer productId, String descr) {
        super(connection, "product", "product_id");
        setNew(productId.intValue() <= 0);
//        if (productId.intValue() != 0) {
            this.productId = productId;
//        }
        this.descr = descr;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        Product product = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT product_id,descr FROM product WHERE product_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                product = new Product(getConnection());
                product.setProductId(new Integer(rs.getInt(1)));
                product.setDescr(rs.getString(2));
                product.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return product;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO product ("+(getProductId().intValue()!=0?"product_id,":"")+"descr) values("+(getProductId().intValue()!=0?"?,":"")+"?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getProductId().intValue()!=0) {
                 ps.setObject(++n, getProductId());
             }
             ps.setObject(++n, getDescr());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getProductId().intValue()==0) {
             stmt = "SELECT max(product_id) FROM product";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setProductId(new Integer(rs.getInt(1)));
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
                    "UPDATE product " +
                    "SET descr = ?" + 
                    " WHERE product_id = " + getProductId();
            try {
                ps = getConnection().prepareStatement(stmt);
                ps.setObject(1, getDescr());
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
        {// delete cascade from peopleinterest
            Peopleinterest[] records = (Peopleinterest[])Peopleinterest.load(getConnection(),"product_id = " + getProductId(),null);
            for (int i = 0; i<records.length; i++) {
                Peopleinterest peopleinterest = records[i];
                peopleinterest.delete();
            }
        }
        {// delete cascade from peopleproduct
            Peopleproduct[] records = (Peopleproduct[])Peopleproduct.load(getConnection(),"product_id = " + getProductId(),null);
            for (int i = 0; i<records.length; i++) {
                Peopleproduct peopleproduct = records[i];
                peopleproduct.delete();
            }
        }
        PreparedStatement ps = null;
        String stmt =
                "DELETE FROM product " +
                "WHERE product_id = " + getProductId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setProductId(new Integer(-getProductId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getProductId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT product_id,descr FROM product " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new Product(con,new Integer(rs.getInt(1)),rs.getString(2)));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        Product[] objects = new Product[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            Product product = (Product) lst.get(i);
            objects[i] = product;
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
        String stmt = "SELECT product_id FROM product " +
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
    //    return getProductId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return productId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setProductId(id);
        setNew(prevIsNew);
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) throws ForeignKeyViolationException {
        setWasChanged(this.productId != null && this.productId != productId);
        this.productId = productId;
        setNew(productId.intValue() == 0);
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.descr != null && !this.descr.equals(descr));
        this.descr = descr;
    }
    public Object[] getAsRow() {
        Object[] columnValues = new Object[2];
        columnValues[0] = getProductId();
        columnValues[1] = getDescr();
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
            setProductId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setProductId(null);
        }
        setDescr(flds[1]);
    }
}
