/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.lookup;

import com.aib.AIBclient;
import com.aib.GeneralFrame;
import com.aib.LookupDialog;
import com.aib.WorldRegionsGrid;
import com.aib.orm.dbobject.ComboItem;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;

/**
 *
 * @author nick
 */
public class WorldRegionLookupAction extends AbstractAction {

    private final JComboBox regionCB;
    private final String select;

    public WorldRegionLookupAction(JComboBox cb) {
        this(cb, null);
    }

    public WorldRegionLookupAction(JComboBox cb, String select) {
        super("...");
        this.regionCB = cb;
        this.select = select;
    }

    private String getLookupSelect() {
        return (select == null ? WorldRegionsGrid.SELECT : select);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            ComboItem citm = (ComboItem) regionCB.getSelectedItem();
            String slct = getLookupSelect();
            LookupDialog ld = new LookupDialog("World Region Lookup", regionCB,
                    new WorldRegionsGrid(AIBclient.getExchanger(), slct, false), new String[]{"descr"});
        } catch (RemoteException ex) {
            GeneralFrame.errMessageBox("Error:", ex.getMessage());
        }
    }
}
