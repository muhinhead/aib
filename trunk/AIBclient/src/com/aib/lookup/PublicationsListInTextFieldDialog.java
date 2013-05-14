/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.lookup;

import com.aib.AIBclient;
import com.aib.EditMentionDialog;

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
    protected boolean additionalDialog(String name) {
        name = name.trim();
        int p = name.indexOf("(");
        newPublicationName = p > 0 ? name.substring(0, p).trim() : name;
        if (AIBclient.publicationNotExist(name)) {
            new EditMentionDialog("Add new publication", null);
            return EditMentionDialog.okPressed;
        }
        return true;
    }
}
