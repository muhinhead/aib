/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.lookup;

import com.aib.AIBclient;
import com.aib.GeneralFrame;
import com.aib.company.CompaniesGrid;
import com.xlend.util.Java2sAutoComboBox;
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
    private final String column;
    private final String value;

    /**
     *
     * @param cb
     * @param readOnly
     */
    public CompanyLookupAction(JComboBox cb, boolean readOnly) {
        super("...");
        this.companyCB = cb;
        this.readOnly = readOnly;
        this.column = this.value = null;
    }

    public CompanyLookupAction(Java2sAutoComboBox cb, String column, String value) {
        super("...");
        this.companyCB = cb;
        this.readOnly = true;
        this.column = column;
        this.value = value;
        try {
            String select = "select " + (column != null && value != null ? "0 \"ID\",'NEW' as \"Name\",'RECORD' as \"Abbreviation\",' ' as \"Country\","
                    + "' ' as \"Main phone\",' ' as \"Main fax\" union select " : "")
                    + "company_id \"ID\","
                    + "full_name \"Name\",abbreviation \"Alternate Name\", "
                    + "(select country from country where country_id=company.country_id) \"Country\","
                    + "main_phone \"Main phone\",main_fax \"Main fax\" "
                    + "from company " + (column != null && value != null ? "where upper(" + column + ") like upper('" + value.trim() + "%')" : "");
            LookupDialog ld = new LookupDialog((column != null && value != null ? "Select old or new record" : "Company Lookup"),companyCB,
                    new CompaniesGrid(AIBclient.getExchanger(), select, readOnly),
                    new String[]{"abbreviation", "full_name", "main_phone", "main_fax"});
        } catch (RemoteException ex) {
            GeneralFrame.errMessageBox("Error:", ex.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            String select = "select company_id \"ID\","
                    + "full_name \"Name\",abbreviation \"Alternate Name\", "
                    + "(select country from country where country_id=company.country_id) \"Country\","
                    + "main_phone \"Main phone\",main_fax \"Main fax\" "
                    + "from company " + (column != null && value != null ? "where upper(" + column + ") like upper('" + value.trim() + "%')" : "");
            LookupDialog ld = new LookupDialog("Company Lookup", companyCB,
                    new CompaniesGrid(AIBclient.getExchanger(), select, readOnly),
                    new String[]{"abbreviation", "full_name", "main_phone", "main_fax"});
        } catch (RemoteException ex) {
            GeneralFrame.errMessageBox("Error:", ex.getMessage());
        }
    }
}
