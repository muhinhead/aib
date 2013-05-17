/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.location;

import com.aib.AIBclient;
import com.aib.EditAreaAction;
import com.aib.EditPanelWithPhoto;
import com.aib.RecordEditPanel;
import static com.aib.RecordEditPanel.getBorderPanel;
import static com.aib.RecordEditPanel.getGridPanel;
import static com.aib.RecordEditPanel.selectComboItem;
import com.aib.lookup.CompanyLookupAction;
import com.aib.lookup.ListInTextFieldDialog;
import com.aib.lookup.WorldRegionLookupAction;
import com.aib.orm.Location;
import com.aib.orm.User;
import com.aib.orm.dbobject.ComboItem;
import com.aib.orm.dbobject.DbObject;
import com.jidesoft.swing.JideTabbedPane;
import com.xlend.util.SelectedDateSpinner;
import com.xlend.util.Util;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.StringTokenizer;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
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
class EditLocationPanel extends EditPanelWithPhoto {

    private JTextField idField;
    private DefaultComboBoxModel regionWorldCbModel;
    private DefaultComboBoxModel countryCbModel;
    private DefaultComboBoxModel companyCbModel;
    private JComboBox companyCB;
    private JTextField locationNameTF;
    private JTextField abbreviationTF;
    private JScrollPane sp1;
    private JTextArea physicAddressTA;
    private JTextField postCodeTF;
    private JScrollPane sp2;
    private JTextArea mailingAddressTA;
    private JTextField mailingPostCodeTF;
    private JComboBox regionWorldCb;
    private JComboBox countryCB;
    private JTextField mainPhoneTF;
    private JTextField mainFaxTF;
    private JTextField lastEditorTF;
    private SelectedDateSpinner lastEditedSP;
    private CompanyLookupAction compLookupAction;
    private JTextArea commentsTA;
    private JTextField linksListTF;
    private JTextField industriesListTF;

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

    public EditLocationPanel(DbObject dbObject) {
        super(dbObject);
    }

    @Override
    protected String getImagePanelLabel() {
        return "Logo";
    }

    @Override
    protected void fillContent() {
        String titles[] = new String[]{
            "ID:",
            "Company:",
            "Location Name:", //"Abbreviation:"
            "Links:",
            "Industry:",
            "Physical Address:",//            "Post Code:",
            "Mailing Address:",//            "Mailing Post Code:",
            "Region of World:", //,"Country:"
            "Main Phone:", //"Main Fax;"
            "Last editor:" //"Last edited:
        };
        companyCbModel = new DefaultComboBoxModel();
        regionWorldCbModel = new DefaultComboBoxModel();
        countryCbModel = new DefaultComboBoxModel();
        for (ComboItem ci : AIBclient.loadAllCompanies()) {
            companyCbModel.addElement(ci);
        }
        for (ComboItem ci : AIBclient.loadAllRegions()) {
            regionWorldCbModel.addElement(ci);
        }
        JComponent[] edits = new JComponent[]{
            getGridPanel(idField = new JTextField(), 6),
            comboPanelWithLookupBtn(companyCB = new JComboBox(companyCbModel), compLookupAction = new CompanyLookupAction(companyCB)),
            getGridPanel(new JComponent[]{
                locationNameTF = new JTextField(),
                getGridPanel(new JComponent[]{
                    new JLabel("Abbreviation:", SwingConstants.RIGHT),
                    abbreviationTF = new JTextField()
                })
            }),
            getBorderPanel(new JComponent[]{null, linksListTF = new JTextField(),
                new JButton(getLinkListAction("..."))}),
            getBorderPanel(new JComponent[]{null, industriesListTF = new JTextField(),
                new JButton(getIndustryListAction("..."))}),
            
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
            getGridPanel(new JComponent[]{
                getBorderPanel(new JComponent[]{
                    lastEditorTF = new JTextField(2),
                    new JLabel("Last edited:", SwingConstants.RIGHT),
                    lastEditedSP = new SelectedDateSpinner()
                })
            })
        };
        sp1.setPreferredSize(new Dimension(sp1.getPreferredSize().width, idField.getPreferredSize().height));
        sp2.setPreferredSize(sp1.getPreferredSize());
        idField.setEnabled(false);

        companyCB.setEnabled(EditLocationDialog.companyID == null);
        if (EditLocationDialog.companyID != null) {
            selectComboItem(companyCB, EditLocationDialog.companyID);
        }
        compLookupAction.setEnabled(EditLocationDialog.companyID == null);

        lastEditedSP.setEditor(new JSpinner.DateEditor(lastEditedSP, DD_MM_YYYY));
        Util.addFocusSelectAllAction(lastEditedSP);

        physicAddressTA.setEditable(false);
        mailingAddressTA.setEditable(false);

        lastEditorTF.setEnabled(false);
        lastEditedSP.setEnabled(false);

        regionWorldCb.addActionListener(countryCBreloadAction());
        regionWorldCb.setSelectedIndex(0);
        organizePanels(titles, edits, null);
        JideTabbedPane downTabs = new JideTabbedPane();

        JScrollPane sp = new JScrollPane(commentsTA = new JTextArea());
        sp.setPreferredSize(new Dimension(400, 150));
        downTabs.add(sp, "Comments");
        add(downTabs, BorderLayout.SOUTH);
    }

    @Override
    public void loadData() {
        Location loc = (Location) getDbObject();
        if (loc != null) {
            idField.setText(loc.getLocationId().toString());
            locationNameTF.setText(loc.getName());
            abbreviationTF.setText(loc.getAbbreviation());
            linksListTF.setText(AIBclient.getLinkListOnLocationID(loc.getLocationId()));
            industriesListTF.setText(AIBclient.getIndustryListOnLocationID(loc.getLocationId()));
            physicAddressTA.setText(loc.getAddress());
            postCodeTF.setText(loc.getPostcode());
            mailingAddressTA.setText(loc.getMailaddress());
            mailingPostCodeTF.setText(loc.getMailpostcode());
            commentsTA.setText(loc.getComments());
            selectComboItem(companyCB, loc.getCompanyId());
            selectComboItem(countryCB, loc.getCountryId());
            selectComboItem(regionWorldCb, AIBclient.getRegionOnCountry(loc.getCountryId()));
            mainPhoneTF.setText(loc.getMainPhone());
            mainFaxTF.setText(loc.getMainFax());
            lastEditedSP.setValue(new java.util.Date(loc.getLasteditDate().getTime()));
            Integer userID = loc.getLasteditedBy();
            if (userID != null) {
                try {
                    User user = (User) AIBclient.getExchanger().loadDbObjectOnID(User.class, userID);
                    lastEditorTF.setText(user.getInitials());
                } catch (RemoteException ex) {
                    AIBclient.log(ex);
                }
            }
            imageData = (byte[]) loc.getLogo();
            setImage(imageData);
        }
    }

    @Override
    public boolean save() throws Exception {
        java.util.Date dt;
        Location loc = (Location) getDbObject();
        boolean isNew = false;
        if (loc == null) {
            loc = new Location(null);
            loc.setLocationId(0);
            isNew = true;
        }
        loc.setName(locationNameTF.getText());
        loc.setAbbreviation(abbreviationTF.getText());
        loc.setAddress(physicAddressTA.getText());
        loc.setPostcode(postCodeTF.getText());
        loc.setMailaddress(mailingAddressTA.getText());
        loc.setMailpostcode(mailingPostCodeTF.getText());
        loc.setComments(commentsTA.getText());
        loc.setCountryId(getSelectedCbItem(countryCB));
        loc.setCompanyId(getSelectedCbItem(companyCB));
        loc.setMainPhone(mainPhoneTF.getText());
        loc.setMainFax(mainFaxTF.getText());
        dt = (Date) lastEditedSP.getValue();
        loc.setLasteditDate(new java.sql.Timestamp(dt.getTime()));
        loc.setLasteditedBy(AIBclient.getCurrentUser().getUserId());
        loc.setLogo(imageData);
        boolean ok = saveDbRecord(loc, isNew);
        loc = (Location) getDbObject();
        
        StringTokenizer tok = new StringTokenizer(linksListTF.getText(), ",");
        while (tok.hasMoreTokens()) {
            AIBclient.saveOrInsertLocationLink(loc.getLocationId(), tok.nextToken());
        }
        AIBclient.removeRedundantLocationLinks(loc.getLocationId(), linksListTF.getText());

        tok = new StringTokenizer(industriesListTF.getText(), ",");
        while (tok.hasMoreTokens()) {
            AIBclient.saveOrInsertLocationIndustry(loc.getLocationId(), tok.nextToken());
        }
        AIBclient.removeRedundantLocationIndustries(loc.getLocationId(), industriesListTF.getText());

        return ok;
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
}
