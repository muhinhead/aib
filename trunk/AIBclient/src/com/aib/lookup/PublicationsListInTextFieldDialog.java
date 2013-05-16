/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.lookup;

import com.aib.AIBclient;
import com.aib.EditMentionDialog;
import com.aib.orm.Aibpublic;
import java.text.SimpleDateFormat;

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
}
