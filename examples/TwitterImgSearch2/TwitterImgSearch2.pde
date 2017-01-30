import twitterp.*;
import twitter4j.*;

/** Searches Twitter for images based on a query. For query help, see:
https://dev.twitter.com/rest/public/search
**/

TwitterAPI api;
int cursor;
String[] words = "It was a bright cold day in April and the clocks were striking thirteen".split(" ");
PImage[] images = new PImage[words.length];

void setup() {
  size(800,600);
  // Important -- replace the below strings with your own info!
    api = new TwitterAPI(this, "consumerKey",
                            "consumerSecret",
                            "accessToken",
                            "accessTokenSecret");
    for (int i = 0; i < words.length; i++){
      PImage[] results = api.searchImages(words[i],3);
      if (results != null && results.length > 0){
          images[i] = results[(int)random(0,results.length)];
      }
    }
}

void draw() {
  background(0);
  fill(255);
  textAlign(CENTER);
  textSize(80);
  
  imageMode(CENTER);
  if (images.length > 0)
    image(images[cursor],width/2,height/2);
  text(words[cursor], width/2, height/2);
}

void keyPressed(){
  cursor++;  
  if (cursor >= images.length) cursor = 0;
}