/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.filter;

import com.aib.orm.Filter;

/**
 *
 * @author Nick Mukhin
 */
public interface IFilterPanel {
    boolean isChanged();
    void setChanged(boolean changed);
    Filter getSelectedFilter();
}
