/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.filter;

import com.aib.FilteredListFrame;
import java.rmi.RemoteException;
import java.sql.Types;

/**
 *
 * @author nick
 */
public abstract class AncestorFilterPanel extends AbstractFilterPanel {

    public AncestorFilterPanel(FilteredListFrame parentFrame, String tabName) {
        super(parentFrame, tabName);
    }

    @Override
    protected boolean isDefaultComplex() {
        return true;
    }

    @Override
    protected void loadColNamesTypes() throws RemoteException {
        super.loadColNamesTypes();
        colNames.add("Links");
        colNamesTypes.put("Links", Types.VARCHAR);
    }
    
}
