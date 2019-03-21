//TODO: sort imports
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * TODO: comment here
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class StatisticsPane extends ExtendedBorderPane
{
    /**
     * Constructor for objects of class StatisticsPane
     */
    public StatisticsPane(AirbnbDataMap data)
    {
        super(data);
        
        //placeholder image:
        Image image = new Image(getClass().getResourceAsStream("img/3.png"));
        Label label1 = new Label();
        label1.setGraphic(new ImageView(image));
        
        setCenter(label1);
    }
    
    /**
     * Update the pane to show properties within the new price range
     */
    protected void updateView()
    {
        //TODO: implement...
    }
}
