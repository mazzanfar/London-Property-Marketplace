//TODO: sort out imports
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
 * This class handles the selection of a borough from the user
 * It has a georgraphically accurate representation of boroughs and 
 * draws a heat map based on properties available in each of them.
 * Upon selection, updates the currentlySelected borough in the data
 * and alerts that other panes need to get updated accordingly.
 * Upon initial selection, the StatisticsPane and MapPane get activated
 * and are now visible. 
 * 
 * This pane is part of the panes in the main window. Extends BorderPane, 
 * adding more functionality
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class BoroughsPane extends BorderPane
{
    // the pane becomes active only after a price range is selected
    private boolean isActivated;
    // a reference to the class that controls all the panes
    // from the window (so they can be updated when needed)
    private MainPanes referenceToOtherPanes;
    // stores the current state of the data HashMap
    private AirbnbDataMap data;
    // the min and max selected prices from the RangeSlider
    private int minPrice, maxPrice;
    
    /**
     * Constructor for objects of class BoroughsPane
     */
    public BoroughsPane()
    {
        isActivated = false;
        
        // placeholder image:
        Image image = new Image(getClass().getResourceAsStream("img/boroughs.png"));
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
     * A new price range has been selected, the pane needs to be updated
     * accordingly
     * 
     * TODO: implement properly;
     * 
     * @param min the minimum price of properties to be considered
     * @param max the maximum price of properties to be considered
     * @param reference pointer to the class that controls other panes
     * @param data the current state of the data HashMap
     */
    public void updatePriceRange(int min, int max, MainPanes reference, AirbnbDataMap data)
    {
        this.data = data;
        referenceToOtherPanes = reference;
        minPrice = min;
        maxPrice = max;
        // update the heat map of the boroughs based on the new availabilities
        updateView();
    }
    
    /**
     * Updates the heat map of boroughs based on availability of properties
     * in the currently selected price range.
     * 
     * TODO: implement
     */
    private void updateView()
    {
        // this method is to be called when a borough is selected
        // it is here for testing purposes
        // commenting it should make some panes unavailable
        setSelectedBorough("Lambeth"); 
    }
    
    /**
     * Alrets the class controlling the other panes that
     * the selected borough has changed so that the other panes can
     * be updated accordingly.
     * 
     * @param name the name of the newly selected borough
     */
    private void setSelectedBorough(String name) 
    {
        referenceToOtherPanes.selectBorough(name);
    }
}
