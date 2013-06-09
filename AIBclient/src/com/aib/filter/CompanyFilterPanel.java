/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.filter;

import com.aib.AIBclient;
import com.aib.FilteredListFrame;
import com.aib.MyJideTabbedPane;
import static com.aib.filter.AbstractFilterPanel.enableIfFilterSelected;
import com.aib.orm.Filter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Nick Mukhin
 */
public class CompanyFilterPanel extends AbstractFilterPanel {

    public CompanyFilterPanel(FilteredListFrame parentFrame) {
        super(parentFrame, "company");
    }

    @Override
    protected void loadColNamesTypes() throws RemoteException {
        super.loadColNamesTypes();
        colNames.add("Links");
        colNamesTypes.put("Links",java.sql.Types.VARCHAR);
    }

    @Override
    protected JComponent getFilterEditor() {
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
                        fillSimpleFilterList();
                        break;
                    case 1:
                        changedLbl = changedComplexQueryLbl;
                        fillComplexFilterList();
                        break;
                }
            }
        });

        fillComplexHeaderPanel();
        fillSimpleHeaderPanel();

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

    private void fillSimpleHeaderPanel() {
        JLabel hdr1;
        JLabel hdr2;
        JComponent[] comps;
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
        }, comps = new JComponent[]{
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
        for (JComponent comp : comps) {
            comp.setEnabled(false);
            if (comp instanceof JPanel) {
                enableIfFilterSelected((JPanel) comp, false);
            }
        }
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

    @Override
    protected void loadFilter(Filter filter) {

        if (complexOrSimpleTab.getSelectedIndex() == 1) {
            super.loadFilter(filter);
        } else {
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
            enableIfFilterSelected(edSimpleComponentPanel, filter != null);
            setChanged(false);
        }
    }

    /**
     * @param changed the changed to set
     */
    @Override
    public void setChanged(boolean changed) {
        this.changed = changed;
        filterTable.setEnabled(!changed);
        if (complexOrSimpleTab.getSelectedIndex() == 1) {
            setComplexChanged(changed);
        } else {
            changedLbl.setText(changed ? " changed" : " saved");
            saveSimpleBtnAct.setEnabled(changed);
        }
        setChangedLabelColor();
    }

    private AbstractAction applySimpleAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                parentFrame.applyFilter(saveSimpleFilter());
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
                parentFrame.reloadFilterComboBox();
                return flt;
            } catch (Exception ex) {
                AIBclient.log(ex);
            }
        }
        return null;
    }
}
