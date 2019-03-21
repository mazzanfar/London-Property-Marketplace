import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;

/**
 * This class represents the pane that the user will land in upon launching
 * the application. It is a static pane - never gets updated regardless of the price
 * range and adivises the user to select a price range. Extends BorderPane.
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class WelcomePane extends ExtendedBorderPane
{
    // decalre static label prefixes for easier localization of the app:
    private static final String WELCOME_PREFIX, PLEASE_SELECT_PREFIX;
    static {
         WELCOME_PREFIX = "Welcome to the AirBnB London Property Marketplace!";
         PLEASE_SELECT_PREFIX = "Please select a price range above to get started.";
    }
    
    /**
     * Constructor for objects of class WelcomePane
     */
    public WelcomePane()
    {
        // this pane doesnt need access to the data HashMap
        super(null);
        
        // this is the only active pane in the beginning
        activate();
        
        // set dimensions
        setPrefHeight(500);
        setMinHeight(500);
        setMaxHeight(500);
        setPrefWidth(700);
        setMinWidth(700);
        setMaxWidth(700);
        
            // init welcome labels and a separator line
            Label welcomeLabel1 = new Label(WELCOME_PREFIX);
            welcomeLabel1.setWrapText(true);
            welcomeLabel1.getStyleClass().add("welcomeLabel1");
            
            Separator separator = new Separator();
            
            Label welcomeLabel2 = new Label(PLEASE_SELECT_PREFIX);
            welcomeLabel2.getStyleClass().add("welcomeLabel2");
            
        // put labels in a vertical box, center and set dimensions
        VBox welcomeWrap = new VBox(welcomeLabel1, separator, welcomeLabel2);
        welcomeWrap.setMaxWidth(400);
        welcomeWrap.setPrefHeight(500);
        welcomeWrap.setAlignment(Pos.CENTER);
        
        // set the center of this class to be the VBox created above
        setCenter(welcomeWrap);
    }
    
    /**
     * Update the pane to show properties within the new price range
     */
    protected void updateView()
    {
        // there is no action needed to be taken when the price range is updated
    }
}
