import java.util.ArrayList;
import java.util.Set;
import java.util.Collection;

/**
 * Write a description of class Statistics here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Statistics
{   
    private ArrayList<AirbnbListing> dataArray;
    private AirbnbDataMap dataMap;  


    public void update(AirbnbDataMap data) 
    {   
        dataMap = data; 
        dataArray =  new ArrayList<AirbnbListing>();

        for(ArrayList arr : dataMap.values())
            dataArray.addAll(arr);
   
    }

    public double getAverageReviews() 
    {
        return dataArray.stream()
        .mapToInt(e -> e.getNumberOfReviews())
        .average()
        .orElse(0);
    }

    public int getTotalProperties() 
    {
        return dataArray.size();
    }

    public long getHomeAndApartments() 
    {
        return dataArray.stream()
        .filter(e -> e.getRoom_type() != "Private room")
        .count();
    }

    public int getMostExpensive() 
    { 
        return dataArray.stream()
        .mapToInt(e -> {
            return e.getMinimumNights() * e.getPrice();
        })
        .max()
        .orElse(0);
    }

    public long getPropertiesWithinPriceRange(int min, int max) 
    {  
        return dataArray.stream()
        .mapToInt(e -> {
            return e.getMinimumNights() * e.getPrice();
        })
        .filter(e -> e >= min && e <= max)
        .count();
    }

    public long getPropertiesWithinPriceRange(int min, int max, String borough) 
    {  
        return getSpecificBorough(borough).stream()
        .mapToInt(e -> {
            return e.getMinimumNights() * e.getPrice();
        })
        .filter(e -> e >= min && e <= max)
        .count();
    }

    public String getSuggestedBorough(int min, int max)
    {
        Set<String> boroughs = dataMap.getDistinctBoroughsSet(); 
        long maxCount = 0;
        String maxBorough = ""; 

        for (String borough : boroughs) { 
            long propertyCount = getPropertiesWithinPriceRange(min, max, borough);
            if (propertyCount > maxCount) { 
                maxCount = propertyCount;
                maxBorough = borough; 
            }
        }

        return maxBorough;
    }
    
    private ArrayList<AirbnbListing> getCurrentBorough() 
    { 
        return dataMap.get(dataMap.getCurrentBorough()); 
    } 

    private ArrayList<AirbnbListing> getSpecificBorough(String borough)
    {
        return dataMap.get(borough);
    }
}

    