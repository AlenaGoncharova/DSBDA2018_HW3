import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * TwitterListenerWriterTest.java
 * Unit tests for TwitterListenerWriter class.
 */
public class TwitterListenerWriterTest {

    private HashMap<String, Integer> mp;

    /**
     * Creates test map of key - country, value - count tweets before each try.
     */
    @Before
    public void setUp() {
        mp = new HashMap <>();
        mp.put("United States", 10);
        mp.put("Brasil", 4);
        mp.put("Japan", 1);
    }

    /**
     * Aggregation test for different size of result countariesMaps
     */
    @Test
    public void changeNumberCountriesTest() {
        TwitterListenerWriter tlw1 = new TwitterListenerWriter();
        TwitterListenerWriter tlw2 = new TwitterListenerWriter();
        tlw1.setCurrentDate("2018-12-01T06:10");
        tlw1.setCountriesTweetsPerMinute(mp);
        tlw2.setCurrentDate("2018-12-01T06:10");
        tlw2.setCountriesTweetsPerMinute(mp);
        tlw1.aggregateByCountry("Brasil", "2018-12-01T06:10");
        tlw2.aggregateByCountry("Argentina", "2018-12-01T06:10");
        assertNotEquals(
                tlw1.getCountriesTweetsPerMinute().size(),
                tlw2.getCountriesTweetsPerMinute().size()
        );
    }

    /**
     * Aggregation test for same size of result countariesMaps
     */
    @Test
    public void nochangeNumberCountriesTest() {
        TwitterListenerWriter tlw1 = new TwitterListenerWriter();
        TwitterListenerWriter tlw2 = new TwitterListenerWriter();
        tlw1.setCurrentDate("2018-12-01T06:10");
        tlw1.setCountriesTweetsPerMinute(mp);
        tlw2.setCurrentDate("2018-12-01T06:10");
        tlw2.setCountriesTweetsPerMinute(mp);
        tlw1.aggregateByCountry("Brasil", "2018-12-01T06:10");
        tlw2.aggregateByCountry("United States", "2018-12-01T06:10");
        assertEquals(
                tlw1.getCountriesTweetsPerMinute().size(),
                tlw2.getCountriesTweetsPerMinute().size()
        );
    }

    /**
     * Aggregation test for reset maps of pair "country;count of tweets" for minute
     */
    @Test
    public void resetCountriesMapForMinute() {
        TwitterListenerWriter tlw1 = new TwitterListenerWriter();
        TwitterListenerWriter tlw2 = new TwitterListenerWriter();
        tlw1.setCurrentDate("2018-12-01T06:10");
        tlw1.setCountriesTweetsPerMinute(mp);
        tlw2.setCurrentDate("2018-12-01T06:10");
        tlw2.setCountriesTweetsPerMinute(mp);
        tlw1.aggregateByCountry("Brasil", "2018-12-01T06:15");
        tlw2.aggregateByCountry("Argentina", "2018-12-01T06:16");
        assertEquals(
                tlw1.getCountriesTweetsPerMinute().size(),
                tlw2.getCountriesTweetsPerMinute().size()
        );

    }

    /**
     * Test for write to file for logstash
     */
    @Test
    public void writeToFileTest() {
        File f = new File("test.txt");
        long lenBeforeWrite = f.length();
        TwitterListenerWriter tlw1 = new TwitterListenerWriter();
        tlw1.setCurrentDate("2018-12-01T06:10");
        tlw1.setCountriesTweetsPerMinute(mp);
        tlw1.writeDataForLogstash("test.txt");
        long lenAfterWrite = f.length();
        assertNotEquals(lenBeforeWrite, lenAfterWrite);
    }


}
