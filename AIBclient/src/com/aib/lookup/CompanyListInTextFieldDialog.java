/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.lookup;

import com.aib.AIBclient;
import com.aib.company.EditCompanyDialog;
import com.aib.orm.Aibaward;
import com.aib.orm.Company;
import com.aib.orm.dbobject.DbObject;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

/**
 *
 * @author Nick Mukhin
 */
public class CompanyListInTextFieldDialog extends ListInTextFieldDialog {

    public CompanyListInTextFieldDialog(String title, Object[] obs) {
        super(title, obs);
    }

    @Override
    protected void syncTextField() {
        StringBuilder sb = new StringBuilder();
        for (Object itm : selectedItems) {
            String sItm = (String) itm;
            sb.append(sb.length() > 0 ? "," : "");
            int p = sItm.indexOf(" - ");
            if (p > 0) {
                sb.append(sItm.substring(0, p));
            } else {
                sb.append(sItm);
            }
        }
        resultList = sb.toString();
    }

    @Override
    protected String additionalDialog(String nameAndID) {
        nameAndID = nameAndID.trim();
        int p = nameAndID.indexOf(" - ");
        if (p > 0) {
            nameAndID = nameAndID.substring(0, p);
        }
        if (AIBclient.companyNotExists(nameAndID)) {
            EditCompanyDialog ed = new EditCompanyDialog("New Company", null);
            if (ed.okPressed) {
                Company comp = (Company) ed.getEditPanel().getDbObject();
                return comp.getAbbreviation() + " - " + comp.getFullName();
            }
        }
        return nameAndID;
    }

    @Override
    protected AbstractAction getEditAction() {
        return new AbstractAction("Edit",new ImageIcon(AIBclient.loadImage("edit16.png", ListInTextFieldDialog.class))) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String itm = (String) urlJList.getSelectedValue();

                if (itm != null) {
                    int p = itm.indexOf(" - ");
                    if (p > 0) {
                        itm = itm.substring(0, p);
                    }
                    int row = urlJList.getSelectedIndex();
                    try {
                        DbObject[] recs = AIBclient.getExchanger().getDbObjects(
                                Company.class, "concat(full_name,'(',company_id,')')='" + itm + "'", null);
//                                Company.class, "abbreviation='" + itm + "'", null);
                        if (recs.length > 0) {
                            EditCompanyDialog ed = new EditCompanyDialog("Edit Company", recs[0]);
                            if (ed.okPressed) {
                                Company comp = (Company) ed.getEditPanel().getDbObject();
                                selectedItems.set(row, comp.getFullName()+"("+comp.getCompanyId()+")");
//                                selectedItems.set(row, comp.getAbbreviation() + " - " + comp.getFullName());
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

    @Override
    protected void fillJList(List linkList) {
        if (oldList.length() > 0) {
            int p;
            for (Object itm : linkList) {
                p = itm.toString().indexOf(" - ");
                String abb = p > 0 ? itm.toString().substring(0, itm.toString().indexOf(" - ")) : itm.toString();
                if (oldList.indexOf(abb) >= 0) {
                    selectedItems.add(itm);
                }
            }
        }
    }
}
