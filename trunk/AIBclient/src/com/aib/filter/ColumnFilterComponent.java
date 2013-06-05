/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.filter;

import com.aib.AIBclient;
import com.aib.GeneralFrame;
import com.aib.RecordEditPanel;
import static com.aib.filter.FilterComponent.BETWEEN_FLD;
import static com.aib.filter.FilterComponent.BETWEEN_STR;
import static com.aib.filter.FilterComponent.EQUALS;
import static com.aib.filter.FilterComponent.GREATER;
import static com.aib.filter.FilterComponent.GREATER_EQ;
import static com.aib.filter.FilterComponent.IN;
import static com.aib.filter.FilterComponent.IS_NOT_NULL;
import static com.aib.filter.FilterComponent.IS_NULL;
import static com.aib.filter.FilterComponent.LESS;
import static com.aib.filter.FilterComponent.LESS_EQ;
import static com.aib.filter.FilterComponent.LIKE;
import static com.aib.filter.FilterComponent.NOT_EQUALS;
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

    public static final String FULLNAME = "company.full_name";
    public static final String FIRSTNAME = "people.first_name";
    public static final String LASTNAME = "people.last_name";
    public static final String MAINEMAIL = "people.main_email";
    public static final String ABBREVIATION = "company.abbreviation";
    public static final String TITLE = "people.title";
    public static final String SUFFIX = "people.suffix";
    public static final String JOB = "people.job_discip";
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
        operatorCB.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                touchParent();
                selectControls2show(ColumnFilterComponent.this.columnName,
                        ColumnFilterComponent.this.columnType);
            }
        });
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
//        sb.append(columnName).append(" ").append(operatorCB.getSelectedItem());
        if (operatorCB.getSelectedItem().equals(BETWEEN_STR)) {
            sb.append(columnName).append(" ").append(operatorCB.getSelectedItem());
            sb.append(" '").append(fromValueTF.getText()).append("' And '").append(toValueTF.getText()).append("'");
        } else if (operatorCB.getSelectedItem().equals(IS_NULL) || operatorCB.getSelectedItem().equals(IS_NOT_NULL)) {
            sb.append(columnName).append(" ").append(operatorCB.getSelectedItem());
        } else if (abbreviationCB != null && !abbreviationCB.getSelectedItem().equals("")) {
            sb.append(columnName).append(" ").append(operatorCB.getSelectedItem());
            sb.append(" '").append(abbreviationCB.getSelectedItem()).append("'");;
        } else if (titleCB != null && !titleCB.getSelectedItem().equals("")) {
            sb.append(columnName).append(" ").append(operatorCB.getSelectedItem());
            sb.append(" '").append(titleCB.getSelectedItem()).append("'");
        } else if (suffixCB != null && !suffixCB.getSelectedItem().equals("")) {
            sb.append(columnName).append(" ").append(operatorCB.getSelectedItem());
            sb.append(" '").append(suffixCB.getSelectedItem()).append("'");
        } else if (jobdiscCB != null && !jobdiscCB.getSelectedItem().equals("")) {
            sb.append(columnName).append(" ").append(operatorCB.getSelectedItem());
            sb.append(" '").append(jobdiscCB.getSelectedItem()).append("'");
        } else if (valueTF != null && !valueTF.getText().equals("")) {
            sb.append(columnName).append(" ").append(operatorCB.getSelectedItem());
            if (operatorCB.getSelectedItem().equals(IN)) {
                sb.append(" ('").append(valueTF.getText().replaceAll(",", "','")).append("')");
            } else {
                sb.append(" '").append(valueTF.getText()).append("'");
            }
        }
//        System.out.println("!!"+sb.toString());
        return sb.toString();
    }

    protected void selectControls2show(String fld, Integer tp) {
        super.selectControls2show(fld, tp);
        if (!operatorCB.getSelectedItem().equals(BETWEEN_STR)
                && !operatorCB.getSelectedItem().equals(IS_NOT_NULL)
                && !operatorCB.getSelectedItem().equals(IS_NULL)) {
            if (abbreviationCB != null) {
                cl.show(valuePanel, ABBREVIATION);
            } else if (titleCB != null) {
                cl.show(valuePanel, TITLE);
            } else if (suffixCB != null) {
                cl.show(valuePanel, SUFFIX);
            } else if (jobdiscCB != null) {
                cl.show(valuePanel, JOB);
            }
        }
    }

    @Override
    protected JPanel getValuePanel() {
        if (valuePanel == null) {
            valuePanel = new JPanel(cl = new CardLayout());
            if (columnName.equals(ABBREVIATION)) {
                valuePanel.add(RecordEditPanel.getGridPanel(
                        abbreviationCB = new Java2sAutoComboBox(AIBclient.loadDistinctAbbreviations()), 2),
                        columnName);
                abbreviationCB.setEditable(true);
                abbreviationCB.setStrict(false);
            } else if (columnName.equals(TITLE)) {
                valuePanel.add(RecordEditPanel.getGridPanel(
                        titleCB = new Java2sAutoComboBox(AIBclient.loadDistinctTitles()), 2),
                        columnName);
                titleCB.setEditable(true);
                titleCB.setStrict(false);
            } else if (columnName.equals(SUFFIX)) {
                valuePanel.add(RecordEditPanel.getGridPanel(
                        suffixCB = new Java2sAutoComboBox(AIBclient.loadDistinctSuffixes()), 2),
                        columnName);
                suffixCB.setEditable(true);
                suffixCB.setStrict(false);
            } else if (columnName.equals(JOB)) {
                valuePanel.add(RecordEditPanel.getGridPanel(
                        jobdiscCB = new Java2sAutoComboBox(AIBclient.loadDistinctJobDisciplines()), 2),
                        columnName);
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
            valuePanel.add(new JPanel(), IS_NOT_NULL);
            valuePanel.add(new JPanel(), IS_NULL);
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

    private static String extract(String s, String lt, String rt) {
        int p1 = s.indexOf(lt);
        int p2 = s.indexOf(rt, p1 + lt.length());
        if (p1 >= 0 && p2 > 0 && p1 < p2) {
            return s.substring(p1 + lt.length(), p2);
        }
        return s;
    }

    private void setVals(String val1, String val2) {
        if (val2 != null) {
            fromValueTF.setText(val1);
            toValueTF.setText(val2);
        } else if (val1 != null) {
            if (columnName.equals(ABBREVIATION)) {
                abbreviationCB.setSelectedItem(val1);
            } else if (columnName.equals(TITLE)) {
                titleCB.setSelectedItem(val1);
            } else if (columnName.equals(SUFFIX)) {
                suffixCB.setSelectedItem(val1);
            } else if (columnName.equals(JOB)) {
                jobdiscCB.setSelectedItem(val1);
            } else {
                valueTF.setText(val1);
            }
        }
    }

    public void load(String expression) {
        operatorCB.setSelectedIndex(0);
        if (expression != null) {
            int p = expression.indexOf(columnName);
            if (p >= 0) {
                String exp = expression.substring(p + columnName.length()).trim();
                String val = null, val2 = null;
                if (exp.startsWith(EQUALS + " ")) {
                    operatorCB.setSelectedItem(EQUALS);
                    val = extract(exp.substring(EQUALS.length() + 1), "'", "'");
                } else if (exp.startsWith(NOT_EQUALS + " ")) {
                    operatorCB.setSelectedItem(NOT_EQUALS);
                    val = extract(exp.substring(NOT_EQUALS.length() + 1), "'", "'");
                } else if (exp.startsWith(GREATER + " ")) {
                    operatorCB.setSelectedItem(GREATER);
                    val = extract(exp.substring(GREATER.length() + 1), "'", "'");
                } else if (exp.startsWith(LESS + " ")) {
                    operatorCB.setSelectedItem(LESS);
                    val = extract(exp.substring(LESS.length() + 1), "'", "'");
                } else if (exp.startsWith(GREATER_EQ + " ")) {
                    operatorCB.setSelectedItem(GREATER_EQ);
                    val = extract(exp.substring(GREATER_EQ.length() + 1), "'", "'");
                } else if (exp.startsWith(LESS_EQ + " ")) {
                    operatorCB.setSelectedItem(LESS_EQ);
                    val = extract(exp.substring(LESS_EQ.length() + 1), "'", "'");
                } else if (exp.startsWith(LIKE + " ")) {
                    operatorCB.setSelectedItem(LIKE);
                    val = extract(exp.substring(LIKE.length() + 1), "'", "'");
                } else if (exp.startsWith(IN + " ")) {
                    operatorCB.setSelectedItem(IN);
                    val = extract(exp.substring(IN.length() + 1), "('", "')").replaceAll("','", ",");
                } else if (exp.startsWith(BETWEEN_STR + " ")) {
                    operatorCB.setSelectedItem(BETWEEN_STR);
                    val = extract(exp.substring(BETWEEN_STR.length() + 1), "'", "' And");
                    val2 = extract(exp.substring(BETWEEN_STR.length() + 1), "And '", "'");
                } else if (exp.startsWith(IS_NOT_NULL + " ")) {
                    operatorCB.setSelectedItem(IS_NOT_NULL);
                } else if (exp.startsWith(IS_NULL + " ")) {
                    operatorCB.setSelectedItem(IS_NULL);
                }
                setVals(val, val2);
            }
//        } else {
//            GeneralFrame.errMessageBox("Attention!", "The query is empty");
        }
    }
}
