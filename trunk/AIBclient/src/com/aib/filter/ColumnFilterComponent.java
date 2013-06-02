/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.filter;

import com.aib.RecordEditPanel;
import java.awt.BorderLayout;
import javax.swing.JComponent;

/**
 *
 * @author Nick Mukhin
 */
public class ColumnFilterComponent extends FilterComponent {

    private final String columnName;
    private final int columnType;

    public ColumnFilterComponent(String columnName, int columnType, IFilterPanel parentPanel) {
        super(parentPanel);
        this.columnName = columnName;
        this.columnType = columnType;
        add(RecordEditPanel.getBorderPanel(new JComponent[]{
            operatorCB,
            getValuePanel()
        }), BorderLayout.CENTER);
    }

}
