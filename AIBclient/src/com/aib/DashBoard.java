/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import com.aib.admin.AdminsFrame;
import com.aib.company.CompanyFrame;
import com.aib.location.LocationsFrame;
import com.aib.people.PeopleFrame;
import com.aib.remote.IMessageSender;
import com.xlend.util.ImagePanel;
import com.xlend.util.TexturedPanel;
import com.xlend.util.ToolBarButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 *
 * @author nick
 */
public class DashBoard extends JFrame {//extends AbstractDashBoard {

    static Window ourInstance;
    private final IMessageSender exchanger;
    protected TexturedPanel main;
    protected JPanel controlsPanel;
    private int dashWidth;
    private int dashHeight;
    private static final String BACKGROUNDIMAGE = "Background.png";
    private ToolBarButton compsButton;
    private ToolBarButton peopleButton;
    private ToolBarButton locationsButton;
    private ToolBarButton setupButton;
    private CompanyFrame companyFrame;
    private PeopleFrame peopleFrame;
    private LocationsFrame locationsFrame;
    private AdminsFrame adminsFrame;

    protected class WinListener extends WindowAdapter {

        public WinListener(JFrame frame) {
        }

        public void windowClosing(WindowEvent e) {
            exit();
        }
    }

    protected class LayerPanel extends JLayeredPane {

        LayerPanel() {
            super();
        }

        @Override
        public void setBounds(int x, int y, int width, int height) {
            super.setBounds(x, y, width, height);
            controlsPanel.setBounds(getBounds());
        }
    }

    public DashBoard(String title, IMessageSender exchanger) {
        super(title);
        this.exchanger = exchanger;
        ourInstance = this;
        addWindowListener(new DashBoard.WinListener(this));
        lowLevelInit();
        initBackground();
        fillControlsPanel();
        setVisible(true);
    }

    protected void initBackground() {
        AIBclient.setWindowIcon(this, "aib.png");
        addWindowListener(new DashBoard.WinListener(this));
        controlsPanel = new JPanel(new BorderLayout());

        setLayout(new BorderLayout());

        LayerPanel layers = new LayerPanel();
        main = new TexturedPanel(getBackGroundImage());
        controlsPanel.add(main, BorderLayout.CENTER);
        addNotify();
        ImagePanel img = new ImagePanel(AIBclient.loadImage(getBackGroundImage(), this));
        Insets insets = getInsets();
        dashWidth = img.getWidth();
        dashHeight = img.getHeight();
        this.setMinimumSize(new Dimension(dashWidth + insets.left + insets.right, dashHeight + insets.top + insets.bottom));
        layers.add(controlsPanel, JLayeredPane.DEFAULT_LAYER);

        getContentPane().add(layers, BorderLayout.CENTER);
    }

    protected String getBackGroundImage() {
        return BACKGROUNDIMAGE;
    }

    public void lowLevelInit() {
        AIBclient.readProperty("junk", ""); // just to init properties
    }

    protected void fillControlsPanel() throws HeadlessException {
//        getContentPane().setLayout(new BorderLayout(10, 10));
//        SceneApplet sa = new DashBoardApplet();
//        JPanel headerPanel = new JPanel();
//        headerPanel.setBackground(AIBclient.HDR_COLOR);
//        headerPanel.setForeground(Color.WHITE);
//        JLabel lbl = new JLabel(getTitle(), SwingConstants.CENTER);
//        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 18));
//        headerPanel.add(lbl);
//        getContentPane().add(headerPanel, BorderLayout.NORTH);
//        getContentPane().add(sa);
//        sa.init();

        String imgName = "companies.png";
        compsButton = new ToolBarButton(imgName, true);
        peopleButton = new ToolBarButton("people.png", true);
        locationsButton = new ToolBarButton("locations.png", true);
        setupButton = new ToolBarButton("setup.png", true);

        int step = 120;
        int shift = 80;

        ImagePanel img = new ImagePanel(AIBclient.loadImage(imgName, this));
        compsButton.setBounds(shift, 40, img.getWidth(), img.getHeight());
        main.add(compsButton);

        peopleButton.setBounds(shift + step, 40, img.getWidth(), img.getHeight());
        main.add(peopleButton);
        shift += step;

        locationsButton.setBounds(shift + step, 40, img.getWidth(), img.getHeight());
        main.add(locationsButton);
        shift += step;

        setupButton.setBounds(shift + step, 40, img.getWidth(), img.getHeight());
        main.add(setupButton);

        compsButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (companyFrame == null) {
                    companyFrame = new CompanyFrame(AIBclient.getExchanger());
                } else {
                    try {
                        companyFrame.setLookAndFeel(AIBclient.readProperty("LookAndFeel",
                                UIManager.getSystemLookAndFeelClassName()));
                    } catch (Exception ex) {
                    }
                    companyFrame.setVisible(true);
                }
            }
        });

        peopleButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (peopleFrame == null) {
                    peopleFrame = new PeopleFrame(AIBclient.getExchanger());
                } else {
                    try {
                        peopleFrame.setLookAndFeel(AIBclient.readProperty("LookAndFeel",
                                UIManager.getSystemLookAndFeelClassName()));
                    } catch (Exception ex) {
                    }
                    peopleFrame.setVisible(true);
                }
            }
        });

        locationsButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (locationsFrame == null) {
                    locationsFrame = new LocationsFrame(AIBclient.getExchanger());
                } else {
                    try {
                        locationsFrame.setLookAndFeel(AIBclient.readProperty("LookAndFeel",
                                UIManager.getSystemLookAndFeelClassName()));
                    } catch (Exception ex) {
                    }
                    locationsFrame.setVisible(true);
                }
            }
        });

        setupButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (adminsFrame == null) {
                    adminsFrame = new AdminsFrame(AIBclient.getExchanger());
                } else {
                    try {
                        adminsFrame.setLookAndFeel(AIBclient.readProperty("LookAndFeel",
                                UIManager.getSystemLookAndFeelClassName()));
                    } catch (Exception ex) {
                    }
                    adminsFrame.setVisible(true);
                }
            }
        });

        centerOnScreen();
        setResizable(false);
    }

    public void centerOnScreen() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(d.width / 2 - getWidth() / 2, d.height / 2 - getHeight() / 2);
    }

    public static void centerWindow(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setExtendedState(Frame.NORMAL);
        frame.validate();
    }

    public static float getXratio(JFrame frame) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        return (float) frame.getWidth() / d.width;
    }

    public static float getYratio(JFrame frame) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        return (float) frame.getHeight() / d.height;
    }

    public static void setSizes(JFrame frame, double x, double y) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) (x * d.width), (int) (y * d.height));
    }

    public void setVisible(boolean show) {
        super.setVisible(show);
    }

    protected void exit() {
        dispose();
        System.exit(1);
    }
}
