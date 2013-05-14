/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import com.aib.orm.Company;
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
public class CompaniesGrid extends GeneralGridPanel {

    private static final String SELECT = "select company_id \"ID\","
            + "abbreviation \"Abbreviation\",full_name \"Name\", "
            + "main_phone \"Main phone\",main_fax \"Main fax\", "
            + "renewal_date \"Renewal Date\", verify_date \"Verify Date\", lastedit_date \"Last Edited\" "
            + "from company";
    
    private static HashMap<Integer, Integer> maxWidths = new HashMap<Integer, Integer>();

    static {
        maxWidths.put(0, 40);
    }

    public CompaniesGrid(IMessageSender exchanger) throws RemoteException {
        super(exchanger, SELECT, maxWidths, false);
    }

    @Override
    protected AbstractAction addAction() {
        return new AbstractAction("Add Company") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    EditCompanyDialog ed = new EditCompanyDialog("Add Company", null);
                    if (EditCompanyDialog.okPressed) {
                        Company comp = (Company)ed.getEditPanel().getDbObject();
                        GeneralFrame.updateGrid(exchanger,
                                getTableView(), getTableDoc(), getSelect(), comp.getCompanyId(), 
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
        return new AbstractAction("Edit Company") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                int id = getSelectedID();
                try {
                    Company comp = (Company) exchanger.loadDbObjectOnID(Company.class, id);
                    new EditCompanyDialog("Edit Company", comp);
                    if (EditCompanyDialog.okPressed) {
                        GeneralFrame.updateGrid(exchanger, getTableView(),
                                getTableDoc(), getSelect(), id, getPageSelector().getSelectedIndex());
                    }
                } catch (RemoteException ex) {
                    AIBclient.logAndShowMessage(ex);
                }
            }
        };
    }

    @Override
    protected AbstractAction delAction() {
        return new AbstractAction("Delete Company") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                int id = getSelectedID();
                try {
                    Company comp = (Company) exchanger.loadDbObjectOnID(Company.class, id);
                    if (comp != null && GeneralFrame.yesNo("Attention!", "Do you want to delete record?") == JOptionPane.YES_OPTION) {
                        exchanger.deleteObject(comp);
                        GeneralFrame.updateGrid(exchanger, getTableView(), getTableDoc(),
                                getSelect(), null, getPageSelector().getSelectedIndex());
                    }
                } catch (RemoteException ex) {
                    AIBclient.logAndShowMessage(ex);
                }
            }
        };
    }
}
