package TweetObjects;

import java.util.*;

import static jdk.nashorn.internal.objects.Global.undefined;

public class TweetsList implements TweetsContainer<Tweet> {

    private List<Tweet> _tweets;
    private String _query;

    public TweetsList(String query) {
        if (query == undefined) query = "";
        _tweets = new ArrayList<>();
        _query = query;
    }

    public void setQuery(String query) {
        _query = query;
    }

    public String getQuery() {
        return _query;
    }

    @Override
    public Tweet getTopRated() {
        if (_tweets.isEmpty()) return null;
        Tweet topRated = iterator().next();
        for (Tweet tweet: _tweets) {
            if (tweet.getFavoriteCount() > topRated.getFavoriteCount()) {
                topRated = tweet;
            }
        }
        return topRated;
    }

    @Override
    public Iterator<Tweet> iterator() {
        return _tweets.iterator();
    }

    @Override
    public void sort(Comparator<Tweet> comparator) {
        if (comparator == null) {
            System.err.println("sort warning: given comparator is null");
            return;
        }
        _tweets.sort(comparator);
    }

    @Override
    public boolean add(Tweet tweet) {
        if (tweet == null) return false;
        return _tweets.add(tweet);
    }

    @Override
    public boolean addAll(Collection<? extends Tweet> tweets) {
        if (tweets == null) return false;
        return _tweets.addAll(tweets);
    }

    @Override
    public void clear() {
        _tweets.clear();
    }

    @Override
    public Tweet getOldest() {
        if (_tweets.isEmpty()) {
            System.err.println("getOldest warning: empty tweets collection");
            return null;
        }
        Tweet oldestTweet = _tweets.iterator().next();
        for (Tweet tweet: _tweets) {
            if (tweet.getTimestamp().before(oldestTweet.getTimestamp())) oldestTweet = tweet;
        }
        return oldestTweet;
    }

    @Override
    public boolean remove(Tweet tweet) {
        if (_tweets.contains(tweet)) {
            return _tweets.remove(tweet);
        }
        else {
            System.err.println("remove error: tweet was not found");
            return false;
        }
    }

    @Override
    public Map<String, Collection<Tweet>> groupByLang() {
        HashMap<String, Collection<Tweet>> grouped = new HashMap<>();
        for (Tweet tweet: _tweets) {
            String lang = tweet.getLang();
            if (!grouped.containsKey(lang)) {
                grouped.put(lang, new ArrayList<Tweet>());
            }
            grouped.get(lang).add(tweet);
        }
        return grouped;
    }

    @Override
    public Map<String, Double> getTagCloud(String lang) {
        Map<String, Collection<Tweet>> grouped = groupByLang();
        if (!grouped.containsKey(lang)) {
            System.err.println("getTagCloud warning: language \""+lang+"\" isn't found");
            return null;
        }
        Collection<Tweet> tweets = grouped.get(lang);
        HashMap<String, Double> uniqueTags = new HashMap<>();
        long occurences = 0;
        for (Tweet tweet: tweets) {
            String[] splitted = tweet.getContent().toLowerCase().split("[\\s]+");
            for (String str: splitted) {
                if (str.startsWith("http")) continue;
                String[] tags = str.split("[\\p{Punct}\\s]+");
                for (String tag : tags) {
                    if (tag.length() <= 3) continue;
                    occurences++;
                    if (!uniqueTags.containsKey(tag)) uniqueTags.put(tag, 0.0);
                    uniqueTags.put(tag, uniqueTags.get(tag) + 1.0);
                }
            }
        }
        if (occurences != 0) {
            for (String key : uniqueTags.keySet()) {
                uniqueTags.put(key, uniqueTags.get(key)/occurences);
            }
        }
        return uniqueTags;
    }


}
