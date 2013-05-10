/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import com.aib.orm.Company;
import com.aib.orm.dbobject.DbObject;

/**
 *
 * @author nick
 */
class EditCompanyPanel extends EditPanelWithPhoto {

    public EditCompanyPanel(DbObject dbObject) {
        super(dbObject);
    }

    @Override
    protected void fillContent() {
        
    }

    @Override
    public void loadData() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean save() throws Exception {
        return true;
    }
}
