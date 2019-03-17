
//TODO: sort imports
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.controlsfx.control.RangeSlider;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/**
 * obvious
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class WelcomePane extends BorderPane
{
    /**
     * Constructor for objects of class WelcomePane
     */
    public WelcomePane()
    {
        setPrefHeight(500);
        setMinHeight(500);
        setMaxHeight(500);
        setPrefWidth(700);
        setMinWidth(700);
        setMaxWidth(700);
        
        //placeholder image:
        Image image = new Image(getClass().getResourceAsStream("img/1.png"));
        Label label1 = new Label();
        label1.setGraphic(new ImageView(image));
        
        setCenter(label1);
    }
}
