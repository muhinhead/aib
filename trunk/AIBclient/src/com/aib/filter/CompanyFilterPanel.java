/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.filter;

import com.aib.AIBclient;
import com.aib.MyJideTabbedPane;
import com.aib.RecordEditPanel;
import com.aib.orm.Filter;
import com.aib.orm.dbobject.ComboItem;
import com.xlend.mvc.dbtable.DbTableView;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author Nick Mukhin
 */
public class CompanyFilterPanel extends JPanel {

    private JPanel editorPanel;
    private JPanel expressionPanel;
    private boolean changed;
    private FilterTable filterTable;
    private FilterGrid cfg;
    private DefaultComboBoxModel userCbModel;
    private JPanel edLabelPanel;
    private JPanel edComponentPanel;
    private JPanel upPerCenterPanel;
    private AbstractAction lftBraketBtnAct;
    private AbstractAction rghtBraketBtnAct;
    private JTextField filterNameTF;
    private JComboBox ownerCB;
    private AbstractAction exprBtnAct;
    private AbstractAction andBtnAct;
    private AbstractAction orBtnAct;
    private AbstractAction notBtnAct;
    private AbstractAction backBtnAct;
    private Vector colNames;
    private AbstractAction saveBtnAct;
    private JLabel changedLbl;
    private AbstractAction reloadBtnAct;

    public class FilterTable extends DbTableView {

        @Override
        public void valueChanged(ListSelectionEvent lse) {
            super.valueChanged(lse);
            reloadSelectedFilter();
        }

        public void reloadSelectedFilter() {
            FilterExprComponent.resetLevel();
            loadFilter(getSelectedFilter());
        }
        
        private Filter getSelectedFilter() {
            int id = cfg.getSelectedID();
            Filter flt = null;
            if (id > 0) {
                try {
                    flt = (Filter) AIBclient.getExchanger().loadDbObjectOnID(Filter.class, id);
                } catch (RemoteException ex) {
                    AIBclient.log(ex);
                }
            }
            return flt;
        }
    }

    protected static Color getHederBackground() {
        return new Color(102, 125, 158);
    }

    protected static Color getHeaderForeground() {
        return new Color(255, 255, 255);
    }

    public CompanyFilterPanel() {
        super(new BorderLayout());
        try {
            colNames = AIBclient.getExchanger().getColNames("select * from company where company_id<0");
        } catch (RemoteException ex) {
            AIBclient.log(ex);
        }
        JScrollPane sp;
        JSplitPane split = new JSplitPane();
        try {
            split.setTopComponent((Component) (cfg = new FilterGrid(
                    AIBclient.getExchanger(), "company", filterTable = new FilterTable())));
        } catch (RemoteException ex) {
            AIBclient.logAndShowMessage(ex);
        }

        cfg.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Saved filters"));
        cfg.setPreferredSize(new Dimension(300, 200));
        split.setMinimumSize(new Dimension(split.getPreferredSize().width, 200));
        split.setBottomComponent(getFilterEditor());

        add(split);
    }

    private JPanel getHeaderPanel(String title) {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(getHederBackground());
        JLabel lbl = new JLabel(title, SwingConstants.CENTER);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 18));
        lbl.setForeground(getHeaderForeground());
        headerPanel.add(lbl);
        return headerPanel;
    }

    private JComponent getFilterEditor() {
        String queryEditor = "Editor";
        String queryExpression = "Expression";
        MyJideTabbedPane tp = new MyJideTabbedPane();

        tp.add(editorPanel = new JPanel(new BorderLayout(10, 10)), queryEditor);
        tp.add(expressionPanel = new JPanel(new BorderLayout(10, 10)), queryExpression);
        editorPanel.add(getHeaderPanel(queryEditor), BorderLayout.NORTH);
        expressionPanel.add(getHeaderPanel(queryExpression), BorderLayout.NORTH);

        userCbModel = new DefaultComboBoxModel();
        for (ComboItem ci : AIBclient.loadAllUsersInitials()) {
            userCbModel.addElement(ci);
        }

        fillPanel(editorPanel, new String[]{
            "  Filter Name:",
            ""
        }, new JComponent[]{
            RecordEditPanel.getGridPanel(new JComponent[]{
                filterNameTF = new JTextField(),
                RecordEditPanel.getBorderPanel(new JComponent[]{
                    new JLabel(" Owner:", SwingConstants.RIGHT),
                    ownerCB = new JComboBox(userCbModel)
                })
            }),
            changedLbl = new JLabel("-", SwingConstants.LEFT)
        });
        changedLbl.setForeground(Color.BLUE);
        editorPanel.add(getToolbarPanel(), BorderLayout.EAST);
        filterNameTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                setChanged(true);
            }
        });
        return tp;
    }

    private void loadFilter(Filter filter) {
        Component[] comps = edComponentPanel.getComponents();
        for (int i = comps.length - 1; i > 1; i--) {
            edComponentPanel.remove(comps[i]);
        }
        edComponentPanel.revalidate();
        filterNameTF.setText(filter == null ? "" : filter.getName());
        if (filter != null) {
            RecordEditPanel.selectComboItem(ownerCB, filter.getOwnerId());
            int r = 0;
            if (filter.getQuery() != null) {
                StringTokenizer st = new StringTokenizer(filter.getQuery(), "\n");
                while (st.hasMoreTokens()) {
                    String line = st.nextToken();
                    if (line.equals("AND")) {
                        addOperator(FilterExprComponent.Type.AND);
                    } else if (line.equals("OR")) {
                        addOperator(FilterExprComponent.Type.OR);
                    } else if (line.equals("NOT")) {
                        addOperator(FilterExprComponent.Type.NOT);
                    } else if (line.equals("(")) {
                        addOperator(FilterExprComponent.Type.LEFT_BRACKET);
                    } else if (line.equals(")")) {
                        addOperator(FilterExprComponent.Type.RIGHT_BRACKET);
                    } else {
                        FilterExprComponent fcomp;
                        fcomp = new FilterExprComponent(FilterExprComponent.Type.EXPRESSION, colNames);
                        addLine(fcomp);
                    }
                }
            }
        }
        setChanged(false);
    }

    private void fillPanel(JPanel papa, String[] labels, JComponent[] comps) {
        JPanel upperPanel = new JPanel(new BorderLayout());
        edLabelPanel = new JPanel();
        edLabelPanel.setLayout(new BoxLayout(edLabelPanel, BoxLayout.Y_AXIS));
        edComponentPanel = new JPanel();
        edComponentPanel.setLayout(new BoxLayout(edComponentPanel, BoxLayout.Y_AXIS));

        for (String lbl : labels) {
            edLabelPanel.add(new JLabel(lbl, SwingConstants.RIGHT));
        }
        for (JComponent comp : comps) {
            edComponentPanel.add(comp);
        }

        upperPanel.add(edLabelPanel, BorderLayout.WEST);
        upperPanel.add(edComponentPanel, BorderLayout.CENTER);
        upPerCenterPanel = new JPanel(new BorderLayout());
        upPerCenterPanel.add(upperPanel, BorderLayout.NORTH);
        papa.add(new JScrollPane(upPerCenterPanel), BorderLayout.CENTER);
    }

    private JPanel getToolbarPanel() {
        JPanel tb = new JPanel(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(11, 1, 10, 10));
        panel.add(new JButton(exprBtnAct = addExpressionLineAction("EXPR")));
        panel.add(new JButton(andBtnAct = addAndAction("AND")));
        panel.add(new JButton(orBtnAct = addOrAction("OR")));
        panel.add(new JButton(notBtnAct = addNotAction("NOT")));
        panel.add(RecordEditPanel.getGridPanel(new JComponent[]{
            new JButton(lftBraketBtnAct = addLeftBracketAction("(")),
            new JButton(rghtBraketBtnAct = addRightBracketAction(")"))
        }));
        panel.add(new JButton(backBtnAct = backSpaceAction("Back")));
        panel.add(new JSeparator());
        panel.add(new JButton(saveBtnAct = saveAction("Save")));
        panel.add(new JButton(reloadBtnAct = reloadAction("Reload")));
        panel.add(new JButton("Apply"));
        tb.add(panel, BorderLayout.NORTH);
        backBtnAct.setEnabled(false);
        saveBtnAct.setEnabled(false);
        FilterExprComponent.checkOrder(exprBtnAct, andBtnAct, orBtnAct,
                notBtnAct, lftBraketBtnAct, rghtBraketBtnAct);
        return tb;
    }

    private AbstractAction addExpressionLineAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                FilterExprComponent fcomp =
                        new FilterExprComponent(FilterExprComponent.Type.EXPRESSION, colNames);
                addLine(fcomp);
            }
        };
    }

    private AbstractAction addLeftBracketAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                addOperator(FilterExprComponent.Type.LEFT_BRACKET);
            }
        };
    }

    private AbstractAction addRightBracketAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                addOperator(FilterExprComponent.Type.RIGHT_BRACKET);
            }
        };
    }

    private AbstractAction addAndAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                addOperator(FilterExprComponent.Type.AND);
            }
        };
    }

    private AbstractAction addOrAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                addOperator(FilterExprComponent.Type.OR);
            }
        };
    }

    private AbstractAction addNotAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                addOperator(FilterExprComponent.Type.NOT);
            }
        };
    }

    private AbstractAction backSpaceAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Component[] comps = edComponentPanel.getComponents();
                if (comps.length > 2) {
                    edComponentPanel.remove(comps[comps.length - 1]);
                    edComponentPanel.updateUI();
                    backBtnAct.setEnabled(edComponentPanel.getComponents().length > 2);
                    FilterExprComponent.backSpace();
                    FilterExprComponent.checkOrder(exprBtnAct, andBtnAct, orBtnAct,
                            notBtnAct, lftBraketBtnAct, rghtBraketBtnAct);
                    setChanged(true);
                }
            }
        };
    }

    private void addOperator(FilterExprComponent.Type type) {
        addLine(new FilterExprComponent(type));
//        FilterExprComponent.checkOrder(exprBtnAct, andBtnAct, orBtnAct, notBtnAct, lftBraketBtnAct, rghtBraketBtnAct);
    }

    private void addLine(FilterExprComponent line) {
        edComponentPanel.add(line);
        edComponentPanel.revalidate();
        backBtnAct.setEnabled(true);
        int height = (int) upPerCenterPanel.getPreferredSize().getHeight();
        Rectangle rect = new Rectangle(0, height, 10, 10);
        upPerCenterPanel.scrollRectToVisible(rect);
        FilterExprComponent.checkOrder(exprBtnAct, andBtnAct, orBtnAct, notBtnAct, lftBraketBtnAct, rghtBraketBtnAct);
        setChanged(true);
    }

    private AbstractAction reloadAction(String lbl) {
        return new AbstractAction(lbl) {

            @Override
            public void actionPerformed(ActionEvent ae) {
                filterTable.reloadSelectedFilter();
            }  
        };
    }
    
    private AbstractAction saveAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Component[] comps = edComponentPanel.getComponents();
                Filter flt = filterTable.getSelectedFilter();
                if (flt != null) {
                    try {
                        flt.setName(filterNameTF.getText());
                        flt.setOwnerId(AIBclient.getCurrentUser().getUserId());
                        StringBuilder sb = new StringBuilder();
                        for (Component comp : comps) {
                            if (comp instanceof FilterExprComponent) {
                                FilterExprComponent fcomp = (FilterExprComponent) comp;
                                sb.append(fcomp.toString());
                            }
                        }
                        flt.setQuery(sb.toString());
                        AIBclient.getExchanger().saveDbObject(flt);
                        cfg.refresh();
                        setChanged(false);
                    } catch (Exception ex) {
                        AIBclient.log(ex);
                    }
                }
            }
        };
    }

    /**
     * @return the changed
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * @param changed the changed to set
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
        filterTable.setEnabled(!changed);
        boolean isComplete = isFilterComplete();
        changedLbl.setText(changed ? (!isFilterComplete() ? "incomplete expression" : "changed") : "saved");
        changedLbl.setForeground(changedLbl.getText().equals("saved") ? Color.BLUE : Color.RED);
        saveBtnAct.setEnabled(changed && isComplete);
    }

    private boolean isFilterComplete() {
        boolean complete = true;
        Component[] comps = edComponentPanel.getComponents();
        if (comps[comps.length - 1] instanceof FilterExprComponent) {
            FilterExprComponent fc = (FilterExprComponent) comps[comps.length - 1];
            complete = (fc.getType() != FilterExprComponent.Type.AND)
                    && (fc.getType() != FilterExprComponent.Type.OR)
                    && (fc.getType() != FilterExprComponent.Type.NOT)
                    && FilterExprComponent.getLevel() == 0;
        }
        return complete;
    }
}
