/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import static com.aib.AIBclient.getCurrentUser;
import com.aib.lookup.ListInTextFieldDialog;
import static com.aib.RecordEditPanel.getBorderPanel;
import static com.aib.RecordEditPanel.getGridPanel;
import com.aib.lookup.PublicationsListInTextFieldDialog;
import com.aib.lookup.WorldRegionLookupAction;
import com.aib.orm.Company;
import com.aib.orm.User;
import com.aib.orm.dbobject.ComboItem;
import com.aib.orm.dbobject.DbObject;
import com.jidesoft.swing.JideTabbedPane;
import com.xlend.util.PopupDialog;
import com.xlend.util.SelectedDateSpinner;
import com.xlend.util.SelectedNumberSpinner;
import com.xlend.util.Util;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author nick
 */
class EditCompanyPanel extends EditPanelWithPhoto {

    private JTextField idField;
    private DefaultComboBoxModel regionWorldCbModel;
    private DefaultComboBoxModel countryCbModel;
    private JTextField fullCompanyNameTF;
    private JCheckBox isDummyCB;
    private SelectedNumberSpinner turnoverSP;
    private JTextField physicAddressTF;
    private JTextField postCodeTF;
    private JTextField mailingAddressTF;
    private JComboBox regionWorldCb;
    private JComboBox countryCB;
    private JTextField abbreviationTF;
    private JTextField linksListTF;
    private JTextField industriesListTF;
    private static AbstractAction linkListAction;
    private static AbstractAction industryListAction;
    private JTextField mailingPostCodeTF;
    private JTextField mainPhoneTF;
    private JTextField mainFaxTF;
    private JComboBox membershipLevelCB;
    private JTextField mentionsListTF;
    private AbstractAction mentionListAction;
    private JSpinner lastVerifiedDateSP;
    private JTextField lastEditorTF;
    private SelectedDateSpinner lastEditedSP;

    private class RegionsLookupAction extends WorldRegionLookupAction {

        RegionsLookupAction(JComboBox cb) {
            super(cb);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            super.actionPerformed(ae);
            syncCountries();
        }
    }

    public EditCompanyPanel(DbObject dbObject) {
        super(dbObject);
    }

    @Override
    protected void fillContent() {
        String titles[] = new String[]{
            "ID:",
            "Full Company Name:", //            "Part Description:",
            "Dummy Company:",//           "Abbreviation:",
            "Links:",
            "Industry:",
            "Turnover/Year:",
            "Physical Address:",
            "Post Code:",
            "Mailing Address:",
            "Mailing Post Code:",
            "Region of World:", //,"Country:"
            "Main Phone:", //"Main Fax;"
            "Membership Level:",
            "AIB mentions:",
            "Date last verified:" //"Last editor:" //"Last edited:
        };
        regionWorldCbModel = new DefaultComboBoxModel();
        countryCbModel = new DefaultComboBoxModel();
        for (ComboItem ci : AIBclient.loadAllRegions()) {
            regionWorldCbModel.addElement(ci);
        }
        JComponent[] edits = new JComponent[]{
            getGridPanel(idField = new JTextField(), 6),
            getGridPanel(new JComponent[]{
                fullCompanyNameTF = new JTextField(),
                getGridPanel(new JComponent[]{
                    new JLabel("Abbreviation:", SwingConstants.RIGHT),
                    abbreviationTF = new JTextField()
                })
            }),
            getGridPanel(isDummyCB = new JCheckBox(), 6),
            getBorderPanel(new JComponent[]{null, linksListTF = new JTextField(),
                new JButton(getLinkListAction("..."))}),
            getBorderPanel(new JComponent[]{null, industriesListTF = new JTextField(),
                new JButton(getIndustryListAction("..."))}),
            getBorderPanel(new JComponent[]{turnoverSP = new SelectedNumberSpinner(.0, .0, 999999999.0, 100.0)}),
            physicAddressTF = new JTextField(),
            getGridPanel(postCodeTF = new JTextField(), 4),
            mailingAddressTF = new JTextField(),
            getGridPanel(mailingPostCodeTF = new JTextField(), 4),
            getGridPanel(new JComponent[]{
                comboPanelWithLookupBtn(regionWorldCb = new JComboBox(regionWorldCbModel),
                new RegionsLookupAction(regionWorldCb)),
                new JLabel("Country:", SwingConstants.RIGHT),
                countryCB = new JComboBox(countryCbModel)
            }),
            getGridPanel(new JComponent[]{
                mainPhoneTF = new JTextField(), new JLabel("Main Fax:", SwingConstants.RIGHT),
                mainFaxTF = new JTextField()
            }),
            getGridPanel(membershipLevelCB = new JComboBox(new String[]{"1", "2", "3", "4", "5", "6"}), 6),
            getBorderPanel(new JComponent[]{null, mentionsListTF = new JTextField(),
                new JButton(getMentionsListAction("..."))}),
            getBorderPanel(new JComponent[]{
                lastVerifiedDateSP = new SelectedDateSpinner(),
                null,
                getGridPanel(new JComponent[]{
                    getBorderPanel(new JComponent[]{
                        new JLabel("Last editor:", SwingConstants.RIGHT),
                        lastEditorTF = new JTextField(2)
                    }),
                    getBorderPanel(new JComponent[]{
                        new JLabel("Last edited:", SwingConstants.RIGHT),
                        lastEditedSP = new SelectedDateSpinner()
                    })
                })
            })
        };
        idField.setEnabled(false);
        lastVerifiedDateSP.setEditor(new JSpinner.DateEditor(lastVerifiedDateSP, DD_MM_YYYY));
        Util.addFocusSelectAllAction(lastVerifiedDateSP);
        lastEditedSP.setEditor(new JSpinner.DateEditor(lastEditedSP, DD_MM_YYYY));
        Util.addFocusSelectAllAction(lastEditedSP);
        linksListTF.setEditable(false);
        industriesListTF.setEditable(false);
        lastEditorTF.setEnabled(false);
        lastEditedSP.setEnabled(false);

        regionWorldCb.addActionListener(countryCBreloadAction());
        regionWorldCb.setSelectedIndex(0);
        organizePanels(titles, edits, null);

//        JideTabbedPane downTabs = new JideTabbedPane();
        JideTabbedPane downTabs = new JideTabbedPane();

        JScrollPane sp = new JScrollPane(new JTextArea());
        sp.setPreferredSize(new Dimension(400, 150));
        downTabs.add(sp, "Comments");
        downTabs.add(new JLabel("Here will be the company locations list", SwingConstants.CENTER), "Company Locations");
        add(downTabs);
    }

    @Override
    public void loadData() {
        Company comp = (Company) getDbObject();
        if (comp != null) {
            idField.setText(comp.getCompanyId().toString());
            fullCompanyNameTF.setText(comp.getFullName());
            abbreviationTF.setText(comp.getAbbreviation());
            isDummyCB.setSelected(comp.getIsDummy() != null && comp.getIsDummy() == 1);
            turnoverSP.setValue(comp.getTurnover());
            physicAddressTF.setText(comp.getAddress());
            postCodeTF.setText(comp.getPostcode());
            mailingAddressTF.setText(comp.getMailaddress());
            mailingPostCodeTF.setText(comp.getMailpostcode());
            selectComboItem(countryCB, comp.getCountryId());
            mainPhoneTF.setText(comp.getMainPhone());
            mainFaxTF.setText(comp.getMainFax());
            membershipLevelCB.setSelectedIndex(comp.getMemberLevel() != null ? comp.getMemberLevel().intValue() - 1 : 1);
            if (comp.getLasteditDate() != null) {
                Timestamp t = comp.getLasteditDate();
                lastVerifiedDateSP.setValue(new java.util.Date(t.getTime()));
            }
            Integer userID = comp.getLasteditedBy();
            if (userID != null) {
                try {
                    User user = (User) AIBclient.getExchanger().loadDbObjectOnID(User.class, userID);
                    lastEditorTF.setText(user.getInitials());
                } catch (RemoteException ex) {
                    AIBclient.log(ex);
                }
            }
            imageData = (byte[]) comp.getLogo();
            setImage(imageData);
        }
    }

    @Override
    public boolean save() throws Exception {
        java.util.Date dt;
        Company comp = (Company) getDbObject();
        boolean isNew = false;
        if (comp == null) {
            comp = new Company(null);
            comp.setCompanyId(0);
            isNew = true;
        }
        comp.setFullName(fullCompanyNameTF.getText());
        comp.setAbbreviation(abbreviationTF.getText());
        comp.setIsDummy(isDummyCB.isSelected() ? 1 : 0);
        comp.setTurnover((Double) turnoverSP.getValue());
        comp.setAddress(physicAddressTF.getText());
        comp.setPostcode(postCodeTF.getText());
        comp.setMailaddress(mailingAddressTF.getText());
        comp.setMailpostcode(mailingPostCodeTF.getText());
        comp.setCountryId(getSelectedCbItem(countryCB));
        comp.setMainPhone(mainPhoneTF.getText());
        comp.setMainFax(mainFaxTF.getText());
        Integer lvl = new Integer((String) membershipLevelCB.getSelectedItem());
        comp.setMemberLevel(lvl);
        dt = (Date) lastVerifiedDateSP.getValue();
        comp.setVerifyDate(new java.sql.Date(dt.getTime()));
        comp.setLasteditedBy(AIBclient.getCurrentUser().getUserId());
        dt = Calendar.getInstance().getTime();
        comp.setLasteditDate(new java.sql.Timestamp(dt.getTime()));
        comp.setLogo(imageData);

        boolean ok = saveDbRecord(comp, isNew);
        comp = (Company) getDbObject();
        StringTokenizer tok = new StringTokenizer(linksListTF.getText(), ",");
        String link;
//        while (tok.hasMoreTokens()) {
//            AIBclient.saveOrInsertCompanyLink(tok.nextToken());
//        }
//        AIBclient.removeRedundantLinks(comp.getCompanyId(),linksListTF.getText());

        return ok;
    }

    protected String getImagePanelLabel() {
        return "Logo";
    }

    private AbstractAction getLinkListAction(String lbl) {
        if (linkListAction == null) {
            linkListAction = new AbstractAction(lbl) {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    new ListInTextFieldDialog("Links List",
                            new Object[]{linksListTF, AIBclient.loadAllLinks(), "Enter URL here:"
                    });
                }
            };
        }
        return linkListAction;
    }

    private AbstractAction getIndustryListAction(String lbl) {
        if (industryListAction == null) {
            industryListAction = new AbstractAction(lbl) {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    new ListInTextFieldDialog("Industry List",
                            new Object[]{industriesListTF, AIBclient.loadAllIndustries(), "Enter industry here:"
                    });
                }
            };
        }
        return industryListAction;
    }

    private AbstractAction getMentionsListAction(String lbl) {
        if (mentionListAction == null) {
            mentionListAction = new AbstractAction(lbl) {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    new PublicationsListInTextFieldDialog("AIB Mentions / Dates",
                            new Object[]{mentionsListTF, AIBclient.loadAllAIBmentions(), "Enter publication here:"
                    });
                }
            };
        }
        return mentionListAction;
    }

    private void syncCountries() {
        ComboItem wr = (ComboItem) regionWorldCb.getSelectedItem();
        countryCbModel.removeAllElements();
        if (wr != null) {
            for (Object itm : AIBclient.loadRegionCountries(wr.getId())) {
                countryCbModel.addElement(itm);
            }
        }
    }

    private ActionListener countryCBreloadAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                syncCountries();
            }
        };
    }
}
