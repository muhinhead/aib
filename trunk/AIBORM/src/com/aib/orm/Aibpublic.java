// Generated by com.xlend.orm.tools.dbgen.DbGenerator.class at Tue Jun 04 09:31:12 EEST 2013
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class Aibpublic extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer aibpublicId = null;
    private String publication = null;
    private Date pubDate = null;

    public Aibpublic(Connection connection) {
        super(connection, "aibpublic", "aibpublic_id");
        setColumnNames(new String[]{"aibpublic_id", "publication", "pub_date"});
    }

    public Aibpublic(Connection connection, Integer aibpublicId, String publication, Date pubDate) {
        super(connection, "aibpublic", "aibpublic_id");
        setNew(aibpublicId.intValue() <= 0);
//        if (aibpublicId.intValue() != 0) {
            this.aibpublicId = aibpublicId;
//        }
        this.publication = publication;
        this.pubDate = pubDate;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        Aibpublic aibpublic = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT aibpublic_id,publication,pub_date FROM aibpublic WHERE aibpublic_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                aibpublic = new Aibpublic(getConnection());
                aibpublic.setAibpublicId(new Integer(rs.getInt(1)));
                aibpublic.setPublication(rs.getString(2));
                aibpublic.setPubDate(rs.getDate(3));
                aibpublic.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return aibpublic;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO aibpublic ("+(getAibpublicId().intValue()!=0?"aibpublic_id,":"")+"publication,pub_date) values("+(getAibpublicId().intValue()!=0?"?,":"")+"?,?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getAibpublicId().intValue()!=0) {
                 ps.setObject(++n, getAibpublicId());
             }
             ps.setObject(++n, getPublication());
             ps.setObject(++n, getPubDate());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getAibpublicId().intValue()==0) {
             stmt = "SELECT max(aibpublic_id) FROM aibpublic";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setAibpublicId(new Integer(rs.getInt(1)));
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
                    "UPDATE aibpublic " +
                    "SET publication = ?, pub_date = ?" + 
                    " WHERE aibpublic_id = " + getAibpublicId();
            try {
                ps = getConnection().prepareStatement(stmt);
                ps.setObject(1, getPublication());
                ps.setObject(2, getPubDate());
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
        {// delete cascade from comppublic
            Comppublic[] records = (Comppublic[])Comppublic.load(getConnection(),"aibpublic_id = " + getAibpublicId(),null);
            for (int i = 0; i<records.length; i++) {
                Comppublic comppublic = records[i];
                comppublic.delete();
            }
        }
        PreparedStatement ps = null;
        String stmt =
                "DELETE FROM aibpublic " +
                "WHERE aibpublic_id = " + getAibpublicId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setAibpublicId(new Integer(-getAibpublicId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getAibpublicId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT aibpublic_id,publication,pub_date FROM aibpublic " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new Aibpublic(con,new Integer(rs.getInt(1)),rs.getString(2),rs.getDate(3)));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        Aibpublic[] objects = new Aibpublic[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            Aibpublic aibpublic = (Aibpublic) lst.get(i);
            objects[i] = aibpublic;
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
        String stmt = "SELECT aibpublic_id FROM aibpublic " +
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
    //    return getAibpublicId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return aibpublicId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setAibpublicId(id);
        setNew(prevIsNew);
    }

    public Integer getAibpublicId() {
        return aibpublicId;
    }

    public void setAibpublicId(Integer aibpublicId) throws ForeignKeyViolationException {
        setWasChanged(this.aibpublicId != null && this.aibpublicId != aibpublicId);
        this.aibpublicId = aibpublicId;
        setNew(aibpublicId.intValue() == 0);
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.publication != null && !this.publication.equals(publication));
        this.publication = publication;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.pubDate != null && !this.pubDate.equals(pubDate));
        this.pubDate = pubDate;
    }
    public Object[] getAsRow() {
        Object[] columnValues = new Object[3];
        columnValues[0] = getAibpublicId();
        columnValues[1] = getPublication();
        columnValues[2] = getPubDate();
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
            setAibpublicId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setAibpublicId(null);
        }
        setPublication(flds[1]);
        setPubDate(toDate(flds[2]));
    }
}
