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
import javax.swing.JOptionPane;

/**
 *
 * @author nick
 */
public class LocationsGrid extends GeneralGridPanel {

    protected static final String SELECT = "select l.location_id \"ID\", "
            + "c.full_name \"Company\","
            + "l.name \"Location\", l.abbreviation \"Abbreviation\","
            + "(select country from country where country_id=l.country_id) \"Country\","
            + "l.main_phone \"Main phone\", l.main_fax \"Main fax\", "
            + "l.lastedit_date \"Last Edited\" "
            + "from location l, company c where l.company_id=c.company_id "
            + "order by c.full_name,l.name";
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
        return new AbstractAction("Add Location") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    additionalSettings();
                    EditLocationDialog ed = new EditLocationDialog("Add Location", null);
                    if (EditLocationDialog.okPressed) {
                        Location loc = (Location) ed.getEditPanel().getDbObject();
                        GeneralFrame.updateGrid(exchanger,
                                getTableView(), getTableDoc(), getSelect(), loc.getCompanyId(),
                                getPageSelector().getSelectedIndex());
                    }
                } catch (RemoteException ex) {
                    AIBclient.logAndShowMessage(ex);
                }
            }
        };
    }

    @Override
    protected AbstractAction editAction() {
        return new AbstractAction("Edit Location") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int id = getSelectedID();
                if (id != 0) {
                    try {
                        Location loc = (Location) exchanger.loadDbObjectOnID(Location.class, id);
                        additionalSettings();
                        new EditLocationDialog("Edit Location", loc);
                        if (EditLocationDialog.okPressed) {
                            GeneralFrame.updateGrid(exchanger, getTableView(),
                                    getTableDoc(), getSelect(), id, getPageSelector().getSelectedIndex());
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
        return new AbstractAction("Add Location") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int id = getSelectedID();
                if (id != 0) {
                    try {
                        Location loc = (Location) exchanger.loadDbObjectOnID(Location.class, id);
                        if (loc != null && GeneralFrame.yesNo("Attention!", "Do you want to delete this record?") == JOptionPane.YES_OPTION) {
                            exchanger.deleteObject(loc);
                            GeneralFrame.updateGrid(exchanger, getTableView(), getTableDoc(),
                                    getSelect(), null, getPageSelector().getSelectedIndex());
                        }
                    } catch (RemoteException ex) {
                        AIBclient.logAndShowMessage(ex);
                    }
                }
            }
        };
    }
}
