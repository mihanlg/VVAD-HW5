package TwitterAPI;

import twitter4j.conf.ConfigurationBuilder;
import twitter4j.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import TweetObjects.*;

public class Accessor {

    private static Twitter twitter;

    Accessor() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("m30gd6M0BaeJ7nrgNSwkKFC1W")
                .setOAuthConsumerSecret("zIAAJD27aThwlwnnOExciHzztYKIavLP5J6yiSjpHnNHEh7HZM")
                .setOAuthAccessToken("160980902-ebrVWPsyEBaaARZAnGbOZiQ2KKqzLtSJwyhsKohF")
                .setOAuthAccessTokenSecret("0fAU0cwVeGNtkuuKdGcWteQsy6OO2fVCp40F6Btu5QV8F");
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    public static Collection<Tweet> search(String query, Date since, int querySize) {
        /**
         * @param query - строка поиска, передаваемая в Twitter4j
         * @param since - дата, с которой начинается поиск (достаточно указать день, месяц и год)
         * @param querySize - количество твитов в результирующей выборке
         */

        //max query size for 1 request to Twitter is 100
        int maxSize = 100;

        //format Date
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        //setting query
        Query tw_query = new Query(query);
        tw_query.setSince(df.format(since));
        tw_query.setResultType(Query.ResultType.recent);

        //return value
        Collection<Tweet> tweets = new ArrayList<>();

        //request
        long max_id = Long.MAX_VALUE;
        while(querySize > 0) {
            System.out.print("\rQuery size: " + querySize + " ");
            System.out.flush();

            //set new query size due to Twitter limits
            int size = querySize/maxSize > 0 ? maxSize : querySize;
            tw_query.setCount(size);

            //search query
            QueryResult result = null;
            try {
                result = twitter.search(tw_query);
            } catch (TwitterException e) {
                if (e.exceededRateLimitation()) {
                    try {
                        long seconds = e.getRateLimitStatus().getSecondsUntilReset() + 10;
                        while(seconds > 0) {
                            System.out.print("\rRate limit. Waiting " + seconds + " seconds...");
                            System.out.flush();
                            Thread.sleep(1000);
                            seconds--;
                        }
                        continue;
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                        break;
                    }
                }
                e.printStackTrace();
                break;
            }

            //load statuses
            Collection<Status> statuses = result.getTweets();
            if (statuses.size() == 0) break;

            //convert statuses to tweets
            for (Status status : statuses) {
                //prepare data
                Date timestamp = status.getCreatedAt();
                String content = status.getText();
                int favoriteCount = status.getFavoriteCount();
                int retweetCount = status.getRetweetCount();
                String lang = status.getLang();

                //construct tweet and add to tweets
                Tweet tweet = new Tweet(content, timestamp, favoriteCount, retweetCount, lang);
                tweets.add(tweet);

                //update max_id and until
                max_id = status.getId() - 1;
            }

            //subtract querySize
            querySize -= statuses.size();
            tw_query.setMaxId(max_id);
        }
        return tweets;
    }
}
