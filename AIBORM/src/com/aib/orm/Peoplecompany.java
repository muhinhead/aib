// Generated by com.aib.orm.tools.dbgen.DbGenerator.class at Sat Apr 05 12:43:59 FET 2014
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class Peoplecompany extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer peoplecompanyId = null;
    private Integer peopleId = null;
    private Integer companyId = null;

    public Peoplecompany(Connection connection) {
        super(connection, "peoplecompany", "peoplecompany_id");
        setColumnNames(new String[]{"peoplecompany_id", "people_id", "company_id"});
    }

    public Peoplecompany(Connection connection, Integer peoplecompanyId, Integer peopleId, Integer companyId) {
        super(connection, "peoplecompany", "peoplecompany_id");
        setNew(peoplecompanyId.intValue() <= 0);
//        if (peoplecompanyId.intValue() != 0) {
            this.peoplecompanyId = peoplecompanyId;
//        }
        this.peopleId = peopleId;
        this.companyId = companyId;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        Peoplecompany peoplecompany = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT peoplecompany_id,people_id,company_id FROM peoplecompany WHERE peoplecompany_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                peoplecompany = new Peoplecompany(getConnection());
                peoplecompany.setPeoplecompanyId(new Integer(rs.getInt(1)));
                peoplecompany.setPeopleId(new Integer(rs.getInt(2)));
                peoplecompany.setCompanyId(new Integer(rs.getInt(3)));
                peoplecompany.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return peoplecompany;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO peoplecompany ("+(getPeoplecompanyId().intValue()!=0?"peoplecompany_id,":"")+"people_id,company_id) values("+(getPeoplecompanyId().intValue()!=0?"?,":"")+"?,?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getPeoplecompanyId().intValue()!=0) {
                 ps.setObject(++n, getPeoplecompanyId());
             }
             ps.setObject(++n, getPeopleId());
             ps.setObject(++n, getCompanyId());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getPeoplecompanyId().intValue()==0) {
             stmt = "SELECT max(peoplecompany_id) FROM peoplecompany";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setPeoplecompanyId(new Integer(rs.getInt(1)));
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
                    "UPDATE peoplecompany " +
                    "SET people_id = ?, company_id = ?" + 
                    " WHERE peoplecompany_id = " + getPeoplecompanyId();
            try {
                ps = getConnection().prepareStatement(stmt);
                ps.setObject(1, getPeopleId());
                ps.setObject(2, getCompanyId());
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
                "DELETE FROM peoplecompany " +
                "WHERE peoplecompany_id = " + getPeoplecompanyId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setPeoplecompanyId(new Integer(-getPeoplecompanyId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getPeoplecompanyId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT peoplecompany_id,people_id,company_id FROM peoplecompany " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new Peoplecompany(con,new Integer(rs.getInt(1)),new Integer(rs.getInt(2)),new Integer(rs.getInt(3))));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        Peoplecompany[] objects = new Peoplecompany[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            Peoplecompany peoplecompany = (Peoplecompany) lst.get(i);
            objects[i] = peoplecompany;
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
        String stmt = "SELECT peoplecompany_id FROM peoplecompany " +
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
    //    return getPeoplecompanyId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return peoplecompanyId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setPeoplecompanyId(id);
        setNew(prevIsNew);
    }

    public Integer getPeoplecompanyId() {
        return peoplecompanyId;
    }

    public void setPeoplecompanyId(Integer peoplecompanyId) throws ForeignKeyViolationException {
        setWasChanged(this.peoplecompanyId != null && this.peoplecompanyId != peoplecompanyId);
        this.peoplecompanyId = peoplecompanyId;
        setNew(peoplecompanyId.intValue() == 0);
    }

    public Integer getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(Integer peopleId) throws SQLException, ForeignKeyViolationException {
        if (peopleId!=null && !People.exists(getConnection(),"people_id = " + peopleId)) {
            throw new ForeignKeyViolationException("Can't set people_id, foreign key violation: peoplecompany_people_fk");
        }
        setWasChanged(this.peopleId != null && !this.peopleId.equals(peopleId));
        this.peopleId = peopleId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) throws SQLException, ForeignKeyViolationException {
        if (companyId!=null && !Company.exists(getConnection(),"company_id = " + companyId)) {
            throw new ForeignKeyViolationException("Can't set company_id, foreign key violation: peoplecompany_company_fk");
        }
        setWasChanged(this.companyId != null && !this.companyId.equals(companyId));
        this.companyId = companyId;
    }
    public Object[] getAsRow() {
        Object[] columnValues = new Object[3];
        columnValues[0] = getPeoplecompanyId();
        columnValues[1] = getPeopleId();
        columnValues[2] = getCompanyId();
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
            setPeoplecompanyId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setPeoplecompanyId(null);
        }
        try {
            setPeopleId(Integer.parseInt(flds[1]));
        } catch(NumberFormatException ne) {
            setPeopleId(null);
        }
        try {
            setCompanyId(Integer.parseInt(flds[2]));
        } catch(NumberFormatException ne) {
            setCompanyId(null);
        }
    }
}
