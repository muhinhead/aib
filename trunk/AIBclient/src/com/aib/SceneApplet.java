/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aib;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javax.swing.JApplet;

/**
 *
 * @author nick
 */
public abstract class SceneApplet extends JApplet {
    private static int JFXPANEL_WIDTH_INT = 700;
    private static int JFXPANEL_HEIGHT_INT = 550;
    private static final double SCALE = 1.4; // коэффициент увеличения
    private static final double DURATION = 200; // время анимации в мс
//    private static final String[] btnTooltips = new String[]{
//        "Companies", "People", "Locations", "Setup" 
//    };
    
    protected JFXPanel fxContainer;
    protected BorderPane root;

    public SceneApplet() {
        super();
    }

    @Override
    public void init() {
        fxContainer = new JFXPanel();
        fxContainer.setPreferredSize(new Dimension(JFXPANEL_WIDTH_INT, JFXPANEL_HEIGHT_INT));
        add(fxContainer, BorderLayout.CENTER);
        // create JavaFX scene
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                createScene();
            }
        });
    }

    protected abstract void createScene();
//    private void createScene() {
//        root = new BorderPane();
//        root.setId("pane");
//        Scene dashBoardScene = new Scene(root, 900, 650, Color.DARKGRAY);
//        dashBoardScene.getStylesheets().addAll(this.getClass().getResource("stylesheet.css").toExternalForm());
//        fxContainer.setScene(dashBoardScene);
//        addSceneContent(root);
//    }

//    private void addSceneContent(BorderPane root) {
//        HBox taskbar = new HBox(20);
//        taskbar.setPadding(new Insets(0, 10, 70, 10));
//        taskbar.setPrefHeight(150);
//        taskbar.setAlignment(Pos.CENTER);
//        for (int i = 1; i <= 4; i++) {
//            Node node = createButton("images/icon-" + i + ".png", btnTooltips[i - 1]);
//            taskbar.getChildren().add(node);
//        }
//        root.setBottom(taskbar);
//    }
    
     protected Node createButton(String iconName, String toolTip, final Runnable action) {
        // загружаем картинку
        final ImageView node = new ImageView(new Image(getClass().getResourceAsStream(iconName)));

        // создаём анимацию увеличения картинки      
        final ScaleTransition animationGrow = new ScaleTransition(new Duration(DURATION), node);
        animationGrow.setToX(SCALE);
        animationGrow.setToY(SCALE);

        // и уменьшения
        final ScaleTransition animationShrink = new ScaleTransition(new Duration(DURATION), node);
        animationShrink.setToX(1);
        animationShrink.setToY(1);

        // обработчик нажатия мыши
        node.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                action.run();
            }
        });
        // при наведении курсора мы запускаем анимацию увеличения кнопки
        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                node.toFront();
                animationShrink.stop();
                animationGrow.playFromStart();
            }
        });
        // когда курсор сдвигается -- запускаем анимацию уменьшения
        node.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                animationGrow.stop();
                animationShrink.playFromStart();
            }
        });
        // добавляем эффект отражения
        final Reflection effect = new Reflection();
        node.setEffect(effect);
        Tooltip t = new Tooltip(toolTip);
        Tooltip.install(node, t);
        return node;
    }

}
