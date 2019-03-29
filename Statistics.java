import java.util.ArrayList;
import java.util.Set;
import java.util.Collection;
import java.lang.Math;

import java.net.URL;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * This class is used to compute the statistics needed for the statisticsPane
 * class. The calculations are in a separate class to follow the standard design
 * pattern of separating data manipulation and data presentation.
 * 
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian
 *         Ghazanfar
 * @version 2.0
 */

public class Statistics {
    private ArrayList<AirbnbListing> dataArray;
    private AirbnbDataMap dataMap;
    private TFLStationsData tflStations;

    // close means within 200m range from station
    public static final double closeDistance = 200;

    private int propertiesCloseToStation;
    // if propertiesCloseToStation has been calculated already
    private boolean propertiesCloseToStationCalculated;

    private String mostServedBorough;
    // if mostServedBorough has already been calculated
    private boolean mostServedBoroughCalculated;

    /**
     * Constructor for Statistics
     */
    public Statistics() {
        propertiesCloseToStationCalculated = false;
        mostServedBoroughCalculated = false;

        tflStations = new TFLStationsData();
        update();
    }

    /**
     * Update (load) the data imported from the csv files
     */
    public void update() {
        dataMap = new AirbnbDataMap();
        dataArray = new ArrayList<AirbnbListing>();

        for (ArrayList arr : dataMap.values()) {
            dataArray.addAll(arr);
        }

    }

    /**
     * Get the average reviews from all of the properties
     * 
     * @return double The average review from all of the properties
     */
    public double getAverageReviews() {
        int totalReviews = dataArray.stream().mapToInt(AirbnbListing::getNumberOfReviews).sum();
        return totalReviews / dataArray.size();

        // small note on this:
        // Tried using the .average() method for streams but it seems to return the
        // wrong result. This is also arguably more readable.
    }

    /**
     * Get the total number of properties
     * 
     * @return int The total number of properties
     */
    public int getTotalProperties() {
        return dataArray.size();
    }

    /**
     * Get the number of Home and Apartments (everything which is not a private
     * room)
     * 
     * @return double The number of Home and Apartments
     */
    public double getHomeAndApartments() {
        return dataArray.stream().filter(e -> !e.getRoom_type().equals("Private room")).count();
    }

    /**
     * Get the most expensive house (minimum nights * price)
     * 
     * @return int The most expensive house (ordered by minimum nights * price)
     */
    public int getMostExpensive() {
        return dataArray.stream().mapToInt(e -> {
            return e.getMinimumNights() * e.getPrice();
        }).max().orElse(0);
    }

    /**
     * Get the number of properties within a certain price range
     * 
     * @param int Minimum price in the range
     * @param int Maximum price in the range
     * 
     * @return double The number of properties between the min and max price range
     */
    public double getPropertiesWithinPriceRange(int min, int max) {
        return dataArray.stream().mapToInt(AirbnbListing::getPrice).filter(e -> e >= min && e <= max).count();
    }

    /**
     * Get the number of properties within a certain price range in a specific
     * borough
     * 
     * @param int Minimum price in the range
     * @param int Maximum price in the range
     * 
     * @return double The number of properties between the min and max price range
     *         in a specific borough
     */
    public double getPropertiesWithinPriceRange(int min, int max, String borough) {
        return getSpecificBorough(borough).stream().mapToInt(AirbnbListing::getPrice).filter(e -> e >= min && e <= max)
                .count();
    }

    /**
     * Get the borough which has the most houses for a specific price range
     * 
     * @param int Minimum price in the range
     * @param int Maximum price in the range
     * 
     * @return String The name of the suggested borough (borough which has the
     *         highest number of houses within a price range)
     */
    public String getSuggestedBorough(int min, int max) {
        Set<String> boroughs = dataMap.getDistinctBoroughsSet();
        double maxCount = 0;
        String maxBorough = "No Borough Found";

        for (String borough : boroughs) {
            double propertyCount = getPropertiesWithinPriceRange(min, max, borough);
            if (propertyCount > maxCount) {
                maxCount = propertyCount;
                maxBorough = borough;
            }
        }

        return maxBorough;
    }

    /**
     * Get number of properties close (within 200m) of a station, distance is
     * computed via the Haversine method from the geo-cordinates of the property and
     * the station
     * 
     * @return int Number of properties which are close (within 200m) to an official
     *         TFL station
     */
    public int getPropertiesCloseToStation() {
        if (!propertiesCloseToStationCalculated) {
            int count = 0;

            for (AirbnbListing listing : dataArray) {
                for (TFLStation station : tflStations.getStations()) {
                    double distance = getDistanceBetweenGeoCoordinates(listing.getLatitude(), station.getLatitude(),
                            listing.getLongitude(), station.getLongitude());
                    if (distance <= closeDistance) {
                        count += 1;
                        // there might some other station within reach but we do not
                        // want to count the property more than once so we exit the
                        // loop
                        break;

                    }
                }
            }
            propertiesCloseToStation = count;
            propertiesCloseToStationCalculated = true;
        }

        return propertiesCloseToStation;
    }

    /**
     * Get the borough which has the highest number of properties which are close
     * (within 200m) of an official TFL station, distance is computed via the
     * Haversine method from the geo-cordinates of the property and the station
     * 
     * @return String The name of the borough which has the highest number of
     *         properties which are close (within 200m) of a TFL station
     */

    public String getMostServedBorough() {
        if (!mostServedBoroughCalculated) {
            Set<String> boroughs = dataMap.getDistinctBoroughsSet();
            int maxCount = 0;
            String maxBorough = "";

            for (String borough : boroughs) {
                int count = 0;
                for (AirbnbListing listing : getSpecificBorough(borough)) {
                    for (TFLStation station : tflStations.getStations()) {
                        double distance = getDistanceBetweenGeoCoordinates(listing.getLatitude(), station.getLatitude(),
                                listing.getLongitude(), station.getLongitude());
                        if (distance <= closeDistance) {
                            count += 1;
                            // there might some other station within reach but we do not
                            // want to count the property more than once so we exit the
                            // loop
                            break;
                        }
                    }

                    if (count > maxCount) {
                        maxCount = count;
                        maxBorough = borough;
                    }
                }
            }
            mostServedBorough = maxBorough;
            mostServedBoroughCalculated = true;
        }

        return mostServedBorough;
    }

    /**
     * https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
     * this method is a modification from this stackoverflow thread
     * 
     * Calculate distance between two points in latitude and longitude. Uses
     * Haversine method as its base.
     * 
     * @param double Latitude of first property
     * @param double Latitude of second property
     * @param double Longitude of first property
     * @param double Longitude of second property
     * 
     * @returns Distance in meters between the two geo-coordinates given
     */
    private static double getDistanceBetweenGeoCoordinates(double lat1, double lat2, double lon1, double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        return distance;
    }

    /**
     * Get properties in the currently selected borough
     * 
     * @return ArrayList<AirbnListing> List of AirbnbListing objects of the current
     *         selected borough
     */
    private ArrayList<AirbnbListing> getCurrentBorough() {
        return dataMap.get(dataMap.getCurrentBorough());
    }

    /**
     * Get properties in a specific borough
     * 
     * @return ArrayList<AirbnListing> List of AirbnbListing objects of a specific
     *         borough
     */
    private ArrayList<AirbnbListing> getSpecificBorough(String borough) {
        return dataMap.get(borough);
    }

    /**
     * This class is responsible of pulling data from the stations CSV file
     */
    public class TFLStationsData {
        private ArrayList<TFLStation> stations;

        /**
         * Constructor for TFLStationsData
         */
        public TFLStationsData() {
            stations = new ArrayList<TFLStation>();
            load();
        }

        /**
         * Load data from the stations CSV file and update the stations field
         */
        public void load() {
            System.out.print("Begin loading TFL Stations london dataset...");
            try {
                URL url = getClass().getResource("stations-london.csv");
                CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
                String[] line;
                // skip the first row (column headers)
                reader.readNext();
                while ((line = reader.readNext()) != null) {
                    String station = line[0];
                    String OSX = line[1];
                    String OSY = line[2];
                    double latitude = convertDouble(line[3]);
                    double longitude = convertDouble(line[4]);
                    String zone = line[5];
                    String postcode = line[6];

                    TFLStation tflstation = new TFLStation(station, OSX, OSY, latitude, longitude, zone, postcode);

                    stations.add(tflstation);
                }

            } catch (IOException | URISyntaxException e) {
                System.out.println("Failure! Something went wrong");
                e.printStackTrace();
            }
            System.out.println("Success! Number of loaded records: " + stations.size());
        }

        /**
         * Return the stations list
         * 
         * @return ArrayList<TFLStation> The list of stations
         */
        public ArrayList<TFLStation> getStations() {
            return stations;
        }

        /**
         * Convert string to double
         * 
         * @param String string to be converted to double
         * 
         * @return Double the double converted from the string, -1.0 if the string could
         *         not be converted
         */
        private Double convertDouble(String doubleString) {
            if (doubleString != null && !doubleString.trim().equals("")) {
                return Double.parseDouble(doubleString);
            }
            return -1.0;
        }
    }

    /**
     * This class is a helper class for TFLStationsData, it structures the object
     * which is then inserted in the stations list
     */
    public class TFLStation {
        private String station;
        private String OSX;
        private String OSY;
        private double latitude;
        private double longitude;
        private String zone;
        private String postcode;

        /**
         * Constructor for TFLStation
         * 
         * @param String The station
         * @param String OSX
         * @param String OSY
         * @param double The latitude
         * @param double The longitude
         * @param String The London zone
         * @param String The postcode
         */
        public TFLStation(String station, String OSX, String OSY, double latitude, double longitude, String zone,
                String postcode) {
            this.station = station;
            this.OSX = OSX;
            this.OSY = OSY;
            this.latitude = latitude;
            this.longitude = longitude;
            this.zone = zone;
            this.postcode = postcode;
        }

        /**
         * The latitude of the station
         * 
         * @return double The latitude of the station
         */
        public double getLatitude() {
            return latitude;
        }

        /**
         * The longitude of the station
         * 
         * @return double The longitude of the station
         */
        public double getLongitude() {
            return longitude;
        }
    }
}
