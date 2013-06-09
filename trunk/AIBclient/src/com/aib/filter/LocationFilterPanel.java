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
public class LocationFilterPanel extends AncestorFilterPanel {

    public LocationFilterPanel(FilteredListFrame parentFrame) {
        super(parentFrame, "location");
        fillComplexFilterList();
    }
}
