/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib.admin;

import com.aib.AIBclient;
import com.aib.GeneralFrame;
import com.aib.GeneralGridPanel;
import com.aib.MyJideTabbedPane;
import com.aib.RecordEditPanel;
import com.aib.orm.Reportform;
import com.aib.orm.Reportformitem;
import com.aib.orm.dbobject.DbObject;
import com.aib.orm.dbobject.ForeignKeyViolationException;
import com.aib.remote.IMessageSender;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author Nick Mukhin
 */
public class AdminsFrame extends GeneralFrame {

    private static HashMap<String, Object[]> tabCols = new HashMap<String, Object[]>();
    private static HashMap<String, Object[]> selCols = new HashMap<String, Object[]>();
    private ArrayList<String> toDelete = new ArrayList<String>();
    private GeneralGridPanel usersPanel;
    private static String[] sheetList = new String[]{
        "Users list", "Output templates editor"
    };
    private JTree leftTree;
    private DefaultMutableTreeNode companyNode;
    private DefaultMutableTreeNode peopleNode;
    private DefaultMutableTreeNode locationNode;
//    private JButton addOneBtn;
//    private JButton addAllBtn;
//    private JButton delOneBtn;
//    private JButton delAllBtn;
    private JList columnList;
    private JList selectedList;
    private DefaultListModel selectedListModel;
    private DefaultListModel columnListModel;
    private JTextField templNameTF;
    private DefaultTreeModel treeModel;
//    private JButton downBtn;
//    private JButton upBtn;
    private JButton addTemplateBtn;
    private JButton delTemplateBtn;
    private DefaultMutableTreeNode root;
    private JTextArea descrArea;
    private boolean wasEdited = false;
    private JButton saveAllBtn;

    private class ReportFormListItem {

        private final Reportform repForm;

        ReportFormListItem(Reportform form) {
            this.repForm = form;
        }

        ReportFormListItem(String name, String descr, String tabName) throws ForeignKeyViolationException, SQLException {
            this.repForm = new Reportform(null);
            repForm.setReportformId(0);
            repForm.setName(name);
            repForm.setDescr(descr);
            repForm.setTablename(tabName.toLowerCase());
        }

        @Override
        public String toString() {
            return getRepForm().getName();
        }

        /**
         * @return the repForm
         */
        public Reportform getRepForm() {
            return repForm;
        }
    }

    public AdminsFrame(IMessageSender exch) {
        super("Setup", exch);
    }

    @Override
    protected String[] getSheetList() {
        return sheetList;
    }

    @Override
    protected void buildMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu m = createMenu("File", "File Operations");
        JMenuItem mi = createMenuItem("Connection setup", "Server connection parameters");
        mi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                AIBclient.configureConnection();
            }
        });
        m.add(mi);
        mi = createMenuItem("Hide", "Hide this window");
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        m.add(mi);
        bar.add(m);
        setJMenuBar(bar);
    }

    @Override
    protected JTabbedPane buildMainPanel() {
        MyJideTabbedPane workTab = new MyJideTabbedPane();
        workTab.add(getUserListPanel(), sheetList[0]);
        workTab.add(getOutputEditorPanel(), sheetList[1]);
        return workTab;
    }

    private JPanel getUserListPanel() {
        if (usersPanel == null) {
            try {
                registerGrid(usersPanel = new UsersGrid(getExchanger()));
            } catch (RemoteException ex) {
                AIBclient.logAndShowMessage(ex);
            }
        }
        return usersPanel;
    }

    private JPanel getOutputEditorPanel() {
        JSplitPane sp = new JSplitPane();
        JPanel shell = new JPanel(new BorderLayout());
        sp.setTopComponent(new JScrollPane(getTree()));
        sp.setBottomComponent(getRepEditor());
        shell.add(sp);
        reloadAll();
        return shell;
    }

    private JTree getTree() {
        leftTree = new JTree();
        root = new DefaultMutableTreeNode("Tables", true);
        fillSubCategories(root);
        leftTree.setModel(treeModel = new DefaultTreeModel(root));
        leftTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent tse) {
                int i;
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tse.getPath().getLastPathComponent();
                if (node != null) {
                    addTemplateBtn.setEnabled(node.getLevel() == 1);
                    delTemplateBtn.setEnabled(node.getLevel() == 2);
                }
                String nodePath = getCurrentNodePath();
                Object[] cols = getColNames(nodePath,
                        tse.getPath().getLastPathComponent().toString().toLowerCase());
                columnListModel.removeAllElements();
                selectedListModel.removeAllElements();
                if (cols != null) {
                    i = 0;
                    for (Object o : cols) {
                        columnListModel.add(i++, o);
                    }
                }
                cols = selCols.get(nodePath);
                if (cols != null) {
                    i = 0;
                    for (Object o : cols) {
                        selectedListModel.add(i++, o);
                    }
                }
                descrArea.setText(null);
                if (tse.getPath().getParentPath() != null && tse.getPath().getParentPath().getParentPath() != null) {
                    templNameTF.setText(tse.getPath().getLastPathComponent().toString());
                    if (node.getUserObject() instanceof ReportFormListItem) {
                        ReportFormListItem ritm = (ReportFormListItem) node.getUserObject();
                        descrArea.setText(ritm.getRepForm().getDescr());
                    }
                } else {
                    templNameTF.setText(null);
                }
            }
        });
        leftTree.setExpandsSelectedPaths(true);
        return leftTree;
    }

    private void fillSubCategories(DefaultMutableTreeNode parent) {
        parent.add(companyNode = new DefaultMutableTreeNode("Company", true));
        parent.add(peopleNode = new DefaultMutableTreeNode("People", true));
        parent.add(locationNode = new DefaultMutableTreeNode("Location", true));
    }

    private JPanel getRepEditor() {
        JPanel repEditorPanel = new JPanel(new BorderLayout(10, 10));
        JPanel upperPanel = new JPanel(new BorderLayout(10, 10));
        JLabel tmplbl;
        JLabel descrlbl;
        JPanel lblPanel1 = new JPanel(new BorderLayout());
        JPanel btnPanel = new JPanel(new BorderLayout());
        JPanel lftBtnPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        btnPanel.add(lftBtnPanel, BorderLayout.WEST);
        lftBtnPanel.add(addTemplateBtn = new JButton(getAddTemplateAction("Add")));
        lftBtnPanel.add(delTemplateBtn = new JButton(getDelTemplateAction("Delete")));
        lftBtnPanel.add(saveAllBtn = new JButton(getSaveAllAction("Save all")));
        lftBtnPanel.add(new JButton(getReloadAllAction("Reload all")));
        addTemplateBtn.setEnabled(false);
        delTemplateBtn.setEnabled(false);

        lblPanel1.add(descrlbl = new JLabel("   Description:", SwingConstants.RIGHT),
                BorderLayout.NORTH);
        JComponent tmpNamePanel;
        upperPanel.add(tmpNamePanel = RecordEditPanel.getBorderPanel(new JComponent[]{
            tmplbl = new JLabel("   Template name:", SwingConstants.RIGHT),
            RecordEditPanel.getGridPanel(new JComponent[]{templNameTF = new JTextField(), btnPanel}),
            new JPanel()
        }), BorderLayout.NORTH);
        upperPanel.add(RecordEditPanel.getBorderPanel(new JComponent[]{
            lblPanel1,
            RecordEditPanel.getGridPanel(
            new JScrollPane(descrArea = new JTextArea(4, 20),
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), 2),
            new JPanel()
        }));
        descrArea.setWrapStyleWord(true);
        descrArea.setLineWrap(true);

        tmpNamePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        descrlbl.setPreferredSize(tmplbl.getPreferredSize());
        repEditorPanel.add(upperPanel, BorderLayout.NORTH);
        repEditorPanel.add(getTemplateEditorPanel(), BorderLayout.CENTER);
        templNameTF.addKeyListener(getKeyAdapter());
        descrArea.addKeyListener(getKeyAdapter());
        return repEditorPanel;
    }

    private KeyAdapter getKeyAdapter() {
        return new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                TreePath[] curPath = leftTree.getSelectionPaths();
                TreePath itm = curPath[curPath.length - 1];
                final DefaultMutableTreeNode node =
                        (DefaultMutableTreeNode) curPath[curPath.length - 1].getLastPathComponent();
                final String prevName = getCurrentNodePath();
                final Object[] cols = tabCols.get(prevName);
                final Object[] selected = selCols.get(prevName);
                if (node.getLevel() == 2) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            if (node.getUserObject() instanceof ReportFormListItem) {
                                ReportFormListItem ritm = (ReportFormListItem) node.getUserObject();
                                try {
                                    ritm.getRepForm().setName(templNameTF.getText());
                                    ritm.getRepForm().setDescr(descrArea.getText());
                                } catch (Exception ex) {
                                    AIBclient.log(ex);
                                }
                            } else {
                                node.setUserObject(templNameTF.getText());
                            }
                            String cur = getCurrentNodePath();
                            tabCols.remove(prevName);
                            tabCols.put(cur, cols);
                            selCols.remove(prevName);
                            selCols.put(cur, selected);
                            leftTree.updateUI();
                            touch();
                        }
                    });
                }
            }
        };
    }

    private JPanel getTemplateEditorPanel() {
        JPanel templateEditorPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JPanel lftCpanel = new JPanel(new BorderLayout(10, 20));
        JScrollPane sp1;
        lftCpanel.add(sp1 = new JScrollPane(columnList = new JList(columnListModel = new DefaultListModel())), BorderLayout.CENTER);
        columnList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel btnPanel = new JPanel(new BorderLayout());
        JPanel upperBtnPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        btnPanel.add(upperBtnPanel, BorderLayout.NORTH);
        upperBtnPanel.add(new JButton(getAddOneAction(">")));
        upperBtnPanel.add(new JButton(getAddAllAction(">>")));
        upperBtnPanel.add(new JButton(getRemoveOneAction("<")));
        upperBtnPanel.add(new JButton(getRemoveAllAction("<<")));
        upperBtnPanel.add(new JButton(getMoveUpAction("^")));
        upperBtnPanel.add(new JButton(getMoveDownAction("v")));
        lftCpanel.add(btnPanel, BorderLayout.EAST);
        templateEditorPanel.add(lftCpanel);
        sp1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Available columns"));
        JScrollPane sp2;
        templateEditorPanel.add(sp2 = new JScrollPane(selectedList = new JList(selectedListModel = new DefaultListModel())),
                BorderLayout.EAST);
        selectedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sp2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Selected columns"));
        templateEditorPanel.setBorder(BorderFactory.createEmptyBorder(1, 20, 15, 20));
        return templateEditorPanel;
    }

    private Object[] getColNames(String pathString, String tableName) {
        Object[] columnList = tabCols.get(pathString);
        if (columnList == null) {
            Object[] obs = null;
            try {
                obs = AIBclient.getExchanger().getColNamesTypes(
                        "select * from " + tableName.toLowerCase() + " where " + tableName + "_id<0");
                ArrayList<String> colNames = (ArrayList<String>) obs[0];
                colNames.remove(tableName.toLowerCase() + "_id");
                colNames.add("Countries");
                colNames.add("Links");
                colNames.add("Industries");
                if (tableName.equalsIgnoreCase("people")) {
                    colNames.add("Companies");
                }
                columnList = colNames.toArray();
                tabCols.put(pathString, columnList);
            } catch (RemoteException ex) {
                columnList = new Object[]{};
            }
        }
        columnList = (columnList == null ? new Object[]{} : columnList);
        return columnList;
    }

    private static boolean targetContains(JList trg, String colName) {
        DefaultListModel trgModel = (DefaultListModel) trg.getModel();
        for (Object o : trgModel.toArray()) {
            if (o.equals(colName)) {
                return true;
            }
        }
        return false;
    }

    private boolean moveItem(String pathString, JList src, JList trg) {
        int p = src.getSelectedIndex();
        String colName = (String) src.getSelectedValue();
        if (colName != null && !targetContains(trg, colName)) {
            int pos = trg.getSelectedIndex();
            DefaultListModel trgModel = (trg == columnList ? columnListModel : selectedListModel);
            trgModel.add(pos + 1, colName);
            DefaultListModel srcModel = (src == columnList ? columnListModel : selectedListModel);
            srcModel.remove(p);
            p = srcModel.getSize() > p ? p : srcModel.getSize() - 1;
            if (p >= 0) {
                src.setSelectedIndex(p);
            }

            if (trg == selectedList) {
                tabCols.put(pathString, srcModel.toArray());
                selCols.put(pathString, trgModel.toArray());
            } else {
                selCols.put(pathString, srcModel.toArray());
                tabCols.put(pathString, trgModel.toArray());
            }
            touch();
            return true;
        }
        return false;
    }

    private String getCurrentNodePath() {
        TreePath[] curPath = leftTree.getSelectionPaths();
        if (curPath != null) {
            TreePath itm = curPath[curPath.length - 1];
            if (itm != null) {
                String tabPrefix = "Tables/";
                String path = itm.toString().replace(", ", "/").replace("[", "").replace("]", "");
                path = path.startsWith(tabPrefix) ? path : tabPrefix + path;
                return path;
            }
        }
        return "";
    }

    private AbstractAction getAddOneAction(String label) {
        return new AbstractAction(label) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                moveItem(getCurrentNodePath(), columnList, selectedList);
            }
        };
    }

    private AbstractAction getRemoveOneAction(String label) {
        return new AbstractAction(label) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                moveItem(getCurrentNodePath(), selectedList, columnList);
            }
        };
    }

    private AbstractAction getAddAllAction(String label) {
        return new AbstractAction(label) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                columnList.setSelectedIndex(columnListModel.toArray().length - 1);
                while (moveItem(getCurrentNodePath(), columnList, selectedList));
            }
        };
    }

    private AbstractAction getRemoveAllAction(String label) {
        return new AbstractAction(label) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                selectedList.setSelectedIndex(selectedListModel.toArray().length - 1);
                while (moveItem(getCurrentNodePath(), selectedList, columnList));
            }
        };
    }

    private static Reportform searchTemplate(DbObject[] templates, String tabName, String name)
            throws ForeignKeyViolationException, SQLException {
        Reportform rf;
        for (DbObject o : templates) {
            rf = (Reportform) o;
            if (rf.getTablename().equals(tabName) && rf.getName().equals(name)) {
                return rf;
            }
        }
        rf = new Reportform(null);
        rf.setNew(true);
        rf.setReportformId(0);
        rf.setTablename(tabName);
        rf.setName(name);
        rf.setOwnerId(AIBclient.getCurrentUser().getUserId());
        return rf;
    }

    private void reloadAll() {
        selCols.clear();
        tabCols.clear();
        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode tableTemplates = (DefaultMutableTreeNode) root.getChildAt(i);
            selectNode(tableTemplates);
            String key = getCurrentNodePath();
            tableTemplates.removeAllChildren();
            TreePath papaPath;
            leftTree.setSelectionPath(papaPath = new TreePath(tableTemplates));
            String tabName = tableTemplates.getUserObject().toString();
            try {
                DbObject[] templates = AIBclient.getExchanger().getDbObjects(
                        Reportform.class, "tablename='" + tabName + "'", null);
                for (DbObject o : templates) {
                    Reportform rf = (Reportform) o;
                    DefaultMutableTreeNode node;
                    tableTemplates.add(node = new DefaultMutableTreeNode(new ReportFormListItem(rf)));
                    selectNode(node);
                    Object[] itemList = loadItems(rf.getReportformId());
                    selCols.put(getCurrentNodePath(), itemList);
                    tabCols.put(getCurrentNodePath(), diffArray(tabCols.get(key), itemList));
                }
            } catch (RemoteException ex) {
                AIBclient.logAndShowMessage(ex);
            }
        }
        leftTree.setSelectionRow(0);
        leftTree.updateUI();
        setWasEdited(false);
    }

    private AbstractAction getReloadAllAction(String label) {
        return new AbstractAction(label) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                reloadAll();
            }
        };
    }

    private Object[] loadItems(int reportform_id) throws RemoteException {
        DbObject[] itms = AIBclient.getExchanger().getDbObjects(
                Reportformitem.class, "reportform_id=" + reportform_id, null);
        Object[] ans = new Object[itms.length];
        for (int i = 0; i < itms.length; i++) {
            Reportformitem ri = (Reportformitem) itms[i];
            ans[i] = ri.getColumnname();
        }
        return ans;
    }

    private AbstractAction getSaveAllAction(String label) {
        return new AbstractAction(label) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                saveAll();
            }
        };
    }

    private void saveAll() {
        DbObject[] templates;
        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode tableTemplates = (DefaultMutableTreeNode) root.getChildAt(i);
            try {
                String tabName = tableTemplates.getUserObject().toString();
                templates = AIBclient.getExchanger().getDbObjects(
                        Reportform.class, "tablename='" + tabName + "'", null);
                for (int j = 0; j < tableTemplates.getChildCount(); j++) {
                    DefaultMutableTreeNode template =
                            (DefaultMutableTreeNode) tableTemplates.getChildAt(j);
                    Reportform rf;
                    if (template.getUserObject() instanceof ReportFormListItem) {
                        ReportFormListItem ritm = (ReportFormListItem) template.getUserObject();
                        rf = ritm.getRepForm();
                        if (rf.getOwnerId() == null || rf.getOwnerId().intValue() == 0) {
                            rf.setOwnerId(AIBclient.getCurrentUser().getUserId());
                        }
                    } else {
                        rf = searchTemplate(
                                templates, tabName, template.getUserObject().toString());
                    }
                    rf = (Reportform) AIBclient.getExchanger().saveDbObject(rf);

                    selectNode(template);
                    saveRepItems(rf.getReportformId(), selectedListModel.toArray());
                }
            } catch (Exception ex) {
                AIBclient.logAndShowMessage(ex);
            }
        }
        if (toDelete.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (String val : toDelete) {
                sb.append(sb.length() > 0 ? ",'" : "'").append(val).append("'");
            }
            try {
                templates = AIBclient.getExchanger().getDbObjects(Reportform.class,
                        "concat(tablename,'/',name) in (" + sb.toString() + ")", null);
                for (DbObject o : templates) {
                    AIBclient.getExchanger().deleteObject(o);
                }
                toDelete.clear();
            } catch (Exception ex) {
                AIBclient.logAndShowMessage(ex);
            }
        }
        setWasEdited(false);
    }

    private void saveRepItems(int reportform_id, Object[] cols)
            throws RemoteException, SQLException, ForeignKeyViolationException {
        DbObject[] obs;
        Reportformitem ri;
        StringBuilder sb = new StringBuilder("0");
        if (cols != null) {
            for (Object col : cols) {
                obs = AIBclient.getExchanger().getDbObjects(
                        Reportformitem.class, "reportform_id=" + reportform_id
                        + " and columnname='" + col + "'", null);
                if (obs.length == 0) {
                    ri = new Reportformitem(null);
                    ri.setReportformitemId(0);
                    ri.setReportformId(reportform_id);
                    ri.setColumnname((String) col);
                    ri.setHeader((String) col);
                    ri = (Reportformitem) AIBclient.getExchanger().saveDbObject(ri);
                } else {
                    ri = (Reportformitem) obs[0];
                }
                sb.append(sb.length() > 0 ? "," : "").append(ri.getReportformitemId());
            }
        }
        obs = AIBclient.getExchanger().getDbObjects(
                Reportformitem.class, "reportform_id=" + reportform_id + " and reportformitem_id not in (" + sb.toString() + ")", null);
        for (DbObject o : obs) {
            AIBclient.getExchanger().deleteObject(o);
        }
    }

    private AbstractAction getDelTemplateAction(String label) {
        return new AbstractAction(label) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String curpath = getCurrentNodePath();
                toDelete.add(curpath.substring(curpath.indexOf("/") + 1));
                TreePath[] curPath = leftTree.getSelectionPaths();
                TreePath itm = curPath[curPath.length - 1];
                DefaultMutableTreeNode node =
                        (DefaultMutableTreeNode) curPath[curPath.length - 1].getLastPathComponent();
                TreePath papaPath = itm.getParentPath();
                leftTree.setSelectionPath(papaPath);
                TreeNode papa = node.getParent();
                node.removeFromParent();
                leftTree.updateUI();
                touch();
            }
        };
    }

    private void selectNode(DefaultMutableTreeNode node) {
        if (node != null) {
            TreeNode[] nodes = treeModel.getPathToRoot(node);
            TreePath path = new TreePath(nodes);
            leftTree.setSelectionPath(new TreePath(nodes));
        }
    }

    private AbstractAction getAddTemplateAction(String label) {
        return new AbstractAction(label) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                TreePath[] curPath = leftTree.getSelectionPaths();
                String tmplName = templNameTF.getText().trim();
                String descr = descrArea.getText();
                if (curPath == null || curPath[curPath.length - 1].getParentPath() == null) {
                    GeneralFrame.errMessageBox("Attention!", "Select a table please");
                    leftTree.requestFocus();
                } else if (tmplName.length() == 0) {
                    GeneralFrame.errMessageBox("Attention!", "Enter the template name please");
                    templNameTF.requestFocus();
                } else {
                    try {
                        TreePath itm = curPath[curPath.length - 1];
                        while (itm.getParentPath().getParentPath() != null) {
                            itm = itm.getParentPath();
                        }
                        DefaultListModel colModel = (DefaultListModel) columnList.getModel();
                        DefaultListModel selModel = (DefaultListModel) selectedList.getModel();
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) itm.getLastPathComponent();
                        DefaultMutableTreeNode child;
                        node.add(child = new DefaultMutableTreeNode(
                                new ReportFormListItem(tmplName, descr, node.toString().toLowerCase())));
                        Object[] cols = colModel.toArray();
                        Object[] sels = selModel.toArray();
                        selectNode(child);
                        String key = getCurrentNodePath();
                        tabCols.put(key, cols);
                        selCols.put(key, sels);
                        selectNode(node);
                        selectNode(child);
                        leftTree.updateUI();
                        touch();
                    } catch (Exception ex) {
                        AIBclient.logAndShowMessage(ex);
                    }
                }
            }
        };
    }

    private AbstractAction getMoveUpAction(String label) {
        return new AbstractAction(label) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int pos = selectedList.getSelectedIndex();
                if (pos > 0) {
                    Object[] arr = selectedListModel.toArray();
                    selectedListModel.setElementAt(arr[pos], pos - 1);
                    selectedListModel.setElementAt(arr[pos - 1], pos);
                    selectedList.setSelectedIndex(pos - 1);
                    selectedList.revalidate();
                    selCols.put(getCurrentNodePath(), selectedListModel.toArray());
                    touch();
                }
            }
        };
    }

    private AbstractAction getMoveDownAction(String label) {
        return new AbstractAction(label) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int pos = selectedList.getSelectedIndex();
                Object[] arr = selectedListModel.toArray();
                if (pos < arr.length - 1) {
                    selectedListModel.setElementAt(arr[pos], pos + 1);
                    selectedListModel.setElementAt(arr[pos + 1], pos);
                    selectedList.setSelectedIndex(pos + 1);
                    selectedList.revalidate();
                    selCols.put(getCurrentNodePath(), selectedListModel.toArray());
                    touch();
                }
            }
        };
    }

    private Object[] diffArray(Object[] all, Object[] toexclude) {
        ArrayList result = new ArrayList(all.length);
        HashSet s = new HashSet();
        for (Object t : toexclude) {
            s.add(t);
        }
        for (Object a : all) {
            if (!s.contains(a)) {
                result.add(a);
            }
        }
        return result.toArray();
    }

    private void touch() {
        setWasEdited(true);
    }

    private void setWasEdited(boolean wasEdited) {
        this.wasEdited = wasEdited;
        saveAllBtn.setEnabled(wasEdited);
    }

    public void setVisible(boolean b) {
        if (wasEdited && !b) {
            if (GeneralFrame.yesNo("Attention!", "Do you want to save changes?") == JOptionPane.YES_OPTION) {
                saveAll();
            }
        }
        if (!wasEdited || b) {
            super.setVisible(b);
        }
    }
}
