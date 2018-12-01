import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 *The custom class which listen a Twitter Stream, aggregate data and write data to file for logstash
 */
public class TwitterListenerWriter {

    private HashMap<String, Integer> countriesTweetsPerMinute = new HashMap <>(); // key - country; value- count of tweets per minute
    private String currentDate = null;

    public void setCurrentDate( String dateValue) {
        currentDate = dateValue;
    }

    public void setCountriesTweetsPerMinute(HashMap<String, Integer> mp) {
        countriesTweetsPerMinute.putAll(mp);
    }

    public HashMap<String, Integer> getCountriesTweetsPerMinute() {
        return countriesTweetsPerMinute;
    }

    /**
     *The function which create listener for collect data
     * @param cb
     */
    public void listen(ConfigurationBuilder cb) {

        StatusListener listener = new StatusListener() {
            @Override
            public void onException(Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice arg) {
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
            }

            @Override
            public void onStallWarning(StallWarning warning) {
            }

            /**
             *The method by which we receive tweets and causes further aggregation and recording of data
             * @param status
             */
            @Override
            public void onStatus(Status status) {

                if (status.getPlace() != null) {
                    SimpleDateFormat sdf_minute = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    String country = status.getPlace().getCountry();
                    String date = sdf_minute.format(status.getCreatedAt());
                    System.out.println(date + " " + currentDate + " - " + country + "\n");
                    //System.out.println(status.getUser().getScreenName() + ":" + status.getText());

                    aggregateByCountry(country, date);

                }
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            }
        };

        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        twitterStream.addListener(listener);
        twitterStream.sample();
    }

    /**
     * A function that aggregates data for the past minute by country
     * @param country
     * @param date
     */
    public void aggregateByCountry(String country, String date) {
        if ((currentDate != null) && !currentDate.equals(date)) {
            writeDataForLogstash("./configurations/output.txt");
            countriesTweetsPerMinute.clear();
        }

        int value = 0;
        if (countriesTweetsPerMinute.containsKey(country)) {
            value = countriesTweetsPerMinute.get(country);
        }
        value = value + 1;
        countriesTweetsPerMinute.put(country, value);
        currentDate = date;

    }

    /**
     * The function that writes data to a file for reading by logstash
     */
    public void writeDataForLogstash(String path) {
        try {
            FileWriter writer = new FileWriter(path, true);

            for (Map.Entry entry : countriesTweetsPerMinute.entrySet()) {
                JSONObject obj = new JSONObject();
                obj.put("Time", currentDate);
                obj.put("Country", entry.getKey());
                obj.put("Count", entry.getValue());

                writer.write(obj.toString() + "\n");
                System.out.println("COUNT TWEET for country: " +  entry.getKey() + " - " + entry.getValue().toString()
                        + " ; " +currentDate + "\n");
            }

            writer.close();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
