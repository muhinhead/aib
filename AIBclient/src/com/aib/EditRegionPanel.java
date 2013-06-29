/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import static com.aib.RecordEditPanel.getGridPanel;
import com.aib.orm.Worldregion;
import com.aib.orm.dbobject.DbObject;
import com.jidesoft.swing.JideTabbedPane;
import com.xlend.util.SelectedNumberSpinner;
import java.rmi.RemoteException;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;

/**
 *
 * @author nick
 */
class EditRegionPanel extends RecordEditPanel {

    private JTextField idField;
    private JTextField regionTF;
    private JSpinner postPriceSP;
    private JSpinner postStatusSP;
    private JSpinner postNumberSP;
    private JideTabbedPane downTab;

    public EditRegionPanel(DbObject dbObject) {
        super(dbObject);
    }

    @Override
    protected void fillContent() {
        String titles[] = new String[]{
            "ID:",
            "Region of World:",
            "Post Price:",
            "Post Status:",
            "Post Number:"
        };
        JComponent[] edits = new JComponent[]{
            getGridPanel(idField = new JTextField(), 6),
            regionTF = new JTextField(),
            getGridPanel(postPriceSP = new SelectedNumberSpinner(0.0, 0.0, 999.99, 0.01), 6),
            getGridPanel(postStatusSP = new SelectedNumberSpinner(0, 0, 10, 1), 6),
            getGridPanel(postNumberSP = new SelectedNumberSpinner(0, 0, 10, 1), 6)
        };
        idField.setEnabled(false);
        organizePanels(titles, edits, null);
//        JideTabbedPane tab;
        add(downTab = new JideTabbedPane());

    }

    @Override
    public void loadData() {
        Worldregion reg = (Worldregion) getDbObject();
        if (reg != null) {
            idField.setText(reg.getWorldregionId().toString());
            regionTF.setText(reg.getDescr());
            if (reg.getPostPrice() != null) {
                postPriceSP.setValue(reg.getPostPrice());
            }
            if (reg.getPostStatus() != null) {
                postStatusSP.setValue(reg.getPostStatus());
            }
            if (reg.getPostNumber() != null) {
                postNumberSP.setValue(reg.getPostNumber());
            }
            try {
                downTab.add(new CountryGrid(AIBclient.getExchanger(), reg.getWorldregionId()), "Country list");
            } catch (RemoteException ex) {
                AIBclient.logAndShowMessage(ex);
            }
        }
    }

    @Override
    public boolean save() throws Exception {
        Worldregion reg = (Worldregion) getDbObject();
        boolean isNew = false;
        if (reg == null) {
            reg = new Worldregion(null);
            reg.setWorldregionId(0);
            isNew = true;
        }
        reg.setDescr(regionTF.getText());
        reg.setPostPrice((Double) postPriceSP.getValue());
        reg.setPostStatus((Integer) postStatusSP.getValue());
        reg.setPostNumber((Integer) postNumberSP.getValue());
        return saveDbRecord(reg, isNew);
    }
}
