/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.people;

import com.aib.EditPanelWithPhoto;
import static com.aib.RecordEditPanel.getGridPanel;
import com.aib.orm.User;
import com.aib.orm.dbobject.DbObject;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Nick Mukhin
 */
class EditUserPanel extends EditPanelWithPhoto {

    private JTextField idField;
    private JTextField firstNameTF;
    private JTextField lastNameTF;
    private JTextField initialsTF;
    private JTextField loginTF;
    private JPanel passwdCardPanel;
    private JTextField passwdTF;
    private JPasswordField passwdPwdF;
    private JCheckBox showPwdCB;
    private CardLayout cl;
    private String tflbl;
    private String pwdlbl;

    public EditUserPanel(DbObject dbObject) {
        super(dbObject);
    }

    @Override
    protected void fillContent() {
        String titles[] = new String[]{
            "", "",
            "ID:",
            "First name:",
            "Family name:",
            "Initials:",
            "Login:",
            "Password:", //"Show password:"
            "show password",
            "", ""
        };
        JComponent[] edits = new JComponent[]{
            new JPanel(), new JPanel(),
            getGridPanel(idField = new JTextField(), 4),
            firstNameTF = new JTextField(20),
            lastNameTF = new JTextField(20),
            getBorderPanel(new JComponent[]{initialsTF = new JTextField(2)}),
            getGridPanel(loginTF = new JTextField(), 2),
            getGridPanel(passwdCardPanel = new JPanel(cl = new CardLayout()), 2),
            showPwdCB = new JCheckBox(),
            new JPanel(), new JPanel()
        };
        idField.setEnabled(false);

        passwdCardPanel.add(passwdPwdF = new JPasswordField(), pwdlbl = "JPasswordField");
        passwdCardPanel.add(passwdTF = new JTextField(), tflbl = "JTextField");

        showPwdCB.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPwdCB.isSelected()) {
                    passwdTF.setText(new String(passwdPwdF.getPassword()));
                } else {
                    passwdPwdF.setText(passwdTF.getText());
                }
                cl.show(passwdCardPanel, (showPwdCB.isSelected() ? tflbl : pwdlbl));
            }
        });
        organizePanels(titles, edits, null);
    }

    @Override
    public void loadData() {
        User usr = (User) getDbObject();
        if (usr != null) {
            idField.setText(usr.getUserId().toString());
            firstNameTF.setText(usr.getFirstName());
            lastNameTF.setText(usr.getLastName());
            initialsTF.setText(usr.getInitials());
            loginTF.setText(usr.getLogin());
            passwdTF.setText(usr.getPasswd());
            passwdPwdF.setText(usr.getPasswd());
            loginTF.setEnabled(!usr.getLogin().equals("admin"));
            imageData = (byte[]) usr.getPhoto();
            setImage(imageData);
        }
    }

    @Override
    public boolean save() throws Exception {
        boolean isNew = false;
        User usr = (User) getDbObject();
        if (usr == null) {
            usr = new User(null);
            usr.setUserId(0);
            isNew = true;
        }
        usr.setFirstName(firstNameTF.getText());
        usr.setLastName(lastNameTF.getText());
        usr.setInitials(initialsTF.getText());
        usr.setLogin(loginTF.getText());
        usr.setPasswd(passwdTF.getText());
        usr.setPhoto(imageData);
        return saveDbRecord(usr, isNew);
    }
}
