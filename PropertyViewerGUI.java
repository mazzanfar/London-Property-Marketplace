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
import com.jfoenix.controls.JFXButton;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList; 
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.*;
/**
 * This class is the main class of the application
 * Control everything
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class PropertyViewerGUI extends Application
{
    // All of the panes that will cycle in the center of the screen
    private MainPanes mainPanes;
    // the root
    private BorderPane root;
    // get the data from the csv
    private AirbnbDataMap data;
    // used for selecing a price range
    private RangeSlider priceSlider;
    // used to show the selected price
    private Label priceLabel;
    // buttons
    private JFXButton forwardButton, backButton;
    
    @Override
    public void start(Stage stage) throws Exception
    {
        // get data and init the panes in the center
        data = new AirbnbDataMap();
        mainPanes = new MainPanes(this);

        //TOP:
        int min = data.getMinPrice();
        int max = data.getMaxPrice();
            // create the double slider:
            priceSlider = new RangeSlider(min, max, min, max);
            priceSlider.setShowTickMarks(true);
            priceSlider.setShowTickLabels(true);
            priceSlider.setMajorTickUnit((max-min)/8);
            priceSlider.setMinorTickCount(15);
            priceSlider.getStyleClass().add("priceSlider");
        
            // events for the double slider:
            priceSlider.highValueProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                    updatePriceLabel();
                }
            });
            priceSlider.lowValueProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                    updatePriceLabel();
                }
            });
            priceSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    changedPriceRange(); // TODO: change from mouse click to some drag even
                }
            });
            
            // create the label, displaying info from the slider
            priceLabel = new Label("Please select a price range.");
            priceLabel.getStyleClass().add("priceLabel");
        
        // place slider and label in a vbox
        VBox priceSelection = new VBox(priceLabel, priceSlider);
        priceSelection.setAlignment(Pos.CENTER);
        priceSelection.getStyleClass().add("priceSelection");
        
        //LEFT:
        backButton = new JFXButton("<");
        backButton.setOnAction(this::backwards);
        backButton.setPrefSize(40, 40);
        backButton.setMaxSize(40, 40);
        backButton.getStyleClass().add("mainWinButtons");
        VBox backButtonWrap = new VBox(backButton);
        backButtonWrap.setPrefWidth(80);
        backButtonWrap.setAlignment(Pos.CENTER);
        
        //RIGHT:
        forwardButton = new JFXButton(">");
        forwardButton.setOnAction(this::forwards);
        forwardButton.setPrefSize(40, 40);
        forwardButton.setMaxSize(40, 40);
        forwardButton.getStyleClass().add("mainWinButtons");
        VBox forwardButtonWrap = new VBox(forwardButton);
        forwardButtonWrap.setPrefWidth(80);
        forwardButtonWrap.setAlignment(Pos.CENTER);
        

        // disable buttons if there is only 1 active pane
        if(!mainPanes.isMoreThanOnePaneActive())
        {
            forwardButton.setDisable(true);
            backButton.setDisable(true);
        }
        
        //BOTTOM:
        Label footerLabel = new Label("copyright text info bla bla");
        footerLabel.getStyleClass().add("footerLabel");
        
        //CENTER:
        //the center pane is the getCurrent() method from Panes
        
        root = new BorderPane(mainPanes.getCurrent(), priceSelection, forwardButtonWrap, footerLabel, backButtonWrap);
        root.setAlignment(backButton, Pos.CENTER);
        root.setAlignment(forwardButton, Pos.CENTER);
        root.getStyleClass().add("rootPane");
        Scene scene = new Scene(root, 860, 630);
        scene.getStylesheets().add("mainStyle.css");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }
    
    private void backwards(ActionEvent e)
    {
        root.setCenter(mainPanes.prev());
    }
    
    private void forwards(ActionEvent e)
    {
        root.setCenter(mainPanes.next());
    }
    
    private void changedPriceRange()
    {
        mainPanes.updatePriceRange();
        root.setCenter(mainPanes.focusBoroughsPane());
        
        // if there are more than one active panes and any of the buttons is disabled
        // enable the buttons
        if(mainPanes.isMoreThanOnePaneActive() && forwardButton.isDisabled()) {
            forwardButton.setDisable(false);
            backButton.setDisable(false);
        }
    }
    
    public void setSelectedBorough(String boroughName)
    {
        data.setCurrentBorough(boroughName);
    }
    
    public String getSelectedBorough(String boroughName)
    {
        return data.getCurrentBorough();
    }
    
    public int getMinSelectedPrice()
    {
        //TODO: make this to get values from slider
        return (int) priceSlider.getLowValue();
    }
    
    public int getMaxSelectedPrice()
    {
        //TODO: make this to get values from slider
        return (int) priceSlider.getHighValue();
    }
    
    public AirbnbDataMap getData()
    {
        return data;
    }
    
    private void updatePriceLabel()
    {
        priceLabel.textProperty().setValue(
        "Currently showing properties from " + 
        String.valueOf((int) priceSlider.getLowValue()) + 
        " to " + 
        String.valueOf((int) priceSlider.getHighValue()));
    }
}
