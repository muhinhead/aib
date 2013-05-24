// Generated by com.xlend.orm.tools.dbgen.DbGenerator.class at Fri May 24 20:05:19 EEST 2013
// generated file: do not modify
package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.Triggers;
import java.sql.*;
import java.util.ArrayList;

public class Company extends DbObject  {
    private static Triggers activeTriggers = null;
    private Integer companyId = null;
    private String fullName = null;
    private Integer isDummy = null;
    private String abbreviation = null;
    private Object logo = null;
    private Double turnover = null;
    private String address = null;
    private String postcode = null;
    private String mailaddress = null;
    private String mailpostcode = null;
    private Integer countryId = null;
    private String mainPhone = null;
    private String mainFax = null;
    private Integer memberLevel = null;
    private Date renewalDate = null;
    private Date verifyDate = null;
    private String comments = null;
    private Integer lasteditedBy = null;
    private Timestamp lasteditDate = null;

    public Company(Connection connection) {
        super(connection, "company", "company_id");
        setColumnNames(new String[]{"company_id", "full_name", "is_dummy", "abbreviation", "logo", "turnover", "address", "postcode", "mailaddress", "mailpostcode", "country_id", "main_phone", "main_fax", "member_level", "renewal_date", "verify_date", "comments", "lastedited_by", "lastedit_date"});
    }

    public Company(Connection connection, Integer companyId, String fullName, Integer isDummy, String abbreviation, Object logo, Double turnover, String address, String postcode, String mailaddress, String mailpostcode, Integer countryId, String mainPhone, String mainFax, Integer memberLevel, Date renewalDate, Date verifyDate, String comments, Integer lasteditedBy, Timestamp lasteditDate) {
        super(connection, "company", "company_id");
        setNew(companyId.intValue() <= 0);
//        if (companyId.intValue() != 0) {
            this.companyId = companyId;
//        }
        this.fullName = fullName;
        this.isDummy = isDummy;
        this.abbreviation = abbreviation;
        this.logo = logo;
        this.turnover = turnover;
        this.address = address;
        this.postcode = postcode;
        this.mailaddress = mailaddress;
        this.mailpostcode = mailpostcode;
        this.countryId = countryId;
        this.mainPhone = mainPhone;
        this.mainFax = mainFax;
        this.memberLevel = memberLevel;
        this.renewalDate = renewalDate;
        this.verifyDate = verifyDate;
        this.comments = comments;
        this.lasteditedBy = lasteditedBy;
        this.lasteditDate = lasteditDate;
    }

    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        Company company = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT company_id,full_name,is_dummy,abbreviation,logo,turnover,address,postcode,mailaddress,mailpostcode,country_id,main_phone,main_fax,member_level,renewal_date,verify_date,comments,lastedited_by,lastedit_date FROM company WHERE company_id=" + id;
        try {
            ps = getConnection().prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                company = new Company(getConnection());
                company.setCompanyId(new Integer(rs.getInt(1)));
                company.setFullName(rs.getString(2));
                company.setIsDummy(new Integer(rs.getInt(3)));
                company.setAbbreviation(rs.getString(4));
                company.setLogo(rs.getObject(5));
                company.setTurnover(rs.getDouble(6));
                company.setAddress(rs.getString(7));
                company.setPostcode(rs.getString(8));
                company.setMailaddress(rs.getString(9));
                company.setMailpostcode(rs.getString(10));
                company.setCountryId(new Integer(rs.getInt(11)));
                company.setMainPhone(rs.getString(12));
                company.setMainFax(rs.getString(13));
                company.setMemberLevel(new Integer(rs.getInt(14)));
                company.setRenewalDate(rs.getDate(15));
                company.setVerifyDate(rs.getDate(16));
                company.setComments(rs.getString(17));
                company.setLasteditedBy(new Integer(rs.getInt(18)));
                company.setLasteditDate(rs.getTimestamp(19));
                company.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        return company;
    }

    protected void insert() throws SQLException, ForeignKeyViolationException {
         if (getTriggers() != null) {
             getTriggers().beforeInsert(this);
         }
         PreparedStatement ps = null;
         String stmt =
                "INSERT INTO company ("+(getCompanyId().intValue()!=0?"company_id,":"")+"full_name,is_dummy,abbreviation,logo,turnover,address,postcode,mailaddress,mailpostcode,country_id,main_phone,main_fax,member_level,renewal_date,verify_date,comments,lastedited_by,lastedit_date) values("+(getCompanyId().intValue()!=0?"?,":"")+"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
         try {
             ps = getConnection().prepareStatement(stmt);
             int n = 0;
             if (getCompanyId().intValue()!=0) {
                 ps.setObject(++n, getCompanyId());
             }
             ps.setObject(++n, getFullName());
             ps.setObject(++n, getIsDummy());
             ps.setObject(++n, getAbbreviation());
             ps.setObject(++n, getLogo());
             ps.setObject(++n, getTurnover());
             ps.setObject(++n, getAddress());
             ps.setObject(++n, getPostcode());
             ps.setObject(++n, getMailaddress());
             ps.setObject(++n, getMailpostcode());
             ps.setObject(++n, getCountryId());
             ps.setObject(++n, getMainPhone());
             ps.setObject(++n, getMainFax());
             ps.setObject(++n, getMemberLevel());
             ps.setObject(++n, getRenewalDate());
             ps.setObject(++n, getVerifyDate());
             ps.setObject(++n, getComments());
             ps.setObject(++n, getLasteditedBy());
             ps.setObject(++n, getLasteditDate());
             ps.execute();
         } finally {
             if (ps != null) ps.close();
         }
         ResultSet rs = null;
         if (getCompanyId().intValue()==0) {
             stmt = "SELECT max(company_id) FROM company";
             try {
                 ps = getConnection().prepareStatement(stmt);
                 rs = ps.executeQuery();
                 if (rs.next()) {
                     setCompanyId(new Integer(rs.getInt(1)));
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
                    "UPDATE company " +
                    "SET full_name = ?, is_dummy = ?, abbreviation = ?, logo = ?, turnover = ?, address = ?, postcode = ?, mailaddress = ?, mailpostcode = ?, country_id = ?, main_phone = ?, main_fax = ?, member_level = ?, renewal_date = ?, verify_date = ?, comments = ?, lastedited_by = ?, lastedit_date = ?" + 
                    " WHERE company_id = " + getCompanyId();
            try {
                ps = getConnection().prepareStatement(stmt);
                ps.setObject(1, getFullName());
                ps.setObject(2, getIsDummy());
                ps.setObject(3, getAbbreviation());
                ps.setObject(4, getLogo());
                ps.setObject(5, getTurnover());
                ps.setObject(6, getAddress());
                ps.setObject(7, getPostcode());
                ps.setObject(8, getMailaddress());
                ps.setObject(9, getMailpostcode());
                ps.setObject(10, getCountryId());
                ps.setObject(11, getMainPhone());
                ps.setObject(12, getMainFax());
                ps.setObject(13, getMemberLevel());
                ps.setObject(14, getRenewalDate());
                ps.setObject(15, getVerifyDate());
                ps.setObject(16, getComments());
                ps.setObject(17, getLasteditedBy());
                ps.setObject(18, getLasteditDate());
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
        if (Location.exists(getConnection(),"company_id = " + getCompanyId())) {
            throw new ForeignKeyViolationException("Can't delete, foreign key violation: location_company_fk");
        }
        if (getTriggers() != null) {
            getTriggers().beforeDelete(this);
        }
        {// delete cascade from peoplecompany
            Peoplecompany[] records = (Peoplecompany[])Peoplecompany.load(getConnection(),"company_id = " + getCompanyId(),null);
            for (int i = 0; i<records.length; i++) {
                Peoplecompany peoplecompany = records[i];
                peoplecompany.delete();
            }
        }
        {// delete cascade from complink
            Complink[] records = (Complink[])Complink.load(getConnection(),"company_id = " + getCompanyId(),null);
            for (int i = 0; i<records.length; i++) {
                Complink complink = records[i];
                complink.delete();
            }
        }
        {// delete cascade from comppublic
            Comppublic[] records = (Comppublic[])Comppublic.load(getConnection(),"company_id = " + getCompanyId(),null);
            for (int i = 0; i<records.length; i++) {
                Comppublic comppublic = records[i];
                comppublic.delete();
            }
        }
        {// delete cascade from compindustry
            Compindustry[] records = (Compindustry[])Compindustry.load(getConnection(),"company_id = " + getCompanyId(),null);
            for (int i = 0; i<records.length; i++) {
                Compindustry compindustry = records[i];
                compindustry.delete();
            }
        }
        PreparedStatement ps = null;
        String stmt =
                "DELETE FROM company " +
                "WHERE company_id = " + getCompanyId();
        try {
            ps = getConnection().prepareStatement(stmt);
            ps.execute();
        } finally {
            if (ps != null) ps.close();
        }
        setCompanyId(new Integer(-getCompanyId().intValue()));
        if (getTriggers() != null) {
            getTriggers().afterDelete(this);
        }
    }

    public boolean isDeleted() {
        return (getCompanyId().intValue() < 0);
    }

    public static DbObject[] load(Connection con,String whereCondition,String orderCondition) throws SQLException {
        ArrayList lst = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stmt = "SELECT company_id,full_name,is_dummy,abbreviation,logo,turnover,address,postcode,mailaddress,mailpostcode,country_id,main_phone,main_fax,member_level,renewal_date,verify_date,comments,lastedited_by,lastedit_date FROM company " +
                ((whereCondition != null && whereCondition.length() > 0) ?
                " WHERE " + whereCondition : "") +
                ((orderCondition != null && orderCondition.length() > 0) ?
                " ORDER BY " + orderCondition : "");
        try {
            ps = con.prepareStatement(stmt);
            rs = ps.executeQuery();
            while (rs.next()) {
                DbObject dbObj;
                lst.add(dbObj=new Company(con,new Integer(rs.getInt(1)),rs.getString(2),new Integer(rs.getInt(3)),rs.getString(4),rs.getObject(5),rs.getDouble(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),new Integer(rs.getInt(11)),rs.getString(12),rs.getString(13),new Integer(rs.getInt(14)),rs.getDate(15),rs.getDate(16),rs.getString(17),new Integer(rs.getInt(18)),rs.getTimestamp(19)));
                dbObj.setNew(false);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } finally {
                if (ps != null) ps.close();
            }
        }
        Company[] objects = new Company[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            Company company = (Company) lst.get(i);
            objects[i] = company;
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
        String stmt = "SELECT company_id FROM company " +
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
    //    return getCompanyId() + getDelimiter();
    //}

    public Integer getPK_ID() {
        return companyId;
    }

    public void setPK_ID(Integer id) throws ForeignKeyViolationException {
        boolean prevIsNew = isNew();
        setCompanyId(id);
        setNew(prevIsNew);
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) throws ForeignKeyViolationException {
        setWasChanged(this.companyId != null && this.companyId != companyId);
        this.companyId = companyId;
        setNew(companyId.intValue() == 0);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.fullName != null && !this.fullName.equals(fullName));
        this.fullName = fullName;
    }

    public Integer getIsDummy() {
        return isDummy;
    }

    public void setIsDummy(Integer isDummy) throws SQLException, ForeignKeyViolationException {
        if (null != isDummy)
            isDummy = isDummy == 0 ? null : isDummy;
        setWasChanged(this.isDummy != null && !this.isDummy.equals(isDummy));
        this.isDummy = isDummy;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.abbreviation != null && !this.abbreviation.equals(abbreviation));
        this.abbreviation = abbreviation;
    }

    public Object getLogo() {
        return logo;
    }

    public void setLogo(Object logo) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.logo != null && !this.logo.equals(logo));
        this.logo = logo;
    }

    public Double getTurnover() {
        return turnover;
    }

    public void setTurnover(Double turnover) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.turnover != null && !this.turnover.equals(turnover));
        this.turnover = turnover;
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
            throw new ForeignKeyViolationException("Can't set country_id, foreign key violation: company_country_fk");
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

    public Integer getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(Integer memberLevel) throws SQLException, ForeignKeyViolationException {
        if (null != memberLevel)
            memberLevel = memberLevel == 0 ? null : memberLevel;
        setWasChanged(this.memberLevel != null && !this.memberLevel.equals(memberLevel));
        this.memberLevel = memberLevel;
    }

    public Date getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(Date renewalDate) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.renewalDate != null && !this.renewalDate.equals(renewalDate));
        this.renewalDate = renewalDate;
    }

    public Date getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Date verifyDate) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.verifyDate != null && !this.verifyDate.equals(verifyDate));
        this.verifyDate = verifyDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) throws SQLException, ForeignKeyViolationException {
        setWasChanged(this.comments != null && !this.comments.equals(comments));
        this.comments = comments;
    }

    public Integer getLasteditedBy() {
        return lasteditedBy;
    }

    public void setLasteditedBy(Integer lasteditedBy) throws SQLException, ForeignKeyViolationException {
        if (null != lasteditedBy)
            lasteditedBy = lasteditedBy == 0 ? null : lasteditedBy;
        if (lasteditedBy!=null && !User.exists(getConnection(),"user_id = " + lasteditedBy)) {
            throw new ForeignKeyViolationException("Can't set lastedited_by, foreign key violation: company_user_fk");
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
        Object[] columnValues = new Object[19];
        columnValues[0] = getCompanyId();
        columnValues[1] = getFullName();
        columnValues[2] = getIsDummy();
        columnValues[3] = getAbbreviation();
        columnValues[4] = getLogo();
        columnValues[5] = getTurnover();
        columnValues[6] = getAddress();
        columnValues[7] = getPostcode();
        columnValues[8] = getMailaddress();
        columnValues[9] = getMailpostcode();
        columnValues[10] = getCountryId();
        columnValues[11] = getMainPhone();
        columnValues[12] = getMainFax();
        columnValues[13] = getMemberLevel();
        columnValues[14] = getRenewalDate();
        columnValues[15] = getVerifyDate();
        columnValues[16] = getComments();
        columnValues[17] = getLasteditedBy();
        columnValues[18] = getLasteditDate();
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
            setCompanyId(Integer.parseInt(flds[0]));
        } catch(NumberFormatException ne) {
            setCompanyId(null);
        }
        setFullName(flds[1]);
        try {
            setIsDummy(Integer.parseInt(flds[2]));
        } catch(NumberFormatException ne) {
            setIsDummy(null);
        }
        setAbbreviation(flds[3]);
        setLogo(flds[4]);
        try {
            setTurnover(Double.parseDouble(flds[5]));
        } catch(NumberFormatException ne) {
            setTurnover(null);
        }
        setAddress(flds[6]);
        setPostcode(flds[7]);
        setMailaddress(flds[8]);
        setMailpostcode(flds[9]);
        try {
            setCountryId(Integer.parseInt(flds[10]));
        } catch(NumberFormatException ne) {
            setCountryId(null);
        }
        setMainPhone(flds[11]);
        setMainFax(flds[12]);
        try {
            setMemberLevel(Integer.parseInt(flds[13]));
        } catch(NumberFormatException ne) {
            setMemberLevel(null);
        }
        setRenewalDate(toDate(flds[14]));
        setVerifyDate(toDate(flds[15]));
        setComments(flds[16]);
        try {
            setLasteditedBy(Integer.parseInt(flds[17]));
        } catch(NumberFormatException ne) {
            setLasteditedBy(null);
        }
        setLasteditDate(toTimeStamp(flds[18]));
    }
}
