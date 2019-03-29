
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class StatisticsTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class StatisticsTest
{
    private Statistics statistics;

    public static final double FLOAT_DELTA = 0.01;
    
    public static final int TOTAL_PROPERTIES = 53904;
    /**
     * Default constructor for test class StatisticsTest
     */
    public StatisticsTest()
    {
        statistics = new Statistics();
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }

    @Test
    public void getAverageReviews() {
        assertEquals(12, statistics.getAverageReviews(), FLOAT_DELTA);
    }

    @Test
    public void getTotalProperties() { 
        assertEquals(TOTAL_PROPERTIES,statistics.getTotalProperties());
    }

    @Test
    public void getHomeAndApartments() {
        assertEquals(27885, statistics.getHomeAndApartments(), FLOAT_DELTA);
    }
    
    @Test
    public void getMostExpensive() {
        assertEquals(2000000, statistics.getMostExpensive());
    }
    
    @Test
    public void getPropertiesWithinPriceRange() {
        assertEquals(8, statistics.getPropertiesWithinPriceRange(3500, 6507), FLOAT_DELTA);
        assertEquals(174, statistics.getPropertiesWithinPriceRange(626, 3181), FLOAT_DELTA);
        assertEquals(TOTAL_PROPERTIES, statistics.getPropertiesWithinPriceRange(0, 7000), FLOAT_DELTA);
        assertEquals(53893, statistics.getPropertiesWithinPriceRange(8, 2542), FLOAT_DELTA);
        assertEquals(3, statistics.getPropertiesWithinPriceRange(6392, 7000), FLOAT_DELTA);
        assertEquals(95, statistics.getPropertiesWithinPriceRange(51, 51), FLOAT_DELTA);
        assertEquals(0, statistics.getPropertiesWithinPriceRange(4624, 4961), FLOAT_DELTA);
    }
    
    @Test
    public void getSuggestedBorough() {
        assertEquals("Westminster", statistics.getSuggestedBorough(3500, 6507));
        assertEquals("Kensington and Chelsea", statistics.getSuggestedBorough(626, 3181));
        assertEquals("Tower Hamlets", statistics.getSuggestedBorough(0, 7000));
        assertEquals("Tower Hamlets", statistics.getSuggestedBorough(8, 2542));
        assertEquals("Westminster", statistics.getSuggestedBorough(6392, 7000));
        assertEquals("Westminster", statistics.getSuggestedBorough(51, 51));
        assertEquals("No Borough Found", statistics.getSuggestedBorough(4624, 4961));
    }
    
    @Test
    public void getPropertiesCloseToStation() {
        assertEquals(8066, statistics.getPropertiesCloseToStation());
    }
    
    @Test
    public void getMostServedBorough() {
        assertEquals("Westminster", statistics.getMostServedBorough());
    }
}
