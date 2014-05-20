/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.lookup;

import com.aib.AIBclient;
import com.aib.EditRegionDialog;
import com.aib.GeneralFrame;
import com.aib.GeneralGridPanel;
import com.aib.orm.Company;
import com.aib.orm.Worldregion;
import com.aib.remote.IMessageSender;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 *
 * @author nick
 */
public class WorldRegionsGrid extends GeneralGridPanel {

    public static final String SELECT =
            "select worldregion_id \"Id\", "
            + "descr \"Region\" from worldregion order by descr";
    private static HashMap<Integer, Integer> maxWidths = new HashMap<Integer, Integer>();

    static {
        maxWidths.put(0, 40);
    }

    public WorldRegionsGrid(IMessageSender exchanger) throws RemoteException {
        super(exchanger, SELECT, maxWidths, false);
    }

    public WorldRegionsGrid(IMessageSender exchanger, String select, boolean readonly) throws RemoteException {
        super(exchanger, select, maxWidths, readonly);
    }

    @Override
    protected AbstractAction addAction() {
        return new AbstractAction("Add") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                EditRegionDialog ed = new EditRegionDialog("Add Region", null);
                if (EditRegionDialog.okPressed) {
                    AIBclient.clearRegionsAndCountries();
                    Worldregion region = (Worldregion) ed.getEditPanel().getDbObject();
                    refresh(region.getWorldregionId());
                }
            }
        };
    }

    @Override
    protected AbstractAction editAction() {
        return new AbstractAction("Edit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = getSelectedID();
                try {
                    Worldregion region = (Worldregion) exchanger.loadDbObjectOnID(Worldregion.class, id);
                    new EditRegionDialog("Edit Region", region);
                    if (EditRegionDialog.okPressed) {
                        AIBclient.clearRegionsAndCountries();
                        refresh();
                    }
                } catch (RemoteException ex) {
                    AIBclient.logAndShowMessage(ex);
                }
            }
        };
    }

    @Override
    protected AbstractAction delAction() {
        return new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = getSelectedID();
                try {
                    Worldregion region = (Worldregion) exchanger.loadDbObjectOnID(Worldregion.class, id);
                    if (region != null && GeneralFrame.yesNo("Attention!", "Do you want to delete this record?") == JOptionPane.YES_OPTION) {
                        AIBclient.clearRegionsAndCountries();
                        exchanger.deleteObject(region);
                        refresh();
                    }
                } catch (RemoteException ex) {
                    AIBclient.logAndShowMessage(ex);
                }
            }
        };
    }
}
