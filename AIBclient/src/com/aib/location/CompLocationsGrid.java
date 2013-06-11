/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.location;

import com.aib.remote.IMessageSender;
import java.rmi.RemoteException;

/**
 *
 * @author Nick Mukhin
 */
public class CompLocationsGrid extends LocationsGrid {

    private static Integer companyID = null;

    public CompLocationsGrid(IMessageSender exchanger, Integer compID) throws RemoteException {
        super(exchanger, SELECT.replace("(select full_name from company where company_id=location.company_id) \"Company\",","")
                .replace("from location", "from location where location.company_id=" + (companyID = compID)));
    }

    @Override
    protected void additionalSettings() {
        EditLocationDialog.companyID = companyID;
    }
}
