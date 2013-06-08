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
public class PeopleFilterPanel extends AbstractFilterPanel {

    public PeopleFilterPanel(FilteredListFrame parentFrame) {
        super(parentFrame, "people");
        fillComplexFilterList();
    }

    @Override
    protected boolean isDefaultComplex() {
        return true;
    }
}
