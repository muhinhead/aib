/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.filter;

import com.aib.AIBclient;
import com.aib.FilteredListFrame;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JComponent;

/**
 *
 * @author nick
 */
public class LocationFilterPanel extends AbstractFilterPanel {

    public LocationFilterPanel(FilteredListFrame parentFrame) {
        super(parentFrame, "location");
        fillComplexFilterList();
    }

    
    @Override
    protected boolean isDefaultComplex() {
        return true;
    }
}
