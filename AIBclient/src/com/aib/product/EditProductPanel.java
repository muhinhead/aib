/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.product;

import com.aib.RecordEditPanel;
import static com.aib.RecordEditPanel.getGridPanel;
import com.aib.orm.Product;
import com.aib.orm.dbobject.DbObject;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 *
 * @author Nick Mukhin
 */
class EditProductPanel extends RecordEditPanel {

    private JTextField idField;
    private JTextField productDescrTF;

    public EditProductPanel(DbObject dbObject) {
        super(dbObject);
    }

    @Override
    protected void fillContent() {
        String titles[] = new String[]{
            "ID:",
            "Product:"
        };
        JComponent[] edits = new JComponent[]{
            getGridPanel(idField = new JTextField(), 6),
            productDescrTF = new JTextField()
        };
        idField.setEnabled(false);
        organizePanels(titles, edits, null);
    }

    @Override
    public void loadData() {
        Product pr = (Product) getDbObject();
        if (pr != null) {
            idField.setText(pr.getProductId().toString());
            productDescrTF.setText(pr.getDescr());
        }
    }

    @Override
    public boolean save() throws Exception {
        Product pr = (Product) getDbObject();
        boolean isNew = false;
        if (pr == null) {
            pr = new Product(null);
            pr.setProductId(0);
            isNew = true;
        }
        pr.setDescr(productDescrTF.getText());
        return saveDbRecord(pr, isNew);
    }
}
