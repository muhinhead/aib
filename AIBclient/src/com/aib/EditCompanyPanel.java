/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import com.aib.lookup.WorldRegionLookupAction;
import com.aib.orm.Company;
import com.aib.orm.dbobject.ComboItem;
import com.aib.orm.dbobject.DbObject;
import com.jidesoft.swing.JideTabbedPane;
import com.xlend.util.PopupDialog;
import com.xlend.util.SelectedNumberSpinner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
            "Region of World:" //,"Country:"
        };
        regionWorldCbModel = new DefaultComboBoxModel();
        countryCbModel = new DefaultComboBoxModel();
        for (ComboItem ci : AIBclient.loadAllRegions()) {
            regionWorldCbModel.addElement(ci);
        }
//        for (ComboItem ci : AIBclient.loadAllCountries()) {
//            countryCbModel.addElement(ci);
//        }
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
            getBorderPanel(new JComponent[]{turnoverSP = new SelectedNumberSpinner(0, 0, 999999999, 100)}),
            physicAddressTF = new JTextField(),
            getGridPanel(postCodeTF = new JTextField(), 4),
            mailingAddressTF = new JTextField(),
            getGridPanel(new JComponent[]{
                comboPanelWithLookupBtn(regionWorldCb = new JComboBox(regionWorldCbModel),
                        new WorldRegionLookupAction(regionWorldCb)),
                new JLabel("Country:", SwingConstants.RIGHT),
                countryCB = new JComboBox(countryCbModel)
            })
        };
        idField.setEnabled(false);
        linksListTF.setEditable(false);
        industriesListTF.setEditable(false);

        regionWorldCb.addActionListener(countryCBreloadAction());
        regionWorldCb.setSelectedIndex(0);
        organizePanels(titles, edits, null);
        
//        linksListTF.setText("www.gmail.com,www.mail.ru");
    }

    @Override
    public void loadData() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean save() throws Exception {
        return true;
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

    private ActionListener countryCBreloadAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                syncCountries();
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
        };
    }
}
