/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.export;

import com.aib.AIBclient;
import com.aib.GeneralGridAdapter;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 *
 * @author Nick Mukhin
 */
public class TemplatesGrid extends GeneralGridAdapter {
    public static final String SELECT = "select reportform_id \"Id\","
            + "name \"Template Name\",descr \"Description\" "
            + "from reportform where tablename='@'";
    
    private static HashMap<Integer, Integer> maxWidths = new HashMap<Integer, Integer>();
    static {
        maxWidths.put(0, 40);
//        maxWidths.put(1, 200);
    }
    
    public TemplatesGrid(String tableName) throws RemoteException {
        super(AIBclient.getExchanger(), SELECT.replace("@", tableName), maxWidths, true);
    }
   
}
