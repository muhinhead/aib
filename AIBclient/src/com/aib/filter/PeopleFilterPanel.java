/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.filter;

import com.aib.AIBclient;
import com.aib.FilteredListFrame;
import com.aib.MyJideTabbedPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author nick
 */
public class PeopleFilterPanel  extends AbstractFilterPanel {
    public PeopleFilterPanel(FilteredListFrame parentFrame) {
        super(parentFrame, "people");
    }

    @Override
    protected void loadColNamesTypes() throws RemoteException {
        colNamesTypes = AIBclient.getExchanger().getColNamesTypes(
                "select * from people where people_id<0");
    }

    @Override
    protected JComponent getFilterEditor() {
        String complexEditor = "Queries editor";
        complexOrSimpleTab = new MyJideTabbedPane();
        complexOrSimpleTab.add(complexEditorPanel = new JPanel(new BorderLayout(5, 5)), complexEditor);
        complexEditorPanel.add(getHeaderPanel(complexEditor), BorderLayout.NORTH);
        changedLbl = changedComplexQueryLbl = new JLabel(" ", SwingConstants.LEFT);
        changedComplexQueryLbl = new JLabel(" ", SwingConstants.LEFT);
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
    
}
