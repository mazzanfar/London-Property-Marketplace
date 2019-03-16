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
 * This class has to be implemented to show pins for all the properties
 * in the current burrow. Extends BorderPane.
 * TODO: implement
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class MapPane extends BorderPane
{
    // whehter the pane should be visible or not
    private boolean isActivated;
    // the data from the HashMap
    private AirbnbDataMap data;
    // current price range selected
    private int minPrice, maxPrice;
    
    /**
     * Constructor for objects of class MapPane
     */
    public MapPane()
    {
        // by default it shouldnt be visible
        isActivated = false;
        
        //placeholder image:
        Image image = new Image(getClass().getResourceAsStream("img/4.png"));
        Label label1 = new Label();
        label1.setGraphic(new ImageView(image));
        
        setCenter(label1);
    }

    /**
     * Indicate that the pane is activated.
     * Invoked when a price range from the RangeSlider is selected.
     */
    public void activate()
    {
        isActivated = true;
    }
    
    /**
     * If needed, deactivate the pane
     */
    public void deactivate()
    {
        isActivated = false;
    }
    
    /**
     * Check if the pane is active or not
     * @return true if the pane is active
     */
    public boolean isActivated()
    {
        return isActivated;
    }
    
    /**
     * Update what should be shown on the pane
     * Invoked when the price ranged has changed or a new borough has been selected
     * 
     * TODO: implement functionality.
     * 
     * @param min the minimum price selected from the RangeSlider
     * @param max the maximum price selected from the RangeSlider
     * @param data the current state of the HashMap
     */
    public void updateMap(int min, int max, AirbnbDataMap data)
    {
        this.data=data;
        minPrice = min;
        maxPrice = max;
    }
    
}
