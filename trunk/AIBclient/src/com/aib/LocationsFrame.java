package com.aib;

import com.aib.FilteredListFrame;
import com.aib.remote.IMessageSender;
import java.rmi.RemoteException;
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
        return new JPanel(); //TODO!
    }
}