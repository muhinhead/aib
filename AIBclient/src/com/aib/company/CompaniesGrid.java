/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.company;

import com.aib.AIBclient;
import com.aib.GeneralFrame;
import com.aib.GeneralGridPanel;
import com.aib.orm.Company;
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
public class CompaniesGrid extends GeneralGridPanel {

    public static final String SELECT = "select distinct company.company_id \"ID\","
            + "company.full_name \"Name\", company.abbreviation \"Alternate Name\", "
//            + "(select abbreviation from company c where company_id=company.parent_id) \"Parent\", "
            + "(select country from country where country_id=company.country_id) \"Country\","
            + "company.main_phone \"Main Phone\",company.main_fax \"Main Fax\", "
            + "company.renewal_date \"Renewal Date\", company.verify_date \"Verify Date\", company.lastedit_date \"Last Edited\" "
            + "from company order by lastedit_date desc "+GeneralGridPanel.SELECTLIMIT;
    private static HashMap<Integer, Integer> maxWidths = new HashMap<Integer, Integer>();

    static {
        maxWidths.put(0, 40);
//        maxWidths.put(1, 400);
//        maxWidths.put(2, 400);
    }

    public CompaniesGrid(IMessageSender exchanger) throws RemoteException {
        super(exchanger, SELECT, maxWidths, false);
    }

    public CompaniesGrid(IMessageSender exchanger, String select) throws RemoteException {
        super(exchanger, select, maxWidths, false);
    }
    
    public CompaniesGrid(IMessageSender exchanger, String select, boolean readOnly) throws RemoteException {
        super(exchanger, select, maxWidths, readOnly);
    }
    
    @Override
    protected AbstractAction addAction() {
        return new AbstractAction("Add") {
            @Override
            public void actionPerformed(ActionEvent ae) {
//                try {
                EditCompanyDialog ed = new EditCompanyDialog("Add Company", null);
                if (EditCompanyDialog.okPressed) {
                    Company comp = (Company) ed.getEditPanel().getDbObject();
                    refresh(comp.getCompanyId());
//                        GeneralFrame.updateGrid(exchanger,
//                                getTableView(), getTableDoc(), getSelect(), comp.getCompanyId(),
//                                getPageSelector().getSelectedIndex());
                }
//                } catch (RemoteException ex) {
//                    AIBclient.logAndShowMessage(ex);
//                }
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
                        Company comp = (Company) exchanger.loadDbObjectOnID(Company.class, id);
                        new EditCompanyDialog("Edit Company", comp);
                        if (EditCompanyDialog.okPressed) {
//                            GeneralFrame.updateGrid(exchanger, getTableView(),
//                                    getTableDoc(), getSelect(), id, getPageSelector().getSelectedIndex());
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
                        Company comp = (Company) exchanger.loadDbObjectOnID(Company.class, id);
                        if (comp != null && GeneralFrame.yesNo("Attention!", "Do you want to delete this record?") == JOptionPane.YES_OPTION) {
                            exchanger.deleteObject(comp);
//                            GeneralFrame.updateGrid(exchanger, getTableView(), getTableDoc(),
//                                    getSelect(), null, getPageSelector().getSelectedIndex());
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
