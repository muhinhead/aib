/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.lookup;

import com.aib.AIBclient;
import com.aib.EditMentionDialog;
//import com.aib.orm.Aibaward;
import com.aib.orm.Aibpublic;
import com.aib.orm.dbobject.DbObject;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

/**
 *
 * @author nick
 */
public class PublicationsListInTextFieldDialog extends ListInTextFieldDialog {

    public static String newPublicationName;

    public PublicationsListInTextFieldDialog(String title, Object[] obs) {
        super(title, obs);
    }

    @Override
    protected String additionalDialog(String name) {
        name = name.trim();
        int p = name.indexOf("(");
        newPublicationName = p > 0 ? name.substring(0, p).trim() : name;
        if (AIBclient.publicationNotExist(name)) {
            EditMentionDialog ed = new EditMentionDialog("Add new publication", null);
            if (EditMentionDialog.okPressed) {
                Aibpublic pub = (Aibpublic) ed.getEditPanel().getDbObject();
                String sdate = new SimpleDateFormat("yyyy-MM-dd").format(pub.getPubDate());
                return pub.getPublication()+" ("+sdate+")";
            }
        }
        return name;
    }
    
        @Override
    protected AbstractAction getEditAction() {
        return new AbstractAction("Edit",new ImageIcon(AIBclient.loadImage("edit16.png", ListInTextFieldDialog.class))) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String itm = (String) urlJList.getSelectedValue();
                
                if (itm != null) {
                    int row = urlJList.getSelectedIndex();
                    try {
                        DbObject[] recs = AIBclient.getExchanger().getDbObjects(
                                Aibpublic.class, "concat(publication,' (',pub_date,')')='" + itm + "'", null);
                        if (recs.length > 0) {
                            EditMentionDialog ed = new EditMentionDialog("Edit action", recs[0]);
                            if (EditMentionDialog.okPressed) {
                                Aibpublic ap = (Aibpublic) ed.getEditPanel().getDbObject();
                                String sdate = new SimpleDateFormat("yyyy-MM-dd").format(ap.getPubDate());
                                selectedItems.set(row, ap.getPublication()+" ("+sdate + ")");
                                urlJList.updateUI();
                            }
                        }
                    } catch (RemoteException ex) {
                        AIBclient.logAndShowMessage(ex);
                    }
                }
            }
        };
    }

}
