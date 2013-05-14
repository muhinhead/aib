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
public class EditMentionDialog extends EditRecordDialog {
    public static boolean okPressed;
    static Integer regionID;
    
    public EditMentionDialog(String title, Object obj) {
        super(title, obj);
    }
    
    @Override
    protected void fillContent() {
        super.fillContent(new EditMentionPanel((DbObject) getObject()));
    }

    @Override
    protected void setOkPressed(boolean b) {
        okPressed = b;
    }
}
