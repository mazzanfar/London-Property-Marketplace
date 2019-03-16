//TODO: sort out imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import com.opencsv.CSVReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Set;
import java.util.List;

/**
 * This class parses all of the data from the airbnb csv file
 * and stores it in a HashMap. The class iself extends HashMap
 * for convenience. Neighbourhoods are used as keys and the values are
 * ArrayLists of AirbnbListings, storing detailed information about each
 * property. 
 * The class also keeps track of the min and max price in the database
 * as well as what the current 'selected' (set by the GUI) borough is.
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class AirbnbDataMap extends HashMap<String, ArrayList<AirbnbListing>>
{
    private int minPrice, maxPrice;
    private String currentSelectedBorough;
    
    /**
     * Construct the map by loading the data in it
     */
    public AirbnbDataMap()
    {
        maxPrice = 0;
        minPrice = 100000; //TODO: implement this better :D
        load();
    }
    
    /**
     * 
     * @return true if a borough has been selected (by the GUI)
     */
    public boolean isBoroughSelected()
    {
        return (currentSelectedBorough == null);
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

                if(price > maxPrice) { // keep track of maxPrice
                    maxPrice = price;
                } else if(price < minPrice) { // keep track of minPrice
                    minPrice = price;
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
}
