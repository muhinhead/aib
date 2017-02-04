/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.lookup;

import com.aib.AIBclient;
import com.aib.GeneralFrame;
import com.aib.GeneralGridPanel;
import com.aib.location.LocationsGrid;
import com.aib.orm.Location;
import com.aib.orm.dbobject.ComboItem;
import com.aib.people.EditPeoplePanel;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 *
 * @author Nick Mukhin
 */
public class LocationLookupAction extends AbstractAction {

    private JComboBox locationCB = null;

    private class LookupDialogWithMailPickup extends LookupDialog {

        public LookupDialogWithMailPickup(String title, JComboBox cb,GeneralGridPanel grid, String[] filteredColumns) {
            super(title, cb, grid, filteredColumns);
        }

        protected void fillAdditionalButtons(JPanel buttonPanel) {
            JButton btn;
            buttonPanel.add(btn = new JButton(new AbstractAction("Copy location address"/*, new ImageIcon(AIBclient.loadImage("copy.png", LookupDialog.class))*/) {

                @Override
                public void actionPerformed(ActionEvent e) {
                    ComboItem citm = (ComboItem) locationCB.getSelectedItem();
                    try {
                        Location l = (Location) AIBclient.getExchanger().loadDbObjectOnID(Location.class, citm.getId());
                        if (l != null) {
                            EditPeoplePanel.instance.getSpecAddressTF().setText(l.getAddress());
                            EditPeoplePanel.instance.getMailingAddressTA().setText(l.getMailaddress());
                            EditPeoplePanel.instance.getMailingPostCodeTF().setText(l.getMailpostcode());
                            EditPeoplePanel.instance.getDeskPhoneTF().setText(l.getMainPhone());
                            EditPeoplePanel.instance.getDeskFaxTF().setText(l.getMainFax());
                        }
                    } catch (RemoteException ex) {
                        AIBclient.logAndShowMessage(ex);
                    }
                }
            }));
            btn.setToolTipText("Copy location's address to the person record");
        }
    }

    public LocationLookupAction(JComboBox cb) {
        super(null, new ImageIcon(AIBclient.loadImage("lookup.png", LocationLookupAction.class)));
        this.locationCB = cb;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            ComboItem citm = (ComboItem) locationCB.getSelectedItem();
            LookupDialog ld = new LookupDialogWithMailPickup("Location Lookup", locationCB, new LocationsGrid(AIBclient.getExchanger(),
                            "select location_id \"Id\",name \"Location\","
                            + "(Select abbreviation from company where company_id=l.company_id) \"Company\" "
                            + "from location l order by name"
                    ), new String[]{"name"});
        } catch (RemoteException ex) {
            GeneralFrame.errMessageBox("Error:", ex.getMessage());
        }
    }
}
