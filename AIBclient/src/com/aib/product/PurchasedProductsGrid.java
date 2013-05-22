/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.product;

import com.aib.AIBclient;
import com.aib.GeneralFrame;
import com.aib.GeneralGridPanel;
import com.aib.orm.Peopleproduct;
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
public class PurchasedProductsGrid extends GeneralGridPanel {

    private static Integer purchaserID;
    private static final String SELECT =
            "select pp.peopleproduct_id \"ID\", pp.purchase_date \"Date\", p.descr \"Product\" "
            + "from peopleproduct pp,product p "
            + "where p.product_id=pp.product_id and pp.people_id=# "
            + "order by pp.purchase_date desc";
    private static HashMap<Integer, Integer> maxWidths = new HashMap<Integer, Integer>();

    static {
        maxWidths.put(0, 40);
    }

    public PurchasedProductsGrid(IMessageSender exchanger, Integer peopleID) throws RemoteException {
        super(exchanger, SELECT.replaceAll("#", (purchaserID = peopleID).toString()), maxWidths, false);
    }

    @Override
    protected AbstractAction addAction() {
        return new AbstractAction("Add") {
            @Override
            public void actionPerformed(ActionEvent ae) {
//                try {
                    EditPurchaseDialog.peopleID = purchaserID;
                    EditPurchaseDialog ed = new EditPurchaseDialog("Add Purchase", null);
                    if (EditPurchaseDialog.okPressed) {
                        Peopleproduct pp = (Peopleproduct) ed.getEditPanel().getDbObject();
                        refresh(pp.getPeopleproductId());
//                        GeneralFrame.updateGrid(exchanger,
//                                getTableView(), getTableDoc(), getSelect(), pp.getPeopleproductId(),
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
            public void actionPerformed(ActionEvent e) {
                int id = getSelectedID();
                if (id != 0) {
                    try {
                        Peopleproduct pp = (Peopleproduct) exchanger.loadDbObjectOnID(Peopleproduct.class, id);
                        EditPurchaseDialog.peopleID = purchaserID;
                        new EditPurchaseDialog("Edit Purchase", pp);
                        if (EditPurchaseDialog.okPressed) {
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
            public void actionPerformed(ActionEvent e) {
                int id = getSelectedID();
                if (id != 0) {
                    try {
                        Peopleproduct pp = (Peopleproduct) exchanger.loadDbObjectOnID(Peopleproduct.class, id);
                        if (pp != null && GeneralFrame.yesNo("Attention!", "Do you want to delete this record?") == JOptionPane.YES_OPTION) {
                            exchanger.deleteObject(pp);
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
