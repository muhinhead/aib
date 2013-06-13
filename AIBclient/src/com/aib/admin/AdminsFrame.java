/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.admin;

import com.aib.AIBclient;
import com.aib.GeneralFrame;
import com.aib.GeneralGridPanel;
import com.aib.MyJideTabbedPane;
import com.aib.remote.IMessageSender;
import java.rmi.RemoteException;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Nick Mukhin
 */
public class AdminsFrame extends GeneralFrame {
    
    private GeneralGridPanel usersPanel;
    
    private static String[] sheetList = new String[]{
        "Users list", "Output editor"
    };
    
    public AdminsFrame(IMessageSender exch) {
        super("Setup", exch);
    }
    
    @Override
    protected String[] getSheetList() {
        return sheetList;
    }

    @Override
    protected JTabbedPane buildMainPanel() {
        MyJideTabbedPane workTab = new MyJideTabbedPane();
        workTab.add(getUserListPanel(), sheetList[0]);
        workTab.add(getOutputEditorPanel(), sheetList[1]);
        return workTab;
    }

    private JPanel getUserListPanel() {
        if (usersPanel == null) {
            try {
                registerGrid(usersPanel = new UsersGrid(getExchanger()));
            } catch (RemoteException ex) {
                AIBclient.logAndShowMessage(ex);
            }
        }
        return usersPanel;
    }

    private JPanel getOutputEditorPanel() {
        return new JPanel();
    }
    
}
