/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import com.xlend.util.PopupDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

/**
 *
 * @author nick
 */
public class EditAreaDialog extends PopupDialog {

    private JTextArea textArea;
    private JButton okButton;
    private AbstractAction okAction;
    private AbstractAction cancelAction;
    private JButton cancelButton;

    public EditAreaDialog(Frame owner, String label, JTextComponent textField) {
        super(owner, label, textField);
//        setLocation(textField.getLocation());
    }

    @Override
    protected Color getHederBackground() {
        return null;//new Color(102, 125, 158);
    }

    @Override
    protected void initSize() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        pack();
        JTextComponent tf = (JTextComponent) getObject();
        Point pt = new Point(tf.getLocation());
        SwingUtilities.convertPointToScreen(pt, tf);
        setLocation(pt.x,pt.y);//+tf.getHeight());
        this.setModal(true);
    }

    @Override
    protected void fillContent() {
        super.fillContent();
        setUndecorated(true);
        final JTextComponent tf = (JTextComponent) getObject();
        JScrollPane sp = new JScrollPane(textArea = new JTextArea(tf.getText()),
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setPreferredSize(new Dimension(tf.getWidth(), 200));
        add(sp, BorderLayout.CENTER);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(okButton = new JButton(okAction = new AbstractAction("Ok") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                tf.setText(textArea.getText());
                tf.setCaretPosition(0);
                dispose();
            }
        }));
        btnPanel.add(cancelButton = new JButton(cancelAction = new AbstractAction("Cancel") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
        }));
        add(btnPanel,BorderLayout.SOUTH);
    }

    @Override
    public void freeResources() {
        okButton.setAction(null);
        cancelButton.setAction(null);
        okAction = null;
        cancelAction = null;
    }
}
