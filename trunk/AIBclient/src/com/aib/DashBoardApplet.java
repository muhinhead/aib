package com.aib;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javax.swing.UIManager;

/**
 *
 * @author Nick Mukhin
 */
public class DashBoardApplet extends SceneApplet {

    private static final String[] btnTooltips = new String[]{
        "Companies", "People", "Locations", "Setup"
    };
    private Runnable[] action = new Runnable[4];
    private CompanyFrame companyFrame;
    private PeopleFrame peopleFrame;
    private LocationsFrame locationsFrame;

    public DashBoardApplet() {
        super();
        action[0] = new Runnable() {
            @Override
            public void run() {
                if (companyFrame == null) {
                    companyFrame = new CompanyFrame(AIBclient.getExchanger());
                } else {
                    try {
                        companyFrame.setLookAndFeel(AIBclient.readProperty("LookAndFeel",
                                UIManager.getSystemLookAndFeelClassName()));
                    } catch (Exception ex) {
                    }
                    companyFrame.setVisible(true);
                }
            }
        };
        action[1] = new Runnable() {
            @Override
            public void run() {
                if (peopleFrame == null) {
                    peopleFrame = new PeopleFrame(AIBclient.getExchanger());
                } else {
                    try {
                        peopleFrame.setLookAndFeel(AIBclient.readProperty("LookAndFeel",
                                UIManager.getSystemLookAndFeelClassName()));
                    } catch (Exception ex) {
                    }
                    peopleFrame.setVisible(true);
                }
            }
        };
        action[2] = new Runnable() {
            @Override
            public void run() {
                if (locationsFrame == null) {
                    locationsFrame = new LocationsFrame(AIBclient.getExchanger());
                } else {
                    try {
                        locationsFrame.setLookAndFeel(AIBclient.readProperty("LookAndFeel",
                                UIManager.getSystemLookAndFeelClassName()));
                    } catch (Exception ex) {
                    }
                    locationsFrame.setVisible(true);
                }
            }
        };
        action[3] = new Runnable() {
            @Override
            public void run() {
                System.out.println("ACTION 4");
            }
        };
    }

    @Override
    protected void createScene() {
        root = new BorderPane();
        root.setId("pane");
        Scene dashBoardScene = new Scene(root, 900, 650, Color.DARKGRAY);
        dashBoardScene.getStylesheets().addAll(this.getClass().getResource("stylesheet.css").toExternalForm());
        fxContainer.setScene(dashBoardScene);
        addSceneContent(root);
    }

    private void addSceneContent(BorderPane root) {
        HBox taskbar = new HBox(20);
        taskbar.setPadding(new Insets(0, 10, 70, 10));
        taskbar.setPrefHeight(150);
        taskbar.setAlignment(Pos.CENTER);
        for (int i = 1; i <= 4; i++) {
            Node node = createButton("images/icon-" + i + ".png", btnTooltips[i - 1], action[i - 1]);
            taskbar.getChildren().add(node);
        }
        root.setBottom(taskbar);
    }
}
