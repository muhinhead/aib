/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.people;

import com.aib.AIBclient;
import com.aib.EditAreaAction;
import com.aib.EditPanelWithPhoto;
import com.aib.GeneralFrame;
import com.aib.ModalGridDialog;
import static com.aib.RecordEditPanel.DD_MM_YYYY;
import static com.aib.RecordEditPanel.comboPanelWithLookupBtn;
import static com.aib.RecordEditPanel.getBorderPanel;
import static com.aib.RecordEditPanel.getGridPanel;
import static com.aib.RecordEditPanel.selectComboItem;
import com.aib.lookup.CompanyListInTextFieldDialog;
import com.aib.lookup.AIBawardsListInTextFieldDialog;
import com.aib.lookup.ListInTextFieldDialog;
import com.aib.lookup.LocationLookupAction;
import com.aib.lookup.LookupDialog;
import com.aib.lookup.PeopleLookupAction;
import com.aib.lookup.UserLookupAction;
import com.aib.lookup.WorldRegionLookupAction;
import com.aib.orm.People;
import com.aib.orm.Peoplecompany;
import com.aib.orm.User;
import com.aib.orm.dbobject.ComboItem;
import com.aib.orm.dbobject.DbObject;
import com.aib.product.PurchaseInterestGrid;
import com.aib.product.PurchasedProductsGrid;
import com.xlend.util.EmailFocusAdapter;
import com.xlend.util.Java2sAutoComboBox;
import com.xlend.util.SelectedDateSpinner;
import com.xlend.util.Util;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Nick Mukhin
 */
public class EditPeoplePanel extends EditPanelWithPhoto {

    public static EditPeoplePanel instance;
    private JTextField idField;
    private Java2sAutoComboBox titleCB;
    private Java2sAutoComboBox sourceCB;
    private JTextField sourceTF;
    private Java2sAutoComboBox firstNameTF;
    private Java2sAutoComboBox familyNameTF;
    private Java2sAutoComboBox suffixCB;
    private Java2sAutoComboBox greetingCB;
    private JTextField linksListTF;
    private Java2sAutoComboBox jobDisciplineCB;
    private Java2sAutoComboBox departmentCB;
    private JTextField industriesListTF;
    private JTextField specAddressTF;
    private JScrollPane sp1;
    private JTextArea mailingAddressTA;
    private JTextField mailingPostCodeTF;
    private JTextField deskPhoneTF;
    private JTextField deskFaxTF;
    private JTextField mobilePhoneTF;
    private Java2sAutoComboBox mainEmailTF;
    private JTextField alterEmailTF;
    private JTextField paTF;
    private JTextField paPhoneTF;
    private JTextField paEmailTF;
    private JTextField lastEditorTF;
    private SelectedDateSpinner lastEditedSP;
    private JTextField otherContactTF;
    private JTextField aibAwardsListTF;
    private SelectedDateSpinner lastVerifiedDateSP;
//    private JTextArea commentsTA;
    private JComboBox locationCB;
    private JTextField companiesListTF;
    private DefaultComboBoxModel locationCbModel;
    private JCheckBox primaryContactCB;
    private JCheckBox channelSubscriberCB;
    private JCheckBox marketingIntelDistCB;
    private JCheckBox mediaBriefingDistCB;
    private JCheckBox sourceBookCB;
    private DefaultComboBoxModel salesContactCbModel;
    private JComboBox salesContactCB;
    private JTextArea nextActionTA;
    private SelectedDateSpinner actionDateSP;
    private JTextField extUserNameTF;
    private JTextField extPassworTF;
    private SelectedDateSpinner timescaleSP;
    private PeopleCommentsGrid commentsGrid;
    private JCheckBox aibCoordinatorCB;
    private JCheckBox aibJudgeCB;
    private JCheckBox aibEntrantCB;
    private DefaultComboBoxModel regionWorldCbModel;
    private DefaultComboBoxModel countryCbModel;
    private JComboBox regionWorldCb;
    private JComboBox countryCB;

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

    public EditPeoplePanel(DbObject dbObject) {
        super(dbObject);
        setEnabledPictureControl(true);
    }

    @Override
    protected void fillContent() {
        instance = this;
        String titles[] = new String[]{
            "ID:",//"Source:",
            "First Name:", //"Family Name:", 
            "Title:",//"Suffix:", //"Greeting:",
            "Region of World:", //,"Country:"
            "Main email:", // "Alternate email:"
            "Companies:",//"Location:"
            "Links:",// "Job discipline:",
            "Department:", //"Industry:",
            "Specific address:",
            "Mailing address:", //"Mailing post code"
            "Desk phone:", // "Desk fax:", "Mobile phone:"
            "PA:", // "PA phone:", //"PA email:"
            "Other contact info:",// "AIB actions/date:",
            "", "",
            "Purchases:", // "Purchase interest:",
            "Sales contact:",//"Action date:", "External user name:", "External user password:"
            "Last verified" //"Last editor:" //"Last edited:
        };
        ComboItem emptyItem = new ComboItem(0, "");
        locationCbModel = new DefaultComboBoxModel();//AIBclient.loadAllLocations(emptyItem));
        salesContactCbModel = new DefaultComboBoxModel(AIBclient.loadAllUsersInitials());
        regionWorldCbModel = new DefaultComboBoxModel(AIBclient.loadAllRegions());
        countryCbModel = new DefaultComboBoxModel();
        JLabel paEmailLBL;
        JLabel alterEmailLBL;
        JComponent[] edits = new JComponent[]{
            getGridPanel(new JComponent[]{
                getGridPanel(idField = new JTextField(), 5),
                getGridPanel(new JComponent[]{
                    new JLabel("Source:", SwingConstants.RIGHT),
                    sourceCB = new Java2sAutoComboBox(AIBclient.loadDistinctSources())
                })
            }),
            getGridPanel(new JComponent[]{
                firstNameTF = new Java2sAutoComboBox(AIBclient.loadDistinctPeopleData("first_name")),
                new JLabel("Family Name:", SwingConstants.RIGHT),
                familyNameTF = new Java2sAutoComboBox(AIBclient.loadDistinctPeopleData("last_name"))
            }),
            getGridPanel(new JComponent[]{
                titleCB = new Java2sAutoComboBox(AIBclient.loadDistinctTitles()),//JTextField(),
                new JLabel("Suffix:", SwingConstants.RIGHT),
                suffixCB = new Java2sAutoComboBox(AIBclient.loadDistinctSuffixes()),
                new JLabel("Greeting:", SwingConstants.RIGHT),
                greetingCB = new Java2sAutoComboBox(AIBclient.loadDistinctGreetings())
            }),
            getGridPanel(new JComponent[]{
                comboPanelWithLookupBtn(regionWorldCb = new JComboBox(regionWorldCbModel),
                new RegionsLookupAction(regionWorldCb)),
                new JLabel("Country:", SwingConstants.RIGHT),
                countryCB = new JComboBox(countryCbModel)
            }),
            getBorderPanel(new JComponent[]{
                mainEmailTF = new Java2sAutoComboBox(AIBclient.loadDistinctPeopleData("main_email")),
                alterEmailLBL = new JLabel("Alternate email:", SwingConstants.RIGHT),
                alterEmailTF = new JTextField(12)
            }),
            getGridPanel(new JComponent[]{
                getBorderPanel(new JComponent[]{
                    null,
                    companiesListTF = new JTextField(16),
                    new JButton(getCompaniesListAction(new ImageIcon(AIBclient.loadImage("lookup.png", EditPeoplePanel.class))))
                }),
                getBorderPanel(new JComponent[]{
                    new JLabel("Location:", SwingConstants.RIGHT),
                    comboPanelWithLookupBtn(locationCB = new JComboBox(locationCbModel),
                    new LocationLookupAction(locationCB))
                })
            }),
            getGridPanel(new JComponent[]{
                getBorderPanel(new JComponent[]{
                    null,
                    linksListTF = new JTextField(16),
                    new JButton(getLinkListAction(new ImageIcon(AIBclient.loadImage("lookup.png", EditPeoplePanel.class))))
                }),
                getBorderPanel(new JComponent[]{
                    new JLabel("Job title:", SwingConstants.RIGHT),
                    jobDisciplineCB = new Java2sAutoComboBox(AIBclient.loadDistinctJobDisciplines())
                })
            }),
            getGridPanel(new JComponent[]{
                getBorderPanel(new JComponent[]{null,
                    departmentCB = new Java2sAutoComboBox(AIBclient.loadDistinctDepartaments()),
                    new JButton(getHistoryAction("History", new ImageIcon(AIBclient.loadImage("history.png", EditPeoplePanel.class))))}),
                getBorderPanel(new JComponent[]{new JLabel("Industry:", SwingConstants.RIGHT),
                    industriesListTF = new JTextField(16),
                    new JButton(getIndustryListAction(new ImageIcon(AIBclient.loadImage("lookup.png", EditPeoplePanel.class))))})
            }),
            getGridPanel(specAddressTF = new JTextField(), 2),
            getGridPanel(new JComponent[]{
                getBorderPanel(new JComponent[]{
                    null,
                    sp1 = new JScrollPane(mailingAddressTA = new JTextArea(1, 20),
                    JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),
                    new JButton(new EditAreaAction(
                    new ImageIcon(AIBclient.loadImage("lookup.png", EditPeoplePanel.class)), getMailingAddressTA()))
                }),
                //                new JPanel()
                getGridPanel(new JComponent[]{
                    new JLabel("Post Code:", SwingConstants.RIGHT),
                    mailingPostCodeTF = new JTextField()
                })
            }),
            getGridPanel(new JComponent[]{
                getGridPanel(new JComponent[]{
                    deskPhoneTF = new JTextField(6),
                    new JLabel("Desk Fax:", SwingConstants.RIGHT),
                    deskFaxTF = new JTextField(6)
                }),
                getGridPanel(new JComponent[]{
                    new JLabel("Mobile phone:", SwingConstants.RIGHT),
                    mobilePhoneTF = new JTextField(6)
                })
            }),
            getGridPanel(new JComponent[]{
                paTF = new JTextField(12),
                getGridPanel(new JComponent[]{
                    new JLabel("PA phone:", SwingConstants.RIGHT),
                    paPhoneTF = new JTextField(6),
                    paEmailLBL = new JLabel("PA email:", SwingConstants.RIGHT),
                    paEmailTF = new JTextField(6)
                })
            }),
            getGridPanel(new JComponent[]{
                otherContactTF = new JTextField(12),
                getBorderPanel(new JComponent[]{
                    new JLabel("AIB actions:", SwingConstants.RIGHT),
                    aibAwardsListTF = new JTextField(12),
                    new JButton(getAwardsListAction(new ImageIcon(AIBclient.loadImage("lookup.png", EditPeoplePanel.class))))
                })
            }),
            getGridPanel(new JComponent[]{
                primaryContactCB = new JCheckBox("Primary contact"),
                channelSubscriberCB = new JCheckBox("Channel subscriber"),
                marketingIntelDistCB = new JCheckBox("Market Intel dist."),
                mediaBriefingDistCB = new JCheckBox("Industry briefing dist."),
                new JPanel()
            }),
            getGridPanel(new JComponent[]{
                sourceBookCB = new JCheckBox("Sourcebook"),
                aibCoordinatorCB = new JCheckBox("AIBs Co-ordinator"),
                aibJudgeCB = new JCheckBox("AIBs Judge"),
                aibEntrantCB = new JCheckBox("AIBs Entrant"),
                new JPanel()
            }),
            getGridPanel(new JComponent[]{
                new JButton(showPurchasesAction("Purchases / Dates...")),
                new JButton(showPurchaseInterestAction("Purchase Interest...")),
                //                getBorderPanel(new JComponent[]{
                //                    null,
                new JLabel("Purchase timescale:", SwingConstants.RIGHT),
                timescaleSP = new SelectedDateSpinner()
//                    new JButton("ZZZ")
//                }), 
            }),
            getBorderPanel(new JComponent[]{
                comboPanelWithLookupBtn(salesContactCB = new JComboBox(salesContactCbModel), new UserLookupAction(salesContactCB)),
                getBorderPanel(new JComponent[]{
                    new JLabel(" Action date:", SwingConstants.RIGHT),
                    getBorderPanel(new JComponent[]{
                        actionDateSP = new SelectedDateSpinner(),
                        getGridPanel(new JComponent[]{
                            getBorderPanel(new JComponent[]{new JLabel(" Ext.user name:"), extUserNameTF = new JTextField(6)}),
                            getBorderPanel(new JComponent[]{new JLabel("Ext.user passwd:"), extPassworTF = new JTextField(6)})})
                    })
                })
            }),
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
        getMailingAddressTA().setWrapStyleWord(true);
        getMailingAddressTA().setLineWrap(true);
//        nextActionTA.setWrapStyleWord(true);
//        nextActionTA.setLineWrap(true);

//        primaryContactCB.setHorizontalTextPosition(SwingConstants.LEFT);
//        channelSubscriberCB.setHorizontalTextPosition(SwingConstants.LEFT);
//        marketingIntelDistCB.setHorizontalTextPosition(SwingConstants.LEFT);
//        mediaBriefingDistCB.setHorizontalTextPosition(SwingConstants.LEFT);
//        sourceBookCB.setHorizontalTextPosition(SwingConstants.LEFT);
        sp1.setPreferredSize(new Dimension(sp1.getPreferredSize().width, idField.getPreferredSize().height));
        idField.setEnabled(false);
        getMailingAddressTA().setEditable(false);

        for (Java2sAutoComboBox cb : new Java2sAutoComboBox[]{
            jobDisciplineCB, sourceCB, departmentCB, titleCB, greetingCB, suffixCB}) {
            cb.setEditable(true);
            cb.setStrict(false);
        }

        for (SelectedDateSpinner sp : new SelectedDateSpinner[]{lastEditedSP, lastVerifiedDateSP, actionDateSP}) {
            sp.setEditor(new JSpinner.DateEditor(sp, DD_MM_YYYY));
            Util.addFocusSelectAllAction(sp);
        }
        timescaleSP.setEditor(new JSpinner.DateEditor(timescaleSP, MMM_YYYY));
        Util.addFocusSelectAllAction(timescaleSP);

        linksListTF.setEditable(false);
        industriesListTF.setEditable(false);
        companiesListTF.setEditable(false);
        aibAwardsListTF.setEditable(false);
        lastEditorTF.setEnabled(false);
        lastEditedSP.setEnabled(false);
        timescaleSP.setEnabled(false);
        regionWorldCb.addActionListener(countryCBreloadAction());
        try {
            regionWorldCb.setSelectedIndex(0);
        } catch (Exception e) {
        }
        organizePanels(titles, edits, null);

        JPanel downTabs = new JPanel(new GridLayout(1, 2, 5, 5));
        try {
            commentsGrid = new PeopleCommentsGrid(AIBclient.getExchanger(), null, this);
            commentsGrid.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Comments"));
            downTabs.add(commentsGrid);
        } catch (RemoteException ex) {
            downTabs.add(new JLabel("Comments loading failed", SwingConstants.CENTER));
            AIBclient.logAndShowMessage(ex);
        }

        if (EditPeopleDialog.locationID != null) {
            selectComboItem(locationCB, EditPeopleDialog.locationID);
//            locationCB.setEnabled(false);
        }

        JScrollPane spNextActions = new JScrollPane(nextActionTA = new JTextArea(),
                JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        spNextActions.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Next Action"));
        downTabs.add(spNextActions);

        downTabs.setPreferredSize(new Dimension(downTabs.getPreferredSize().width, 150));
        add(downTabs);

        titleCB.setPreferredSize(new Dimension(titleCB.getPreferredSize().width, idField.getPreferredSize().height));
        linksListTF.setPreferredSize(new Dimension(linksListTF.getPreferredSize().width, idField.getPreferredSize().height));
        jobDisciplineCB.setPreferredSize(new Dimension(jobDisciplineCB.getPreferredSize().width, idField.getPreferredSize().height));
        departmentCB.setPreferredSize(new Dimension(departmentCB.getPreferredSize().width, idField.getPreferredSize().height));
        suffixCB.setPreferredSize(new Dimension(suffixCB.getPreferredSize().width, idField.getPreferredSize().height));
        greetingCB.setPreferredSize(new Dimension(greetingCB.getPreferredSize().width, idField.getPreferredSize().height));

        paEmailTF.addFocusListener(new EmailFocusAdapter(paEmailLBL, paEmailTF));
        alterEmailTF.addFocusListener(new EmailFocusAdapter(alterEmailLBL, alterEmailTF));
        mainEmailTF.addFocusListener(new EmailFocusAdapter(labels[10], (JTextComponent) (mainEmailTF.getEditor().getEditorComponent())));

        familyNameTF.setEditable(true);
        familyNameTF.setStrict(false);
        familyNameTF.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
//                People people = AIBclient.getPeopleOnValue("main_email", mainEmailTF.getSelectedItem().toString());
//                reload(people);
                String familyName = familyNameTF.getSelectedItem().toString();
                if (familyName.trim().length() > 0) {
                    People people = AIBclient.getPeopleOnValue("last_name", familyNameTF.getSelectedItem().toString());
                    if (people != null && (getDbObject() == null || people.getPeopleId().intValue() != getDbObject().getPK_ID().intValue())) {
                        new PeopleLookupAction(familyNameTF, "p.last_name", familyNameTF.getSelectedItem().toString());
                        if (LookupDialog.getChoosed() != null) {
                            try {
                                people = (People) AIBclient.getExchanger().loadDbObjectOnID(People.class,
                                        LookupDialog.getChoosed().intValue());
                                reload(people);
                            } catch (RemoteException ex) {
                                AIBclient.logAndShowMessage(ex);
                            }
                        }
                    }
                }
            }
        });
        firstNameTF.setEditable(true);
        firstNameTF.setStrict(false);
        mainEmailTF.setEditable(true);
        mainEmailTF.setStrict(false);
        mainEmailTF.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                People people = AIBclient.getPeopleOnValue("main_email", mainEmailTF.getSelectedItem().toString());
                if (people != null && (getDbObject() == null || people.getPeopleId().intValue() != getDbObject().getPK_ID().intValue())) {
                    new PeopleLookupAction(mainEmailTF, "p.main_email", mainEmailTF.getSelectedItem().toString());
                    if (LookupDialog.getChoosed() != null) {
                        try {
                            people = (People) AIBclient.getExchanger().loadDbObjectOnID(People.class,
                                    LookupDialog.getChoosed().intValue());
                            reload(people);
                        } catch (RemoteException ex) {
                            AIBclient.logAndShowMessage(ex);
                        }
                    }
                }
            }
        });
    }

    private ActionListener countryCBreloadAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                syncCountries();
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

    private void reload(People people) {
        if (people != getDbObject()) {
            getOwnerDialog().setTitle("Edit Person");
            setDbObject(people);
            loadData();
            commentsGrid.refresh(people.getPeopleId());
        }
    }

    @Override
    public void loadData() {
        People person = (People) getDbObject();
        if (person != null) {
            idField.setText(person.getPeopleId().toString());
            sourceCB.setSelectedItem(person.getSource());
            titleCB.setSelectedItem(person.getTitle());
            firstNameTF.setSelectedItem(person.getFirstName());
            familyNameTF.setSelectedItem(person.getLastName());
            suffixCB.setSelectedItem(person.getSuffix());
            greetingCB.setSelectedItem(person.getGreeting());
            jobDisciplineCB.setSelectedItem(person.getJobDiscip());
            selectComboItem(regionWorldCb, AIBclient.getRegionOnCountry(person.getCountryId()));
            selectComboItem(countryCB, person.getCountryId());
            selectComboItem(salesContactCB, person.getSalesContactId());
            departmentCB.setSelectedItem(person.getDepartment());
            linksListTF.setText(AIBclient.getLinkListOnPeopleID(person.getPeopleId()));
            industriesListTF.setText(AIBclient.getIndustryListOnPeopleID(person.getPeopleId()));
            aibAwardsListTF.setText(AIBclient.getAwardsOnPeopleID(person.getPeopleId()));
            String compList;
            companiesListTF.setText(compList = AIBclient.getCompaniesOnPeopleID(person.getPeopleId()));

            locationCB.setModel(locationCbModel = AIBclient.loadAllLocations());
                    //= AIBclient.loadLocationsForCompanies(compList,
                    //        AIBclient.getLocationForCombo(person.getLocationId())));
            selectComboItem(locationCB, person.getLocationId());
            getSpecAddressTF().setText(person.getSpecAddress());
            getMailingAddressTA().setText(person.getMailaddress());
            getMailingPostCodeTF().setText(person.getMailpostcode());
            getDeskPhoneTF().setText(person.getDeskPhone());
            getDeskFaxTF().setText(person.getDeskFax());
            mobilePhoneTF.setText(person.getMobilePhone());
            mainEmailTF.setSelectedItem(person.getMainEmail());
            alterEmailTF.setText(person.getAlterEmail());
            paTF.setText(person.getPa());
            paPhoneTF.setText(person.getPaPhone());
            paEmailTF.setText(person.getPaEmail());
            primaryContactCB.setSelected(person.getIsPrimary() != null && person.getIsPrimary() == 1);
            channelSubscriberCB.setSelected(person.getIsSubscriber() != null && person.getIsSubscriber() == 1);
            marketingIntelDistCB.setSelected(person.getIsMarketintl() != null && person.getIsMarketintl() == 1);
            mediaBriefingDistCB.setSelected(person.getIsMediabrief() != null && person.getIsMediabrief() == 1);
            sourceBookCB.setSelected(person.getIsInsourcebook() != null && person.getIsInsourcebook() == 1);
            aibCoordinatorCB.setSelected(person.getIsAibCoordinator() != null && person.getIsAibCoordinator() == 1);
            aibJudgeCB.setSelected(person.getIsAibJudge() != null && person.getIsAibJudge() == 1);
            aibEntrantCB.setSelected(person.getIsAibEntrant() != null && person.getIsAibEntrant() == 1);
            nextActionTA.setText(person.getNextAction());
            extUserNameTF.setText(person.getExternalUser());
            extPassworTF.setText(person.getExternalPasswd());
            timescaleSP.setValue(AIBclient.getNearestPurchaseTimeScale(person.getPeopleId(), timescaleSP));
            if (person.getActionDate() != null) {
                actionDateSP.setValue(new java.util.Date(person.getActionDate().getTime()));
            }
            if (person.getLasteditDate() != null) {
                Timestamp t = person.getLasteditDate();
                lastEditedSP.setValue(new java.util.Date(t.getTime()));
            }
            Integer userID = person.getLasteditedBy();
            if (userID != null) {
                try {
                    User user = (User) AIBclient.getExchanger().loadDbObjectOnID(User.class, userID);
                    lastEditorTF.setText(user.getInitials());
                } catch (RemoteException ex) {
                    AIBclient.log(ex);
                }
            }
            commentsGrid.setPeopleID(person.getPeopleId());//updateContent(person.getPeopleId());
            imageData = (byte[]) person.getPhoto();
            setImage(imageData);
        }
    }

    @Override
    public boolean save() throws Exception {
        java.util.Date dt;
        People person = (People) getDbObject();
        boolean isNew = false;
        if (person == null) {
            person = new People(null);
            person.setPeopleId(0);
            isNew = true;
        }
        person.setTitle((String) titleCB.getSelectedItem());
        person.setSource((String) sourceCB.getSelectedItem());
        person.setFirstName(firstNameTF.getSelectedItem().toString());
        person.setLastName(familyNameTF.getSelectedItem().toString());
        person.setLocationId(getSelectedCbItem(locationCB));
        person.setCountryId(getSelectedCbItem(countryCB));
        person.setSuffix((String) suffixCB.getSelectedItem());
        person.setGreeting((String) greetingCB.getSelectedItem());
        person.setJobDiscip((String) jobDisciplineCB.getSelectedItem());
        person.setDepartment((String) departmentCB.getSelectedItem());
        person.setDeskPhone(getDeskPhoneTF().getText());
        person.setDeskFax(getDeskFaxTF().getText());
        person.setMobilePhone(mobilePhoneTF.getText());
        person.setMainEmail((String) mainEmailTF.getSelectedItem());
        person.setMailaddress(getMailingAddressTA().getText());
        person.setMailpostcode(getMailingPostCodeTF().getText());
        person.setAlterEmail(alterEmailTF.getText());
        person.setPa(paTF.getText());
        person.setPaEmail(paEmailTF.getText());
        person.setPaPhone(paPhoneTF.getText());
        person.setIsPrimary(primaryContactCB.isSelected() ? 1 : 0);
        person.setIsSubscriber(channelSubscriberCB.isSelected() ? 1 : 0);
        person.setIsMarketintl(marketingIntelDistCB.isSelected() ? 1 : 0);
        person.setIsMediabrief(mediaBriefingDistCB.isSelected() ? 1 : 0);
        person.setIsInsourcebook(sourceBookCB.isSelected() ? 1 : 0);
        person.setIsAibCoordinator(aibCoordinatorCB.isSelected() ? 1 : 0);
        person.setIsAibJudge(aibJudgeCB.isSelected() ? 1 : 0);
        person.setIsAibEntrant(aibEntrantCB.isSelected() ? 1 : 0);

        person.setLasteditedBy(AIBclient.getCurrentUser().getUserId());
        dt = Calendar.getInstance().getTime();
        person.setLasteditDate(new java.sql.Timestamp(dt.getTime()));
        dt = (Date) lastVerifiedDateSP.getValue();
        person.setVerifyDate(new java.sql.Date(dt.getTime()));
        dt = (Date) actionDateSP.getValue();
        person.setActionDate(new java.sql.Date(dt.getTime()));
//        person.setComments(commentsTA.getText());
        person.setNextAction(nextActionTA.getText());
        person.setSalesContactId(getSelectedCbItem(salesContactCB));
        person.setExternalUser(extUserNameTF.getText());
        person.setExternalPasswd(extPassworTF.getText());
        person.setPhoto(imageData);

        boolean ok = saveDbRecord(person, isNew);
        if (ok) {
            person = (People) getDbObject();
            StringTokenizer tok = new StringTokenizer(linksListTF.getText(), ",");
            while (tok.hasMoreTokens()) {
                AIBclient.saveOrInsertPeopleLink(person.getPeopleId(), tok.nextToken().trim());
            }
            AIBclient.removeRedundantPeopleLinks(person.getPeopleId(), linksListTF.getText());

            tok = new StringTokenizer(industriesListTF.getText(), ",");
            while (tok.hasMoreTokens()) {
                AIBclient.saveOrInsertPeopleIndustry(person.getPeopleId(), tok.nextToken().trim());
            }
            AIBclient.removeRedundantPeopleIndustries(person.getPeopleId(), industriesListTF.getText());

            tok = new StringTokenizer(aibAwardsListTF.getText(), ",");
            while (tok.hasMoreTokens()) {
                AIBclient.savePeopleAward(person.getPeopleId(), tok.nextToken().trim());
            }
            AIBclient.removeRedundantAwards(person.getPeopleId(), aibAwardsListTF.getText());

            tok = new StringTokenizer(companiesListTF.getText(), ",");
            while (tok.hasMoreTokens()) {
                AIBclient.savePeopleCompany(person.getPeopleId(), tok.nextToken().trim());
            }
            AIBclient.removeRedundantPeopleCompany(person.getPeopleId(), companiesListTF.getText());
            if (PeopleGrid.parentComapnyID != null) {
                DbObject[] obs = AIBclient.getExchanger().getDbObjects(Peoplecompany.class, 
                        "company_id="+PeopleGrid.parentComapnyID+" and people_id=" + person.getPK_ID(), null);
                if (obs.length == 0) {
                    Peoplecompany pk = new Peoplecompany(null);
                    pk.setPK_ID(0);
                    pk.setPeopleId(person.getPK_ID());
                    pk.setCompanyId(PeopleGrid.parentComapnyID);
                    pk.setNew(true);
                    AIBclient.getExchanger().saveDbObject(pk);
                }
            }
        }
        return ok;
    }

    private AbstractAction getLinkListAction(ImageIcon img) {
        return new AbstractAction(null, img) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                new ListInTextFieldDialog("Links List", new Object[]{linksListTF.getText(), AIBclient.loadAllLinks(), "Enter URL here:"});
                linksListTF.setText(ListInTextFieldDialog.getResultList());
            }
        };
    }

    private AbstractAction getIndustryListAction(ImageIcon img) {
        return new AbstractAction(null, img) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                new ListInTextFieldDialog("Industry List",
                        new Object[]{industriesListTF.getText(), AIBclient.loadAllIndustries(),
                            "Enter industry here:"});
                industriesListTF.setText(ListInTextFieldDialog.getResultList());
            }
        };
    }

    private AbstractAction getHistoryAction(String lbl, ImageIcon img) {
        return new AbstractAction(lbl, img) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                People person = (People) getDbObject();
                new DepartmentsHistoryDialog(person == null || person.getPeopleId() == null ? 0 : person.getPeopleId());
            }
        };
    }

    private AbstractAction getAwardsListAction(ImageIcon img) {
        return new AbstractAction(null, img) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                new AIBawardsListInTextFieldDialog("AIB actions / Dates",
                        new Object[]{aibAwardsListTF.getText(), AIBclient.loadAllAwards(),
                            "Enter action here:"});
                aibAwardsListTF.setText(AIBawardsListInTextFieldDialog.getResultList());
            }
        };
    }

    private AbstractAction getCompaniesListAction(ImageIcon img) {
        return new AbstractAction(null, img) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                new CompanyListInTextFieldDialog("Companies List",
                        new Object[]{companiesListTF.getText(), AIBclient.loadAllCompaniesNames(),
                            "Company name:"});
                String compList = ListInTextFieldDialog.getResultList();
                companiesListTF.setText(compList);
                locationCB.setModel(locationCbModel
                        = AIBclient.loadLocationsForCompanies(compList, (ComboItem) locationCB.getSelectedItem()));//new ComboItem(0, "")));
                if (getDbObject() != null) {
                    People person = (People) getDbObject();
                    selectComboItem(locationCB, person.getLocationId());
                }
            }
        };
    }

    private AbstractAction showPurchasesAction(String label) {
        return new AbstractAction(label) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Integer peopleID = getPeopleID();
                if (peopleID != null) {
                    try {
                        new ModalGridDialog("Purchases", new PurchasedProductsGrid(AIBclient.getExchanger(), peopleID));
                    } catch (RemoteException ex) {
                        AIBclient.logAndShowMessage(ex);
                    }
                }
            }
        };
    }

    private AbstractAction showPurchaseInterestAction(String label) {
        return new AbstractAction(label) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Integer peopleID = getPeopleID();
                if (peopleID != null) {
                    try {
                        new ModalGridDialog("Purchase Interest", new PurchaseInterestGrid(AIBclient.getExchanger(), peopleID));
                        AIBclient.getNearestPurchaseTimeScale(peopleID, timescaleSP);
                    } catch (RemoteException ex) {
                        AIBclient.logAndShowMessage(ex);
                    }
                }
            }
        };
    }

    private Integer getPeopleID() {
        Integer peopleID = getDbObject() == null ? null : ((People) getDbObject()).getPeopleId();
        if (getDbObject() == null && GeneralFrame.yesNo("Attention!",
                "Do you want to save this person's record before adding dependant info?") == JOptionPane.YES_OPTION) {
            try {
                if (save()) {
                    peopleID = ((People) getDbObject()).getPeopleId();
                }
            } catch (Exception ex) {
                peopleID = null;
                AIBclient.logAndShowMessage(ex);
            }
        }
        return peopleID;
    }

    /**
     * @return the specAddressTF
     */
    public JTextField getSpecAddressTF() {
        return specAddressTF;
    }

    /**
     * @return the mailingAddressTA
     */
    public JTextArea getMailingAddressTA() {
        return mailingAddressTA;
    }

    /**
     * @return the mailingPostCodeTF
     */
    public JTextField getMailingPostCodeTF() {
        return mailingPostCodeTF;
    }

    /**
     * @return the deskPhoneTF
     */
    public JTextField getDeskPhoneTF() {
        return deskPhoneTF;
    }

    /**
     * @return the deskFaxTF
     */
    public JTextField getDeskFaxTF() {
        return deskFaxTF;
    }
}
