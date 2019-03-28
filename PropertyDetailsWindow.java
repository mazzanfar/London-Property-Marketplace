import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXButton;
import java.awt.Desktop;
import java.net.URI;

/**
 * This class shows the details of each property in an aesthetic way.
 * There are two buttons, one to mark the property as favorite or too unmark it,
 * the other to see the property i=on a map.
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class PropertyDetailsWindow
{
    private AirbnbListing property;
    private HBox root;
    private Button favoriteButton;
    /**
     * Constructor for objects of class PropertyDetailsWindow
     * Creates all the labels, the buttons and the VBox, the Hbox and the GridPane for the layout
     */
    public PropertyDetailsWindow(AirbnbListing property)
    {
        this.property = property;
        
        // All the Labels.
        Label nameLabel = new Label("Name : ");
        Label host_idLabel = new Label ("Host id : ");
        Label host_nameLabel = new Label ("Host name : ");
        Label neighbourhoudLabel = new Label ("Neighbourhood : "); 
        Label room_typeLabel = new Label ("Room Type : ");
        Label priceLabel = new Label ("Price : ");
        Label minimumNightsLabel = new Label ("Minimum nights : ");
        Label numberOfReviewsLabel = new Label ("Number of Reviews : ");
        Label lastReviewLabel = new Label ("Last review : ");
        Label reviewsPerMonthLabel = new Label ("Review per month : ");
        Label calculatedHostListingsCountLabel = new Label ("Number of listings frome this host : "); 
        Label availability365Label = new Label ("Availability : ");
        Label getIdLabel = new Label (property.getId());
        Label getNameLabel = new Label(property.getName());
        Label getHost_idLabel = new Label (property.getHost_id());
        Label getHost_nameLabel = new Label (property.getHost_name());
        Label getNeighbourhoudLabel = new Label (property.getNeighbourhood()); 
        Label getRoom_typeLabel = new Label (property.getRoom_type());
        Label getPriceLabel = new Label (""+property.getPrice());
        Label getMinimumNightsLabel = new Label (""+property.getMinimumNights());
        Label getNumberOfReviewsLabel = new Label (""+property.getNumberOfReviews());
        Label getLastReviewLabel = new Label (property.getLastReview());
        Label getReviewsPerMonthLabel = new Label (""+property.getReviewsPerMonth());
        Label getCalculatedHostListingsCountLabel = new Label (""+property.getCalculatedHostListingsCount()); 
        Label getAvailability365Label = new Label (""+property.getAvailability365());
        
        // The button to mark as favorite.
        favoriteButton = new Button();
        favoriteButton.setOnAction(this::toggleFavorite);
        favoriteButton.getStyleClass().add("favoriteButton");
        
        // The GridPane where the informations about the host are listed.
        GridPane hostInformationGridPane = new GridPane();
        hostInformationGridPane.getStyleClass().add("hostInformationGridPane");
        hostInformationGridPane.add(host_idLabel,0,0);
        System.out.println("Style: " + host_idLabel.getStyle());
        hostInformationGridPane.add(getHost_idLabel,1,0);
        hostInformationGridPane.add(host_nameLabel,0,1);
        hostInformationGridPane.add(getHost_nameLabel,1,1);
        hostInformationGridPane.add(calculatedHostListingsCountLabel,0,2);
        hostInformationGridPane.add(getCalculatedHostListingsCountLabel,1,2);
        
        // The GridPane where the informations about the reviews are listed.
        GridPane reviewGridPane = new GridPane();
        reviewGridPane.getStyleClass().add("reviewGridPane");
        reviewGridPane.add(numberOfReviewsLabel,0,0);
        reviewGridPane.add(getNumberOfReviewsLabel,1,0);
        reviewGridPane.add(reviewsPerMonthLabel,0,1);
        reviewGridPane.add(getReviewsPerMonthLabel,1,1);
        reviewGridPane.add(lastReviewLabel,0,2);
        reviewGridPane.add(getLastReviewLabel,1,2);
        
        // The GridPane where the informations about the property are listed.
        GridPane informationGridPane = new GridPane();
        informationGridPane.getStyleClass().add("informationGridPane");
        informationGridPane.add(favoriteButton, 1, 0);
        informationGridPane.add(nameLabel, 0, 1);
        informationGridPane.add(getNameLabel, 1, 1);
        informationGridPane.add(neighbourhoudLabel, 0, 2);
        informationGridPane.add(getNeighbourhoudLabel, 1, 2);
        informationGridPane.add(room_typeLabel, 0, 3);
        informationGridPane.add(getRoom_typeLabel, 1, 3);
        informationGridPane.add(priceLabel, 0, 4);
        informationGridPane.add(getPriceLabel, 1, 4);
        informationGridPane.add(minimumNightsLabel,0, 5);
        informationGridPane.add(getMinimumNightsLabel, 1, 5);
        informationGridPane.add(availability365Label, 0, 6);
        informationGridPane.add(getAvailability365Label, 1, 6);
        
        // The button to view the location of the property on the map.
        JFXButton viewOnMapBtn = new JFXButton("View on Map");
        viewOnMapBtn.setOnAction(this::viewOnMap);
        viewOnMapBtn.getStyleClass().add("mapButton");
        
        // The VBox that is on the right which contains the hostInformationGridPane
        // the reviewGridPane and the viewOnMapBtn.
        VBox rightHandVbox = new VBox(10);
        rightHandVbox.getChildren().addAll(hostInformationGridPane,reviewGridPane, viewOnMapBtn); 
        
        // The HBox that contains the informationGridPane and the rightHandVbox.
        root = new HBox(10);
        root.getChildren().addAll(informationGridPane, rightHandVbox);
        root.getStyleClass().add("rootPane");
    }
    
    /**
     * method which displays the view
     */
    public HBox getView()
    {
        return root;
    }
    
    /**
     * This method is called from the favorite button
     * 
     */
    private void toggleFavorite(ActionEvent e)
    {
        if(property.isFavorite()) {
            property.removeFavorite();
        }
        else {
            property.setFavorite();
        }
    }
    
    /**
     * This method is called from the viewOnMapBtn button
     * 
     */
    private void viewOnMap(ActionEvent e)
    {
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI("https://www.google.com/maps?q=" + property.getLatitude() + "," + property.getLongitude()));
        } catch (Exception es) {
            es.printStackTrace();
        }
    }
}

