/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

//import com.xlend.util.PopupDialog;
import com.xlend.util.PopupDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Nick Mukhin
 */
public class ModalGridDialog extends PopupDialog {

    private JButton closeBtn;
    private AbstractAction closeAction;
    private GeneralGridPanel gridPanel;

    public ModalGridDialog(String title, Object obj) {
        super(null, title, obj);
    }

    @Override
    protected Color getHederBackground() {
        return AIBclient.HDR_COLOR;
    }

    @Override
    protected void fillContent() {
        super.fillContent();

        gridPanel = (GeneralGridPanel) getObject();
        add(gridPanel, BorderLayout.CENTER);
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(closeBtn = new JButton(closeAction = new AbstractAction("Close") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
        }));
        add(new JPanel(), BorderLayout.WEST);
        add(btnPanel, BorderLayout.SOUTH);
    }

    @Override
    public void freeResources() {
        closeBtn.setAction(null);
        closeAction = null;
    }
}
