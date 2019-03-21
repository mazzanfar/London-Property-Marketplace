import javafx.scene.layout.BorderPane;

/**
 * This class extends the BorderPane javafx class, adding functionality
 * that all of the non-static panes (the ones that require change everytime
 * a new price range has been selected require).
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public abstract class ExtendedBorderPane extends BorderPane
{
    // whehter a pane should be visible or not
    private boolean isActivated;
    // a reference to the current state of the data HashMap
    protected AirbnbDataMap data;
    // current price range selected
    protected int minPrice, maxPrice;
    
    /**
     * Super constructor
     */
    public ExtendedBorderPane(AirbnbDataMap data)
    {
        // get the current state of the data HashMap
        this.data = data;
        
        // by default, the panes should not be active
        isActivated = false;
        
        //set the dimensions
        setPrefHeight(500);
        setMinHeight(500);
        setMaxHeight(500);
        setPrefWidth(700);
        setMinWidth(700);
        setMaxWidth(700);
    }

    /**
     * Indicate that the pane has been activated.
     * Invoked when a price range from the RangeSlider is selected.
     */
    public void activate()
    {
        isActivated = true;
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
     * Updates the pane 
     * Invoked when a new price range has been selected
     * 
     * @param min the minimum price from the RangeSlider
     * @param max the maximum price from the RangeSlider
     */
    public void updatePriceRange(int min, int max)
    {
        minPrice = min;
        maxPrice = max;
        updateView();
    }
    
    /**
     * Update the pane to show properties within the new price range
     */
    protected abstract void updateView();   
}
