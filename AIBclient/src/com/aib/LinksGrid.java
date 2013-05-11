/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import com.aib.remote.IMessageSender;
import java.rmi.RemoteException;
import java.util.HashMap;
import javax.swing.AbstractAction;

/**
 *
 * @author Nick Mukhin
 */
public class LinksGrid extends GeneralGridPanel {

    private static final String SELECT = "select link_id \"Id\", url from link order by url";
    
    private static HashMap<Integer, Integer> maxWidths = new HashMap<Integer, Integer>();

    static {
        maxWidths.put(0, 40);
    }

    public LinksGrid(IMessageSender exchanger) throws RemoteException {
        super(exchanger, SELECT, maxWidths, false);
    }

    @Override
    protected AbstractAction addAction() {
        return null;
    }

    @Override
    protected AbstractAction editAction() {
        return null;
    }

    @Override
    protected AbstractAction delAction() {
        return null;
    }
}
