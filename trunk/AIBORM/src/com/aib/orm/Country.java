// Generated by com.xlend.orm.tools.dbgen.DbGenerator.class at Sun Jun 02 13:58:13 EEST 2013
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class Country extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer countryId = null;
    private String country = null;
    private String shortname = null;
    private Integer worldregionId = null;

    public Country(Connection connection) {
        super(connection, "country", "country_id");
        setColumnNames(new String[]{"country_id", "country", "shortname", "worldregion_id"});
    }

    public Country(Connection connection, Integer countryId, String country, String shortname, Integer worldregionId) {
        super(connection, "country", "country_id");
        setNew(countryId.intValue() <= 0);
//        if (countryId.intValue() != 0) {
            this.countryId = countryId;
//        }
        this.country = country;
        this.shortname = shortname;
        this.worldregionId = worldregionId;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        Country country = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT country_id,country,shortname,worldregion_id FROM country WHERE country_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                country = new Country(getConnection());
                country.setCountryId(new Integer(rs.getInt(1)));
                country.setCountry(rs.getString(2));
                country.setShortname(rs.getString(3));
                country.setWorldregionId(new Integer(rs.getInt(4)));
                country.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return country;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO country ("+(getCountryId().intValue()!=0?"country_id,":"")+"country,shortname,worldregion_id) values("+(getCountryId().intValue()!=0?"?,":"")+"?,?,?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getCountryId().intValue()!=0) {
                 ps.setObject(++n, getCountryId());
             }
             ps.setObject(++n, getCountry());
             ps.setObject(++n, getShortname());
             ps.setObject(++n, getWorldregionId());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getCountryId().intValue()==0) {
             stmt = "SELECT max(country_id) FROM country";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setCountryId(new Integer(rs.getInt(1)));
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
                    "UPDATE country " +
                    "SET country = ?, shortname = ?, worldregion_id = ?" + 
                    " WHERE country_id = " + getCountryId();
            try {
                ps = getConnection().prepareStatement(stmt);
                ps.setObject(1, getCountry());
                ps.setObject(2, getShortname());
                ps.setObject(3, getWorldregionId());
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
        if (Location.exists(getConnection(),"country_id = " + getCountryId())) {
            throw new ForeignKeyViolationException("Can't delete, foreign key violation: location_country_fk");
        }
        if (Company.exists(getConnection(),"country_id = " + getCountryId())) {
            throw new ForeignKeyViolationException("Can't delete, foreign key violation: company_country_fk");
        }
        if (getTriggers() != null) {
            getTriggers().beforeDelete(this);
        }
        PreparedStatement ps = null;
        String stmt =
                "DELETE FROM country " +
                "WHERE country_id = " + getCountryId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setCountryId(new Integer(-getCountryId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getCountryId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT country_id,country,shortname,worldregion_id FROM country " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new Country(con,new Integer(rs.getInt(1)),rs.getString(2),rs.getString(3),new Integer(rs.getInt(4))));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        Country[] objects = new Country[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            Country country = (Country) lst.get(i);
            objects[i] = country;
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
        String stmt = "SELECT country_id FROM country " +
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
    //    return getCountryId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return countryId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setCountryId(id);
        setNew(prevIsNew);
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) throws ForeignKeyViolationException {
        setWasChanged(this.countryId != null && this.countryId != countryId);
        this.countryId = countryId;
        setNew(countryId.intValue() == 0);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.country != null && !this.country.equals(country));
        this.country = country;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.shortname != null && !this.shortname.equals(shortname));
        this.shortname = shortname;
    }

    public Integer getWorldregionId() {
        return worldregionId;
    }

    public void setWorldregionId(Integer worldregionId) throws SQLException, ForeignKeyViolationException {
        if (worldregionId!=null && !Worldregion.exists(getConnection(),"worldregion_id = " + worldregionId)) {
            throw new ForeignKeyViolationException("Can't set worldregion_id, foreign key violation: country_worldregion_fk");
        }
        setWasChanged(this.worldregionId != null && !this.worldregionId.equals(worldregionId));
        this.worldregionId = worldregionId;
    }
    public Object[] getAsRow() {
        Object[] columnValues = new Object[4];
        columnValues[0] = getCountryId();
        columnValues[1] = getCountry();
        columnValues[2] = getShortname();
        columnValues[3] = getWorldregionId();
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
            setCountryId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setCountryId(null);
        }
        setCountry(flds[1]);
        setShortname(flds[2]);
        try {
            setWorldregionId(Integer.parseInt(flds[3]));
        } catch(NumberFormatException ne) {
            setWorldregionId(null);
        }
    }
}
