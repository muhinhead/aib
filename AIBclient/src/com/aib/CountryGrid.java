/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import com.aib.orm.Country;
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
public class CountryGrid extends GeneralGridPanel {

    private Integer regionID;
    public static final String SELECT =
            "select country_id \"Id\", "
            + "country \"Country name\", shortname \"Short name\" from country where worldregion_id=# order by country";
    private static HashMap<Integer, Integer> maxWidths = new HashMap<Integer, Integer>();

    static {
        maxWidths.put(0, 40);
    }

    public CountryGrid(IMessageSender exchanger) throws RemoteException {
        super(exchanger, SELECT.replace("where worldregion_id=#", ""), maxWidths, false);
        regionID = null;
    }

    public CountryGrid(IMessageSender exchanger, int reg_id) throws RemoteException {
        super(exchanger, SELECT.replace("#", "" + reg_id), maxWidths, false);
        regionID = new Integer(reg_id);
    }

    @Override
    protected AbstractAction addAction() {
        return new AbstractAction("Add") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    EditCountryDialog.regionID = regionID;
                    EditCountryDialog ed = new EditCountryDialog("Add Country", null);
                    if (EditCountryDialog.okPressed) {
                        AIBclient.clearRegionsAndCountries();
                        Country country = (Country) ed.getEditPanel().getDbObject();
                        GeneralFrame.updateGrid(exchanger,
                                getTableView(), getTableDoc(), getSelect(), country.getWorldregionId(),
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
        return new AbstractAction("Edit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = getSelectedID();
                if (id != 0) {
                    try {
                        Country country = (Country) exchanger.loadDbObjectOnID(Country.class, id);
                        EditCountryDialog.regionID = regionID;
                        new EditCountryDialog("Edit Country", country);
                        if (EditCountryDialog.okPressed) {
                            AIBclient.clearRegionsAndCountries();
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
        return new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = getSelectedID();
                if (id != 0) {
                    try {
                        Country country = (Country) exchanger.loadDbObjectOnID(Country.class, id);
                        if (country != null && GeneralFrame.yesNo("Attention!", "Do you want to delete record?") == JOptionPane.YES_OPTION) {
                            AIBclient.clearRegionsAndCountries();
                            exchanger.deleteObject(country);
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
