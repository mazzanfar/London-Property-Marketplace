//TODO: sort out imports
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
import java.util.List;

/**
 * TODO: comment here
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
    
    /**
     * Construct the map by loading the data in it
     */
    public AirbnbDataMap()
    {
        geographicallyOrderBoroughs();
        load();
        favorites = new ArrayList<AirbnbListing>();
        //hardCodeFavorites(); // TODO: not hardcode
    }
    
    private void hardCodeFavorites()
    {
        favorites = get("Havering");
    }
    
    public ArrayList<AirbnbListing> getFavoriteProperties()
    {
        return favorites;
    }
    
    /**
     * 
     * @return the minimum price found in data
     */
    public int getMinPrice()
    {
        return minPrice;
    }
    
    /**
     * 
     * @return the maximum price found in data
     */
    public int getMaxPrice()
    {
        return maxPrice;
    }
    
    /**
     * 
     * @param newBorough the name of the newly selected neighbourhood
     */
    public void setCurrentBorough(String newBorough)
    {
        currentSelectedBorough = newBorough;
    }
    
    /**
     * 
     * @return the name of the selected neighbourhood
     */
    public String getCurrentBorough()
    {
        return currentSelectedBorough;
    }
    
    /**
     * 
     * @return a set of all boroughs provided in the data
     */
    public Set<String> getDistinctBoroughsSet()
    {
        return keySet();
    }
    
    /**
     * @param name the name of the borough to fetch data from
     * @return an ArrayList of listings of properties
     */
    public ArrayList<AirbnbListing> getListingsInBorough(String name)
    {
        return get(name);
    }
    
    /**
     * @return the geographic order of boroughs
     */
    public ArrayList<String> getGeoOrderedBoroughs()
    {
        return geoOrderedBoroughs;
    }
    
    /**
     * @param key the borough to get properties from
     * @param min the minimum price of properties
     * @param max the maximum price of properties
     * @return an array list of listings with properties matching the description
     */
    public ArrayList<AirbnbListing> getWithinPriceRange(String key, int min, int max)
    {
        ArrayList<AirbnbListing> result = new ArrayList<AirbnbListing>();
        
        for(AirbnbListing listing : get(key))  {
            if(listing.getPrice() <= max && listing.getPrice() >= min) {
                result.add(listing);
            }
        }
        return result;
    }
    
    /**
     * @param min the minimum price
     * @param max the maximum price
     * @return the number of properties within a price range
     */
    public int getWithinPriceRange(int min, int max)
    {
        int count = 0;
        
        for(String key : keySet())  {
            for(AirbnbListing listing : get(key)) { 
                if(listing.getPrice() <= max && listing.getPrice() >= min) {
                    count ++;
                }
            }
        }
        return count;
    }
    
    public void setFavorite(String neighbourhood, String id)
    {
        for(AirbnbListing listing : get(neighbourhood)) {
            if(id.equals(listing.getId())) {
                listing.setFavorite();
                favorites.add(listing);
                return;
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
    
    public AirbnbListing getPropertyById(String id)
    {
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
     * Load the CSV file and iterate through every row, storing data in 
     * an AirbnbListing object and map boroughs to properties with a HashMap
     * (this class extends HashMap)
     */
    private void load() {
        System.out.print("Begin loading Airbnb london dataset...");
        try {
            URL url = getClass().getResource("airbnb-london.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] line;
            //skip the first row (column headers)
            reader.readNext();
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

        } catch(IOException | URISyntaxException e){
            System.out.println("Failure! Something went wrong");
            e.printStackTrace();
        }
        System.out.println("Success! Number of loaded records: " + values().stream().mapToInt(List::size).sum());
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
