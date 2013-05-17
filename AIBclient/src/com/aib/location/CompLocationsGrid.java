/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.location;

import com.aib.remote.IMessageSender;
import java.rmi.RemoteException;

/**
 *
 * @author nick
 */
public class CompLocationsGrid extends LocationsGrid {

    private static Integer companyID = null;

    public CompLocationsGrid(IMessageSender exchanger, Integer compID) throws RemoteException {
        super(exchanger, SELECT.replace("c.full_name \"Company\",","")
                .replace("where ", "where c.company_id=" + (companyID = compID) + " and "));
    }

    @Override
    protected void additionalSetttings() {
        EditLocationDialog.companyID = companyID;
    }
}
