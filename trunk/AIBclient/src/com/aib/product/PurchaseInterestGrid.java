/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.product;

import com.aib.AIBclient;
import com.aib.GeneralFrame;
import com.aib.GeneralGridPanel;
import com.aib.orm.Peopleinterest;
//import com.aib.orm.Peopleproduct;
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
public class PurchaseInterestGrid extends GeneralGridPanel {

    private static Integer peopleID;
    private static final String SELECT =
            "select pi.peopleinterest_id \"Id\", pr.descr \"Product\", "
            + "ifnull(pi.purchase_date,'unknown') \"Approx.date\", pi.prospecting_level \"Prospecting level\" "
            + "from peopleinterest pi,product pr "
            + "where pr.product_id=pi.product_id and people_id=# "
            + "order by pi.purchase_date";
    private static HashMap<Integer, Integer> maxWidths = new HashMap<Integer, Integer>();

    static {
        maxWidths.put(0, 40);
    }

    public PurchaseInterestGrid(IMessageSender exchanger, Integer byerID) throws RemoteException {
        super(exchanger, SELECT.replaceAll("#", (peopleID = byerID).toString()), maxWidths, false);
    }
    @Override
    protected AbstractAction addAction() {
        return new AbstractAction("Add") {
            @Override
            public void actionPerformed(ActionEvent ae) {

                EditPurchaseInterestDialog.peopleID = peopleID;
                EditPurchaseInterestDialog ed = new EditPurchaseInterestDialog("Add Purchase Interest", null);
                if (EditPurchaseInterestDialog.okPressed) {
                    Peopleinterest pi = (Peopleinterest) ed.getEditPanel().getDbObject();
                    refresh(pi.getPeopleinterestId());
                }
            }
        };
    }

    @Override
    protected AbstractAction editAction() {
        return new AbstractAction("Edit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = getSelectedID();
                if (id != 0) {
                    try {
                        Peopleinterest pi = (Peopleinterest) exchanger.loadDbObjectOnID(Peopleinterest.class, id);
                        EditPurchaseInterestDialog.peopleID = peopleID;
                        new EditPurchaseInterestDialog("Edit Purchase Interest", pi);
                        if (EditPurchaseInterestDialog.okPressed) {
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
            public void actionPerformed(ActionEvent e) {
                int id = getSelectedID();
                if (id != 0) {
                    try {
                        Peopleinterest pi = (Peopleinterest)  exchanger.loadDbObjectOnID(Peopleinterest.class, id);
                        if (pi != null && GeneralFrame.yesNo("Attention!", "Do you want to delete this record?") == JOptionPane.YES_OPTION) {
                            exchanger.deleteObject(pi);
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
