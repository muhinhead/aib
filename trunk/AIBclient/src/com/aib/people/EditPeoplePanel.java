/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.people;

import com.aib.AIBclient;
import com.aib.EditAreaAction;
import com.aib.EditPanelWithPhoto;
import com.aib.GeneralFrame;
import com.aib.RecordEditPanel;
import static com.aib.RecordEditPanel.getBorderPanel;
import static com.aib.RecordEditPanel.getGridPanel;
import com.aib.lookup.CompanyListInTextFieldDialog;
import com.aib.lookup.AIBawardsListInTextFieldDialog;
import com.aib.lookup.ListInTextFieldDialog;
import com.aib.lookup.LocationLookupAction;
import com.aib.orm.Company;
import com.aib.orm.People;
import com.aib.orm.User;
import com.aib.orm.dbobject.ComboItem;
import com.aib.orm.dbobject.DbObject;
import com.aib.product.PurchasedProductsDialog;
import com.jidesoft.swing.JideTabbedPane;
import com.xlend.util.EmailFocusAdapter;
import com.xlend.util.Java2sAutoComboBox;
import com.xlend.util.SelectedDateSpinner;
import com.xlend.util.Util;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
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
    private JTextField titleTF;
    private JTextField firstNameTF;
    private JTextField familyName;
    private JTextField suffixTF;
    private JTextField greetingTF;
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
    private JSpinner lastEditedSP;
    private JTextField otherContactTF;
    private JTextField aibAwardsListTF;
    private SelectedDateSpinner lastVerifiedDateSP;
    private JTextArea commentsTA;
    private JComboBox locationCB;
    private JTextField companiesListTF;
    private DefaultComboBoxModel locationCbModel;
    private JCheckBox primaryContactCB;
    private JCheckBox channelSubscriberCB;
    private JCheckBox marketingIntelDistCB;
    private JCheckBox mediaBriefingDistCB;

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
            "Last verified" //"Last editor:" //"Last edited:
        //            "Prospecting level:",
        //            "Purchase timescale:",
        //            "Sales contact:",
        //            "Action date:", //"Next action:"
        //            "External user name:", //"External user password:"
        //            "Date last verified:" //"Last editor:" //"Last edited:
        };
        locationCbModel = new DefaultComboBoxModel();
        for (ComboItem ci : AIBclient.loadAllLocations()) {
            locationCbModel.addElement(ci);
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
                titleTF = new JTextField(),
                new JLabel("Suffix:", SwingConstants.RIGHT),
                suffixTF = new JTextField(6),
                new JLabel("Greeting:", SwingConstants.RIGHT),
                greetingTF = new JTextField()
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
                marketingIntelDistCB = new JCheckBox("Marketing Intel dist."),
                mediaBriefingDistCB = new JCheckBox("Media briefing dist.")
            }),
            getGridPanel(new JComponent[]{
                new JButton(showPurchasesAction("Purchases / Dates...")),
                new JButton("Purchase Interest...")
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
        sp1.setPreferredSize(new Dimension(sp1.getPreferredSize().width, idField.getPreferredSize().height));
        idField.setEnabled(false);
        mailingAddressTA.setEditable(false);
        jobDisciplineCB.setEditable(true);
        jobDisciplineCB.setStrict(false);
        departmentCB.setEditable(true);
        departmentCB.setStrict(false);
        lastEditedSP.setEditor(new JSpinner.DateEditor(lastEditedSP, DD_MM_YYYY));
        Util.addFocusSelectAllAction(lastEditedSP);
        linksListTF.setEditable(false);
        industriesListTF.setEditable(false);
        companiesListTF.setEditable(false);
        aibAwardsListTF.setEditable(false);
        lastEditorTF.setEnabled(false);
        lastEditedSP.setEnabled(false);

        organizePanels(titles, edits, null);

        JideTabbedPane downTabs = new JideTabbedPane();
        JScrollPane sp = new JScrollPane(commentsTA = new JTextArea());
        sp.setPreferredSize(new Dimension(400, 150));
        downTabs.add(sp, "Comments");

        downTabs.setPreferredSize(new Dimension(downTabs.getPreferredSize().width, 200));
        add(downTabs);

        linksListTF.setPreferredSize(new Dimension(linksListTF.getPreferredSize().width, idField.getPreferredSize().height));
        jobDisciplineCB.setPreferredSize(new Dimension(jobDisciplineCB.getPreferredSize().width, idField.getPreferredSize().height));
        departmentCB.setPreferredSize(new Dimension(departmentCB.getPreferredSize().width, idField.getPreferredSize().height));

        paEmailTF.addFocusListener(new EmailFocusAdapter(paEmailLBL, paEmailTF));
        alterEmailTF.addFocusListener(new EmailFocusAdapter(alterEmailLBL, alterEmailTF));
        mainEmailTF.addFocusListener(new EmailFocusAdapter(labels[10], mainEmailTF));
    }

    @Override
    public void loadData() {
        People person = (People) getDbObject();
        if (person != null) {
            idField.setText(person.getPeopleId().toString());
            titleTF.setText(person.getTitle());
            firstNameTF.setText(person.getFirstName());
            familyName.setText(person.getLastName());
            suffixTF.setText(person.getSuffix());
            greetingTF.setText(person.getGreeting());
            jobDisciplineCB.setSelectedItem(person.getJobDiscip());
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
        person.setTitle(titleTF.getText());
        person.setFirstName(firstNameTF.getText());
        person.setLastName(familyName.getText());
        person.setSuffix(suffixTF.getText());
        person.setGreeting(greetingTF.getText());
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
        person.setLasteditedBy(AIBclient.getCurrentUser().getUserId());
        dt = Calendar.getInstance().getTime();
        person.setLasteditDate(new java.sql.Timestamp(dt.getTime()));
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
            AIBclient.removeRedundantPeopleCompany(person.getPeopleId(), aibAwardsListTF.getText());
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
                Integer peopleID = getDbObject() == null ? null : ((People) getDbObject()).getPeopleId();
                if (getDbObject() == null && GeneralFrame.yesNo("Attention!",
                        "Do you want to save this person's record?") == JOptionPane.YES_OPTION) {
                    try {
                        if (save()) {
                            peopleID = ((People) getDbObject()).getPeopleId();
                        }
                    } catch (Exception ex) {
                        peopleID = null;
                        AIBclient.logAndShowMessage(ex);
                    }
                }
                if (peopleID != null) {
                    new PurchasedProductsDialog("Purchases", peopleID);
                }
            }
        };
    }
}
