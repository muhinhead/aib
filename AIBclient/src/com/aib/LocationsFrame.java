package com.aib;

import com.aib.filter.LocationFilterPanel;
import com.aib.filter.PeopleFilterPanel;
import com.aib.location.LocationsGrid;
import com.aib.orm.Filter;
import com.aib.orm.dbobject.ComboItem;
import com.aib.people.PeopleGrid;
import com.aib.remote.IMessageSender;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import javax.swing.AbstractAction;
import javax.swing.JPanel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nick Mukhin
 */
public class LocationsFrame extends FilteredListFrame {

    private static String[] sheetList = new String[]{
        "List", "Filter"
    };
    private LocationsGrid locationPanel;

    public LocationsFrame(IMessageSender exch) {
        super("Locations", exch);
    }

    @Override
    protected JPanel getListPanel() {
        if (locationPanel == null) {
            try {
                registerGrid(locationPanel = new LocationsGrid(getExchanger()));
            } catch (RemoteException ex) {
                AIBclient.logAndShowMessage(ex);
            }
        }
        return locationPanel;
    }

    @Override
    protected JPanel getFilterPanel() {
        if (filterPanel == null) {
            filterPanel = new JPanel(new BorderLayout());
            filterPanel.add(new LocationFilterPanel(this));
        }
        return filterPanel;
    }

    @Override
    public void applyFilter(Filter flt) {
        if (flt != null) {
            final String FROM_LOCATION = "from location l, company c where l.company_id=c.company_id ";
            String newSelect = null;
            int p = LocationsGrid.SELECT.indexOf(FROM_LOCATION);
            String orderBy = LocationsGrid.SELECT.substring(p + FROM_LOCATION.length());
            if (flt.getIsComplex() != null && flt.getIsComplex().intValue() == 1) {
                newSelect = LocationsGrid.SELECT.substring(0, p + FROM_LOCATION.length())
                        + " and (" + flt.getQuery().replaceAll("==", "=") + ") " + orderBy;
            }
            if (flt.getQuery() == null || flt.getQuery().trim().length() == 0) {
                GeneralFrame.errMessageBox("Attention!", "The empty filter couldn't be applied");
            } else {
                locationPanel.setSelect(newSelect);
                locationPanel.refresh();
                gotoFilterApplied(flt.getFilterId().intValue());
            }
        }
    }

    @Override
    protected String getMainTableName() {
        return "location";
    }

    @Override
    protected ActionListener getChooseFilterAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!dontFilter) {
                    if (filtersCB.getSelectedIndex() == 0) {
                        locationPanel.setSelect(LocationsGrid.SELECT);
                    } else {
                        ComboItem ci = (ComboItem) filtersCB.getSelectedItem();
                        try {
                            Filter flt = (Filter) AIBclient.getExchanger().loadDbObjectOnID(Filter.class, ci.getId());
                            if (flt != null) {
                                applyFilter(flt);
                            }
                        } catch (RemoteException ex) {
                            AIBclient.logAndShowMessage(ex);
                            filtersCB.setSelectedIndex(0);
                        }
                    }
                }
                locationPanel.refresh();
            }
        };
    }
}