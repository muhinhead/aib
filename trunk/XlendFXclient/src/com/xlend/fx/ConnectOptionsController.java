/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlend.fx;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author nick
 */
public class ConnectOptionsController extends GeneralController implements Initializable, ControlledScreen {

//    private ScreensController myController;
    
    @FXML
    TextField pathField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//        System.out.println("initialize of ConnectOptionsController");
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    @FXML
    private void gotoLoginScreen(ActionEvent event) {
        myController.setScreen(XlendFXclient.screenLoginID);
    }

    @FXML
    private void chooseDocPath(ActionEvent event) {

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Default document path");
        File selectedDirectory = chooser.showDialog(null);
        if (selectedDirectory != null) {
            pathField.setText(selectedDirectory.getPath());
        }
    }
}
