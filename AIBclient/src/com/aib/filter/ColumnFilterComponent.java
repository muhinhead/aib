/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.filter;

import com.aib.AIBclient;
import com.aib.RecordEditPanel;
import static com.aib.filter.FilterComponent.TEXT_FLD;
import com.xlend.util.Java2sAutoComboBox;
import com.xlend.util.SelectedDateSpinner;
import com.xlend.util.Util;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Nick Mukhin
 */
public class ColumnFilterComponent extends FilterComponent {

    private final String columnName;
    private final int columnType;
    private Java2sAutoComboBox titleCB;
    private Java2sAutoComboBox suffixCB;
    private Java2sAutoComboBox abbreviationCB;
    private Java2sAutoComboBox jobdiscCB;

    public ColumnFilterComponent(String columnName, int columnType, IFilterPanel parentPanel) {
        super(parentPanel);
        this.columnName = columnName;
        this.columnType = columnType;
        add(RecordEditPanel.getBorderPanel(new JComponent[]{
            operatorCB,
            getValuePanel()
        }), BorderLayout.CENTER);
    }

    @Override
    protected JPanel getValuePanel() {
        if (valuePanel == null) {
            valuePanel = new JPanel(cl = new CardLayout());
            if (columnName.equals("company.abbreviation")) {
                valuePanel.add(RecordEditPanel.getGridPanel(
                        abbreviationCB = new Java2sAutoComboBox(AIBclient.loadDistinctAbbreviations()), 2));
                abbreviationCB.setEditable(true);
                abbreviationCB.setStrict(false);
            } else if (columnName.equals("people.title")) {
                valuePanel.add(RecordEditPanel.getGridPanel(
                        titleCB = new Java2sAutoComboBox(AIBclient.loadDistinctTitles()), 2));
                titleCB.setEditable(true);
                titleCB.setStrict(false);
            } else if (columnName.equals("people.suffix")) {
                valuePanel.add(RecordEditPanel.getGridPanel(
                        suffixCB = new Java2sAutoComboBox(AIBclient.loadDistinctSuffixes()), 2));
                suffixCB.setEditable(true);
                suffixCB.setStrict(false);
            } else if (columnName.equals("people.job_discip")) {
                valuePanel.add(RecordEditPanel.getGridPanel(
                        jobdiscCB = new Java2sAutoComboBox(AIBclient.loadDistinctJobDisciplines()), 2));
                jobdiscCB.setEditable(true);
                jobdiscCB.setStrict(false);
            } else {
                valuePanel.add(valueTF = new JTextField(), TEXT_FLD);
            }
            valuePanel.add(RecordEditPanel.getBorderPanel(new JComponent[]{
                fromValueTF = new JTextField(10), new JLabel("and", SwingConstants.CENTER),
                toValueTF = new JTextField(10)
            }), BETWEEN_FLD);
            valuePanel.add(RecordEditPanel.getBorderPanel(new JComponent[]{
                fromDateSP = new SelectedDateSpinner(), new JLabel("and", SwingConstants.CENTER),
                toDateSP = new SelectedDateSpinner()
            }), BETWEENDATES_FLD);
            if (valueTF != null) {
                valueTF.addKeyListener(getKeyAdapter());
            } else if (titleCB != null) {
                titleCB.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        touchParent();
                    }
                });
            } else if (suffixCB != null) {
                suffixCB.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        touchParent();
                    }
                });
            } else if (abbreviationCB != null) {
                abbreviationCB.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        touchParent();
                    }
                });
            } else if (jobdiscCB != null) {
                jobdiscCB.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        touchParent();
                    }
                });
            }
            fromValueTF.addKeyListener(getKeyAdapter());
            toValueTF.addKeyListener(getKeyAdapter());
            fromDateSP.setEditor(new JSpinner.DateEditor(fromDateSP, RecordEditPanel.DD_MM_YYYY));
            Util.addFocusSelectAllAction(fromDateSP);
            toDateSP.setEditor(new JSpinner.DateEditor(toDateSP, RecordEditPanel.DD_MM_YYYY));
            Util.addFocusSelectAllAction(toDateSP);
            valuePanel.add(new JPanel(), IS_NULL);
        }
        return valuePanel;
    }
}
