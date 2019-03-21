//TODO: sort imports
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;

/**
 * TODO: implement
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class PropertyDetailsWindow
{
    // instance variables - replace the example below with your own
    private AirbnbListing property;
    private BorderPane root;
    /**
     * Constructor for objects of class PropertyDetailsWindow
     */
    public PropertyDetailsWindow(AirbnbListing property)
    {
        this.property = property;
        System.out.println(property.getId());
        
        root = new BorderPane(new Label(property.getHost_name()), null, null, null, null);
        
    }
    
    public BorderPane getView()
    {
        return root;
    }
}
