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
class EditPeopleNoteDialog extends EditRecordDialog {

    static Integer peopleID;
    public static boolean okPressed;

    public EditPeopleNoteDialog(String title, DbObject obj) {
        super(title, obj);
    }

    @Override
    protected void fillContent() {
        super.fillContent(new EditPeopleNotePanel((DbObject) getObject()));
    }

    @Override
    protected void setOkPressed(boolean b) {
        okPressed = b;
    }
}
