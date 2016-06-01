package com.aib.people;

import com.aib.AIBclient;
import com.aib.FilteredListFrame;
import com.aib.GeneralFrame;
import static com.aib.GeneralFrame.adjustFilterQuery;
import com.aib.filter.PeopleFilterPanel;
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
public class PeopleFrame extends FilteredListFrame {

    static final String LIKE = " LIKE ";

    private static String[] sheetList = new String[]{
        "List", "Filter"
    };
    private PeopleGrid peoplePanel;

    public PeopleFrame(IMessageSender exch) {
        super("People", exch);
    }

    @Override
    protected JPanel getListPanel() {
        if (peoplePanel == null) {
            try {
                registerGrid(peoplePanel = new PeopleGrid(getExchanger()));
            } catch (RemoteException ex) {
                AIBclient.logAndShowMessage(ex);
            }
        }
        return peoplePanel;
    }

    @Override
    protected JPanel getFilterPanel() {
        if (filterPanel == null) {
            filterPanel = new JPanel(new BorderLayout());
            filterPanel.add(new PeopleFilterPanel(this));
        }
        return filterPanel;
    }

    @Override
    protected ActionListener getPrintAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOutputDialog("People", peoplePanel.getSelect());
            }
        };
    }

    @Override
    public void applyFilter(Filter flt) {
        if (flt != null) {
            adjustFilterQuery(flt, "Links",
                    "exists (select url from link,peoplelink where link.link_id=peoplelink.link_id "
                    + "and peoplelink.people_id=people.people_id and url");
            adjustFilterQuery(flt, "Industries",
                    "exists (select descr from industry,peopleindustry where industry.industry_id=peopleindustry.industry_id "
                    + "and peopleindustry.people_id=people.people_id and descr");
            adjustFilterQuery(flt, "Awards",
                    "exists (select award from aibaward,peopleaward where aibaward.aibaward_id=peopleaward.aibaward_id "
                    + "and peopleaward.people_id=people.people_id and award");
            adjustFilterQuery(flt, "Location.address", "exists (select location_id from location where location_id=people.location_id and address");
            adjustFilterQuery(flt, "Location.postcode", "exists (select location_id from location where location_id=people.location_id and postcode");
            adjustFilterQuery(flt, "Location.mailaddress", "exists (select location_id from location where location_id=people.location_id and mailaddress");
            adjustFilterQuery(flt, "Location.mailpostcode", "exists (select location_id from location where location_id=people.location_id and mailpostcode");
            String existPeopleCompany = "exists (select peoplecompany_id from peoplecompany where people_id=people.people_id and company_id";
            adjustFilterQuery(flt, "Companies", existPeopleCompany);
            int p = flt.getQuery().indexOf(existPeopleCompany + " IN (");
            if (p >= 0) {
                int shift = existPeopleCompany.length() + 5;
                int pp = flt.getQuery().substring(p + shift).indexOf(")");
                String sid = flt.getQuery().substring(p + shift, p + shift + pp);
                int filter_id = Integer.parseInt(sid);
                try {
                    Filter subflt = (Filter) AIBclient.getExchanger().loadDbObjectOnID(Filter.class, filter_id);
                    String newQry = flt.getQuery().substring(0, p + shift)
                            + "select company_id from company where "
                            + subflt.getQuery()
                            + flt.getQuery().substring(p + shift + pp);
                    flt.setQuery(newQry);
                } catch (Exception ex) {
                    AIBclient.logAndShowMessage(ex);
                }
            }
//            adjustFilterQuery(flt, "Companies",
//                    "exists (select company_id from company)");
            String newSelect = adjustSelect(flt, "from people ", PeopleGrid.SELECT);
            if (flt.getQuery() == null || flt.getQuery().trim().length() == 0) {
                GeneralFrame.errMessageBox("Attention!", "The empty filter couldn't be applied");
            } else {
                newSelect = adjustLikeExpression(newSelect);
//                System.out.println("!!!newSelect:" + newSelect);
                peoplePanel.setSelect(newSelect);
                peoplePanel.refresh();
                gotoFilterApplied(flt.getFilterId().intValue());
            }
        }
    }

    @Override
    protected String getMainTableName() {
        return "people";
    }

    @Override
    protected ActionListener getChooseFilterAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!dontFilter) {
                    ComboItem itm = (ComboItem) filtersCB.getSelectedItem();
                    if (itm.getId() < 0) {//filtersCB.getSelectedIndex() == 0) {
                        if (itm.getId() == AIBclient.MULTI_COMPANY_PERSON) {
                            peoplePanel.setSelect(PeopleGrid.SELECT.replace("from people ",
                                    "from people where people_id in (select people_id from peoplecompany group by people_id having count(*)>1)"));
                        } else if(itm.getId() == AIBclient.DUPLICATED) {
                            peoplePanel.setSelect(PeopleGrid.SELECT.replace(
                                    "from people order by people.lastedit_date desc,people.first_name",
                                    "from people where (first_name,last_name) in "
                                            + "(select first_name,last_name "
                                            + "from people group by first_name,last_name "
                                            + "having count(*)>1) order by people.first_name,people.last_name"));
                            //System.out.println("!!"+peoplePanel.getSelect());
                        } else {
                            peoplePanel.setSelect(PeopleGrid.SELECT);
                        }
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
                peoplePanel.refresh();
            }
        };
    }

    private String adjustLikeExpression(String newSelect) {
        int p = newSelect.indexOf(LIKE);
        int c;
        while (p >= 0) {
            c = newSelect.indexOf("'", p);
            if (c >= 0) {
                c = newSelect.indexOf("'", c + 1);
                if (c >= 0 && newSelect.charAt(c - 1) != '%') {
                    String rest = newSelect.substring(c);
                    newSelect = newSelect.substring(0, c) + '%' + rest;
                }
            }
            p = newSelect.indexOf(LIKE, p + 1);
        }
        return newSelect;
    }
}
