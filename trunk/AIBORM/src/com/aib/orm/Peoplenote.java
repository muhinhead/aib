// Generated by com.xlend.orm.tools.dbgen.DbGenerator.class at Fri Jun 28 16:12:16 EEST 2013
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class Peoplenote extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer peoplenoteId = null;
    private Integer peopleId = null;
    private String comments = null;
    private Date noteDate = null;
    private Integer lasteditedBy = null;
    private Timestamp lasteditDate = null;

    public Peoplenote(Connection connection) {
        super(connection, "peoplenote", "peoplenote_id");
        setColumnNames(new String[]{"peoplenote_id", "people_id", "comments", "note_date", "lastedited_by", "lastedit_date"});
    }

    public Peoplenote(Connection connection, Integer peoplenoteId, Integer peopleId, String comments, Date noteDate, Integer lasteditedBy, Timestamp lasteditDate) {
        super(connection, "peoplenote", "peoplenote_id");
        setNew(peoplenoteId.intValue() <= 0);
//        if (peoplenoteId.intValue() != 0) {
            this.peoplenoteId = peoplenoteId;
//        }
        this.peopleId = peopleId;
        this.comments = comments;
        this.noteDate = noteDate;
        this.lasteditedBy = lasteditedBy;
        this.lasteditDate = lasteditDate;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        Peoplenote peoplenote = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT peoplenote_id,people_id,comments,note_date,lastedited_by,lastedit_date FROM peoplenote WHERE peoplenote_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                peoplenote = new Peoplenote(getConnection());
                peoplenote.setPeoplenoteId(new Integer(rs.getInt(1)));
                peoplenote.setPeopleId(new Integer(rs.getInt(2)));
                peoplenote.setComments(rs.getString(3));
                peoplenote.setNoteDate(rs.getDate(4));
                peoplenote.setLasteditedBy(new Integer(rs.getInt(5)));
                peoplenote.setLasteditDate(rs.getTimestamp(6));
                peoplenote.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return peoplenote;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO peoplenote ("+(getPeoplenoteId().intValue()!=0?"peoplenote_id,":"")+"people_id,comments,note_date,lastedited_by,lastedit_date) values("+(getPeoplenoteId().intValue()!=0?"?,":"")+"?,?,?,?,?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getPeoplenoteId().intValue()!=0) {
                 ps.setObject(++n, getPeoplenoteId());
             }
             ps.setObject(++n, getPeopleId());
             ps.setObject(++n, getComments());
             ps.setObject(++n, getNoteDate());
             ps.setObject(++n, getLasteditedBy());
             ps.setObject(++n, getLasteditDate());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getPeoplenoteId().intValue()==0) {
             stmt = "SELECT max(peoplenote_id) FROM peoplenote";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setPeoplenoteId(new Integer(rs.getInt(1)));
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
                    "UPDATE peoplenote " +
                    "SET people_id = ?, comments = ?, note_date = ?, lastedited_by = ?, lastedit_date = ?" + 
                    " WHERE peoplenote_id = " + getPeoplenoteId();
            try {
                ps = getConnection().prepareStatement(stmt);
                ps.setObject(1, getPeopleId());
                ps.setObject(2, getComments());
                ps.setObject(3, getNoteDate());
                ps.setObject(4, getLasteditedBy());
                ps.setObject(5, getLasteditDate());
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
                "DELETE FROM peoplenote " +
                "WHERE peoplenote_id = " + getPeoplenoteId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setPeoplenoteId(new Integer(-getPeoplenoteId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getPeoplenoteId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT peoplenote_id,people_id,comments,note_date,lastedited_by,lastedit_date FROM peoplenote " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new Peoplenote(con,new Integer(rs.getInt(1)),new Integer(rs.getInt(2)),rs.getString(3),rs.getDate(4),new Integer(rs.getInt(5)),rs.getTimestamp(6)));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        Peoplenote[] objects = new Peoplenote[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            Peoplenote peoplenote = (Peoplenote) lst.get(i);
            objects[i] = peoplenote;
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
        String stmt = "SELECT peoplenote_id FROM peoplenote " +
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
    //    return getPeoplenoteId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return peoplenoteId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setPeoplenoteId(id);
        setNew(prevIsNew);
    }

    public Integer getPeoplenoteId() {
        return peoplenoteId;
    }

    public void setPeoplenoteId(Integer peoplenoteId) throws ForeignKeyViolationException {
        setWasChanged(this.peoplenoteId != null && this.peoplenoteId != peoplenoteId);
        this.peoplenoteId = peoplenoteId;
        setNew(peoplenoteId.intValue() == 0);
    }

    public Integer getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(Integer peopleId) throws SQLException, ForeignKeyViolationException {
        if (peopleId!=null && !People.exists(getConnection(),"people_id = " + peopleId)) {
            throw new ForeignKeyViolationException("Can't set people_id, foreign key violation: peoplenote_people_fk");
        }
        setWasChanged(this.peopleId != null && !this.peopleId.equals(peopleId));
        this.peopleId = peopleId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.comments != null && !this.comments.equals(comments));
        this.comments = comments;
    }

    public Date getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(Date noteDate) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.noteDate != null && !this.noteDate.equals(noteDate));
        this.noteDate = noteDate;
    }

    public Integer getLasteditedBy() {
        return lasteditedBy;
    }

    public void setLasteditedBy(Integer lasteditedBy) throws SQLException, ForeignKeyViolationException {
        if (null != lasteditedBy)
            lasteditedBy = lasteditedBy == 0 ? null : lasteditedBy;
        if (lasteditedBy!=null && !User.exists(getConnection(),"user_id = " + lasteditedBy)) {
            throw new ForeignKeyViolationException("Can't set lastedited_by, foreign key violation: peoplenote_user_fk");
        }
        setWasChanged(this.lasteditedBy != null && !this.lasteditedBy.equals(lasteditedBy));
        this.lasteditedBy = lasteditedBy;
    }

    public Timestamp getLasteditDate() {
        return lasteditDate;
    }

    public void setLasteditDate(Timestamp lasteditDate) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.lasteditDate != null && !this.lasteditDate.equals(lasteditDate));
        this.lasteditDate = lasteditDate;
    }
    public Object[] getAsRow() {
        Object[] columnValues = new Object[6];
        columnValues[0] = getPeoplenoteId();
        columnValues[1] = getPeopleId();
        columnValues[2] = getComments();
        columnValues[3] = getNoteDate();
        columnValues[4] = getLasteditedBy();
        columnValues[5] = getLasteditDate();
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
            setPeoplenoteId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setPeoplenoteId(null);
        }
        try {
            setPeopleId(Integer.parseInt(flds[1]));
        } catch(NumberFormatException ne) {
            setPeopleId(null);
        }
        setComments(flds[2]);
        setNoteDate(toDate(flds[3]));
        try {
            setLasteditedBy(Integer.parseInt(flds[4]));
        } catch(NumberFormatException ne) {
            setLasteditedBy(null);
        }
        setLasteditDate(toTimeStamp(flds[5]));
    }
}
