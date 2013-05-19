/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.lookup;

import com.aib.AIBclient;
import com.aib.EditAwardDialog;
import com.aib.orm.Aibaward;
import com.aib.orm.dbobject.DbObject;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import javax.swing.AbstractAction;

/**
 *
 * @author Nick Mukhin
 */
public class AIBawardsListInTextFieldDialog extends ListInTextFieldDialog {

    public static String newAwardName;

    public AIBawardsListInTextFieldDialog(String title, Object[] obs) {
        super(title, obs);
    }

    @Override
    protected String additionalDialog(String name) {
        name = name.trim();
        int p = name.indexOf("(");
        newAwardName = p > 0 ? name.substring(0, p).trim() : name;
        if (AIBclient.awardNotExist(name)) {
            EditAwardDialog ed = new EditAwardDialog("Add new action", null);
            if (EditAwardDialog.okPressed) {
                Aibaward award = (Aibaward) ed.getEditPanel().getDbObject();
                String sdate = new SimpleDateFormat("yyyy-MM-dd").format(award.getAwardDate());
                return award.getAward() + " (" + sdate + ")";
            }
        }
        return name;
    }

    @Override
    protected AbstractAction getEditAction() {
        return new AbstractAction("Edit") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String itm = (String) urlJList.getSelectedValue();
                
                if (itm != null) {
                    int row = urlJList.getSelectedIndex();
                    try {
                        DbObject[] recs = AIBclient.getExchanger().getDbObjects(
                                Aibaward.class, "concat(award,' (',award_date,')')='" + itm + "'", null);
                        if (recs.length > 0) {
                            EditAwardDialog ed = new EditAwardDialog("Edit action", recs[0]);
                            if (EditAwardDialog.okPressed) {
                                Aibaward aw = (Aibaward) ed.getEditPanel().getDbObject();
                                String sdate = new SimpleDateFormat("yyyy-MM-dd").format(aw.getAwardDate());
                                selectedItems.set(row, aw.getAward()+" ("+sdate + ")");
                                urlJList.updateUI();
                            }
                        }
                    } catch (RemoteException ex) {
                        AIBclient.logAndShowMessage(ex);
                    }
                }
            }
        };
    }
}
