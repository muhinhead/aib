/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.people;

import com.aib.EditRecordDialog;
import com.aib.orm.dbobject.DbObject;

/**
 *
 * @author Nick Mukhin
 */
public class EditPeopleDialog extends EditRecordDialog {

    public static boolean okPressed;
    static Integer companyID;
    
    public EditPeopleDialog(String title, Object obj) {
        super(title, obj);
    }
    
     @Override
    protected void fillContent() {
        super.fillContent(new EditPeoplePanel((DbObject) getObject()));
    }

    @Override
    protected void setOkPressed(boolean b) {
        okPressed = b;
    }   
}
