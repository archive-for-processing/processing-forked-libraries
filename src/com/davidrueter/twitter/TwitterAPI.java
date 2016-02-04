package com.davidrueter.twitter;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;

import processing.core.PApplet;
import processing.core.PImage;
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
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;

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
		System.err.println("Twitter API error encountered, details below:");
		t.printStackTrace(System.err);
	}

	protected void initOnStatusCallback() {
		try {
			statusMethod = myParent.getClass().getMethod("onStatus", Status.class);
		} catch (NoSuchMethodException e){
			throw new RuntimeException("No onStatus() method found in the sketch.");
		}
	}

	/** Tweets the supplied status.
	 * 
	 * @param status the status to tweet
	 * @return
	 */
	public boolean tweet(String status){
		try {
			twitter.updateStatus(status);
			return true;
		} catch (TwitterException e){
			logThrowable(e);
			return false;
		}
	}

	/** Tweets an image plus a status.
	 * 
	 * @param image the image to tweet
	 * @param status the status to add
	 * @param imageName a name for the image.
	 * @return
	 */
	public boolean tweetImage( PImage image, String status, String imageName){
		try {
			return tweetImage(makeStreamFromPImage(image), imageName, status);
		} catch (IOException e) {
			logThrowable(e);
			return false;
		}
	}

	private ImageWriter imageioWriter(String extension) {
		Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName(extension);
		if (iter.hasNext()) {
			return iter.next();
		}
		return null;
	}


	/** Implementation copied from LGPL source in Processing's PImage class. Unfortunately,
	 * the code wasn't factored to allow reuse :(
	 * Currently, this implementation stores the image in memory as a JPEG, then sends it.
	 * A future implementation might optimize this and start streaming the JPEG before it is fully
	 * constructed.
	 * @param image
	 * @return
	 */
	public InputStream makeStreamFromPImage(PImage image) throws IOException {

		int outputFormat = BufferedImage.TYPE_INT_RGB;

		BufferedImage bimage = new BufferedImage(image.pixelWidth, image.pixelHeight, outputFormat);
		bimage.setRGB(0, 0, image.pixelWidth, image.pixelHeight, image.pixels, 0, image.pixelWidth);

		ImageWriter writer = null;
		ImageWriteParam param = null;
		IIOMetadata metadata = null;

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		if ((writer = imageioWriter("jpeg")) != null) {
			// Set JPEG quality to 90% with baseline optimization. Setting this
			// to 1 was a huge jump (about triple the size), so this seems good.
			// Oddly, a smaller file size than Photoshop at 90%, but I suppose
			// it's a completely different algorithm.
			param = writer.getDefaultWriteParam();
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(0.9f);

			writer.setOutput(ImageIO.createImageOutputStream(out));
			//		        writer.write(null, new IIOImage(bimage, null, null), param);
			writer.write(metadata, new IIOImage(bimage, null, metadata), param);
			writer.dispose();

		} else {
			boolean written = javax.imageio.ImageIO.write(bimage, "jpeg", out);
			if (!written) return null;
		}
		return new ByteArrayInputStream(out.toByteArray());

	}

	/** Tweets the supplied image plus a status. Uses the same logic for resolving file
	 * paths as Processing's loadImage and loadStrings functions.
	 * 
	 * @param imagePath path to the image (a local path relative to the data folder,
	 * an absolute path, or a url.
	 * @param imageName
	 * @param status
	 * @return
	 */
	public boolean tweetImage(String imagePath, String imageName, String status ){
		InputStream in = myParent.createInput(imagePath);
		return tweetImage(in, imageName, status);
	}

	/** Tweets the supplied image plus a status.
	 * 
	 * @param in
	 * @param imageName
	 * @param status
	 * @return
	 */
	public boolean tweetImage(InputStream in, String imageName, String status) {
		ImageUpload uploader = new ImageUploadFactory(twitterConfig)
		.getInstance( new OAuthAuthorization(twitterConfig));	
		try {
			uploader.upload(imageName, in ,status);
			return true;
		} catch (TwitterException e) {
			logThrowable(e);
			return false;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** Gets an array of longs, representing the current users's followers.
	 * 
	 * @return a long array of foller IDs
	 */
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

