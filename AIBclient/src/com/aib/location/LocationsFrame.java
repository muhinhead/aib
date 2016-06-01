package com.aib.location;

import com.aib.AIBclient;
import com.aib.FilteredListFrame;
import com.aib.GeneralFrame;
import static com.aib.GeneralFrame.adjustFilterQuery;
import com.aib.filter.LocationFilterPanel;
import com.aib.orm.Filter;
import com.aib.orm.dbobject.ComboItem;
import com.aib.remote.IMessageSender;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import javax.swing.AbstractAction;
import javax.swing.JPanel;

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
    protected ActionListener getPrintAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOutputDialog("Location", locationPanel.getSelect());
            }
        };
    }

    @Override
    public void applyFilter(Filter flt) {
        if (flt != null) {
            adjustFilterQuery(flt, "Countries",
                    "exists (select country from country where country.country_id=location.country_id and country");
            adjustFilterQuery(flt, "Links",
                    "exists (select url from link,loclink where link.link_id=loclink.link_id "
                    + "and loclink.location_id=location.location_id and url");
            adjustFilterQuery(flt, "Industries",
                    "exists (select descr from industry,locindustry where industry.industry_id=locindustry.industry_id "
                    + "and locindustry.location_id=location.location_id and descr");
            String newSelect = adjustSelect(flt, "from location ", LocationsGrid.SELECT);
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
                    ComboItem itm = (ComboItem) filtersCB.getSelectedItem();
                    if (itm.getId() < 0) {//filtersCB.getSelectedIndex() == 0) {
                        if (itm.getId() == AIBclient.DUPLICATED) {
                            locationPanel.setSelect(LocationsGrid.SELECT.replace(
                                    "from location order by location.lastedit_date desc,location.name", 
                                    "from location where name in (select name from location group by name having count(*)>1) "
                                            + "order by location.lastedit_date desc,location.name"));
                        } else {
                            locationPanel.setSelect(LocationsGrid.SELECT);
                        }
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
