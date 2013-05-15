/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import com.aib.dbutil.DbConnection;
import com.aib.remote.IMessageSender;
import com.aib.rmi.RmiMessageSender;
import java.awt.Color;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Timer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

/**
 *
 * @author nick
 */
public class AIBserver {

    private static final String version = "0.1";
    public static final String PROPERTYFILENAME = "AIBserver.config";
    private static final String ICONNAME = "aib.png";
    private static Logger logger = null;
    private static FileHandler fh;
    private static Thread rmiServer;
    private static TrayIcon ti;
    private static Properties props;
    private static boolean isTraySupported = SystemTray.isSupported();
//    private static final String XLEND_SERVER = "Xlend Server";
    private static Timer syncTimer;
    private static Thread syncThread;
    private static boolean isCycle = true;
    private static final int TIMESTEP = 60 * 1000;
    private static Thread backupThread;
    public static final Color unformColor = new Color(102, 125, 158);

//    private static boolean compareDbVersions() {
//        try {
//            Connection localConnection = DbConnection.getConnection();
////            Connection remoteConnection = SyncPushTimer.getRemoteDBconnection();
//            Dbversion vLocal = (Dbversion) new Dbversion(localConnection).loadOnId(1);
//            Dbversion vRemote = (Dbversion) new Dbversion(remoteConnection).loadOnId(1);
//            if (vLocal.getVersionId().intValue() != vRemote.getVersionId().intValue()) {
//                AIBserver.log("Local DB version:" + vLocal.getVersion() + "(" + vLocal.getVersionId() + ")"
//                        + " Remote DB version:" + vRemote.getVersion() + "(" + vRemote.getVersionId() + ")");
//            }
//            return vRemote.getVersionId().intValue() - vLocal.getVersionId().intValue() <= 1;
//        } catch (Exception ex) {
//            AIBserver.log(ex);
//        }
//        return false;
//    }
//    private static void ftpUpload(String absolutePath) {
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//    private static boolean upload2FTP(String fileName, String targetName, String fileToRemove) {
//        boolean ok = false;
//        String host = DbConnection.getFtpURL();
//        String user = DbConnection.getFtpLogin();
//        String pwd = DbConnection.getFtpPassword();
//        FileTransferClient ftp = null;
//        try {
//            ftp = new FileTransferClient();
//            ftp.setRemoteHost(host);
//            ftp.setUserName(user);
//            ftp.setPassword(pwd);
//            ftp.getAdvancedFTPSettings().setConnectMode(FTPConnectMode.ACTIVE);
//            AIBserver.log("FTP: connection...");
//            ftp.connect();
//            AIBserver.log("FTP: chdir to /root/backups...");
//            ftp.changeDirectory("root/backups");
//            try {
//                AIBserver.log("FTP: delete old dump " + fileToRemove + "...");
//                ftp.deleteFile(fileToRemove);
//            } catch (Exception eex) {
//            }
//            AIBserver.log("FTP: start upload " + targetName + "...");
//            ftp.uploadFile(fileName, targetName);
//            AIBserver.log("FTP: " + targetName + " uploaded!");
//            ftp.disconnect();
//            ok = true;
//        } catch (Exception ex) {
//            AIBserver.log(ex);
//            JOptionPane.showMessageDialog(null, ex.getMessage() + "\nServer will work as usually",
//                    "FTP upload failed", JOptionPane.WARNING_MESSAGE);
//        }
//        return ok;
//    }
    /**
     * @return the version
     */
    public static String getVersion() {
        return version;
    }

    private static void deleteLocalDumps(String ext, String exceptFile) {
        File folder = new File("./");
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.getName().endsWith(ext) && !fileEntry.getName().equals(exceptFile)) {
                if (fileEntry.exists()) {
                    fileEntry.delete();
                }
            }
        }
    }

    private static void closeAllConnections() {
        try {
            DbConnection.closeAllConnections();
        } catch (SQLException ex) {
            log(ex);
        }
    }

    private static class CtrlCtrapper extends Thread {

        private Timer oTimer;

        public CtrlCtrapper(Timer p_oTimer) {
            super();
            closeAllConnections();
            oTimer = p_oTimer;
        }

        public void run() {
            System.out.println("Ctrl+C pressed!!!");
            oTimer.cancel();
            rmiServer.stop();
        }
    };

    public static void log(String msg) {
        log(msg, null);
    }

    public static void log(Throwable th) {
        log(null, th);
    }

    private static void log(String msg, Throwable th) {
        if (logger == null) {
            try {
                logger = Logger.getLogger("AIBserver");
                fh = new FileHandler("%h/AIBserver.log", 1048576, 10, true);
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
        String appToLog = "\n" + (msg == null ? th.getMessage() : msg);
        LogViewDialog.logBuffer.append(appToLog);
        logger.log(Level.SEVERE, msg, th);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (!isTraySupported) {
            System.out.println("Java version: " + System.getProperty("java.version"));
            if (args.length < 1) {
                System.out.println("Usage:\n\tcom.csa.cmc.AIBserver [port] (default 1099)");
            }
        } else {
            try {
                String theme = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
                UIManager.setLookAndFeel(theme);
//                SwingUtilities.updateComponentTreeUI(this);
            } catch (Exception ex) {
            }
            props = new Properties();
            File propFile = new File(PROPERTYFILENAME);
            try {
                if (propFile.exists()) {
                    props.load(new FileInputStream(propFile));
                    DbConnection.setProps(props);
                    System.out.println("Properties loaded from " + PROPERTYFILENAME);
                }
                System.out.println("\nPress Ctrl+C to interrupt");
            } catch (IOException ioe) {
                log(ioe);
            }
            initTray();
        }
        final int port = (args.length > 0 ? Integer.parseInt(args[0]) : 1099);
        rmiServer = new Thread() {
            public void run() {
                try {
                    final Timer queueRunner = new Timer();
                    System.out.println("Starting server on port " + port + "... ");
                    java.rmi.registry.LocateRegistry.createRegistry(port);
                    IMessageSender c = new RmiMessageSender();
                    Naming.rebind("rmi://localhost:" + port + "/AIBserver", c);
                    if (!isTraySupported) {
                        Runtime.getRuntime().addShutdownHook(new CtrlCtrapper(queueRunner));
                    }
//                    runSyncService();
                } catch (Exception ex) {
                    log("RMI server trouble: " + ex.getMessage());
                    closeAllConnections();
                    System.exit(1);
                }
            }
        };
        rmiServer.start();
//        log("Start backup...");
//        makeBackup();
//        log("Backup completed!");
    }

//    private static void runSyncService() throws RemoteException {
//
//        if (compareDbVersions()) {
//            syncThread = new Thread() {
//                @Override
//                public void run() {
//                    while (isCycle) {
//                        SyncPushTimer.syncRemoteDB();
//                        try {
//                            sleep(TIMESTEP);
//                        } catch (InterruptedException ex) {
//                        }
//                    }
//                }
//            };
//            syncThread.start();
//            (syncTimer = new Timer()).schedule(new SyncPushTimer(), Calendar.getInstance().getTime(), TIMESTEP);
//        } else {
//            AIBserver.log("ATTENTION! Local and remote database versions "
//                    + "does not match, replication doesn't start!");
//        }
//    }
//    private static void makeBackup() {
//        Process p;
//        String mysqlDumpPath = props.getProperty("dbDump", "E:\\Program Files\\MySQL\\MySQL Server 5.1\\bin\\mysqldump.exe");//findPath(DbConnection.getBackupCommand());
//        String[] runCmd = new String[]{mysqlDumpPath, "-u",
//            DbConnection.getLogin(), "-p" + DbConnection.getPassword(), "xlend"
//        };
//        Calendar cal = Calendar.getInstance();
//        File dump = new File("xlend-" + cal.get(Calendar.YEAR) + "-"
//                + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH)
//                + ".dmp");
//        cal.add(Calendar.DATE, -5);
//        String fileToRemove = "xlend-" + cal.get(Calendar.YEAR) + "-"
//                + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH)
//                + ".dmp";
//
//        DataInputStream std = null;
//        DataInputStream stderr = null;
//        FileOutputStream dumpStream = null;
//        try {
//            p = Runtime.getRuntime().exec(runCmd);
//            stderr = new DataInputStream(p.getInputStream());
//            std = new DataInputStream(p.getInputStream());
//            dumpStream = new FileOutputStream(dump);
//            byte[] buf = new byte[1024];
//            int len;
//            while ((len = std.read(buf)) > 0) {
//                dumpStream.write(buf, 0, len);
//            }
//            StringBuffer errbuff = new StringBuffer();
//            String line;
//            while ((line = stderr.readLine()) != null) {
//                errbuff.append(line);
//            }
//            if (errbuff.length() > 0) {
//                JOptionPane.showMessageDialog(null, errbuff.toString(),
//                        "Backup error:", JOptionPane.ERROR_MESSAGE);
//            } else {
//                upload2FTP(dump.getAbsolutePath(), dump.getName(), fileToRemove);
//                deleteLocalDumps(".dmp", dump.getName());
////                File oldDump = new File(fileToRemove);
////                if (oldDump.exists()) {
////                    oldDump.delete();
////                }
//
//            }
//        } catch (Exception ex) {
//            AIBserver.log(ex);
//            JOptionPane.showMessageDialog(null, ex.getMessage() + "\nServer will work as usually",
//                    "Backup failed", JOptionPane.WARNING_MESSAGE);
//        } finally {
//            try {
//                if (dumpStream != null) {
//                    dumpStream.flush();
//                    dumpStream.close();
//                }
//            } catch (IOException ioe) {
//            }
//        }
//    }
//    private static String findPath(String progname) {
//        String initialName = progname;
//        String runCmd = progname;
//        String[] extnesions = new String[]{"", ".exe"};
//        String[] drives = new String[]{"C:", "E:", "D:"};
//        String[] paths = new String[]{"",
//            "\\Program Files\\MySQL\\MySQL Server 5.1\\bin\\",
//            "\\Program Files\\MySQL\\MySQL Server 5.2\\bin\\",
//            "\\Program Files\\MySQL\\MySQL Server 5.3\\bin\\",
//            "\\Program Files\\MySQL\\MySQL Server 5.4\\bin\\",
//            "\\Program Files\\MySQL\\MySQL Server 5.5\\bin\\"
//        };
//        Process p;
//        for (String drv : drives) {
//            for (String path : paths) {
//                for (String extension : extnesions) {
//                    try {
//                        p = Runtime.getRuntime().exec(progname = drv + path + runCmd + extension);
//                        return progname;
//                    } catch (Exception ex) {
//                    }
//                }
//            }
//        }
//        return initialName;
//    }
    public static Image loadImage(String iconName) {
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
                im = ImageIO.read(AIBserver.class.getResourceAsStream("/" + iconName));
            } catch (Exception ie) {
                log(ie);
            }
        }
        return im;
    }

    private static void initTray() {
        final SystemTray tray = SystemTray.getSystemTray();
        try {
            Image icon = loadImage(ICONNAME);
            final PopupMenu popup = new PopupMenu();
            MenuItem miExit = new MenuItem("Exit");
            miExit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    rmiServer.stop();
                    isCycle = false;
                    closeAllConnections();
                    System.exit(0);
                }
            });
            MenuItem miAbout = new MenuItem("About...");
            miAbout.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new AboutDialog();
                }
            });
            MenuItem miLog = new MenuItem("Server log...");
            miLog.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showLog();
                }
            });
//            MenuItem miFix = new MenuItem("Recreate Site Diary tables");
//            miFix.addActionListener(new ActionListener() {
//
//                public void actionPerformed(ActionEvent e) {
//                    recreateSiteDiary();
//                }
//
//                private void recreateSiteDiary() {
//                    DbConnection.recreateSiteDiary();
//                }
//            });

            popup.add(miLog);
            popup.add(miAbout);

//            if (props.getProperty("dbDriverName", "org.hsqldb.jdbcDriver").equals("org.hsqldb.jdbcDriver")) {
//                MenuItem miMigrate = new MenuItem("Migrate");
//                miMigrate.addActionListener(new ActionListener() {
//                    public void actionPerformed(ActionEvent e) {
//                        new MigrationDialog();
//                    }
//                });
//                popup.addSeparator();
//                popup.add(miMigrate);
//            }

            popup.addSeparator();
            popup.add(miExit);
            ti = new TrayIcon(icon, "AIB Server", popup);
            ti.setActionCommand("DoubleClick");
            ti.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showLog();
                }
            });
            tray.add(ti);
        } catch (Exception ex) {
            log("RMI server trouble: " + ex.getMessage());
            isCycle = false;
            closeAllConnections();
            System.exit(2);
        }
    }

    private static void showLog() {
        new LogViewDialog(getVersion(), DbConnection.DB_VERSION);
    }

    public static void setWindowIcon(Window w, String iconName) {
        w.setIconImage(loadImage(iconName));
    }
}
