/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import com.aib.orm.dbobject.DbObject;

/**
 *
 * @author Nick Mukhin
 */
public class EditAwardDialog extends EditRecordDialog {
    public static boolean okPressed;
    static Integer regionID;
    
    public EditAwardDialog(String title, Object obj) {
        super(title, obj);
    }
    
    @Override
    protected void fillContent() {
        super.fillContent(new EditAwardPanel((DbObject) getObject()));
    }

    @Override
    protected void setOkPressed(boolean b) {
        okPressed = b;
    }
}
