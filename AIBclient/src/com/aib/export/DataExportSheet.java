/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.export;

import com.aib.AIBclient;
import com.aib.GeneralFrame;
import com.aib.GeneralGridPanel;
import com.aib.orm.Reportform;
import com.aib.orm.Reportformitem;
import com.aib.orm.dbobject.DbObject;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.xlend.util.FileFilterOnExtension;
import com.xlend.util.PopupDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Nick Mukhin
 */
public class DataExportSheet extends PopupDialog {

    private Vector[] result;

    public DataExportSheet(Frame frame, Object[] params) {//params=>{Integer templateId, String selectExp}
        super(frame, "Export data for table " + getTableName((Integer) params[0]), params);
    }

    @Override
    protected Color getHeaderBackground() {
        return AIBclient.HDR_COLOR;
    }

    @Override
    protected void fillContent() {
        super.fillContent();
        final String tmpHtmlName = System.getProperty("user.home").replace("\\", "/") + "/$tmp.html";
        Object[] params = (Object[]) getObject();
        generateOutputHTML(tmpHtmlName, (Integer) params[0], (String) params[1]);
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(new JButton(new AbstractAction("Export as HTML") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                File expFile = chooseFileForExport("html");
                if (expFile != null && !tmpHtmlName.equals(expFile.getPath())) {
                    if (expFile.exists()) {
                        expFile.delete();
                    }
                    try {
                        if (!new File(tmpHtmlName).renameTo(expFile)) {
                            GeneralFrame.errMessageBox("Error", "Couldn't rename file " + tmpHtmlName + " to " + expFile);
                        } else {
                            GeneralFrame.infoMessageBox("Ok", "Data exported to " + expFile.getPath());
                            if (expFile != null) {
                                Desktop desktop = Desktop.getDesktop();
                                desktop.open(expFile);
                                dispose();
                            }
                        }
                    } catch (Exception ex) {
                        AIBclient.logAndShowMessage(ex);
                    }
                }
            }
        }));
        btnPanel.add(new JButton(new AbstractAction("Export as PDF") {

            @Override
            public void actionPerformed(ActionEvent e) {
                File pdfFile = chooseFileForExport("pdf");
                if (pdfFile != null && !tmpHtmlName.equals(pdfFile.getPath())) {
                    if (pdfFile.exists()) {
                        pdfFile.delete();
                    }
                    try {
                        Document document = new Document();
                        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
                        document.open();
                        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                                new FileInputStream(tmpHtmlName));
                        document.close();

                        if (pdfFile != null) {
                            GeneralFrame.infoMessageBox("Ok", "Data exported to " + pdfFile.getPath());
                            Desktop desktop = Desktop.getDesktop();
                            desktop.open(pdfFile);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(rootPane,
                                    "Can't create file " + pdfFile.getAbsolutePath()
                                    + "! Check the target folder permissions", "Error!",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        AIBclient.logAndShowMessage(ex);
                    }
                }
            }

        }));
        btnPanel.add(new JButton(new AbstractAction("Export as CSV") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int col;
                File expFile = chooseFileForExport("csv");
                if (expFile != null) {
                    if (expFile.exists()) {
                        expFile.delete();
                    }
                    BufferedOutputStream bufferedOutput = null;
                    try {
                        bufferedOutput = new BufferedOutputStream(new FileOutputStream(expFile));
                        Vector headers = result[0];
                        for (col = 0; col < headers.size(); col++) {
                            if (col > 0) {
                                bufferedOutput.write(',');
                            }
                            if (headers.get(col).toString().endsWith("address") || headers.get(col).toString().endsWith("addr.")) {
                                for (int n = 1; n <= 3; n++) {
                                    bufferedOutput.write((headers.get(col).toString().replace("_", " ") + n + (n < 3 ? "," : "")).getBytes());
                                }
                            } else {
                                bufferedOutput.write(headers.get(col).toString().replace("_", " ").replace("discip", "title").getBytes());
                            }
                        }
                        bufferedOutput.write('\n');
                        Vector lines = result[1];
                        for (Object l : lines) {
                            String buff = "";
                            col = 0;
                            Vector line = (Vector) l;
                            for (Object c : line) {
                                if (col > 0) {
                                    bufferedOutput.write(',');
                                    buff += ',';
                                }
                                if (headers.get(col).toString().endsWith("address") || headers.get(col).toString().endsWith("addr.")) {
                                    String[] addrArray = c.toString().split("\n", 3);
                                    for (int i = 0; i < 3; i++) {
                                        if (i < addrArray.length && addrArray[i].indexOf(",") > 0) {
                                            bufferedOutput.write(("\"" + addrArray[i].replaceAll("\n", " ") + "\"" + (i < 2 ? "," : "")).getBytes());
                                        } else {
                                            bufferedOutput.write(((i < addrArray.length ? addrArray[i].replaceAll("\n", " ") : "") + (i < 2 ? "," : "")).getBytes());
                                        }
                                        buff += (i < addrArray.length ? addrArray[i].replaceAll("\n", "") : "") + (i < 2 ? "," : "");
                                    }

                                } else {
                                    if (c.toString().indexOf(",") > 0) {
                                        bufferedOutput.write(("\"" + c.toString() + "\"").getBytes());
                                        buff += ("\"" + c.toString() + "\"");
                                    } else {
                                        bufferedOutput.write(c.toString().getBytes());
                                        buff += c.toString();
                                    }
                                }
                                col++;
                            }
                            bufferedOutput.write('\n');
                            buff += '\n';
                        }
                        GeneralFrame.infoMessageBox("Ok", "Data exported to " + expFile.getPath());
                        if (expFile != null) {
                            Desktop desktop = Desktop.getDesktop();
                            desktop.open(expFile);
                            dispose();
                        }
                    } catch (Exception ex) {
                        AIBclient.logAndShowMessage(ex);
                    } finally {
                        if (bufferedOutput != null) {
                            try {
                                bufferedOutput.flush();
                                bufferedOutput.close();
                            } catch (IOException ex) {
                            }
                        }
                    }
                }
            }

        }));
        btnPanel.add(new JButton(new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
        }));
        getContentPane().add(btnPanel, BorderLayout.SOUTH);
//        browser.start();
    }

    private File chooseFileForExport(String extension) {
        JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
        chooser.setFileFilter(new FileFilterOnExtension(extension));
        chooser.setDialogTitle("File to export");
        chooser.setApproveButtonText("Save");
        int retVal = chooser.showOpenDialog(null);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            String name = chooser.getSelectedFile().getAbsolutePath();
            if (!name.endsWith("." + extension)) {
                name += "." + extension;
            }
            return new File(name);
        } else {
            return null;
        }
    }

    @Override
    public void freeResources() {
    }

    private static String getTableName(Integer templateId) {
        try {
            Reportform template = (Reportform) AIBclient.getExchanger()
                    .loadDbObjectOnID(Reportform.class, templateId);
            return template.getTablename();
        } catch (RemoteException ex) {
            AIBclient.logAndShowMessage(ex);
        }
        return "unknown table";
    }

//    private static void saveImage(String fname, byte[] imageData) {
//        File fout = new File(System.getProperty("user.home") + "/" + fname);
//        Util.writeFile(fout, imageData);
//    }
    private void generateOutputHTML(String tmpHtmlName, Integer tmpID, String select) {
        StringBuffer sb = new StringBuffer(select);
        int braceLevel = 0;
        int i = 0;
        for (i = 0; i < select.length(); i++) {
            if (sb.charAt(i) == '(') {
                braceLevel++;
            } else if (sb.charAt(i) == ')') {
                braceLevel--;
            } else if (sb.substring(i).toLowerCase().startsWith(" from ") && braceLevel == 0) {
                break;
            }
        }
        StringBuilder newSelect = new StringBuilder(select.startsWith("select distinct") ? "select distinct " : "select ");
        newSelect.append(getColumnList(tmpID)).append(sb.substring(i).replaceAll(GeneralGridPanel.SELECTLIMIT, ""));
        BufferedOutputStream bufferedOutput = null;
        try {
            result = AIBclient.getExchanger().getTableBody(newSelect.toString());
            Vector tds = result[0];
            Vector lines = result[1];
            bufferedOutput = new BufferedOutputStream(new FileOutputStream(tmpHtmlName));
            bufferedOutput.write("<html>\n".getBytes());
            bufferedOutput.write("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"/>\n".getBytes());
            bufferedOutput.write("<head>\n".getBytes());
            bufferedOutput.write("</head>\n".getBytes());
            bufferedOutput.write(("<style type=\"text/css\">"
                    + "table.mystyle"
                    + "{"
                    + "border-width: 1 1 1px 1px;"
                    + "border-spacing: 0;"
                    + "border-collapse: collapse;"
                    + "border-style: solid;"
                    + "}"
                    + ".mystyle td, .mystyle th"
                    + "{"
                    + "margin: 0;"
                    + "padding: 4px;"
                    + "border-width: 1px 1px 1 1;"
                    + "border-style: solid;"
                    + "}"
                    + "</style>\n").getBytes());
            bufferedOutput.write("<body>\n".getBytes());
            bufferedOutput.write("<table class=\"mystyle\">\n".getBytes());
            bufferedOutput.write("<tr>\n".getBytes());
            for (Object td : tds) {
                bufferedOutput.write(("<th>" + td.toString().replace("_", " ").replace("discip", "title") + "</th>\n").getBytes());
            }
            bufferedOutput.write("</tr>\n".getBytes());
//            int rownum = 0;
            for (Object l : lines) {
                bufferedOutput.write("<tr>\n".getBytes());
                Vector line = (Vector) l;
//                int colnum = 0;
                for (Object c : line) {
                    bufferedOutput.write("<td>\n".getBytes());
                    bufferedOutput.write(c.toString().replace("\n", "<p/>").getBytes());
                    bufferedOutput.write("</td>\n".getBytes());
//                    colnum++;
                }
                bufferedOutput.write("</tr>\n".getBytes());
//                rownum++;
            }
            bufferedOutput.write("</table>\n".getBytes());
            bufferedOutput.write("</body></html>\n".getBytes());
        } catch (Exception ex) {
            AIBclient.logAndShowMessage(ex);
        } finally {
            if (bufferedOutput != null) {
                try {
                    bufferedOutput.flush();
                    bufferedOutput.close();
                } catch (IOException ex) {
                    AIBclient.log(ex);
                }
            }
        }
    }

    private String getColumnList(Integer tmpID) {
        try {
            DbObject[] cols = AIBclient.getExchanger().getDbObjects(
                    Reportformitem.class, "reportform_id=" + tmpID, "reportformitem_id");
            StringBuilder colsList = new StringBuilder();
            String outerTable = getTableName(tmpID).toLowerCase();
            for (DbObject c : cols) {
                Reportformitem itm = (Reportformitem) c;
                colsList.append(colsList.length() > 0 ? "," : "");
                if (itm.getColumnname().equals("mailaddress") || itm.getColumnname().equals("address")
                        || itm.getColumnname().equals("mailpostcode") || itm.getColumnname().equals("postcode")) {
                    colsList.append(outerTable + "." + itm.getColumnname());
                } else if (itm.getColumnname().equals("Links")) {
                    if (outerTable.equals("company")) {
                        colsList.append("(select group_concat(url) "
                                + "from complink join link on "
                                + "complink.link_id=link.link_id "
                                + "where company_id=company.company_id) as \"Links\"");
                    } else if (outerTable.equals("people")) {
                        colsList.append("(select group_concat(url) "
                                + "from peoplelink join link on "
                                + "peoplelink.link_id=link.link_id "
                                + "where people_id=people.people_id) as \"Links\"");
                    } else if (outerTable.equals("location")) {
                        colsList.append("(select group_concat(url) "
                                + "from loclink join link on "
                                + "loclink.link_id=link.link_id "
                                + "where location_id=location.location_id) as \"Links\"");
                    }
                } else if (itm.getColumnname().equals("Industries")) {
                    if (outerTable.equals("company")) {
                        colsList.append("(select group_concat(descr) "
                                + "from compindustry join industry on "
                                + "compindustry.industry_id=industry.industry_id "
                                + "where company_id=company.company_id) as \"Industries\"");
                    } else if (outerTable.equals("people")) {
                        colsList.append("(select group_concat(descr) "
                                + "from peopleindustry join industry on "
                                + "peopleindustry.industry_id=industry.industry_id "
                                + "where people_id=people.people_id) as \"Industries\"");
                    } else if (outerTable.equals("location")) {
                        colsList.append("(select group_concat(descr) "
                                + "from locindustry join industry on "
                                + "locindustry.industry_id=industry.industry_id "
                                + "where location_id=location.location_id) as \"Industries\"");
                    }
                } else if (itm.getColumnname().equals("Companies") && outerTable.equals("people")) {
                        colsList.append("(select group_concat(full_name) "
                                + "from peoplecompany join company on "
                                + "peoplecompany.company_id=company.company_id "
                                + "where people_id=people.people_id) as \"Companies\"");
                } else if (itm.getColumnname().equals("Countries") && outerTable.equals("people")) {
                        colsList.append("(select group_concat(country) from peoplecompany join company on "
                                + "peoplecompany.company_id=company.company_id, country "
                                + "where company.country_id=country.country_id and people_id=people.people_id) as \"" + itm.getColumnname() + "\"");
                } else if (itm.getColumnname().equals("Company physical addr.") && outerTable.equals("people")) {
                        colsList.append("(select replace(group_concat(address),',',char(10)) from peoplecompany join company on "
                                + "peoplecompany.company_id=company.company_id "
                                + "where people_id=people.people_id) as \"" + itm.getColumnname() + "\"");
                } else if (itm.getColumnname().equals("Company physical post code.") && outerTable.equals("people")) {
                        colsList.append("(select group_concat(post_code) from peoplecompany join company on "
                                + "peoplecompany.company_id=company.company_id "
                                + "where people_id=people.people_id) as \"" + itm.getColumnname() + "\"");
                } else if (itm.getColumnname().equals("Company mailing addr.") && outerTable.equals("people")) {
                    colsList.append("(select replace(group_concat(mailaddress),',',char(10)) from peoplecompany join company on "
                            + "peoplecompany.company_id=company.company_id "
                            + "where people_id=people.people_id) as \"" + itm.getColumnname() + "\"");
                } else if (itm.getColumnname().equals("Company mailing post code") && outerTable.equals("people")) {
                    colsList.append("(select group_concat(mailing_post_code) from peoplecompany join company on "
                            + "peoplecompany.company_id=company.company_id "
                            + "where people_id=people.people_id) as \"" + itm.getColumnname() + "\"");
                } else if (itm.getColumnname().equals("Location physical addr.") && outerTable.equals("people")) {
                    colsList.append("(select address from location where location_id=people.location_id) as \"" + itm.getColumnname() + "\"");
                } else if (itm.getColumnname().equals("Location physical post code.") && outerTable.equals("people")) {
                    colsList.append("(select postcode from location where location_id=people.location_id) as \"" + itm.getColumnname() + "\"");
                } else if (itm.getColumnname().equals("Location mailing addr.") && outerTable.equals("people")) {
                    colsList.append("(select mailaddress from location where location_id=people.location_id) as \"" + itm.getColumnname() + "\"");
                } else if (itm.getColumnname().equals("Location mailing post code") && outerTable.equals("people")) {
                    colsList.append("(select mailpostcode from location where location_id=people.location_id) as \"" + itm.getColumnname() + "\"");
                } else if (itm.getColumnname().equals("company_id")) {
                    colsList.append("(select abbreviation from company where company_id="
                            + outerTable + ".company_id) as \"Company\"");
                } else if (itm.getColumnname().equals("location_id")) {
                    colsList.append("(select abbreviation from location where location_id="
                            + outerTable + ".location_id) as \"Location\"");
                } else if (itm.getColumnname().equals("country_id")) {
                    colsList.append("(select shortname from country where country_id="
                            + outerTable + ".country_id) as \"Country\"");
                } else if (itm.getColumnname().equals("lastedited_by")) {
                    colsList.append("(select initials from user where user_id="
                            + outerTable + ".lastedited_by) as \"Edited By\"");
                } else {
                    colsList.append(itm.getColumnname())
                            .append(itm.getHeader() != null ? " as \"" + itm.getHeader() + "\"" : "");
                }
            }
            return colsList.toString();
        } catch (RemoteException ex) {
            AIBclient.logAndShowMessage(ex);
        }
        return "*";
    }
}
