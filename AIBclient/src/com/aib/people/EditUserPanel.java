/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.people;

import com.aib.AIBclient;
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
public class EditUserPanel extends EditPanelWithPhoto {

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
    private JCheckBox adminPermCB;

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
            "Password:",
            //            "show password",
            "", "", ""
        };
        JComponent[] edits = new JComponent[]{
            new JPanel(), new JPanel(),
            getGridPanel(idField = new JTextField(), 4),
            firstNameTF = new JTextField(20),
            lastNameTF = new JTextField(20),
            getBorderPanel(new JComponent[]{initialsTF = new JTextField(2)}),
            getGridPanel(loginTF = new JTextField(), 2),
            getGridPanel(new JComponent[]{
                passwdCardPanel = new JPanel(cl = new CardLayout()),
                new JLabel("show", SwingConstants.RIGHT),
                showPwdCB = new JCheckBox("show")
            }),
            new JPanel(), new JPanel(),
            adminPermCB = new JCheckBox("Admin permissions")
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
        for (JComponent comp : new JComponent[]{
            firstNameTF, lastNameTF, initialsTF, loginTF,
            passwdTF, passwdPwdF, showPwdCB, adminPermCB}) {
            if (comp != null) {
                comp.setEnabled(false);
            }
        }

    }

    @Override
    public void loadData() {
        User usr = (User) getDbObject();
        setEnabledPictureControl(false);
        if (usr != null) {
            User curUser = AIBclient.getCurrentUser();
            Integer isCurrentAdmin = curUser.getIsAdmin();
            idField.setText(usr.getUserId().toString());
            firstNameTF.setText(usr.getFirstName());
            lastNameTF.setText(usr.getLastName());
            initialsTF.setText(usr.getInitials());
            loginTF.setText(usr.getLogin());
            passwdTF.setText(usr.getPasswd());
            passwdPwdF.setText(usr.getPasswd());
            imageData = (byte[]) usr.getPhoto();
            adminPermCB.setSelected(usr.getIsAdmin() != null && usr.getIsAdmin() == 1);
            setImage(imageData);
            if (curUser.getIsAdmin().intValue() == 1
                    || curUser.getUserId().intValue() == usr.getUserId().intValue()) {
                setEnabledPictureControl(true);
                firstNameTF.setEnabled(true);
                lastNameTF.setEnabled(true);
                initialsTF.setEnabled(true);
                loginTF.setEnabled(true);
                passwdTF.setEnabled(true);
                passwdPwdF.setEnabled(true);
                showPwdCB.setEnabled(true);
                adminPermCB.setEnabled(true);
                setEnabledPictureControl(true);
            }
            if (isCurrentAdmin == null || isCurrentAdmin.intValue() == 0
                    || usr.getUserId().intValue() == 1) {
                adminPermCB.setEnabled(false);
            }
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
        usr.setIsAdmin(adminPermCB.isSelected() ? 1 : 0);
        usr.setPhoto(imageData);
        return saveDbRecord(usr, isNew);
    }
}
