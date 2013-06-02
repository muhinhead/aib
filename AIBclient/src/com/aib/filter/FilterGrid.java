/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.filter;

import com.aib.AIBclient;
import com.aib.GeneralFrame;
import com.aib.GeneralGridPanel;
import com.aib.orm.Filter;
import com.aib.remote.IMessageSender;
import com.xlend.mvc.dbtable.DbTableView;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 *
 * @author Nick Mukhin
 */
public class FilterGrid extends GeneralGridPanel {

    public static final String SELECT =
            "select filter_id \"ID\", name \"Name\", initials \"Owner\" "
            + "from filter,user where owner_id=user_id and tablename='@'";
    private static HashMap<Integer, Integer> maxWidths = new HashMap<Integer, Integer>();

    static {
        maxWidths.put(0, 40);
        maxWidths.put(2, 60);
    }

    public FilterGrid(IMessageSender exchanger, String tabName, boolean complex,
            DbTableView tv) throws RemoteException {
        super(exchanger,
                SELECT.replace("@", tabName)
                .replace("where ", "where " + (complex ? "is_complex and " : "not is_complex and ")),
                maxWidths, false, tv);
    }

    @Override
    protected AbstractAction addAction() {
        return new AbstractAction("Add") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    String mark = "tablename='";
                    int p = getSelect().indexOf(mark);
                    Filter filter = new Filter(null);
                    filter.setNew(true);
                    filter.setFilterId(0);
                    filter.setName("[New Filter]");
                    filter.setTablename(getSelect().substring(p + mark.length()).replaceAll("'", ""));
                    filter.setOwnerId(AIBclient.getCurrentUser().getUserId());
                    filter.setIsComplex(getSelect().indexOf("not is complex") < 0 ? 1 : 0);
                    filter = (Filter) AIBclient.getExchanger().saveDbObject(filter);
                    refresh(filter.getFilterId());
                } catch (Exception ex) {
                    AIBclient.logAndShowMessage(ex);
                }
            }
        };
    }

    @Override
    protected AbstractAction editAction() {
        return null;
    }

    @Override
    protected AbstractAction delAction() {
        return new AbstractAction("Del") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int id = getSelectedID();
                try {
                    Filter filter = (Filter) exchanger.loadDbObjectOnID(Filter.class, id);
                    if (filter != null && GeneralFrame.yesNo(
                            "Attention!", "Do you want to delete this filter?") == JOptionPane.YES_OPTION) {
                        exchanger.deleteObject(filter);
                        refresh();
                    }
                } catch (RemoteException ex) {
                    AIBclient.logAndShowMessage(ex);
                }
            }
        };
    }
}
