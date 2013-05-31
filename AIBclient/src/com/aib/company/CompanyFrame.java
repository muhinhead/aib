/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.company;

import com.aib.AIBclient;
import com.aib.FilteredListFrame;
import com.aib.filter.CompanyFilterPanel;
import com.aib.orm.dbobject.DbObject;
import com.aib.remote.IMessageSender;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

/**
 *
 * @author nick
 */
public class CompanyFrame extends FilteredListFrame {

    private static String[] sheetList = new String[]{
        "List", "Filter"
    };
    private static DbObject[] filterRecs = AIBclient.allFilters("company");
    private CompaniesGrid companiesPanel;
    private JPanel filterPanel;
    private JPanel editorPanel;
    private JPanel expressionPanel;
    private JPanel edLabelPanel;
    private JPanel edComponentPanel;
//    private JList filterList;
//    private JTextField filterNameTF;
    private JComboBox ownerCB;
    private DefaultComboBoxModel userCbModel;
//    private CompanyFilterGrid cfg;
    private JPanel resultPanel;
    private AbstractAction lftBraketBtn;
    private AbstractAction rghtBraketBtn;
    private JButton backBtn;
    private JPanel upPerCenterPanel;

    public CompanyFrame(IMessageSender exch) {
        super("Companies", exch);
    }

    @Override
    protected JPanel getListPanel() {
        if (companiesPanel == null) {
            try {
                registerGrid(companiesPanel = new CompaniesGrid(getExchanger()));
            } catch (RemoteException ex) {
                AIBclient.logAndShowMessage(ex);
            }
        }
        return companiesPanel;
    }

    @Override
    protected JPanel getFilterPanel() {
        if (filterPanel == null) {
            filterPanel = new JPanel(new BorderLayout());
            JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            sp.setTopComponent(new CompanyFilterPanel());
            sp.setBottomComponent(new JLabel("Here would be the query results", SwingConstants.CENTER));
            filterPanel.add(sp, BorderLayout.CENTER);
        }
        return filterPanel;
    }

    @Override
    protected ActionListener addNewFilterAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    @Override
    protected ActionListener editFilterAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    @Override
    protected ActionListener delFilterAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }
}
