package com.aib;

import com.aib.people.PeopleGrid;
import com.aib.filter.PeopleFilterPanel;
import com.aib.orm.Filter;
import com.aib.orm.dbobject.ComboItem;
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
public class PeopleFrame extends FilteredListFrame {

    private static String[] sheetList = new String[]{
        "List", "Filter"
    };
    private PeopleGrid peoplePanel;

    public PeopleFrame(IMessageSender exch) {
        super("People", exch);
    }

    @Override
    protected JPanel getListPanel() {
        if (peoplePanel == null) {
            try {
                registerGrid(peoplePanel = new PeopleGrid(getExchanger()));
            } catch (RemoteException ex) {
                AIBclient.logAndShowMessage(ex);
            }
        }
        return peoplePanel;
    }

    @Override
    protected JPanel getFilterPanel() {
        if (filterPanel == null) {
            filterPanel = new JPanel(new BorderLayout());
            filterPanel.add(new PeopleFilterPanel(this));
        }
        return filterPanel;
    }

    @Override
    public void applyFilter(Filter flt) {
        if (flt!=null) {
            final String FROM_PEOPLE = "from people";
            String newSelect = null;
            int p = PeopleGrid.SELECT.indexOf(FROM_PEOPLE);
            if (flt.getIsComplex() != null && flt.getIsComplex().intValue() == 1) {
                newSelect = PeopleGrid.SELECT.substring(0, p + FROM_PEOPLE.length())
                        + " where " + flt.getQuery().replaceAll("==", "=");
            }
            if (flt.getQuery() == null || flt.getQuery().trim().length()==0) {
                GeneralFrame.errMessageBox("Attention!", "The empty filter couldn't be applied");
            } else {
                peoplePanel.setSelect(newSelect);
                peoplePanel.refresh();
                gotoFilterApplied(flt.getFilterId().intValue());
            }
        }
    }

    @Override
    protected String getMainTableName() {
        return "people";
    }
    
    @Override
    protected ActionListener getChooseFilterAction() {
        return new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!dontFilter) {
                    if (filtersCB.getSelectedIndex() == 0) {
                        peoplePanel.setSelect(PeopleGrid.SELECT);
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
                peoplePanel.refresh();
            }
        };
    }
}