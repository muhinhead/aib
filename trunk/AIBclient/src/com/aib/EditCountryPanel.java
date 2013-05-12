/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import static com.aib.RecordEditPanel.getGridPanel;
import static com.aib.RecordEditPanel.selectComboItem;
import com.aib.orm.Country;
import com.aib.orm.dbobject.ComboItem;
import com.aib.orm.dbobject.DbObject;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author nick
 */
class EditCountryPanel extends RecordEditPanel {

    private JTextField idField;
    private DefaultComboBoxModel regionWorldCbModel;
    private JComboBox regionWorldCb;
    private JTextField countryNameTF;
    private JTextField shortNameTF;

    public EditCountryPanel(DbObject dbObject) {
        super(dbObject);
    }

    @Override
    protected void fillContent() {
        String titles[] = new String[]{
            "ID:",
            "World Region:",
            "Country:",
            "Short name:"
        };
        regionWorldCbModel = new DefaultComboBoxModel();
        for (ComboItem ci : AIBclient.loadAllRegions()) {
            regionWorldCbModel.addElement(ci);
        }
        JComponent[] edits = new JComponent[]{
            getGridPanel(idField = new JTextField(), 6),
            regionWorldCb = new JComboBox(regionWorldCbModel),
            countryNameTF = new JTextField(),
            getGridPanel(shortNameTF = new JTextField(2), 6)
        };
        idField.setEnabled(false);
        if (EditCountryDialog.regionID != null && EditCountryDialog.regionID.intValue() > 0) {
            regionWorldCb.setEnabled(false);
        }
        organizePanels(titles, edits, null);
    }

    @Override
    public void loadData() {
        Country country = (Country) getDbObject();
        if (country != null) {
            idField.setText(country.getCountryId().toString());
            countryNameTF.setText(country.getCountry());
            shortNameTF.setText(country.getShortname());
//            selectComboItem(regionWorldCb, country.getWorldregionId());
        }
        selectComboItem(regionWorldCb, EditCountryDialog.regionID);
    }

    @Override
    public boolean save() throws Exception {
        if (EditCountryDialog.regionID == null || EditCountryDialog.regionID.intValue() == 0) {
            return false;
        }
        Country country = (Country) getDbObject();
        boolean isNew = false;
        if (country == null) {
            country = new Country(null);
            country.setCountryId(0);
            isNew = true;
        }
        country.setCountry(countryNameTF.getText());
        country.setShortname(shortNameTF.getText());
        country.setWorldregionId(getSelectedCbItem(regionWorldCb));
        return saveDbRecord(country, isNew);
    }
}
