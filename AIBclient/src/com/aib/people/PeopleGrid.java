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
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Nick Mukhin
 */
public class PeopleGrid extends GeneralGridPanel {

    private static final String COMPANY_ID = " company_id=";
    public static final String SELECT = "select people_id \"ID\","
            + "job_discip \"Job Title\","
            + "title \"Title\",first_name \"First Name\", "
            + "last_name \"Last Name\",main_email \"E-mail\","
            + "(select group_concat(full_name separator '; ') "
            + "from company c,peoplecompany p where c.company_id=p.company_id and p.people_id=people.people_id "
            + "group by p.people_id) \"COMPANY\","
            //            + "suffix \"Suffix\", greeting \"Greeting\", "
            + "desk_phone \"Desk Phone\", mobile_phone \"Mobile Phone\", "
            + "lastedit_date \"Last Edited\", "
            + "(select initials from user where user_id=people.lastedited_by) \"Editor\" "
            + "from people order by people.lastedit_date desc,people.first_name "
            + GeneralGridPanel.SELECTLIMIT;
    private static HashMap<Integer, Integer> maxWidths = new HashMap<Integer, Integer>();
    public static Integer parentComapnyID = null;

    static {
        maxWidths.put(0, 40);
        maxWidths.put(2, 45);
//        maxWidths.put(4, 80);
//        maxWidths.put(5, 80);
//        maxWidths.put(9, 40);
    }

    public PeopleGrid(IMessageSender exchanger) throws RemoteException {
        super(exchanger, SELECT, maxWidths, false);
        parentComapnyID = null;
    }

    public PeopleGrid(IMessageSender exchanger, String select, boolean readOnly) throws RemoteException {
        super(exchanger, select, maxWidths, readOnly);
        int p = select.indexOf(COMPANY_ID);
        if (p > 0) {
            //System.out.println(select);
            int pp = select.indexOf(")order");
            if (pp > -1) { 
                parentComapnyID = Integer.parseInt(select.substring(p + COMPANY_ID.length(), pp));
                System.out.println("!!!!!!!!!!!!!!!!!parentComapnyID=" + parentComapnyID);
            }
        } else {
            parentComapnyID = null;
        }
        EditPeopleDialog.locationID = null;
    }

    public void filterOnLocationID(Integer locationID) {
        setSelect(SELECT.replace("from people", "from people where location_id="
                + (EditPeopleDialog.locationID = locationID)));
        refresh();
    }

    public void refresh(Integer compID) {
        setSelect(SELECT.replace(GeneralGridPanel.SELECTLIMIT, "").replace("from people ",
                "from people where people_id in (select people_id from peoplecompany where company_id=" + compID + ")"));
        //System.out.println(getSelect());
        refresh();
    }

    protected void additionalSettings() {

    }

    @Override
    protected AbstractAction addAction() {
        return new AbstractAction("Add", new ImageIcon(AIBclient.loadImage("newcontact.png", GeneralGridPanel.class))) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                additionalSettings();
                EditPeopleDialog ed = new EditPeopleDialog("Add Person", null);
                if (EditPeopleDialog.okPressed) {
                    People person = (People) ed.getEditPanel().getDbObject();
                    if(parentComapnyID!=null) {
                        refresh(parentComapnyID);
                    } else
                    refresh();//person.getPeopleId());
                }
            }
        };
    }

    @Override
    protected AbstractAction editAction() {
        return new AbstractAction("Edit", new ImageIcon(AIBclient.loadImage("editcontact.png", GeneralGridPanel.class))) {
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
        return new AbstractAction("Delete", new ImageIcon(AIBclient.loadImage("delcontact.png", GeneralGridPanel.class))) {
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
