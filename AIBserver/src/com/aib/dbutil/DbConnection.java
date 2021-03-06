package com.aib.dbutil;

//import com.xlend.AIBserver;
//import com.xlend.orm.Xopmachassing;
import com.aib.AIBserver;
import com.aib.rmi.RmiMessageSender;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Nick Mukhin
 */
public class DbConnection {

    private static class ConnectionWithFlag {

        boolean isBusy = false;
        Connection connection = null;
        long lastUsage = Calendar.getInstance().getTimeInMillis();

        ConnectionWithFlag(Connection c) {
            connection = c;
            isBusy = true;
//            AIBserver.log("connection (" + new Date(lastUsage).toString() + ") opened");
        }

        private void freeConnection() {
            isBusy = false;
//            AIBserver.log("connection (" + new Date(lastUsage).toString() + ") released");
        }
    }

    private static class ConnectionTouchTask extends TimerTask {

        private final boolean withLog;

        public ConnectionTouchTask(boolean withLog) {
            super();
            this.withLog = withLog;
        }

        @Override
        public void run() {
            for (ConnectionWithFlag cf : connections) {
                if (!cf.isBusy) {
                    if (Calendar.getInstance().getTimeInMillis() - cf.lastUsage > 10 * 1000) {
                        try {
                            cf.connection.close();
                            connections.remove(cf);
                            AIBserver.log("connection (" + new Date(cf.lastUsage).toString() + ") closed on timeout");
                            return;
                        } catch (SQLException ex) {
                            AIBserver.log(ex);
                        }
                    }
                } else {
                    AIBserver.log("connection busy");
                }
            }
        }
    }
    private static final int DB_VERSION_ID = 54;
    public static final String DB_VERSION = "0.54";
    private static boolean isFirstTime = true;
    private static Properties props = new Properties();
    private static String[] createLocalDBsqls = loadDDLscript("crebas.sql", ";");
    private static ArrayList<ConnectionWithFlag> connections = new ArrayList<ConnectionWithFlag>();
    private static String[] fixLocalDBsqls = new String[]{
        "update dbversion set version_id = " + DB_VERSION_ID + ",version = '" + DB_VERSION + "'",
        "delete from link where not exists("
        + "select complink_id from complink where link_id=link.link_id) and not exists("
        + "select peoplelink_id from peoplelink where link_id=link.link_id)",
        "delete from industry where not exists("
        + "select compindustry_id from compindustry where industry_id=industry.industry_id) and not exists("
        + "select peopleindustry_id from peopleindustry where industry_id=industry.industry_id)",
        "delete from aibpublic where not exists(select comppublic_id from comppublic where aibpublic_id=aibpublic.aibpublic_id)",
//        "alter table worldregion add post_price decimal(6,2) null",
//        "alter table worldregion add post_status int default 1",
//        "alter table worldregion add post_number int",
//        "alter table country add status bit default 1",
//        "alter table company add parent_id int null",
//        "alter table company add constraint company_company_fk foreign key (parent_id) references company (company_id)",
//        "alter table people add source varchar(50)",
//        "alter table people modify greeting varchar(32) null",
//        "alter table people modify desk_phone varchar(80) null",
//        "alter table people modify desk_fax varchar(80) null",
//        "alter table people modify job_discip varchar(150) null",
//        "alter table people add is_aib_coordinator bit default 0",
//        "alter table people add is_aib_judge     bit default 0",
//        "alter table people add is_aib_entrant   bit default 0",
//        "alter table location drop foreign key location_company_fk",
//        "alter table location add constraint location_company_fk foreign key (company_id) references company (company_id) on delete cascade",
//        "alter table people drop foreign key people_location_fk",
//        "alter table people add constraint people_location_fk foreign key (location_id) references location (location_id) on delete cascade",
//        //51->52
//        "alter table people add fulltext "
//            + "(source,first_name,last_name,suffix,job_discip,department,spec_address,"
//            + "mailaddress,desk_phone,desk_fax,mobile_phone,main_email,alter_email,pa,pa_phone,pa_email)",
//        "alter table location add fulltext(name,abbreviation,address,postcode,mailaddress,mailpostcode,main_phone,main_fax,comments)",
//        "alter table company add fulltext(full_name,abbreviation,address,post_code,mailaddress,mailing_post_code,main_phone,main_fax)",
//        //52->53
//        "alter table people add country_id int",
//        "alter table people add constraint people_country_fk foreign key(country_id) references country (country_id)"
        "alter table people CHARACTER SET utf8, COLLATE utf8_general_ci",
        "alter table company CHARACTER SET utf8, COLLATE utf8_general_ci",
        "alter table location CHARACTER SET utf8, COLLATE utf8_general_ci",        
        "alter table country CHARACTER SET utf8, COLLATE utf8_general_ci",
        //53->54
        "alter table people add is_individual_member bit default 0"        
    };

    public static String getLogin() {
        return props.getProperty("dbUser", "aib");
    }

    public static String getPassword() {
        return props.getProperty("dbPassword", "qwerty");
    }

    public static String getBackupCommand() {
        return props.getProperty("dbDump", "mysqldump");
    }

    public static String getFtpURL() {
        return props.getProperty("ftpURL", "ec2-54-226-3-180.compute-1.amazonaws.com");
    }

    public static String getFtpPath() {
        return props.getProperty("ftpPath", "/root/backups/");
    }

    public static String getFtpLogin() {
        return props.getProperty("ftpLogin", "alison");
    }

    public static String getFtpPassword() {
        return props.getProperty("ftpLogin", "dainton");
    }

    public static Connection getConnection() throws RemoteException {
        for (ConnectionWithFlag con : connections) {
            if (!con.isBusy) {
                con.isBusy = true;
//                AIBserver.log("connection (" + new Date(con.lastUsage).toString() + ") used");
                con.lastUsage = Calendar.getInstance().getTimeInMillis();
                return con.connection;
            }
        }
        Connection connection = null;
        try {
            Locale.setDefault(Locale.ENGLISH);
            DriverManager.registerDriver(
                    (java.sql.Driver) Class.forName(
                    props.getProperty("dbDriverName",
                    "com.mysql.jdbc.Driver")).newInstance());
            connection = DriverManager.getConnection(
                    props.getProperty("dbConnection",
                    "jdbc:mysql://aibcontact.db.9298823.hostedresource.com/aibcontact?characterEncoding=UTF8"),
                    getLogin(), getPassword());
            connection.setAutoCommit(true);
            RmiMessageSender.isMySQL = (connection.getClass().getCanonicalName().indexOf("mysql") > -1);

        } catch (Exception e) {
            AIBserver.log(e);
        }
        if (isFirstTime) {
            initLocalDB(connection);
            fixLocalDB(connection);
            Timer timer = new Timer();
            timer.schedule(new ConnectionTouchTask(false), 1000, 60 * 1000);
            isFirstTime = false;
        }
        if (connection != null) {
            connections.add(new ConnectionWithFlag(connection));
            return checkVersion(connection);
        } else {
            return null;
        }
    }

    public static void initLocalDB(Connection connection) {
        sqlBatch(createLocalDBsqls, connection, false);
    }

    public static void fixLocalDB(Connection connection) {
        sqlBatch(fixLocalDBsqls, connection,
                props.getProperty("LogDbFixes", "false").equalsIgnoreCase("true"));
    }

    public static void sqlBatch(String sql, Connection connection, boolean tolog) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.execute();
            if (tolog) {
                AIBserver.log("STATEMENT [" + sql.substring(0,
                        sql.length() > 60 ? 60 : sql.length()) + "]... processed");
            }
        } catch (SQLException e) {
            if (tolog) {
                AIBserver.log(e);
            }
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
            }
        }
    }

    public static void sqlBatch(String[] sqls, Connection connection, boolean tolog) {
        PreparedStatement ps = null;
        for (int i = 0; i < sqls.length; i++) {
            try {
                ps = connection.prepareStatement(sqls[i]);
                ps.execute();
                if (tolog) {
                    AIBserver.log("STATEMENT [" + sqls[i].substring(0,
                            sqls[i].length() > 60 ? 60 : sqls[i].length()) + "]... processed");
                }
            } catch (SQLException e) {
                if (tolog) {
                    AIBserver.log(e);
                }
            } finally {
                try {
                    ps.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    public static void setProps(Properties props) {
        DbConnection.props = props;
    }

    public static Properties getProps() {
        return props;
    }

    public static void closeConnection(Connection connection) throws SQLException {
        for (ConnectionWithFlag cf : connections) {
            if (cf.connection == connection) {
                cf.freeConnection();
                return;
            }
        }
        connection = null;
    }

    public static void closeAllConnections() throws SQLException {
        for (ConnectionWithFlag cf : connections) {
            cf.connection.close();
            cf.connection = null;
        }
        connections.clear();
    }

    public static String getCurDir() {
        File curdir = new File("./");
        return curdir.getAbsolutePath();
    }

    private static Connection checkVersion(Connection connection) throws RemoteException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String err;
        String stmt = "SELECT version_id,version FROM dbversion WHERE dbversion_id=1";
        int curversion_id = 0;
        String curversion = "0.0";
        try {
            ps = connection.prepareStatement(stmt);
            rs = ps.executeQuery();
            if (rs.next()) {
                curversion_id = rs.getInt(1);
                curversion = rs.getString(2);
            }
            if (DB_VERSION_ID > curversion_id || !curversion.equals(DB_VERSION)) {
                err = "Invalid database version! " + "expected:" + DB_VERSION + "(" + DB_VERSION_ID + ") "
                        + "found:" + curversion + "(" + curversion_id + ")";
                AIBserver.log(err);
                throw new RemoteException(err);
            }
        } catch (SQLException ex) {
            AIBserver.log(ex);
            try {
                closeConnection(connection);
            } catch (SQLException ex1) {
            }
        } finally {
            try {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                    }
                }
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException ex) {
                    }
                }
            }
        }
        return connection;
    }

    public static String[] loadDDLscript(String fname, String splitter) {
        String[] ans = new String[0];
        File sqlFile = new File(fname);
        boolean toclean = true;
        if (!sqlFile.exists()) {
            fname = "../sql/" + fname;
            sqlFile = new File(fname);
            toclean = false;
        }
        if (sqlFile.exists()) {
            StringBuffer contents = new StringBuffer();
            java.io.BufferedReader reader = null;
            try {
                reader = new java.io.BufferedReader(new FileReader(sqlFile));
                String line = null;
                int lineNum = 0;
                while ((line = reader.readLine()) != null) {
                    int cut = line.indexOf("--");
                    if (cut >= 0) {
                        line = line.substring(0, cut);
                    }
                    contents.append(line).append(System.getProperty("line.separator"));
                }
                ans = contents.toString().split(splitter);
            } catch (Exception e) {
                AIBserver.log(e);
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException ie) {
                }
            }
            if (toclean) {
                sqlFile.delete();
            }
        } else {
            AIBserver.log("File " + fname + " not found");
        }
        return ans;
    }
}
