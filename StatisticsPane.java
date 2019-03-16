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
 * This class will show statistics for all properties in the currently selected borough
 * and price range. TODO: implement funcionality
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class StatisticsPane extends BorderPane
{
    // whether the pane should be visible or not
    private boolean isActivated;
    // the current state of the data from the HashMap
    private AirbnbDataMap data;
    private int minPrice, maxPrice;
    
    /**
     * Constructor for objects of class StatisticsPane
     */
    public StatisticsPane()
    {
        // by default it should be seen - until a borough is specified
        isActivated = false;
        
        //placeholder image:
        Image image = new Image(getClass().getResourceAsStream("img/3.png"));
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
     * Updates the statics
     * Invoked when a new price range or a new borough has been selected
     * TODO: implement
     * 
     * @param min the minimum price from the RangeSlider
     * @param max the maximum price from the RangeSlider
     * @param data the current state of the data from the HashMaps
     */
    public void updateStatistics(int min, int max, AirbnbDataMap data)
    {
        this.data=data;
        minPrice = min;
        maxPrice = max;
    }
}
