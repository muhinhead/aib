// Generated by com.aib.orm.tools.dbgen.DbGenerator.class at Sat Apr 05 12:44:00 FET 2014
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class Peopleaward extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer peopleawardId = null;
    private Integer peopleId = null;
    private Integer aibawardId = null;

    public Peopleaward(Connection connection) {
        super(connection, "peopleaward", "peopleaward_id");
        setColumnNames(new String[]{"peopleaward_id", "people_id", "aibaward_id"});
    }

    public Peopleaward(Connection connection, Integer peopleawardId, Integer peopleId, Integer aibawardId) {
        super(connection, "peopleaward", "peopleaward_id");
        setNew(peopleawardId.intValue() <= 0);
//        if (peopleawardId.intValue() != 0) {
            this.peopleawardId = peopleawardId;
//        }
        this.peopleId = peopleId;
        this.aibawardId = aibawardId;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        Peopleaward peopleaward = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT peopleaward_id,people_id,aibaward_id FROM peopleaward WHERE peopleaward_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                peopleaward = new Peopleaward(getConnection());
                peopleaward.setPeopleawardId(new Integer(rs.getInt(1)));
                peopleaward.setPeopleId(new Integer(rs.getInt(2)));
                peopleaward.setAibawardId(new Integer(rs.getInt(3)));
                peopleaward.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return peopleaward;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO peopleaward ("+(getPeopleawardId().intValue()!=0?"peopleaward_id,":"")+"people_id,aibaward_id) values("+(getPeopleawardId().intValue()!=0?"?,":"")+"?,?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getPeopleawardId().intValue()!=0) {
                 ps.setObject(++n, getPeopleawardId());
             }
             ps.setObject(++n, getPeopleId());
             ps.setObject(++n, getAibawardId());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getPeopleawardId().intValue()==0) {
             stmt = "SELECT max(peopleaward_id) FROM peopleaward";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setPeopleawardId(new Integer(rs.getInt(1)));
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
                    "UPDATE peopleaward " +
                    "SET people_id = ?, aibaward_id = ?" + 
                    " WHERE peopleaward_id = " + getPeopleawardId();
            try {
                ps = getConnection().prepareStatement(stmt);
                ps.setObject(1, getPeopleId());
                ps.setObject(2, getAibawardId());
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
                "DELETE FROM peopleaward " +
                "WHERE peopleaward_id = " + getPeopleawardId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setPeopleawardId(new Integer(-getPeopleawardId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getPeopleawardId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT peopleaward_id,people_id,aibaward_id FROM peopleaward " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new Peopleaward(con,new Integer(rs.getInt(1)),new Integer(rs.getInt(2)),new Integer(rs.getInt(3))));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        Peopleaward[] objects = new Peopleaward[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            Peopleaward peopleaward = (Peopleaward) lst.get(i);
            objects[i] = peopleaward;
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
        String stmt = "SELECT peopleaward_id FROM peopleaward " +
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
    //    return getPeopleawardId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return peopleawardId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setPeopleawardId(id);
        setNew(prevIsNew);
    }

    public Integer getPeopleawardId() {
        return peopleawardId;
    }

    public void setPeopleawardId(Integer peopleawardId) throws ForeignKeyViolationException {
        setWasChanged(this.peopleawardId != null && this.peopleawardId != peopleawardId);
        this.peopleawardId = peopleawardId;
        setNew(peopleawardId.intValue() == 0);
    }

    public Integer getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(Integer peopleId) throws SQLException, ForeignKeyViolationException {
        if (peopleId!=null && !People.exists(getConnection(),"people_id = " + peopleId)) {
            throw new ForeignKeyViolationException("Can't set people_id, foreign key violation: peopleaib_people_fk");
        }
        setWasChanged(this.peopleId != null && !this.peopleId.equals(peopleId));
        this.peopleId = peopleId;
    }

    public Integer getAibawardId() {
        return aibawardId;
    }

    public void setAibawardId(Integer aibawardId) throws SQLException, ForeignKeyViolationException {
        if (aibawardId!=null && !Aibaward.exists(getConnection(),"aibaward_id = " + aibawardId)) {
            throw new ForeignKeyViolationException("Can't set aibaward_id, foreign key violation: peopleaib_aibaward_fk");
        }
        setWasChanged(this.aibawardId != null && !this.aibawardId.equals(aibawardId));
        this.aibawardId = aibawardId;
    }
    public Object[] getAsRow() {
        Object[] columnValues = new Object[3];
        columnValues[0] = getPeopleawardId();
        columnValues[1] = getPeopleId();
        columnValues[2] = getAibawardId();
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
            setPeopleawardId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setPeopleawardId(null);
        }
        try {
            setPeopleId(Integer.parseInt(flds[1]));
        } catch(NumberFormatException ne) {
            setPeopleId(null);
        }
        try {
            setAibawardId(Integer.parseInt(flds[2]));
        } catch(NumberFormatException ne) {
            setAibawardId(null);
        }
    }
}
