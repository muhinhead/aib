package com.aib.rmi;

import com.aib.AIBclient;
import com.aib.remote.IMessageSender;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Nick Mukhin
 */
public class ExchangeFactory {

    private static final int DB_VERSION_ID = 54;
    public static final String DB_VERSION = "0.54";
    private static String[] fixLocalDBsqls = new String[]{
        "update dbversion set version_id = " + DB_VERSION_ID + ",version = '" + DB_VERSION + "'",
        //51->52
        "alter table people add fulltext "
        + "(source,first_name,last_name,suffix,job_discip,department,spec_address,"
        + "mailaddress,desk_phone,desk_fax,mobile_phone,main_email,alter_email,pa,pa_phone,pa_email)",
        "alter table location add fulltext(name,abbreviation,address,postcode,mailaddress,mailpostcode,main_phone,main_fax,comments)",
        "alter table company add fulltext(full_name,abbreviation,address,post_code,mailaddress,mailing_post_code,main_phone,main_fax)",
        //52->53
        "alter table people add country_id int",
        "alter table people add constraint people_country_fk foreign key(country_id) references country (country_id)",
        //53->54
        "alter table people add is_individual_member bit default 0"
    };

    public static IMessageSender getExchanger(String connectString, Properties props) {
        IMessageSender exchanger = null;
        if (connectString.startsWith("rmi:")) {
            try {
                exchanger = (IMessageSender) Naming.lookup(connectString);
                AIBclient.protocol = "rmi";
            } catch (Exception ex) {
                AIBclient.log("RMI server not found");
            }
        } else if (connectString.startsWith("jdbc:")) {
            String dbUser = props.getProperty("dbUser", "root");
            String dbPassword = props.getProperty("dbPassword", "root");
            String dbDriver = props.getProperty("dbDriverName", "com.mysql.jdbc.Driver");
            try {
                exchanger = createJDBCexchanger(dbDriver, connectString, dbUser, dbPassword);
                AIBclient.protocol = "jdbc";
            } catch (Exception ex) {
                AIBclient.log(ex);
            }
        }
        return exchanger;
    }

    public static IMessageSender createRMIexchanger(String address) throws NotBoundException, MalformedURLException, RemoteException {
        AIBclient.protocol = "rmi";
        return (IMessageSender) Naming.lookup("rmi://" + address + "/AIBserver");
    }

    public static IMessageSender createJDBCexchanger(String[] dbParams) throws SQLException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        if (dbParams.length < 4) {
            return null;
        }
        return createJDBCexchanger(dbParams[0], dbParams[1], dbParams[2], dbParams[3]);
    }

    public static IMessageSender createJDBCexchanger(String dbDriver, String connectString,
            String dbUser, String dbPassword) throws SQLException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        if (dbDriver == null || dbDriver.isEmpty() || connectString == null || connectString.isEmpty()
                || dbUser == null || dbUser.isEmpty() || dbPassword == null || dbPassword.isEmpty()) {
            throw new SQLException("Incomplete DB connection parameters");
        }
        AIBclient.protocol = "jdbc";
        IMessageSender exchanger;
        DriverManager.registerDriver(
                (java.sql.Driver) Class.forName(dbDriver).newInstance());
        Connection connection = DriverManager.getConnection(connectString, dbUser, dbPassword);
        connection.setAutoCommit(true);
        sqlBatch(fixLocalDBsqls, connection, false);
        exchanger = new DbClientDataSender(AIBclient.getProperties());
        return exchanger;
    }

    public static void sqlBatch(String[] sqls, Connection connection, boolean tolog) {
        PreparedStatement ps = null;
        for (int i = 0; i < sqls.length; i++) {
            try {
                ps = connection.prepareStatement(sqls[i]);
                ps.execute();
                if (tolog) {
                    AIBclient.log("STATEMENT [" + sqls[i].substring(0,
                            sqls[i].length() > 60 ? 60 : sqls[i].length()) + "]... processed");
                }
            } catch (SQLException e) {
                if (tolog) {
                    AIBclient.log(e);
                }
            } finally {
                try {
                    ps.close();
                } catch (SQLException ex) {
                }
            }
        }
    }
}
