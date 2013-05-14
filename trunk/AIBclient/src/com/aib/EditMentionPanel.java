/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import static com.aib.RecordEditPanel.getGridPanel;
import com.aib.lookup.PublicationsListInTextFieldDialog;
import com.aib.orm.Aibpublic;
import com.aib.orm.dbobject.DbObject;
import com.xlend.util.SelectedDateSpinner;
import com.xlend.util.Util;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;

/**
 *
 * @author nick
 */
class EditMentionPanel extends RecordEditPanel {

    private JTextField idField;
    private JTextField publicationTF;
    private JSpinner publicationDateSP;

    public EditMentionPanel(DbObject dbObject) {
        super(dbObject);
    }

    @Override
    protected void fillContent() {
        String titles[] = new String[]{
            "ID:",
            "Publication name:",
            "Publication date:"
        };
        JComponent[] edits = new JComponent[]{
            getGridPanel(idField = new JTextField(), 6),
            publicationTF = new JTextField(),
            getBorderPanel(new JComponent[]{publicationDateSP = new SelectedDateSpinner()})
        };
        idField.setEnabled(false);
        publicationDateSP.setEditor(new JSpinner.DateEditor(publicationDateSP, DD_MM_YYYY));
        Util.addFocusSelectAllAction(publicationDateSP);
        organizePanels(titles, edits, null);
    }

    @Override
    public void loadData() {
        Aibpublic aibPublic = (Aibpublic) getDbObject();
        if (aibPublic != null) {
            idField.setText(aibPublic.getAibpublicId().toString());
            publicationTF.setText(aibPublic.getPublication());
            java.sql.Date dt = aibPublic.getPubDate();
            if (dt != null) {
                publicationDateSP.setValue(new java.util.Date(dt.getTime()));
            }
        } else if (PublicationsListInTextFieldDialog.newPublicationName != null) {
            publicationTF.setText(PublicationsListInTextFieldDialog.newPublicationName);
        }
    }

    @Override
    public boolean save() throws Exception {
        Aibpublic aibPublic = (Aibpublic) getDbObject();
        boolean isNew = false;
        if (aibPublic == null) {
            aibPublic = new Aibpublic(null);
            aibPublic.setAibpublicId(0);
            isNew = true;
        }
        aibPublic.setPublication(publicationTF.getText());
        java.util.Date dt = (java.util.Date) publicationDateSP.getValue();
        if (dt!=null) {
            aibPublic.setPubDate(new java.sql.Date(dt.getTime()));
        }
        return saveDbRecord(aibPublic, isNew);
    }
}
