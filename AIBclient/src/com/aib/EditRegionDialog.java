/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import com.aib.orm.dbobject.DbObject;

/**
 *
 * @author nick
 */
public class EditRegionDialog extends EditRecordDialog {

    public static boolean okPressed;
    public EditRegionDialog(String title, Object obj) {
        super(title, obj);
    }
    
    @Override
    protected void fillContent() {
        super.fillContent(new EditRegionPanel((DbObject) getObject()));
    }

    @Override
    protected void setOkPressed(boolean b) {
        okPressed = b;
    }
}
