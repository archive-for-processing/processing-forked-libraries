import com.davidrueter.twitter.*;
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
  api.user();
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
  lastStatus = status.getText();
}