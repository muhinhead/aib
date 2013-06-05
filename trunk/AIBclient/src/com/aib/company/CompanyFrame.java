/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.company;

import com.aib.AIBclient;
import com.aib.FilteredListFrame;
import com.aib.GeneralFrame;
import com.aib.filter.CompanyFilterPanel;
import com.aib.orm.Filter;
import com.aib.orm.dbobject.ComboItem;
import com.aib.remote.IMessageSender;
import com.xlend.util.Util;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

/**
 *
 * @author nick
 */
public class CompanyFrame extends FilteredListFrame {

    private static String[] sheetList = new String[]{
        "List", "Filter"
    };
    private CompaniesGrid companiesPanel;
    private JPanel filterPanel;
    private JToggleButton filterButton;
    private JComboBox filtersCB;
    private JLabel filterLbl;
    private boolean dontFilter = false;

    public CompanyFrame(IMessageSender exch) {
        super("Companies", exch);
    }

    @Override
    protected void addAfterSearch() {
        getToolBar().add(filterButton = new JToggleButton(new ImageIcon(Util.loadImage("filter.png"))));
        getToolBar().add(filterLbl = new JLabel("Filter:"));
        getToolBar().add(filtersCB = new JComboBox(AIBclient.loadAllFilters("company")));
        filterButton.setMinimumSize(getSearchButton().getPreferredSize());
        filtersCB.setMaximumSize(new Dimension(200, filtersCB.getPreferredSize().height));
        filtersCB.setVisible(false);
        filterLbl.setVisible(false);
        filterButton.addActionListener(getShowFilterCBaction());
        filtersCB.addActionListener(getChooseFilterAction());
        filterButton.setToolTipText("Select a filter to apply");
    }

    @Override
    protected JPanel getListPanel() {
        if (companiesPanel == null) {
            try {
                registerGrid(companiesPanel = new CompaniesGrid(getExchanger()));
            } catch (RemoteException ex) {
                AIBclient.logAndShowMessage(ex);
            }
        }
        return companiesPanel;
    }

    @Override
    protected JPanel getFilterPanel() {
        if (filterPanel == null) {
            filterPanel = new JPanel(new BorderLayout());
            JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            sp.setTopComponent(new CompanyFilterPanel(this));
            sp.setBottomComponent(new JLabel("Here should be a query expression", SwingConstants.CENTER));
            filterPanel.add(sp, BorderLayout.CENTER);
        }
        return filterPanel;
    }

    @Override
    protected ActionListener addNewFilterAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    @Override
    protected ActionListener editFilterAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    @Override
    protected ActionListener delFilterAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    private ActionListener getShowFilterCBaction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean pressed = filterButton.isSelected();
                filterLbl.setVisible(pressed);
                filtersCB.setVisible(pressed);
                if (pressed) {
                    filtersCB.requestFocus();
                }
            }
        };
    }

    @Override
    public void applyFilter(Filter flt) {
        if (flt != null) {
            final String FROM_COMP = "from company ";
            String newSelect;
            int p = CompaniesGrid.SELECT.indexOf(FROM_COMP);
            if (flt.getIsComplex() != null && flt.getIsComplex().intValue() == 1) {
                newSelect = CompaniesGrid.SELECT.substring(0, p + FROM_COMP.length())
                        + "where " + flt.getQuery().replaceAll("==", "=");
                companiesPanel.setSelect(newSelect);
            } else {
                newSelect = CompaniesGrid.SELECT.substring(0, p + FROM_COMP.length())
                        + ",people,peoplecompany pc "
                        + "where pc.company_id=company.company_id "
                        + "and pc.people_id=people.people_id "
                        + "and (" + flt.getQuery().replaceAll("==", "=") + ")";
                companiesPanel.setSelect(newSelect);
            }
            companiesPanel.refresh();
            dontFilter = true;
            ComboBoxModel fmd = filtersCB.getModel();
            Object ob;
            for (int i = 0; (ob = fmd.getElementAt(i)) != null; i++) {
                ComboItem ci = (ComboItem) ob;
                if (ci.getId() == flt.getFilterId().intValue()) {
                    filtersCB.setSelectedIndex(i);
                    break;
                }
            }
            dontFilter = false;
            getMainPanel().setSelectedIndex(0);
        }
    }

    private ActionListener getChooseFilterAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!dontFilter) {
                    if (filtersCB.getSelectedIndex() == 0) {
                        companiesPanel.setSelect(CompaniesGrid.SELECT);
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
                companiesPanel.refresh();
            }
        };
    }
}
