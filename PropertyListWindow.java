//TODO: sort out imports
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.Pane;
import java.util.ArrayList;

/**
 * TODO: implement
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class PropertyListWindow
{
    private BorderPane root;
    private AirbnbDataMap data;
    private ArrayList<AirbnbListing> listings;
    
    public PropertyListWindow(AirbnbDataMap data)
    {
        listings = data.get(data.getCurrentBorough());       
        Label testLabel = new Label(data.getCurrentBorough());
        testLabel.getStyleClass().add("testLabel");
        root = new BorderPane(testLabel);
    }
    
    /**
     * @return the root Pane of this window
     */
    public Pane getView()
    {
        return root;
    }
}
