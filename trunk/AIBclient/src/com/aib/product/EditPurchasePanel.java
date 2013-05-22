/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.product;

import com.aib.AIBclient;
import com.aib.RecordEditPanel;
import static com.aib.RecordEditPanel.getGridPanel;
import com.aib.lookup.PeopleLookupAction;
import com.aib.lookup.ProductLookupAction;
import com.aib.orm.Peopleproduct;
import com.aib.orm.dbobject.ComboItem;
import com.aib.orm.dbobject.DbObject;
import com.xlend.util.SelectedDateSpinner;
import com.xlend.util.Util;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

/**
 *
 * @author Nick Mukhin
 */
class EditPurchasePanel extends RecordEditPanel {

    private JTextField idField;
    private JComboBox productCB;
    private JComboBox peopleCB;
    private DefaultComboBoxModel productCbModel;
    private DefaultComboBoxModel peopleCbModel;
    private SelectedDateSpinner purchaseDateSP;
    private JLabel purchaserLbl;
//    private JPanel cp;

    public EditPurchasePanel(DbObject dbObject) {
        super(dbObject);
    }

    @Override
    protected void fillContent() {
        String titles[] = new String[]{
            "ID:",
            "Purchaser:",
            "Purchase date:",
            "Product:"
        };
        productCbModel = new DefaultComboBoxModel();
        for (ComboItem ci : AIBclient.loadAllProducts()) {
            productCbModel.addElement(ci);
        }
        if (EditPurchaseDialog.peopleID == null) {
            peopleCbModel = new DefaultComboBoxModel();
            for (ComboItem ci : AIBclient.loadAllPeople()) {
                peopleCbModel.addElement(ci);
            }
        }
        JComponent purchaser;
        if (EditPurchaseDialog.peopleID == null) {
            purchaser = comboPanelWithLookupBtn(peopleCB = new JComboBox(peopleCbModel), new PeopleLookupAction(peopleCB));
        } else {
            purchaser = purchaserLbl = new JLabel(AIBclient.getPeopleInfoOnID(EditPurchaseDialog.peopleID));
        }
        JComponent[] edits = new JComponent[]{
            getGridPanel(idField = new JTextField(), 6),
            purchaser,
            getBorderPanel(new JComponent[]{purchaseDateSP = new SelectedDateSpinner()}),
            comboPanelWithLookupBtn(productCB = new JComboBox(productCbModel), new ProductLookupAction(productCB))
        };
        idField.setEnabled(false);
        if (EditPurchaseDialog.peopleID != null) {
            purchaserLbl.setBorder(BorderFactory.createEtchedBorder());
        }
        purchaseDateSP.setEditor(new JSpinner.DateEditor(purchaseDateSP, DD_MM_YYYY));
        Util.addFocusSelectAllAction(purchaseDateSP);
//        cp.setMinimumSize(new Dimension(300,cp.getPreferredSize().height));
        organizePanels(titles, edits, null);
    }

    @Override
    public void loadData() {
        Peopleproduct pp = (Peopleproduct) getDbObject();
        if (pp != null) {
            idField.setText(pp.getPeopleproductId().toString());
            if (EditPurchaseDialog.peopleID == null) {
                selectComboItem(peopleCB, pp.getPeopleId());
            }
            selectComboItem(productCB, pp.getProductId());
            purchaseDateSP.setValue(new java.util.Date(pp.getPurchaseDate().getTime()));
        }
    }

    @Override
    public boolean save() throws Exception {
        boolean isNew = false;
        Peopleproduct pp = (Peopleproduct) getDbObject();
        if (pp == null) {
            pp = new Peopleproduct(null);
            isNew = true;
            pp.setPeopleproductId(0);
        }
        pp.setProductId(getSelectedCbItem(productCB));
        if (EditPurchaseDialog.peopleID == null) {
            pp.setPeopleId(getSelectedCbItem(peopleCB));
        } else {
            pp.setPeopleId(EditPurchaseDialog.peopleID);
        }
        java.util.Date dt = (java.util.Date) purchaseDateSP.getValue();
        if (dt != null) {
            pp.setPurchaseDate(new java.sql.Date(dt.getTime()));
        }
        return saveDbRecord(pp, isNew);
    }
}
