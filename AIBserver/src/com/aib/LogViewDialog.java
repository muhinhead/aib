package com.aib;

import com.xlend.util.PopupDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Nick Mukhin
 */
public class LogViewDialog extends PopupDialog {

    private AbstractAction closeAction;
    private JButton closeBtn;
    private AbstractAction sendLogAction;
    private JButton sendLogBtn;
    private JScrollPane sp;
//    private String serverVersion;
//    private String dbVersion;
    public static StringBuffer logBuffer = new StringBuffer().append(loadFile()).append("--Started at:" + Calendar.getInstance().getTime() + "\n");

    public LogViewDialog(String serverVerion, String dbVersion) {
        super(null, "AIB Server Log", new String[]{serverVerion, dbVersion});
        AIBserver.setWindowIcon(this, "aib.png");
    }

    @Override
    protected Color getHeaderBackground() {
        return AIBserver.unformColor;
    }

    @Override
    protected void fillContent() {
        super.fillContent();
//        String[] versions = (String[]) getObject();
//        serverVersion = versions[0];
//        dbVersion = versions[1];

        JTextArea logViewArea = new JTextArea();
        logViewArea.setText(logBuffer.toString());

        JPanel btnPanel = new JPanel();
        btnPanel.add(sendLogBtn = new JButton(sendLogAction = new AbstractAction("Send log to developer") {
            public void actionPerformed(ActionEvent e) {
                if (sendLog("mukhin.nick@gmail.com")) {
                    JOptionPane.showMessageDialog(null, "The server's log sent succesfully");
                } else {
                    JOptionPane.showMessageDialog(null, "Can't send log, sorry");
                }
            }
        }));
        btnPanel.add(closeBtn = new JButton(closeAction = new AbstractAction("Close") {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }));

        getContentPane().add(sp = new JScrollPane(logViewArea));
        sp.setPreferredSize(new Dimension(800, 400));
        getContentPane().add(btnPanel, BorderLayout.SOUTH);
    }

    @Override
    public void freeResources() {
        sendLogBtn.removeActionListener(closeAction);
        sendLogAction = null;
        closeBtn.removeActionListener(closeAction);
        closeAction = null;
    }

    private static String loadFile() {
        BufferedReader reader = null;
        try {
            StringBuffer sb = new StringBuffer();
            reader = new BufferedReader(new FileReader(System.getProperty("user.home")
                    + "/AIBserver.log.0"));
            String text;
            while ((text = reader.readLine()) != null) {
                sb.append(text).append("\n");
            }
            return sb.toString();
        } catch (Exception ex) {
            AIBserver.log(ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                }
            }
        }
        return "...Error loading log...";
    }

    public boolean sendLog(String email) {
        Properties mailProps = new Properties();
        String[] versions = (String[]) getObject();
        String STARTTLS = "true";
        String AUTH = "true";
        String DEBUG = "true";
        String SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";
        String SUBJECT = "AIBserver server version:" + versions[0]
                + " db version:" + versions[1] + " sent:" + Calendar.getInstance().getTime();
        String PORT = "465";
        String HOST = "smtp.yandex.ru";
        String FROM = "nm250660@yandex.ru";
        String PASSWORD = "ghbdtnnt";
        String USER = FROM;
        mailProps.put("mail.smtp.host", HOST);
        mailProps.put("mail.smtp.port", PORT);
        mailProps.put("mail.smtp.user", FROM);
        mailProps.put("mail.smtp.socketFactory.port", PORT);
        mailProps.put("mail.smtp.auth", AUTH);
        mailProps.put("mail.smtp.starttls.enable", STARTTLS);
        mailProps.put("mail.smtp.debug", DEBUG);
        mailProps.put("mail.smtp.socketFactory.class", SOCKET_FACTORY);
        mailProps.put("mail.smtp.socketFactory.fallback", "false");

        try {
            Session session = Session.getDefaultInstance(mailProps, null);

            MimeMessage message = new MimeMessage(session);
            message.setText(logBuffer.toString());

            message.setSubject(SUBJECT);
            message.setFrom(new InternetAddress(FROM));
            message.addRecipient(RecipientType.TO, new InternetAddress(email));
            message.saveChanges();

            Transport transport = session.getTransport("smtp");
            transport.connect(HOST, USER, PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            return true;
        } catch (Exception ex) {
            AIBserver.log(ex);
        }
        return false;
    }
}
