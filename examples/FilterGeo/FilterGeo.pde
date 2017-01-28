import twitterp.*;
import twitter4j.*;


TwitterAPI api;
String lastStatus;

void setup() {
  size(800,600);
  // Important -- replace the below strings with your own info!
  api = new TwitterAPI(this, "consumerKey",
                              "consumerSecret",
                              "accessToken",
                              "accessTokenSecret");
  api.filterGeo(41.529755, -88.065227,42.089961, -87.341794);
  lastStatus = "";
}

void draw() {
  background(0);
  fill(255);
  textAlign(CENTER);
  rectMode(CENTER);
  text(lastStatus, width/2, height/2, width, height / 4);
}

void onStatus(Status status){
  // Only update status if it has an actual geo location, not just a "place"
  GeoLocation loc = status.getGeoLocation();
  if (loc != null){
    lastStatus = loc.getLatitude() + "," + loc.getLongitude() + ": " + status.getText();
  }
}