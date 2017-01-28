import twitterp.*;
import twitter4j.*;


TwitterAPI api;
PImage lastImage;

void setup() {
  size(800,600);
  // Important -- replace the below strings with your own info!
  api = new TwitterAPI(this, "consumerKey",
                              "consumerSecret",
                              "accessToken",
                              "accessTokenSecret");
  api.filter("Internet");
}

void draw() {
  background(0);
  fill(255);
  textAlign(CENTER);
  rectMode(CENTER);
  if (lastImage != null){
    image(lastImage,0,0);
  }
}

void onStatus(Status status){
   
   lastImage = api.extractFirstImage(status);
}