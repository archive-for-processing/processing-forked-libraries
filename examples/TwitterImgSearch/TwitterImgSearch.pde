import twitterp.*;
import twitter4j.*;

/** Searches Twitter for images based on a query. For query help, see:
https://dev.twitter.com/rest/public/search
**/

TwitterAPI api;
PImage[] images;
int cursor;

void setup() {
  size(800,600);
  // Important -- replace the below strings with your own info!
    api = new TwitterAPI(this, "consumerKey",
                              "consumerSecret",
                              "accessToken",
                              "accessTokenSecret");
    images = api.searchImages("#tree", 30);
}

void draw() {
  background(0);
  fill(255);
  textAlign(CENTER);
  rectMode(CENTER);
  if (images.length > 0)
    image(images[cursor],0,0);
  
}

void keyPressed(){
  cursor++;  
  if (cursor >= images.length) cursor = 0;
}