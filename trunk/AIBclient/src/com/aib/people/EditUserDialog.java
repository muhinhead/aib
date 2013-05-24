/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.people;

import com.aib.EditRecordDialog;
import com.aib.orm.User;
import com.aib.orm.dbobject.DbObject;
import static com.aib.people.EditPeopleDialog.okPressed;

/**
 *
 * @author nick
 */
class EditUserDialog extends EditRecordDialog {

    public static boolean okPressed;
    
    public EditUserDialog(String title, Object obj) {
        super(title, obj);
    }
    
     @Override
    protected void fillContent() {
        super.fillContent(new EditUserPanel((DbObject) getObject()));
    }

    @Override
    protected void setOkPressed(boolean b) {
        okPressed = b;
    }   
}
