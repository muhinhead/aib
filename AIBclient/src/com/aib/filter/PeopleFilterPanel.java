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
        int idx = colNames.indexOf("location_id");
        if (idx>=0) {
            colNames.remove("location_id");
            colNames.add(idx, "Location.postcode");
            colNames.add(idx, "Location.address");
            colNames.add(idx, "Location.mailaddress");
            colNames.add(idx, "Location.mailpostcode");
            colNamesTypes.put("Location.postcode", Types.VARCHAR);
            colNamesTypes.put("Location.address", Types.VARCHAR);
            colNamesTypes.put("Location.mailpostcode", Types.VARCHAR);
            colNamesTypes.put("Location.mailaddress", Types.VARCHAR);
        }
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
