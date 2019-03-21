//TODO: sort imports
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


/**
 * TODO: comment here
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class MapPane extends ExtendedBorderPane
{    
    /**
     * Constructor for objects of class MapPane
     */
    public MapPane(AirbnbDataMap data)
    {
        super(data);

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("googlemap.html").toString());
        
        setCenter(webView);
    }
    
    /**
     * Update the pane to show properties within the new price range
     */
    protected void updateView()
    {
        //TODO: implement...
    }
}
