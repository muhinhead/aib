// Generated by com.xlend.orm.tools.dbgen.DbGenerator.class at Sat May 25 08:57:48 EEST 2013
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class Departmenthistory extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer departmenthistoryId = null;
    private Integer peopleId = null;
    private String department = null;
    private Timestamp untilDate = null;

    public Departmenthistory(Connection connection) {
        super(connection, "departmenthistory", "departmenthistory_id");
        setColumnNames(new String[]{"departmenthistory_id", "people_id", "department", "until_date"});
    }

    public Departmenthistory(Connection connection, Integer departmenthistoryId, Integer peopleId, String department, Timestamp untilDate) {
        super(connection, "departmenthistory", "departmenthistory_id");
        setNew(departmenthistoryId.intValue() <= 0);
//        if (departmenthistoryId.intValue() != 0) {
            this.departmenthistoryId = departmenthistoryId;
//        }
        this.peopleId = peopleId;
        this.department = department;
        this.untilDate = untilDate;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        Departmenthistory departmenthistory = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT departmenthistory_id,people_id,department,until_date FROM departmenthistory WHERE departmenthistory_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                departmenthistory = new Departmenthistory(getConnection());
                departmenthistory.setDepartmenthistoryId(new Integer(rs.getInt(1)));
                departmenthistory.setPeopleId(new Integer(rs.getInt(2)));
                departmenthistory.setDepartment(rs.getString(3));
                departmenthistory.setUntilDate(rs.getTimestamp(4));
                departmenthistory.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return departmenthistory;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO departmenthistory ("+(getDepartmenthistoryId().intValue()!=0?"departmenthistory_id,":"")+"people_id,department,until_date) values("+(getDepartmenthistoryId().intValue()!=0?"?,":"")+"?,?,?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getDepartmenthistoryId().intValue()!=0) {
                 ps.setObject(++n, getDepartmenthistoryId());
             }
             ps.setObject(++n, getPeopleId());
             ps.setObject(++n, getDepartment());
             ps.setObject(++n, getUntilDate());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getDepartmenthistoryId().intValue()==0) {
             stmt = "SELECT max(departmenthistory_id) FROM departmenthistory";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setDepartmenthistoryId(new Integer(rs.getInt(1)));
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
                    "UPDATE departmenthistory " +
                    "SET people_id = ?, department = ?, until_date = ?" + 
                    " WHERE departmenthistory_id = " + getDepartmenthistoryId();
            try {
                ps = getConnection().prepareStatement(stmt);
                ps.setObject(1, getPeopleId());
                ps.setObject(2, getDepartment());
                ps.setObject(3, getUntilDate());
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
                "DELETE FROM departmenthistory " +
                "WHERE departmenthistory_id = " + getDepartmenthistoryId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setDepartmenthistoryId(new Integer(-getDepartmenthistoryId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getDepartmenthistoryId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT departmenthistory_id,people_id,department,until_date FROM departmenthistory " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new Departmenthistory(con,new Integer(rs.getInt(1)),new Integer(rs.getInt(2)),rs.getString(3),rs.getTimestamp(4)));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        Departmenthistory[] objects = new Departmenthistory[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            Departmenthistory departmenthistory = (Departmenthistory) lst.get(i);
            objects[i] = departmenthistory;
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
        String stmt = "SELECT departmenthistory_id FROM departmenthistory " +
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
    //    return getDepartmenthistoryId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return departmenthistoryId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setDepartmenthistoryId(id);
        setNew(prevIsNew);
    }

    public Integer getDepartmenthistoryId() {
        return departmenthistoryId;
    }

    public void setDepartmenthistoryId(Integer departmenthistoryId) throws ForeignKeyViolationException {
        setWasChanged(this.departmenthistoryId != null && this.departmenthistoryId != departmenthistoryId);
        this.departmenthistoryId = departmenthistoryId;
        setNew(departmenthistoryId.intValue() == 0);
    }

    public Integer getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(Integer peopleId) throws SQLException, ForeignKeyViolationException {
        if (peopleId!=null && !People.exists(getConnection(),"people_id = " + peopleId)) {
            throw new ForeignKeyViolationException("Can't set people_id, foreign key violation: departmenthistory_people_fk");
        }
        setWasChanged(this.peopleId != null && !this.peopleId.equals(peopleId));
        this.peopleId = peopleId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.department != null && !this.department.equals(department));
        this.department = department;
    }

    public Timestamp getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(Timestamp untilDate) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.untilDate != null && !this.untilDate.equals(untilDate));
        this.untilDate = untilDate;
    }
    public Object[] getAsRow() {
        Object[] columnValues = new Object[4];
        columnValues[0] = getDepartmenthistoryId();
        columnValues[1] = getPeopleId();
        columnValues[2] = getDepartment();
        columnValues[3] = getUntilDate();
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
            setDepartmenthistoryId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setDepartmenthistoryId(null);
        }
        try {
            setPeopleId(Integer.parseInt(flds[1]));
        } catch(NumberFormatException ne) {
            setPeopleId(null);
        }
        setDepartment(flds[2]);
        setUntilDate(toTimeStamp(flds[3]));
    }
}
