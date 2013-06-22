/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.people;

import com.aib.AIBclient;
import com.aib.GeneralGridPanel;
import com.aib.remote.IMessageSender;
import com.xlend.util.PopupDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author nick
 */
class DepartmentsHistoryDialog extends PopupDialog {

    private static HashMap<Integer, Integer> maxWidths = new HashMap<Integer, Integer>();

    static {
        maxWidths.put(0, 40);
    }

    class DepartmentsGrid extends GeneralGridPanel {

        public DepartmentsGrid() throws RemoteException {
            super(AIBclient.getExchanger(), "select departmenthistory_id \"Id\", department \"Department\",until_date \"Until\" "
                    + "from departmenthistory where people_id="+getObject().toString()+" order by until_date desc", maxWidths, false);
        }

        @Override
        protected AbstractAction addAction() {
            return null;
        }

        @Override
        protected AbstractAction editAction() {
            return null;
        }

        @Override
        protected AbstractAction delAction() {
            return null;
        }
    }

    public DepartmentsHistoryDialog(Integer peopleID) {
        super(null, "Departments history", peopleID);
    }

    @Override
    protected Color getHeaderBackground() {
        return new Color(102, 125, 158);
    }

    @Override
    protected void fillContent() {
        super.fillContent();
        add(new JPanel(), BorderLayout.WEST);
//        add(new JPanel(), BorderLayout.EAST);
        try {
            add(new DepartmentsGrid());
        } catch (RemoteException ex) {
            AIBclient.logAndShowMessage(ex);
        }
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(new JButton(new AbstractAction("Ok"){

            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
        }));
        add(btnPanel, BorderLayout.SOUTH);
    }

    @Override
    public void freeResources() {
        //
    }
}
