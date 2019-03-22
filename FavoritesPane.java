import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileReader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.TextAlignment;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import com.jfoenix.controls.JFXButton;
import java.util.ArrayList;
import javafx.stage.FileChooser;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import com.opencsv.CSVWriter;
import com.opencsv.CSVReader;
import java.net.URISyntaxException;
import javafx.scene.Scene;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.scene.control.Tooltip;
import java.lang.NullPointerException;

/**
 * This class represents the 'favorite properties' functionality of the app
 * It show all favorites that are marked as favorite in a list. The user can
 * also save all of the current favorite properties in a .csv file for future refence
 * and vice versa (the user can load .csv files and mark all properties found in them
 * as favorite and display them in a list)
 *
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class FavoritesPane extends ExtendedBorderPane
{
    // decalre static label prefixes for easier localization of the app:
    private static final String LOAD_PREFIX, SAVE_PREFIX, REFRESH_PREFIX,
                                FAVORITES_HEADER_PREFIX, FAVORITES_INFO_PREFIX,
                                GUIDE_DEFAULT_PREFIX, GUIDE_SECONDARY_PREFIX, ALERT_SAVING,
                                ALERT_SAVED, ALERT_SAVING_ERROR, ALERT_ERROR, ALERT_NO_FAVORITES,
                                ALERT_NO_FILE_ERROR, ALERT_INVALID_FILE_ERROR, ALERT_LOADING,
                                ALERT_LOADED, ALERT_EXCEPTION, ALERT_NULL_PROPERTY;
    static {
        LOAD_PREFIX = "Load";
        SAVE_PREFIX = "Save";
        REFRESH_PREFIX = "Refresh";
        FAVORITES_HEADER_PREFIX = "Favorite properties";
        FAVORITES_INFO_PREFIX = "You can view of your favorite properties here!";
        GUIDE_DEFAULT_PREFIX = "Oops! Looks like you don't have any favorite properties yet! \n" +
                               "If you have recently marked a property as favorite, please press the '"+ REFRESH_PREFIX + "' button to display it. \n" + 
                               "Alternatively you can press the '" + LOAD_PREFIX + "' button to load favorite properties from previous sessions.";
        GUIDE_SECONDARY_PREFIX = "Good! Looks like you have added some favorite properties. Don't forget to '" + REFRESH_PREFIX + "' every time you mark a new property as favorite. \n" +
                                 "You can also at any time '" + SAVE_PREFIX + "' all of your favorite properties for future reference.";    
        ALERT_SAVING = "Writing files to favorites.csv...";
        ALERT_SAVED = "Success! your properties have been saved to favorites.csv";
        ALERT_SAVING_ERROR = "You currently dont have any properties marked as favorite!";
        ALERT_ERROR = "Error!";
        ALERT_NO_FAVORITES = "There were no 'marked as favorite' properties found";
        ALERT_NO_FILE_ERROR = "No file has been selected";
        ALERT_INVALID_FILE_ERROR = "The .csv file that has been selected doesn't contain properties of valid format";
        ALERT_LOADING = "Loading favorite properties from ";
        ALERT_LOADED = "Successfully loaded all favorite properites from ";
        ALERT_EXCEPTION = "The exception stacktrace was: ";
        ALERT_NULL_PROPERTY = "The csv file provided contained unknown properties, aborting.";
    }
    
    // the wrapper that contains the header and the list of
    // all favorite properties
    private VBox wrapper;
    
    // a label that changes to instruct the user of how
    // the GUI functions
    private Label guideLabel;
    
    /**
     * Constructor for objects of class FavoritesPane
     */
    public FavoritesPane(AirbnbDataMap data)
    {
        // pass the data to the super-constructor
        super(data);
        
        // INIT header labels and buttons...
            // labels:
            Label favoritesHeader = new Label(FAVORITES_HEADER_PREFIX);
            favoritesHeader.getStyleClass().add("favoritesHeader");
            
            Label favoritesInfo = new Label(FAVORITES_INFO_PREFIX);
            favoritesInfo.getStyleClass().add("favoritesInfo");
            favoritesInfo.wrapTextProperty();
            
                    // buttons with images:
                    Image refreshImg = new Image(getClass().getResourceAsStream("/img/refresh.png"));
                    JFXButton refreshBtn = new JFXButton(REFRESH_PREFIX, new ImageView(refreshImg));
                    refreshBtn.setOnAction(this::refresh);
                    refreshBtn.getStyleClass().add("favoritesButton");
                    
                    Image loadImg = new Image(getClass().getResourceAsStream("/img/load.png"));
                    JFXButton loadBtn = new JFXButton(LOAD_PREFIX, new ImageView(loadImg));
                    loadBtn.setOnAction(this::load);
                    loadBtn.getStyleClass().add("favoritesButton");
                    
                    Image saveImg = new Image(getClass().getResourceAsStream("/img/save.png"));
                    JFXButton saveBtn = new JFXButton(SAVE_PREFIX, new ImageView(saveImg));
                    saveBtn.setOnAction(this::save);
                    saveBtn.getStyleClass().add("favoritesButton");
                    
                // wrap the buttons in a border pane fashion 
                BorderPane buttonWrap = new BorderPane(loadBtn, null, saveBtn, null, refreshBtn);
                buttonWrap.setPrefWidth(400);
                buttonWrap.setMinWidth(400);
                buttonWrap.setMaxWidth(400);
            
            // wrap the labels and buttons in a VBox, center, set dimensions
            VBox headerWrapper = new VBox(favoritesHeader, favoritesInfo, buttonWrap);
            headerWrapper.setAlignment(Pos.CENTER);
            headerWrapper.setPrefWidth(600);
            headerWrapper.setMaxWidth(600);
        
            // init the label that guides the user, indicating
            // all of the functionality supported
            guideLabel = new Label(GUIDE_DEFAULT_PREFIX);
            guideLabel.setMaxWidth(450);
            guideLabel.setWrapText(true);
            guideLabel.setTextAlignment(TextAlignment.CENTER);
            
            guideLabel.getStyleClass().add("guideLabel");
        
        // init wrapper, add header and guide label, center, add spacing
        wrapper = new VBox(headerWrapper, guideLabel);
        wrapper.setAlignment(Pos.TOP_CENTER);
        
        wrapper.setSpacing(30);
        
        // init scroll panes that handles VBox overflow, center,
        // set dimensions
        ScrollPane scrollWrapper = new ScrollPane();
        scrollWrapper.setContent(wrapper);
        scrollWrapper.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollWrapper.setMaxWidth(600);
        scrollWrapper.setPrefWidth(600);

            // a spacer label to provide space between the footerlabel
            // of the root border pane and this border pane
            Label spacerLabel = new Label();
            spacerLabel.setMinHeight(5);
            spacerLabel.setMaxHeight(5);
            
        // the scrollable VBox goes in the middle
        setCenter(scrollWrapper);
        // the spacer goes in the bottom
        setBottom(spacerLabel);
        // set this pane to be responisve to height changes
        setMaxHeight(USE_COMPUTED_SIZE);
        setPrefHeight(USE_COMPUTED_SIZE);
    }
    
    /**
     * abstract method from parent class
     */
    protected void updateView()
    {
        /* this method is called everytime there is a price range change
         * and this pane is independant of any price changes. No need to update anything.
         */
    }
    
    /**
     * This method is called from the referesh button
     * Refresh the displayed properties
     */
    private void refresh(ActionEvent e)
    {
        refresh();
    }

    /**
     * This method refreshes the displayed properties.
     * If a property has recently been marked as favorite, this method
     * will display it. Also used in other instances
     */
    private void refresh()
    {
        // check if the user has set any properties as favorite 
        if(!data.getFavoriteProperties().isEmpty()) {
            if(wrapper.getChildren().size() == 3) {
                // getting here means that the refresh button
                // has been clicked when there are already some proeperties 
                // being displayed. They need to be deleted from the GUI only
                // to make room for the refreshed view
                wrapper.getChildren().remove(2);
            }

            // add another VBox to the wrapper with a list of properties
            // (check PropertyLister class for details)
            wrapper.getChildren().add(new PropertyLister(data.getFavoriteProperties()));
            
            if(guideLabel.getText().equals(GUIDE_DEFAULT_PREFIX)) {
                guideLabel.setText(GUIDE_SECONDARY_PREFIX);
            }
        } else { // getting here means there are no properties
            // that have been set as favorite either by loading
            // from a .csv file or manually favoriting them
            if(wrapper.getChildren().size() == 3) {
                // getting here means that the user has 
                // unsuccesffuly tried to load properties, so
                // all favorites have been deleted, thus we need to remove
                // them from the view
                wrapper.getChildren().remove(2);
            }
            // Alert the user that there are no favorite properties found
            // as 'favorite'
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(ALERT_NO_FAVORITES);
            alert.setHeaderText(null);
            alert.setContentText(ALERT_NO_FAVORITES);
            alert.show();
        }
    }
    
    /**
     * This method gets called by the 'Load' button.
     * It tries to read properties from .csv file, mark them as favorite
     * and display them on this pane. Handles a lot of exceptions and alerts
     */
    private void load(ActionEvent e)
    {
        // open a file chooser that looks only for .csv files
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV Files", "*.csv");
        FileChooser csvChooser = new FileChooser();
        csvChooser.getExtensionFilters().add(csvFilter);
        File csvFile = csvChooser.showOpenDialog(this.getScene().getWindow());
        
        // the file can be null if 'Cancel' was clicked
        if(csvFile != null) {
            try {
                // create the reader
                CSVReader reader = new CSVReader(new FileReader(csvFile));
                String [] line = reader.readNext();
                //  the followin String array is used to check if the .csv file contains properties,
                // listed in the same way they are listed in the database
                String [] assertionStrArr = data.getFirstLineModel();
                boolean assertionCheck = true;
                
                // check if the first line of the file and the first line
                // of the model match
                for(int i = 0; i < assertionStrArr.length && i < line.length; i++) {
                    if(!line[i].equals(assertionStrArr[i])) {
                        assertionCheck = false;
                    }
                }
                if(assertionStrArr.length != line.length) {
                    assertionCheck = false; // make sure nothing dodged the check
                }

                // if they match
                if(assertionCheck) {
                    // alert the user that loading has began...
                    Alert alertLoading = new Alert(AlertType.INFORMATION);
                    alertLoading.setTitle(ALERT_LOADING);
                    alertLoading.setHeaderText(null);
                    alertLoading.setContentText(ALERT_LOADING + csvFile.getName());
                    alertLoading.show();

                    try { 
                        data.resetFavorites();
                        // try loading (possible null pointer exception if
                        // there are properties in the same format as the database
                        // but not in the database, thus can't be viewed or set as favorite)
                        while ((line = reader.readNext()) != null) {
                            // we can set a property as favorite only by the id
                            // and the neighbourhood (for faster look-up);
                            String id = line[0];
                            String neighbourhood = line[4];
                            data.setFavorite(neighbourhood, id); 
                        }

                        // loading is done... alert the user
                        alertLoading.setContentText(ALERT_LOADED + csvFile.getName());

                        // close the writer
                        reader.close();

                        // refresh this pane with all the new properties that were set as favorite
                        refresh();
                    } catch(NullPointerException nullExc) {
                        // getting here means that the .csv file had the correct first 
                        // line (header columns) but included properties that were
                        // not included in the database and can't be recognized

                        // alert the user of the error
                        Alert alertException = new Alert(AlertType.ERROR);
                        alertException.setTitle(ALERT_ERROR);
                        alertException.setHeaderText(ALERT_ERROR);
        
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        nullExc.printStackTrace(pw);
                        String exceptionText = sw.toString();
                        
                        Label label = new Label(ALERT_EXCEPTION);
                        
                        TextArea textArea = new TextArea(exceptionText);
                        textArea.setEditable(false);
                        textArea.setWrapText(true);
                        
                        textArea.setMaxWidth(Double.MAX_VALUE);
                        textArea.setMaxHeight(Double.MAX_VALUE);
                        GridPane.setVgrow(textArea, Priority.ALWAYS);
                        GridPane.setHgrow(textArea, Priority.ALWAYS);
                        
                        GridPane expContent = new GridPane();
                        expContent.setMaxWidth(Double.MAX_VALUE);
                        expContent.add(label, 0, 0);
                        expContent.add(textArea, 0, 1);
        
                        // Set expandable Exception into the dialog pane.
                        alertException.getDialogPane().setExpandableContent(expContent);
                        alertException.show();
                    }
                } else { // getting here means that the .csv file did not contain
                    // properties listed in the way they are listed in the database
                    // alert the user
                    Alert invalidFile = new Alert(AlertType.ERROR);
                    invalidFile.setTitle(ALERT_ERROR);
                    invalidFile.setHeaderText(null);
                    invalidFile.setContentText(ALERT_INVALID_FILE_ERROR);
                    invalidFile.show();
                }
            } catch(IOException exc){ // exceptions here can be
                // the file has been deleted, etc
                // alert the user with an expandable exception
                // that shows the stack trace
                Alert alertException = new Alert(AlertType.ERROR);
                alertException.setTitle(ALERT_ERROR);
                alertException.setHeaderText(ALERT_ERROR);

                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                exc.printStackTrace(pw);
                String exceptionText = sw.toString();
                
                Label label = new Label(ALERT_EXCEPTION);
                
                TextArea textArea = new TextArea(exceptionText);
                textArea.setEditable(false);
                textArea.setWrapText(true);
                
                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(textArea, Priority.ALWAYS);
                GridPane.setHgrow(textArea, Priority.ALWAYS);
                
                GridPane expContent = new GridPane();
                expContent.setMaxWidth(Double.MAX_VALUE);
                expContent.add(label, 0, 0);
                expContent.add(textArea, 0, 1);

                // Set expandable Exception into the dialog pane.
                alertException.getDialogPane().setExpandableContent(expContent);
                alertException.show();
            }
        } else { // getting here means 'Cancel' was clicked on the file chooser
            // alert the user that no file has been selected
            Alert alertNoFile = new Alert(AlertType.ERROR);
            alertNoFile.setTitle(ALERT_ERROR);
            alertNoFile.setHeaderText(null);
            alertNoFile.setContentText(ALERT_NO_FILE_ERROR);
            alertNoFile.show();
        }
    }
    
    /**
     * This method gets called when the 'Save' button gets clicked
     * It takes all of the user's currently 'marked as favorite' properties
     * and extracts them to a .csv file. Involves a lot of Exception and alert handling
     */
    private void save(ActionEvent e)
    { 
        // check if there are any favorite properties
        if(!data.getFavoriteProperties().isEmpty()) {
            // alert the user that the properties have began to save      
            Alert alertSaving = new Alert(AlertType.INFORMATION);
            alertSaving.setTitle(ALERT_SAVING);
            alertSaving.setHeaderText(null);
            alertSaving.setContentText(ALERT_SAVING);
            alertSaving.show();
            
            // the file name and location are set by default
            File file = new File("favorites.csv"); 
        
            List<String[]> dataToWrite = new ArrayList<String[]>(); 
            // get the model of the first line column headers from the data map
            dataToWrite.add(data.getFirstLineModel());

            try {
                // create FileWriter object with file as parameter 
                FileWriter outputfile = new FileWriter(file); 
        
                // create CSVWriter object
                CSVWriter writer = new CSVWriter(outputfile); 
                
                // iterate through all the favorite properties stored in the datamap
                for(AirbnbListing property : data.getFavoriteProperties()) {
                    dataToWrite.add(new String[] {property.getId(), property.getName(), property.getHost_id(),
                                                  property.getHost_name(), property.getNeighbourhood(), "" + property.getLatitude(),
                                                  "" + property.getLongitude(), property.getRoom_type(), "" + property.getPrice(), 
                                                  "" + property.getMinimumNights(), "" + property.getNumberOfReviews(), property.getLastReview(), 
                                                  "" + property.getReviewsPerMonth(), "" + property.getCalculatedHostListingsCount(), 
                                                  "" + property.getAvailability365()});
                }
                
                // write the String[]s to the csv file
                writer.writeAll(dataToWrite); 

                // close the writer
                writer.close();
                
                // alert the user that writing is complete
                alertSaving.setContentText(ALERT_SAVED);
            } catch (IOException ex) { // possible exceptions are: file is being used, etc.
                // alert the user that there was an error
                Alert alertException = new Alert(AlertType.ERROR);
                alertException.setTitle(ALERT_ERROR);
                alertException.setHeaderText(ALERT_ERROR);
                
                // Create expandable Exception.
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                // include the stack track of the exception
                ex.printStackTrace(pw);
                String exceptionText = sw.toString();
                
                Label label = new Label(ALERT_EXCEPTION);
                
                TextArea textArea = new TextArea(exceptionText);
                textArea.setEditable(false);
                textArea.setWrapText(true);
                
                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(textArea, Priority.ALWAYS);
                GridPane.setHgrow(textArea, Priority.ALWAYS);
                
                GridPane expContent = new GridPane();
                expContent.setMaxWidth(Double.MAX_VALUE);
                expContent.add(label, 0, 0);
                expContent.add(textArea, 0, 1);

                // Set expandable Exception into the dialog pane.
                alertException.getDialogPane().setExpandableContent(expContent);
                alertException.show();
            } 
        } else { // getting here means that there were no favorite properties found
            // alert the user
            Alert alertEmpty = new Alert(AlertType.INFORMATION);
            alertEmpty.setTitle(ALERT_ERROR);
            alertEmpty.setHeaderText(null);
            alertEmpty.setContentText(ALERT_SAVING_ERROR);
            alertEmpty.show();
        }
    } 
}
