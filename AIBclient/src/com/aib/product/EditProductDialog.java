/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.product;

import com.aib.EditRecordDialog;
import com.aib.orm.dbobject.DbObject;

/**
 *
 * @author Nick Mukhin
 */
class EditProductDialog extends EditRecordDialog {
    public static boolean okPressed;

    public EditProductDialog(String title, Object obj) {
        super(title, obj);
    }

    @Override
    protected void fillContent() {
        super.fillContent(new EditProductPanel((DbObject) getObject()));
    }

    @Override
    protected void setOkPressed(boolean b) {
        okPressed = b;
    }
}
