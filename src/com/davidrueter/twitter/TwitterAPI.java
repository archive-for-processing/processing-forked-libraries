package com.davidrueter.twitter;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import processing.core.PApplet;
import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.UserStreamAdapter;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Simplified interface to twitter4j for programming beginners.
 * Intended to be useful for beginners who do not yet know what
 * objects, classes, and exceptions are.
 * Also useful for anyone who just wants a simplified interface.
 * 
 */
public class TwitterAPI {
	
	// myParent is a reference to the parent sketch
	PApplet myParent;

	private Method statusMethod;
	private Configuration twitterConfig;
	
	public final static String VERSION = "##library.prettyVersion##";
	private Twitter twitter;
	

	/** Constructs a new Twitter API instance.
	 * 
	 * To receive status updates, your Processing app must implement this method:
	 * 
	 * void onStatus(Status status){
	 * 
	 * }
	 * 
	 * @param theParent parent applet.
	 * @param consumerKey Twitter API consumer key
	 * @param consumerSecret Twitter API consumer secret
	 * @param accessToken Twitter API access token
	 * @param accessTokenSecret Twitter API access token secret.
	 */
	public TwitterAPI(PApplet theParent,
			String consumerKey,
			String consumerSecret,
			String accessToken,
			String accessTokenSecret) {
		myParent = theParent;
		ConfigurationBuilder cb = new ConfigurationBuilder();
		  cb.setOAuthConsumerKey(consumerKey);
		  cb.setOAuthConsumerSecret(consumerSecret);
		  cb.setOAuthAccessToken(accessToken);
		  cb.setOAuthAccessTokenSecret(accessTokenSecret);
		twitterConfig = cb.build();
		twitter = new TwitterFactory(twitterConfig).getInstance();
		  
	}
	
	/** Filter all tweets, receiving current statuses that contain the supplied
	 * search term. Multiple strings may be supplied. Do not include hashtags --
	 * if you want to filter hashtags only, you'll have to exclude the # symbol
	 * from the filters argument, then manually check each received status.
	 * @param filters
	 */
	public void filter(String... filters){
		TwitterStream twitterStream = setupFilterStream();
		twitterStream.filter(filters);
	}
	
	/** Start receiving status updates from users whose IDs are in
	 * idsToFollow.
	 * @param idsToFollow the list of IDs to follow
	 */
	public void filterUsers(long... idsToFollow){
		FilterQuery q = new FilterQuery(idsToFollow);
		filter(q);
	}
	
	/** Filter an arbitrary FilterQuery. See Twitter4j docs for more details.
	 * 
	 * @param query
	 */
	public void filter(FilterQuery query){
		TwitterStream twitterStream = setupFilterStream();
		twitterStream.filter(query);
	}
	
	/** Filter based on geographical location. Specify a bounding box, by
	 * supplying the SW corner first (latitude, then longitude), then the NE corner.
	 * 
	 * @param swLat
	 * @param swLon
	 * @param neLat
	 * @param neLon
	 */
	public void filterGeo(double swLat, double swLon, double neLat, double neLon){
		FilterQuery q = new FilterQuery();
		q.locations(new double[][]{{swLon, swLat},{neLon,neLat}});
		filter(q);
	}
	
	/** Receive all status updates made by a user.
	 * FIXME FIXME FIXME
	 */
	public void user(){
		initOnStatusCallback();
		UserStreamAdapter adapter = new UserStreamAdapter(){

			@Override
			public void onStatus(Status status) {
				dispatchStatus(status);
			}
			
		};
		TwitterStream twitterStream = makeStream();
		twitterStream.addListener(adapter);
		twitterStream.user();
		
	}
	
	
	
	protected TwitterStream setupFilterStream() {
		initOnStatusCallback();

		// Create a new status listener
	  StatusListener listener = new StatusAdapter(){
	    public void onStatus(Status status) {
	       dispatchStatus(status); 
	    }
	    public void onException(Exception ex) {
	        ex.printStackTrace();
	    }
	  };
	  TwitterStream twitterStream = makeStream();
	  twitterStream.addListener(listener);
		return twitterStream;
	}

	protected TwitterStream makeStream() {
		TwitterStream twitterStream = new TwitterStreamFactory(twitterConfig).getInstance();
		return twitterStream;
	}

	protected void dispatchStatus(Status status){
		try {
			statusMethod.invoke(myParent, status);
		} catch (IllegalAccessException e) {
			logThrowable(e);
		} catch (IllegalArgumentException e) {
			logThrowable(e);
		} catch (InvocationTargetException e) {
			logThrowable(e);
		}
	}
	
	protected void logThrowable(Throwable t){
		t.printStackTrace();
	}

	protected void initOnStatusCallback() {
		try {
			statusMethod = myParent.getClass().getMethod("onStatus", Status.class);
		} catch (NoSuchMethodException e){
			throw new RuntimeException("No onStatus() method found in the sketch.");
		}
	}
	
	public boolean tweet(String status){
		try {
			twitter.updateStatus(status);
			return true;
		} catch (TwitterException e){
			logThrowable(e);
			return false;
		}
	}
	
	public long[] getFollowers(){
		try {
			return twitter.getFollowersIDs(twitter.getId()).getIDs();
		} catch (Exception e){
			logThrowable(e);
			return new long[0];
		}
	}

	/**
	 * return the version of the Library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}
}

