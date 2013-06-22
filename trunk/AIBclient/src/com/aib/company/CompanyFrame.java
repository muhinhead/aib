/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.company;

import com.aib.AIBclient;
import com.aib.FilteredListFrame;
import com.aib.GeneralFrame;
import static com.aib.GeneralFrame.adjustFilterQuery;
import com.aib.filter.CompanyFilterPanel;
import com.aib.orm.Filter;
import com.aib.orm.dbobject.ComboItem;
import com.aib.remote.IMessageSender;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import javax.swing.AbstractAction;
import javax.swing.JPanel;

/**
 *
 * @author Nick Mukhin
 */
public class CompanyFrame extends FilteredListFrame {

    private static String[] sheetList = new String[]{
        "List", "Filter"
    };
    private CompaniesGrid companiesPanel;
//    private JLabel outLbl;
//    private JComboBox outTemplatesCB;

    public CompanyFrame(IMessageSender exch) {
        super("Companies", exch);
    }

    @Override
    protected JPanel getListPanel() {
        if (companiesPanel == null) {
            try {
                registerGrid(companiesPanel = new CompaniesGrid(getExchanger()));
            } catch (RemoteException ex) {
                AIBclient.logAndShowMessage(ex);
            }
        }
        return companiesPanel;
    }

    @Override
    protected JPanel getFilterPanel() {
        if (filterPanel == null) {
            filterPanel = new JPanel(new BorderLayout());
            filterPanel.add(new CompanyFilterPanel(this));
        }
        return filterPanel;
    }

    @Override
    protected ActionListener getPrintAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOutputDialog("Company", companiesPanel.getSelect());
            }
        };
    }

    @Override
    public void applyFilter(Filter flt) {
        if (flt != null) {
            final String FROM_COMP = "from company ";
            String newSelect;
            int p = CompaniesGrid.SELECT.indexOf(FROM_COMP);
            if (flt.getIsComplex() != null && flt.getIsComplex().intValue() == 1) {
                adjustFilterQuery(flt, "Links",
                        "exists (select url from link,complink where link.link_id=complink.link_id "
                        + "and complink.company_id=company.company_id and url");
                adjustFilterQuery(flt, "Industries",
                        "exists (select descr from industry,compindustry where industry.industry_id=compindustry.industry_id "
                        + "and compindustry.company_id=company.company_id and descr");
                newSelect = adjustSelect(flt, FROM_COMP, CompaniesGrid.SELECT);
            } else {
                newSelect = CompaniesGrid.SELECT.substring(0, p + FROM_COMP.length())
                        + "left join peoplecompany pc on "
                        + "company.company_id=pc.company_id "
                        + "left join people on people.people_id=pc.people_id "
                        + "where (" + flt.getQuery().replaceAll("==", "=") + ")";
            }
            if (flt.getQuery() == null || flt.getQuery().trim().length() == 0) {
                GeneralFrame.errMessageBox("Attention!", "The empty filter couldn't be applied");
            } else {
                companiesPanel.setSelect(newSelect);
                companiesPanel.refresh();
                gotoFilterApplied(flt.getFilterId().intValue());
            }
        }
    }

    @Override
    protected ActionListener getChooseFilterAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!dontFilter) {
                    if (filtersCB.getSelectedIndex() == 0) {
                        companiesPanel.setSelect(CompaniesGrid.SELECT);
                    } else {
                        ComboItem ci = (ComboItem) filtersCB.getSelectedItem();
                        try {
                            Filter flt = (Filter) AIBclient.getExchanger().loadDbObjectOnID(Filter.class, ci.getId());
                            if (flt != null) {
                                applyFilter(flt);
                            }
                        } catch (RemoteException ex) {
                            AIBclient.logAndShowMessage(ex);
                            filtersCB.setSelectedIndex(0);
                        }
                    }
                }
                companiesPanel.refresh();
            }
        };
    }

    @Override
    protected String getMainTableName() {
        return "company";
    }
}
