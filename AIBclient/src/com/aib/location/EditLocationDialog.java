/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.location;

import com.aib.EditRecordDialog;
import com.aib.orm.Company;
import com.aib.orm.dbobject.DbObject;

/**
 *
 * @author nick
 */
class EditLocationDialog extends EditRecordDialog {

    public static boolean okPressed;
    static Integer companyID;
    
    public EditLocationDialog(String title, Object obj) {
        super(title, obj);
    }
    
     @Override
    protected void fillContent() {
        super.fillContent(new EditLocationPanel((DbObject) getObject()));
    }

    @Override
    protected void setOkPressed(boolean b) {
        okPressed = b;
    }
}
