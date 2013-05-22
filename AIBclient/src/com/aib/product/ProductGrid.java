/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.product;

import com.aib.AIBclient;
import com.aib.GeneralFrame;
import com.aib.GeneralGridPanel;
import com.aib.orm.Peopleproduct;
import com.aib.orm.Product;
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
public class ProductGrid extends GeneralGridPanel {

    private Integer regionID;
    public static final String SELECT = "select product_id \"Id\", descr \"Product\" from product order by descr";
    private static HashMap<Integer, Integer> maxWidths = new HashMap<Integer, Integer>();

    static {
        maxWidths.put(0, 40);
    }

    public ProductGrid(IMessageSender exchanger) throws RemoteException {
        super(exchanger, SELECT, maxWidths, false);
        regionID = null;
    }

    @Override
    protected AbstractAction addAction() {
        return new AbstractAction("Add") {
            @Override
            public void actionPerformed(ActionEvent ae) {
//                try {
                    EditProductDialog ed = new EditProductDialog("Add Product", null);
                    if (EditProductDialog.okPressed) {
                        Product pr = (Product) ed.getEditPanel().getDbObject();
                        refresh();
//                        GeneralFrame.updateGrid(exchanger,
//                                getTableView(), getTableDoc(), getSelect(), pr.getProductId(),
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
                        Product pr = (Product) exchanger.loadDbObjectOnID(Product.class, id);
                        new EditProductDialog("Edit Product", pr);
                        if (EditProductDialog.okPressed) {
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
                        Product pr = (Product) exchanger.loadDbObjectOnID(Product.class, id);
                        if (pr != null && GeneralFrame.yesNo("Attention!", "Do you want to delete this record?") == JOptionPane.YES_OPTION) {
                            exchanger.deleteObject(pr);
                            refresh();
//                            GeneralFrame.updateGrid(exchanger, getTableView(), getTableDoc(),
//                                    getSelect(), null, getPageSelector().getSelectedIndex());
                        }
                    } catch (RemoteException ex) {
                        AIBclient.logAndShowMessage(ex);
                    }
                }
            }
        };
    }
}
