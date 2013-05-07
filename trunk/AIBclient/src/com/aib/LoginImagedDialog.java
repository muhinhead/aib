package com.aib;

import com.aib.orm.User;
import com.jtattoo.plaf.aero.AeroLookAndFeel;
import com.jtattoo.plaf.bernstein.BernsteinLookAndFeel;
import com.jtattoo.plaf.hifi.HiFiLookAndFeel;
import com.jtattoo.plaf.noire.NoireLookAndFeel;
//import com.aib.orm.Userprofile;
import com.aib.orm.dbobject.DbObject;
import com.aib.remote.IMessageSender;
import com.xlend.util.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Nick Mukhin
 */
public class LoginImagedDialog extends PopupDialog {

    private static final String BACKGROUNDIMAGE = "Login.png";
    private final String NMSOFTWARE = "Nick Mukhin (c)2013";

    /**
     * @return the okPressed
     */
    public static boolean isOkPressed() {
        return okPressed;
    }
    private JPanel controlsPanel;
    private Java2sAutoComboBox loginField;
    private JPasswordField pwdField;
    private static IMessageSender exchanger;
    private static boolean okPressed;

    public LoginImagedDialog(Object obj) {
        super(null, "Login", obj);
    }

    @Override
    protected void fillContent() {
        AIBclient.setWindowIcon(this, "aib.png");
        okPressed = false;
        buildMenu();
        try {
            String theme = AIBclient.readProperty("LookAndFeel",
                    "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            if (theme.indexOf("HiFi") > 0) {
                HiFiLookAndFeel.setTheme("Default", "", NMSOFTWARE);
            } else if (theme.indexOf("Noire") > 0) {
                NoireLookAndFeel.setTheme("Default", "", NMSOFTWARE);
            } else if (theme.indexOf("Bernstein") > 0) {
                BernsteinLookAndFeel.setTheme("Default", "", NMSOFTWARE);
            } else if(theme.indexOf("Aero")>0) {
                AeroLookAndFeel.setTheme("Green", "", NMSOFTWARE);
            }
            UIManager.setLookAndFeel(theme);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(this);
            } catch (Exception ie) {
                AIBclient.log(ie);
            }
        }
        exchanger = (IMessageSender) getObject();
        loginField = new Java2sAutoComboBox(AIBclient.loadAllLogins(exchanger));
        loginField.setEditable(true);
        pwdField = new JPasswordField(20);
        controlsPanel = new JPanel(new BorderLayout());
        JPanel main = new TexturedPanel(BACKGROUNDIMAGE);
        controlsPanel.add(main, BorderLayout.CENTER);
        ImagePanel img = new ImagePanel(AIBclient.loadImage(BACKGROUNDIMAGE, this));
        addNotify();
        Insets insets = getInsets();
        int dashWidth = img.getWidth();
        int dashHeight = img.getHeight();
        int yShift = 60;
        int xShift = 20;

        this.setMinimumSize(new Dimension(dashWidth + insets.left + insets.right - 8,
                dashHeight + insets.top + insets.bottom + 20));
        LayerPanel layers = new LayerPanel();
        layers.add(controlsPanel, JLayeredPane.DEFAULT_LAYER);
        getContentPane().add(layers, BorderLayout.CENTER);

        loginField.setBounds(222, 56, 225, 26);
        loginField.setBorder(null);
        main.add(loginField);
        pwdField.setBounds(222, 91, 225, 26);
        pwdField.setBorder(null);
        main.add(pwdField);

        JButton okButton = new ToolBarButton("Lock.png");
        img = new ImagePanel(AIBclient.loadImage("Lock.png", this));

        okButton.setBounds(dashWidth - img.getWidth() - xShift, dashHeight - img.getHeight() - yShift,
                img.getWidth(), img.getHeight());

        main.add(okButton);
        okButton.addActionListener(okButtonListener());

        Preferences userPref = Preferences.userRoot();
        String pwdmd5 = userPref.get("DEVPWD", "");
        pwdField.setText(pwdmd5);
        if (pwdmd5.trim().length() > 0) {
            loginField.setSelectedItem(AIBclient.readProperty("Lastlogin", ""));
        }
        getRootPane().setDefaultButton(okButton);
        setResizable(false);
    }

    private void buildMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu m = new JMenu("Options");
        m.add(new JMenuItem(new AbstractAction("Settings") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newAddress = AIBclient.serverSetup("Options");
                if (newAddress != null) {
                    AIBclient.getProperties().setProperty("ServerAddress", newAddress);
                    try {
                        AIBclient.setExchanger(exchanger =
                                (IMessageSender) Naming.lookup("rmi://"
                                + newAddress + "/AIBserver"));
                    } catch (Exception ex) {
                        AIBclient.logAndShowMessage(ex);
                        System.exit(1);
                    }
                }
            }
        }));
        m.add(appearanceMenu("Theme"));
        bar.add(m);
        setJMenuBar(bar);
    }

    protected JMenu appearanceMenu(String item) {
        JMenu m;
        JMenuItem it;
        m = new JMenu(item);
        it = m.add(new JMenuItem("Tiny"));
        it.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    setLookAndFeel("de.muntjak.tinylookandfeel.TinyLookAndFeel");
                } catch (Exception e1) {
                }
            }
        });
//        ch.randelshofer.quaqua.QuaquaLookAndFeel
//        it = m.add(new JMenuItem("Quaqua"));
//        it.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
//            }
//        });

        it = m.add(new JMenuItem("Nimbus"));
        it.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception e1) {
                }
            }
        });
        it = m.add(new JMenuItem("Nimrod"));
        it.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    setLookAndFeel("com.nilo.plaf.nimrod.NimRODLookAndFeel");
                } catch (Exception e1) {
                }
            }
        });
        it = m.add(new JMenuItem("Plastic"));
        it.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
                } catch (Exception e1) {
                }
            }
        });
        it = m.add(new JMenuItem("HiFi"));
        it.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    HiFiLookAndFeel.setTheme("Default", "", NMSOFTWARE);
                    setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
                } catch (Exception e1) {
                }
            }
        });
        it = m.add(new JMenuItem("Noire"));
        it.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    NoireLookAndFeel.setTheme("Default", "", NMSOFTWARE);
                    setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
                } catch (Exception e1) {
                }
            }
        });
        it = m.add(new JMenuItem("Bernstein"));
        it.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    BernsteinLookAndFeel.setTheme("Default", "", NMSOFTWARE);
                    setLookAndFeel("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");
                } catch (Exception e1) {
                }
            }
        });
        it = m.add(new JMenuItem("Aero"));
        it.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    AeroLookAndFeel.setTheme("Green", "", NMSOFTWARE);
                    setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
                } catch (Exception e1) {
                }
            }
        });

        it = m.add(new JMenuItem("System"));
        it.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e1) {
                }
            }
        });
        it = m.add(new JMenuItem("Java"));
        it.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                } catch (Exception e1) {
                }
            }
        });
        it = m.add(new JMenuItem("Motif"));
        it.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                } catch (Exception e1) {
                }
            }
        });
        return m;
    }

    private void setLookAndFeel(String lf) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(lf);
        SwingUtilities.updateComponentTreeUI(this);
        AIBclient.getProperties().setProperty("LookAndFeel", lf);
        AIBclient.saveProps();
    }

    @Override
    public void freeResources() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    private ActionListener okButtonListener() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = (String) loginField.getSelectedItem();
                String pwd = new String(pwdField.getPassword());
                try {
                    //TODO: check MD5(pwd) instead of pwd
                    DbObject[] users = exchanger.getDbObjects(User.class,
                            "login='" + login + "' and passwd='" + pwd + "'", null);
                    okPressed = (users.length > 0);
                    if (isOkPressed()) {
                        User currentUser = (User) users[0];
                        try {
                            currentUser.setPasswd("");
                        } catch (Exception ex) {
                        }
                        AIBclient.setCurrentUser(currentUser);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Access denied", "Oops!", JOptionPane.ERROR_MESSAGE);
                        loginField.requestFocus();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Server not respondind, sorry", "Error:", JOptionPane.ERROR_MESSAGE);
                    AIBclient.log(ex);
                    dispose();
                }

            }
        };
    }

    private class LayerPanel extends JLayeredPane {

        private LayerPanel() {
            super();
        }

        @Override
        public void setBounds(int x, int y, int width, int height) {
            super.setBounds(x, y, width, height);
            controlsPanel.setBounds(getBounds());
        }
    }
}