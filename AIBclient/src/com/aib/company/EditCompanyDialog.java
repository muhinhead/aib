package com.aib.company;

import com.aib.EditRecordDialog;
import com.aib.orm.Company;

/**
 *
 * @author Nick Mukhin
 */
class EditCompanyDialog extends EditRecordDialog {

    public static boolean okPressed;
     public EditCompanyDialog(String title, Object obj) {
        super(title, obj);
    }
    
    @Override
    protected void fillContent() {
        super.fillContent(new EditCompanyPanel((Company) getObject()));
    }

    @Override
    protected void setOkPressed(boolean b) {
        okPressed = b;
    }
}
