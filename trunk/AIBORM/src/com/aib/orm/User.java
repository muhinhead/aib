// Generated by com.xlend.orm.tools.dbgen.DbGenerator.class at Mon May 06 20:16:23 EEST 2013
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class User extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer userId = null;
    private String firstName = null;
    private String lastName = null;
    private String initials = null;
    private String login = null;
    private String passwd = null;

    public User(Connection connection) {
        super(connection, "user", "user_id");
        setColumnNames(new String[]{"user_id", "first_name", "last_name", "initials", "login", "passwd"});
    }

    public User(Connection connection, Integer userId, String firstName, String lastName, String initials, String login, String passwd) {
        super(connection, "user", "user_id");
        setNew(userId.intValue() <= 0);
//        if (userId.intValue() != 0) {
            this.userId = userId;
//        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.initials = initials;
        this.login = login;
        this.passwd = passwd;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        User user = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT user_id,first_name,last_name,initials,login,passwd FROM user WHERE user_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                user = new User(getConnection());
                user.setUserId(new Integer(rs.getInt(1)));
                user.setFirstName(rs.getString(2));
                user.setLastName(rs.getString(3));
                user.setInitials(rs.getString(4));
                user.setLogin(rs.getString(5));
                user.setPasswd(rs.getString(6));
                user.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return user;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO user ("+(getUserId().intValue()!=0?"user_id,":"")+"first_name,last_name,initials,login,passwd) values("+(getUserId().intValue()!=0?"?,":"")+"?,?,?,?,?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getUserId().intValue()!=0) {
                 ps.setObject(++n, getUserId());
             }
             ps.setObject(++n, getFirstName());
             ps.setObject(++n, getLastName());
             ps.setObject(++n, getInitials());
             ps.setObject(++n, getLogin());
             ps.setObject(++n, getPasswd());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getUserId().intValue()==0) {
             stmt = "SELECT max(user_id) FROM user";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setUserId(new Integer(rs.getInt(1)));
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
                    "UPDATE user " +
                    "SET first_name = ?, last_name = ?, initials = ?, login = ?, passwd = ?" + 
                    " WHERE user_id = " + getUserId();
            try {
                ps = getConnection().prepareStatement(stmt);
                ps.setObject(1, getFirstName());
                ps.setObject(2, getLastName());
                ps.setObject(3, getInitials());
                ps.setObject(4, getLogin());
                ps.setObject(5, getPasswd());
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
        if (People.exists(getConnection(),"lastedited_by = " + getUserId())) {
            throw new ForeignKeyViolationException("Can't delete, foreign key violation: people_user_fk");
        }
        if (Location.exists(getConnection(),"lastedited_by = " + getUserId())) {
            throw new ForeignKeyViolationException("Can't delete, foreign key violation: location_user_fk");
        }
        if (Company.exists(getConnection(),"lastedited_by = " + getUserId())) {
            throw new ForeignKeyViolationException("Can't delete, foreign key violation: company_user_fk");
        }
        if (People.exists(getConnection(),"sales_contact_id = " + getUserId())) {
            throw new ForeignKeyViolationException("Can't delete, foreign key violation: people_user_fk2");
        }
        if (getTriggers() != null) {
            getTriggers().beforeDelete(this);
        }
        PreparedStatement ps = null;
        String stmt =
                "DELETE FROM user " +
                "WHERE user_id = " + getUserId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setUserId(new Integer(-getUserId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getUserId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT user_id,first_name,last_name,initials,login,passwd FROM user " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new User(con,new Integer(rs.getInt(1)),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        User[] objects = new User[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            User user = (User) lst.get(i);
            objects[i] = user;
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
        String stmt = "SELECT user_id FROM user " +
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
    //    return getUserId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return userId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setUserId(id);
        setNew(prevIsNew);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) throws ForeignKeyViolationException {
        setWasChanged(this.userId != null && this.userId != userId);
        this.userId = userId;
        setNew(userId.intValue() == 0);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.firstName != null && !this.firstName.equals(firstName));
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.lastName != null && !this.lastName.equals(lastName));
        this.lastName = lastName;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.initials != null && !this.initials.equals(initials));
        this.initials = initials;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.login != null && !this.login.equals(login));
        this.login = login;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.passwd != null && !this.passwd.equals(passwd));
        this.passwd = passwd;
    }
    public Object[] getAsRow() {
        Object[] columnValues = new Object[6];
        columnValues[0] = getUserId();
        columnValues[1] = getFirstName();
        columnValues[2] = getLastName();
        columnValues[3] = getInitials();
        columnValues[4] = getLogin();
        columnValues[5] = getPasswd();
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
            setUserId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setUserId(null);
        }
        setFirstName(flds[1]);
        setLastName(flds[2]);
        setInitials(flds[3]);
        setLogin(flds[4]);
        setPasswd(flds[5]);
    }
}
