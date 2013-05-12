package com.aib;

import com.aib.orm.Country;
import com.aib.orm.Industry;
import com.aib.orm.Link;
import com.aib.orm.User;
import com.aib.orm.Worldregion;
import com.aib.orm.dbobject.ComboItem;
import com.aib.orm.dbobject.DbObject;
import com.aib.remote.IMessageSender;
import com.jidesoft.plaf.LookAndFeelFactory;
import java.awt.Image;
import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.prefs.Preferences;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Nick Mukhin
 */
public class AIBclient {

    private static final String version = "0.74.a";
//    private static Userprofile currentUser;
    private static Logger logger = null;
    private static FileHandler fh;
    private static Properties props;
    private static final String PROPERTYFILENAME = "AIBclient.config";
    private static User currentUser;
    private static IMessageSender exchanger;
    private static ComboItem[] regionsDictionary;
    private static Country[] countryDictionary;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        String serverIP = readProperty("ServerAddress", "localhost");
//        IMessageSender exchanger;
        while (true) {
            try {
                exchanger = (IMessageSender) Naming.lookup("rmi://" + serverIP + "/AIBserver");
                if (login(exchanger)) {
                    new DashBoard("AIBclient", exchanger);
                    break;
                } else {
                    System.exit(1);
                }
            } catch (Exception ex) {
                logAndShowMessage(ex);
                if ((serverIP = serverSetup("Check server settings")) == null) {
                    System.exit(1);
                } else {
                    saveProps();
                }
            }
        }
    }

    public static void logAndShowMessage(Throwable ne) {
        JOptionPane.showMessageDialog(null, ne.getMessage(), "Error:", JOptionPane.ERROR_MESSAGE);
        log(ne);
    }

    public static boolean login(IMessageSender exchanger) {
        try {
            new LoginImagedDialog(exchanger);//new Object[]{loginField, pwdField, exchanger});
            return LoginImagedDialog.isOkPressed();
        } catch (Throwable ee) {
            JOptionPane.showMessageDialog(null, "Server failure\nCheck your logs please", "Error:", JOptionPane.ERROR_MESSAGE);
            log(ee);
        }
        return false;
    }

    public static void log(String msg) {
        log(msg, null);
    }

    public static void log(Throwable th) {
        log(null, th);
    }

    private static void log(String msg, Throwable th) {
        if (logger == null) {
            try {
                logger = Logger.getLogger("AIBclient");
                fh = new FileHandler("%h/AIBclient.log", 1048576, 10, true);
                logger.addHandler(fh);
                logger.setLevel(Level.ALL);
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.log(Level.SEVERE, msg, th);
    }

    public static void setWindowIcon(Window w, String iconName) {
        w.setIconImage(loadImage(iconName, w));
    }

    public static Image loadImage(String iconName, Window w) {
        Image im = null;
        File f = new File("images/" + iconName);
        if (f.exists()) {
            try {
                ImageIcon ic = new javax.swing.ImageIcon("images/" + iconName, "");
                im = ic.getImage();
            } catch (Exception ex) {
                log(ex);
            }
        } else {
            try {
                im = ImageIO.read(w.getClass().getResourceAsStream("/" + iconName));
            } catch (Exception ie) {
                log(ie);
            }
        }
        return im;
    }

    public static String readProperty(String key, String deflt) {
        if (null == props) {
            props = new Properties();
            try {
                File propFile = new File(PROPERTYFILENAME);
                if (!propFile.exists() || propFile.length() == 0) {
                    String curPath = propFile.getAbsolutePath();
                    curPath = curPath.substring(0,
                            curPath.indexOf(PROPERTYFILENAME)).replace('\\', '/');
                    props.setProperty("user", "admin");
                    props.setProperty("userPassword", "admin");
                    propFile.createNewFile();
                } else {
                    props.load(new FileInputStream(propFile));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return deflt;
            }
        }
        return props.getProperty(key, deflt);
    }

    public static void saveProps() {
        if (props != null) {
            if (getCurrentUser() != null) {
                props.setProperty("LastLogin", getCurrentUser().getLogin());
            }
            props.setProperty("ServerAddress", props.getProperty("ServerAddress", "localhost:1099"));
        }
        Preferences userPref = Preferences.userRoot();
        saveProperties();
    }

    public static void saveProperties() {
        try {
            if (props != null) {
                props.store(new FileOutputStream(PROPERTYFILENAME),
                        "-----------------------");
            }
        } catch (IOException e) {
            //e.printStackTrace();
            logAndShowMessage(e);
        }
    }

    public static String serverSetup(String title) {
        String address = readProperty("ServerAddress", "localhost");
        String[] vals = address.split(":");
        JTextField imageDirField = new JTextField(getProperties().getProperty("imagedir"));
        JTextField addressField = new JTextField(16);
        addressField.setText(vals[0]);
        JSpinner portSpinner = new JSpinner(new SpinnerNumberModel(
                vals.length > 1 ? new Integer(vals[1]) : 1099, 0, 65536, 1));
        JComponent[] edits = new JComponent[]{imageDirField, addressField, portSpinner};
        new ConfigEditor(title, edits);
        if (addressField.getText().trim().length() > 0) {
            String addr = addressField.getText() + ":" + portSpinner.getValue();
            getProperties().setProperty("ServerAddress", addr);
            getProperties().setProperty("imagedir", imageDirField.getText());
            return addr;
        } else {
            return null;
        }
    }

    /**
     * @return the currentUser
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * @param aCurrentUser the currentUser to set
     */
    public static void setCurrentUser(User aCurrentUser) {
        currentUser = aCurrentUser;
    }

    public static Properties getProperties() {
        return props;
    }

    public static List loadAllLogins() {
        try {
            DbObject[] users = exchanger.getDbObjects(User.class, null, "login");
            ArrayList logins = new ArrayList();
            logins.add("");
            int i = 1;
            for (DbObject o : users) {
                User up = (User) o;
                logins.add(up.getLogin());
            }
            return logins;
        } catch (RemoteException ex) {
            log(ex);
        }
        return null;
    }

//    public static List loadAllRegions() {
//        try {
//            DbObject[] regions = exchanger.getDbObjects(Worldregion.class, null, null);
//            ArrayList logins = new ArrayList();
//            logins.add("");
//            int i = 1;
//            for (DbObject o : regions) {
//                Worldregion wr = (Worldregion) o;
//                logins.add(wr.getDescr());
//            }
//            return logins;
//        } catch (RemoteException ex) {
//            log(ex);
//        }
//        return null;
//    }
    private static ComboItem[] loadOnSelect(IMessageSender exchanger, String select) {
        try {
            Vector[] tab = exchanger.getTableBody(select);
            Vector rows = tab[1];
            ComboItem[] ans = new ComboItem[rows.size()];
            for (int i = 0; i < rows.size(); i++) {
                Vector line = (Vector) rows.get(i);
                int id = Integer.parseInt(line.get(0).toString());
                String tmvnr = line.get(1).toString();
                ans[i] = new ComboItem(id, tmvnr);
            }
            return ans;
        } catch (RemoteException ex) {
            log(ex);
        }
        return new ComboItem[]{new ComboItem(0, "")};
    }

    public static ComboItem[] loadAllRegions() {
        if (regionsDictionary == null) {
            regionsDictionary = loadOnSelect(exchanger, "select worldregion_id, descr from worldregion");
        }
        return regionsDictionary;
    }

    public static Country[] loadAllCountries() {
        if (countryDictionary == null) {
            try {
                DbObject[] clst = getExchanger().getDbObjects(Country.class, null, null);
                countryDictionary = new Country[clst.length];
                int n = 0;
                for(DbObject itm : clst) {
                    countryDictionary[n++] = (Country) itm;
                }
            } catch (RemoteException ex) {
                log(ex);
            }
        }
        return countryDictionary;
    }

    public static Object[] loadRegionCountries(int region_id) {
        ArrayList<ComboItem> lst = new ArrayList<>();
        for (Country c : loadAllCountries()) {
            if (c.getWorldregionId().intValue()==region_id) {
                lst.add(new ComboItem(c.getCountryId(),c.getCountry()));
            }
        }
        return lst.toArray();
    }

    public static IMessageSender getExchanger() {
        return exchanger;
    }

    static void setExchanger(IMessageSender iMessageSender) {
        exchanger = iMessageSender;
    }

    /**
     * @return the version
     */
    public static String getVersion() {
        return version;
    }

    public static List loadAllLinks() {
        try {
            DbObject[] linkArray = exchanger.getDbObjects(Link.class, null, "url");
            ArrayList links = new ArrayList();
            int i = 1;
            links.add("");
            for (DbObject o : linkArray) {
                Link up = (Link) o;
                links.add(up.getUrl());
            }
            return links;
        } catch (RemoteException ex) {
            log(ex);
        }
        return null;
    }

    static Object loadAllIndustries() {
        try {
            DbObject[] indArray = exchanger.getDbObjects(Industry.class, null, "descr");
            ArrayList inds = new ArrayList();
            int i = 1;
            inds.add("");
            for (DbObject o : indArray) {
                Industry up = (Industry) o;
                inds.add(up.getDescr());
            }
            return inds;
        } catch (RemoteException ex) {
            log(ex);
        }
        return null;
    }
}
