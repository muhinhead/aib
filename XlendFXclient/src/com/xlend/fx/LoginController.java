/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlend.fx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author nick
 */
public class LoginController extends GeneralController implements Initializable, ControlledScreen {

    @FXML
    private void handleButtonAction(MouseEvent event) {
        myController.setScreen(XlendFXclient.screenDashBoardID);
    }

    @FXML
    private void gotoOptionsScreen(ActionEvent event) {
        myController.setScreen(XlendFXclient.screenConnectOptionsID);
    }

    @FXML
    private void onCloseItemAction(ActionEvent event) {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//        System.out.println("initialize of LoginController");
    }
}
