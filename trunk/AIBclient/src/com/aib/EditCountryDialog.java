/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import static com.aib.EditRegionDialog.okPressed;
import com.aib.orm.dbobject.DbObject;

/**
 *
 * @author nick
 */
class EditCountryDialog extends EditRecordDialog {

    public static boolean okPressed;
    static Integer regionID;
    
    public EditCountryDialog(String title, Object obj) {
        super(title, obj);
    }
    
    @Override
    protected void fillContent() {
        super.fillContent(new EditCountryPanel((DbObject) getObject()));
    }

    @Override
    protected void setOkPressed(boolean b) {
        okPressed = b;
    }
}
