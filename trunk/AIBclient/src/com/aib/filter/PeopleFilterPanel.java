/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.filter;

import com.aib.FilteredListFrame;

/**
 *
 * @author nick
 */
public class PeopleFilterPanel extends AncestorFilterPanel {

    public PeopleFilterPanel(FilteredListFrame parentFrame) {
        super(parentFrame, "people");
        fillComplexFilterList();
    }
}
