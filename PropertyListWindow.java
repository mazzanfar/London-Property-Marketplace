import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
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
    
    // the number of currently loaded properties
    private int currentLoad;
    
    private ArrayList<AirbnbListing> sortByReviewNumber = new ArrayList<>();
    private ArrayList<AirbnbListing> sortAlphabetically = new ArrayList<>();
    private ArrayList<AirbnbListing> sortAscendingPrice = new ArrayList<>();
    private ArrayList<AirbnbListing> sortDescendingPrice = new ArrayList<>();
    
    private String displayChoice;
    private ScrollPane verticalScroll;
    private Button showMoreButton;
    private AirbnbListing[] inArray;
    private Stage detailsPopUpWin;
    private PropertyLister scrollVBox; //extends a VBox
    
    /**
     * Constructor for objects of class PropertyListWindow
     */
    public PropertyListWindow(ArrayList<AirbnbListing> currentListing)
    {
        this.currentListing = currentListing;
        sizeOfListing = currentListing.size();

        //middle of borderPane
        scrollVBox = new PropertyLister(currentListing);
        scrollVBox.getStyleClass().add("scrollVBox");
        //scrollVBox.getChildren().add(showMoreButton = new Button());
        verticalScroll = new ScrollPane(scrollVBox);
        scrollVBox.setAlignment(Pos.CENTER);
        verticalScroll.getStyleClass().add("verticalScroll");
        //disp();

        //TOP of borderpane
        Label sort = new Label("Sort: ");
        sort.getStyleClass().add("sortLabel");
        ChoiceBox sortBy = new ChoiceBox();
        sortBy.getItems().addAll("by reviews", "lowest to highest price", "highest to lowest price",
            "alphabetically");
        sortBy.getStyleClass().add("ChoiceBox");
        HBox top = new HBox(sort, sortBy);
        
        //action event handler for the choicebox
        sortBy.getSelectionModel().selectedItemProperty().addListener(
            (e, oldValue, newValue) -> updateDisplay((String) newValue));
        
        //BOTTOM:
        Label footerLabel = new Label(getCurrentLoad() + " properties available"  );
        footerLabel.getStyleClass().add("footerLabel");
        
        //creates the 4 sorted property lists
        inArray = currentListing.toArray(new AirbnbListing[sizeOfListing]);
        sortByReviewNumber();
        sortAlphabetically();
        sortByLowestToHighest();
        sortByHighestToLowest();

        root = new BorderPane();

        root.setCenter(verticalScroll);
        root.setBottom(footerLabel);

        
        BorderPane.setAlignment(verticalScroll, Pos.CENTER);
        verticalScroll.setFitToWidth(true);
        root.setTop(top);
        root.getStyleClass().add("rootPane");
        root.setPrefHeight(500);
        root.setPrefWidth(600);
        detailsPopUpWin = new Stage();
    }

    /**
     * method which displays the view
     * @return the root of the scene which is a BorderPane
     */
    public BorderPane getView()
    {
        return root;
    }
    
    private int getCurrentLoad()
    {
        return currentLoad = currentListing.size();
    }
    
    /**
     * updates the display by clearing the children of the scrollVBox and then adding them again 
     * using a differently ordered ArrayList as the parameter to the PropertyLister object
     */
    private void updateDisplay(String displayChoice)
    {     
        scrollVBox.getChildren().clear();
        scrollVBox.getChildren().add(new PropertyLister(howToDisplay(displayChoice)));
    }

    /**
     * sorts the list of properties by number of reviews
     */
    private void sortByReviewNumber()
    {
        AirbnbListing temp = null;

        for (int i = 0; i < sizeOfListing; i++) {
            for(int j = i+1; j < sizeOfListing-1; j++) {
                if (inArray[i].getNumberOfReviews() < inArray[j].getNumberOfReviews()) 
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
     * returns the an ordered arraylist depending on how it should be ordered.
     * @return whichever ordered arraylist of AirbnbListings
     * @param whichOrder the order by which the returned arraylist should follow
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

    /**
     * handles the action event when the view details button is clicked
     * it will open a new window containing more information for that specific property
     */
    private void buttonClicked(ActionEvent e)
    {
        Button btn = (Button) e.getSource();

        String id = btn.getTooltip().getText().substring(VIEWBTN_TOOLTIP.length(), btn.getTooltip().getText().length());

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
