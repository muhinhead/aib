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
import java.util.HashMap;
import java.util.StringTokenizer;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author Nick Mukhin
 */
public class CompanyFilterPanel extends JPanel implements IFilterPanel {

    private JPanel complexEditorPanel;
    private JPanel simpleEditorPanel;
    private boolean changed;
    private FilterTable filterTable;
    private FilterGrid cfg;
    private DefaultComboBoxModel userCbModel;
    private JPanel edComplexLabelPanel;
    private JPanel edComplexComponentPanel;
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
    private HashMap<String, Integer> colNamesTypes;
    private AbstractAction saveComplexBtnAct;
    private JLabel changedComplexQueryLbl;
    private AbstractAction reloadBtnAct;
//    private final CompanyFilterPanel _this;
    private MyJideTabbedPane complexOrSimpleTab;
    private JPanel edSimpleLabelPanel;
    private JPanel edSimpleComponentPanel;
    private JLabel changedSimpleQueryLbl;
    private JTextField simpleFilterNameTF;

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
            colNamesTypes = AIBclient.getExchanger().getColNamesTypes(
                    "select * from company where company_id<0");
        } catch (RemoteException ex) {
            AIBclient.log(ex);
        }
        JScrollPane sp;
        JSplitPane split = new JSplitPane();
        try {
            split.setTopComponent((Component) (cfg = new FilterGrid(
                    AIBclient.getExchanger(), "company", false,
                    filterTable = new FilterTable())));
        } catch (RemoteException ex) {
            AIBclient.logAndShowMessage(ex);
        }

        cfg.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Saved filters"));
        cfg.setPreferredSize(new Dimension(300, 200));
        split.setMinimumSize(new Dimension(split.getPreferredSize().width, 200));
        split.setBottomComponent(getFilterEditor());

        add(split);
    }

    public Filter getSelectedFilter() {
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
        String simpleEditor = "Simplified queries editor";
        String complexEditor = "Complex queries editor";
        complexOrSimpleTab = new MyJideTabbedPane();

        complexOrSimpleTab.add(simpleEditorPanel = new JPanel(new BorderLayout(10, 10)), simpleEditor);
        complexOrSimpleTab.add(complexEditorPanel = new JPanel(new BorderLayout(5, 5)), complexEditor);
        complexEditorPanel.add(getHeaderPanel(complexEditor), BorderLayout.NORTH);
        simpleEditorPanel.add(getHeaderPanel(simpleEditor), BorderLayout.NORTH);

        complexOrSimpleTab.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                switch (complexOrSimpleTab.getSelectedIndex()) {
                    case 0:
                        cfg.setSelect(FilterGrid.SELECT.replace("@", "company")
                                .replace("where ", "where " + "not is_complex and "));
                        cfg.refresh();
                        break;
                    case 1:
                        cfg.setSelect(FilterGrid.SELECT.replace("@", "company")
                                .replace("where ", "where " + "is_complex and "));
                        cfg.refresh();
                        break;
                }
            }
        });

        userCbModel = new DefaultComboBoxModel();
        for (ComboItem ci : AIBclient.loadAllUsersInitials()) {
            userCbModel.addElement(ci);
        }

        fillComplexEditorPanel(complexEditorPanel, new JComponent[]{
            new JLabel(" Filter Name:", SwingConstants.RIGHT),
            changedComplexQueryLbl = new JLabel(" ", SwingConstants.RIGHT)
        }, new JComponent[]{
            RecordEditPanel.getGridPanel(new JComponent[]{
                filterNameTF = new JTextField(),
                RecordEditPanel.getBorderPanel(new JComponent[]{
                    new JLabel(" Owner:", SwingConstants.RIGHT),
                    ownerCB = new JComboBox(userCbModel)
                })
            }),
            new JPanel()
        });
        JLabel hdr1;
        JLabel hdr2;
        //TODO: here should be simple query editor pane filling
        fillSimpleEditorPanel(simpleEditorPanel, new JComponent[]{
            new JLabel(" Filter Name:", SwingConstants.RIGHT),
            changedSimpleQueryLbl = new JLabel(" ", SwingConstants.RIGHT),
            hdr1 = new JLabel("Company details", SwingConstants.LEFT),
            new JLabel("Company name:", SwingConstants.RIGHT),
            new JLabel("Abbreviation:", SwingConstants.RIGHT),
            hdr2 = new JLabel("Contact person's details", SwingConstants.LEFT),
            new JLabel("Title:", SwingConstants.RIGHT),
            new JLabel("First Name:", SwingConstants.RIGHT),
            new JLabel("Last Name:", SwingConstants.RIGHT),
            new JLabel("Suffix:", SwingConstants.RIGHT),
            new JLabel("Job Title:", SwingConstants.RIGHT),
            new JLabel("Email:", SwingConstants.RIGHT)
        }, new JComponent[]{
            simpleFilterNameTF = new JTextField(),
            new JPanel(),
            new JSeparator(),
            new ColumnFilterComponent("company.full_name", java.sql.Types.VARCHAR, this),
            new ColumnFilterComponent("company.abbreviation", java.sql.Types.VARCHAR, this),
            new JSeparator(),
            new ColumnFilterComponent("people.title", java.sql.Types.VARCHAR, this),
            new ColumnFilterComponent("people.first_name", java.sql.Types.VARCHAR, this),
            new ColumnFilterComponent("people.last_name", java.sql.Types.VARCHAR, this),
            new ColumnFilterComponent("people.suffix", java.sql.Types.VARCHAR, this),
            new ColumnFilterComponent("people.job_discip", java.sql.Types.VARCHAR, this),
            new ColumnFilterComponent("people.main_email", java.sql.Types.VARCHAR, this)
        });
        hdr1.setForeground(Color.BLUE);
        hdr2.setForeground(Color.BLUE);

        changedComplexQueryLbl.setForeground(Color.BLUE);
        complexEditorPanel.add(getToolbarPanel(), BorderLayout.EAST);
        filterNameTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                setChanged(true);
            }
        });
        return complexOrSimpleTab;
    }

    private void loadFilter(Filter filter) {
        if (complexOrSimpleTab.getSelectedIndex() == 1) {
            Component[] comps = edComplexComponentPanel.getComponents();
            for (int i = comps.length - 1; i > 1; i--) {
                edComplexComponentPanel.remove(comps[i]);
            }
            edComplexComponentPanel.revalidate();
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
                            fcomp = new FilterExprComponent(
                                    FilterExprComponent.Type.EXPRESSION,
                                    colNamesTypes, this, line);
                            addLine(fcomp);
                        }
                    }
                }
            }
            FilterExprComponent.checkOrder(exprBtnAct, andBtnAct, orBtnAct, notBtnAct, lftBraketBtnAct, rghtBraketBtnAct);
            setChanged(false);
        } else {
            //TODO: load simple filter
        }
    }

    private void fillComplexEditorPanel(JPanel papa, JComponent[] labels, JComponent[] comps) {
        JPanel upperPanel = new JPanel(new BorderLayout());
        edComplexLabelPanel = new JPanel();
        edComplexLabelPanel.setLayout(new BoxLayout(edComplexLabelPanel, BoxLayout.Y_AXIS));
        edComplexComponentPanel = new JPanel();
        edComplexComponentPanel.setLayout(new BoxLayout(edComplexComponentPanel, BoxLayout.Y_AXIS));

        for (JComponent lbl : labels) {
            edComplexLabelPanel.add(lbl);
        }
        for (JComponent comp : comps) {
            edComplexComponentPanel.add(comp);
        }

        upperPanel.add(edComplexLabelPanel, BorderLayout.WEST);
        upperPanel.add(edComplexComponentPanel, BorderLayout.CENTER);
        upPerCenterPanel = new JPanel(new BorderLayout());
        upPerCenterPanel.add(upperPanel, BorderLayout.NORTH);
        papa.add(new JScrollPane(upPerCenterPanel), BorderLayout.CENTER);
    }

    private void fillSimpleEditorPanel(JPanel papa, JComponent[] labels, JComponent[] comps) {
        JPanel upperPanel = new JPanel(new BorderLayout());
        edSimpleLabelPanel = new JPanel(new GridLayout(labels.length, 1, 5, 5));
//        edSimpleLabelPanel.setLayout(new BoxLayout(edSimpleLabelPanel, BoxLayout.Y_AXIS));

        edSimpleComponentPanel = new JPanel(new GridLayout(comps.length, 1, 5, 5));
//        edSimpleComponentPanel.setLayout(new BoxLayout(edSimpleComponentPanel, BoxLayout.Y_AXIS));

        for (JComponent lbl : labels) {
            edSimpleLabelPanel.add(lbl);
        }
//        for (int i=0; i<comps.length-labels.length;i++) {
//            edSimpleLabelPanel.add(new JPanel());
//        }
        for (JComponent comp : comps) {
            edSimpleComponentPanel.add(comp);
        }
//        for (int i=0; i<labels.length-comps.length;i++) {
//            edSimpleComponentPanel.add(new JPanel());
//        }
        upperPanel.add(edSimpleLabelPanel, BorderLayout.WEST);
        upperPanel.add(edSimpleComponentPanel, BorderLayout.CENTER);
        JPanel uprPanel = new JPanel(new BorderLayout());
        uprPanel.add(upperPanel, BorderLayout.NORTH);
        papa.add(new JScrollPane(uprPanel), BorderLayout.CENTER);
    }

    private JPanel getToolbarPanel() {
        JPanel tb = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridLayout(11, 1, 2, 2));
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
        panel.add(new JButton(saveComplexBtnAct = saveComplexAction("Save")));
        panel.add(new JButton(reloadBtnAct = reloadAction("Reload")));
        panel.add(new JButton("Apply"));
        tb.add(panel, BorderLayout.NORTH);
        backBtnAct.setEnabled(false);
        saveComplexBtnAct.setEnabled(false);
        FilterExprComponent.checkOrder(exprBtnAct, andBtnAct, orBtnAct,
                notBtnAct, lftBraketBtnAct, rghtBraketBtnAct);
        return tb;
    }

    private AbstractAction addExpressionLineAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                FilterExprComponent fcomp;
                fcomp = new FilterExprComponent(FilterExprComponent.Type.EXPRESSION,
                        colNamesTypes, CompanyFilterPanel.this, null);
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
                Component[] comps = edComplexComponentPanel.getComponents();
                if (comps.length > 2) {
                    edComplexComponentPanel.remove(comps[comps.length - 1]);
                    edComplexComponentPanel.updateUI();
                    backBtnAct.setEnabled(edComplexComponentPanel.getComponents().length > 2);
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
        edComplexComponentPanel.add(line);
        edComplexComponentPanel.revalidate();
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

    private AbstractAction saveComplexAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Component[] comps = edComplexComponentPanel.getComponents();
                Filter flt = getSelectedFilter();
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
    @Override
    public boolean isChanged() {
        return changed;
    }

    /**
     * @param changed the changed to set
     */
    @Override
    public void setChanged(boolean changed) {
        this.changed = changed;
        filterTable.setEnabled(!changed);
        if (complexOrSimpleTab.getSelectedIndex() == 1) {
            boolean isComplete = isFilterComplete();
            changedComplexQueryLbl.setText(changed ? (!isFilterComplete() ? " incomplete" : " changed") : " saved");
            changedComplexQueryLbl.setForeground(changedComplexQueryLbl.getText().equals(" saved") ? Color.BLUE : Color.RED);
            saveComplexBtnAct.setEnabled(changed && isComplete);
        }
    }

    private boolean isFilterComplete() {
        boolean complete = true;
        Component[] comps = edComplexComponentPanel.getComponents();
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
