// Generated by com.xlend.orm.tools.dbgen.DbGenerator.class at Mon May 06 19:05:04 EEST 2013
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class People_aibaward extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer peopleAibawardId = null;
    private Integer peopleId = null;
    private Integer aibawardId = null;

    public People_aibaward(Connection connection) {
        super(connection, "people_aibaward", "people_aibaward_id");
        setColumnNames(new String[]{"people_aibaward_id", "people_id", "aibaward_id"});
    }

    public People_aibaward(Connection connection, Integer peopleAibawardId, Integer peopleId, Integer aibawardId) {
        super(connection, "people_aibaward", "people_aibaward_id");
        setNew(peopleAibawardId.intValue() <= 0);
//        if (peopleAibawardId.intValue() != 0) {
            this.peopleAibawardId = peopleAibawardId;
//        }
        this.peopleId = peopleId;
        this.aibawardId = aibawardId;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        People_aibaward people_aibaward = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT people_aibaward_id,people_id,aibaward_id FROM people_aibaward WHERE people_aibaward_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                people_aibaward = new People_aibaward(getConnection());
                people_aibaward.setPeopleAibawardId(new Integer(rs.getInt(1)));
                people_aibaward.setPeopleId(new Integer(rs.getInt(2)));
                people_aibaward.setAibawardId(new Integer(rs.getInt(3)));
                people_aibaward.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return people_aibaward;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO people_aibaward ("+(getPeopleAibawardId().intValue()!=0?"people_aibaward_id,":"")+"people_id,aibaward_id) values("+(getPeopleAibawardId().intValue()!=0?"?,":"")+"?,?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getPeopleAibawardId().intValue()!=0) {
                 ps.setObject(++n, getPeopleAibawardId());
             }
             ps.setObject(++n, getPeopleId());
             ps.setObject(++n, getAibawardId());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getPeopleAibawardId().intValue()==0) {
             stmt = "SELECT max(people_aibaward_id) FROM people_aibaward";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setPeopleAibawardId(new Integer(rs.getInt(1)));
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
                    "UPDATE people_aibaward " +
                    "SET people_id = ?, aibaward_id = ?" + 
                    " WHERE people_aibaward_id = " + getPeopleAibawardId();
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
                "DELETE FROM people_aibaward " +
                "WHERE people_aibaward_id = " + getPeopleAibawardId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setPeopleAibawardId(new Integer(-getPeopleAibawardId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getPeopleAibawardId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT people_aibaward_id,people_id,aibaward_id FROM people_aibaward " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new People_aibaward(con,new Integer(rs.getInt(1)),new Integer(rs.getInt(2)),new Integer(rs.getInt(3))));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        People_aibaward[] objects = new People_aibaward[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            People_aibaward people_aibaward = (People_aibaward) lst.get(i);
            objects[i] = people_aibaward;
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
        String stmt = "SELECT people_aibaward_id FROM people_aibaward " +
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
    //    return getPeopleAibawardId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return peopleAibawardId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setPeopleAibawardId(id);
        setNew(prevIsNew);
    }

    public Integer getPeopleAibawardId() {
        return peopleAibawardId;
    }

    public void setPeopleAibawardId(Integer peopleAibawardId) throws ForeignKeyViolationException {
        setWasChanged(this.peopleAibawardId != null && this.peopleAibawardId != peopleAibawardId);
        this.peopleAibawardId = peopleAibawardId;
        setNew(peopleAibawardId.intValue() == 0);
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
        columnValues[0] = getPeopleAibawardId();
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
            setPeopleAibawardId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setPeopleAibawardId(null);
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