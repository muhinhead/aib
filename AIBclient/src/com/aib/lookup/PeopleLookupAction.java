/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.lookup;

import com.aib.AIBclient;
import com.aib.GeneralFrame;
import com.aib.orm.dbobject.ComboItem;
import com.aib.people.PeopleGrid;
import com.xlend.util.Java2sAutoComboBox;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;

/**
 *
 * @author nick
 */
public class PeopleLookupAction extends AbstractAction {

    private final JComboBox locationCB;
    private final boolean readOnly;
    private final String column;
    private final String value;

    /**
     *
     * @param cb
     */
    public PeopleLookupAction(JComboBox cb, boolean readOnly) {
        super(null,new ImageIcon(AIBclient.loadImage("lookup.png", PeopleLookupAction.class)));
        this.locationCB = cb;
        this.readOnly = readOnly;
        this.column = this.value = null;
    }
    
    public PeopleLookupAction(JComboBox cb) {
        this(cb, false);
    }

    public PeopleLookupAction(Java2sAutoComboBox cb, String column, String value) {
        super(null,new ImageIcon(AIBclient.loadImage("lookup.png", PeopleLookupAction.class)));
        this.locationCB = cb;
        this.readOnly = true;
        this.column = column;
        this.value = value;
        try {
            String select = "select " + (column != null && value != null ? "0 \"ID\",'NEW' as \"First Name\",'RECORD' as \"Last name\","
                    + "' ' as \"E-mail\", ' ' as \"Job\","
                    + "' ' as \"Company\","
                    + "' ' as \"Department\""
                    + " union select " : "")
                    + "p.people_id \"ID\","
                    + "p.first_name \"First Name\", "
                    + "p.last_name \"Last Name\", p.main_email as \"E-mail\",p.job_discip \"Job\", "
                    + "group_concat(c.full_name) \"Company\", "
                    + "p.department \"Department\" "
                    + "from people p left outer join peoplecompany pc on p.people_id=pc.people_id "
                    + "left outer join company c on pc.company_id=c.company_id "
                    + (column != null && value != null ? "where upper(" + column + ") like upper('" + value.trim() + "%')" : "")
                    + " group by p.people_id";
            LookupDialog ld = new LookupDialog((column != null && value != null ? "Select old or new record" : "Company Lookup"), locationCB,
                    new PeopleGrid(AIBclient.getExchanger(), select, readOnly),
                    new String[]{"abbreviation", "full_name", "main_phone", "main_fax"});
        } catch (RemoteException ex) {
            GeneralFrame.errMessageBox("Error:", ex.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            ComboItem citm = (ComboItem) locationCB.getSelectedItem();
            LookupDialog ld = new LookupDialog("People Lookup", locationCB,
                    new PeopleGrid(AIBclient.getExchanger(), "select people_id \"ID\","
                    + "first_name \"First Name\", "
                    + "last_name \"Last Name\",job_discip \"Job\", department \"Department\", main_email \"E-mail\" "
                    + "from people", false),
                    new String[]{"first_name", "last_name", "suffix", "greeting"});
        } catch (RemoteException ex) {
            GeneralFrame.errMessageBox("Error:", ex.getMessage());
        }
    }
}