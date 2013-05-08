/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import com.aib.remote.IMessageSender;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.HashMap;
import javax.swing.AbstractAction;

/**
 *
 * @author nick
 */
public class LocationsGrid extends GeneralGridPanel {

    private static final String SELECT = "select location_id \"ID\","
            + "name \"Name\", abbreviation \"Abbreviation\", country_id \"Country\","
            + "main_phone \"Main phone\",main_fax \"Main fax\", lastedit_date \"Last Edited\" "
            + "from location";
    
    private static HashMap<Integer, Integer> maxWidths = new HashMap<Integer, Integer>();

    static {
        maxWidths.put(0, 40);
//        maxWidths.put(1, 140);
    }

    public LocationsGrid(IMessageSender exchanger) throws RemoteException {
        super(exchanger, SELECT, maxWidths, false);
    }

    @Override
    protected AbstractAction addAction() {
        return new AbstractAction("Add Location") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                GeneralFrame.notImplementedYet();
            }
        };
    }

    @Override
    protected AbstractAction editAction() {
        return new AbstractAction("Edit Location") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                GeneralFrame.notImplementedYet();
            }
        };
    }

    @Override
    protected AbstractAction delAction() {
        return new AbstractAction("Delete Location") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                GeneralFrame.notImplementedYet();
            }
        };
    }
}
