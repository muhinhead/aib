/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import com.aib.export.DataExportSheet;
import com.aib.export.TemplatesGrid;
import com.xlend.util.PopupDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Nick Mukhin
 */
public class OutputDialog extends PopupDialog {

    private JButton cancelBtn;
    private AbstractAction cancelAction;
    private String tableName;
    private JButton selectBtn;
    private TemplatesGrid grid;
    private AbstractAction selectAction;
    private static Frame parentFrame;
    private String selectExp;

    public OutputDialog(Frame f, String[] tableAndSelect) {
        super(parentFrame = f, "Data Export", tableAndSelect);
    }

    @Override
    protected Color getHeaderBackground() {
        return AIBclient.HDR_COLOR;
    }

    @Override
    protected void fillContent() {
        super.fillContent();
        tableName = ((String[]) getObject())[0];
        selectExp = ((String[]) getObject())[1];
        getContentPane().add(getMainPanel());
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(selectBtn = new JButton(selectAction = new AbstractAction("Select template") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int tmp_id = grid.getSelectedID();
                if (tmp_id > 0) {
                    new DataExportSheet(parentFrame,new Object[]{new Integer(tmp_id),selectExp});
                }
            }
        }));
        btnPanel.add(cancelBtn = new JButton(cancelAction = new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
        }));
        getRootPane().setDefaultButton(selectBtn);
        getContentPane().add(btnPanel, BorderLayout.SOUTH);
    }

    @Override
    public void freeResources() {
        cancelBtn.setAction(null);
        cancelAction = null;
    }

    private JPanel getMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        try {
            mainPanel.add(grid = new TemplatesGrid(tableName));
        } catch (RemoteException ex) {
            AIBclient.logAndShowMessage(ex);
        }
        mainPanel.add(new JPanel(), BorderLayout.WEST);
        mainPanel.add(new JPanel(), BorderLayout.EAST);
        return mainPanel;
    }
}