/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.company;

import com.aib.AIBclient;
import com.aib.EditAreaAction;
//import com.aib.EditAreaAction;
import com.aib.EditPanelWithPhoto;
import com.aib.location.LocationsGrid;
import com.aib.lookup.ListInTextFieldDialog;
import static com.aib.RecordEditPanel.getBorderPanel;
import static com.aib.RecordEditPanel.getGridPanel;
import com.aib.location.CompLocationsGrid;
import com.aib.lookup.PublicationsListInTextFieldDialog;
import com.aib.lookup.WorldRegionLookupAction;
import com.aib.orm.Company;
import com.aib.orm.User;
import com.aib.orm.dbobject.ComboItem;
import com.aib.orm.dbobject.DbObject;
import com.jidesoft.swing.JideTabbedPane;
import com.xlend.util.SelectedDateSpinner;
import com.xlend.util.SelectedNumberSpinner;
import com.xlend.util.Util;
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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
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
    private JTextArea physicAddressTA;
    private JTextField postCodeTF;
    private JTextArea mailingAddressTA;
    private JComboBox regionWorldCb;
    private JComboBox countryCB;
    private JTextField abbreviationTF;
    private JTextField linksListTF;
    private JTextField industriesListTF;
    private JTextField mailingPostCodeTF;
    private JTextField mainPhoneTF;
    private JTextField mainFaxTF;
    private JComboBox membershipLevelCB;
    private JTextField mentionsListTF;
    private JSpinner lastVerifiedDateSP;
    private JTextField lastEditorTF;
    private SelectedDateSpinner lastEditedSP;
    private JTextArea commentsTA;

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
            "Full Company Name:",//           "Abbreviation:", 
            "Dummy Company:",
            "Links:",
            "Industry:",
            "Turnover/Year:",
            "Physical Address:",//            "Post Code:",
            "Mailing Address:",//            "Mailing Post Code:",
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
        JScrollPane sp1;
        JScrollPane sp2;
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
            getGridPanel(new JComponent[]{
                getBorderPanel(new JComponent[]{
                    null,
                    sp1 = new JScrollPane(physicAddressTA = new JTextArea(1, 20),
                    JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),
                    new JButton(new EditAreaAction("...", physicAddressTA))
                }),
                getGridPanel(new JComponent[]{
                    new JLabel("Post Code:", SwingConstants.RIGHT),
                    postCodeTF = new JTextField()
                })
            }),
            getGridPanel(new JComponent[]{
                getBorderPanel(new JComponent[]{
                    null,
                    sp2 = new JScrollPane(mailingAddressTA = new JTextArea(1, 20),
                    JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),
                    new JButton(new EditAreaAction("...", mailingAddressTA))
                }),
                getGridPanel(new JComponent[]{
                    new JLabel("Mailing Post Code:", SwingConstants.RIGHT),
                    mailingPostCodeTF = new JTextField()
                })
            }),
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
        sp1.setPreferredSize(new Dimension(sp1.getPreferredSize().width, idField.getPreferredSize().height));
        sp2.setPreferredSize(sp1.getPreferredSize());
        idField.setEnabled(false);
        lastVerifiedDateSP.setEditor(new JSpinner.DateEditor(lastVerifiedDateSP, DD_MM_YYYY));
        Util.addFocusSelectAllAction(lastVerifiedDateSP);
        lastEditedSP.setEditor(new JSpinner.DateEditor(lastEditedSP, DD_MM_YYYY));
        Util.addFocusSelectAllAction(lastEditedSP);
        linksListTF.setEditable(false);
        physicAddressTA.setEditable(false);
        mailingAddressTA.setEditable(false);
        industriesListTF.setEditable(false);
        mentionsListTF.setEditable(false);
        lastEditorTF.setEnabled(false);
        lastEditedSP.setEnabled(false);

        regionWorldCb.addActionListener(countryCBreloadAction());
        regionWorldCb.setSelectedIndex(0);
        organizePanels(titles, edits, null);

        JideTabbedPane downTabs = new JideTabbedPane();

        JScrollPane sp = new JScrollPane(commentsTA = new JTextArea());
        sp.setPreferredSize(new Dimension(400, 150));
        downTabs.add(sp, "Comments");
        try {
            Company comp = (Company) getDbObject();
            Integer compID = comp == null ? new Integer(0) : comp.getCompanyId();
            downTabs.add(new CompLocationsGrid(AIBclient.getExchanger(), compID), "Company Locations");
        } catch (RemoteException ex) {
            AIBclient.logAndShowMessage(ex);
        }
        downTabs.setPreferredSize(new Dimension(downTabs.getPreferredSize().width, 200));
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
            linksListTF.setText(AIBclient.getLinkListOnCompanyID(comp.getCompanyId()));
            industriesListTF.setText(AIBclient.getIndustryListOnCompanyID(comp.getCompanyId()));
            mentionsListTF.setText(AIBclient.getPublicationsOnCompanyID(comp.getCompanyId()));
            turnoverSP.setValue(comp.getTurnover());
            physicAddressTA.setText(comp.getAddress());
            physicAddressTA.setCaretPosition(0);
            postCodeTF.setText(comp.getPostcode());
            mailingAddressTA.setText(comp.getMailaddress());
            mailingAddressTA.setCaretPosition(0);
            mailingPostCodeTF.setText(comp.getMailpostcode());
            selectComboItem(regionWorldCb, AIBclient.getRegionOnCountry(comp.getCountryId()));
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
            commentsTA.setText(comp.getComments());
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
        comp.setAddress(physicAddressTA.getText());
        comp.setPostcode(postCodeTF.getText());
        comp.setMailaddress(mailingAddressTA.getText());
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
        comp.setComments(commentsTA.getText());

        boolean ok = saveDbRecord(comp, isNew);
        if (ok) {
            comp = (Company) getDbObject();
            StringTokenizer tok = new StringTokenizer(linksListTF.getText(), ",");
            while (tok.hasMoreTokens()) {
                AIBclient.saveOrInsertCompanyLink(comp.getCompanyId(), tok.nextToken());
            }
            AIBclient.removeRedundantCompanyLinks(comp.getCompanyId(), linksListTF.getText());

            tok = new StringTokenizer(industriesListTF.getText(), ",");
            while (tok.hasMoreTokens()) {
                AIBclient.saveOrInsertCompanyIndustry(comp.getCompanyId(), tok.nextToken());
            }
            AIBclient.removeRedundantCompanyIndustries(comp.getCompanyId(), industriesListTF.getText());

            tok = new StringTokenizer(mentionsListTF.getText(), ",");
            while (tok.hasMoreTokens()) {
                AIBclient.saveCompanyPublication(comp.getCompanyId(), tok.nextToken());
            }
            AIBclient.removeRedundantPublications(comp.getCompanyId(), mentionsListTF.getText());
        }
        return ok;
    }

    @Override
    protected String getImagePanelLabel() {
        return "Logo";
    }

    private AbstractAction getLinkListAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                new ListInTextFieldDialog("Links List",
                        new Object[]{linksListTF.getText(), AIBclient.loadAllLinks(), "Enter URL here:"});
                linksListTF.setText(ListInTextFieldDialog.getResultList());
            }
        };
    }

    private AbstractAction getIndustryListAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                new ListInTextFieldDialog("Industry List",
                        new Object[]{industriesListTF.getText(), AIBclient.loadAllIndustries(),
                    "Enter industry here:"});
                industriesListTF.setText(ListInTextFieldDialog.getResultList());
            }
        };
    }

    private AbstractAction getMentionsListAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                new PublicationsListInTextFieldDialog("AIB Mentions / Dates",
                        new Object[]{mentionsListTF.getText(), AIBclient.loadAllAIBmentions(),
                    "Enter publication here:"});
                mentionsListTF.setText(PublicationsListInTextFieldDialog.getResultList());
            }
        };
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
