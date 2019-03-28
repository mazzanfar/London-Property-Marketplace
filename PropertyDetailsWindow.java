//TODO: sort imports
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.TextAlignment;
import com.sun.javafx.application.HostServicesDelegate;
import java.io.IOException;
import java.awt.Desktop;
import java.net.URI;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


/**
 * TODO: implement
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class PropertyDetailsWindow
{
    private static final String HOST_NAME_PREFIX, PRICE_PREFIX, MINIMUM_NIGHTS_PREFIX,
                                REVIEWS_PREFIX, MAPBTN_PREFIX, HOST_ID_PREFIX, ROOM_TYPE_PREFIX,
                                AVAILABILITY_PREFIX, NUMBER_OF_REVIEWS_PREFIX, LAST_REVIEW_PREFIX,
                                TOGGLE_PREFIX, SINGLE_NIGHT, PLURAL_NIGHT;
    static {
        HOST_NAME_PREFIX =  "Host name:";
        PRICE_PREFIX = "Price:";
        MINIMUM_NIGHTS_PREFIX = "Minimum stay:";
        REVIEWS_PREFIX = "Number of reviews:";
        HOST_ID_PREFIX = "Host ID:";
        ROOM_TYPE_PREFIX = "Room type:";
        AVAILABILITY_PREFIX = "Availability 365:";
        NUMBER_OF_REVIEWS_PREFIX = "Number of reviews:";
        LAST_REVIEW_PREFIX = "Last review:";
        MAPBTN_PREFIX = "View on Map";
        TOGGLE_PREFIX = "Toggle this property favorite";
        SINGLE_NIGHT = " night";
        PLURAL_NIGHT = " nights";
    }
    // instance variables - replace the example below with your own
    private AirbnbListing property;
    private BorderPane root;
    /**
     * Constructor for objects of class PropertyDetailsWindow
     */
    public PropertyDetailsWindow(AirbnbListing property)
    {
        this.property = property;
                    Label boroughLabel = new Label(property.getNeighbourhood().substring(0, 4).toUpperCase());
                    boroughLabel.setAlignment(Pos.CENTER);
                    boroughLabel.getStyleClass().add("boroughLabel");

                        Label headerLabel = new Label(property.getName());
                        headerLabel.setAlignment(Pos.CENTER);
                        headerLabel.setWrapText(true);
                        headerLabel.getStyleClass().add("headerLabel");
                     
                    HBox headerLabelWrap = new HBox(headerLabel);
                    headerLabelWrap.setAlignment(Pos.CENTER);
                    headerLabelWrap.getStyleClass().add("headerLabelWrap");

                    ToggleButton favoriteBtn = new ToggleButton();
                    favoriteBtn.setOnAction(this::toggleFavorite);
                    favoriteBtn.getStyleClass().add("favoriteBtn");
                    favoriteBtn.setTooltip(new Tooltip(TOGGLE_PREFIX));
                    if(property.isFavorite()) {
                        favoriteBtn.setSelected(true);
                    }
                    
                BorderPane headerWrapper = new BorderPane(headerLabelWrap, null, favoriteBtn, null, boroughLabel);
                headerWrapper.setMinWidth(510);
                headerWrapper.setMaxWidth(510);

                            Label hostIDText = new Label(HOST_ID_PREFIX);
                            hostIDText.setWrapText(true);
                            hostIDText.getStyleClass().add("bigLabel");
                            
                            Label hostID = new Label(property.getHost_id());
                            hostID.setWrapText(true);
                            hostID.getStyleClass().add("smallLabel");
                            
                        VBox hostIDWrap = new VBox(hostIDText, hostID);
                        hostIDWrap.setSpacing(2);
                        hostIDWrap.getStyleClass().add("labelWrap");
                        
                            Label hostNameText = new Label(HOST_NAME_PREFIX);
                            hostNameText.setWrapText(true);
                            hostNameText.getStyleClass().add("bigLabel");
                            
                            Label hostName = new Label(property.getHost_name());
                            hostName.setWrapText(true);
                            hostName.getStyleClass().add("smallLabel");
                            
                        VBox hostNameWrap = new VBox(hostNameText, hostName);
                        hostNameWrap.setSpacing(2);
                        hostNameWrap.getStyleClass().add("labelWrap");
                        
                            Label roomTypeText = new Label(ROOM_TYPE_PREFIX);
                            roomTypeText.setWrapText(true);
                            roomTypeText.getStyleClass().add("bigLabel");
                            
                            Label roomType = new Label(property.getRoom_type());
                            roomType.setWrapText(true);
                            roomType.getStyleClass().add("smallLabel");
                            
                        VBox roomTypeWrap = new VBox(roomTypeText, roomType);
                        roomTypeWrap.setSpacing(2);
                        roomTypeWrap.getStyleClass().add("labelWrap");
                        
                            Label availText = new Label(AVAILABILITY_PREFIX);
                            availText.setWrapText(true);
                            availText.getStyleClass().add("bigLabel");
                            
                            Label avail = new Label(""+ property.getAvailability365());
                            avail.setWrapText(true);
                            avail.getStyleClass().add("smallLabel");
                            
                        VBox availWrap = new VBox(availText, avail);
                        availWrap.setSpacing(2);
                        availWrap.getStyleClass().add("labelWrap");

                            Label priceText = new Label(PRICE_PREFIX);
                            priceText.setWrapText(true);
                            priceText.getStyleClass().add("bigLabel");
                            
                            Label price = new Label("" + property.getPrice() + "£");
                            price.setWrapText(true);
                            price.getStyleClass().add("smallLabel");
                            
                        VBox priceWrap = new VBox(priceText, price);
                        priceWrap.setSpacing(2);
                        priceWrap.getStyleClass().add("labelWrap");
                        
                            Label nightsText = new Label(MINIMUM_NIGHTS_PREFIX);
                            nightsText.setWrapText(true);
                            nightsText.getStyleClass().add("bigLabel");
                            
                            Label nights = new Label("");
                            if(property.getMinimumNights() == 1)
                                nights.setText(property.getMinimumNights() + SINGLE_NIGHT);
                            else
                                nights.setText(property.getMinimumNights() + PLURAL_NIGHT);
                            nights.setWrapText(true);
                            nights.getStyleClass().add("smallLabel");
                            
                        VBox nightsWrap = new VBox(nightsText, nights);
                        nightsWrap.setSpacing(2);
                        nightsWrap.getStyleClass().add("labelWrap");
                        
                            Label reviewsText = new Label(NUMBER_OF_REVIEWS_PREFIX);
                            reviewsText.setWrapText(true);
                            reviewsText.getStyleClass().add("bigLabel");
                            
                            Label reviews = new Label("" + property.getNumberOfReviews());
                            reviews.setWrapText(true);
                            reviews.getStyleClass().add("smallLabel");
                            
                        VBox reviewsWrap = new VBox(reviewsText, reviews);
                        reviewsWrap.setSpacing(2);
                        reviewsWrap.getStyleClass().add("labelWrap");
                        
                            Label lReviewText = new Label(LAST_REVIEW_PREFIX);
                            lReviewText.setWrapText(true);
                            lReviewText.getStyleClass().add("bigLabel");
                            
                            Label lReviews = new Label(property.getLastReview());
                            lReviews.setWrapText(true);
                            lReviews.getStyleClass().add("smallLabel");
                            
                        VBox lReviewWrap = new VBox(lReviewText, lReviews);
                        lReviewWrap.setSpacing(2);
                        lReviewWrap.getStyleClass().add("labelWrap");
                        
                    // wrap the labels in a gridpane, center, set dimensions
                    GridPane gridPane = new GridPane();
                    gridPane.add(hostIDWrap, 0, 0);
                    gridPane.add(hostNameWrap, 0, 1);
                    gridPane.add(roomTypeWrap, 0, 2);
                    gridPane.add(availWrap, 0, 3);
                    gridPane.add(priceWrap, 1, 0);
                    gridPane.add(nightsWrap, 1, 1);
                    gridPane.add(reviewsWrap, 1, 2);
                    gridPane.add(lReviewWrap, 1, 3);
                    gridPane.setAlignment(Pos.TOP_CENTER);
                    gridPane.getColumnConstraints().add(new ColumnConstraints(235));
                    gridPane.getColumnConstraints().add(new ColumnConstraints(235));
                    
                    HBox contentWrapper = new HBox(gridPane);
                    contentWrapper.setAlignment(Pos.CENTER);
                    contentWrapper.setMinWidth(510);
                    contentWrapper.setMaxWidth(510);
                    contentWrapper.getStyleClass().add("contentWrapper");
                
                Image mapImg = new Image(getClass().getResourceAsStream("/img/map.png"));
                JFXButton viewOnMapBtn = new JFXButton(MAPBTN_PREFIX, new ImageView(mapImg));
                viewOnMapBtn.setOnAction(this::viewOnMap);
                viewOnMapBtn.getStyleClass().add("mapButton");
                

            VBox propertyWrapper = new VBox(headerWrapper, contentWrapper, viewOnMapBtn);
            propertyWrapper.setAlignment(Pos.CENTER);
            propertyWrapper.setMinWidth(550);
            propertyWrapper.setMaxWidth(500);
            propertyWrapper.getStyleClass().add("propertyWrapper");

        root = new BorderPane(propertyWrapper, null, null, null, null);
        root.getStyleClass().add("rootPane");
    }
    
    public BorderPane getView() 
    {
        return root;
    }

    private void viewOnMap(ActionEvent e)
    {
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI("https://www.google.com/maps?q=" + property.getLatitude() + "," + property.getLongitude()));
        } catch (Exception es) {
            es.printStackTrace();
        }
    }
    
    private void toggleFavorite(ActionEvent e)
    {
        if(property.isFavorite()) {
            property.removeFavorite();
        }
        else {
            property.setFavorite();
        }
    }
}
