/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.filter;

import com.aib.RecordEditPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.util.Stack;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author nick
 */
public class FilterExprComponent extends JPanel {

    private static final String TEXT_FLD = "textFieldPanel";
    private static final String BETWEEN_FLD = "betweenFieldPanel";
    private static final String DECIMAL_FLD = "decomalFieldPanel";
    private static final String INT_FLD = "intFieldPanel";
    private static final String DATE_FLD = "dateFieldPanel";
    private static final String DATETIME_FLD = "dateTimeFieldPanel";
    private static int level = 0;
    private static Stack<Type> lastType = new Stack<Type>();
    private JPanel valuePanel;
    private CardLayout cl;
    private JTextField valueTF;
    private JTextField fromValueTF;
    private JTextField toValueTF;
    private JComboBox operatorCB;
    private JComboBox fldCB;

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
    private Operator operator;
    private String field;
    private String[] fldList;

    /**
     * @return the level
     */
    public static int getLevel() {
        return level;
    }
    
    public static void resetLevel() {
        level = 0;
    }
    
    public FilterExprComponent(Type type) {
        this(type, null);
    }

    public FilterExprComponent(Type type, Vector fldlist) {
        super(new BorderLayout());
        this.type = type;
        lastType.push(type);
        if (type == Type.RIGHT_BRACKET && level > 0) {
            level--;
        }
        add(new JLabel(replicate(' ', getLevel() * 2)), BorderLayout.WEST);
        if (type == Type.EXPRESSION) {
            add(RecordEditPanel.getGridPanel(new JComponent[]{
                RecordEditPanel.getBorderPanel(new JComponent[]{
                    null,
                    fldCB = new JComboBox(fldlist),
                    operatorCB = new JComboBox(new String[]{
                        "==",//Operator.EQUALS.toString(),
                        "!=",//Operator.NOT_EQUALS.toString(),
                        ">",//Operator.GREATER.toString(),
                        "<",//Operator.LESS.toString(),
                        ">=",//Operator.GREATER_EQ.toString(),
                        "<=",//Operator.LESS_EQ.toString(),
                        "LIKE",//Operator.LIKE.toString(),
                        "IN",//Operator.IN.toString(),
                        "BETWEEN",//Operator.BETWEEN.toString()
                    })}),
                getValuePanel()
            }), BorderLayout.CENTER);
            operatorCB.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    if (operatorCB.getSelectedItem().equals("BETWEEN")) {
                        cl.show(valuePanel, BETWEEN_FLD);
                    } else {
                        cl.show(valuePanel, TEXT_FLD);
                    }
                }
            });
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

    private JPanel getValuePanel() {
        if (valuePanel == null) {
            valuePanel = new JPanel(cl = new CardLayout());
            valuePanel.add(valueTF = new JTextField(), TEXT_FLD);
            valuePanel.add(RecordEditPanel.getBorderPanel(new JComponent[]{
                fromValueTF = new JTextField(10),
                new JLabel("and", SwingConstants.CENTER),
                toValueTF = new JTextField(10)
            }), BETWEEN_FLD);
        }
        return valuePanel;
    }

    public static void backSpace() {
        if (lastType.peek().equals(Type.LEFT_BRACKET) && getLevel() > 0) {
            level--;
        } else if (lastType.peek().equals(Type.RIGHT_BRACKET)) {
            level++;
        }
        lastType.pop();
    }

    private String replicate(char ch, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append(ch);
        }
        return sb.toString();
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
                sb.append(fldCB.getSelectedItem()).append(" "+operatorCB.getSelectedItem()+" ");
                if (operatorCB.getSelectedItem().equals("BETWEEN")) {
                    sb.append("'").append(fromValueTF.getText()).append("' and '").append(toValueTF.getText()).append("'");
                } else {
                    sb.append("'").append(valueTF.getText()).append("'");
                }
                sb.append("\n");
                break;
        }
        return sb.toString();
    }
}

