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
import java.util.List;
import javafx.collections.FXCollections;
import java.util.stream.Collectors;
import javafx.scene.control.Tooltip;

/**
 * Write a description of class PropertyListWindow here.
 *
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class PropertyListWindow 
{
    private static final String VIEWBTN_TOOLTIP;
    static {
        VIEWBTN_TOOLTIP = "View property ";
    }
    private BorderPane root;
    private ArrayList<AirbnbListing> currentListing;
    private int sizeOfListing;
    
    
    private ArrayList<AirbnbListing> sortByReviewNumber = new ArrayList<>();
    private ArrayList<AirbnbListing> sortAlphabetically = new ArrayList<>();
    private ArrayList<AirbnbListing> sortAscendingPrice = new ArrayList<>();
    private ArrayList<AirbnbListing> sortDescendingPrice = new ArrayList<>();
    
    private int numberAvailable;
    private String displayChoice = "";
    ArrayList<StackPane> ScrollPanes = new ArrayList<>();
    private ScrollPane verticalScroll;
    private AirbnbListing[] inArray;
    private Stage detailsPopUpWin;
    
    /**
     * Constructor for objects of class PropertyListWindow
     */
    public PropertyListWindow(ArrayList<AirbnbListing> currentListing)
    {
        
        this.currentListing = currentListing;
        sizeOfListing = currentListing.size();
        
        inArray = currentListing.toArray(new AirbnbListing[sizeOfListing]);
        //middle of borderPane
        VBox scrollVBox = new VBox();

        displayList(currentListing); 
 
        //place all the properties (without any preferences) inside the scrollVBox.
        for (StackPane panes: ScrollPanes)
        {
            scrollVBox.getChildren().add(panes);
        }
        verticalScroll = new ScrollPane(scrollVBox);

        //TOP of borderpane
        // Vbox which contains a text label which shows the name and number of properties
        //in this borough, then there is an HBox containing ways to refine the search
        //even more ie. (number of rooms, minimum nights, arrange by price...)
        //Label label = new Label(data.getCurrentBorough() + ": " + getNumberAvailable());

        ChoiceBox sortBy = new ChoiceBox();
        sortBy.getItems().addAll("by reviews", "lowest to highest price", "highest to lowest price",
        "alphabetically");

        //action listener to choiceBox
        sortBy.getSelectionModel().selectedItemProperty().addListener
            ((v, oldValue, newValue) -> displayChoice = (String) newValue);
        displayList(howToDisplay(displayChoice));
        
        HBox refiningTools = new HBox(sortBy);
        VBox topVBox = new VBox();
        topVBox.getChildren().addAll( refiningTools);
        
        //right of borderPane NOTHING
        //BOTTOM:
        Label footerLabel = new Label("copyright text info bla bla");
        footerLabel.setId("footerLabel");
        
        //left of borderpane NOTHING
        //creates the 4 sorted property lists
        sortByReviewNumber();
        sortAlphabetically();
        sortByLowestToHighest();
        sortByHighestToLowest();
        
        root = new BorderPane(verticalScroll, topVBox, null, footerLabel, null);
        detailsPopUpWin = new Stage();
    }

    /**
     * method which displays the view
     */
    public BorderPane getView()
    {
        return root;
    }
    
    /**
     * this is what creates all the panes and allows the properties to be displayed
     */
    private void displayList(ArrayList<AirbnbListing> list)
    {
        //for (AirbnbListing listing : howToDisplay()) {
        
        for (AirbnbListing listing : howToDisplay(displayChoice)){
                
                Button but = new Button(listing.getName());
                but.setTooltip(new Tooltip(VIEWBTN_TOOLTIP + listing.getId()));
                but.setOnAction(this::buttonClicked);
                
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
    
    /**
     * sorts the list of properties by number of reviews
     */
    private void sortByReviewNumber()
    {
        AirbnbListing temp = null;
        
        for (int i = 0; i < sizeOfListing; i++) {
            for(int j = i+1; j < sizeOfListing-1; j++) {
                if (inArray[i].getNumberOfReviews() > inArray[j].getNumberOfReviews()) 
                {
                    temp = inArray[i];
                    inArray[i] = inArray[j];
                    inArray[j] = temp;
                }
            }
        }
        
        //transform the sorted array into a sorted arrayList
        for (int i = 0; i < inArray.length; i++) {
            sortByReviewNumber.add(inArray[i]);
        }
    }
    
    /**
     * sorts the list of properties by alphabetical order
     */
    public void sortAlphabetically()
    {
         List sortedAlphabetically = currentListing.stream()
                .sorted((a, b) -> a.getName().compareTo(b.getName()))
                .collect(Collectors.toList());
         sortAlphabetically.addAll(sortedAlphabetically);
    }
    
    /**
     * sorts the list of properties by ascending price
     */
    private void sortByLowestToHighest()
    {
        AirbnbListing temp = null;

        for (int i = 0; i < sizeOfListing; i++) {
            for(int j = i+1; j < sizeOfListing-1; j++) {
                if (inArray[i].getPrice() > inArray[j].getPrice()) 
                {
                    temp = inArray[i];
                    inArray[i] = inArray[j];
                    inArray[j] = temp;
                }
            }
        }
        
        //transform the sorted array into a sorted arrayList
        for (int i = 0; i < inArray.length; i++) {
            sortAscendingPrice.add(inArray[i]);
        }
    }
    
    /**
     * sorts the list of properties by descending price
     */
    private void sortByHighestToLowest()
    {
        AirbnbListing temp = null;

        for (int i = 0; i < sizeOfListing; i++) {
            for(int j = i+1; j < sizeOfListing-1; j++) {
                if (inArray[i].getPrice() < inArray[j].getPrice()) 
                {
                    temp = inArray[i];
                    inArray[i] = inArray[j];
                    inArray[j] = temp;
                }
            }
        }
        
        //transform the sorted array into a sorted arrayList
        for (int i = 0; i < inArray.length; i++) {
            sortDescendingPrice.add(inArray[i]);
        }
    }
    
    /**
     * registers and acts accordingly when a button is clicked in the choicebox
     */
    private void choiceBoxListener(ChoiceBox sortBy)
    {
        
        sortBy.getSelectionModel().selectedItemProperty().addListener
            ((v, oldValue, newValue) -> displayChoice = (String) newValue);
        displayList(howToDisplay(displayChoice));
    }
    
    private int getNumberAvailable()
    {
        return numberAvailable;
    }
    
    /**
     * returns the an ordered arraylist depending on how it should be ordered.
     */
    private ArrayList<AirbnbListing> howToDisplay(String whichOrder)
    {   
        if(whichOrder.equals("by reviews")) {
            return sortByReviewNumber;
        } else if (whichOrder.equals("highest to lowest price")) {
            return sortDescendingPrice;
        } else if (whichOrder.equals("lowest to highest price")) {
            return sortAscendingPrice;
        } else if (whichOrder.equals("alphabetically")) {
            return sortAlphabetically;
        } else {
            return currentListing;
        }
    }
    
    private void setCurrentProperty()
    {
        
    }
    
    private void buttonClicked(ActionEvent e)
    {
        Button btn = (Button) e.getSource();
        
        String id = btn.getTooltip().getText().substring(VIEWBTN_TOOLTIP.length(), btn.getTooltip().getText().length()) ;
        System.out.println(id);
        
        for(AirbnbListing property: currentListing) {
            if(property.getId().equals(id)) {
                PropertyDetailsWindow propDetails = new PropertyDetailsWindow(property);
                Pane newWinRoot = propDetails.getView();
                
                Scene newScene = new Scene(newWinRoot, 350, 350);
                newScene.getStylesheets().add("propertyDetailsStyle.css");
                
                detailsPopUpWin.setScene(newScene);
        
                detailsPopUpWin.setTitle("Showing details for property id: " + id);
                detailsPopUpWin.show();
                return;
            }
        }
    }
}
