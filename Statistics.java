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
 * Write a description of class Statistics here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Statistics {
    private ArrayList<AirbnbListing> dataArray;
    private AirbnbDataMap dataMap;
    private TFLStationsData tflStations;  

    // close means within 200m range from station 
    public static final double closeDistance = 200;

    public Statistics() {
        tflStations = new TFLStationsData();
        update();
    }

    public void update() {
        dataMap = new AirbnbDataMap();
        dataArray = new ArrayList<AirbnbListing>();

        for (ArrayList arr : dataMap.values()) {
            dataArray.addAll(arr);
        }

    }

    public double getAverageReviews() {
        return dataArray.stream().mapToInt(e -> e.getNumberOfReviews()).average().orElse(0);
    }

    public int getTotalProperties() {
        return dataArray.size();
    }

    public double getHomeAndApartments() {
        return dataArray.stream().filter(e -> e.getRoom_type() != "Private room").count();
    }

    public int getMostExpensive() {
        return dataArray.stream().mapToInt(e -> {
            return e.getMinimumNights() * e.getPrice();
        }).max().orElse(0);
    }

    public double getPropertiesWithinPriceRange(int min, int max) {
        return dataArray.stream().mapToInt(e -> {
            return e.getMinimumNights() * e.getPrice();
        }).filter(e -> e >= min && e <= max).count();
    }

    public double getPropertiesWithinPriceRange(int min, int max, String borough) {
        return getSpecificBorough(borough).stream().mapToInt(e -> {
            return e.getMinimumNights() * e.getPrice();
        }).filter(e -> e >= min && e <= max).count();
    }

    public String getSuggestedBorough(int min, int max) {
        Set<String> boroughs = dataMap.getDistinctBoroughsSet();
        double maxCount = 0;
        String maxBorough = "";

        for (String borough : boroughs) {
            double propertyCount = getPropertiesWithinPriceRange(min, max, borough);
            if (propertyCount > maxCount) {
                maxCount = propertyCount;
                maxBorough = borough;
            }
        }

        return maxBorough;
    }

    public int getPropertiesCloseToStation() { 
        int count = 0;

        for (AirbnbListing listing : dataArray) { 
            for (TFLStation station : tflStations.getStations()) {
                double distance = getDistanceBetweenGeoCoordinates(listing.getLatitude(), station.getLatitude(), listing.getLongitude(), station.getLongitude()); 
                if (distance <= closeDistance)
                    count += 1;
            }
        }
        
        return count;
    }

    public String getMostServedBorough() { 
        Set<String> boroughs = dataMap.getDistinctBoroughsSet();
        int maxCount = 0;
        String maxBorough = "";

        for (String borough : boroughs) { 
            int count = 0;
            for (AirbnbListing listing : getSpecificBorough(borough)) {
                for (TFLStation station : tflStations.getStations()) {
                    double distance = getDistanceBetweenGeoCoordinates(listing.getLatitude(), station.getLatitude(), listing.getLongitude(), station.getLongitude());
                    if (distance <= closeDistance)
                        count += 1;
                } 

                if (count > maxCount) { 
                    maxCount = count;
                    maxBorough = borough;
                }
            } 
        }

        return maxBorough;
    }
    
    /**
     * https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude this method is a modification from this stackoverflow thread
     * 
     * Calculate distance between two points in latitude and longitude. Uses Haversine method as its base.
     * 
     * @returns Distance in Meters
     */
    private static double getDistanceBetweenGeoCoordinates(double lat1, double lat2, double lon1, double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        return distance;
    }


    private ArrayList<AirbnbListing> getCurrentBorough() {
        return dataMap.get(dataMap.getCurrentBorough());
    }

    private ArrayList<AirbnbListing> getSpecificBorough(String borough) {
        return dataMap.get(borough);
    }   
    
    public class TFLStationsData { 
        private ArrayList<TFLStation> stations; 
        public TFLStationsData() {
            stations = new ArrayList<TFLStation>();
            load();
        }
        public void load() {
            System.out.print("Begin loading TFL Stations london dataset...");
            try {
                URL url = getClass().getResource("stations-london.csv");
                CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
                String [] line;
                //skip the first row (column headers)
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
    
            } catch(IOException | URISyntaxException e){
                System.out.println("Failure! Something went wrong");
                e.printStackTrace();
            }
            System.out.println("Success! Number of loaded records: " + stations.size());
        }

        public ArrayList<TFLStation> getStations() {
            return stations; 
        }
        
        private Double convertDouble(String doubleString){
            if(doubleString != null && !doubleString.trim().equals("")){
                return Double.parseDouble(doubleString);
            }
            return -1.0;
        }
    }

    public class TFLStation {
        private String station;
        private String OSX;
        private String OSY;
        private double latitude;
        private double longitude;
        private String zone;
        private String postcode;

        public TFLStation(String station, String OSX, String OSY, double latitude, double longitude, String zone, String postcode) {
            this.station = station;
            this.OSX = OSX;
            this.OSY = OSY;
            this.latitude = latitude;
            this.longitude = longitude;
            this.zone = zone;
            this.postcode = postcode;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() { 
            return longitude;
        }
    }
}

