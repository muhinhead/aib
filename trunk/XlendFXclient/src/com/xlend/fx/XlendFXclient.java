/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlend.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author nick
 */
public class XlendFXclient extends Application {

    public static String screenLoginID = "Login";
    public static String screenLoginFile = "Login.fxml";
    public static String screenConnectOptionsID = "ConnectOptions";
    public static String screenConnectOptionsFile = "ConnectOptions.fxml";
    public static String screenDashBoardID = "DashBoard";
    public static String screenDashBoardFile = "DashBoard.fxml";

    @Override
    public void start(Stage stage) throws Exception {

        ScreensController mainController = new ScreensController();
        mainController.loadScreen(screenLoginID, screenLoginFile);
        mainController.loadScreen(screenConnectOptionsID, screenConnectOptionsFile);
        mainController.loadScreen(screenDashBoardID, screenDashBoardFile);
        
        mainController.setScreen(screenLoginID);
        
        Group root = new Group();
        root.getChildren().addAll(mainController);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}