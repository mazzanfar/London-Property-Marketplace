import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.controlsfx.control.RangeSlider;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Main class for the application
 * instantiates starting window
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class PropertyViewerGUI extends Application
{
    private Stage stage;
    private Panes panes = new Panes();
    private BorderPane root;
    
    @Override
    public void start(Stage stage) throws Exception
    {
        this.stage = stage;
        //TOP:
        //TODO: pull prices from API
        RangeSlider priceSlider = new RangeSlider(0, 100, 10, 90);
        
        //LEFT:
        Button backButton = new Button("<");
        backButton.setOnAction(this::backwards);
            
        //RIGHT:
        Button forwardButton = new Button(">");
        forwardButton.setOnAction(this::forwards);
        
        //BOTTOM:
        Label footerLabel = new Label("copyright text info bla bla");
        
        //CENTER:
        //the center pane is the getCurrent() method from Panes
        
        root = new BorderPane(panes.getCurrent(), null , forwardButton, footerLabel, backButton);
        System.out.println(root.getCenter().toString());
        Scene scene = new Scene(root);
        stage.setTitle("AirBnB Property Viewer");
        stage.setScene(scene);

        // Show the Stage (window)
        stage.show();
    }
    
    private void backwards(ActionEvent e)
    {
        root.setCenter(panes.prev());
        System.out.println(root.getCenter().toString());
    }
    
    private void forwards(ActionEvent e)
    {
        root.setCenter(panes.next());
        System.out.println(root.getCenter().toString());
    }
}
