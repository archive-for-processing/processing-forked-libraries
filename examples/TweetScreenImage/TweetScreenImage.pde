import twitterp.*;
import twitter4j.*;

TwitterAPI api;
int hue = 0;
PGraphics g; // offscreen graphics object.

/*
  This example tweets what Processing is displaying.
  Press the 't' key to tweet the image on the screen.
*/

void setup() {
  size(480,480);
    // Important -- replace the below strings with your own info!
  api = new TwitterAPI(this, "consumerKey",
                              "consumerSecret",
                              "accessToken",
                              "accessTokenSecret");
  g = createGraphics(width, height); // initialize offscreen graphics
}

void keyPressed(){
  if (key == 't'){ // Press t to tweet the screen
    boolean success = api.tweetImage(g, "Twitter2.jpeg", "image from screen");  
    if (success)  java.awt.Toolkit.getDefaultToolkit().beep();
  }
}

void draw() {
  // Draw to an offscreen buffer, g, so that we can send the image both to the screen and twitter.
  g.beginDraw();
  g.colorMode(HSB);
  g.background(hue, 255, 200);
  g.endDraw();
  
  // draw the image to the screen
  image(g,0,0);
  hue++;
  if (hue > 255) hue = 0;
}
