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
public class PeopleGrid extends GeneralGridPanel {

    private static final String SELECT = "select people_id \"ID\","
            + "title \"Title\",first_name \"First Name\", "
            + "last_name \"Last Name\",suffix \"Suffix\", greeting \"Greeting\", "
            + "desk_phone \"Desk Phone\", mobile_phone \"Mobile Phone\", lastedit_date \"Last Edited\" "
            + "from people";
    
    private static HashMap<Integer, Integer> maxWidths = new HashMap<Integer, Integer>();

    static {
        maxWidths.put(0, 40);
        maxWidths.put(1, 80);
        maxWidths.put(4, 80);
        maxWidths.put(5, 80);
    }

    public PeopleGrid(IMessageSender exchanger) throws RemoteException {
        super(exchanger, SELECT, maxWidths, false);
    }

    @Override
    protected AbstractAction addAction() {
        return new AbstractAction("Add Person") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                GeneralFrame.notImplementedYet();
            }
        };
    }

    @Override
    protected AbstractAction editAction() {
        return new AbstractAction("Edit Person") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                GeneralFrame.notImplementedYet();
            }
        };
    }

    @Override
    protected AbstractAction delAction() {
        return new AbstractAction("Delete Person") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                GeneralFrame.notImplementedYet();
            }
        };
    }
}
