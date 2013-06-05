/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.filter;

import com.aib.AIBclient;
import com.aib.FilteredListFrame;
import com.aib.GeneralFrame;
import com.aib.GeneralGridPanel;
import com.aib.MyJideTabbedPane;
import com.aib.RecordEditPanel;
import com.aib.orm.Filter;
import com.aib.orm.dbobject.ComboItem;
import com.aib.orm.dbobject.ForeignKeyViolationException;
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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private JTextField complexFilterNameTF;
    private JComboBox ownerCB;
    private AbstractAction exprBtnAct;
    private AbstractAction andBtnAct;
    private AbstractAction orBtnAct;
    private AbstractAction notBtnAct;
    private AbstractAction backBtnAct;
    private HashMap<String, Integer> colNamesTypes;
    private AbstractAction saveComplexBtnAct;
    private JLabel changedComplexQueryLbl;
    private JLabel changedSimpleQueryLbl;
    private JLabel changedLbl;
    private AbstractAction reloadComplexBtnAct;
    private MyJideTabbedPane complexOrSimpleTab;
    private JPanel edSimpleLabelPanel;
    private JPanel edSimpleComponentPanel;
    private JTextField simpleFilterNameTF;
    private AbstractAction saveSimpleBtnAct;
    private AbstractAction reloadSimleBtnAct;
    private AbstractAction applySimpleBtnAct;
    private AbstractAction applyComplexBtnAct;
    private int rowNumSimple = 0;
    private int rowNumComplex = 0;
//    private final GeneralGridPanel gridPanel;
    private final FilteredListFrame parentFrame;
    
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
    
    public CompanyFilterPanel(FilteredListFrame parentFrame) {
        super(new BorderLayout());
        this.parentFrame = parentFrame;
        try {
            colNamesTypes = AIBclient.getExchanger().getColNamesTypes(
                    "select * from company where company_id<0");
        } catch (RemoteException ex) {
            AIBclient.log(ex);
        }
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
        String simpleEditor = "Simple queries editor";
        String complexEditor = "Complex queries editor";
        complexOrSimpleTab = new MyJideTabbedPane();
        
        complexOrSimpleTab.add(simpleEditorPanel = new JPanel(new BorderLayout(10, 10)), simpleEditor);
        complexOrSimpleTab.add(complexEditorPanel = new JPanel(new BorderLayout(5, 5)), complexEditor);
        complexEditorPanel.add(getHeaderPanel(complexEditor), BorderLayout.NORTH);
        simpleEditorPanel.add(getHeaderPanel(simpleEditor), BorderLayout.NORTH);
        changedLbl = changedSimpleQueryLbl = new JLabel(" ", SwingConstants.LEFT);
        changedComplexQueryLbl = new JLabel(" ", SwingConstants.LEFT);
        
        complexOrSimpleTab.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                switch (complexOrSimpleTab.getSelectedIndex()) {
                    case 0:
                        changedLbl = changedSimpleQueryLbl;
                        cfg.setSelect(FilterGrid.SELECT.replace("@", "company")
                                .replace("where ", "where " + "(is_complex is null or not is_complex) and "));
                        cfg.refresh();
                        cfg.getTableView().gotoRow(getRowNumSimple());
                        break;
                    case 1:
                        changedLbl = changedComplexQueryLbl;
                        cfg.setSelect(FilterGrid.SELECT.replace("@", "company")
                                .replace("where ", "where " + "is_complex and "));
                        cfg.refresh();
                        cfg.getTableView().gotoRow(getRowNumComplex());
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
        JLabel hdr1;
        JLabel hdr2;
        
        fillSimpleEditorPanel(simpleEditorPanel, new JComponent[]{
            new JLabel(" Filter Name:", SwingConstants.RIGHT),
            changedSimpleQueryLbl,
            //            hdr1 = new JLabel("Company details", SwingConstants.LEFT),
            new JPanel(),
            new JLabel("Company name:", SwingConstants.RIGHT),
            new JLabel("Abbreviation:", SwingConstants.RIGHT),
            //            hdr2 = new JLabel("Contact person's details", SwingConstants.LEFT),
            new JPanel(),
            new JLabel("Title:", SwingConstants.RIGHT),
            new JLabel("First Name:", SwingConstants.RIGHT),
            new JLabel("Last Name:", SwingConstants.RIGHT),
            new JLabel("Suffix:", SwingConstants.RIGHT),
            new JLabel("Job Title:", SwingConstants.RIGHT),
            new JLabel("Email:", SwingConstants.RIGHT)
        }, new JComponent[]{
            simpleFilterNameTF = new JTextField(),
            new JPanel(),
            hdr1 = new JLabel("Company details", SwingConstants.LEFT),
            new ColumnFilterComponent(
            ColumnFilterComponent.FULLNAME, java.sql.Types.VARCHAR, this),
            new ColumnFilterComponent(
            ColumnFilterComponent.ABBREVIATION, java.sql.Types.VARCHAR, this),
            hdr2 = new JLabel("Contact person's details", SwingConstants.LEFT),
            new ColumnFilterComponent(
            ColumnFilterComponent.TITLE, java.sql.Types.VARCHAR, this),
            new ColumnFilterComponent(
            ColumnFilterComponent.FIRSTNAME, java.sql.Types.VARCHAR, this),
            new ColumnFilterComponent(
            ColumnFilterComponent.LASTNAME, java.sql.Types.VARCHAR, this),
            new ColumnFilterComponent(
            ColumnFilterComponent.SUFFIX, java.sql.Types.VARCHAR, this),
            new ColumnFilterComponent(
            ColumnFilterComponent.JOB, java.sql.Types.VARCHAR, this),
            new ColumnFilterComponent(
            ColumnFilterComponent.MAINEMAIL, java.sql.Types.VARCHAR, this)
        });
        hdr1.setForeground(Color.BLUE);
        hdr2.setForeground(Color.BLUE);
        
        changedComplexQueryLbl.setForeground(Color.BLUE);
        changedSimpleQueryLbl.setForeground(Color.BLUE);
        complexEditorPanel.add(getComplexToolbarPanel(), BorderLayout.EAST);
        simpleEditorPanel.add(getSimpleToolbarPanel(), BorderLayout.EAST);
        complexFilterNameTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                setChanged(true);
            }
        });
        simpleFilterNameTF.addKeyListener(new KeyAdapter() {
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
                            fcomp = new FilterExprComponent(
                                    FilterExprComponent.Type.EXPRESSION,
                                    colNamesTypes, this, line);
                            addLine(fcomp);
                        }
                    }
                }
            }
            FilterExprComponent.checkOrder(exprBtnAct, andBtnAct, orBtnAct, notBtnAct, lftBraketBtnAct, rghtBraketBtnAct);
            setRowNumComplex(cfg.getTableView().getSelectedRow());
        } else {
            //TODO: load simple filter
            simpleFilterNameTF.setText(filter == null ? "" : filter.getName());
            setRowNumSimple(cfg.getTableView().getSelectedRow());
            Component[] comps = edSimpleComponentPanel.getComponents();
            Filter flt = getSelectedFilter();
            if (flt != null) {
                String expression = flt.getQuery();
                for (Component comp : comps) {
                    if (comp instanceof ColumnFilterComponent) {
                        ColumnFilterComponent fc = (ColumnFilterComponent) comp;
                        fc.load(expression);
                    }
                }
            }
        }
        setChanged(false);
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
        
        edSimpleComponentPanel = new JPanel(new GridLayout(comps.length, 1, 5, 5));
        
        for (JComponent lbl : labels) {
            edSimpleLabelPanel.add(lbl);
        }
        for (JComponent comp : comps) {
            edSimpleComponentPanel.add(comp);
        }
        upperPanel.add(edSimpleLabelPanel, BorderLayout.WEST);
        upperPanel.add(edSimpleComponentPanel, BorderLayout.CENTER);
        JPanel uprPanel = new JPanel(new BorderLayout());
        uprPanel.add(upperPanel, BorderLayout.NORTH);
        papa.add(new JScrollPane(uprPanel), BorderLayout.CENTER);
    }
    
    private JPanel getSimpleToolbarPanel() {
        JPanel tb = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridLayout(3, 1, 2, 2));
        panel.add(new JButton(saveSimpleBtnAct = saveSimpleAction("Save")));
        panel.add(new JButton(reloadSimleBtnAct = reloadAction("Reload")));
        panel.add(new JButton(applySimpleBtnAct = applySimpleAction("Apply")));
        tb.add(panel, BorderLayout.NORTH);
        saveSimpleBtnAct.setEnabled(false);
        return tb;
    }
    
    private JPanel getComplexToolbarPanel() {
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
        panel.add(new JButton(reloadComplexBtnAct = reloadAction("Reload")));
        panel.add(new JButton(applyComplexBtnAct = applyComplexAction("Apply")));
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
    
    private AbstractAction applySimpleAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                parentFrame.applyFilter(saveSimpleFilter());
            }
        };
    }
    
    private AbstractAction applyComplexAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                parentFrame.applyFilter(saveComplexFilter());
            }
        };
    }
    
    private AbstractAction saveSimpleAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                saveSimpleFilter();
            }
        };
    }
    
    private Filter saveSimpleFilter() {
        Component[] comps = edSimpleComponentPanel.getComponents();
        Filter flt = getSelectedFilter();
        if (flt != null) {
            try {
                flt.setName(simpleFilterNameTF.getText());
                flt.setOwnerId(AIBclient.getCurrentUser().getUserId());
                StringBuilder sb = new StringBuilder();
                for (Component comp : comps) {
                    String str = comp.toString();
                    if (comp instanceof ColumnFilterComponent && str.length() > 0) {
                        sb.append(sb.length() > 0 ? " AND " : "").append(str);
                    }
                }
                flt.setQuery(sb.toString());
                AIBclient.getExchanger().saveDbObject(flt);
                cfg.refresh();
                setChanged(false);
                return flt;
            } catch (Exception ex) {
                AIBclient.log(ex);
            }
        }
        return null;
    }
    
    private Filter saveComplexFilter() {
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
                return flt;
            } catch (Exception ex) {
                AIBclient.log(ex);
            }
        }
        return null;
    }
    
    private AbstractAction saveComplexAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                saveComplexFilter();
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
            changedLbl.setText(changed ? (!isFilterComplete() ? " incomplete" : " changed") : " saved");
            saveComplexBtnAct.setEnabled(changed && isComplete);
        } else {
            changedLbl.setText(changed ? " changed" : " saved");
            saveSimpleBtnAct.setEnabled(changed);
        }
        changedLbl.setForeground(changedLbl.getText().equals(" saved") ? Color.BLUE : Color.RED);
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

    /**
     * @return the rowNumComplex
     */
    public int getRowNumComplex() {
        return rowNumComplex;
    }

    /**
     * @param rowNumComplex the rowNumComplex to set
     */
    public void setRowNumComplex(int rowNumComplex) {
        this.rowNumComplex = rowNumComplex < 0 ? 0 : rowNumComplex;
    }
}
