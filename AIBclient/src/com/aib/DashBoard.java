/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import com.aib.admin.AdminsFrame;
import com.aib.company.CompaniesGrid;
import com.aib.company.CompanyFrame;
import com.aib.location.LocationsFrame;
import com.aib.location.LocationsGrid;
import com.aib.people.PeopleFrame;
import com.aib.people.PeopleGrid;
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
import java.util.StringTokenizer;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
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
    private ToolBarButton textSearchButton;
    private JTextField searchTextField;
    private static final String matchedCompanyColumns = "full_name,abbreviation,"
            + "address,post_code,mailaddress,mailing_post_code,"
            + "main_phone,main_fax";
    private static final String matchedLocationColumns = "name,abbreviation,address,"
            + "postcode,mailaddress,mailpostcode,"
            + "main_phone,main_fax,comments";
    private static final String matchedPeopleColumns = "source,first_name,"
            + "last_name,suffix,job_discip,department,spec_address,"
            + "mailaddress,desk_phone,desk_fax,mobile_phone,main_email,"
            + "alter_email,pa,pa_phone,pa_email";
    private ToolBarButton cleanButton;

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

    private static String getCondition(String columnList, String searchString) {
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(searchString, ".,;:\t\n ");
        while (st.hasMoreTokens()) {
            if (!sb.toString().isEmpty()) {
                sb.append(" or ");
            }
            sb.append("upper(concat(ifnull("+columnList.replaceAll(",", ",''),ifnull(")+",''))) like '%"+st.nextToken().toUpperCase()+"%'");
        }
        return sb.toString();
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
        int yshift = 55;

        ImagePanel img = new ImagePanel(AIBclient.loadImage(imgName, this));
        compsButton.setBounds(shift, yshift, img.getWidth(), img.getHeight());
        main.add(compsButton);

        locationsButton.setBounds(shift + step, yshift, img.getWidth(), img.getHeight());
        main.add(locationsButton);
        shift += step;

        peopleButton.setBounds(shift + step, yshift, img.getWidth(), img.getHeight());
        main.add(peopleButton);
        shift += step;

        setupButton.setBounds(shift + step, yshift, img.getWidth(), img.getHeight());
        main.add(setupButton);

        final String lookup_png = "lookup.png";
        img = new ImagePanel(AIBclient.loadImage(lookup_png, this));
        textSearchButton = new ToolBarButton(lookup_png, true);
        textSearchButton.setBounds(setupButton.getX() + setupButton.getWidth() + 2, 10, img.getWidth() + 2, img.getHeight() + 2);
        searchTextField = new JTextField();
        searchTextField.setToolTipText("enter here list of tokens to search");
        JLabel searchLbl = new JLabel("Quick search:");
        searchLbl.setBounds(compsButton.getX() - searchLbl.getPreferredSize().width, 10, searchLbl.getPreferredSize().width, searchLbl.getPreferredSize().height);
        searchTextField.setBounds(compsButton.getX(), 10, setupButton.getX() + setupButton.getWidth() - compsButton.getX(), searchTextField.getPreferredSize().height);

        cleanButton = new ToolBarButton("clear.png", true);
        cleanButton.setBounds(textSearchButton.getX() + textSearchButton.getWidth() + 2, 10, img.getWidth() + 2, img.getHeight() + 2);

        main.add(searchLbl);
        main.add(searchTextField);
        main.add(textSearchButton);
        main.add(cleanButton);

        getRootPane().setDefaultButton(textSearchButton);

        cleanButton.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                searchTextField.setText("");
            }
        });
        textSearchButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (GeneralFrame frame : new GeneralFrame[]{
                    companyFrame,
                    peopleFrame,
                    locationsFrame,
                    adminsFrame}) {
                    if (frame != null) {
                        frame.setVisible(false);
                    }
                }
                boolean companiesExist = AIBclient.isThereRecords(exchanger,
                        "select 1 from company where "
                                + getCondition(matchedCompanyColumns, searchTextField.getText()));
                                //+ "match(" + matchedCompanyColumns + ") against ('" + searchTextField.getText() + "')");
                boolean locationsExist = AIBclient.isThereRecords(exchanger,
                        "select 1 from location where "
                                + getCondition(matchedLocationColumns, searchTextField.getText()));
                                //+ "match(" + matchedLocationColumns + ") against ('" + searchTextField.getText() + "')");
                boolean peopleExist = AIBclient.isThereRecords(exchanger,
                        "select 1 from people where "
                                + getCondition(matchedPeopleColumns, searchTextField.getText()));
                                //+ "match(" + matchedPeopleColumns + ") against ('" + searchTextField.getText() + "')");
                if (searchTextField.getText().isEmpty() || companiesExist) {
                    showCompanyFrame(searchTextField.getText());
                }
                if (searchTextField.getText().isEmpty() || locationsExist) {
                    showLocationFrame(searchTextField.getText());
                }
                if (searchTextField.getText().isEmpty() || peopleExist) {
                    showPeopleFrame(searchTextField.getText());
                }
                if (!searchTextField.getText().isEmpty()) {
                    if (!companiesExist && !locationsExist && !peopleExist) {
                        GeneralFrame.infoMessageBox("Warning:", "The search term was not found");
                    } else {
                        StringBuilder sb = new StringBuilder("The search term was found among: ");
                        if (companiesExist) {
                            sb.append("companies");
                        }
                        if (locationsExist) {
                            if (companiesExist) {
                                if (peopleExist) {
                                    sb.append(",");
                                } else {
                                    sb.append(" and ");
                                }
                            }
                            sb.append("locations");
                        }
                        if (peopleExist) {
                            if (companiesExist || locationsExist) {
                                sb.append(" and ");
                            }
                            sb.append("people");
                        }
                        GeneralFrame.infoMessageBox("FYI:", sb.toString());
                    }
                }
            }
        });

        compsButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCompanyFrame(null);
            }
        });

        peopleButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                showPeopleFrame(null);
            }
        });

        locationsButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                showLocationFrame(null);
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

    private void showCompanyFrame(String matchCond) {
        boolean wasnull = (companyFrame == null);
        if (wasnull) {
            companyFrame = new CompanyFrame(AIBclient.getExchanger());
        } else {
            try {
                companyFrame.setLookAndFeel(AIBclient.readProperty("LookAndFeel",
                        UIManager.getSystemLookAndFeelClassName()));
            } catch (Exception ex) {
            }
            companyFrame.setVisible(true);
        }
        if (matchCond != null) {
            for (GeneralGridPanel grid : companyFrame.getGrids()) {
                if (grid instanceof CompaniesGrid) {
                    if (matchCond.isEmpty()) {
                        grid.setSelect(CompaniesGrid.SELECT);
                    } else {
                        grid.setSelect(GeneralFrame.adjustSelect(
                                //"match(" + matchedCompanyColumns + ") against ('" + matchCond + "')", "from company ",
                                getCondition(matchedCompanyColumns, matchCond), "from company ",
                                CompaniesGrid.SELECT));
                    }
                    grid.refresh();
                }
            }
        }
    }

    private void showPeopleFrame(String matchCond) {
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
        if (matchCond != null) {
            for (GeneralGridPanel grid : peopleFrame.getGrids()) {
                if (grid instanceof PeopleGrid) {
                    if (matchCond.isEmpty()) {
                        grid.setSelect(PeopleGrid.SELECT);
                    } else {
                        grid.setSelect(GeneralFrame.adjustSelect(
                                //"match(" + matchedPeopleColumns + ") against ('" + matchCond + "')", "from people ", 
                                getCondition(matchedPeopleColumns, matchCond), "from people ",
                                PeopleGrid.SELECT));
                    }
                    grid.refresh();
                }
            }
        }
    }

    private void showLocationFrame(String matchCond) {
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
        if (matchCond != null) {
            for (GeneralGridPanel grid : locationsFrame.getGrids()) {
                if (grid instanceof LocationsGrid) {
                    if (matchCond.isEmpty()) {
                        grid.setSelect(LocationsGrid.SELECT);
                    } else {
                        grid.setSelect(GeneralFrame.adjustSelect(
                                //"match(" + matchedLocationColumns + ") against ('" + matchCond + "')", "from location ", 
                                getCondition(matchedLocationColumns, matchCond), "from location ",
                                LocationsGrid.SELECT));
                    }
                    grid.refresh();
                }
            }
        }
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
