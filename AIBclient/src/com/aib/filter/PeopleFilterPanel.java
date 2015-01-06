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
public class PeopleFilterPanel extends AncestorFilterPanel {

    public PeopleFilterPanel(FilteredListFrame parentFrame) {
        super(parentFrame, "people");
        fillComplexFilterList();
    }
    
     @Override
    protected void loadColNamesTypes() throws RemoteException {
        super.loadColNamesTypes();
//        colNames.add("Links");
//        colNamesTypes.put("Links", Types.VARCHAR);
//        colNames.add("Industries");
//        colNamesTypes.put("Industries", Types.VARCHAR);        
        colNames.add("Awards");
        colNamesTypes.put("Awards", Types.VARCHAR);
        colNames.add("Companies");
        colNamesTypes.put("Companies", Types.VARCHAR);
    }
}
