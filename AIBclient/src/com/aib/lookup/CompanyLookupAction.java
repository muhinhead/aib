/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.lookup;

import com.aib.AIBclient;
import com.aib.GeneralFrame;
import com.aib.company.CompaniesGrid;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;

/**
 *
 * @author nick
 */
public class CompanyLookupAction extends AbstractAction {

    private final JComboBox companyCB;
    private final boolean readOnly;

    public CompanyLookupAction(JComboBox cb, boolean readOnly) {
        super("...");
        this.companyCB = cb;
        this.readOnly = readOnly;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            String select = "select company_id \"ID\","
                    + "abbreviation \"Abbreviation\",full_name \"Name\", "
                    + "(select country from country where country_id=company.country_id) \"Country\","
                    + "main_phone \"Main phone\",main_fax \"Main fax\" "
                    + "from company";
            LookupDialog ld = new LookupDialog("Company Lookup", companyCB,
                    new CompaniesGrid(AIBclient.getExchanger(), select, readOnly),
                    new String[]{"abbreviation", "full_name", "main_phone", "main_fax"});
        } catch (RemoteException ex) {
            GeneralFrame.errMessageBox("Error:", ex.getMessage());
        }
    }
}
