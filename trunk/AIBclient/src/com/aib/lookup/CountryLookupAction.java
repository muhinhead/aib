/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.lookup;

import com.aib.AIBclient;
import com.aib.CountryGrid;
import com.aib.GeneralFrame;
import com.aib.orm.dbobject.ComboItem;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;

/**
 *
 * @author Nic Mukhin
 */
public class CountryLookupAction extends AbstractAction {

    public static final String SELECT =
            "select country_id \"Id\", "
            + "concat(country,' (',shortname,')') \"Country\" "
            + "from country order by country";
    
    private final JComboBox countryCB;
    private final String select;
    private boolean readOnly;

    public CountryLookupAction(JComboBox cb) {
        this(cb, null);
    }
    
    public CountryLookupAction(JComboBox cb, boolean readOnly) {
        this(cb, null);
        this.readOnly = readOnly;
    }
    
    public CountryLookupAction(JComboBox cb, String select) {
        super("...");
        this.countryCB = cb;
        this.select = select;
        this.readOnly = false;
    }

    private String getLookupSelect() {
        return (select == null ? SELECT : select);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            ComboItem citm = (ComboItem) countryCB.getSelectedItem();
            String slct = getLookupSelect();
            LookupDialog ld = new LookupDialog("Coyntry Lookup", countryCB,
                    new CountryGrid(AIBclient.getExchanger(),readOnly), new String[]{"country","shortname"});
        } catch (RemoteException ex) {
            GeneralFrame.errMessageBox("Error:", ex.getMessage());
        }
    }
}
