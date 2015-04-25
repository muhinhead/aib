/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.people;

import com.aib.AIBclient;
import com.aib.GeneralFrame;
import com.aib.GeneralGridPanel;
import com.aib.orm.People;
import com.aib.people.EditPeopleDialog;
import com.aib.remote.IMessageSender;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 *
 * @author Nick Mukhin
 */
public class PeopleGrid extends GeneralGridPanel {    
    
    public static final String SELECT = "select people_id \"ID\","
            + "job_discip \"Job Title\","
            + "title \"Title\",first_name \"First Name\", "
            + "last_name \"Last Name\",main_email \"E-mail\","
//            + "suffix \"Suffix\", greeting \"Greeting\", "
            + "desk_phone \"Desk Phone\", mobile_phone \"Mobile Phone\", "
            + "lastedit_date \"Last Edited\", "
            + "(select initials from user where user_id=people.lastedited_by) \"Editor\" "
            + "from people "+GeneralGridPanel.SELECTLIMIT;
    private static HashMap<Integer, Integer> maxWidths = new HashMap<Integer, Integer>();

    static {
        maxWidths.put(0, 40);
        maxWidths.put(2, 45);
//        maxWidths.put(4, 80);
//        maxWidths.put(5, 80);
//        maxWidths.put(9, 40);
    }

    public PeopleGrid(IMessageSender exchanger) throws RemoteException {
        super(exchanger, SELECT, maxWidths, false);
    }

    public PeopleGrid(IMessageSender exchanger, String select, boolean readOnly) throws RemoteException {
        super(exchanger, select, maxWidths, readOnly);
        EditPeopleDialog.locationID = null;
    }

    public void filterOnLocationID(Integer locationID) {
        setSelect(SELECT.replace("from people", "from people where location_id=" 
                + (EditPeopleDialog.locationID = locationID)));
        refresh();
    }

    public void refresh(Integer compID) {
        setSelect(SELECT.replace(GeneralGridPanel.SELECTLIMIT, "")
                    + " where people_id in (select people_id from peoplecompany where company_id=" + compID + ")");
        refresh();
    }
    
    protected void additionalSettings() {
        
    }

    @Override
    protected AbstractAction addAction() {
        return new AbstractAction("Add") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                additionalSettings();
                EditPeopleDialog ed = new EditPeopleDialog("Add Person", null);
                if (EditPeopleDialog.okPressed) {
                    People person = (People) ed.getEditPanel().getDbObject();
                    refresh(person.getPeopleId());
                }
            }
        };
    }

    @Override
    protected AbstractAction editAction() {
        return new AbstractAction("Edit") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int id = getSelectedID();
                if (id != 0) {
                    try {
                        People person = (People) exchanger.loadDbObjectOnID(People.class, id);
                        additionalSettings();
                        new EditPeopleDialog("Edit Person", person);
                        if (EditPeopleDialog.okPressed) {
                            refresh();
                        }
                    } catch (RemoteException ex) {
                        AIBclient.logAndShowMessage(ex);
                    }
                }
            }
        };
    }

    @Override
    protected AbstractAction delAction() {
        return new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int id = getSelectedID();
                if (id != 0) {
                    try {
                        People person = (People) exchanger.loadDbObjectOnID(People.class, id);
                        if (person != null && GeneralFrame.yesNo("Attention!", 
                                "Do you want to delete record of this person?") == JOptionPane.YES_OPTION) {
                            exchanger.deleteObject(person);
                            refresh();
                        }
                    } catch (RemoteException ex) {
                        AIBclient.logAndShowMessage(ex);
                    }
                }
            }
        };
    }
}
