/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xlend.fx;

import java.util.HashMap;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

/**
 *
 * @author nick
 */
public class GeneralController {
    protected static final double DURATION = 200; // время анимации в мс
    protected static final double SCALE = 1.5; // коэффициент увеличения
//    protected ScaleTransition animationGrow = null;
//    protected ScaleTransition animationShrink = null;
    @FXML
    protected MenuItem closeItem;
    protected ScreensController myController;
    protected HashMap<Node, ScaleTransition> growAnimations = new HashMap<>();
    protected HashMap<Node, ScaleTransition> shrinkAnimations = new HashMap<>();

    /**
     * @return the animationGrow
     */
    public ScaleTransition getAnimationGrow(Node node) {
        ScaleTransition trans = growAnimations.get(node);
        if (trans==null) {
             trans = new ScaleTransition(new Duration(DURATION), node);
             trans.setToX(SCALE);
             trans.setToY(SCALE);
             growAnimations.put(node, trans);
        }
        return trans;
    }

    /**
     * @return the animationShrink
     */
    public ScaleTransition getAnimationShrink(Node node) {
        ScaleTransition trans = shrinkAnimations.get(node);
        if (trans==null) {
             trans = new ScaleTransition(new Duration(DURATION), node);
             trans.setToX(1);
             trans.setToY(1);
             shrinkAnimations.put(node, trans);
        }
        return trans;
    }

    @FXML
    protected void handleMouseEnter(MouseEvent event) {
        Node node = (Node) event.getSource();
        node.toFront();
        getAnimationShrink(node).stop();
        getAnimationGrow(node).playFromStart();
    }

    @FXML
    protected void handleMouseExit(MouseEvent event) {
        Node node = (Node) event.getSource();
        getAnimationGrow(node).stop();
        getAnimationShrink(node).playFromStart();
    }
    
}
