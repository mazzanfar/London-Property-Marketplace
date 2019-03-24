import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import com.opencsv.CSVReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.lang.NullPointerException;
/**
 * This class handles the database for the app. It loads the .csv file
 * and maps the data in a HashMap, using boroughs as keys. That helps with 
 * a lot of algorithms, as they can be executed faster if they are narrowed down
 * to boroughs and also helps with the borough buttons in the BoroughsPane
 * 
 * The class itself extends HashMap
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class AirbnbDataMap extends HashMap<String, ArrayList<AirbnbListing>>
{
    // the lowest and highest priced properties
    private int minPrice, maxPrice;
    // the currently selected borough (from the GUI);
    private String currentSelectedBorough;
    // all the boroughs indexed with respect to their geographical location
    private ArrayList<String> geoOrderedBoroughs;
    // the favorites list
    private ArrayList<AirbnbListing> favorites;
    // the header names of all columns
    private String [] firstLine;
    
    /**
     * Construct the map by loading the data in it
     */
    public AirbnbDataMap()
    {
        // order boroughs geographically (read top to bottom, left to right)
        // from the provided png imate
        geographicallyOrderBoroughs();

        // read the data from the .csv file and map it
        // using neighbourhood as keys
        load();

        // init an arraylist to store favorite properties
        favorites = new ArrayList<AirbnbListing>();
    }
    
    /**
     * Cheapest property across all
     * @return the minimum price found in data
     */
    public int getMinPrice()
    {
        return minPrice;
    }
    
    /**
     * Most exepnsive property across all
     * @return the maximum price found in data
     */
    public int getMaxPrice()
    {
        return maxPrice;
    }
    
    /**
     * Invoked when the user selects a new borough
     * @param newBorough the name of the newly selected neighbourhood
     */
    public void setCurrentBorough(String newBorough)
    {
        currentSelectedBorough = newBorough;
    }
    
    /**
     * Accessor method for the currently selected borough
     * by the user
     * @return the name of the selected neighbourhood
     */
    public String getCurrentBorough()
    {
        return currentSelectedBorough;
    }
    
    /**
     * This method implements no additional functionality,
     * however it helps for readability (it is the same as calling
     * the keySet() method on an object of this class).
     * @return a set of all boroughs provided in the data
     */
    public Set<String> getDistinctBoroughsSet()
    {
        return keySet();
    }
    
    /**
     * A method to get an array list of all properties in a borough
     * @param name the name of the borough to fetch data from
     * @return an ArrayList of listings of properties
     */
    public ArrayList<AirbnbListing> getListingsInBorough(String name)
    {
        return get(name);
    }
    
    /**
     * Returns the defined geographic order of boroughs
     * @return the geographic order of boroughs
     */
    public ArrayList<String> getGeoOrderedBoroughs()
    {
        return geoOrderedBoroughs;
    }
    
    /**
     * Return all properties within a specific price range, also 
     * narrowing it down to a borough for faster execution
     * @param key the borough to get properties from
     * @param min the minimum price of properties
     * @param max the maximum price of properties
     * @return an array list of listings with properties matching the description
     */
    public ArrayList<AirbnbListing> getWithinPriceRange(String key, int min, int max)
    {
        ArrayList<AirbnbListing> result = new ArrayList<AirbnbListing>();
        
        // iterate...
        for(AirbnbListing listing : get(key))  {
            if(listing.getPrice() <= max && listing.getPrice() >= min) {
                result.add(listing);
            }
        }
        return result;
    }
    
    /**
     * Return the number of properties within a specific price range
     * @param min the minimum price
     * @param max the maximum price
     * @return the number of properties within a price range
     */
    public int getWithinPriceRange(int min, int max)
    {
        int count = 0;
        
        // iterate...
        for(String key : keySet())  {
            for(AirbnbListing listing : get(key)) { 
                if(listing.getPrice() <= max && listing.getPrice() >= min) {
                    count ++;
                }
            }
        }
        return count;
    }

    /**
     * Get a property based on a provided id
     * @param id provided id
     * @return the property with a matching id, null if none such exists
     */
    public AirbnbListing getPropertyById(String id)
    {
        // iterate...
        for(String key : keySet()) {
            for(AirbnbListing listing : get(key)) {
                if(listing.getId().equals(id)) {
                    return listing;
                }
            }
        }
        
        return null;
    }

    /**
     * Get the model of the .csv file. In other words
     * the first line of the csv file, which included column headers
     * @return String array with all column headers from loaded csv
     */
    public String[] getFirstLineModel()
    {
        return firstLine;
    }


    /**
     * Sets a favorite property. Also specifies borough for faster
     * execution. No need to look through all properties, just the ones
     * in the given broough and look for its unique id
     * 
     * Throws NullPointerException if the provided details are invalid.
     * 
     * @param neighbourhood the borough the property should be located in
     * @param id the unique id of the property that is to be matched
     * 
     */
    public void setFavorite(String neighbourhood, String id) throws NullPointerException
    {
        // iterate...
        for(AirbnbListing listing : get(neighbourhood)) {
            // possibly throw exception
            if(listing == null) throw new NullPointerException(id);
            if(id.equals(listing.getId())) {
                if(!listing.isFavorite()) {
                    listing.setFavorite();
                    favorites.add(listing);
                    return;
                }
            }   
        }
    }
    
    /**
     * Clears the favorites private array list and unmarks all listings
     * as 'favorite'
     */
    public void resetFavorites()
    {
        // the method only needs to execute if there are any free properties
        if(!favorites.isEmpty()) {
            favorites.clear();
            // iterate...
            for(String key : keySet())  {
                for(AirbnbListing listing : get(key)) { 
                    listing.removeFavorite();
                }
            }
        }
    }

    /**
     * Get access to all favorite properties
     * @return an array list including only properties that are marked as favorite
     */
    public ArrayList<AirbnbListing> getFavoriteProperties()
    {
        return favorites;
    }
    
    /**
     * Look for newly added favorites and add them to the 
     * array list. Clear before to avoid duplicate entries
     */
    public void lookForFavorites()
    {
        // clear to avoid duplicate entries
        favorites.clear();
        
        // iterate..
        for(String key : keySet()) {
            for(AirbnbListing listing : get(key)) {
                if(listing.isFavorite()) {
                    favorites.add(listing);
                }
            }
        }
    }

    /**
     * Set the geographic order of the boroughs
     * Used when creating the buttons in the BoroughsPane
     */
    private void geographicallyOrderBoroughs()
    {
        geoOrderedBoroughs = new ArrayList<String>();
        geoOrderedBoroughs.add("Enfield");
        geoOrderedBoroughs.add("Barnet");
        geoOrderedBoroughs.add("Haringey");
        geoOrderedBoroughs.add("Waltham Forest");
        geoOrderedBoroughs.add("Harrow");
        geoOrderedBoroughs.add("Brent");
        geoOrderedBoroughs.add("Camden");
        geoOrderedBoroughs.add("Islington");
        geoOrderedBoroughs.add("Hackney");
        geoOrderedBoroughs.add("Redbridge");
        geoOrderedBoroughs.add("Havering");
        geoOrderedBoroughs.add("Hillingdon");
        geoOrderedBoroughs.add("Ealing");
        geoOrderedBoroughs.add("Kensington and Chelsea");
        geoOrderedBoroughs.add("Westminster");
        geoOrderedBoroughs.add("Tower Hamlets");
        geoOrderedBoroughs.add("Newham");
        geoOrderedBoroughs.add("Barking and Dagenham");
        geoOrderedBoroughs.add("Hounslow");
        geoOrderedBoroughs.add("Hammersmith and Fulham");
        geoOrderedBoroughs.add("Wandsworth");
        geoOrderedBoroughs.add("City of London");
        geoOrderedBoroughs.add("Greenwich");
        geoOrderedBoroughs.add("Bexley");
        geoOrderedBoroughs.add("Richmond upon Thames");
        geoOrderedBoroughs.add("Merton");
        geoOrderedBoroughs.add("Lambeth");
        geoOrderedBoroughs.add("Southwark");
        geoOrderedBoroughs.add("Lewisham");
        geoOrderedBoroughs.add("Kingston upon Thames");
        geoOrderedBoroughs.add("Sutton");
        geoOrderedBoroughs.add("Croydon");
        geoOrderedBoroughs.add("Bromley");
    }

    /** 
     * Load the CSV file and iterate through every row, storing data in 
     * an AirbnbListing object and map boroughs to properties with a HashMap
     * (this class extends HashMap)
     */
    private void load() {
        try {
            URL url = getClass().getResource("airbnb-london.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] line;
            // save the header columns for future reference
            firstLine = reader.readNext();
            while ((line = reader.readNext()) != null) {

                String id = line[0];
                String name = line[1];
                String host_id = line[2];
                String host_name = line[3];
                String neighbourhood = line[4];
                double latitude = convertDouble(line[5]);
                double longitude = convertDouble(line[6]);
                String room_type = line[7];
                int price = convertInt(line[8]);

                if(isEmpty()) { // the first iteration
                    maxPrice = price;
                    minPrice = price;
                } else {
                    if(price > maxPrice) { // keep track of maxPrice
                        maxPrice = price;
                    } else if(price < minPrice) { // keep track of minPrice
                        minPrice = price;
                    }
                }

                int minimumNights = convertInt(line[9]);
                int numberOfReviews = convertInt(line[10]);
                String lastReview = line[11];
                double reviewsPerMonth = convertDouble(line[12]);
                int calculatedHostListingsCount = convertInt(line[13]);
                int availability365 = convertInt(line[14]);

                AirbnbListing listing = new AirbnbListing(id, name, host_id,
                        host_name, neighbourhood, latitude, longitude, room_type,
                        price, minimumNights, numberOfReviews, lastReview,
                        reviewsPerMonth, calculatedHostListingsCount, availability365
                    );

                //map the listing:
                if(containsKey(neighbourhood)) { 
                    get(neighbourhood).add(listing);
                } else {
                    put(neighbourhood, new ArrayList<AirbnbListing>(Arrays.asList(listing)));
                }
            }
            reader.close();
        } catch(IOException | URISyntaxException e){
            System.out.println("Failure! Something went wrong");
            e.printStackTrace();
        }
    }

    /**
     *
     * @param doubleString the string to be converted to Double type
     * @return the Double value of the string, or -1.0 if the string is 
     * either empty or just whitespace
     */
    private Double convertDouble(String doubleString){
        if(doubleString != null && !doubleString.trim().equals("")){
            return Double.parseDouble(doubleString);
        }
        return -1.0;
    }

    /**
     *
     * @param intString the string to be converted to Integer type
     * @return the Integer value of the string, or -1 if the string is 
     * either empty or just whitespace
     */
    private Integer convertInt(String intString){
        if(intString != null && !intString.trim().equals("")){
            return Integer.parseInt(intString);
        }
        return -1;
    }
}
