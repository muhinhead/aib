/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.filter;

import com.aib.AIBclient;
import com.aib.FilteredListFrame;
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
import java.util.ArrayList;
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
public abstract class AbstractFilterPanel extends JPanel implements IFilterPanel {

    protected static Color getHeaderForeground() {
        return new Color(255, 255, 255);
    }

    protected static Color getHederBackground() {
        return new Color(102, 125, 158);
    }
    protected JPanel simpleEditorPanel;
    protected JLabel changedSimpleQueryLbl;
    protected JPanel edSimpleLabelPanel;
    protected JPanel edSimpleComponentPanel;
    protected JTextField simpleFilterNameTF;
    protected AbstractAction saveSimpleBtnAct;
    protected AbstractAction reloadSimleBtnAct;
    protected AbstractAction applySimpleBtnAct;
    protected AbstractAction andBtnAct;
    protected AbstractAction applyComplexBtnAct;
    protected AbstractAction backBtnAct;
    protected FilterGrid cfg;
    protected boolean changed;
    protected JLabel changedComplexQueryLbl;
    protected JLabel changedLbl;
    protected HashMap<String, Integer> colNamesTypes;
    protected ArrayList<String> colNames;
    protected JPanel complexEditorPanel;
    protected JTextField complexFilterNameTF;
    protected MyJideTabbedPane complexOrSimpleTab;
    protected JPanel edComplexComponentPanel;
    protected JPanel edComplexLabelPanel;
    protected AbstractAction exprBtnAct;
    protected FilterTable filterTable;
    protected AbstractAction lftBraketBtnAct;
    protected AbstractAction notBtnAct;
    protected AbstractAction orBtnAct;
    protected JComboBox ownerCB;
    //    private final GeneralGridPanel gridPanel;
    protected final FilteredListFrame parentFrame;
    protected AbstractAction reloadComplexBtnAct;
    protected AbstractAction rghtBraketBtnAct;
    protected int rowNumComplex = 0;
    protected AbstractAction saveComplexBtnAct;
    protected JPanel upPerCenterPanel;
    protected DefaultComboBoxModel userCbModel;
    protected final JSplitPane split;
    private int rowNumSimple = 0;
    private final String tabName;

    protected class FilterTable extends DbTableView {

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

    protected boolean isDefaultComplex() {
        return false;
    }

    public AbstractFilterPanel(FilteredListFrame parentFrame, String tabName) {
        super(new BorderLayout());
        this.parentFrame = parentFrame;
        this.tabName = tabName;
        userCbModel = new DefaultComboBoxModel();
        for (ComboItem ci : AIBclient.loadAllUsersInitials()) {
            userCbModel.addElement(ci);
        }
        try {
            loadColNamesTypes();
        } catch (RemoteException ex) {
            AIBclient.log(ex);
        }
        split = new JSplitPane();
        JComponent filterEditor = getFilterEditor();
        try {
            split.setTopComponent((Component) (cfg = new FilterGrid(
                    AIBclient.getExchanger(), tabName, isDefaultComplex(),
                    filterTable = new FilterTable(), parentFrame)));
        } catch (RemoteException ex) {
            AIBclient.logAndShowMessage(ex);
        }
        split.setDividerLocation(400);
        cfg.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Saved filters"));
        cfg.setPreferredSize(new Dimension(300, 200));
        split.setMinimumSize(new Dimension(split.getPreferredSize().width, 200));
        split.setBottomComponent(filterEditor);
        add(split);
    }

    protected void fillSimpleFilterList() {
        cfg.setSelect(FilterGrid.SELECT.replace("@", tabName)
                .replace("where ", "where " + "(is_complex is null or not is_complex) and "));
        cfg.refresh();
        cfg.getTableView().gotoRow(getRowNumSimple());
    }

    protected void fillComplexFilterList() {
        cfg.setSelect(FilterGrid.SELECT.replace("@", tabName)
                .replace("where ", "where " + "is_complex and "));
        cfg.refresh();
        cfg.getTableView().gotoRow(getRowNumComplex());
    }

    protected void fillComplexHeaderPanel() {
        fillComplexEditorPanel(complexEditorPanel, new JComponent[]{
            new JLabel(" Filter Name:", SwingConstants.RIGHT),
            changedComplexQueryLbl
        }, new JComponent[]{
            RecordEditPanel.getGridPanel(new JComponent[]{
                complexFilterNameTF = new JTextField(),
                RecordEditPanel.getBorderPanel(new JComponent[]{
                    new JLabel(" Owner:", SwingConstants.RIGHT),
                    ownerCB = new JComboBox(userCbModel)
                })
            }),
            new JPanel()
        });
        complexFilterNameTF.setEnabled(false);
        ownerCB.setEnabled(false);
    }
    
    protected JComponent getFilterEditor() {
        String complexEditor = "Queries editor";
        complexOrSimpleTab = new MyJideTabbedPane();
        complexOrSimpleTab.add(complexEditorPanel = new JPanel(new BorderLayout(5, 5)), complexEditor);
        complexEditorPanel.add(getHeaderPanel(complexEditor), BorderLayout.NORTH);
        changedLbl = changedComplexQueryLbl = new JLabel(" ", SwingConstants.LEFT);
//        changedComplexQueryLbl = new JLabel(" ", SwingConstants.LEFT);
        complexOrSimpleTab.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                switch (complexOrSimpleTab.getSelectedIndex()) {
                    case 0:
                        changedLbl = changedComplexQueryLbl;
                        fillComplexFilterList();
                        break;
                    //ADD if needed
                }
            }
        });
        fillComplexHeaderPanel();
        changedComplexQueryLbl.setForeground(Color.BLUE);
        complexEditorPanel.add(getComplexToolbarPanel(), BorderLayout.EAST);
        complexFilterNameTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                setChanged(true);
            }
        });
        return complexOrSimpleTab;
    }
    
    protected void loadColNamesTypes() throws RemoteException {
        Object[] obs = AIBclient.getExchanger().getColNamesTypes(
                "select * from " + tabName + " where " + tabName + "_id<0");
        colNames = (ArrayList<String>) obs[0];
        colNames.remove(tabName + "_id");
        colNamesTypes = (HashMap<String, Integer>) obs[1];
    }

    protected AbstractAction addAndAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                addOperator(FilterExprComponent.Type.AND);
            }
        };
    }

    protected AbstractAction addExpressionLineAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                FilterExprComponent fcomp;
                fcomp = new FilterExprComponent(FilterExprComponent.Type.EXPRESSION,
                        colNamesTypes, colNames,
                        AbstractFilterPanel.this, null);
                addLine(fcomp);
            }
        };
    }

    protected AbstractAction addLeftBracketAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                addOperator(FilterExprComponent.Type.LEFT_BRACKET);
            }
        };
    }

    protected void addLine(FilterExprComponent line) {
        edComplexComponentPanel.add(line);
        edComplexComponentPanel.revalidate();
        backBtnAct.setEnabled(true);
        int height = (int) upPerCenterPanel.getPreferredSize().getHeight();
        Rectangle rect = new Rectangle(0, height, 10, 10);
        upPerCenterPanel.scrollRectToVisible(rect);
        FilterExprComponent.checkOrder(exprBtnAct, andBtnAct, orBtnAct, notBtnAct, lftBraketBtnAct, rghtBraketBtnAct);
        setChanged(true);
    }

    protected AbstractAction addNotAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                addOperator(FilterExprComponent.Type.NOT);
            }
        };
    }

    protected void addOperator(FilterExprComponent.Type type) {
        addLine(new FilterExprComponent(type));
    }

    protected AbstractAction addOrAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                addOperator(FilterExprComponent.Type.OR);
            }
        };
    }

    protected AbstractAction addRightBracketAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                addOperator(FilterExprComponent.Type.RIGHT_BRACKET);
            }
        };
    }

    protected AbstractAction applyComplexAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                parentFrame.applyFilter(saveComplexFilter());
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

    protected void fillComplexEditorPanel(JPanel papa, JComponent[] labels, JComponent[] comps) {
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

    protected JPanel getComplexToolbarPanel() {
        JPanel tb = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridLayout(11, 1, 2, 2));
        panel.add(new JButton(exprBtnAct = addExpressionLineAction("EXPR")));
        panel.add(new JButton(andBtnAct = addAndAction("AND")));
        panel.add(new JButton(orBtnAct = addOrAction("OR")));
        panel.add(new JButton(notBtnAct = addNotAction("NOT")));
        panel.add(RecordEditPanel.getGridPanel(new JComponent[]{new JButton(lftBraketBtnAct = addLeftBracketAction("(")), new JButton(rghtBraketBtnAct = addRightBracketAction(")"))}));
        panel.add(new JButton(backBtnAct = backSpaceAction("Back")));
        panel.add(new JSeparator());
        panel.add(new JButton(saveComplexBtnAct = saveComplexAction("Save")));
        panel.add(new JButton(reloadComplexBtnAct = reloadAction("Reload")));
        panel.add(new JButton(applyComplexBtnAct = applyComplexAction("Apply")));
        tb.add(panel, BorderLayout.NORTH);
        backBtnAct.setEnabled(false);
        saveComplexBtnAct.setEnabled(false);
        FilterExprComponent.checkOrder(exprBtnAct, andBtnAct, orBtnAct, notBtnAct, lftBraketBtnAct, rghtBraketBtnAct);
        return tb;
    }

    protected JPanel getHeaderPanel(String title) {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(getHederBackground());
        JLabel lbl = new JLabel(title, SwingConstants.CENTER);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 18));
        lbl.setForeground(getHeaderForeground());
        headerPanel.add(lbl);
        return headerPanel;
    }

    /**
     * @return the rowNumComplex
     */
    public int getRowNumComplex() {
        return rowNumComplex;
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

    /**
     * @return the changed
     */
    @Override
    public boolean isChanged() {
        return changed;
    }

    protected boolean isFilterComplete() {
        boolean complete = true;
        Component[] comps = edComplexComponentPanel.getComponents();
        if (comps[comps.length - 1] instanceof FilterExprComponent) {
            FilterExprComponent fc = (FilterExprComponent) comps[comps.length - 1];
            complete = (fc.getType() != FilterExprComponent.Type.AND) && (fc.getType() != FilterExprComponent.Type.OR) && (fc.getType() != FilterExprComponent.Type.NOT) && FilterExprComponent.getLevel() == 0;
        }
        return complete;
    }

    protected static void enableIfFilterSelected(JPanel edPanel, boolean enable) {
        Component[] comps = edPanel.getComponents();
        for (Component comp : comps) {
            comp.setEnabled(enable);
            if (comp instanceof JPanel) {
                enableIfFilterSelected((JPanel) comp, enable);
            }
        }
    }

    protected void loadFilter(Filter filter) {
        Component[] comps = edComplexComponentPanel.getComponents();
        for (int i = comps.length - 1; i > 1; i--) {
            edComplexComponentPanel.remove(comps[i]);
        }
        edComplexComponentPanel.revalidate();
        complexFilterNameTF.setText(filter == null ? "" : filter.getName());
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
                        fcomp = new FilterExprComponent(FilterExprComponent.Type.EXPRESSION,
                                colNamesTypes, colNames, this, line);
                        addLine(fcomp);
                    }
                }
            }
        }
        enableIfFilterSelected(edComplexComponentPanel, filter != null);
        FilterExprComponent.checkOrder(exprBtnAct, andBtnAct, orBtnAct, notBtnAct, lftBraketBtnAct, rghtBraketBtnAct);
        setRowNumComplex(cfg.getTableView().getSelectedRow());
        setChanged(false);
    }

    protected AbstractAction reloadAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                filterTable.reloadSelectedFilter();
            }
        };
    }

    protected AbstractAction saveComplexAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                saveComplexFilter();
            }
        };
    }

    protected Filter saveComplexFilter() {
        Component[] comps = edComplexComponentPanel.getComponents();
        Filter flt = getSelectedFilter();
        if (flt != null) {
            try {
                flt.setName(complexFilterNameTF.getText());
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
                parentFrame.reloadFilterComboBox();
                return flt;
            } catch (Exception ex) {
                AIBclient.log(ex);
            }
        }
        return null;
    }

    /**
     * @param changed the changed to set
     */
    @Override
    public void setChanged(boolean changed) {
        this.changed = changed;
        filterTable.setEnabled(!changed);
        setComplexChanged(changed);
        setChangedLabelColor();
    }

    protected void setComplexChanged(boolean changed) {
        boolean isComplete = isFilterComplete();
        changedLbl.setText(changed ? (!isFilterComplete() ? " incomplete" : " changed") : " saved");
        saveComplexBtnAct.setEnabled(changed && isComplete);
    }

    protected void setChangedLabelColor() {
        changedLbl.setForeground(changedLbl.getText().equals(" saved") ? Color.BLUE : Color.RED);
    }

    /**
     * @param rowNumComplex the rowNumComplex to set
     */
    public void setRowNumComplex(int rowNumComplex) {
        this.rowNumComplex = rowNumComplex < 0 ? 0 : rowNumComplex;
    }

    /**
     * @return the rowNumSimple
     */
    public int getRowNumSimple() {
        return rowNumSimple;
    }

    /**
     * @param rowNumSimple the rowNumSimple to set
     */
    public void setRowNumSimple(int rowNumSimple) {
        this.rowNumSimple = rowNumSimple < 0 ? 0 : rowNumSimple;
    }
}
