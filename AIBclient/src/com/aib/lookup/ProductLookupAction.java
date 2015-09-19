/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.lookup;

import com.aib.AIBclient;
import com.aib.GeneralFrame;
import com.aib.orm.dbobject.ComboItem;
import com.aib.product.ProductGrid;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;

/**
 *
 * @author Nick Mukhin
 */
public class ProductLookupAction extends AbstractAction {

    private final JComboBox productCB;

    public ProductLookupAction(JComboBox cb) {
        super(null,new ImageIcon(AIBclient.loadImage("lookup.png", ProductLookupAction.class)));
        this.productCB = cb;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            ComboItem citm = (ComboItem) productCB.getSelectedItem();
            LookupDialog ld = new LookupDialog("Product Lookup", productCB,
                    new ProductGrid(AIBclient.getExchanger()),
                    new String[]{"descr"});
        } catch (RemoteException ex) {
            GeneralFrame.errMessageBox("Error:", ex.getMessage());
        }
    }

}
