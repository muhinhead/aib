/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.lookup;

import com.aib.AIBclient;
import com.aib.GeneralFrame;
import com.aib.orm.dbobject.ComboItem;
import com.aib.people.PeopleGrid;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;

/**
 *
 * @author nick
 */
public class PeopleLookupAction extends AbstractAction {

    private final JComboBox locationCB;

    public PeopleLookupAction(JComboBox cb) {
        super("...");
        this.locationCB = cb;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            ComboItem citm = (ComboItem) locationCB.getSelectedItem();
            LookupDialog ld = new LookupDialog("People Lookup", locationCB,
                    new PeopleGrid(AIBclient.getExchanger(), "select people_id \"ID\","
                    + "title \"Title\",first_name \"First Name\", "
                    + "last_name \"Last Name\",suffix \"Suffix\", greeting \"Greeting\" "
                    + "from people"),
                    new String[]{"title", "first_name", "last_name", "suffix", "greeting"});
        } catch (RemoteException ex) {
            GeneralFrame.errMessageBox("Error:", ex.getMessage());
        }
    }
}