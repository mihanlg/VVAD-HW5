package TweetObjects;

import java.io.Serializable;
import java.util.*;

public class Tweet implements Serializable{

    private String content;
    private Date timestamp;
    private int favoriteCount;
    private int retweetCount;
    private String lang;

    public Tweet(String _content, Date _timestamp, int _favoriteCount, int _retweetCount, String _lang) {
        content = _content;
        timestamp = _timestamp;
        favoriteCount = _favoriteCount;
        retweetCount = _retweetCount;
        lang = _lang;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof Tweet)) return false;
        Tweet tweet = (Tweet)o;
        return timestamp.equals(tweet.getTimestamp()) && content.equals(tweet.getContent());
    }

    @Override
    public int hashCode() {
        return (timestamp.toString() + content).hashCode();
    }

    @Override
    public String toString() {
        return "[content=" + content + ", timestamp=" + timestamp.toString() + ", favoriteCount="
                + favoriteCount + ", retweetCount=" + retweetCount + ", lang=" + lang + "]";
    }


    public int getFavoriteCount() {
        return favoriteCount;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public String getContent() {
        return content;
    }

    public String getLang() {
        return lang;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public static final class TimestampComparator implements Comparator<Tweet> {
        @Override
        public int compare(Tweet tweet1, Tweet tweet2) {
            return tweet1.getTimestamp().compareTo(tweet2.getTimestamp());
        }
    }

    public static final class FavoriteCountComparator implements Comparator<Tweet> {
        @Override
        public int compare(Tweet tweet1, Tweet tweet2) {
            return tweet1.getFavoriteCount() - tweet2.getFavoriteCount();
        }
    }

    public static final class RetweetCountComparator implements Comparator<Tweet> {
        @Override
        public int compare(Tweet tweet1, Tweet tweet2) {
            return tweet1.getRetweetCount() - tweet2.getRetweetCount();
        }
    }


}

