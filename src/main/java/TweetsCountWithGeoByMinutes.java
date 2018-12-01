
import twitter4j.conf.ConfigurationBuilder;

public class TweetsCountWithGeoByMinutes {

    /**
     * Main function which use a ConfigurationBuilder class to configure Twitter4J
     * and passes the configuration to new TwitterListenerWriter class
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        // The factory instance is re-useable and thread safe.

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("RxxixnoyNw2MGNyZaaeCfn3H6")
                .setOAuthConsumerSecret("Tzc7ad9Wp9hYUsaR6wDMqf2tyw1Ypcccz2y0A8cYBuEDEjo7hf")
                .setOAuthAccessToken("2580410239-Wy0EJcGV1jMux8OMDUJEWBlNpcCPp9xUM6rdnk3")
                .setOAuthAccessTokenSecret("sRZrptwccX0VGXF0M8rvijniKOXZyX0HU54nBAjuauuQR");

        TwitterListenerWriter tl = new TwitterListenerWriter();
        tl.listen(cb);

    }
}
