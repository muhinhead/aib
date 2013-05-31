// Generated by com.xlend.orm.tools.dbgen.DbGenerator.class at Thu May 30 09:20:42 EEST 2013
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class Aibaward extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer aibawardId = null;
    private String award = null;
    private Date awardDate = null;

    public Aibaward(Connection connection) {
        super(connection, "aibaward", "aibaward_id");
        setColumnNames(new String[]{"aibaward_id", "award", "award_date"});
    }

    public Aibaward(Connection connection, Integer aibawardId, String award, Date awardDate) {
        super(connection, "aibaward", "aibaward_id");
        setNew(aibawardId.intValue() <= 0);
//        if (aibawardId.intValue() != 0) {
            this.aibawardId = aibawardId;
//        }
        this.award = award;
        this.awardDate = awardDate;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        Aibaward aibaward = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT aibaward_id,award,award_date FROM aibaward WHERE aibaward_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                aibaward = new Aibaward(getConnection());
                aibaward.setAibawardId(new Integer(rs.getInt(1)));
                aibaward.setAward(rs.getString(2));
                aibaward.setAwardDate(rs.getDate(3));
                aibaward.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return aibaward;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO aibaward ("+(getAibawardId().intValue()!=0?"aibaward_id,":"")+"award,award_date) values("+(getAibawardId().intValue()!=0?"?,":"")+"?,?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getAibawardId().intValue()!=0) {
                 ps.setObject(++n, getAibawardId());
             }
             ps.setObject(++n, getAward());
             ps.setObject(++n, getAwardDate());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getAibawardId().intValue()==0) {
             stmt = "SELECT max(aibaward_id) FROM aibaward";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setAibawardId(new Integer(rs.getInt(1)));
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
                    "UPDATE aibaward " +
                    "SET award = ?, award_date = ?" + 
                    " WHERE aibaward_id = " + getAibawardId();
            try {
                ps = getConnection().prepareStatement(stmt);
                ps.setObject(1, getAward());
                ps.setObject(2, getAwardDate());
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
        {// delete cascade from peopleaward
            Peopleaward[] records = (Peopleaward[])Peopleaward.load(getConnection(),"aibaward_id = " + getAibawardId(),null);
            for (int i = 0; i<records.length; i++) {
                Peopleaward peopleaward = records[i];
                peopleaward.delete();
            }
        }
        PreparedStatement ps = null;
        String stmt =
                "DELETE FROM aibaward " +
                "WHERE aibaward_id = " + getAibawardId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setAibawardId(new Integer(-getAibawardId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getAibawardId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT aibaward_id,award,award_date FROM aibaward " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new Aibaward(con,new Integer(rs.getInt(1)),rs.getString(2),rs.getDate(3)));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        Aibaward[] objects = new Aibaward[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            Aibaward aibaward = (Aibaward) lst.get(i);
            objects[i] = aibaward;
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
        String stmt = "SELECT aibaward_id FROM aibaward " +
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
    //    return getAibawardId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return aibawardId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setAibawardId(id);
        setNew(prevIsNew);
    }

    public Integer getAibawardId() {
        return aibawardId;
    }

    public void setAibawardId(Integer aibawardId) throws ForeignKeyViolationException {
        setWasChanged(this.aibawardId != null && this.aibawardId != aibawardId);
        this.aibawardId = aibawardId;
        setNew(aibawardId.intValue() == 0);
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.award != null && !this.award.equals(award));
        this.award = award;
    }

    public Date getAwardDate() {
        return awardDate;
    }

    public void setAwardDate(Date awardDate) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.awardDate != null && !this.awardDate.equals(awardDate));
        this.awardDate = awardDate;
    }
    public Object[] getAsRow() {
        Object[] columnValues = new Object[3];
        columnValues[0] = getAibawardId();
        columnValues[1] = getAward();
        columnValues[2] = getAwardDate();
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
            setAibawardId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setAibawardId(null);
        }
        setAward(flds[1]);
        setAwardDate(toDate(flds[2]));
    }
}
