import twitterp.*;
import twitter4j.*;


TwitterAPI api;
String lastStatus;

long[] followers;

void setup() {
  size(800,600);
    // Important -- replace the below strings with your own info!
  api = new TwitterAPI(this, "consumerKey",
                              "consumerSecret",
                              "accessToken",
                              "accessTokenSecret");
  lastStatus = "";
  
  followers = api.getFollowers();
  if (followers.length > 0){
    api.filterUsers(followers);
  } else {
    println("No Followers!");  
  }
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
  if (status.getText().toLowerCase().contains("hello")){
    api.tweet("Hi @" + status.getUser().getName() + "!");
  }
}