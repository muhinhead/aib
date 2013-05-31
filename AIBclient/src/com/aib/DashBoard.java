/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import com.aib.remote.IMessageSender;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Window;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author nick
 */
public class DashBoard extends AbstractDashBoard {
    static Window ourInstance;

    private final IMessageSender exchanger;

    public DashBoard(String title, IMessageSender exchanger) {
        super(title);
        AIBclient.setWindowIcon(this, "aib.png");
        this.exchanger = exchanger;
        ourInstance = this;
        addWindowListener(new DashBoard.WinListener(this));
        setSize(new Dimension(550, 300));
        centerOnScreen();
        setVisible(true);
        setResizable(false);
    }

    @Override
    public void lowLevelInit() {
        AIBclient.readProperty("junk", ""); // just to init properties
    }

    @Override
    protected void fillControlsPanel() throws HeadlessException {
        getContentPane().setLayout(new BorderLayout(10, 10));
        SceneApplet sa = new DashBoardApplet();
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(AIBclient.HDR_COLOR);
        JLabel lbl = new JLabel(getTitle(), SwingConstants.CENTER);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 18));
        headerPanel.add(lbl);
        getContentPane().add(headerPanel, BorderLayout.NORTH);
        getContentPane().add(sa);
        sa.init();
    }
}
