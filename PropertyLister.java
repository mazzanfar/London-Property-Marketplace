import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileReader;
import javafx.scene.control.ScrollPane;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import com.jfoenix.controls.JFXButton;
import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Tooltip;

/**
 * This class handles  listing properties in a digestible
 * form for the user. It is an extension of a VBox which
 * adds boxes for every AirbnbListing that has been passed.
 * In only loads a certain number of properties at a time in order
 * to optimize memory usage and speed. Also implements a 'Show more' button that
 * loads more properties upon request
 * 
 * The class itself extends VBox
 *
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class PropertyLister extends VBox
{
    // decalre static label prefixes for easier localization of the app:
    private static final String HOST_NAME_PREFIX, PRICE_PREFIX, MINIMUM_NIGHTS_PREFIX,
                                REVIEWS_PREFIX, VIEWBTN_TOOLTIP, VIEWBTN_PREFIX,
                                COUNT_PREFIX, SHOW_MORE_PREFIX, DETAILS_POPUP_PREFIX;
    // the maximum number of properites to be loaded 
    // in the beginning or everytime time the show more button is pressed
    private static final int maxLoad;
    static {
        HOST_NAME_PREFIX =  "Name of the property's host: ";
        PRICE_PREFIX = "Price: ";
        MINIMUM_NIGHTS_PREFIX = "Minimum number of nights: ";
        REVIEWS_PREFIX = "Number of reviews: ";
        VIEWBTN_TOOLTIP = "View property ";
        VIEWBTN_PREFIX = "View Details";
        COUNT_PREFIX = "Showing: ";
        SHOW_MORE_PREFIX = "Show More";
        DETAILS_POPUP_PREFIX = "Showing details for property id: " ;
        maxLoad = 20;
    }
    
    // the stage for the pop-up
    private Stage detailsPopUpWin;

    // the list of passed properties
    private ArrayList<AirbnbListing> properties;
    
    // the number of currently loaded properties
    private int currentLoad;

    /**
     * Constructor for objects of class PropertyLister
     */
    public PropertyLister(ArrayList<AirbnbListing> properties)
    {
        // get the passed properties
        this.properties = properties;

        // no properties have been loaded yet
        currentLoad = 0;
        
        // create GUI view for a certain number of properties
        createPropertyBoxes();

        // update the label that indicated how many properties are being show
        updateLabel();

        // add a button to load more properties
        addMoreButton();
        
        // add space between each property, center
        setSpacing(30);
        setAlignment(Pos.CENTER);
        
        // init the stage for the popUpWindow so that
        // a new window is not opened every time
        detailsPopUpWin = new Stage();
    }
    
    /**
     * This method displays additional/initial properties
     * equal to the 'maxLoad' static field
     */
    private void createPropertyBoxes()
    {
        // save the current load
        int temp = currentLoad;
        // make exactly as many properties as maxLoad or until
        // there are no more properties left to create
        for(int i = temp; i < (temp + maxLoad) && i < properties.size(); i++) {
            // get the property
            AirbnbListing property = properties.get(i);
            
            // the header of the box is the property name
            Label headerLabel = new Label(property.getName());
            headerLabel.setWrapText(true);
            headerLabel.getStyleClass().add("headerLabelProperty");
            
                    // init labels:
                    Label hostName = new Label(HOST_NAME_PREFIX + property.getHost_name());
                    hostName.setWrapText(true);
                    hostName.getStyleClass().add("propertyLabel");
                    
                    Label propertyPrice = new Label(PRICE_PREFIX + property.getPrice() + "£");
                    propertyPrice.setWrapText(true);
                    propertyPrice.getStyleClass().add("propertyLabel");
                    
                    Label numberOfReviews = new Label(REVIEWS_PREFIX + property.getNumberOfReviews());
                    numberOfReviews.setWrapText(true);
                    numberOfReviews.getStyleClass().add("propertyLabel");
                    
                    Label minimumNights = new Label(MINIMUM_NIGHTS_PREFIX + property.getMinimumNights());
                    minimumNights.setWrapText(true);
                    minimumNights.getStyleClass().add("propertyLabel");
                
            
                    // init 'View Details' button
                    JFXButton viewBtn = new JFXButton(VIEWBTN_PREFIX);
                    viewBtn.setTooltip(new Tooltip(VIEWBTN_TOOLTIP + property.getId()));
                    viewBtn.setOnAction(this::viewProperty);
                    viewBtn.getStyleClass().add("viewDetails");
                    
                // wrap the button in a VBox
                VBox viewBtnWrapper = new VBox(viewBtn);
                viewBtnWrapper.setMaxWidth(150);
                viewBtnWrapper.setMinWidth(150);
                viewBtnWrapper.setAlignment(Pos.CENTER);
                
            // wrap the labels in a gridpane, center, set dimensions
                GridPane gridPane = new GridPane();
                gridPane.add(hostName, 0, 0);
                gridPane.add(propertyPrice, 0, 1);
                gridPane.add(minimumNights, 1, 0);
                gridPane.add(numberOfReviews, 1, 1);
                gridPane.setAlignment(Pos.TOP_LEFT);
                gridPane.getColumnConstraints().add(new ColumnConstraints(160));
                gridPane.getColumnConstraints().add(new ColumnConstraints(150));
                
                // spacer label to offset the gridpane from the left
                Label spacerLabel = new Label();
                spacerLabel.setMinWidth(35);
                spacerLabel.setMaxWidth(35);
            
            // wrap all the details of a property in an HBox
            HBox contentWrapper = new HBox(spacerLabel, gridPane, viewBtnWrapper);
                
            // wrap the two wrappers in a VBox
            VBox propertyWrapper = new VBox(headerLabel, contentWrapper);
            propertyWrapper.setSpacing(20);
            propertyWrapper.setMaxWidth(500);
            propertyWrapper.setPrefWidth(500);
            propertyWrapper.setAlignment(Pos.CENTER);
            propertyWrapper.getStyleClass().add("propertyWrapper");
            
            // add outer wrapper to the children of this class (VBox extension)
            getChildren().add(propertyWrapper);
            // increment the number of currently loaded properties
            currentLoad ++;
        }
    }
    
    /**
     * This method updates the label showing the currently displayed properties
     * and the maximum number of properties that are to be displayed
     */
    private void updateLabel()
    {
        Label countLabel = new Label(COUNT_PREFIX + currentLoad + "/" + properties.size());
        countLabel.getStyleClass().add("countLabel");
        getChildren().add(countLabel);
    }
    
    /**
     * This method adds a 'Show More' button as the last child of the VBox (this class)
     */
    private void addMoreButton()
    {
        // we only need a show more button if there are more properties to show
        if(currentLoad < properties.size()) {
            Image moreImg = new Image(getClass().getResourceAsStream("/img/more.png"));
            JFXButton showMoreBtn = new JFXButton(SHOW_MORE_PREFIX, new ImageView(moreImg));
            showMoreBtn.getStyleClass().add("favoritesButton");
            showMoreBtn.getStyleClass().add("moreBtn");
            showMoreBtn.setOnAction(this::showMore);
            getChildren().add(showMoreBtn);
        }
    }

        
    /**
     * This method gets called when the 'View details' button is clicked
     * It shows the property of that button in a new window, displaying
     * further information about the property
     */
    private void viewProperty(ActionEvent e)
    {
        // get the button that is the source of the click
        JFXButton btn = (JFXButton) e.getSource();
        
        // extract the id of the property that the button is representing from 
        // the buttons tooltip. This is acceptable, because even if the app gets localized,
        // the property ID is a number and it will remain unchanged
        String id = btn.getTooltip().getText().substring(VIEWBTN_TOOLTIP.length(), btn.getTooltip().getText().length());
        
        // iterate...
        for(AirbnbListing listing : properties) {
            if(listing.getId().equals(id)) {
                //find the matching property by id
                PropertyDetailsWindow propList = new PropertyDetailsWindow(listing);
                Pane newWinRoot = propList.getView();
                
                // add the details in a new scene
                Scene newScene = new Scene(newWinRoot);
                newScene.getStylesheets().add("propertyDetailsStyle.css");
        
                // add the scene to the popup stage
                detailsPopUpWin.setScene(newScene);
                detailsPopUpWin.setMinHeight(400);
                // set title and show
                detailsPopUpWin.setTitle(DETAILS_POPUP_PREFIX + id);
                detailsPopUpWin.show();
                
                // no need to look for the property anymore
                return;
            }
        }
    }
    
    /**
     * This method handles the 'Show More' button click
     * It displays more properties, based on the static field 'maxLoad'
     */
    private void showMore(ActionEvent e)
    {
        // remove the button and label
        getChildren().remove(currentLoad);
        getChildren().remove(currentLoad);
     
        // create a number of properties equal to 'maxLoad'
        createPropertyBoxes();
        
        // add the label back
        updateLabel();

        // add the button back
        addMoreButton();
    }
}