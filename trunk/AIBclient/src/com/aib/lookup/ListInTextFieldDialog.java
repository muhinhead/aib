/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.lookup;

import com.aib.RecordEditPanel;
import com.xlend.util.Java2sAutoComboBox;
import com.xlend.util.PopupDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author nick
 */
public class ListInTextFieldDialog extends PopupDialog {

    private static String resultList;

    /**
     * @return the resultList
     */
    public static String getResultList() {
        return resultList;
    }
    private String oldList;
    private JButton okButton;
    private JButton cancelButton;
    private Java2sAutoComboBox urlField;
    private ArrayList selectedItems;
    private JList urlJList;
    private AbstractListModel listModel;
//    private JTextField tf;

    public ListInTextFieldDialog(String title, Object[] obs) {
        super(null, title, obs);
    }

    @Override
    protected Color getHederBackground() {
        return new Color(102, 125, 158);
    }

    @Override
    protected void fillContent() {
        super.fillContent();
        getContentPane().add(getCentralPanel());
        JPanel okCancelBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        okCancelBtnPanel.add(okButton = new JButton(new AbstractAction("Ok") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                syncTextField();
                dispose();
            }
        }));
        okCancelBtnPanel.add(cancelButton = new JButton(new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                resultList = oldList;
                dispose();
            }
        }));
        JPanel delClearBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        delClearBtnPanel.add(new JButton(getRemoveAction()));
        delClearBtnPanel.add(new JButton(getClearAllAction()));
        JPanel btnPanel = new JPanel(new GridLayout(1, 2));
        btnPanel.add(delClearBtnPanel);
        btnPanel.add(okCancelBtnPanel);
        getContentPane().add(btnPanel, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(okButton);
//        setMaximumSize(new Dimension(400, 600));
    }

    @Override
    public void freeResources() {
        //
    }

    private JPanel getCentralPanel() {
        Object[] params = (Object[]) getObject();
        oldList = resultList = (String) params[0];
        List linkList = (List) params[1];
        String prompt = (String) params[2];
        JPanel cPanel = new JPanel(new BorderLayout(5, 10));
        JPanel upperPanel = new JPanel(new BorderLayout(5, 5));
        upperPanel.add(new JLabel("   " + prompt), BorderLayout.WEST);
        upperPanel.add(urlField = new Java2sAutoComboBox(linkList));
        urlField.setStrict(false);
        upperPanel.add(RecordEditPanel.getBorderPanel(new JComponent[]{
            new JButton(getAddAction()), new JPanel(), new JPanel()
        }), BorderLayout.EAST);
        cPanel.add(upperPanel, BorderLayout.NORTH);

        selectedItems = new ArrayList();
        if (oldList.length() > 0) {
            for (Object url : linkList) {
                if (oldList.indexOf((String) url) >= 0) {
                    selectedItems.add(url);
                }
            }
        }

        listModel = new AbstractListModel() {
            @Override
            public int getSize() {
                return selectedItems.size();
            }

            @Override
            public Object getElementAt(int i) {
                return selectedItems.get(i);
            }
        };

        JScrollPane sp = new JScrollPane(urlJList = new JList(listModel));
        sp.setPreferredSize(new Dimension(400, 400));
        cPanel.add(sp, BorderLayout.CENTER);
        cPanel.add(new JPanel(), BorderLayout.EAST);
        cPanel.add(new JPanel(), BorderLayout.WEST);

        return cPanel;
    }

    private AbstractAction getAddAction() {
        return new AbstractAction("Add") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                boolean toAdd = true;
                String url = (String) urlField.getSelectedItem();
                if (url.trim().length() > 0) {
                    for (Object itm : selectedItems) {
                        if (itm.equals(url)) {
                            toAdd = false;
                        }
                    }
                    if (toAdd) {
                        if ((url = additionalDialog(url)) != null) {
                            selectedItems.add(url);
                            urlJList.updateUI();
                        }
                    }
                }
            }
        };
    }

    private void syncTextField() {
        StringBuilder sb = new StringBuilder();
        for (Object itm : selectedItems) {
            sb.append(sb.length() > 0 ? "," : "");
            sb.append(itm);
        }
        resultList = sb.toString();
    }

    protected String additionalDialog(String url) {
        //Implement additional dialog here
        return url;
    }

    private AbstractAction getClearAllAction() {
        return new AbstractAction("Clear") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                selectedItems.clear();
                urlJList.updateUI();
            }
        };
    }

    private AbstractAction getRemoveAction() {
        return new AbstractAction("Remove") {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int inds[] = urlJList.getSelectedIndices();
                for (int i = inds.length - 1; i >= 0; i--) {
                    selectedItems.remove(inds[i]);
                }
                urlJList.updateUI();
            }
        };
    }
}
