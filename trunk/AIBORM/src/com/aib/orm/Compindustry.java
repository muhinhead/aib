// Generated by com.xlend.orm.tools.dbgen.DbGenerator.class at Tue May 21 14:01:47 EEST 2013
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class Compindustry extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer compindustryId = null;
    private Integer companyId = null;
    private Integer industryId = null;

    public Compindustry(Connection connection) {
        super(connection, "compindustry", "compindustry_id");
        setColumnNames(new String[]{"compindustry_id", "company_id", "industry_id"});
    }

    public Compindustry(Connection connection, Integer compindustryId, Integer companyId, Integer industryId) {
        super(connection, "compindustry", "compindustry_id");
        setNew(compindustryId.intValue() <= 0);
//        if (compindustryId.intValue() != 0) {
            this.compindustryId = compindustryId;
//        }
        this.companyId = companyId;
        this.industryId = industryId;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        Compindustry compindustry = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT compindustry_id,company_id,industry_id FROM compindustry WHERE compindustry_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                compindustry = new Compindustry(getConnection());
                compindustry.setCompindustryId(new Integer(rs.getInt(1)));
                compindustry.setCompanyId(new Integer(rs.getInt(2)));
                compindustry.setIndustryId(new Integer(rs.getInt(3)));
                compindustry.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return compindustry;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO compindustry ("+(getCompindustryId().intValue()!=0?"compindustry_id,":"")+"company_id,industry_id) values("+(getCompindustryId().intValue()!=0?"?,":"")+"?,?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getCompindustryId().intValue()!=0) {
                 ps.setObject(++n, getCompindustryId());
             }
             ps.setObject(++n, getCompanyId());
             ps.setObject(++n, getIndustryId());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getCompindustryId().intValue()==0) {
             stmt = "SELECT max(compindustry_id) FROM compindustry";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setCompindustryId(new Integer(rs.getInt(1)));
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
                    "UPDATE compindustry " +
                    "SET company_id = ?, industry_id = ?" + 
                    " WHERE compindustry_id = " + getCompindustryId();
            try {
                ps = getConnection().prepareStatement(stmt);
                ps.setObject(1, getCompanyId());
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
                "DELETE FROM compindustry " +
                "WHERE compindustry_id = " + getCompindustryId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setCompindustryId(new Integer(-getCompindustryId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getCompindustryId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT compindustry_id,company_id,industry_id FROM compindustry " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new Compindustry(con,new Integer(rs.getInt(1)),new Integer(rs.getInt(2)),new Integer(rs.getInt(3))));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        Compindustry[] objects = new Compindustry[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            Compindustry compindustry = (Compindustry) lst.get(i);
            objects[i] = compindustry;
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
        String stmt = "SELECT compindustry_id FROM compindustry " +
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
    //    return getCompindustryId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return compindustryId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setCompindustryId(id);
        setNew(prevIsNew);
    }

    public Integer getCompindustryId() {
        return compindustryId;
    }

    public void setCompindustryId(Integer compindustryId) throws ForeignKeyViolationException {
        setWasChanged(this.compindustryId != null && this.compindustryId != compindustryId);
        this.compindustryId = compindustryId;
        setNew(compindustryId.intValue() == 0);
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) throws SQLException, ForeignKeyViolationException {
        if (companyId!=null && !Company.exists(getConnection(),"company_id = " + companyId)) {
            throw new ForeignKeyViolationException("Can't set company_id, foreign key violation: compindustry_company_fk");
        }
        setWasChanged(this.companyId != null && !this.companyId.equals(companyId));
        this.companyId = companyId;
    }

    public Integer getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Integer industryId) throws SQLException, ForeignKeyViolationException {
        if (industryId!=null && !Industry.exists(getConnection(),"industry_id = " + industryId)) {
            throw new ForeignKeyViolationException("Can't set industry_id, foreign key violation: compindustry_industry_fk");
        }
        setWasChanged(this.industryId != null && !this.industryId.equals(industryId));
        this.industryId = industryId;
    }
    public Object[] getAsRow() {
        Object[] columnValues = new Object[3];
        columnValues[0] = getCompindustryId();
        columnValues[1] = getCompanyId();
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
            setCompindustryId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setCompindustryId(null);
        }
        try {
            setCompanyId(Integer.parseInt(flds[1]));
        } catch(NumberFormatException ne) {
            setCompanyId(null);
        }
        try {
            setIndustryId(Integer.parseInt(flds[2]));
        } catch(NumberFormatException ne) {
            setIndustryId(null);
        }
    }
}
