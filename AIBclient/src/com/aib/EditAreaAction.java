/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.text.JTextComponent;

/**
 *
 * @author nick
 */
public class EditAreaAction extends AbstractAction {

    private final JTextComponent textField;

    public EditAreaAction(ImageIcon img, JTextComponent textField) {
        super(null, img);
        this.textField = textField;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        new EditAreaDialog(null, null, textField);
    }
}
