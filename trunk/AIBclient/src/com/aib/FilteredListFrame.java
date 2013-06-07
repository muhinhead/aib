/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import com.aib.orm.Filter;
import com.aib.remote.IMessageSender;
import com.xlend.util.Util;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;

/**
 *
 * @author nick
 */
public abstract class FilteredListFrame extends GeneralFrame {

    protected JPanel filterPanel;
    protected JComboBox filtersCB;
    private JLabel filterLbl;
    private JToggleButton filterButton;
    private static String[] sheetList = new String[]{
        "List", "Filter"
    };

    public FilteredListFrame(String title, IMessageSender exch) {
        super(title, exch);
    }

    @Override
    protected void addAfterSearch() {
        getToolBar().add(filterButton = new JToggleButton(new ImageIcon(Util.loadImage("filter.png"))));
        getToolBar().add(filterLbl = new JLabel("Filter:"));
        getToolBar().add(filtersCB = new JComboBox(AIBclient.loadAllFilters(getMainTableName())));
        filterButton.setMinimumSize(getSearchButton().getPreferredSize());
        filtersCB.setMaximumSize(new Dimension(200, filtersCB.getPreferredSize().height));
        filtersCB.setVisible(false);
        filterLbl.setVisible(false);
        filterButton.addActionListener(getShowFilterCBaction());
        filtersCB.addActionListener(getChooseFilterAction());
        filterButton.setToolTipText("Select a filter to apply");
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
    protected String[] getSheetList() {
        return sheetList;
    }

    protected abstract String getMainTableName();
    
    protected abstract ActionListener getChooseFilterAction();

    @Override
    protected JTabbedPane buildMainPanel() {

        MyJideTabbedPane workTab = new MyJideTabbedPane();
        workTab.add(getListPanel(), sheetList[0]);
        workTab.add(getFilterPanel(), sheetList[1]);
        return workTab;
    }

    protected abstract JPanel getListPanel();

    protected abstract JPanel getFilterPanel();

    public abstract void applyFilter(Filter flt);
}
