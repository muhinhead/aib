/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.people;

import com.aib.AIBclient;
import com.aib.GeneralFrame;
import com.aib.GeneralGridPanel;
import com.aib.orm.Peoplenote;
import com.aib.remote.IMessageSender;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 *
 * @author Nick Mukhin
 */
public class PeopleCommentsGrid extends GeneralGridPanel {

    private static final String SELECT = "select n.peoplenote_id \"ID\","
            + "to_char(n.note_date,'YYYY-MM-DD') \"Date\",concat(substr(n.comments,1,60),'...') \"Note\", "
            + "to_char(n.lastedit_date,'YYYY-MM-DD HH24:MI') \"Last edited\", u.initials \"Editor\" "
            + "from peoplenote n, user u where u.user_id=n.lastedited_by and n.people_id=# order by n.note_date desc";
    private static HashMap<Integer, Integer> maxWidths = new HashMap<Integer, Integer>();

    static {
        maxWidths.put(0, 40);
        maxWidths.put(1, 100);
        maxWidths.put(2, 300);
        maxWidths.put(3, 130);
        maxWidths.put(4, 40);
    }
    private Integer peopleID;
    private final EditPeoplePanel peoplePanel;

    public PeopleCommentsGrid(IMessageSender exchanger, Integer peopleID, EditPeoplePanel peoplePanel) throws RemoteException {
        super(exchanger, SELECT.replace("#", peopleID == null ? "0" : peopleID.toString()), maxWidths, false);
        this.peoplePanel = peoplePanel;
    }

    private void updateContent(Integer peopleID) {
        this.peopleID = peopleID;
        setSelect(SELECT.replace("#", peopleID == null ? "0" : peopleID.toString()));
        refresh();
    }

    @Override
    protected AbstractAction addAction() {
        return new AbstractAction("Add") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                boolean ok = true;
                if (peopleID == null || peopleID.intValue() == 0) {
                    ok = false;
                    if (GeneralFrame.yesNo("Attention!", 
                            "Do you want to save this person's record before adding dependant info?") == JOptionPane.YES_OPTION) {
                        try {
                            ok = peoplePanel.save();
                            setPeopleID(peoplePanel.getDbObject().getPK_ID());
                        } catch (Exception ex) {
                            AIBclient.logAndShowMessage(ex);
                        }
                    }
                }
                if (ok) {
                    EditPeopleNoteDialog.peopleID = peopleID;
                    EditPeopleNoteDialog ed = new EditPeopleNoteDialog("Add note", null);
                    if (EditPeopleNoteDialog.okPressed) {
                        Peoplenote pn = (Peoplenote) ed.getEditPanel().getDbObject();
                        refresh(pn.getPeoplenoteId());
                    }
                }
            }
        };
    }

    @Override
    protected AbstractAction editAction() {
        return new AbstractAction("Edit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = getSelectedID();
                if (id != 0) {
                    try {
                        Peoplenote pn = (Peoplenote) exchanger.loadDbObjectOnID(Peoplenote.class, id);
                        EditPeopleNoteDialog.peopleID = peopleID;
                        new EditPeopleNoteDialog("Edit note", pn);
                        if (EditPeopleNoteDialog.okPressed) {
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
            public void actionPerformed(ActionEvent e) {
                int id = getSelectedID();
                if (id != 0) {
                    try {
                        Peoplenote pn = (Peoplenote) exchanger.loadDbObjectOnID(Peoplenote.class, id);
                        if (pn != null && GeneralFrame.yesNo("Attention!", 
                                "Do you want to delete this record?") == JOptionPane.YES_OPTION) {
                            exchanger.deleteObject(pn);
                            refresh();
                        }
                    } catch (RemoteException ex) {
                        AIBclient.logAndShowMessage(ex);
                    }
                }
            }
        };
    }

    /**
     * @param peopleID the peopleID to set
     */
    public void setPeopleID(Integer peopleID) {
        updateContent(peopleID);
    }
}
