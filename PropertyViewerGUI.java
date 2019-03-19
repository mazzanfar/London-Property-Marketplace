
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

/**
 * This class is the main class of the application Control everything
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian
 *         Ghazanfar
 * @version 1.0
 */
public class PropertyViewerGUI extends Application {
    // All of the panes that will cycle in the center of the screen
    private MainPanes mainPanes;
    // the root
    private BorderPane root;
    // get the data from the csv
    private AirbnbDataMap data;
    // used for selecing a price range
    private RangeSlider priceSlider;
    // buttons
    private Button forwardButton, backButton;

    @Override
    public void start(Stage stage) {
        // get data and init panes
        data = new AirbnbDataMap();
        mainPanes = new MainPanes(this);

        // TOP:
        int min = data.getMinPrice();
        int max = data.getMaxPrice();
        priceSlider = new RangeSlider(min, max, min, max);
        priceSlider.setBlockIncrement(100);
        priceSlider.setShowTickMarks(true);
        priceSlider.setShowTickLabels(true);
        // priceSlider.setSnapToTicks(true);
        priceSlider.setMajorTickUnit(max - min);
        // TODO: implement event listeners to call the changedPriceRange method
        Label currentPrice = new Label("make this show selected price");
        VBox priceDetails = new VBox(priceSlider, currentPrice);
        priceDetails.setId("priceDetails");
        priceDetails.setAlignment(Pos.CENTER);

        // LEFT:
        backButton = new Button("<");
        backButton.setOnAction(this::backwards);
        backButton.setPrefSize(40, 40);
        backButton.setId("mainWinButtons");
        BorderPane leftBtn = new BorderPane();
        // center it byputting it in a border pane
        leftBtn.setCenter(backButton);
        leftBtn.setId("leftBtn");

        // RIGHT:
        forwardButton = new Button(">");
        forwardButton.setOnAction(this::forwards);
        forwardButton.setPrefSize(40, 40);
        forwardButton.setId("mainWinButtons");
        BorderPane rightBtn = new BorderPane();
        // center it by putting it in a broder pane
        rightBtn.setCenter(forwardButton);
        rightBtn.setId("rightBtn");

        // disable buttons if there is only 1 active pane
        if (!mainPanes.isMoreThanOnePaneActive()) {
            forwardButton.setDisable(true);
            backButton.setDisable(true);
        }

        // TODO: remove this method from here and make it be invoked
        // based on events from the price slider
        // the requirements specify that only the welcome pane
        // should be shown until a price is selected and this method
        // will make other panes visible
        changedPriceRange();

        // BOTTOM:
        Label footerLabel = new Label("copyright text info bla bla");
        footerLabel.setId("footerLabel");

        // CENTER:
        // the center pane is the getCurrent() method from Panes

        root = new BorderPane(mainPanes.getCurrent(), priceDetails, rightBtn, footerLabel, leftBtn);

        Scene scene = new Scene(root, 860, 630);
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    private void backwards(ActionEvent e) {
        root.setCenter(mainPanes.prev());
    }

    private void forwards(ActionEvent e) {
        root.setCenter(mainPanes.next());
    }

    private void changedPriceRange() {
        // TODO: implement this to get values from slider
        mainPanes.updatePriceRange();

        // if there are more than one active panes and any of the buttons is disabled
        // enable the buttons
        if (mainPanes.isMoreThanOnePaneActive() && forwardButton.isDisabled()) {
            forwardButton.setDisable(false);
            backButton.setDisable(false);
        }
    }

    public void setSelectedBorough(String boroughName) {
        data.setCurrentBorough(boroughName);
    }

    public String getSelectedBorough(String boroughName) {
        return data.getCurrentBorough();
    }

    public int getMinSelectedPrice() {
        // TODO: make this to get values from slider
        return 50;
    }

    public int getMaxSelectedPrice() {
        // TODO: make this to get values from slider
        return 100;
    }

    public AirbnbDataMap getData() {
        return data;
    }
}
