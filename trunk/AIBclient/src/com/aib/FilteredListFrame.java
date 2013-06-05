/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import com.aib.orm.Filter;
import com.aib.remote.IMessageSender;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author nick
 */
public abstract class FilteredListFrame extends GeneralFrame {

    private static String[] sheetList = new String[]{
        "List", "Filter"
    };

    public FilteredListFrame(String title, IMessageSender exch) {
        super(title, exch);
    }

    @Override
    protected String[] getSheetList() {
        return sheetList;
    }

    @Override
    protected JTabbedPane buildMainPanel() {

        MyJideTabbedPane workTab = new MyJideTabbedPane();
        workTab.add(getListPanel(), sheetList[0]);
        workTab.add(getFilterPanel(), sheetList[1]);
//        workTab.addChangeListener(new ChangeListener() {
//            @Override
//            public void stateChanged(ChangeEvent ce) {
//                MyJideTabbedPane tp = (MyJideTabbedPane) ce.getSource();
//                filterMenu.setEnabled(tp.getSelectedIndex() == 1);
//            }
//        });
        return workTab;
    }

    protected abstract JPanel getListPanel();

    protected abstract JPanel getFilterPanel();
    
    public abstract void applyFilter(Filter flt);
}
