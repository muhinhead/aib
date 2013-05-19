/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import com.aib.lookup.AIBawardsListInTextFieldDialog;
import com.aib.orm.Aibaward;
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
class EditAwardPanel extends RecordEditPanel {

    private JTextField idField;
    private JTextField awardTF;
    private JSpinner awardDateSP;

    public EditAwardPanel(DbObject dbObject) {
        super(dbObject);
    }

    @Override
    protected void fillContent() {
        String titles[] = new String[]{
            "ID:",
            "Action name:",
            "Action date:"
        };
        JComponent[] edits = new JComponent[]{
            getGridPanel(idField = new JTextField(), 6),
            awardTF = new JTextField(),
            getBorderPanel(new JComponent[]{awardDateSP = new SelectedDateSpinner()})
        };
        idField.setEnabled(false);
        awardDateSP.setEditor(new JSpinner.DateEditor(awardDateSP, DD_MM_YYYY));
        Util.addFocusSelectAllAction(awardDateSP);
        organizePanels(titles, edits, null);
    }

    @Override
    public void loadData() {
        Aibaward award = (Aibaward) getDbObject();
        if (award != null) {
            idField.setText(award.getAibawardId().toString());
            awardTF.setText(award.getAward());
            java.sql.Date dt = award.getAwardDate();
            if (dt != null) {
                awardDateSP.setValue(new java.util.Date(dt.getTime()));
            }
        } else if (AIBawardsListInTextFieldDialog.newAwardName != null) {
            awardTF.setText(AIBawardsListInTextFieldDialog.newAwardName);
        }
    }

    @Override
    public boolean save() throws Exception {
        Aibaward award = (Aibaward) getDbObject();
        boolean isNew = false;
        if (award == null) {
            award = new Aibaward(null);
            award.setAibawardId(0);
            isNew = true;
        }
        award.setAward(awardTF.getText());
        java.util.Date dt = (java.util.Date) awardDateSP.getValue();
        if (dt != null) {
            award.setAwardDate(new java.sql.Date(dt.getTime()));
        }
        return saveDbRecord(award, isNew);
    }
}
