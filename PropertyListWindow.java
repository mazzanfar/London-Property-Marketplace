//TODO: sort imports
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.controlsfx.control.RangeSlider;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import javafx.collections.FXCollections;

/**
 * TODO: implement
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class PropertyListWindow extends Application
{
    // get the data from the csv
    private AirbnbDataMap data;

    private BorderPane root;

    //private String currentBorough;
    ArrayList<StackPane> ScrollPanes = new ArrayList<>();

    private int numberAvailable;
    @Override
    public void start(Stage stage)
    {
        data = new AirbnbDataMap();
        //currentBorough = getBoroughName();

        // Pagination pagination = new Pagination(); 
        //<-- page number bar so that not all 3700 properties are shown

        //middle of borderPane
        VBox scrollVBox = new VBox();
        arrangePropertyList(getMinPrice(), getMaxPrice());

        for (StackPane panes: ScrollPanes)
        {
            scrollVBox.getChildren().add(panes);
        }
        ScrollPane verticalScroll = new ScrollPane(scrollVBox);

        //TOP of borderpane
        // Vbox which contains a text label which shows the name and number of properties
        //in this borough, then there is an HBox containing ways to refine the search
        //even more ie. (number of rooms, minimum nights, arrange by price...)
        Label label = new Label(getBoroughName() + ": " + getNumberAvailable());

        Button sortByReviews = new Button("by reviews");
        sortByReviews.setOnAction(this::byNumberOfReviews);
        Button sortLowestToHighestPrice = new Button("lowest to highest price");
        sortLowestToHighestPrice.setOnAction(this::HighestToLowest);
        Button sortHighestToLowestPrice = new Button("highest to lowest price");
        sortHighestToLowestPrice.setOnAction(this::LowestToHighest);
        Button sortAlphabetically = new Button("alphabetically");
        sortAlphabetically.setOnAction(this::alphabetically);

        ChoiceBox sortBy = new ChoiceBox(FXCollections.observableArrayList(sortByReviews.getText(),
                    sortLowestToHighestPrice.getText(), sortHighestToLowestPrice.getText(),
                    sortAlphabetically.getText()));
        HBox refiningTools = new HBox(sortBy);
        VBox topVBox = new VBox();
        topVBox.getChildren().addAll(label, refiningTools);

        //right of borderPane

        //BOTTOM:
        Label footerLabel = new Label("copyright text info bla bla");
        footerLabel.setId("footerLabel");

        //left of borderpane

        //
        root = new BorderPane(verticalScroll, topVBox, null, footerLabel, null);

        Scene scene = new Scene(root, 860, 630);
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    private String getBoroughName()
    {
        return "df";
    }

    private int getNumberAvailable()
    {
        return numberAvailable;
    }

    private void arrangePropertyList(int minPrice, int maxPrice)
    {
        //iterate over all the listings
        //make a new button with the name 
        //to change later
        String boroughName = "Camden";

        for (AirbnbListing listing : data.getListingsInBorough(boroughName)) {
            if (listing.getPrice() > minPrice && listing.getPrice()< maxPrice){
                Button but = new Button(listing.getName());
                Label hostName = new Label(listing.getHost_name());
                Label propertyPrice = new Label("Price: " + listing.getPrice());
                Label numberOfReviews = new Label("Number of reviews: " + listing.getNumberOfReviews());
                Label minimumNights = new Label("Minimum number of nights: " + listing.getMinimumNights());

                GridPane infoView = new GridPane();
                
                //infoView.getStyleClass().add("infoView");
                infoView.add(hostName, 0, 0);
                infoView.add(propertyPrice, 0, 1);
                infoView.add(numberOfReviews, 1, 0);
                infoView.add(minimumNights, 1, 1);
                infoView.setId("infoView"); //for styling
                
                VBox verticalArrange = new VBox(but, infoView);
                StackPane propertyInfo = new StackPane(verticalArrange);

                ScrollPanes.add(propertyInfo);
                numberAvailable++;
                //this will be made nicer later
            }
        }
    }

    private int numberPropertySelected(String boroughName)
    {
        return data.getListingsInBorough(boroughName).size();
    }

    private void byNumberOfReviews(ActionEvent e)
    {
        for (AirbnbListing listing : data.getListingsInBorough("Camden")){
            listing.getNumberOfReviews();
            
        }
    }

    private void HighestToLowest(ActionEvent e)
    {
        
    }

    private void LowestToHighest(ActionEvent e)
    {

    }

    private void alphabetically(ActionEvent e)
    {

    }
    
    private int getMinPrice()
    {
        //get this from the slider (welcomePane)
        return 300;
    }

    private int getMaxPrice()
    {
        //get this from the slider (welcomePane)
        return 600;
    }
    
    private void test()
    {
        
    }
}
