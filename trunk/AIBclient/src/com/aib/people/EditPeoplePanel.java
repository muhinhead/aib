/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.people;

import com.aib.AIBclient;
import com.aib.EditAreaAction;
import com.aib.EditPanelWithPhoto;
import com.aib.RecordEditPanel;
import static com.aib.RecordEditPanel.getBorderPanel;
import static com.aib.RecordEditPanel.getGridPanel;
import com.aib.lookup.AIBawardsListInTextFieldDialog;
import com.aib.lookup.ListInTextFieldDialog;
import com.aib.orm.Company;
import com.aib.orm.People;
import com.aib.orm.User;
import com.aib.orm.dbobject.DbObject;
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
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
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

    public EditPeoplePanel(DbObject dbObject) {
        super(dbObject);
    }

    @Override
    protected void fillContent() {
        String titles[] = new String[]{
            "ID:",//"Title:",
            "First Name:", //"Family Name:", 
            "Suffix:", //"Greeting:",
            "Links:",
            "Job discipline:",
            "Department:",
            "Industry:",
            "Specific address:",
            "Mailing address:",
            "Desk phone:", // "Desk fax:", "Mibile phone:"
            "Main email:", // "Alternate email:"
            "PA:", // "PA phone:", //"PA email:"
            "Other contact info:",
            "AIB actions/date:",
            "Last editor:" //"Last edited:
        //            
        //            "AIB involvement:",
        //            "Primary contact:", //"Channel subscriber","Marketing Intel dist.", "Media briefing dist."
        //            "Purchases:",
        //            "Purchase interest:",
        //            "Prospecting level:",
        //            "Purchase timescale:",
        //            "Sales contact:",
        //            "Action date:", //"Next action:"
        //            "External user name:", //"External user password:"
        //            "Date last verified:" //"Last editor:" //"Last edited:
        };
        JLabel paEmailLBL;
        JLabel alterEmailLBL;
        mailingAddressTA = new JTextArea(1, 20);
        JComponent[] edits = new JComponent[]{
            getGridPanel(getGridPanel(new JComponent[]{
                idField = new JTextField(),
                new JLabel("Title:", SwingConstants.RIGHT),
                titleTF = new JTextField()
            }), 2),
            getGridPanel(new JComponent[]{
                getBorderPanel(new JComponent[]{null, firstNameTF = new JTextField(16)}),
                getBorderPanel(new JComponent[]{new JLabel("Family Name:", SwingConstants.RIGHT),
                    familyName = new JTextField(16)})
            }),
            getGridPanel(getGridPanel(new JComponent[]{
                suffixTF = new JTextField(6),
                new JLabel("Greeting:", SwingConstants.RIGHT),
                greetingTF = new JTextField()
            }), 2),
            getBorderPanel(new JComponent[]{null, linksListTF = new JTextField(), new JButton(getLinkListAction("..."))}),
            getGridPanel(jobDisciplineCB = new Java2sAutoComboBox(AIBclient.loadDistinctJobDisciplines()), 2),
            getBorderPanel(new JComponent[]{null,
                departmentCB = new Java2sAutoComboBox(AIBclient.loadDistinctDepartaments()),
                new JButton(getHistoryAction("History"))}),
            getBorderPanel(new JComponent[]{null, industriesListTF = new JTextField(),
                new JButton(getIndustryListAction("..."))}),
            getGridPanel(specAddressTF = new JTextField(), 3),
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
            otherContactTF = new JTextField(),
            getBorderPanel(new JComponent[]{
                null,
                aibAwardsListTF = new JTextField(),
                new JButton(getAwardsListAction("..."))
            }),
            getBorderPanel(new JComponent[]{
                lastEditorTF = new JTextField(2),
                getBorderPanel(new JComponent[]{
                    new JLabel("Last edited:", SwingConstants.RIGHT),
                    getBorderPanel(new JComponent[]{
                        lastEditedSP = new SelectedDateSpinner()
                    })
                })
            })
        };
        sp1.setMaximumSize(new Dimension(sp1.getPreferredSize().width, idField.getPreferredSize().height));
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
        
        lastEditorTF.setEnabled(false);
        lastEditedSP.setEnabled(false);

        organizePanels(titles, edits, null);

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
}