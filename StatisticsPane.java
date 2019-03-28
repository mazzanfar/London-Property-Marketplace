
//TODO: sort imports
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.*;
import org.controlsfx.control.RangeSlider;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.text.DecimalFormat;
import javafx.geometry.Pos;

/**
 * This class shows statistics for all properties in the currently selected
 * borough and price range.
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian
 *         Ghazanfar
 * @version 2.0
 */
public class StatisticsPane extends ExtendedBorderPane {
    private Statistics statistics;

    private ArrayList<Label> labels;
    private int counter;
    private int loadingCounter;

    // format to two decimal places
    private DecimalFormat df;

    private Timeline loadingAnimation;
    private Timeline statisticsAnimation;

    // TODO: MOVE THIS
    public static final int PANE_WIDTH = 700;
    public static final int PANE_HEIGHT = 500;

    /**
     * Constructor for objects of class StatisticsPane
     */
    public StatisticsPane(AirbnbDataMap data) {
        super(data);

        statistics = new Statistics();

        labels = new ArrayList<Label>();

        // set dimensions
        setPrefHeight(500);
        setMinHeight(500);
        setMaxHeight(500);
        setPrefWidth(700);
        setMinWidth(700);
        setMaxWidth(700);

        loadingAnimation = new Timeline(new KeyFrame(Duration.millis(500), e -> {
            loadingAnimation();
        }));

        statisticsAnimation = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            statisticsAnimation();
        }));

        loadingAnimation.setCycleCount(Animation.INDEFINITE);
        statisticsAnimation.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * This method is called every time the scroll bar is called. 
     * It is responsible for drawing the animations on the GUI.
     */
    public void updateView() {
        statisticsAnimation.stop();
        
        // Running the computations on a new thread allows the program to still function and not freeze while it is doing the computations. 
        // Since the computations are rather long, this is a very nice workaround.
        Thread sideCompute = new Thread(() -> {
            // start loading animation when calling the statistics methods 
            loadingAnimation.play();
            createLabel("Average reviews: " + format(statistics.getAverageReviews(), 2));
            createLabel("Total properties: " + format(statistics.getTotalProperties(), 0));
            createLabel("Home and Apartments: " + format(statistics.getHomeAndApartments(), 0));
            createLabel("Most expensive: " + format(statistics.getMostExpensive(), 0));
            createLabel(
                    "Properties within price range: " + format(statistics.getPropertiesWithinPriceRange(minPrice, maxPrice), 0));
            createLabel("Suggested borough for price range: " + statistics.getSuggestedBorough(minPrice, maxPrice));
            createLabel("Number of properties close to transportation: "
                    + format(statistics.getPropertiesCloseToStation(), 0));
            createLabel("Most served borough (transportation):" + statistics.getMostServedBorough());

            // stop animation and start displaying the statistics
            loadingAnimation.stop();

            statisticsAnimation.play();
        });

        sideCompute.start();
    }

    /**
     * Create a new label and add it to the labels list
     * 
     * @param String The text which should be displayed by the label
     * @return Label the created label
     */
    private Label createLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("statisticslabel");
        label.setMinWidth(PANE_WIDTH / 2);
        label.setMinHeight(PANE_HEIGHT / 2);
        labels.add(label);
        return label;
    }

    /**
     * Helper method for statistics timeline animation
     */
    private void statisticsAnimation() {
        GridPane gridPane = new GridPane();

        gridPane.add(addLabel(), 0, 0);
        gridPane.add(addLabel(), 1, 0);
        gridPane.add(addLabel(), 0, 1);
        gridPane.add(addLabel(), 1, 1);
        counterOffset();
        setCenter(gridPane);
    }

    /**
     * Helper method for loading timeline animation
     */
    private void loadingAnimation() {
        Label welcomeLabel1 = new Label(loadingString());
        welcomeLabel1.setWrapText(true);
        welcomeLabel1.getStyleClass().add("welcomeLabel1");

        Separator separator = new Separator();

        Label welcomeLabel2 = new Label("Loading, please wait.");
        welcomeLabel2.getStyleClass().add("welcomeLabel2");

        // put labels in a vertical box, center and set dimensions
        VBox welcomeWrap = new VBox(welcomeLabel1, separator, welcomeLabel2);
        welcomeWrap.setMaxWidth(400);
        welcomeWrap.setPrefHeight(500);
        welcomeWrap.setAlignment(Pos.CENTER);

        // set the center of this class to be the VBox created above
        setCenter(welcomeWrap);
    }

    /**
     * Create the animated loading string to display in the loading animation
     * depending on the loadingCounter
     * 
     * @return String The dynamic string to display on the loading screen
     */
    private String loadingString() {
        String loadString = "Loading";
        loadingCounter++;

        // append "" "." ".." "..." depending on loadingCounter (mod 4)
        for (int i = 0; i < loadingCounter % 4; i++) {
            loadString += ".";
        }

        return loadString;
    }

    /**
     * Add label to the statistics animated panel
     * 
     * @return Label The label to display on the statistics animated panel
     */
    private Label addLabel() {
        counter++;
        // return the label in the array which is in rotation depending on the counter
        // mod the cardinality of the labels
        return labels.get(counter % labels.size());
    }

    /**
     * Offset counter by -3
     */
    private void counterOffset() {
        // we want to offset the counter by 3 to create a "snake" animation with the
        // statistics display
        counter -= 3;
    }

    /**
     * Format the string to be displayed on the statistics screen.
     * 
     * @param Object A number-type object which can be casted to int or Double
     * @param int    The number of decimal places to show for the decimal number
     * 
     * @return String The formatted results
     */
    private String format(Object number, int decimalPlaces) {
        try {
            // if decimalPlaces is 0  we want to try to cast number to 
            // an int. 
            if (decimalPlaces == 0) {
                try {
                    int castToInt = (int) number;
                    return Integer.toString(castToInt);
                } catch (ClassCastException e) {
                    // assume number is of type Double
                    Double doubleTest = (Double) number;
                    int castToInt = doubleTest.intValue();
                    return Integer.toString(castToInt);
                } catch (Exception e) {
                    // was not able to cast to int
                    System.out.println("Exception in trying to cast class " + number.getClass() + " to int.");
                    return "";
                }
            }

            String format = ".";
            // insert number of decimal places inside the format for 
            // DecimalFormat constructor
            for (int i = 0; i < decimalPlaces; i++) {
                format += "#";
            }

            df = new DecimalFormat(format);
            return df.format(number);

        } catch (IllegalArgumentException e) {
            System.out.println("Illegal argument, not an instance of number.");
            return "";
        }
    }
}
