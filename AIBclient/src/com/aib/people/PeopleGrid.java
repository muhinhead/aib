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
 * @author nick
 */
public class PeopleGrid extends GeneralGridPanel {

    private static final String SELECT = "select people_id \"ID\","
            + "title \"Title\",first_name \"First Name\", "
            + "last_name \"Last Name\",suffix \"Suffix\", greeting \"Greeting\", "
            + "desk_phone \"Desk Phone\", mobile_phone \"Mobile Phone\", lastedit_date \"Last Edited\" "
            + "from people";
    private static HashMap<Integer, Integer> maxWidths = new HashMap<Integer, Integer>();

    static {
        maxWidths.put(0, 40);
        maxWidths.put(1, 80);
        maxWidths.put(4, 80);
        maxWidths.put(5, 80);
    }

    public PeopleGrid(IMessageSender exchanger) throws RemoteException {
        super(exchanger, SELECT, maxWidths, false);
    }

    protected void additionalSettings() {
    }

    @Override
    protected AbstractAction addAction() {
        return new AbstractAction("Add Person") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    additionalSettings();
                    EditPeopleDialog ed = new EditPeopleDialog("Add Person", null);
                    if (EditPeopleDialog.okPressed) {
                        People person = (People) ed.getEditPanel().getDbObject();
                        GeneralFrame.updateGrid(exchanger,
                                getTableView(), getTableDoc(), getSelect(), person.getPeopleId(),
                                getPageSelector().getSelectedIndex());
                    }
                } catch (RemoteException ex) {
                    AIBclient.logAndShowMessage(ex);
                }
            }
        };
    }

    @Override
    protected AbstractAction editAction() {
        return new AbstractAction("Edit Person") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int id = getSelectedID();
                if (id != 0) {
                    try {
                        People person = (People) exchanger.loadDbObjectOnID(People.class, id);
                        additionalSettings();
                        new EditPeopleDialog("Edit Person", person);
                        if (EditPeopleDialog.okPressed) {
                            GeneralFrame.updateGrid(exchanger, getTableView(),
                                    getTableDoc(), getSelect(), id, getPageSelector().getSelectedIndex());
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
        return new AbstractAction("Delete Person") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int id = getSelectedID();
                if (id != 0) {
                    try {
                        People person = (People) exchanger.loadDbObjectOnID(People.class, id);
                        if (person != null && GeneralFrame.yesNo("Attention!", "Do you want to delete this record?") == JOptionPane.YES_OPTION) {
                            exchanger.deleteObject(person);
                            GeneralFrame.updateGrid(exchanger, getTableView(), getTableDoc(),
                                    getSelect(), null, getPageSelector().getSelectedIndex());
                        }
                    } catch (RemoteException ex) {
                        AIBclient.logAndShowMessage(ex);
                    }
                }
            }
        };
    }
}
