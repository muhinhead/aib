/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.admin;

import com.aib.AIBclient;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author nick
 */
class PropPanel extends JPanel {

    private Properties props = AIBclient.getProperties();
    private static String[] colHeaders = new String[]{
        "Property", "Value"
    };
    private ArrayList<String[]> lst = new ArrayList<String[]>(20);

    private TableModel tabModel = new TableModel() {

        private void fillArray(boolean reload) {
            if (lst.size() == 0 || reload) {
                lst.clear();
                for (Object key : props.keySet()) {
                    if (!key.toString().startsWith("user")) {
                        lst.add(new String[]{key.toString(), props.get(key).toString()});
                    }
                }
            }
        }

        @Override
        public int getRowCount() {
            fillArray(false);
            return lst == null ? 0 : lst.size();
        }

        @Override
        public int getColumnCount() {
            return colHeaders.length;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return colHeaders[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            fillArray(false);
            boolean isAdmin = AIBclient.getCurrentUser().getIsAdmin().intValue() > 0;
            String[] line = lst.get(rowIndex);
            return columnIndex > 0 && (!line[0].endsWith("Password") || isAdmin);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            fillArray(false);
            boolean isAdmin = AIBclient.getCurrentUser().getIsAdmin().intValue() > 0;
            String[] line = lst.get(rowIndex);
            return (columnIndex==0 || !line[0].endsWith("Password") || isAdmin) ? lst.get(rowIndex)[columnIndex] : "*********";
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            fillArray(false);
            String[] line = lst.get(rowIndex);
            line[columnIndex] = aValue.toString();
            props.setProperty(line[0], line[1]);
            AIBclient.saveProps();
            fillArray(true);
        }

        @Override
        public void addTableModelListener(TableModelListener l) {
        }

        @Override
        public void removeTableModelListener(TableModelListener l) {
        }
    };

    public PropPanel() {
        super(new BorderLayout());
        AIBclient.getDefaultPageLimit();
        add(new JScrollPane(new JTable(tabModel)));
    }

}
