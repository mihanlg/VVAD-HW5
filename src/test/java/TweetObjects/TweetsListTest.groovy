package TweetObjects

import org.junit.Test;
import static org.junit.Assert.assertEquals

class TweetsListTest extends GroovyTestCase {

    Date date = new Date();
    String query = "TestQuery";
    String message = "SomeMessage";
    String message1 = "SomeMessage1";
    String message2 = "SomeMessage2";
    String message3 = "SomeMessage3";
    int favorited = 100;
    int retweeted = 200;
    String lang = "ru"

    @Test
    void  testSetQuery() {
        TweetsList list = new TweetsList();
        list.setQuery(query);
        assertEquals(query, list.getQuery());
    }

    @Test
    void testGetQuery() {
        TweetsList list = new TweetsList(query);
        assertEquals(query, list.getQuery());
    }

    @Test
    void testIterator() {
        Tweet tweet1 = new Tweet(message1, date + 10, favorited, retweeted, lang);
        Tweet tweet2 = new Tweet(message2, date + 1, favorited, retweeted, lang);
        Tweet tweet3 = new Tweet(message3, date + 35, favorited, retweeted, lang);

        ArrayList<Tweet> tweets = new ArrayList<>();
        tweets.add(tweet1);
        tweets.add(tweet2);
        tweets.add(tweet3);

        TweetsList list = new TweetsList();
        list.addAll(tweets);
        Iterator<Tweet> iterator = tweets.iterator();
        for (Tweet tweet : list) {
            assertEquals(tweet, iterator.next());
        }
        assertEquals(false, iterator.hasNext());
    }

    @Test
    void testSort() {
        Tweet tweet1 = new Tweet(message1, date + 10, favorited + 1, retweeted + 35, lang);
        Tweet tweet2 = new Tweet(message2, date + 1, favorited + 35, retweeted + 10, lang);
        Tweet tweet3 = new Tweet(message3, date + 35, favorited + 10, retweeted + 1, lang);

        ArrayList<Tweet> tweets = new ArrayList<>();
        tweets.add(tweet1);
        tweets.add(tweet2);
        tweets.add(tweet3);

        TweetsList list = new TweetsList();
        list.addAll(tweets);
        for (Comparator<Tweet> comparator: [new Tweet.FavoriteCountComparator(),
                                            new Tweet.RetweetCountComparator(),
                                            new Tweet.TimestampComparator()]) {
            tweets.sort(comparator);
            list.sort(comparator);

            Iterator<Tweet> iterator = tweets.iterator();
            for (Tweet tweet : list) {
                assertEquals(tweet, iterator.next());
            }
            assertEquals(false, iterator.hasNext());
        }
    }

    @Test
    void testAdd() {
        Tweet tweet1 = new Tweet(message, date, favorited, retweeted, lang);
        TweetsList list = new TweetsList();
        list.add(tweet1);
        assertEquals(true, list.contains(tweet1));
    }

    @Test
    void testAddAll() {
        Tweet tweet1 = new Tweet(message1, date + 10, favorited + 1, retweeted + 35, lang);
        Tweet tweet2 = new Tweet(message2, date + 1, favorited + 35, retweeted + 10, lang);
        Tweet tweet3 = new Tweet(message3, date + 35, favorited + 10, retweeted + 1, lang);

        ArrayList<Tweet> tweets = new ArrayList<>();
        tweets.add(tweet1);
        tweets.add(tweet2);
        tweets.add(tweet3);

        TweetsList list = new TweetsList();
        list.addAll(tweets);
        for(Tweet tweet: tweets) {
            assertEquals(true, list.contains(tweet));
        }
    }

    @Test
    void testClear() {
        Tweet tweet1 = new Tweet(message1, date + 10, favorited, retweeted, lang);
        Tweet tweet2 = new Tweet(message2, date + 1, favorited, retweeted, lang);
        Tweet tweet3 = new Tweet(message3, date + 35, favorited, retweeted, lang);

        TweetsList list = new TweetsList();
        list.add(tweet1);
        list.add(tweet2);
        list.add(tweet3);

        assertEquals(true, list.contains(tweet1));
        assertEquals(true, list.contains(tweet2));
        assertEquals(true, list.contains(tweet3));

        list.clear();

        assertEquals(false, list.contains(tweet1));
        assertEquals(false, list.contains(tweet2))
        assertEquals(false, list.contains(tweet3));
    }

    @Test
    void testGetTopRated() {
        Tweet tweet1 = new Tweet(message1, date, favorited + 1, retweeted, lang);
        Tweet tweet2 = new Tweet(message2, date, favorited + 20, retweeted, lang);
        Tweet tweet3 = new Tweet(message3, date, favorited + 13, retweeted, lang);

        TweetsList list = new TweetsList();
        list.add(tweet1);
        list.add(tweet2);
        list.add(tweet3);
        assertEquals(tweet2, list.getTopRated());
    }

    @Test
    void testGetOldest() {
        Tweet tweet1 = new Tweet(message1, date + 10, favorited, retweeted, lang);
        Tweet tweet2 = new Tweet(message2, date + 1, favorited, retweeted, lang);
        Tweet tweet3 = new Tweet(message3, date + 35, favorited, retweeted, lang);

        TweetsList list = new TweetsList();
        list.add(tweet1);
        list.add(tweet2);
        list.add(tweet3);
        assertEquals(tweet2, list.getOldest());
    }

    @Test
    void testRemove() {
        Tweet tweet1 = new Tweet(message1, date, favorited, retweeted, lang);
        Tweet tweet2 = new Tweet(message2, date, favorited, retweeted, lang);
        Tweet tweet3 = new Tweet(message3, date, favorited, retweeted, lang);

        TweetsList list = new TweetsList();
        list.add(tweet1);
        list.add(tweet2);
        list.add(tweet3);

        assertEquals(true, list.contains(tweet1));
        assertEquals(true, list.contains(tweet2));
        assertEquals(true, list.contains(tweet3));

        list.remove(tweet2);

        assertEquals(true, list.contains(tweet1));
        assertEquals(false, list.contains(tweet2))
        assertEquals(true, list.contains(tweet3));
    }

    @Test
    void testGroupByLang() {
        Tweet tweet1 = new Tweet(message1, date, favorited, retweeted, "ru");
        Tweet tweet2 = new Tweet(message2, date, favorited, retweeted, "en");
        Tweet tweet3 = new Tweet(message3, date, favorited, retweeted, "ru");

        TweetsList list = new TweetsList();
        list.add(tweet1);
        list.add(tweet2);
        list.add(tweet3);

        Map<String, Collection<Tweet>> map = list.groupByLang();
        for (String key: map.keySet()) {
            if (key == "ru") {
                assertEquals(true, map.get(key).contains(tweet1));
                assertEquals(false, map.get(key).contains(tweet2));
                assertEquals(true, map.get(key).contains(tweet3));
            } else if (key == "en") {
                assertEquals(false, map.get(key).contains(tweet1));
                assertEquals(true, map.get(key).contains(tweet2));
                assertEquals(false, map.get(key).contains(tweet3));
            }

        }
    }

    @Test
    void testGetTagCloud() {
        Tweet tweet1 = new Tweet("Hello HELLO a ab abc - My name is Mike :)", date, favorited, retweeted, "en");
        Tweet tweet2 = new Tweet("Hello! Test Mike's program...", date, favorited, retweeted, "en");
        Tweet tweet3 = new Tweet("Привет! Hello! Salut! Как дела?", date, favorited, retweeted, "ru");

        //hello hello hello - 3 times
        //mike mike - 2 times
        //name - 1 time
        //test - 1 time
        //program - 1 time
        //total: 8 times

        TweetsList list = new TweetsList();
        list.add(tweet1);
        list.add(tweet2);
        list.add(tweet3);

        Map<String, Double> real = list.getTagCloud("en");
        Double eps = 1e-10;
        Double diff = (1.0 - real.values().sum());
        assertEquals(true, diff < eps && diff > -eps);
        Set<String> keys = real.keySet();
        for (String key: keys) {
            assertEquals(true, key.length() > 3);
        }
        assertEquals(true, "hello" in keys);
        assertEquals(true, "mike" in keys);
        assertEquals(true, "name" in keys);
        assertEquals(true, "test" in keys);
        assertEquals(true, "program" in keys);
        assertEquals(false, "Привет" in keys);
        assertEquals(false, "Salut" in keys);
        assertEquals(true, real.get("hello") > real.get("mike"));
        assertEquals(true, real.get("program") > 0.0);
    }
}
