package com.aib;

import com.aib.orm.Aibaward;
import com.aib.orm.Aibpublic;
import com.aib.orm.Company;
import com.aib.orm.Compindustry;
import com.aib.orm.Complink;
import com.aib.orm.Comppublic;
import com.aib.orm.Country;
import com.aib.orm.Filter;
import com.aib.orm.Industry;
import com.aib.orm.Link;
import com.aib.orm.Locindustry;
import com.aib.orm.Loclink;
import com.aib.orm.People;
import com.aib.orm.Peopleaward;
import com.aib.orm.Peoplecompany;
import com.aib.orm.Peopleindustry;
import com.aib.orm.Peopleinterest;
import com.aib.orm.Peoplelink;
import com.aib.orm.User;
import com.aib.orm.dbobject.ComboItem;
import com.aib.orm.dbobject.DbObject;
import com.aib.remote.IMessageSender;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.xlend.util.SelectedDateSpinner;
import java.awt.Color;
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
import javax.swing.ComboBoxModel;
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

    private static final String version = "0.5";
//    private static Userprofile currentUser;
    private static Logger logger = null;
    private static FileHandler fh;
    private static Properties props;
    private static final String PROPERTYFILENAME = "AIBclient.config";
    private static User currentUser;
    private static IMessageSender exchanger;
    private static ComboItem[] regionsDictionary;
    private static Country[] countryDictionary;
    private static ComboItem[] locationsDictionary;
    public static final Color HDR_COLOR = new Color(102, 125, 158);
    private static List prospLevelList;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(System.getProperty("user.home"));
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        String serverIP = readProperty("ServerAddress", "localhost");
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
                log(e);
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

    public static ComboItem[] loadAllUsersInitials() {
        return loadOnSelect(exchanger,
                "select user_id,concat(initials,' (',first_name,' ',last_name,')') "
                + "from user order by initials");
    }

    public static List loadAllLogins(String fld) {
        try {
            DbObject[] users = exchanger.getDbObjects(User.class, null, "login");
            ArrayList logins = new ArrayList();
            logins.add("");
            int i = 1;
            for (DbObject o : users) {
                User up = (User) o;
                if (fld.equals("login")) {
                    logins.add(up.getLogin());
                } else if (fld.equals("initials")) {
                    logins.add(up.getInitials());
                }
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

    private static List loadStringsOnSelect(IMessageSender exchanger, String select, String begin) {
        int pos = select.indexOf("select distinct ");
        String slct = pos == 0 ? select.replaceAll("select distinct ", "select distinct 0,")
                : select.replaceAll("select ", "select 0,");
        ComboItem[] itms = loadOnSelect(exchanger, slct);
        List answerArray = new ArrayList();
        if (begin != null) {
            answerArray.add(begin);
        }
        for (int i = 0; i < itms.length; i++) {
            answerArray.add(//i, 
                    itms[i].getValue());
        }
        return answerArray;
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

    public static ComboItem[] loadAllLocations() {
        if (locationsDictionary == null) {
            locationsDictionary = loadOnSelect(exchanger,
                    "select location_id, concat(l.name,' (',c.abbreviation,')') "
                    + "from location l, company c where c.company_id=l.company_id "
                    + "order by c.abbreviation,l.name");
        }
        return locationsDictionary;
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

    public static List loadAllAwards() {
        try {
            DbObject[] aibArray = exchanger.getDbObjects(Aibaward.class, null, "award_date desc");
            ArrayList pubs = new ArrayList();
            int i = 1;
            pubs.add("");
            for (DbObject o : aibArray) {
                Aibaward award = (Aibaward) o;
                pubs.add(award.getAward() + " (" + award.getAwardDate() + ")");
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

    public static boolean awardNotExist(String awardWithDate) {
        int qty = 0;
        int p = awardWithDate.indexOf("(");
        if (p > 0) {
            String action = awardWithDate.substring(0, p).trim();
            String pubDate = awardWithDate.substring(p + 1, awardWithDate.indexOf(")"));
            try {
                qty = exchanger.getCount(
                        "select aibaward_id from aibaward where award='"
                        + action + "' and award_date='" + pubDate + "'");
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

    public static void savePeopleCompany(Integer peopleID, String abbreviation) {
        try {
            Company comp = null;
            Peoplecompany pc = null;
            DbObject[] recs = getExchanger().getDbObjects(Company.class,
                    "abbreviation='" + abbreviation + "'", null);
            comp = (Company) recs[0];
            if (getExchanger().getCount("select peoplecompany_id "
                    + "from peoplecompany where people_id="
                    + peopleID + " and company_id=" + comp.getCompanyId()) == 0) {
                pc = new Peoplecompany(null);
                pc.setPeoplecompanyId(0);
                pc.setPeopleId(peopleID);
                pc.setCompanyId(comp.getCompanyId());
                pc.setNew(true);
                getExchanger().saveDbObject(pc);
            }
        } catch (Exception ex) {
            log(ex);
        }
    }

    public static void savePeopleAward(Integer peopleID, String award) {
        try {
            Aibaward aw = null;
            Peopleaward pa = null;
            DbObject[] recs = getExchanger().getDbObjects(Aibaward.class, "concat(award,' (',award_date,')')='" + award + "'", null);
            aw = (Aibaward) recs[0];
            if (getExchanger().getCount("select peopleaward_id from peopleaward where people_id="
                    + peopleID + " and aibaward_id=" + aw.getAibawardId()) == 0) {
                pa = new Peopleaward(null);
                pa.setPeopleawardId(0);
                pa.setPeopleId(peopleID);
                pa.setAibawardId(aw.getAibawardId());
                pa.setNew(true);
                getExchanger().saveDbObject(pa);
            }
        } catch (Exception ex) {
            log(ex);
        }
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
                getExchanger().saveDbObject(cp);
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
                ind = (Industry) getExchanger().saveDbObject(ind);
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
                getExchanger().saveDbObject(ci);
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
                lnk = (Link) getExchanger().saveDbObject(lnk);
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
                getExchanger().saveDbObject(ll);
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
                ind = (Industry) getExchanger().saveDbObject(ind);
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
                getExchanger().saveDbObject(li);
            }
        } catch (Exception ex) {
            log(ex);
        }
    }

    public static void saveOrInsertPeopleIndustry(Integer peopleID, String industry) {
        try {
            Industry ind = null;
            Peopleindustry pi = null;
            DbObject[] recs = getExchanger().getDbObjects(Industry.class, "descr='" + industry + "'", null);
            if (recs.length == 0) {
                ind = new Industry(null);
                ind.setIndustryId(0);
                ind.setDescr(industry);
                ind.setNew(true);
                ind = (Industry) getExchanger().saveDbObject(ind);
            } else {
                ind = (Industry) recs[0];
            }
            if (getExchanger().getCount("select peopleindustry_id from peopleindustry where people_id="
                    + peopleID + " and industry_id=" + ind.getIndustryId()) == 0) {
                pi = new Peopleindustry(null);
                pi.setPeopleindustryId(0);
                pi.setPeopleId(peopleID);
                pi.setIndustryId(ind.getIndustryId());
                pi.setNew(true);
                getExchanger().saveDbObject(pi);
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
                lnk = (Link) getExchanger().saveDbObject(lnk);
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
                getExchanger().saveDbObject(cl);
            }
        } catch (Exception ex) {
            log(ex);
        }
    }

    public static void saveOrInsertPeopleLink(Integer peopleID, String link) {
        try {
            Link lnk = null;
            Peoplelink pl = null;
            DbObject[] recs = getExchanger().getDbObjects(Link.class, "url='" + link + "'", null);
            if (recs.length == 0) {
                lnk = new Link(null);
                lnk.setLinkId(0);
                lnk.setUrl(link);
                lnk.setNew(true);
                lnk = (Link) getExchanger().saveDbObject(lnk);
            } else {
                lnk = (Link) recs[0];
            }
            if (getExchanger().getCount("select peoplelink_id from peoplelink where people_id="
                    + peopleID + " and link_id=" + lnk.getLinkId()) == 0) {
                pl = new Peoplelink(null);
                pl.setPeoplelinkId(0);
                pl.setPeopleId(peopleID);
                pl.setLinkId(lnk.getLinkId());
                pl.setNew(true);
                getExchanger().saveDbObject(pl);
            }
        } catch (Exception ex) {
            log(ex);
        }
    }

    public static void removeRedundantPeopleCompany(Integer peopleID, String abrreviationsList) {
        try {
            DbObject[] recs = getExchanger().getDbObjects(Peoplecompany.class,
                    "people_id=" + peopleID + " and company_id not in "
                    + "(select company_id from company where instr('" + abrreviationsList + "',abbreviation)>0)", null);
            for (DbObject rec : recs) {
                getExchanger().deleteObject(rec);
            }
        } catch (RemoteException ex) {
            log(ex);
        }
    }

    public static void removeRedundantAwards(Integer peopleID, String actionsList) {
        try {
            DbObject[] recs = getExchanger().getDbObjects(Peopleaward.class,
                    "people_id=" + peopleID + " and aibaward_id not in "
                    + "(select aibaward_id from aibaward where instr('" + actionsList + "',concat(award,' (',award_date,')'))>0)", null);
            for (DbObject rec : recs) {
                getExchanger().deleteObject(rec);
            }
        } catch (RemoteException ex) {
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
        removeRedundatnItms(Compindustry.class, companyID, "company", "industry", "descr", industryList);
    }

    public static void removeRedundantLocationIndustries(Integer locationID, String industryList) {
        removeRedundatnItms(Locindustry.class, locationID, "location", "industry", "descr", industryList);
    }

    public static void removeRedundantPeopleIndustries(Integer peopleID, String industryList) {
        removeRedundatnItms(Peopleindustry.class, peopleID, "people", "industry", "descr", industryList);
    }

    public static void removeRedundantPeopleLinks(Integer peopleID, String linkList) {
        removeRedundatnItms(Peoplelink.class, peopleID, "people", "link", "url", linkList);
    }

    public static void removeRedundantCompanyLinks(Integer companyID, String linkList) {
        removeRedundatnItms(Complink.class, companyID, "company", "link", "url", linkList);
    }

    public static void removeRedundantLocationLinks(Integer locationID, String linkList) {
        removeRedundatnItms(Loclink.class, locationID, "location", "link", "url", linkList);
    }

    private static void removeRedundatnItms(Class cl, Integer id, String source, String target, String fld, String list) {
        try {
            DbObject[] recs = getExchanger().getDbObjects(cl,
                    source + "_id=" + id + " and " + target + "_id not in (select " + target + "_id from " + target + " where instr('" + list + "'," + fld + ")>0)", null);
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

    public static String getLinkListOnPeopleID(Integer peopleID) {
        try {
            StringBuilder sb = new StringBuilder();
            DbObject[] recs = getExchanger().getDbObjects(Peoplelink.class, "people_id=" + peopleID, null);
            for (DbObject rec : recs) {
                Peoplelink pl = (Peoplelink) rec;
                Link lnk = (Link) getExchanger().loadDbObjectOnID(Link.class, pl.getLinkId());
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

    public static String getIndustryListOnPeopleID(Integer peopleID) {
        try {
            StringBuilder sb = new StringBuilder();
            DbObject[] recs = getExchanger().getDbObjects(Peopleindustry.class, "people_id=" + peopleID, null);
            for (DbObject rec : recs) {
                Peopleindustry pi = (Peopleindustry) rec;
                Industry ind = (Industry) getExchanger().loadDbObjectOnID(Industry.class, pi.getIndustryId());
                sb.append(sb.length() > 0 ? "," : "");
                sb.append(ind.getDescr());
            }
            return sb.toString();
        } catch (RemoteException ex) {
            log(ex);
        }
        return "can't load industries list";
    }

    public static String getCompaniesOnPeopleID(Integer peopleID) {
        try {
            StringBuilder sb = new StringBuilder();
            DbObject[] recs = getExchanger().getDbObjects(Peoplecompany.class, "people_id=" + peopleID, null);
            for (DbObject rec : recs) {
                Peoplecompany pc = (Peoplecompany) rec;
                Company comp = (Company) getExchanger().loadDbObjectOnID(Company.class, pc.getCompanyId());
                sb.append(sb.length() > 0 ? "," : "");
                sb.append(comp.getAbbreviation());
            }
            return sb.toString();
        } catch (RemoteException ex) {
            log(ex);
        }
        return "can't load actions list";
    }

    public static String getAwardsOnPeopleID(Integer peopleID) {
        try {
            StringBuilder sb = new StringBuilder();
            DbObject[] recs = getExchanger().getDbObjects(Peopleaward.class, "people_id=" + peopleID, null);
            for (DbObject rec : recs) {
                Peopleaward pa = (Peopleaward) rec;
                Aibaward aw = (Aibaward) getExchanger().loadDbObjectOnID(Aibaward.class, pa.getAibawardId());
                sb.append(sb.length() > 0 ? "," : "");
                sb.append(aw.getAward()).append(" (").append(aw.getAwardDate()).append(")");
            }
            return sb.toString();
        } catch (RemoteException ex) {
            log(ex);
        }
        return "can't load actions list";
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

    public static List loadDistinctJobDisciplines() {
        List ans = loadStringsOnSelect(getExchanger(),
                "select distinct job_discip from people order by job_discip", "");
//        ans.add("");
        return ans;
    }

    public static List loadDistinctDepartaments() {
        List ans = loadStringsOnSelect(getExchanger(),
                "select distinct department from departmenthistory "
                + "union select distinct department from people order by department", "");
//        ans.add("");
        return ans;
    }

    public static List loadDistinctTitles() {
        List ans = loadStringsOnSelect(getExchanger(),
                "select distinct title from people order by title", "");
//        ans.add("");
        return ans;
    }

    public static List loadDistinctAbbreviations() {
        List ans = loadStringsOnSelect(getExchanger(),
                "select distinct abbreviation from company order by abbreviation", "");
        return ans;
    }

    public static List loadDistinctSuffixes() {
        List ans = loadStringsOnSelect(getExchanger(),
                "select distinct suffix from people order by suffix", "");
//        ans.add("");
        return ans;
    }

    public static List loadDistinctGreetings() {
        List ans = loadStringsOnSelect(getExchanger(),
                "select distinct greeting from people order by greeting", "");
//        ans.add("");
        return ans;
    }

    public static List loadAllCompaniesShortNames() {
        List ans = loadStringsOnSelect(getExchanger(),
                "select '' abbreviation union "
                + "select concat(abbreviation,' - ',full_name) from company order by abbreviation", null);
        return ans;
    }

    public static boolean companyNotExists(String shortName) {
        try {
            DbObject[] recs = getExchanger().getDbObjects(Company.class, "abbreviation='" + shortName + "'", null);
            if (recs.length > 0) {
                return false;
            }
        } catch (RemoteException ex) {
            logAndShowMessage(ex);
        }
        return true;
    }

    public static ComboItem[] loadAllProducts() {
        return loadOnSelect(getExchanger(), "select product_id,descr from product order by descr");
    }

    public static ComboItem[] loadAllPeople() {
        return loadOnSelect(getExchanger(),
                "select people_id,concat(greeting,' ',substr(first_name,1,1),'.',last_name) "
                + "from people order by last_name");
    }

    public static String getPeopleInfoOnID(Integer peopleID) {
        DbObject rec;
        try {
            rec = getExchanger().loadDbObjectOnID(People.class, peopleID);
            if (rec != null) {
                People p = (People) rec;
                return p.getGreeting() + " "
                        + (p.getFirstName() != null && p.getFirstName().length() > 0 ? p.getFirstName().substring(0, 1) + "." : "")
                        + p.getLastName();
            }
        } catch (RemoteException ex) {
            log(ex);
        }
        return "unknown";
    }

    public static List getProspLevelsList() {
        if (prospLevelList == null) {
            prospLevelList = new ArrayList<String>();
            prospLevelList.add("No budget or not sure");
            prospLevelList.add("Planned purchase in next 1 month");
            prospLevelList.add("Planned purchase in next 3 months");
            prospLevelList.add("Planned purchase in next 6 months");
        }
        return prospLevelList;
    }

    public static java.util.Date getNearestPurchaseTimeScale(Integer peopleId, SelectedDateSpinner timescaleSP) {
        java.util.Date dt = (java.util.Date) timescaleSP.getValue();
        timescaleSP.setVisible(false);
        try {
            DbObject[] recs = getExchanger().getDbObjects(Peopleinterest.class,
                    "people_id=" + peopleId + " and purchase_date="
                    + "(select min(purchase_date) from peopleinterest where people_id="
                    + peopleId + " and purchase_date>=now())", null);
            if (recs.length > 0) {
                Peopleinterest pi = (Peopleinterest) recs[0];
                dt = new java.util.Date(pi.getPurchaseDate().getTime());
                timescaleSP.setValue(dt);
                timescaleSP.setVisible(true);
            }
        } catch (RemoteException ex) {
            log(ex);
        }
        return dt;
    }

    public static DbObject[] allFilters(String tabname) {
        DbObject[] ans = new DbObject[0];
        try {
            ans = getExchanger().getDbObjects(Filter.class, "tablename='" + tabname + "'", "name");
        } catch (RemoteException ex) {
            log(ex);
        }
        return ans;
    }
//    public static ListModel loadFilterList(String tabname) throws RemoteException {
//        final DbObject[] recs = getExchanger().getDbObjects(Filter.class, "tablename='"+tabname+"'", "name");
//        
//        return new ListModel() {
//
//            @Override
//            public int getSize() {
//                return recs.length;
//            }
//
//            @Override
//            public Object getElementAt(int i) {
//                Filter f = (Filter) recs[i];
//                return f.getName();
//            }
//
//            @Override
//            public void addListDataListener(ListDataListener ll) {
//                
//            }
//
//            @Override
//            public void removeListDataListener(ListDataListener ll) {
//                
//            }
//        };
//    }

    public static ComboItem[] loadAllFilters(String tableName) {
        ComboItem[] fltrs = loadOnSelect(getExchanger(),
                "select filter_id,name from filter where tablename='" + tableName + "'");
        ComboItem[] fltrs1 = new ComboItem[fltrs.length+1];
        fltrs1[0] = new ComboItem(-1, "300 last edited rows");
        for (int i=1; i<=fltrs.length; i++) {
            fltrs1[i] = fltrs[i-1];
        }
        return fltrs1;
    }
}
