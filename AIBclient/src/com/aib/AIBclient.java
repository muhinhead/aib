package com.aib;

import com.aib.orm.Aibpublic;
import com.aib.orm.Compindustry;
import com.aib.orm.Complink;
import com.aib.orm.Comppublic;
import com.aib.orm.Country;
import com.aib.orm.Industry;
import com.aib.orm.Link;
import com.aib.orm.Locindustry;
import com.aib.orm.Loclink;
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

    public static void clearRegionsAndCountries() {
        regionsDictionary = null;
        countryDictionary = null;
    }

    public static ComboItem[] loadAllCompanies() {
        return loadOnSelect(exchanger,
                "select company_id,concat(abbreviation,' (',full_name,')') "
                + "from company order by abbreviation");
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
                for (DbObject itm : clst) {
                    countryDictionary[n++] = (Country) itm;
                }
            } catch (RemoteException ex) {
                log(ex);
            }
        }
        return countryDictionary;
    }

    public static Object[] loadRegionCountries(int region_id) {
        ArrayList<ComboItem> lst = new ArrayList<ComboItem>();
        for (Country c : loadAllCountries()) {
            if (c.getWorldregionId().intValue() == region_id) {
                lst.add(new ComboItem(c.getCountryId(), c.getCountry()));
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

    public static List loadAllIndustries() {
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

    public static List loadAllAIBmentions() {
        try {
            DbObject[] aibArray = exchanger.getDbObjects(Aibpublic.class, null, "pub_date desc");
            ArrayList pubs = new ArrayList();
            int i = 1;
            pubs.add("");
            for (DbObject o : aibArray) {
                Aibpublic aibPub = (Aibpublic) o;
                pubs.add(aibPub.getPublication() + " (" + aibPub.getPubDate() + ")");
            }
            return pubs;
        } catch (RemoteException ex) {
            log(ex);
        }
        return null;
    }

    public static boolean publicationNotExist(String publicationWithDate) {
        int qty = 0;
        int p = publicationWithDate.indexOf("(");
        if (p > 0) {
            String publication = publicationWithDate.substring(0, p).trim();
            String pubDate = publicationWithDate.substring(p + 1, publicationWithDate.indexOf(")"));
            try {
                qty = exchanger.getCount(
                        "select aibpublic_id from aibpublic where publication='"
                        + publication + "' and pub_date='" + pubDate + "'");
            } catch (RemoteException ex) {
                log(ex);
            }
        }
        return qty == 0;
    }

    public static Integer getRegionOnCountry(Integer countryId) {
        try {
            Country country = (Country) getExchanger().loadDbObjectOnID(Country.class, countryId);
            return country.getWorldregionId();
        } catch (RemoteException ex) {
            log(ex);
        }
        return null;
    }

    public static void saveCompanyPublication(Integer companyID, String publication) {
        try {
            Aibpublic pub = null;
            Comppublic cp = null;
            DbObject[] recs = getExchanger().getDbObjects(Aibpublic.class, "concat(publication,' (',pub_date,')')='" + publication + "'", null);
            pub = (Aibpublic) recs[0]; //it should already exist, because new item is already saved
            if (getExchanger().getCount("select comppublic_id from comppublic where company_id="
                    + companyID + " and aibpublic_id=" + pub.getAibpublicId()) == 0) {
                cp = new Comppublic(null);
                cp.setComppublicId(0);
                cp.setCompanyId(companyID);
                cp.setAibpublicId(pub.getAibpublicId());
                cp.setNew(true);
                AIBclient.getExchanger().saveDbObject(cp);
            }
        } catch (Exception ex) {
            log(ex);
        }
    }

    public static void saveOrInsertCompanyIndustry(Integer companyID, String industry) {
        try {
            Industry ind = null;
            Compindustry ci = null;
            DbObject[] recs = getExchanger().getDbObjects(Industry.class, "descr='" + industry + "'", null);
            if (recs.length == 0) {
                ind = new Industry(null);
                ind.setIndustryId(0);
                ind.setDescr(industry);
                ind.setNew(true);
                ind = (Industry) AIBclient.getExchanger().saveDbObject(ind);
            } else {
                ind = (Industry) recs[0];
            }
            if (getExchanger().getCount("select compindustry_id from compindustry where company_id="
                    + companyID + " and industry_id=" + ind.getIndustryId()) == 0) {
                ci = new Compindustry(null);
                ci.setCompindustryId(0);
                ci.setCompanyId(companyID);
                ci.setIndustryId(ind.getIndustryId());
                ci.setNew(true);
                AIBclient.getExchanger().saveDbObject(ci);
            }
        } catch (Exception ex) {
            log(ex);
        }
    }

    public static void saveOrInsertLocationLink(Integer locationID, String link) {
        try {
            Link lnk = null;
            Loclink ll = null;
            DbObject[] recs = getExchanger().getDbObjects(Link.class, "url='" + link + "'", null);
            if (recs.length == 0) {
                lnk = new Link(null);
                lnk.setLinkId(0);
                lnk.setUrl(link);
                lnk.setNew(true);
                lnk = (Link) AIBclient.getExchanger().saveDbObject(lnk);
            } else {
                lnk = (Link) recs[0];
            }
            if (getExchanger().getCount("select loclink_id from loclink where location_id="
                    + locationID + " and link_id=" + lnk.getLinkId()) == 0) {
                ll = new Loclink(null);
                ll.setLoclinkId(0);
                ll.setLocationId(locationID);
                ll.setLinkId(lnk.getLinkId());
                ll.setNew(true);
                AIBclient.getExchanger().saveDbObject(ll);
            }
        } catch (Exception ex) {
            log(ex);
        }
    }

    public static void saveOrInsertLocationIndustry(Integer locationID, String industry) {
        try {
            Industry ind = null;
            Locindustry li = null;
            DbObject[] recs = getExchanger().getDbObjects(Industry.class, "descr='" + industry + "'", null);
            if (recs.length == 0) {
                ind = new Industry(null);
                ind.setIndustryId(0);
                ind.setDescr(industry);
                ind.setNew(true);
                ind = (Industry) AIBclient.getExchanger().saveDbObject(ind);
            } else {
                ind = (Industry) recs[0];
            }
            if (getExchanger().getCount("select locindustry_id from locindustry where location_id="
                    + locationID + " and industry_id=" + ind.getIndustryId()) == 0) {
                li = new Locindustry(null);
                li.setLocindustryId(0);
                li.setLocationId(locationID);
                li.setIndustryId(ind.getIndustryId());
                li.setNew(true);
                AIBclient.getExchanger().saveDbObject(li);
            }
        } catch (Exception ex) {
            log(ex);
        }
    }
    
    public static void saveOrInsertCompanyLink(Integer companyID, String link) {
        try {
            Link lnk = null;
            Complink cl = null;
            DbObject[] recs = getExchanger().getDbObjects(Link.class, "url='" + link + "'", null);
            if (recs.length == 0) {
                lnk = new Link(null);
                lnk.setLinkId(0);
                lnk.setUrl(link);
                lnk.setNew(true);
                lnk = (Link) AIBclient.getExchanger().saveDbObject(lnk);
            } else {
                lnk = (Link) recs[0];
            }
            if (getExchanger().getCount("select complink_id from complink where company_id="
                    + companyID + " and link_id=" + lnk.getLinkId()) == 0) {
                cl = new Complink(null);
                cl.setComplinkId(0);
                cl.setCompanyId(companyID);
                cl.setLinkId(lnk.getLinkId());
                cl.setNew(true);
                AIBclient.getExchanger().saveDbObject(cl);
            }
        } catch (Exception ex) {
            log(ex);
        }
    }

    public static void removeRedundantPublications(Integer companyID, String publicationList) {
        try {
            DbObject[] recs = getExchanger().getDbObjects(Comppublic.class,
                    "company_id=" + companyID + " and aibpublic_id not in "
                    + "(select aibpublic_id from aibpublic where instr('" + publicationList + "',concat(publication,' (',pub_date,')'))>0)", null);
            for (DbObject rec : recs) {
                getExchanger().deleteObject(rec);
            }
        } catch (RemoteException ex) {
            log(ex);
        }
    }

    public static void removeRedundantCompanyIndustries(Integer companyID, String industryList) {
        try {
            DbObject[] recs = getExchanger().getDbObjects(Compindustry.class,
                    "company_id=" + companyID + " and industry_id not in (select industry_id from industry where instr('" + industryList + "',descr)>0)", null);
            for (DbObject rec : recs) {
                getExchanger().deleteObject(rec);
            }
        } catch (RemoteException ex) {
            log(ex);
        }
    }

    public static void removeRedundantLocationIndustries(Integer locationID, String industryList) {
        try {
            DbObject[] recs = getExchanger().getDbObjects(Locindustry.class,
                    "location_id=" + locationID + " and industry_id not in (select industry_id from industry where instr('" + industryList + "',descr)>0)", null);
            for (DbObject rec : recs) {
                getExchanger().deleteObject(rec);
            }
        } catch (RemoteException ex) {
            log(ex);
        }
    }
    
    public static void removeRedundantCompanyLinks(Integer companyID, String linkList) {
        try {
            DbObject[] recs = getExchanger().getDbObjects(Complink.class,
                    "company_id=" + companyID + " and link_id not in (select link_id from link where instr('" + linkList + "',url)>0)", null);
            for (DbObject rec : recs) {
                getExchanger().deleteObject(rec);
            }
        } catch (RemoteException ex) {
            log(ex);
        }
    }

    public static void removeRedundantLocationLinks(Integer locationID, String linkList) {
        try {
            DbObject[] recs = getExchanger().getDbObjects(Loclink.class,
                    "location_id=" + locationID + " and link_id not in (select link_id from link where instr('" + linkList + "',url)>0)", null);
            for (DbObject rec : recs) {
                getExchanger().deleteObject(rec);
            }
        } catch (RemoteException ex) {
            log(ex);
        }
    }

    public static String getLinkListOnLocationID(Integer locationID) {
        try {
            StringBuilder sb = new StringBuilder();
            DbObject[] recs = getExchanger().getDbObjects(Loclink.class, "location_id=" + locationID, null);
            for (DbObject rec : recs) {
                Loclink ll = (Loclink) rec;
                Link lnk = (Link) getExchanger().loadDbObjectOnID(Link.class, ll.getLinkId());
                sb.append(sb.length() > 0 ? "," : "");
                sb.append(lnk.getUrl());
            }
            return sb.toString();
        } catch (RemoteException ex) {
            log(ex);
        }
        return "can't load link list";
    }
    
    public static String getLinkListOnCompanyID(Integer companyID) {
        try {
            StringBuilder sb = new StringBuilder();
            DbObject[] recs = getExchanger().getDbObjects(Complink.class, "company_id=" + companyID, null);
            for (DbObject rec : recs) {
                Complink cl = (Complink) rec;
                Link lnk = (Link) getExchanger().loadDbObjectOnID(Link.class, cl.getLinkId());
                sb.append(sb.length() > 0 ? "," : "");
                sb.append(lnk.getUrl());
            }
            return sb.toString();
        } catch (RemoteException ex) {
            log(ex);
        }
        return "can't load link list";
    }
    
    public static String getIndustryListOnLocationID(Integer locationID) {
        try {
            StringBuilder sb = new StringBuilder();
            DbObject[] recs = getExchanger().getDbObjects(Locindustry.class, "location_id=" + locationID, null);
            for (DbObject rec : recs) {
                Locindustry ci = (Locindustry) rec;
                Industry ind = (Industry) getExchanger().loadDbObjectOnID(Industry.class, ci.getIndustryId());
                sb.append(sb.length() > 0 ? "," : "");
                sb.append(ind.getDescr());
            }
            return sb.toString();
        } catch (RemoteException ex) {
            log(ex);
        }
        return "can't load industry list";
    }

    public static String getIndustryListOnCompanyID(Integer companyID) {
        try {
            StringBuilder sb = new StringBuilder();
            DbObject[] recs = getExchanger().getDbObjects(Compindustry.class, "company_id=" + companyID, null);
            for (DbObject rec : recs) {
                Compindustry ci = (Compindustry) rec;
                Industry ind = (Industry) getExchanger().loadDbObjectOnID(Industry.class, ci.getIndustryId());
                sb.append(sb.length() > 0 ? "," : "");
                sb.append(ind.getDescr());
            }
            return sb.toString();
        } catch (RemoteException ex) {
            log(ex);
        }
        return "can't load industry list";
    }

    public static String getPublicationsOnCompanyID(Integer companyID) {
        try {
            StringBuilder sb = new StringBuilder();
            DbObject[] recs = getExchanger().getDbObjects(Comppublic.class, "company_id=" + companyID, null);
            for (DbObject rec : recs) {
                Comppublic cp = (Comppublic) rec;
                Aibpublic pub = (Aibpublic) getExchanger().loadDbObjectOnID(Aibpublic.class, cp.getAibpublicId());
                sb.append(sb.length() > 0 ? "," : "");
                sb.append(pub.getPublication()).append(" (").append(pub.getPubDate()).append(")");
            }
            return sb.toString();
        } catch (RemoteException ex) {
            log(ex);
        }
        return "can't load publications list";
    }
}
