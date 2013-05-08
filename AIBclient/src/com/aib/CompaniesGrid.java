/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import com.aib.remote.IMessageSender;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.HashMap;
import javax.swing.AbstractAction;

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
//        maxWidths.put(1, 140);
    }

    public CompaniesGrid(IMessageSender exchanger) throws RemoteException {
        super(exchanger, SELECT, maxWidths, false);
    }

    @Override
    protected AbstractAction addAction() {
        return new AbstractAction("Add Company") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                GeneralFrame.notImplementedYet();
            }
        };
    }

    @Override
    protected AbstractAction editAction() {
        return new AbstractAction("Edit Company") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                GeneralFrame.notImplementedYet();
            }
        };
    }

    @Override
    protected AbstractAction delAction() {
        return new AbstractAction("Delete Company") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                GeneralFrame.notImplementedYet();
            }
        };
    }
}
