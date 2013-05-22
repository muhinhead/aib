/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.product;

import com.aib.AIBclient;
import com.xlend.util.PopupDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author NIck Mukhin
 */
public class PurchasedProductsDialog extends PopupDialog {
    private JButton closeBtn;
    private AbstractAction closeAction;

    public PurchasedProductsDialog(String title, Object obj) {
        super(null, title, obj);
    }
    
     @Override
    protected Color getHederBackground() {
        return AIBclient.HDR_COLOR;
    }

    @Override
    protected void fillContent() {
        super.fillContent(); 
        Integer peopleID = (Integer) getObject();
        try {
            add(new PurchasedProductsGrid(AIBclient.getExchanger(), peopleID), BorderLayout.CENTER);
        } catch (RemoteException ex) {
            AIBclient.logAndShowMessage(ex);
        }
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(closeBtn = new JButton(closeAction = new AbstractAction("Close"){

            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
        }));
        add(new JPanel(), BorderLayout.WEST);
        add(btnPanel, BorderLayout.SOUTH);
    }    
    
    @Override
    public void freeResources() {
       closeBtn.setAction(null);
       closeAction = null;
    }
    
}
