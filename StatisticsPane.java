
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


/**
 * This class will show statistics for all properties in the currently selected
 * borough and price range. TODO: implement funcionality
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian
 *         Ghazanfar
 * @version 1.0
 */
public class StatisticsPane extends BorderPane {
    // whether the pane should be visible or not
    private boolean isActivated;
    // the current state of the data from the HashMap
    private AirbnbDataMap data;
    private int minPrice, maxPrice;
    private Statistics statistics;

    private ArrayList<Label> labels;
    private int counter;

    // format to two decimal places
    private DecimalFormat df;

    // TODO: MOVE THIS
    public static final int PANE_WIDTH = 700;
    public static final int PANE_HEIGHT = 500;

    /**
     * Constructor for objects of class StatisticsPane
     */
    public StatisticsPane() {
        // by default it should be seen - until a borough is specified
        isActivated = false;
        statistics = new Statistics();

        labels = new ArrayList<Label>();

        System.out.println(statistics.getAverageReviews());
        // GridPane gridPane = new GridPane();

        Label label1 = createLabel("Average reviews: " + format(statistics.getAverageReviews(), 2));
        Label label2 = createLabel("Total properties: " + format(statistics.getTotalProperties(), 0));
        Label label3 = createLabel("Home and Apartments: " + format(statistics.getHomeAndApartments(), 0));
        Label label4 = createLabel("Most expensive: " + format(statistics.getMostExpensive(), 0));
        Label label5 = createLabel("Properties within price range: " + format(statistics.getPropertiesWithinPriceRange(10, 1000), 0));
        Label label6 = createLabel("Suggested borough for price range: " + statistics.getSuggestedBorough(10, 1000));
        Label label7 = createLabel("Number of properties close to transportation: " + format(statistics.getPropertiesCloseToStation(), 0));
        Label label8 = createLabel("Most served borough (transportation):" + statistics.getMostServedBorough());

        // gridPane.add(label1, 0, 0);
        // gridPane.add(label2, 0, 1);
        // gridPane.add(label3, 1, 0);
        // gridPane.add(label4, 1, 1);
        // gridPane.add(label5, 0, 0);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            statisticsAnimation();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Indicate that the pane is activated. Invoked when a price range from the
     * RangeSlider is selected.
     */
    public void activate() {
        isActivated = true;
    }

    /**
     * If needed, deactivate the pane
     */
    public void deactivate() {
        isActivated = false;
    }

    /**
     * Check if the pane is active or not
     * 
     * @return true if the pane is active
     */
    public boolean isActivated() {
        return isActivated;
    }

    /**
     * Updates the statics Invoked when a new price range or a new borough has been
     * selected TODO: implement
     * 
     * @param min  the minimum price from the RangeSlider
     * @param max  the maximum price from the RangeSlider
     * @param data the current state of the data from the HashMaps
     */
    public void updateStatistics(int min, int max, AirbnbDataMap data) {
        this.data = data;
        minPrice = min;
        maxPrice = max;
        statistics.update();
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setMinWidth(PANE_WIDTH / 2);
        label.setMinHeight(PANE_HEIGHT / 2);
        labels.add(label);
        return label;
    }

    private void statisticsAnimation() {
        GridPane gridPane = new GridPane();

        gridPane.add(addLabel(), 0, 0);
        gridPane.add(addLabel(), 1, 0);
        gridPane.add(addLabel(), 0, 1);
        gridPane.add(addLabel(), 1, 1);
        counterOffset();
        setCenter(gridPane);
    }

    private Label addLabel() {
        counter++;

        return labels.get(counter % labels.size());
    }

    private void counterOffset() {
        counter -= 3;
    }

    private String format(Object number, int decimalPlaces) {
        try {
            if (decimalPlaces == 0) {
                try {
                    int castToInt = (int) number; 
                    System.out.println(castToInt);
                    return Integer.toString(castToInt);
                } catch (ClassCastException e) { 
                    Double doubleTest = (Double) number;
                    int castToInt = doubleTest.intValue();
                    return Integer.toString(castToInt);
                } catch(Exception e) {
                    System.out.println("Exception in trying to cast class " + number.getClass() + " to int.");
                    return "";
                }   
            }

            String format = ".";

            for (int i = 0; i < decimalPlaces; i++)
                format += "#";

            df = new DecimalFormat(format);
            return df.format(number);

        } catch (IllegalArgumentException e) {
            System.out.println("Illegal argument, not an instance of number.");
            return "";
        }
    }

}
