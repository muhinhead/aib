/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.filter;

import com.aib.AIBclient;
import com.aib.RecordEditPanel;
import com.aib.lookup.CountryLookupAction;
import com.aib.orm.Country;
import com.aib.orm.Filter;
import com.aib.orm.dbobject.ComboItem;
import com.xlend.util.SelectedDateSpinner;
import com.xlend.util.Util;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Types;
import java.text.SimpleDateFormat;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Nick Mukhin
 */
public abstract class FilterComponent extends JPanel {

    static final String BETWEENDATES_FLD = "betweenDatesFieldPanel";
    static final String BETWEEN_FLD = "betweenFieldPanel";
    static final String BETWEEN_STR = "BETWEEN";
    static final String DATETIME_FLD = "dateTimeFieldPanel";
    static final String DATE_FLD = "dateFieldPanel";
    static final String DECIMAL_FLD = "decomalFieldPanel";
    static final String IN = "IN";
    static final String INT_FLD = "intFieldPanel";
    static final String EQUALS = "=";
    static final String NOT_EQUALS = "!=";
    static final String GREATER = ">";
    static final String LESS = "<";
    static final String GREATER_EQ = ">=";
    static final String LESS_EQ = "<=";
    static final String LIKE = "LIKE";
    static final String IS_NULL = "IS NULL";
    static final String IS_NOT_NULL = "IS NOT NULL";
    static final String TEXT_FLD = "textFieldPanel";
    static final String COUNTRY_ID = "countryIDpanel";
    static final String LASTEDITED_BY = "editedBYpanel";
    static final String COMPANIES = "companiesPanel";
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    protected CardLayout cl;
    protected SelectedDateSpinner fromDateSP;
    protected JTextField fromValueTF;
    protected SelectedDateSpinner toDateSP;
    protected JTextField toValueTF;
    protected JPanel valuePanel;
    protected JTextField valueTF;
    protected IFilterPanel parentPanel;
    protected JComboBox operatorCB;
    protected JComboBox countryCB;
    protected JComboBox userCB;
    protected JComboBox companyFilterCB;
    private JLabel betwAndLabel;
    private boolean wasCountryIDselected = false;
    private boolean wasUserSelected = false;
    private boolean wasCompaniesSelected = false;
    private static ComboItem[] countries;
    private static ComboItem[] users;
    private static ComboItem[] companyFilters;

    static {
        Country[] countryList = AIBclient.loadAllCountries();
        countries = new ComboItem[countryList.length];
        int i = 0;
        for (Country c : countryList) {
            countries[i++] = new ComboItem(c.getCountryId(),
                    c.getCountry() + " (" + c.getShortname() + ")");
        }
        Filter[] filters = AIBclient.loadCompanyFilters();
        companyFilters = new ComboItem[filters.length];
        for (i=0; i<filters.length; i++) {
            companyFilters[i] = new ComboItem(filters[i].getFilterId(), 
                    filters[i].getName());
        }
        users = AIBclient.loadAllUsersInitials();
    }

    public FilterComponent(IFilterPanel parentPanel) {
        super(new BorderLayout());
        this.parentPanel = parentPanel;
        if (this instanceof ColumnFilterComponent) {
            operatorCB = new JComboBox(new String[]{
                EQUALS,
                NOT_EQUALS,
                GREATER,
                LESS,
                GREATER_EQ,
                LESS_EQ,
                LIKE,
                IN,
                BETWEEN_STR,
                IS_NOT_NULL,
                IS_NULL
            });
        } else {
//            operatorCB = new JComboBox(new String[]{
//                EQUALS,
//                NOT_EQUALS,
//                GREATER,
//                LESS,
//                GREATER_EQ,
//                LESS_EQ,
//                LIKE,
//                IN,
//                BETWEEN_STR,
//                IS_NULL
//            });
            operatorCB = new JComboBox();
            setDefaultOperatorSet();
        }
    }

    private void setDefaultOperatorSet() {
        operatorCB.setModel(new DefaultComboBoxModel(new String[]{
            EQUALS,
            NOT_EQUALS,
            GREATER,
            LESS,
            GREATER_EQ,
            LESS_EQ,
            LIKE,
            IN,
            BETWEEN_STR,
            IS_NULL
        }));
    }

    protected JPanel getValuePanel() {
        if (valuePanel == null) {
            valuePanel = new JPanel(cl = new CardLayout());
            valuePanel.add(valueTF = new JTextField(), TEXT_FLD);
            valuePanel.add(RecordEditPanel.getBorderPanel(new JComponent[]{
                fromValueTF = new JTextField(10),
                new JLabel("and", SwingConstants.CENTER),
                toValueTF = new JTextField(10)
            }), BETWEEN_FLD);
            valuePanel.add(RecordEditPanel.getBorderPanel(new JComponent[]{
                fromDateSP = new SelectedDateSpinner(),
                betwAndLabel = new JLabel("and", SwingConstants.CENTER),
                toDateSP = new SelectedDateSpinner()
            }), BETWEENDATES_FLD);
            valuePanel.add(RecordEditPanel.comboPanelWithLookupBtn(
                    countryCB = new JComboBox(new DefaultComboBoxModel(countries)),
                    new CountryLookupAction(countryCB, true)),
                    COUNTRY_ID);
            valuePanel.add(userCB = new JComboBox(
                    new DefaultComboBoxModel(users)),
                    LASTEDITED_BY);
            valuePanel.add(companyFilterCB = new JComboBox(
                    new DefaultComboBoxModel(companyFilters)),COMPANIES);
            valueTF.addKeyListener(getKeyAdapter());
            fromValueTF.addKeyListener(getKeyAdapter());
            toValueTF.addKeyListener(getKeyAdapter());
            countryCB.addActionListener(getCbListener());
            userCB.addActionListener(getCbListener());
            companyFilterCB.addActionListener(getCbListener());
            fromDateSP.setEditor(new JSpinner.DateEditor(fromDateSP, RecordEditPanel.DD_MM_YYYY));
            Util.addFocusSelectAllAction(fromDateSP);
            toDateSP.setEditor(new JSpinner.DateEditor(toDateSP, RecordEditPanel.DD_MM_YYYY));
            Util.addFocusSelectAllAction(toDateSP);
            valuePanel.add(new JPanel(), IS_NULL);
        }
        return valuePanel;
    }

    protected KeyAdapter getKeyAdapter() {
        return new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                touchParent();
            }
        };
    }

    protected AbstractAction getCbListener() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                touchParent();
            }
        };
    }

    protected void touchParent() {
        if (parentPanel != null) {
            parentPanel.setChanged(true);
        }
    }

    protected void selectControls2show(String fld, Integer tp) {
        if (fld.equals("country_id")) {
            if (!wasCountryIDselected) {
                operatorCB.setModel(new DefaultComboBoxModel(new String[]{
                    EQUALS,
                    NOT_EQUALS
                }));
                wasCountryIDselected = true;
            }
            cl.show(valuePanel, COUNTRY_ID);
        } else if (fld.equals("lastedited_by")) {
            if (!wasUserSelected) {
                operatorCB.setModel(new DefaultComboBoxModel(new String[]{
                    EQUALS,
                    NOT_EQUALS
                }));
                wasUserSelected = true;
            }
            cl.show(valuePanel, LASTEDITED_BY);
        } else if (fld.equals("Companies")) {
            if (!wasCompaniesSelected) {
                operatorCB.setModel(new DefaultComboBoxModel(new String[]{
                    IN
                }));
                wasCompaniesSelected = true;
            }
            cl.show(valuePanel, COMPANIES);
        } else {
            if (wasCountryIDselected || wasUserSelected || wasCompaniesSelected) {
                setDefaultOperatorSet();
            }
            wasCountryIDselected = false;
            wasUserSelected = false;
            if (operatorCB.getSelectedItem().equals(BETWEEN_STR)) {
                if (tp != null) {
                    switch (tp.intValue()) {
                        case Types.DATE:
                        case Types.TIMESTAMP:
                            cl.show(valuePanel, BETWEENDATES_FLD);
                            betwAndLabel.setVisible(true);
                            toDateSP.setVisible(true);
                            break;
                        default:
                            cl.show(valuePanel, BETWEEN_FLD);
                            break;
                    }
                } else {
                    cl.show(valuePanel, BETWEEN_FLD);
                }
            } else if (operatorCB.getSelectedItem().equals(IS_NULL) || operatorCB.getSelectedItem().equals(IS_NOT_NULL)) {
                cl.show(valuePanel, IS_NULL);
            } else {
                if (tp.intValue() == Types.DATE || tp.intValue() == Types.TIMESTAMP) {
                    cl.show(valuePanel, BETWEENDATES_FLD);
                    betwAndLabel.setVisible(false);
                    toDateSP.setVisible(false);
                } else {
                    cl.show(valuePanel, TEXT_FLD);
                }
            }
        }
    }

    protected static String removeBraces(String s) {
        if (s.startsWith("(") && s.endsWith(")")) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    protected static String removeQuotes(String s) {
        if (s.trim().startsWith("'")) {
            s = s.trim().substring(1);
        }
        if (s.trim().endsWith("'")) {
            s = s.trim().substring(0, s.trim().length() - 1);
        }
        return s;
    }

    protected static String replicate(char ch, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }
}
