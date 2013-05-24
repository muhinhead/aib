/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.product;

import com.aib.AIBclient;
import com.aib.RecordEditPanel;
import static com.aib.RecordEditPanel.getGridPanel;
import static com.aib.RecordEditPanel.getSelectedCbItem;
import static com.aib.RecordEditPanel.selectComboItem;
import com.aib.lookup.PeopleLookupAction;
import com.aib.lookup.ProductLookupAction;
import com.aib.orm.Peopleinterest;
import com.aib.orm.dbobject.ComboItem;
import com.aib.orm.dbobject.DbObject;
import com.xlend.util.Java2sAutoComboBox;
import com.xlend.util.SelectedDateSpinner;
import com.xlend.util.Util;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

/**
 *
 * @author Nick Mukhin
 */
class EditPurchaseInterestPanel extends RecordEditPanel {

    private JComboBox peopleCB;
    private JLabel purchaserLbl;
    private DefaultComboBoxModel peopleCbModel;
    private DefaultComboBoxModel productCbModel;
    private JTextField idField;
    private JComboBox productCB;
    private Java2sAutoComboBox prospLevelCB;
    private SelectedDateSpinner purchaseDateSP;

    public EditPurchaseInterestPanel(DbObject dbObject) {
        super(dbObject);
    }

    @Override
    protected void fillContent() {
        String titles[] = new String[]{
            "ID:",
            "Purchaser:",
            "Product:",
            "Prospecting Level:",
            "Approx.purchase date:"
        };
        productCbModel = new DefaultComboBoxModel();
        for (ComboItem ci : AIBclient.loadAllProducts()) {
            productCbModel.addElement(ci);
        }
        if (EditPurchaseInterestDialog.peopleID == null) {
            peopleCbModel = new DefaultComboBoxModel();
            for (ComboItem ci : AIBclient.loadAllPeople()) {
                peopleCbModel.addElement(ci);
            }
        }
        JComponent purchaser;
        if (EditPurchaseInterestDialog.peopleID == null) {
            purchaser = comboPanelWithLookupBtn(peopleCB = new JComboBox(peopleCbModel), new PeopleLookupAction(peopleCB));
        } else {
            purchaser = purchaserLbl = new JLabel(AIBclient.getPeopleInfoOnID(EditPurchaseInterestDialog.peopleID));
        }
        JComponent[] edits = new JComponent[]{
            getGridPanel(idField = new JTextField(), 6),
            purchaser,
            comboPanelWithLookupBtn(productCB = new JComboBox(productCbModel), new ProductLookupAction(productCB)),
            getBorderPanel(new JComponent[]{prospLevelCB = new Java2sAutoComboBox(AIBclient.getProspLevelsList())}),
            getBorderPanel(new JComponent[]{purchaseDateSP = new SelectedDateSpinner()})
        };
        idField.setEnabled(false);
        if (EditPurchaseInterestDialog.peopleID != null) {
            purchaserLbl.setBorder(BorderFactory.createEtchedBorder());
        }
        purchaseDateSP.setEditor(new JSpinner.DateEditor(purchaseDateSP, DD_MM_YYYY));
//        purchaseDateSP.setEnabled(false);
        Util.addFocusSelectAllAction(purchaseDateSP);
        prospLevelCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Calendar calendar = Calendar.getInstance();
                String itm = (String) prospLevelCB.getSelectedItem();
                if (itm != null) {
                    int addMonths = 0;
                    if (itm.indexOf(" 1 month") > 0) {
                        addMonths = 1;
                    } else if (itm.indexOf(" 3 month") > 0) {
                        addMonths = 3;
                    } else if (itm.indexOf(" 6 month") > 0) {
                        addMonths = 6;
                    }
                    if (addMonths > 0) {
                        calendar.add(Calendar.MONTH, addMonths);
                        purchaseDateSP.setValue(calendar.getTime());
                        purchaseDateSP.setVisible(true);
                    } else {
                        purchaseDateSP.setVisible(false);
                    }
                }
            }
        });

        organizePanels(titles, edits, null);
    }

    @Override
    public void loadData() {
        Peopleinterest pi = (Peopleinterest) getDbObject();
        if (pi != null) {
            idField.setText(pi.getPeopleinterestId().toString());
            if (EditPurchaseInterestDialog.peopleID == null) {
                selectComboItem(peopleCB, pi.getPeopleId());
            }
            prospLevelCB.setSelectedItem(pi.getProspectingLevel());
            selectComboItem(productCB, pi.getProductId());
            if (pi.getPurchaseDate() == null) {
                purchaseDateSP.setVisible(false);
            } else {
                purchaseDateSP.setValue(new java.util.Date(pi.getPurchaseDate().getTime()));
            }
        }
    }

    @Override
    public boolean save() throws Exception {
        boolean isNew = false;
        Peopleinterest pi = (Peopleinterest) getDbObject();
        if (pi == null) {
            pi = new Peopleinterest(null);
            pi.setPeopleinterestId(0);
            isNew = true;
        }
        pi.setProductId(getSelectedCbItem(productCB));
        pi.setProspectingLevel((String) prospLevelCB.getSelectedItem());
        if (EditPurchaseInterestDialog.peopleID == null) {
            pi.setPeopleId(getSelectedCbItem(peopleCB));
        } else {
            pi.setPeopleId(EditPurchaseInterestDialog.peopleID);
        }
        if (purchaseDateSP.isVisible()) {
            java.util.Date dt = (java.util.Date) purchaseDateSP.getValue();
            if (dt != null) {
                pi.setPurchaseDate(new java.sql.Date(dt.getTime()));
            }
        } else {
            pi.setPurchaseDate(null);
        }
        return saveDbRecord(pi, isNew);
    }
}
