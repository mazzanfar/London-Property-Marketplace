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

/**
 * Write a description of class FavoritesPane here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class FavoritesPane extends ExtendedBorderPane
{
    // decalre static label prefixes for easier localization of the app:
    private static final String LOAD_PREFIX, SAVE_PREFIX, REFRESH_PREFIX,
                                FAVORITES_HEADER_PREFIX, FAVORITES_INFO_PREFIX,
                                GUIDE_DEFAULT_PREFIX, GUIDE_SECONDARY_PREFIX,
                                VIEWBTN_PREFIX, HOST_NAME_PREFIX, PRICE_PREFIX,
                                MINIMUM_NIGHTS_PREFIX, REVIEWS_PREFIX, ALERT_SAVING,
                                ALERT_SAVED, ALERT_SAVING_ERROR, ALERT_ERROR, ALERT_NO_FAVORTIES,
                                ALERT_NO_FILE_ERROR, ALERT_INVALID_FILE_ERROR, ALERT_LOADING,
                                ALERT_LOADED, ALERT_EXCEPTION, FAVORITE_COUNT_PREFIX, VIEWBTN_TOOLTIP;
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
        VIEWBTN_PREFIX = "View Details";
        HOST_NAME_PREFIX =  "Name of the property's host: ";
        PRICE_PREFIX = "Price: ";
        MINIMUM_NIGHTS_PREFIX = "Minimum number of nights: ";
        REVIEWS_PREFIX = "Number of reviews: ";
        ALERT_SAVING = "Writing files to favorites.csv...";
        ALERT_SAVED = "Success! your properties have been saved to favorites.csv";
        ALERT_SAVING_ERROR = "You currently dont have any properties marked as favorite!";
        ALERT_ERROR = "Error!";
        ALERT_NO_FAVORTIES = "There were no 'marked as favorite' properties found";
        ALERT_NO_FILE_ERROR = "No file has been selected";
        ALERT_INVALID_FILE_ERROR = "The .csv file that has been selected doesn't contain properties of valid format";
        ALERT_LOADING = "Loading favorite properties from ";
        ALERT_LOADED = "Successfully loaded all favorite properites from ";
        ALERT_EXCEPTION = "The exception stacktrace was:";
        FAVORITE_COUNT_PREFIX = " favorite properties";
        VIEWBTN_TOOLTIP = "View property ";
    }
    
    private ArrayList<AirbnbListing> favoriteProperties;
    
    private VBox wrapper;
    
    private Stage detailsPopUpWin;
    
    private Label guideLabel;
    
    /**
     * Constructor for objects of class FavoritesPane
     */
    public FavoritesPane(AirbnbDataMap data)
    {
        super(data);
        favoriteProperties = new ArrayList<AirbnbListing>();
        
            Label favoritesHeader = new Label(FAVORITES_HEADER_PREFIX);
            favoritesHeader.getStyleClass().add("favoritesHeader");
            
            Label favoritesInfo = new Label(FAVORITES_INFO_PREFIX);
            favoritesInfo.getStyleClass().add("favoritesInfo");
            favoritesInfo.wrapTextProperty();
            
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
                    
                    
                BorderPane buttonWrap = new BorderPane(loadBtn, null, saveBtn, null, refreshBtn);
                buttonWrap.setPrefWidth(400);
                buttonWrap.setMinWidth(400);
                buttonWrap.setMaxWidth(400);
            
        
            VBox headerWrapper = new VBox(favoritesHeader, favoritesInfo, buttonWrap);
            headerWrapper.setAlignment(Pos.CENTER);
            headerWrapper.setPrefWidth(600);
            headerWrapper.setMaxWidth(600);
        
            guideLabel = new Label(GUIDE_DEFAULT_PREFIX);
            guideLabel.setMaxWidth(450);
            guideLabel.setWrapText(true);
            guideLabel.setTextAlignment(TextAlignment.CENTER);
            
            guideLabel.getStyleClass().add("guideLabel");
            
        wrapper = new VBox(headerWrapper, guideLabel);
        wrapper.setAlignment(Pos.TOP_CENTER);
        
        wrapper.setSpacing(30);
        
        ScrollPane scrollWrapper = new ScrollPane();
        scrollWrapper.setContent(wrapper);
        scrollWrapper.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollWrapper.setMaxWidth(600);
        scrollWrapper.setPrefWidth(600);
            Label spacerLabel = new Label();
            spacerLabel.setMinHeight(5);
            spacerLabel.setMaxHeight(5);
            
        setCenter(scrollWrapper);
        setBottom(spacerLabel);
        setMaxHeight(USE_COMPUTED_SIZE);
        setPrefHeight(USE_COMPUTED_SIZE);
        
        detailsPopUpWin = new Stage();
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
    
    private void refresh(ActionEvent e)
    {
        refresh();
    }

    private void refresh()
    {
        favoriteProperties = data.getFavoriteProperties();
        if(!favoriteProperties.isEmpty()) {
            int count = 0;
            for(AirbnbListing property : favoriteProperties) {
                        Label hostName = new Label(HOST_NAME_PREFIX + property.getHost_name());
                        hostName.setWrapText(true);
                        hostName.getStyleClass().add("propertyLabel");
                        
                        Label propertyPrice = new Label(PRICE_PREFIX + property.getPrice() + "£");
                        propertyPrice.getStyleClass().add("propertyLabel");
                        
                        Label numberOfReviews = new Label(REVIEWS_PREFIX + property.getNumberOfReviews());
                        numberOfReviews.getStyleClass().add("propertyLabel");
                        
                        Label minimumNights = new Label(MINIMUM_NIGHTS_PREFIX + property.getMinimumNights());
                        minimumNights.getStyleClass().add("propertyLabel");
                        
                    VBox detailsWrapper = new VBox(hostName, propertyPrice, numberOfReviews, minimumNights);
                    detailsWrapper.setMaxWidth(300);
                    detailsWrapper.setMinWidth(300);
                    detailsWrapper.setPrefWidth(300);
                    detailsWrapper.setSpacing(20);
                
                        JFXButton viewBtn = new JFXButton(VIEWBTN_PREFIX);
                        viewBtn.setTooltip(new Tooltip(VIEWBTN_TOOLTIP + property.getId()));
                        viewBtn.setOnAction(this::viewProperty);
                        viewBtn.getStyleClass().add("favoritesButton");
                        
                    VBox viewBtnWrapper = new VBox(viewBtn);
                    viewBtnWrapper.setMaxWidth(200);
                    viewBtnWrapper.setMinWidth(200);
                    viewBtnWrapper.setPrefWidth(200);
                    viewBtnWrapper.setAlignment(Pos.CENTER);
                    
                HBox propertyWrapper = new HBox(detailsWrapper, viewBtnWrapper);
                propertyWrapper.setMaxWidth(500);
                propertyWrapper.setPrefWidth(500);
                propertyWrapper.getStyleClass().add("propertyWrapper");
                
                wrapper.getChildren().add(propertyWrapper);
                System.out.println("done with " + count);
                count ++;
            }
            
            Label favoriteCount = new Label("" + favoriteProperties.size() + FAVORITE_COUNT_PREFIX);
            wrapper.getChildren().add(favoriteCount);
            
            if(guideLabel.getText().equals(GUIDE_DEFAULT_PREFIX)) {
                guideLabel.setText(GUIDE_SECONDARY_PREFIX);
            }
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(ALERT_NO_FAVORTIES);
            alert.setHeaderText(null);
            alert.setContentText(ALERT_NO_FAVORTIES);
            
            alert.show();
        }
    }
    
    private void load(ActionEvent e)
    {
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV Files", "*.csv");
        FileChooser csvChooser = new FileChooser();
        csvChooser.getExtensionFilters().add(csvFilter);
        File csvFile = csvChooser.showOpenDialog(this.getScene().getWindow());
        
        if(csvFile != null) {
            try {
                CSVReader reader = new CSVReader(new FileReader(csvFile));
                String [] line = reader.readNext();
                String [] assertionStrArr = {"id", "name", "host_id", "host_name", "neighbourhood", "latitude", "longitude", 
                                             "room_type", "price", "minimum_nights", "number_of_reviews", "last_review", 
                                             "reviews_per_month", "calculated_host_listings_count", "availability_365"};
                boolean assertionCheck = true;
                
                for(int i = 0; i < assertionStrArr.length; i++) {
                    if(!line[i].equals(assertionStrArr[i])) {
                        assertionCheck = false;
                    }
                }
                
                if(assertionCheck) {
                    Alert alertLoading = new Alert(AlertType.INFORMATION);
                    alertLoading.setTitle(ALERT_LOADING);
                    alertLoading.setHeaderText(null);
                    alertLoading.setContentText(ALERT_LOADING + csvFile.getName());
                    alertLoading.show();
                    int count = 0;
                    while ((line = reader.readNext()) != null) {
                        String id = line[0];
                        String neighbourhood = line[4];
                        data.setFavorite(neighbourhood, id);
                        System.out.println(count);
                        count ++;
                    }
                    alertLoading.setContentText(ALERT_LOADED + csvFile.getName());
                    refresh();
                } else {
                    Alert invalidFile = new Alert(AlertType.ERROR);
                    invalidFile.setTitle(ALERT_ERROR);
                    invalidFile.setHeaderText(null);
                    invalidFile.setContentText(ALERT_INVALID_FILE_ERROR);
                    
                    invalidFile.show();
                }
    
            } catch(IOException exc){
                Alert alertException = new Alert(AlertType.ERROR);
                alertException.setTitle(ALERT_ERROR);
                alertException.setHeaderText(ALERT_ERROR);
                
                // Create expandable Exception.
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
        } else {
            Alert alertNoFile = new Alert(AlertType.ERROR);
            alertNoFile.setTitle(ALERT_ERROR);
            alertNoFile.setHeaderText(null);
            alertNoFile.setContentText(ALERT_NO_FILE_ERROR);
            
            alertNoFile.show();
        }
    }
    
    private void save(ActionEvent e)
    { 
        if(!favoriteProperties.isEmpty()) {      
            Alert alertSaving = new Alert(AlertType.INFORMATION);
            alertSaving.setTitle(ALERT_SAVING);
            alertSaving.setHeaderText(null);
            alertSaving.setContentText(ALERT_SAVING);
            
            alertSaving.show();
            
            File file = new File("favorites.csv"); 
        
            List<String[]> dataToWrite = new ArrayList<String[]>(); 
            dataToWrite.add(new String[] { "id", "name", "host_id", "host_name", "neighbourhood", "latitude", "longitude", 
                                           "room_type", "price", "minimum_nights", "number_of_reviews", "last_review", 
                                           "reviews_per_month", "calculated_host_listings_count", "availability_365"});

            try { 
                // create FileWriter object with file as parameter 
                FileWriter outputfile = new FileWriter(file); 
        
                // create CSVWriter object filewriter object as parameter 
                CSVWriter writer = new CSVWriter(outputfile); 
        
                for(AirbnbListing property : favoriteProperties) {
                    dataToWrite.add(new String[] {property.getId(), property.getName(), property.getHost_id(),
                                                  property.getHost_name(), property.getNeighbourhood(), "" + property.getLatitude(),
                                                  "" + property.getLongitude(), property.getRoom_type(), "" + property.getPrice(), 
                                                  "" + property.getMinimumNights(), "" + property.getNumberOfReviews(), property.getLastReview(), 
                                                  "" + property.getReviewsPerMonth(), "" + property.getCalculatedHostListingsCount(), 
                                                  "" + property.getAvailability365()});
                }
                
                writer.writeAll(dataToWrite); 

                writer.close(); 
                alertSaving.setContentText(ALERT_SAVED);
            }
            catch (IOException ex) {
                Alert alertException = new Alert(AlertType.ERROR);
                alertException.setTitle(ALERT_ERROR);
                alertException.setHeaderText(ALERT_ERROR);
                
                // Create expandable Exception.
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                String exceptionText = sw.toString();
                
                Label label = new Label("The exception stacktrace was:");
                
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
        } else {
            Alert alertEmpty = new Alert(AlertType.INFORMATION);
            alertEmpty.setTitle(ALERT_ERROR);
            alertEmpty.setHeaderText(null);
            alertEmpty.setContentText(ALERT_SAVING_ERROR);
            alertEmpty.show();
        }
    } 
    
    private void viewProperty(ActionEvent e)
    {
        JFXButton btn = (JFXButton) e.getSource();
        
        String id = btn.getTooltip().getText().substring(VIEWBTN_TOOLTIP.length(), btn.getTooltip().getText().length());
        
        PropertyDetailsWindow propList = new PropertyDetailsWindow(data.getPropertyById(id));
        Pane newWinRoot = propList.getView();
        
        Scene newScene = new Scene(newWinRoot, 350, 350);
        newScene.getStylesheets().add("propertyDetailsStyle.css");
        detailsPopUpWin.setScene(newScene);
        
        detailsPopUpWin.setTitle("Showing details for property id: " + id);
        detailsPopUpWin.show();
    }
}
