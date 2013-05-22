/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.product;

import com.aib.EditRecordDialog;
import com.aib.orm.dbobject.DbObject;
import java.awt.Dimension;

/**
 *
 * @author nick
 */
class EditPurchaseDialog extends EditRecordDialog {

    static Integer peopleID;
    public static boolean okPressed;

    public EditPurchaseDialog(String title, Object obj) {
        super(title, obj);
    }

    @Override
    protected void fillContent() {
        super.fillContent(new EditPurchasePanel((DbObject) getObject()));
    }

    @Override
    protected void setOkPressed(boolean b) {
        okPressed = b;
    }
}
