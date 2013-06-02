/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.filter;

import com.aib.RecordEditPanel;
import com.xlend.util.SelectedDateSpinner;
import com.xlend.util.Util;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.LayoutManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Types;
import java.text.SimpleDateFormat;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author nick
 */
public abstract class FilterComponent extends JPanel {

    static final String BETWEENDATES_FLD = "betweenDatesFieldPanel";
    static final String BETWEEN_FLD = "betweenFieldPanel";
    static final String BETWEEN_STR = "BETWEEN";
    static final String DATETIME_FLD = "dateTimeFieldPanel";
    static final String DATE_FLD = "dateFieldPanel";
    static final String DECIMAL_FLD = "decomalFieldPanel";
    static final String IN = "IN";
    static final String INT_FLD = "intFieldPanel";
    static final String IS_NULL = "IS NULL";
    static final String TEXT_FLD = "textFieldPanel";
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    protected static String removeQuotes(String s) {
        if (s.trim().startsWith("'")) {
            s = s.trim().substring(1);
        }
        if (s.trim().endsWith("'")) {
            s = s.trim().substring(0, s.trim().length() - 1);
        }
        return s;
    }

    protected static String replicate(char ch, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }
    protected CardLayout cl;
    protected SelectedDateSpinner fromDateSP;
    protected JTextField fromValueTF;
    protected SelectedDateSpinner toDateSP;
    protected JTextField toValueTF;
    protected JPanel valuePanel;
    protected JTextField valueTF;
    protected IFilterPanel parentPanel;
    protected JComboBox operatorCB;

    public FilterComponent(IFilterPanel parentPanel) {
        super(new BorderLayout());
        this.parentPanel = parentPanel;
        operatorCB = new JComboBox(new String[]{
            "==",//Operator.EQUALS.toString(),
            "!=",//Operator.NOT_EQUALS.toString(),
            ">",//Operator.GREATER.toString(),
            "<",//Operator.LESS.toString(),
            ">=",//Operator.GREATER_EQ.toString(),
            "<=",//Operator.LESS_EQ.toString(),
            "LIKE",//Operator.LIKE.toString(),
            IN,//Operator.IN.toString(),
            BETWEEN_STR,//Operator.BETWEEN.toString()
            IS_NULL
        });
    }

    protected JPanel getValuePanel() {
        if (valuePanel == null) {
            valuePanel = new JPanel(cl = new CardLayout());
            valuePanel.add(valueTF = new JTextField(), TEXT_FLD);
            valuePanel.add(RecordEditPanel.getBorderPanel(new JComponent[]{fromValueTF = new JTextField(10), new JLabel("and", SwingConstants.CENTER), toValueTF = new JTextField(10)}), BETWEEN_FLD);
            valuePanel.add(RecordEditPanel.getBorderPanel(new JComponent[]{fromDateSP = new SelectedDateSpinner(), new JLabel("and", SwingConstants.CENTER), toDateSP = new SelectedDateSpinner()}), BETWEENDATES_FLD);
            valueTF.addKeyListener(getKeyAdapter());
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

    protected KeyAdapter getKeyAdapter() {
        return new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                touchParent();
            }
        };
    }

    protected void touchParent() {
        if (parentPanel != null) {
            parentPanel.setChanged(true);
        }
    }

    protected void selectControls2show(String fld, Integer tp) {
        if (operatorCB.getSelectedItem().equals(BETWEEN_STR)) {
            if (tp != null) {
                switch (tp.intValue()) {
                    case Types.DATE:
                        cl.show(valuePanel, BETWEENDATES_FLD);
                        toDateSP.setVisible(true);
                        break;
                    default:
                        cl.show(valuePanel, BETWEEN_FLD);
                        break;
                }
            } else {
                cl.show(valuePanel, BETWEEN_FLD);
            }
        } else if (operatorCB.getSelectedItem().equals(IS_NULL)) {
            cl.show(valuePanel, IS_NULL);
        } else {
            if (tp.intValue() == Types.DATE) {
                cl.show(valuePanel, BETWEENDATES_FLD);
                toDateSP.setVisible(false);
            } else {
                cl.show(valuePanel, TEXT_FLD);
            }
        }
    }
}
