
//TODO: sort imports
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.controlsfx.control.RangeSlider;
import com.jfoenix.controls.JFXButton;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList; 
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.*;
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
            Label welcomeLabel1 = new Label("Welcome to the AirBnB London Property Marketplace!");
            welcomeLabel1.setWrapText(true);
            welcomeLabel1.getStyleClass().add("welcomeLabel1");
            Separator separator = new Separator();
            Label welcomeLabel2 = new Label("Please select a price range above to get started.");
            welcomeLabel2.getStyleClass().add("welcomeLabel2");
        VBox welcomeWrap = new VBox(welcomeLabel1, separator, welcomeLabel2);
        welcomeWrap.setMaxWidth(400);
        welcomeWrap.setPrefHeight(500);
        welcomeWrap.setAlignment(Pos.CENTER);
        
        setCenter(welcomeWrap);
    }
}
