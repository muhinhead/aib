/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.lookup;

import com.aib.AIBclient;
import com.aib.GeneralFrame;
import com.aib.location.LocationsGrid;
import com.aib.orm.dbobject.ComboItem;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;

/**
 *
 * @author Nick Mukhin
 */
public class LocationLookupAction extends AbstractAction {

    private final JComboBox locationCB;

    public LocationLookupAction(JComboBox cb) {
        super("...");
        this.locationCB = cb;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
       try {
            ComboItem citm = (ComboItem) locationCB.getSelectedItem();
            LookupDialog ld = new LookupDialog("Location Lookup", locationCB,
                    new LocationsGrid(AIBclient.getExchanger(), 
                    "select location_id \"Id\",name \"Location\","
                    + "(Select abbreviation from company where company_id=l.company_id) \"Company\" "
                    + "from location l order by name"
                    ), new String[]{"name","c.abbreviation"});
        } catch (RemoteException ex) {
            GeneralFrame.errMessageBox("Error:", ex.getMessage());
        }
    }
}
