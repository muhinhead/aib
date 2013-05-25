/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.people;

import com.aib.AIBclient;
import com.aib.RecordEditPanel;
import static com.aib.RecordEditPanel.getBorderPanel;
import static com.aib.RecordEditPanel.getGridPanel;
import com.aib.orm.Peoplenote;
import com.aib.orm.User;
import com.aib.orm.dbobject.DbObject;
import com.xlend.util.SelectedDateSpinner;
import com.xlend.util.Util;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Nick Mukhin
 */
class EditPeopleNotePanel extends RecordEditPanel {

    private JTextField idField;
    private JSpinner noteDateSP;
    private JTextField lastEditorTF;
    private JSpinner lastEditedSP;
    private JTextArea commentsTA;

    public EditPeopleNotePanel(DbObject dbObject) {
        super(dbObject);
    }

    @Override
    protected void fillContent() {
        String titles[] = new String[]{
            "ID:",
            "Date:",
            "Last edited:",
            "Edited by:"
        };
        JComponent[] edits = new JComponent[]{
            getGridPanel(idField = new JTextField(), 6),
            getBorderPanel(new JComponent[]{noteDateSP = new SelectedDateSpinner()}),
            getBorderPanel(new JComponent[]{
                lastEditedSP = new SelectedDateSpinner()
            }),
            getBorderPanel(new JComponent[]{
                lastEditorTF = new JTextField(2)
            })
        };
        idField.setEnabled(false);
        lastEditedSP.setEnabled(false);
        lastEditorTF.setEnabled(false);
        for (JSpinner sp : new JSpinner[]{lastEditedSP, noteDateSP}) {
            sp.setEditor(new JSpinner.DateEditor(sp, DD_MM_YYYY));
            Util.addFocusSelectAllAction(sp);
        }
        organizePanels(titles, edits, null);
        JScrollPane spComments = new JScrollPane(
                commentsTA = new JTextArea(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        spComments.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Comments"));
        spComments.setPreferredSize(new Dimension(400, 200));
        add(spComments, BorderLayout.CENTER);
    }

    @Override
    public void loadData() {
        Peoplenote pn = (Peoplenote) getDbObject();
        if (pn != null) {
            idField.setText(pn.getPeoplenoteId().toString());
            noteDateSP.setValue(new java.util.Date(pn.getNoteDate().getTime()));
            commentsTA.setText(pn.getComments());
            if (pn.getLasteditDate() != null) {
                Timestamp t = pn.getLasteditDate();
                lastEditedSP.setValue(new java.util.Date(t.getTime()));
            }
            Integer userID = pn.getLasteditedBy();
            if (userID != null) {
                try {
                    User user = (User) AIBclient.getExchanger().loadDbObjectOnID(User.class, userID);
                    lastEditorTF.setText(user.getInitials());
                } catch (RemoteException ex) {
                    AIBclient.log(ex);
                }
            }
        }
    }

    @Override
    public boolean save() throws Exception {
        boolean isNew = false;
        Peoplenote pn = (Peoplenote) getDbObject();
        if (pn == null) {
            pn = new Peoplenote(null);
            pn.setPeoplenoteId(0);
            isNew = true;
        }
        pn.setComments(commentsTA.getText());
        pn.setPeopleId(EditPeopleNoteDialog.peopleID);
        Date dt = Calendar.getInstance().getTime();
        pn.setLasteditDate(new java.sql.Timestamp(dt.getTime()));
        dt = (Date) noteDateSP.getValue();
        pn.setNoteDate(new java.sql.Date(dt.getTime()));
        pn.setLasteditedBy(AIBclient.getCurrentUser().getUserId());
        return saveDbRecord(pn, isNew);
    }
}
