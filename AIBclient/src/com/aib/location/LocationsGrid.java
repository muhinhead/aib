/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.location;

import com.aib.AIBclient;
import com.aib.GeneralFrame;
import com.aib.GeneralGridPanel;
import com.aib.orm.Location;
import com.aib.remote.IMessageSender;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Nick Mukhin
 */
public class LocationsGrid extends GeneralGridPanel {

    public static final String SELECT = "select location.location_id \"ID\", "
            + "(select full_name from company where company_id=location.company_id) \"Company\","
            + "location.name \"Location Name\", location.abbreviation \"Alternate Name\","
            + "(select country from country where country_id=location.country_id) \"Country\","
            + "location.main_phone \"Main phone\", location.main_fax \"Main fax\", "
            + "location.lastedit_date \"Last Edited\" "
            + "from location order by location.lastedit_date desc,location.name "+GeneralGridPanel.SELECTLIMIT;
    private static HashMap<Integer, Integer> maxWidths = new HashMap<Integer, Integer>();

    static {
        maxWidths.put(0, 40);
    }

    public LocationsGrid(IMessageSender exchanger) throws RemoteException {
        super(exchanger, SELECT, maxWidths, false);
    }

    public LocationsGrid(IMessageSender exchanger, String select) throws RemoteException {
        super(exchanger, select, maxWidths, false);
    }

    protected void additionalSettings() {
        EditLocationDialog.companyID = null;
    }

    @Override
    protected AbstractAction addAction() {
        return new AbstractAction("Add ",new ImageIcon(AIBclient.loadImage("newdocument.png", GeneralGridPanel.class))) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                additionalSettings();
                EditLocationDialog ed = new EditLocationDialog("Add Location", null);
                if (EditLocationDialog.okPressed) {
                    Location loc = (Location) ed.getEditPanel().getDbObject();
                    refresh(loc.getLocationId());
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            AIBclient.reloadLocations(null);
                        }
                    });
                }
            }
        };
    }

    @Override
    protected AbstractAction editAction() {
        return new AbstractAction("Edit",new ImageIcon(AIBclient.loadImage("edit.png", GeneralGridPanel.class))) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int id = getSelectedID();
                if (id != 0) {
                    try {
                        Location loc = (Location) exchanger.loadDbObjectOnID(Location.class, id);
                        additionalSettings();
                        new EditLocationDialog("Edit Location", loc);
                        if (EditLocationDialog.okPressed) {
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
        return new AbstractAction("Delete",new ImageIcon(AIBclient.loadImage("trash.png", GeneralGridPanel.class))) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int id = getSelectedID();
                if (id != 0) {
                    try {
                        Location loc = (Location) exchanger.loadDbObjectOnID(Location.class, id);
                        if (loc != null && GeneralFrame.yesNo("Attention!", 
                                "Do you want to delete this record?") == JOptionPane.YES_OPTION) {
                            exchanger.deleteObject(loc);
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
