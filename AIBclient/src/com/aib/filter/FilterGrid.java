/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.filter;

import com.aib.GeneralGridPanel;
import com.aib.remote.IMessageSender;
import com.xlend.mvc.dbtable.DbTableView;
import java.rmi.RemoteException;
import java.util.HashMap;
import javax.swing.AbstractAction;

/**
 *
 * @author Nick Mukhin
 */
public class FilterGrid extends GeneralGridPanel {

    
    private static final String SELECT =
            "select filter_id \"ID\", name \"Name\", initials \"Owner\" "
            + "from filter,user where owner_id=user_id and tablename='@'";
    private static HashMap<Integer, Integer> maxWidths = new HashMap<Integer, Integer>();

    static {
        maxWidths.put(0, 40);
        maxWidths.put(2, 60);
    }

    public FilterGrid(IMessageSender exchanger, String tabName, DbTableView tv) throws RemoteException {
        super(exchanger, SELECT.replace("@", tabName), maxWidths, false, tv);
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
