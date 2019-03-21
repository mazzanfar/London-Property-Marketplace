import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.RangeSlider;
import com.jfoenix.controls.JFXButton;

/**
 * This is the main class of the London Property Marketplace (AirBnB)
 * It takes care of the price selection in the header with a range slider
 * and labels to display values from the range slider, the footer displaying
 * how many available properties there are in the currently selected price
 * range and back and forward buttons to scroll through different panes.
 * 
 * It also updates all panes that need updating whenever a new price range
 * is selected by the user
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class PropertyViewerGUI extends Application
{
    // decalre static label prefixes for easier localization of the app:
    private static final String PRICE_FROM_PREFIX, PRICE_TO_PREFIX, 
                         PRICE_PLEASE_SELECT_PREFIX, FOOTER_PREFIX,
                         TOOLTIP_DISABLED_PREFIX, TOOLTIP_NEXT_PREFIX,
                         TOOLTIP_PREV_PREFIX, WINDOW_TITLE_PREFIX;
    static {
         PRICE_FROM_PREFIX = "Currently showing properties from: ";
         PRICE_TO_PREFIX = " to: ";
         PRICE_PLEASE_SELECT_PREFIX = "Use the slider below to select a desired price range.";
         FOOTER_PREFIX = "Number of properties available: ";
         TOOLTIP_DISABLED_PREFIX = "Please select a price range first";
         TOOLTIP_NEXT_PREFIX = "Next";
         TOOLTIP_PREV_PREFIX = "Previous";
         WINDOW_TITLE_PREFIX = "London Property Marketplace (AirBnB)";
    }
    // All of the panes that will cycle in the center of the screen
    private PanesController panesController;
    // the root
    private BorderPane root;
    // used for selecing a price range
    private RangeSlider priceSlider;
    // all the labels
    private Label priceLabelFrom, priceLabelTo, priceLow, priceHigh, footerLabel;
    // buttons
    private JFXButton forwardButton, backButton;
    
    /**
     * Starting the application...
     */
    @Override
    public void start(Stage stage) throws Exception
    {
        // init panes controller
        panesController = new PanesController();

        //TOP:
        int min = panesController.getDataMinPrice();
        int max = panesController.getDataMaxPrice();

            // init the double slider:
            priceSlider = new RangeSlider(min, max, min, max);
            priceSlider.setShowTickMarks(true);
            priceSlider.setShowTickLabels(true);
            priceSlider.setMajorTickUnit((max-min)/8);
            priceSlider.setMinorTickCount(15);
            priceSlider.getStyleClass().add("priceSlider");
            
            // event to udpate labels while right thumb is dragged:
            priceSlider.highValueProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                    updatePriceLabels();
                }
            });
            
            // event to update labels while left thumb is dragged:
            priceSlider.lowValueProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                    updatePriceLabels();
                }
            });
            
            // event to dipslay properties when a price range is selected
            priceSlider.setOnMouseReleased(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    changedPriceRange(); 
                    updatePriceLabels();
                }
            });
            
                // create the all the labels, displaying info from the slider
                // use different labels, so they can be styled differently (bold numbers)
                priceLabelFrom = new Label(PRICE_PLEASE_SELECT_PREFIX);
                priceLabelFrom.getStyleClass().add("pricePrefix");
                
                priceLow = new Label();
                priceLow.getStyleClass().add("priceNumber");
                
                priceLabelTo = new Label();
                priceLabelTo.getStyleClass().add("pricePrefix");
                
                priceHigh = new Label();
                priceHigh.getStyleClass().add("priceNumber");
            
            // place all the labels in a horizontal box, center it
            HBox priceLabelWrap = new HBox(priceLabelFrom, priceLow, priceLabelTo, priceHigh);
            priceLabelWrap.setAlignment(Pos.CENTER);
        
        // place slider and labels in a vbox, center it
        VBox priceSelection = new VBox(priceLabelWrap, priceSlider);
        priceSelection.setAlignment(Pos.CENTER);
        priceSelection.getStyleClass().add("priceSelection");
        
        //LEFT:
            // init back button
            backButton = new JFXButton("<");
            backButton.setOnAction(this::backwards);
            backButton.setPrefSize(40, 40);
            backButton.setMaxSize(40, 40);
            backButton.getStyleClass().add("mainWinButtons");
            
        // place button in a VBox, center and set width
        VBox backButtonWrap = new VBox(backButton);
        backButtonWrap.setPrefWidth(80);
        backButtonWrap.setAlignment(Pos.CENTER);
        
        //RIGHT:
            // init forward button
            forwardButton = new JFXButton(">");
            forwardButton.setOnAction(this::forwards);
            forwardButton.setPrefSize(40, 40);
            forwardButton.setMaxSize(40, 40);
            forwardButton.getStyleClass().add("mainWinButtons");

        // place button in a VBox, center and set width
        VBox forwardButtonWrap = new VBox(forwardButton);
        forwardButtonWrap.setPrefWidth(80);
        forwardButtonWrap.setAlignment(Pos.CENTER);
        
        // initially, the buttons should are disabled
        toggleButtonActivity();
        
        //BOTTOM:
        // init footer label
        footerLabel = new Label();
        updateFooterLabel();
        footerLabel.getStyleClass().add("footerLabel");
        
        //CENTER:
        //the center pane is the getCurrent() method from PanesController class
        root = new BorderPane(panesController.getCurrent(), priceSelection, forwardButtonWrap, footerLabel, backButtonWrap);
        root.getStyleClass().add("rootPane");
        
        // init scene
        Scene scene = new Scene(root, 860, 640);
        scene.getStylesheets().add("mainStyle.css");

        // set-up the stage
        stage.setScene(scene);
        stage.setMinHeight(680);
        stage.setMinWidth(880);
        stage.setTitle(WINDOW_TITLE_PREFIX);
        stage.show();
        stage.setOnCloseRequest(e -> Platform.exit());
    }
    
    /**
     * A method to handle the back button click
     * Shows previous pane in the center of the BorderPane root
     */
    private void backwards(ActionEvent e)
    {
        root.setCenter(panesController.prev());
    }
    
    /**
     * A method to handle the forward button click
     * Shows next pane in the center of the BorderPane root
     */
    private void forwards(ActionEvent e)
    {
        root.setCenter(panesController.next());
    }
    
    /**
     * A method to enable/disable the back and forward buttons 
     * based on how many active panes there are
     */
    private void toggleButtonActivity()
    {
        if(!panesController.isMoreThanOnePaneActive()) {
            // disable buttons if there is only 1 active pane
            forwardButton.setDisable(true);
            // set tooltip to inform the user that a price must be selected first
            forwardButton.setTooltip(new Tooltip(TOOLTIP_DISABLED_PREFIX));
            backButton.setDisable(true);
            // set tooltip to inform the user that a price must be selected first
            backButton.setTooltip(new Tooltip(TOOLTIP_DISABLED_PREFIX));
        } else if(forwardButton.isDisable() || backButton.isDisable()) {
            // enable buttons if there is more than one active panes and they are disabled
            forwardButton.setDisable(false);
            forwardButton.setTooltip(new Tooltip(TOOLTIP_NEXT_PREFIX));
            backButton.setDisable(false);
            backButton.setTooltip(new Tooltip(TOOLTIP_PREV_PREFIX));
        }
    }
    
    /**
     * A method to update the price labels whenever one of the
     * thumbs of the slider is being dragged
     */
    private void updatePriceLabels()
    {
        priceLabelFrom.textProperty().setValue(PRICE_FROM_PREFIX);
        priceLow.textProperty().setValue("" + (int) priceSlider.getLowValue() + "£");
        priceLabelTo.textProperty().setValue(PRICE_TO_PREFIX);
        priceHigh.textProperty().setValue("" + (int) priceSlider.getHighValue() + "£");
    }
    
    /**
     * Updates the footer label to show how many available properties
     * there are in the currently selected price range
     */
    private void updateFooterLabel()
    {
        int minPrice = (int) priceSlider.getLowValue();
        int maxPrice = (int) priceSlider.getHighValue();
        footerLabel.textProperty().setValue(FOOTER_PREFIX + 
            panesController.getNumberOfAvailableProperties(minPrice, maxPrice));
    }
   
    /**
     * A new price range has been selected from the range slider
     * Notify the MainPanes controller class of the change. 
     */
    private void changedPriceRange()
    {
        int minPrice = (int) priceSlider.getLowValue();
        int maxPrice = (int) priceSlider.getHighValue();

        // update the main panes with respect to the new price range
        panesController.updatePriceRange(minPrice, maxPrice);

        // focus the boroughs pane
        root.setCenter(panesController.focusBoroughsPane());

        //update the footer label
        updateFooterLabel();

        // enable buttons if they have been disabled
        toggleButtonActivity();
    }
}