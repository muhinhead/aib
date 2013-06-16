/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.lookup;

import com.aib.AIBclient;
import com.aib.GeneralFrame;
import com.aib.orm.dbobject.ComboItem;
import com.aib.admin.UsersGrid;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;

/**
 *
 * @author Nick Mukhin
 */
public class UserLookupAction extends AbstractAction {

    private final JComboBox userCB;

    public UserLookupAction(JComboBox cb) {
        super("...");
        this.userCB = cb;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
       try {
            ComboItem citm = (ComboItem) userCB.getSelectedItem();
            LookupDialog ld = new LookupDialog("User Lookup", userCB,
                    new UsersGrid(AIBclient.getExchanger()), new String[]{"first_name","last_name","initials", "login"});
        } catch (RemoteException ex) {
            GeneralFrame.errMessageBox("Error:", ex.getMessage());
        }
    }
}
