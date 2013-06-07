package com.aib;

import com.aib.people.PeopleGrid;
import com.aib.FilteredListFrame;
import com.aib.filter.PeopleFilterPanel;
import com.aib.orm.Filter;
import com.aib.remote.IMessageSender;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nick Mukhin
 */
public class PeopleFrame extends FilteredListFrame {

    private static String[] sheetList = new String[]{
        "List", "Filter"
    };
    private PeopleGrid peoplePanel;

    public PeopleFrame(IMessageSender exch) {
        super("People", exch);
    }

    @Override
    protected JPanel getListPanel() {
        if (peoplePanel == null) {
            try {
                registerGrid(peoplePanel = new PeopleGrid(getExchanger()));
            } catch (RemoteException ex) {
                AIBclient.logAndShowMessage(ex);
            }
        }
        return peoplePanel;
    }

    @Override
    protected JPanel getFilterPanel() {
        if (filterPanel == null) {
            filterPanel = new JPanel(new BorderLayout());
            JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            sp.setTopComponent(new PeopleFilterPanel(this));
            sp.setBottomComponent(new JLabel("Here should be a query expression", SwingConstants.CENTER));
            filterPanel.add(sp, BorderLayout.CENTER);
        }
        return filterPanel;
    }

    @Override
    protected ActionListener addNewFilterAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }

    @Override
    protected ActionListener editFilterAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }
    @Override
    protected ActionListener delFilterAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }

    @Override
    public void applyFilter(Filter flt) {
        //TODO !
    }

    @Override
    protected String getMainTableName() {
        return "people";
    }
    
    @Override
    protected ActionListener getChooseFilterAction() {
        return new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                //TODO !
            }
            
        };
    }
}