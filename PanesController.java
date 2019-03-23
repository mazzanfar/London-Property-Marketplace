import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

/**
 * This class controls all changes that are supposed to happen in the four
 * main panes of the application. It takes care of the back/forward button 
 * functionality, as well as alerting panes of a price range change by the user
 * 
 * It is also the class that initializes the data HashMap
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class PanesController
{
    // A list to store of all the panes (used for the button functionality)
    private ArrayList<ExtendedBorderPane> panes;
    // a pointer to the currently visible pane
    private int pointer;
    // an indication of how many panes CAN be visible
    private int numberOfActivePanes;
    // loads the data from the .csv file and maps it in a HashMap
    private AirbnbDataMap data;
    
    /**
     * Constructor for objects of class MainPanes
     */
    public PanesController()
    {
        // load the data
        data = new AirbnbDataMap();
        
        // create panes and add them to an ArrayList (so they can be indexed)
        panes = new ArrayList<ExtendedBorderPane>();
        
        panes.add(new WelcomePane());
        panes.add(new BoroughsPane(data));
        panes.add(new StatisticsPane(data));
        panes.add(new FavoritesPane(data));
        
        // only the welcome pane will be available at the start
        numberOfActivePanes = 1;
        
        // the welcome pane is at index 0
        pointer = 0;
    }
    
    /**
     * Accessor method for the current pane
     * @return the pane that is to be displayed in the GUI
     */
    public Pane getCurrent()
    {
        return panes.get(pointer);
    }
    
    /**
     * A method to display the next pane.
     * Considers only active panes.
     * @return the next pane which will be displayed in the GUI
     */
    public Pane next()
    {
        if(pointer == numberOfActivePanes - 1) {
            pointer = 0; // roll over
        } else {
            pointer ++;
        }
        // smoothly fade in the new pane
        fadeIn();

        return getCurrent();
    }
    
    /**
     * A method to display the previous pane.
     * Considers only active panes.
     * @return the previous pane which will be displayed in the GUI
     */
    public Pane prev()
    {
        if(pointer == 0) {
            pointer = numberOfActivePanes - 1; // roll over
        } else {
            pointer --;
        }
        // smoothly fade in the new pane
        fadeIn();
        
        return getCurrent();
    }
    
    /**
     * Focus the boroughs pane specifically
     */
    public Pane focusBoroughsPane()
    {
        pointer = 1;
        // smoothly fade in the pane
        fadeIn();
        
        return getCurrent();
    }
        
    /**
     * @return true if there is more than one pane active
     */
    public boolean isMoreThanOnePaneActive()
    {
        return (numberOfActivePanes > 1);
    }
    
    /**
     * A new price range has been selected from the GUI
     * Update all panes accordingly
     * @param min the new minimum selected price 
     * @param max the new maximum selected price
     */
    public void updatePriceRange(int min, int max)
    {        
        for(ExtendedBorderPane pane : panes) {
            if(!pane.isActivated()) {
                pane.activate();
                numberOfActivePanes ++;
            }
            
            pane.updatePriceRange(min, max);
        }
    }
    
    /**
     * Accessor method to get the minimum price across all properties
     * from the .csv file. Used by the GUI class to create the price range 
     * slider with proper limits.
     * @return the lowest priced properties of all
     */
    public int getDataMinPrice()
    {
        return data.getMinPrice();
    }
    
    /**
     * Accessor method to get the maximum price across all properties
     * from the .csv file. Used by the GUI class to create the price range 
     * slider with proper limits.
     * @return the highest priced properties of all
     */
    public int getDataMaxPrice()
    {
        return data.getMaxPrice();
    }
    
    /**
     * Used to update the footer label to show the number of properties available
     * 
     * @return the number of properties available in a specific price range
     */
    public int getNumberOfAvailableProperties(int min, int max)
    {
        return data.getWithinPriceRange(min, max);
    }
    
    /**
     * A private method to create a smooth transition when the panes are changing
     */
    private void fadeIn()
    {
        FadeTransition tr = new FadeTransition(Duration.millis(200), panes.get(pointer));
        tr.setFromValue(0);
        tr.setToValue(1);
        tr.play();
    }
}
