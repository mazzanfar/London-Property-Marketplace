import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import java.util.ArrayList; 

/**
 * This class represents the pane that shows a geographically-accurate representation
 * of all the boroughs in London. It consists of a button for each neighbourhood, all of 
 * which are heat mapped - the darker the color, the more available properties there are
 * Upon clicking a button a new window pops up showing a list of all properties in that
 * borough.
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class BoroughsPane extends ExtendedBorderPane
{    
    // decalre static label prefixes for easier localization of the app:
    private static final String LOW_PREFIX, HIGH_PREFIX, PROP_COUNT_PREFIX,
                                POPUP_TITLE_PREFIX;
    static {
        LOW_PREFIX = "Low";
        HIGH_PREFIX = "High";
        PROP_COUNT_PREFIX = "Property count";
        POPUP_TITLE_PREFIX = "Showing all properties in: ";
    }
    
    // the window that pops up when one of the boroughs is clicked
    private Stage popUpWin;
    
    // all of the borough buttons
    private ArrayList<BoroughButton> boroughButtons;
    
    /**
     * Constructor for objects of class BoroughsPane
     */
    public BoroughsPane(AirbnbDataMap data)
    {
        // pass the data along to the super constructor
        super(data);
        
        // init the array to store all buttons
        boroughButtons = new ArrayList<BoroughButton>();
        
        // get the names of all boroughs with respect to geographic
        // locations in an array list
        ArrayList<String> boroughs = data.getGeoOrderedBoroughs();
        
        // create a button for each borough
        for(String boroughName : boroughs) {
            // the button text will be the first for letters of the borough
            // name, in upper-case only
            String btnText = boroughName.substring(0, 4).toUpperCase();
            
            // init button (BoroughButton extends JFXButton and adds a private
            // field to store the borough represented by the button)
            BoroughButton btn = new BoroughButton(btnText, boroughName);
            
            btn.setOnAction(this::setSelectedBorough);
            
            // css scales the button on hover, thus it needs to be bringed to front
            // so that it is above all other button
            btn.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                  @Override
                  public void handle(MouseEvent e) {
                      btn.toFront();
                  }
            });
            
            btn.setPrefHeight(75.0);
            btn.setPrefWidth(70.0);
            btn.getStyleClass().add("boroughButton");
            
            // add the button to the array list for future reference
            boroughButtons.add(btn);
        }
        
        // wrap all of the buttons in an anchor pane
        AnchorPane buttonWrap = new AnchorPane();
        buttonWrap.getChildren().addAll(boroughButtons);
        
        // position buttons in a 'beehive' pattern
        positionButtons();
        
        // create the heat map legend:

            Label lowLabel = new Label(LOW_PREFIX);
            lowLabel.getStyleClass().add("smallLabelBoroughsPane");
            Label propCount = new Label(PROP_COUNT_PREFIX);
            propCount.getStyleClass().add("bigLabelBoroughsPane");
            Label highLabel = new Label (HIGH_PREFIX);
            highLabel.getStyleClass().add("smallLabelBoroughsPane");            
            
            Label heatMapLegend = new Label();
            heatMapLegend.setMinWidth(250);
            heatMapLegend.setMinHeight(5);
            heatMapLegend.setPrefHeight(5);
            heatMapLegend.getStyleClass().add("heatMapLegend");
        
        // the heat map legend is stored in a BorderPane
        BorderPane heatMapLegendWrap = new BorderPane(propCount, null, highLabel, heatMapLegend, lowLabel);
        heatMapLegendWrap.setPrefWidth(250);
        heatMapLegendWrap.setMaxWidth(150);
        
        // the buttons go in the center
        setCenter(buttonWrap);
        // the heat map legend goes in the footer
        setBottom(heatMapLegendWrap);
        // center
        setAlignment(heatMapLegendWrap, Pos.TOP_CENTER);
        
        // init the stage for the popUpWindow so that
        // a new window is not opened every time
        popUpWin = new Stage();
        
        // dimensions:
        setMinHeight(480);
        setMaxHeight(480);
        setPrefHeight(480);
    }

    /**
     * Updates the heat map of boroughs based on availability of properties
     * in the currently selected price range
     */
    protected void updateView()
    {
        int propertyCount = 0;
        // iterate through all buttons
        for(BoroughButton btn : boroughButtons) {
            // get the count of properties for a button
            propertyCount = data.getWithinPriceRange(btn.getBoroughOfButton(), minPrice, maxPrice).size();
            
            // disable the button if there are no properties for its borough
            if(propertyCount == 0) {
                btn.setDisable(true);
            } else { // heat map by setting the background based on the property count in the button's borough
                if(propertyCount < 700) {
                    if(propertyCount < 500) {
                        if(propertyCount < 300) {
                            if(propertyCount < 100) {
                                btn.setBackground((new Background(new BackgroundFill(Color.web("ffb2b3"), CornerRadii.EMPTY, Insets.EMPTY))));
                                btn.setDisable(false);
                            } else {
                                btn.setBackground((new Background(new BackgroundFill(Color.web("ff9b9e"), CornerRadii.EMPTY, Insets.EMPTY))));
                                btn.setDisable(false);
                            }
                        } else {
                            btn.setBackground((new Background(new BackgroundFill(Color.web("ff7579"), CornerRadii.EMPTY, Insets.EMPTY))));
                            btn.setDisable(false);
                        }
                    } else {
                        btn.setBackground((new Background(new BackgroundFill(Color.web("FF5A5F"), CornerRadii.EMPTY, Insets.EMPTY))));
                        btn.setDisable(false);
                    }
                } else {
                    btn.setBackground((new Background(new BackgroundFill(Color.web("ce4a4e"), CornerRadii.EMPTY, Insets.EMPTY))));
                    btn.setDisable(false);
                }
            }
        }
        
        // close the pop-up window if its open, because the price range is now changed,
        // thus new properties should be displayed
        popUpWin.close();
    }
    
    /**
     * Handles the functionality of every BoroughButton
     */
    private void setSelectedBorough(ActionEvent e) 
    {   
        // get the button
        BoroughButton btn = (BoroughButton) e.getSource();

        // get the name of borough it is representing
        // (from a private field so that localization is seamless)
        // and set it as the selected borough in the HashMap
        String borough = btn.getBoroughOfButton();
        
        data.setCurrentBorough(borough);
        
        // get the list of properties in that borough
        PropertyListWindow propList = new PropertyListWindow(data.getWithinPriceRange(borough, minPrice, maxPrice));
        Pane newWinRoot = propList.getView();
        
        // put the list in a new scene
        Scene newScene = new Scene(newWinRoot);
        newScene.getStylesheets().add("propertyListStyle.css");
        newScene.getStylesheets().add("mainStyle.css");

        // put the scene in the new window
        popUpWin.setScene(newScene);
        // set title and show
        popUpWin.setTitle(POPUP_TITLE_PREFIX + btn.getBoroughOfButton());
        popUpWin.show();
    }

    /**
     * This method is responsible for positioning all buttons representing a borough
     * in a 'beehive' pattern. The logic behind the method uses the concept
     * of a 'sparse' matrix which defines where the buttons should be placed
     */
    private void positionButtons()
    {
        // this grid indicates where buttons should be placed
        // true means place a button here and false means dont
        boolean grid[][] = new boolean[][] {
            {false,  false,  false,  false,  false,  false,  false,  true,   false,  false,  false,  false,  false,  false},
            {false,  false,  false,  false,  true,   false,  true,   false,  true,   false,  false,  false,  false,  false},
            {false,  true,   false,  true,   false,  true,   false,  true,   false,  true,   false,  true,   false,  true },
            {true,   false,  true,   false,  true,   false,  true,   false,  true,   false,  true,   false,  true,   false},
            {false,  true,   false,  true,   false,  true,   false,  true,   false,  true,   false,  true,   false,  false},
            {false,  false,  true,   false,  true,   false,  true,   false,  true,   false,  true,   false,  false,  false},
            {false,  false,  false,  true,   false,  true,   false,  true,   false,  true,   false,  false,  false,  false},
        };

        // keep track which button we are positioning
        int btnIndex = 0;

        // iterate through grid...        
        for(int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if(grid[i][j]) {
                    // offsetX by half the buttons width + a constant 80px
                    // to center them based on the window
                    boroughButtons.get(btnIndex).setLayoutX(j*37 + 80);
                    // offset Y by enough to be a beehive pattern
                    boroughButtons.get(btnIndex).setLayoutY(i*58);
                    btnIndex ++;
                }
            }
        }
    }
}
