// Generated by com.xlend.orm.tools.dbgen.DbGenerator.class at Sun Feb 07 10:15:05 CET 2016
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class Location extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer locationId = null;
    private String name = null;
    private String abbreviation = null;
    private String address = null;
    private String postcode = null;
    private String mailaddress = null;
    private String mailpostcode = null;
    private Integer countryId = null;
    private String mainPhone = null;
    private String mainFax = null;
    private Object logo = null;
    private String comments = null;
    private Integer companyId = null;
    private Integer lasteditedBy = null;
    private Timestamp lasteditDate = null;

    public Location(Connection connection) {
        super(connection, "location", "location_id");
        setColumnNames(new String[]{"location_id", "name", "abbreviation", "address", "postcode", "mailaddress", "mailpostcode", "country_id", "main_phone", "main_fax", "logo", "comments", "company_id", "lastedited_by", "lastedit_date"});
    }

    public Location(Connection connection, Integer locationId, String name, String abbreviation, String address, String postcode, String mailaddress, String mailpostcode, Integer countryId, String mainPhone, String mainFax, Object logo, String comments, Integer companyId, Integer lasteditedBy, Timestamp lasteditDate) {
        super(connection, "location", "location_id");
        setNew(locationId.intValue() <= 0);
//        if (locationId.intValue() != 0) {
            this.locationId = locationId;
//        }
        this.name = name;
        this.abbreviation = abbreviation;
        this.address = address;
        this.postcode = postcode;
        this.mailaddress = mailaddress;
        this.mailpostcode = mailpostcode;
        this.countryId = countryId;
        this.mainPhone = mainPhone;
        this.mainFax = mainFax;
        this.logo = logo;
        this.comments = comments;
        this.companyId = companyId;
        this.lasteditedBy = lasteditedBy;
        this.lasteditDate = lasteditDate;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        Location location = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT location_id,name,abbreviation,address,postcode,mailaddress,mailpostcode,country_id,main_phone,main_fax,logo,comments,company_id,lastedited_by,lastedit_date FROM location WHERE location_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                location = new Location(getConnection());
                location.setLocationId(new Integer(rs.getInt(1)));
                location.setName(rs.getString(2));
                location.setAbbreviation(rs.getString(3));
                location.setAddress(rs.getString(4));
                location.setPostcode(rs.getString(5));
                location.setMailaddress(rs.getString(6));
                location.setMailpostcode(rs.getString(7));
                location.setCountryId(new Integer(rs.getInt(8)));
                location.setMainPhone(rs.getString(9));
                location.setMainFax(rs.getString(10));
                location.setLogo(rs.getObject(11));
                location.setComments(rs.getString(12));
                location.setCompanyId(new Integer(rs.getInt(13)));
                location.setLasteditedBy(new Integer(rs.getInt(14)));
                location.setLasteditDate(rs.getTimestamp(15));
                location.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return location;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO location ("+(getLocationId().intValue()!=0?"location_id,":"")+"name,abbreviation,address,postcode,mailaddress,mailpostcode,country_id,main_phone,main_fax,logo,comments,company_id,lastedited_by,lastedit_date) values("+(getLocationId().intValue()!=0?"?,":"")+"?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getLocationId().intValue()!=0) {
                 ps.setObject(++n, getLocationId());
             }
             ps.setObject(++n, getName());
             ps.setObject(++n, getAbbreviation());
             ps.setObject(++n, getAddress());
             ps.setObject(++n, getPostcode());
             ps.setObject(++n, getMailaddress());
             ps.setObject(++n, getMailpostcode());
             ps.setObject(++n, getCountryId());
             ps.setObject(++n, getMainPhone());
             ps.setObject(++n, getMainFax());
             ps.setObject(++n, getLogo());
             ps.setObject(++n, getComments());
             ps.setObject(++n, getCompanyId());
             ps.setObject(++n, getLasteditedBy());
             ps.setObject(++n, getLasteditDate());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getLocationId().intValue()==0) {
             stmt = "SELECT max(location_id) FROM location";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setLocationId(new Integer(rs.getInt(1)));
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
                    "UPDATE location " +
                    "SET name = ?, abbreviation = ?, address = ?, postcode = ?, mailaddress = ?, mailpostcode = ?, country_id = ?, main_phone = ?, main_fax = ?, logo = ?, comments = ?, company_id = ?, lastedited_by = ?, lastedit_date = ?" + 
                    " WHERE location_id = " + getLocationId();
            try {
                ps = getConnection().prepareStatement(stmt);
                ps.setObject(1, getName());
                ps.setObject(2, getAbbreviation());
                ps.setObject(3, getAddress());
                ps.setObject(4, getPostcode());
                ps.setObject(5, getMailaddress());
                ps.setObject(6, getMailpostcode());
                ps.setObject(7, getCountryId());
                ps.setObject(8, getMainPhone());
                ps.setObject(9, getMainFax());
                ps.setObject(10, getLogo());
                ps.setObject(11, getComments());
                ps.setObject(12, getCompanyId());
                ps.setObject(13, getLasteditedBy());
                ps.setObject(14, getLasteditDate());
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
        if (People.exists(getConnection(),"location_id = " + getLocationId())) {
            throw new ForeignKeyViolationException("Can't delete, foreign key violation: people_location_fk");
        }
        if (getTriggers() != null) {
            getTriggers().beforeDelete(this);
        }
        {// delete cascade from loclink
            Loclink[] records = (Loclink[])Loclink.load(getConnection(),"location_id = " + getLocationId(),null);
            for (int i = 0; i<records.length; i++) {
                Loclink loclink = records[i];
                loclink.delete();
            }
        }
        {// delete cascade from locindustry
            Locindustry[] records = (Locindustry[])Locindustry.load(getConnection(),"location_id = " + getLocationId(),null);
            for (int i = 0; i<records.length; i++) {
                Locindustry locindustry = records[i];
                locindustry.delete();
            }
        }
        PreparedStatement ps = null;
        String stmt =
                "DELETE FROM location " +
                "WHERE location_id = " + getLocationId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setLocationId(new Integer(-getLocationId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getLocationId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT location_id,name,abbreviation,address,postcode,mailaddress,mailpostcode,country_id,main_phone,main_fax,logo,comments,company_id,lastedited_by,lastedit_date FROM location " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new Location(con,new Integer(rs.getInt(1)),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),new Integer(rs.getInt(8)),rs.getString(9),rs.getString(10),rs.getObject(11),rs.getString(12),new Integer(rs.getInt(13)),new Integer(rs.getInt(14)),rs.getTimestamp(15)));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        Location[] objects = new Location[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            Location location = (Location) lst.get(i);
            objects[i] = location;
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
        String stmt = "SELECT location_id FROM location " +
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
    //    return getLocationId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return locationId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setLocationId(id);
        setNew(prevIsNew);
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) throws ForeignKeyViolationException {
        setWasChanged(this.locationId != null && this.locationId != locationId);
        this.locationId = locationId;
        setNew(locationId.intValue() == 0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.name != null && !this.name.equals(name));
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.abbreviation != null && !this.abbreviation.equals(abbreviation));
        this.abbreviation = abbreviation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.address != null && !this.address.equals(address));
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.postcode != null && !this.postcode.equals(postcode));
        this.postcode = postcode;
    }

    public String getMailaddress() {
        return mailaddress;
    }

    public void setMailaddress(String mailaddress) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.mailaddress != null && !this.mailaddress.equals(mailaddress));
        this.mailaddress = mailaddress;
    }

    public String getMailpostcode() {
        return mailpostcode;
    }

    public void setMailpostcode(String mailpostcode) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.mailpostcode != null && !this.mailpostcode.equals(mailpostcode));
        this.mailpostcode = mailpostcode;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) throws SQLException, ForeignKeyViolationException {
        if (null != countryId)
            countryId = countryId == 0 ? null : countryId;
        if (countryId!=null && !Country.exists(getConnection(),"country_id = " + countryId)) {
            throw new ForeignKeyViolationException("Can't set country_id, foreign key violation: location_country_fk");
        }
        setWasChanged(this.countryId != null && !this.countryId.equals(countryId));
        this.countryId = countryId;
    }

    public String getMainPhone() {
        return mainPhone;
    }

    public void setMainPhone(String mainPhone) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.mainPhone != null && !this.mainPhone.equals(mainPhone));
        this.mainPhone = mainPhone;
    }

    public String getMainFax() {
        return mainFax;
    }

    public void setMainFax(String mainFax) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.mainFax != null && !this.mainFax.equals(mainFax));
        this.mainFax = mainFax;
    }

    public Object getLogo() {
        return logo;
    }

    public void setLogo(Object logo) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.logo != null && !this.logo.equals(logo));
        this.logo = logo;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.comments != null && !this.comments.equals(comments));
        this.comments = comments;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) throws SQLException, ForeignKeyViolationException {
        if (null != companyId)
            companyId = companyId == 0 ? null : companyId;
        if (companyId!=null && !Company.exists(getConnection(),"company_id = " + companyId)) {
            throw new ForeignKeyViolationException("Can't set company_id, foreign key violation: location_company_fk");
        }
        setWasChanged(this.companyId != null && !this.companyId.equals(companyId));
        this.companyId = companyId;
    }

    public Integer getLasteditedBy() {
        return lasteditedBy;
    }

    public void setLasteditedBy(Integer lasteditedBy) throws SQLException, ForeignKeyViolationException {
        if (null != lasteditedBy)
            lasteditedBy = lasteditedBy == 0 ? null : lasteditedBy;
        if (lasteditedBy!=null && !User.exists(getConnection(),"user_id = " + lasteditedBy)) {
            throw new ForeignKeyViolationException("Can't set lastedited_by, foreign key violation: location_user_fk");
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
        Object[] columnValues = new Object[15];
        columnValues[0] = getLocationId();
        columnValues[1] = getName();
        columnValues[2] = getAbbreviation();
        columnValues[3] = getAddress();
        columnValues[4] = getPostcode();
        columnValues[5] = getMailaddress();
        columnValues[6] = getMailpostcode();
        columnValues[7] = getCountryId();
        columnValues[8] = getMainPhone();
        columnValues[9] = getMainFax();
        columnValues[10] = getLogo();
        columnValues[11] = getComments();
        columnValues[12] = getCompanyId();
        columnValues[13] = getLasteditedBy();
        columnValues[14] = getLasteditDate();
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
            setLocationId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setLocationId(null);
        }
        setName(flds[1]);
        setAbbreviation(flds[2]);
        setAddress(flds[3]);
        setPostcode(flds[4]);
        setMailaddress(flds[5]);
        setMailpostcode(flds[6]);
        try {
            setCountryId(Integer.parseInt(flds[7]));
        } catch(NumberFormatException ne) {
            setCountryId(null);
        }
        setMainPhone(flds[8]);
        setMainFax(flds[9]);
        setLogo(flds[10]);
        setComments(flds[11]);
        try {
            setCompanyId(Integer.parseInt(flds[12]));
        } catch(NumberFormatException ne) {
            setCompanyId(null);
        }
        try {
            setLasteditedBy(Integer.parseInt(flds[13]));
        } catch(NumberFormatException ne) {
            setLasteditedBy(null);
        }
        setLasteditDate(toTimeStamp(flds[14]));
    }
}
