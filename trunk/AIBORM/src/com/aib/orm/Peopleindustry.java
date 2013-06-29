// Generated by com.xlend.orm.tools.dbgen.DbGenerator.class at Fri Jun 28 16:12:16 EEST 2013
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class Peopleindustry extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer peopleindustryId = null;
    private Integer peopleId = null;
    private Integer industryId = null;

    public Peopleindustry(Connection connection) {
        super(connection, "peopleindustry", "peopleindustry_id");
        setColumnNames(new String[]{"peopleindustry_id", "people_id", "industry_id"});
    }

    public Peopleindustry(Connection connection, Integer peopleindustryId, Integer peopleId, Integer industryId) {
        super(connection, "peopleindustry", "peopleindustry_id");
        setNew(peopleindustryId.intValue() <= 0);
//        if (peopleindustryId.intValue() != 0) {
            this.peopleindustryId = peopleindustryId;
//        }
        this.peopleId = peopleId;
        this.industryId = industryId;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        Peopleindustry peopleindustry = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT peopleindustry_id,people_id,industry_id FROM peopleindustry WHERE peopleindustry_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                peopleindustry = new Peopleindustry(getConnection());
                peopleindustry.setPeopleindustryId(new Integer(rs.getInt(1)));
                peopleindustry.setPeopleId(new Integer(rs.getInt(2)));
                peopleindustry.setIndustryId(new Integer(rs.getInt(3)));
                peopleindustry.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return peopleindustry;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO peopleindustry ("+(getPeopleindustryId().intValue()!=0?"peopleindustry_id,":"")+"people_id,industry_id) values("+(getPeopleindustryId().intValue()!=0?"?,":"")+"?,?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getPeopleindustryId().intValue()!=0) {
                 ps.setObject(++n, getPeopleindustryId());
             }
             ps.setObject(++n, getPeopleId());
             ps.setObject(++n, getIndustryId());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getPeopleindustryId().intValue()==0) {
             stmt = "SELECT max(peopleindustry_id) FROM peopleindustry";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setPeopleindustryId(new Integer(rs.getInt(1)));
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
                    "UPDATE peopleindustry " +
                    "SET people_id = ?, industry_id = ?" + 
                    " WHERE peopleindustry_id = " + getPeopleindustryId();
            try {
                ps = getConnection().prepareStatement(stmt);
                ps.setObject(1, getPeopleId());
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
                "DELETE FROM peopleindustry " +
                "WHERE peopleindustry_id = " + getPeopleindustryId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setPeopleindustryId(new Integer(-getPeopleindustryId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getPeopleindustryId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT peopleindustry_id,people_id,industry_id FROM peopleindustry " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new Peopleindustry(con,new Integer(rs.getInt(1)),new Integer(rs.getInt(2)),new Integer(rs.getInt(3))));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        Peopleindustry[] objects = new Peopleindustry[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            Peopleindustry peopleindustry = (Peopleindustry) lst.get(i);
            objects[i] = peopleindustry;
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
        String stmt = "SELECT peopleindustry_id FROM peopleindustry " +
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
    //    return getPeopleindustryId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return peopleindustryId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setPeopleindustryId(id);
        setNew(prevIsNew);
    }

    public Integer getPeopleindustryId() {
        return peopleindustryId;
    }

    public void setPeopleindustryId(Integer peopleindustryId) throws ForeignKeyViolationException {
        setWasChanged(this.peopleindustryId != null && this.peopleindustryId != peopleindustryId);
        this.peopleindustryId = peopleindustryId;
        setNew(peopleindustryId.intValue() == 0);
    }

    public Integer getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(Integer peopleId) throws SQLException, ForeignKeyViolationException {
        if (peopleId!=null && !People.exists(getConnection(),"people_id = " + peopleId)) {
            throw new ForeignKeyViolationException("Can't set people_id, foreign key violation: peopleindustry_people_fk");
        }
        setWasChanged(this.peopleId != null && !this.peopleId.equals(peopleId));
        this.peopleId = peopleId;
    }

    public Integer getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Integer industryId) throws SQLException, ForeignKeyViolationException {
        if (industryId!=null && !Industry.exists(getConnection(),"industry_id = " + industryId)) {
            throw new ForeignKeyViolationException("Can't set industry_id, foreign key violation: peopleindustry_industry_fk");
        }
        setWasChanged(this.industryId != null && !this.industryId.equals(industryId));
        this.industryId = industryId;
    }
    public Object[] getAsRow() {
        Object[] columnValues = new Object[3];
        columnValues[0] = getPeopleindustryId();
        columnValues[1] = getPeopleId();
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
            setPeopleindustryId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setPeopleindustryId(null);
        }
        try {
            setPeopleId(Integer.parseInt(flds[1]));
        } catch(NumberFormatException ne) {
            setPeopleId(null);
        }
        try {
            setIndustryId(Integer.parseInt(flds[2]));
        } catch(NumberFormatException ne) {
            setIndustryId(null);
        }
    }
}
