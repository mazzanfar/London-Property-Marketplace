//TODO: sort out imports
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.controlsfx.control.RangeSlider;
import com.jfoenix.controls.JFXButton;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import javafx.fxml.*;
import java.net.URL;
import java.io.File;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import javafx.scene.control.ScrollPane;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList; 
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.*;
import java.util.Set;
import java.util.Iterator;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * TODO: comment here
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class BoroughsPane extends ExtendedBorderPane
{    
    // decalre static label prefixes for easier localization of the app:
    private static final String LOW_PREFIX, HIGH_PREFIX, PROP_COUNT_PREFIX;
    static {
         LOW_PREFIX = "Low";
         HIGH_PREFIX = "High";
         PROP_COUNT_PREFIX = "Property count";
    }
    
    private Stage popUpWin;
    
    private ArrayList<BoroughButton> boroughButtons;

    private AnchorPane buttonWrap;
    
    /**
     * Constructor for objects of class BoroughsPane
     */
    public BoroughsPane(AirbnbDataMap data)
    {
        super(data);
        
        boroughButtons = new ArrayList<BoroughButton>();
        
        ArrayList<String> boroughs = data.getGeoOrderedBoroughs();
        
        for(String boroughName : boroughs) {
            String btnText = boroughName.substring(0, 4).toUpperCase();
            
            BoroughButton btn = new BoroughButton(btnText, boroughName);
            
            btn.setOnAction(this::setSelectedBorough);
            
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
            boroughButtons.add(btn);
        }
        
        buttonWrap = new AnchorPane();
        buttonWrap.getChildren().addAll(boroughButtons);
        
        positionButtons();
        
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
            
        BorderPane heatMapLegendWrap = new BorderPane(propCount, null, highLabel, heatMapLegend, lowLabel);
        heatMapLegendWrap.setPrefWidth(250);
        heatMapLegendWrap.setMaxWidth(150);
        
        setCenter(buttonWrap);
        setBottom(heatMapLegendWrap);
        setAlignment(heatMapLegendWrap, Pos.TOP_CENTER);
        
        popUpWin = new Stage();
        
        setMinHeight(480);
        setMaxHeight(480);
        setPrefHeight(480);
    }

    /**
     * Updates the heat map of boroughs based on availability of properties
     * in the currently selected price range.
     * 
     * TODO: implement
     */
    protected void updateView()
    {
        int propertyCount = 0;
        for(BoroughButton btn : boroughButtons) {
            propertyCount = data.getWithinPriceRange(btn.getBoroughOfButton(), minPrice, maxPrice).size();
            if(propertyCount == 0) {
                btn.setDisable(true);
            } else {
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
        
        popUpWin.close();
    }
    
    /**
     * Alrets the class controlling the other panes that
     * the selected borough has changed so that the other panes can
     * be updated accordingly.
     * 
     * @param name the name of the newly selected borough
     */
    private void setSelectedBorough(ActionEvent e) 
    {   
        BoroughButton btn = (BoroughButton) e.getSource();
        data.setCurrentBorough(btn.getBoroughOfButton());
        
        PropertyListWindow propList = new PropertyListWindow(data);
        Pane newWinRoot = propList.getView();
        
        Scene newScene = new Scene(newWinRoot);
        newScene.getStylesheets().add("propertyListStyle.css");
        popUpWin.setScene(newScene);
        
        popUpWin.setTitle("Properties in: " + btn.getBoroughOfButton());
        popUpWin.show();
    }

    private void positionButtons()
    {
        boolean grid[][] = new boolean[][] {
            {false,  false,  false,  false,  false,  false,  false,  true,   false,  false,  false,  false,  false,  false},
            {false,  false,  false,  false,  true,   false,  true,   false,  true,   false,  false,  false,  false,  false},
            {false,  true,   false,  true,   false,  true,   false,  true,   false,  true,   false,  true,   false,  true },
            {true,   false,  true,   false,  true,   false,  true,   false,  true,   false,  true,   false,  true,   false},
            {false,  true,   false,  true,   false,  true,   false,  true,   false,  true,   false,  true,   false,  false},
            {false,  false,  true,   false,  true,   false,  true,   false,  true,   false,  true,   false,  false,  false},
            {false,  false,  false,  true,   false,  true,   false,  true,   false,  true,   false,  false,  false,  false},
        };

        int btnIndex = 0;

        for(int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if(grid[i][j]) {
                    boroughButtons.get(btnIndex).setLayoutX(j*37 + 80);
                    boroughButtons.get(btnIndex).setLayoutY(i*59);
                    btnIndex ++;
                }
            }
        }
    }
}
