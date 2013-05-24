/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.product;

import com.aib.EditRecordDialog;
import com.aib.orm.dbobject.DbObject;
import static com.aib.product.EditPurchaseDialog.okPressed;

/**
 *
 * @author Nick Mukhin
 */
class EditPurchaseInterestDialog extends EditRecordDialog {

    static Integer peopleID;
    public static boolean okPressed;

    public EditPurchaseInterestDialog(String title, DbObject obj) {
        super(title, obj);
    }

    @Override
    protected void fillContent() {
        super.fillContent(new EditPurchaseInterestPanel((DbObject) getObject()));
    }

    @Override
    protected void setOkPressed(boolean b) {
        okPressed = b;
    }
}
