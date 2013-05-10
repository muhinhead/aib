/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlend.fx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author nick
 */
public class DashBoardController extends GeneralController implements Initializable, ControlledScreen {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void gotoLoginScreen(MouseEvent event) {
        myController.setScreen(XlendFXclient.screenLoginID);
    }
}
