package com.aib.orm;

import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.orm.dbobject.TriggerAdapter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 *
 * @author Nick Mukhin
 */
public class PeopleWithTriggers extends People {

    private String oldDepartment;

    public PeopleWithTriggers(Connection connection) {
        super(connection);
        addTriggers();
    }

    public PeopleWithTriggers(Connection connection, Integer peopleId, String source, String title, String firstName, String lastName, String suffix, String greeting, Integer locationId, Object photo, String level, String jobDiscip, String department, String specAddress, String mailaddress, String mailpostcode, String deskPhone, String deskFax, String mobilePhone, String mainEmail, String alterEmail, String pa, String paPhone, String paEmail, String otherContacts, Integer isPrimary, Integer isSubscriber, Integer isMarketintl, Integer isMediabrief, Integer isInsourcebook, Integer isAibCoordinator, Integer isAibJudge, Integer isAibEntrant, Integer isIndividualMember, Date verifyDate, Integer salesContactId, Date actionDate, String nextAction, String externalUser, String externalPasswd, Integer lasteditedBy, Timestamp lasteditDate, Integer isInvoiceCntct, Integer isDigitalChnl, Integer countryId) {
        super(connection, peopleId, source, title, firstName, lastName, suffix, greeting, locationId, photo,
                level, jobDiscip, department, specAddress, mailaddress, mailpostcode, deskPhone, deskFax,
                mobilePhone, mainEmail, alterEmail, pa, paPhone, paEmail, otherContacts, isPrimary, isSubscriber,
                isMarketintl, isMediabrief, isInsourcebook, isAibCoordinator, isAibJudge, isAibEntrant, isIndividualMember, verifyDate, salesContactId, actionDate, nextAction,
                externalUser, externalPasswd, lasteditedBy, lasteditDate, isInvoiceCntct, isDigitalChnl, countryId);
        addTriggers();
    }

    public PeopleWithTriggers(People papa) {
        super(papa.getConnection(), papa.getPeopleId(), papa.getSource(), papa.getTitle(), papa.getFirstName(), papa.getLastName(), papa.getSuffix(), papa.getGreeting(), papa.getLocationId(), papa.getPhoto(),
                papa.getLevel(), papa.getJobDiscip(), papa.getDepartment(), papa.getSpecAddress(), papa.getMailaddress(), papa.getMailpostcode(), papa.getDeskPhone(), papa.getDeskFax(),
                papa.getMobilePhone(), papa.getMainEmail(), papa.getAlterEmail(), papa.getPa(), papa.getPaPhone(), papa.getPaEmail(), papa.getOtherContacts(), papa.getIsPrimary(), papa.getIsSubscriber(),
                papa.getIsMarketintl(), papa.getIsMediabrief(), papa.getIsInsourcebook(), papa.getIsAibCoordinator(), papa.getIsAibJudge(), papa.getIsAibEntrant(), papa.getIsIndividualMember(),papa.getVerifyDate(), papa.getSalesContactId(), papa.getActionDate(), papa.getNextAction(),
                papa.getExternalUser(), papa.getExternalPasswd(), papa.getLasteditedBy(), papa.getLasteditDate(), papa.getIsInvoiceCntct(), papa.getIsDigitalChnl(), papa.getCountryId());
        setNew(papa.isNew());
        addTriggers();
    }

    @Override
    public void setDepartment(String newDepartment) throws SQLException, ForeignKeyViolationException {
        if (newDepartment != getDepartment()) {
            oldDepartment = newDepartment;
        }
        super.setDepartment(newDepartment);
    }

    @Override
    public void setConnection(Connection connection) {
        super.setConnection(connection);
        addTriggers();
    }

    @Override
    public DbObject loadOnId(int id) throws SQLException, ForeignKeyViolationException {
        People papa = (People) super.loadOnId(id);
        return papa == null ? null : new PeopleWithTriggers(papa);
    }

    private void addTriggers() {
        TriggerAdapter t;
        t = new TriggerAdapter() {
            @Override
            public void afterUpdate(DbObject dbObject) throws SQLException {
                People self = (People) dbObject;
                if (oldDepartment != null && self.getDepartment() != oldDepartment) {
                    long tm = Calendar.getInstance().getTimeInMillis();
                    Departmenthistory dh = new Departmenthistory(getConnection(),
                            0, getPeopleId(), oldDepartment, new Timestamp(tm));
                    dh.setNew(true);
                    try {
                        dh.save();
                    } catch (ForeignKeyViolationException ex) {
                    }
                }
            }
        };
        setTriggers(t);
    }
}
