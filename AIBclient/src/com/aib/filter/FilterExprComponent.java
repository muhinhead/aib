/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.filter;

import com.aib.AIBclient;
import com.aib.RecordEditPanel;
import static com.aib.filter.FilterComponent.removeQuotes;
import com.aib.orm.Filter;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 *
 * @author nick
 */
public class FilterExprComponent extends FilterComponent {

    private static int level = 0;
    private static Stack<Type> lastType = new Stack<Type>();
    private JComboBox fldCB;
    private HashMap<String, Integer> fldNamesTypes;

    /**
     * @return the type
     */
    public Type getType() {
        return type;
    }

    public static enum Type {

        EXPRESSION, AND, OR, NOT, LEFT_BRACKET, RIGHT_BRACKET
    };

    public static enum Operator {

        EQUALS,
        NOT_EQUALS,
        GREATER, LESS,
        GREATER_EQ,
        LESS_EQ,
        LIKE,
        IN,
        BETWEEN;
    };
    private Type type;

    /**
     * @return the level
     */
    public static int getLevel() {
        return level;
    }

    public static void resetLevel() {
        level = 0;
        lastType.clear();
    }

    public FilterExprComponent(Type type) {
        this(type, null, null, null, null);
    }

    public FilterExprComponent(Type type, final HashMap<String, Integer> fldNamesTypes,
            final ArrayList<String> colNames,
            IFilterPanel parentPanel, String expr) {
        super(parentPanel);
        this.type = type;
        this.fldNamesTypes = fldNamesTypes;
        lastType.push(type);
        if (type == Type.RIGHT_BRACKET && level > 0) {
            level--;
        }
        add(new JLabel(replicate(' ', getLevel() * 2)), BorderLayout.WEST);
        if (type == Type.EXPRESSION) {
            Object[] fldlist = colNames.toArray();//fldNamesTypes.keySet().toArray();
            add(RecordEditPanel.getGridPanel(new JComponent[]{
                RecordEditPanel.getBorderPanel(new JComponent[]{
                    null,
                    fldCB = new JComboBox(fldlist),
                    operatorCB
                }),
                getValuePanel()
            }), BorderLayout.CENTER);
            fldCB.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    touchParent();
                    selectControls2show((String) fldCB.getSelectedItem(),
                            fldNamesTypes.get((String) fldCB.getSelectedItem()));
                }
            });
            operatorCB.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    touchParent();
                    selectControls2show((String) fldCB.getSelectedItem(),
                            fldNamesTypes.get((String) fldCB.getSelectedItem()));
                }
            });
            if (expr != null) {
                int p = expr.indexOf(" ");
                int pp = expr.indexOf(" ", p + 1);
                String fieldName = expr.substring(0, p);
                fldCB.setSelectedItem(fieldName);
                if (fieldName.equals("country_id")) {
                    operatorCB.setSelectedItem(expr.substring(p + 1, pp));
                    Integer countryID = new Integer(expr.substring(pp + 1));
                    RecordEditPanel.selectComboItem(countryCB, countryID);
                } else if (fieldName.equals("lastedited_by")) {
                    operatorCB.setSelectedItem(expr.substring(p + 1, pp));
                    Integer userID = new Integer(expr.substring(pp + 1));
                    RecordEditPanel.selectComboItem(userCB, userID);
                } else if (expr.substring(p + 1).startsWith(IS_NULL)) {
                    operatorCB.setSelectedItem(IS_NULL);
                } else {
                    operatorCB.setSelectedItem(expr.substring(p + 1, pp));
                    String fld = (String) fldCB.getSelectedItem();
                    Integer tp = fldNamesTypes.get(fld);
                    if (expr.substring(p + 1, pp).equals(BETWEEN_STR)) {
                        p = expr.indexOf(BETWEEN_STR) + BETWEEN_STR.length();
                        if (tp != null && (tp.intValue() == java.sql.Types.DATE || tp.intValue() == java.sql.Types.TIMESTAMP)) {
                            pp = expr.indexOf(" and ", p + 1);
                            try {
                                Date date = dateFormat.parse(expr.substring(p + 1, pp));
                                fromDateSP.setValue(date);
                                date = dateFormat.parse(expr.substring(pp + 5));
                                toDateSP.setValue(date);
                            } catch (ParseException ex) {
                                AIBclient.log(ex);
                            }
                        } else {
                            pp = expr.indexOf("' and '", p + 1);
                            fromValueTF.setText(removeQuotes(expr.substring(p + 1, pp)));
                            toValueTF.setText(removeQuotes(expr.substring(pp + 7)));
                        }
                    } else {
                        if (operatorCB.getSelectedItem().equals(IN)) {
                            valueTF.setText(removeQuotes(removeBraces(expr.substring(pp + 1).replaceAll("','", ","))));
                        } else {
                            valueTF.setText(removeQuotes(expr.substring(pp + 1)));
                        }
                    }
                }
            }
        } else if (type == Type.LEFT_BRACKET) {
            add(new JLabel("("));
            level++;
        } else if (type == Type.RIGHT_BRACKET && level >= 0) {
            add(new JLabel(")"));
        } else if (type == Type.AND) {
            add(new JLabel("AND"));
        } else if (type == Type.OR) {
            add(new JLabel("OR"));
        } else if (type == Type.NOT) {
            add(new JLabel("NOT"));
        }

    }

    public static void backSpace() {
        if (lastType.peek().equals(Type.LEFT_BRACKET) && getLevel() > 0) {
            level--;
        } else if (lastType.peek().equals(Type.RIGHT_BRACKET)) {
            level++;
        }
        lastType.pop();
    }

    public static void checkOrder(AbstractAction expr, AbstractAction and,
            AbstractAction or, AbstractAction not, AbstractAction lb, AbstractAction rb) {
        if (!lastType.empty()) {
            expr.setEnabled(true);
            and.setEnabled(true);
            or.setEnabled(true);
            not.setEnabled(true);
            lb.setEnabled(true);
            rb.setEnabled(level > 0);
            Type tp = lastType.peek();
            switch (tp) {
                case EXPRESSION:
                case RIGHT_BRACKET:
                    expr.setEnabled(false);
                    not.setEnabled(false);
                    lb.setEnabled(false);
                    break;
                case AND:
                case OR:
                case NOT:
                case LEFT_BRACKET:
                    and.setEnabled(false);
                    or.setEnabled(false);
                    rb.setEnabled(false);
                    break;
            }
        } else {
            expr.setEnabled(true);
            not.setEnabled(true);
            lb.setEnabled(true);
            rb.setEnabled(false);
            and.setEnabled(false);
            or.setEnabled(false);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        switch (getType()) {
            case LEFT_BRACKET:
                sb.append("(\n");
                break;
            case RIGHT_BRACKET:
                sb.append(")\n");
                break;
            case AND:
                sb.append("AND\n");
                break;
            case OR:
                sb.append("OR\n");
                break;
            case NOT:
                sb.append("NOT\n");
                break;
            case EXPRESSION:
                String fld = (String) fldCB.getSelectedItem();
                Integer tp = fldNamesTypes.get(fld);
                sb.append(fld).append(" ").append(operatorCB.getSelectedItem()).append(" ");
                if (fld.equals("Companies")) {
//                    try {
//                        Filter flt = (Filter) AIBclient.getExchanger().loadDbObjectOnID(
//                                Filter.class, RecordEditPanel.getSelectedCbItem(companyFilterCB).intValue());
//                        sb.append("(").append("select company_id from company where ").append(flt.getQuery()).append(")");
//                    } catch (RemoteException ex) {
//                        AIBclient.logAndShowMessage(ex);
//                    }
                    sb.append("(").append(RecordEditPanel.getSelectedCbItem(companyFilterCB).toString()).append(")");
                } else if (fld.equals("lastedited_by")) {
                    sb.append(RecordEditPanel.getSelectedCbItem(userCB).toString());
                } else if (fld.equals("country_id")) {
                    sb.append(RecordEditPanel.getSelectedCbItem(countryCB).toString());
                } else if (operatorCB.getSelectedItem().equals("BETWEEN")) {
                    if (tp != null && (tp.intValue() == java.sql.Types.DATE || tp.intValue() == java.sql.Types.TIMESTAMP)) {
                        Date dt1 = (Date) fromDateSP.getValue();
                        Date dt2 = (Date) toDateSP.getValue();
                        sb.append(dateFormat.format(dt1)).append(" and ").append(dateFormat.format(dt2));
                    } else {
                        sb.append("'").append(fromValueTF.getText()).append("' and '").append(toValueTF.getText()).append("'");
                    }
                } else if (operatorCB.getSelectedItem().equals(IS_NULL)) {
                } else if (operatorCB.getSelectedItem().equals(IN)) {
                    sb.append("('").append(valueTF.getText().replaceAll(",", "','")).append("')");
                } else if (tp.intValue() == java.sql.Types.DATE || tp.intValue() == java.sql.Types.TIMESTAMP) {
                    Date dt1 = (Date) fromDateSP.getValue();
                    sb.append(dateFormat.format(dt1));
                } else {
                    sb.append("'").append(valueTF.getText()).append("'");
                }
                sb.append("\n");
                break;
        }
        return sb.toString();
    }
}
