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
import static com.aib.RecordEditPanel.getBorderPanel;
import static com.aib.RecordEditPanel.getGridPanel;
import com.aib.lookup.CompanyListInTextFieldDialog;
import com.aib.lookup.AIBawardsListInTextFieldDialog;
import com.aib.lookup.CompanyLookupAction;
import com.aib.lookup.ListInTextFieldDialog;
import com.aib.lookup.LocationLookupAction;
import com.aib.lookup.UserLookupAction;
import com.aib.orm.People;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Nick Mukhin
 */
class EditPeoplePanel extends EditPanelWithPhoto {
    
    private JTextField idField;
    private Java2sAutoComboBox titleCB;
    private JTextField firstNameTF;
    private JTextField familyName;
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
    private JTextField mainEmailTF;
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
    
    public EditPeoplePanel(DbObject dbObject) {
        super(dbObject);
    }
    
    @Override
    protected void fillContent() {
        String titles[] = new String[]{
            "ID:",//"Title:",
            "First Name:", //"Family Name:", 
            "Title:",//"Suffix:", //"Greeting:",
            "Companies:",//"Location:"
            "Links:",// "Job discipline:",
            "Department:", //"Industry:",
            "Specific address:",
            "Mailing address:", //"Mailing post code"
            "Desk phone:", // "Desk fax:", "Mobile phone:"
            "Main email:", // "Alternate email:"
            "PA:", // "PA phone:", //"PA email:"
            "Other contact info:",// "AIB actions/date:",
            "",
            "Purchases:", // "Purchase interest:",
            "Sales contact:",//"Action date:", "External user name:", "External user password:"
            "Last verified" //"Last editor:" //"Last edited:
        };
        locationCbModel = new DefaultComboBoxModel();
        for (ComboItem ci : AIBclient.loadAllLocations()) {
            locationCbModel.addElement(ci);
        }
        salesContactCbModel = new DefaultComboBoxModel();
        for (ComboItem ci : AIBclient.loadAllUsersInitials()) {
            salesContactCbModel.addElement(ci);
        }
        JLabel paEmailLBL;
        JLabel alterEmailLBL;
        JComponent[] edits = new JComponent[]{
            getGridPanel(idField = new JTextField(), 6),
            getGridPanel(new JComponent[]{
                getBorderPanel(new JComponent[]{null, firstNameTF = new JTextField(16)}),
                getBorderPanel(new JComponent[]{new JLabel("Family Name:", SwingConstants.RIGHT),
                    familyName = new JTextField(16)})
            }),
            getGridPanel(new JComponent[]{
                titleCB = new Java2sAutoComboBox(AIBclient.loadDistinctTitles()),//JTextField(),
                new JLabel("Suffix:", SwingConstants.RIGHT),
                suffixCB = new Java2sAutoComboBox(AIBclient.loadDistinctSuffixes()),
                new JLabel("Greeting:", SwingConstants.RIGHT),
                greetingCB = new Java2sAutoComboBox(AIBclient.loadDistinctGreetings())
            }),
            getGridPanel(new JComponent[]{
                getBorderPanel(new JComponent[]{
                    null,
                    companiesListTF = new JTextField(),
                    new JButton(getCompaniesListAction("..."))
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
                    linksListTF = new JTextField(),
                    new JButton(getLinkListAction("..."))
                }),
                getBorderPanel(new JComponent[]{
                    new JLabel("Job discipline:", SwingConstants.RIGHT),
                    jobDisciplineCB = new Java2sAutoComboBox(AIBclient.loadDistinctJobDisciplines())
                })
            }),
            getGridPanel(new JComponent[]{
                getBorderPanel(new JComponent[]{null,
                    departmentCB = new Java2sAutoComboBox(AIBclient.loadDistinctDepartaments()),
                    new JButton(getHistoryAction("History"))}),
                getBorderPanel(new JComponent[]{new JLabel("Industry:", SwingConstants.RIGHT), industriesListTF = new JTextField(),
                    new JButton(getIndustryListAction("..."))})
            }),
            getGridPanel(specAddressTF = new JTextField(), 2),
            getGridPanel(new JComponent[]{
                getBorderPanel(new JComponent[]{
                    null,
                    sp1 = new JScrollPane(mailingAddressTA = new JTextArea(1, 20),
                    JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),
                    new JButton(new EditAreaAction("...", mailingAddressTA))
                }),
                getGridPanel(new JComponent[]{
                    new JLabel("Post Code:", SwingConstants.RIGHT),
                    mailingPostCodeTF = new JTextField()
                })
            }),
            getGridPanel(new JComponent[]{
                getGridPanel(new JComponent[]{
                    deskPhoneTF = new JTextField(10),
                    new JLabel("Desk Fax:", SwingConstants.RIGHT),
                    deskFaxTF = new JTextField(10)
                }),
                getGridPanel(new JComponent[]{
                    new JLabel("Mobile phone:", SwingConstants.RIGHT),
                    mobilePhoneTF = new JTextField()
                })
            }),
            getGridPanel(new JComponent[]{
                mainEmailTF = new JTextField(),
                alterEmailLBL = new JLabel("Alternate email:", SwingConstants.RIGHT),
                alterEmailTF = new JTextField()
            }),
            getGridPanel(new JComponent[]{
                paTF = new JTextField(),
                getGridPanel(new JComponent[]{
                    new JLabel("PA phone:", SwingConstants.RIGHT),
                    paPhoneTF = new JTextField(),
                    paEmailLBL = new JLabel("PA email:", SwingConstants.RIGHT),
                    paEmailTF = new JTextField()
                })
            }),
            getGridPanel(new JComponent[]{
                otherContactTF = new JTextField(),
                getBorderPanel(new JComponent[]{
                    new JLabel("AIB actions:", SwingConstants.RIGHT),
                    aibAwardsListTF = new JTextField(),
                    new JButton(getAwardsListAction("..."))
                })
            }),
            getGridPanel(new JComponent[]{
                primaryContactCB = new JCheckBox("Primary contact"),
                channelSubscriberCB = new JCheckBox("Channel subscriber"),
                marketingIntelDistCB = new JCheckBox("Market Intel dist."),
                mediaBriefingDistCB = new JCheckBox("Industry briefing dist."),
                sourceBookCB = new JCheckBox("Sourcebook")
            }),
            getGridPanel(new JComponent[]{
                new JButton(showPurchasesAction("Purchases / Dates...")),
                new JButton(showPurchaseInterestAction("Purchase Interest...")),
                getBorderPanel(new JComponent[]{
                    null,
                    new JLabel("Purchase timescale:", SwingConstants.RIGHT),
                    timescaleSP = new SelectedDateSpinner()
                }),
                new JPanel()
            }),
            getBorderPanel(new JComponent[]{
                comboPanelWithLookupBtn(salesContactCB = new JComboBox(salesContactCbModel), new UserLookupAction(salesContactCB)),
                getBorderPanel(new JComponent[]{
                    new JLabel(" Action date:", SwingConstants.RIGHT),
                    getBorderPanel(new JComponent[]{
                        actionDateSP = new SelectedDateSpinner(),
                        getGridPanel(new JComponent[]{
                            getBorderPanel(new JComponent[]{new JLabel(" Ext.user name:"), extUserNameTF = new JTextField(6)}),
                            getBorderPanel(new JComponent[]{new JLabel("Ext.user passwd:"), extPassworTF = new JTextField(6)}),})
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
        
        primaryContactCB.setHorizontalTextPosition(SwingConstants.LEFT);
        channelSubscriberCB.setHorizontalTextPosition(SwingConstants.LEFT);
        marketingIntelDistCB.setHorizontalTextPosition(SwingConstants.LEFT);
        mediaBriefingDistCB.setHorizontalTextPosition(SwingConstants.LEFT);
        sourceBookCB.setHorizontalTextPosition(SwingConstants.LEFT);
        
        sp1.setPreferredSize(new Dimension(sp1.getPreferredSize().width, idField.getPreferredSize().height));
//        salesContactCB.setPreferredSize(new Dimension(salesContactCB.getPreferredSize().width, idField.getPreferredSize().height));
        idField.setEnabled(false);
        mailingAddressTA.setEditable(false);
        
        for (Java2sAutoComboBox cb : new Java2sAutoComboBox[]{jobDisciplineCB, departmentCB, titleCB, greetingCB, suffixCB}) {
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
            locationCB.setEnabled(false);
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
        mainEmailTF.addFocusListener(new EmailFocusAdapter(labels[10], mainEmailTF));
    }
    
    @Override
    public void loadData() {
        People person = (People) getDbObject();
        if (person != null) {
            idField.setText(person.getPeopleId().toString());
            titleCB.setSelectedItem(person.getTitle());
            firstNameTF.setText(person.getFirstName());
            familyName.setText(person.getLastName());
            suffixCB.setSelectedItem(person.getSuffix());
            greetingCB.setSelectedItem(person.getGreeting());
            jobDisciplineCB.setSelectedItem(person.getJobDiscip());
//            locationCB.setSelectedIndex(person.getLocationId());
            selectComboItem(locationCB, person.getLocationId());
            selectComboItem(salesContactCB, person.getSalesContactId());
            departmentCB.setSelectedItem(person.getDepartment());
            linksListTF.setText(AIBclient.getLinkListOnPeopleID(person.getPeopleId()));
            industriesListTF.setText(AIBclient.getIndustryListOnPeopleID(person.getPeopleId()));
            aibAwardsListTF.setText(AIBclient.getAwardsOnPeopleID(person.getPeopleId()));
            companiesListTF.setText(AIBclient.getCompaniesOnPeopleID(person.getPeopleId()));
            specAddressTF.setText(person.getSpecAddress());
            mailingAddressTA.setText(person.getMailaddress());
            mailingPostCodeTF.setText(person.getMailpostcode());
            deskPhoneTF.setText(person.getDeskPhone());
            deskFaxTF.setText(person.getDeskFax());
            mobilePhoneTF.setText(person.getMobilePhone());
            mainEmailTF.setText(person.getMainEmail());
            alterEmailTF.setText(person.getAlterEmail());
            paTF.setText(person.getPa());
            paPhoneTF.setText(person.getPaPhone());
            paEmailTF.setText(person.getPaEmail());
            primaryContactCB.setSelected(person.getIsPrimary() != null && person.getIsPrimary() == 1);
            channelSubscriberCB.setSelected(person.getIsSubscriber() != null && person.getIsSubscriber() == 1);
            marketingIntelDistCB.setSelected(person.getIsMarketintl() != null && person.getIsMarketintl() == 1);
            mediaBriefingDistCB.setSelected(person.getIsMediabrief() != null && person.getIsMediabrief() == 1);
            sourceBookCB.setSelected(person.getIsInsourcebook() != null && person.getIsInsourcebook() == 1);
//            commentsTA.setText(person.getComments());
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
        person.setFirstName(firstNameTF.getText());
        person.setLastName(familyName.getText());
        person.setLocationId(getSelectedCbItem(locationCB));
        person.setSuffix((String) suffixCB.getSelectedItem());
        person.setGreeting((String) greetingCB.getSelectedItem());
        person.setJobDiscip((String) jobDisciplineCB.getSelectedItem());
        person.setDepartment((String) departmentCB.getSelectedItem());
        person.setDeskPhone(deskPhoneTF.getText());
        person.setDeskFax(deskFaxTF.getText());
        person.setMobilePhone(mobilePhoneTF.getText());
        person.setMainEmail(mainEmailTF.getText());
        person.setAlterEmail(alterEmailTF.getText());
        person.setPa(paTF.getText());
        person.setPaEmail(paEmailTF.getText());
        person.setPaPhone(paPhoneTF.getText());
        person.setIsPrimary(primaryContactCB.isSelected() ? 1 : 0);
        person.setIsSubscriber(channelSubscriberCB.isSelected() ? 1 : 0);
        person.setIsMarketintl(marketingIntelDistCB.isSelected() ? 1 : 0);
        person.setIsMediabrief(mediaBriefingDistCB.isSelected() ? 1 : 0);
        person.setIsInsourcebook(sourceBookCB.isSelected() ? 1 : 0);
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
                AIBclient.saveOrInsertPeopleLink(person.getPeopleId(), tok.nextToken());
            }
            AIBclient.removeRedundantPeopleLinks(person.getPeopleId(), linksListTF.getText());
            
            tok = new StringTokenizer(industriesListTF.getText(), ",");
            while (tok.hasMoreTokens()) {
                AIBclient.saveOrInsertPeopleIndustry(person.getPeopleId(), tok.nextToken());
            }
            AIBclient.removeRedundantPeopleIndustries(person.getPeopleId(), industriesListTF.getText());
            
            tok = new StringTokenizer(aibAwardsListTF.getText(), ",");
            while (tok.hasMoreTokens()) {
                AIBclient.savePeopleAward(person.getPeopleId(), tok.nextToken());
            }
            AIBclient.removeRedundantAwards(person.getPeopleId(), aibAwardsListTF.getText());
            
            tok = new StringTokenizer(companiesListTF.getText(), ",");
            while (tok.hasMoreTokens()) {
                AIBclient.savePeopleCompany(person.getPeopleId(), tok.nextToken());
            }
            AIBclient.removeRedundantPeopleCompany(person.getPeopleId(), companiesListTF.getText());
        }
        return ok;
    }
    
    private AbstractAction getLinkListAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                new ListInTextFieldDialog("Links List", new Object[]{linksListTF.getText(), AIBclient.loadAllLinks(), "Enter URL here:"});
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
    
    private AbstractAction getHistoryAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                People person = (People) getDbObject();
                new DepartmentsHistoryDialog(person == null || person.getPeopleId() == null ? 0 : person.getPeopleId());
            }
        };
    }
    
    private AbstractAction getAwardsListAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                new AIBawardsListInTextFieldDialog("AIB actions / Dates",
                        new Object[]{aibAwardsListTF.getText(), AIBclient.loadAllAwards(),
                    "Enter action here:"});
                aibAwardsListTF.setText(AIBawardsListInTextFieldDialog.getResultList());
            }
        };
    }
    
    private AbstractAction getCompaniesListAction(String lbl) {
        return new AbstractAction(lbl) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                new CompanyListInTextFieldDialog("Companies List",
                        new Object[]{companiesListTF.getText(), AIBclient.loadAllCompaniesShortNames(), "Company short name:"});
                companiesListTF.setText(ListInTextFieldDialog.getResultList());
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
}
