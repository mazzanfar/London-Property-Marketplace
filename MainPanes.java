//TODO: sort out imports
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.lang.reflect.Method;
/**
 * This class updates all panes from the main window.
 * The functionality it provides is as follows: Initially, only the 
 * welcome pane is visible for the user. When a price range is selected 
 * from the RangeSlider, the BoroughsPane becomes available as well.
 * Once a borough is selected from there, the StatisticsPane and MapPane
 * get updated according to the borough that has been selected. When a new
 * price range/borough is selected, all panes get updated again
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class MainPanes
{
    // A list to store of all the panes (used for the button functionality)
    private ArrayList<Pane> panes;
    private Pane welcomePane, boroughsPane, statisticsPane, mapPane;
    // a pointer to the currently visible pane
    private int pointer;
    // an indication of how many panes CAN be visible
    private int numberOfActivePanes;
    // a pointer to the main window
    private PropertyViewerGUI referenceToGUI;
    
    /**
     * Constructor for objects of class Panes
     */
    public MainPanes(PropertyViewerGUI reference)
    {
        this.referenceToGUI = reference;
        
        panes = new ArrayList<Pane>();
        
        welcomePane = new WelcomePane();
        boroughsPane = new BoroughsPane();
        statisticsPane = new StatisticsPane();
        mapPane = new MapPane();
        
        panes.add(welcomePane);
        panes.add(boroughsPane);
        panes.add(statisticsPane);
        panes.add(mapPane);
        
        // only the welcome pane will be available at the start
        numberOfActivePanes = 1;
        // the welcome pane is at index 0
        pointer = 0;
    }
    
    /**
     * 
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
            pointer = numberOfActivePanes - 1; // rollover
        } else {
            pointer --;
        }
       
        return getCurrent();
    }
    
    /**
     * A new price range has been selected from the GUI
     * Adjust all panes accordingly
     */
    public void updatePriceRange()
    {
        updateBoroughsPane();
        updateStatisticsPane();
        updateMapPane();
    }
    
    /**
     * Update the boroughs pane based on the selected price range
     * Invoked when the price range has changed.
     * 
     * Note: for some reason it wont recognize the methods if you do soemthing like:
     * boroughsPane.isActive
     * idk why
     * anyway, i had to use java reflection
     * same goes for other pane-update methods
     */
    private void updateBoroughsPane()
    {
        // get the price range
        int min = referenceToGUI.getMinSelectedPrice();
        int max = referenceToGUI.getMaxSelectedPrice();
        
        try {
            // check if the pane has been activated or not
            Method checkActive =  panes.get(1).getClass().getMethod("isActivated");
            Object isPaneActive =  checkActive.invoke(panes.get(1));
            if(!((boolean) isPaneActive)) { // activate:
                Method activate =  panes.get(1).getClass().getMethod("activate");
                activate.invoke(panes.get(1));
                // increment so that the buttons will scroll properly
                numberOfActivePanes ++;
            }
            
            // invoke the updatePriceRange method on the boroughPane
            Method updatePriceRange = panes.get(1).getClass().getMethod("updatePriceRange", int.class, int.class, MainPanes.class, AirbnbDataMap.class);
            updatePriceRange.invoke(panes.get(1), min, max, this, referenceToGUI.getData());
        } catch(Exception e) {
            System.out.println(e);
        } 
    }
    
    /**
     * update what is displayed in the statistics pane.
     * Invoked when a new price range or a new borough 
     * has been selected.
     */
    private void updateStatisticsPane()
    {
        // get the price range
        int min = referenceToGUI.getMinSelectedPrice();
        int max = referenceToGUI.getMaxSelectedPrice();

        try {
            // check if the pane has been activated or not
            Method checkActive =  panes.get(2).getClass().getMethod("isActivated");
            Object isPaneActive =  checkActive.invoke(panes.get(2));
            if(!((boolean) isPaneActive)) {// activate:
                Method activate =  panes.get(2).getClass().getMethod("activate");
                activate.invoke(panes.get(2));
                // increment so that the buttons will scroll properly
                numberOfActivePanes ++;
            }
            
            // invoke the updateStatistics method on the statistcsPane
            Method updateStatistics = panes.get(2).getClass().getMethod("updateStatistics", int.class, int.class, AirbnbDataMap.class);
            updateStatistics.invoke(panes.get(2), min, max, referenceToGUI.getData());
        } catch(Exception e) {
            System.out.println(e);
        } 
    }
    
    /**
     * update what is displayed in the map pane.
     * Invoked when a new price range or a new borough 
     * has been selected.
     */
    private void updateMapPane()
    {
        // get the price range..
        int min = referenceToGUI.getMinSelectedPrice();
        int max = referenceToGUI.getMaxSelectedPrice();
        
        try {
            // check if the pane has been activated or not
            Method checkActive =  panes.get(3).getClass().getMethod("isActivated");
            Object isPaneActive =  checkActive.invoke(panes.get(3));
            if(!((boolean) isPaneActive)) { // activate:
                Method activate =  panes.get(3).getClass().getMethod("activate");
                activate.invoke(panes.get(3));
                // increment so that the buttons will scroll properly
                numberOfActivePanes ++;
            }
            
            //invoke the updateMap method on the mapPane
            Method updateMap = panes.get(3).getClass().getMethod("updateMap", int.class, int.class, AirbnbDataMap.class);
            updateMap.invoke(panes.get(3), min, max, referenceToGUI.getData());
        } catch(Exception e) {
            System.out.println(e);
        } 
         
    }
    
    /**
     * 
     * @return true if there is more than one pane active
     */
    public boolean isMoreThanOnePaneActive()
    {
        return (numberOfActivePanes > 1);
    }
    
    /**
     * Changes the selected borough value in the HashMap private field
     * For this we need to first pass it to the GUI, as this is where the
     * HashMap is instantiated. Panes get updated to the new data accordingly;
     */
    public void selectBorough(String boroughName)
    {
        referenceToGUI.setSelectedBorough(boroughName);
        updateStatisticsPane();
        updateMapPane();
    }
}
