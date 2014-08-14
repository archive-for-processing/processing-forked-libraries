import ZstProcessing.*;
import ZST.*;
import java.util.Map;

Showtime node;
HashMap<Integer, Float> meterValues;
JSONArray trackList;
color[] trackColors;
float lastTime = 0;

void setup() {
  size(800, 400);
  smooth();

  meterValues = new HashMap<Integer, Float>();
  //Create a new Showtime node and connect it to a stage node.
  //Here I have it connecting a Python instance running the zst_stage.py script
  node = new Showtime("processing", "tcp://curiosity.soad.vuw.ac.nz:6000");

  //Get a list of nodes connected to the stage
  Map<String, ZstPeerlink> peers = node.requestNodePeerlinks();

  //Subscribe to outgoing messages from a Showtime-Live node called 'LiveNode' running in Ableton Live.
  ZstPeerlink liveNode = peers.get("LiveNode");
  node.subscribeToNode(liveNode);

  //Register a local callback to catch 'meters_updated' messages being sent from Live
  node.subscribe(liveNode.getMethod("meters_updated"), "displayMeter", this);

  //Tell the Ableton Live Showtime node to listen to our messages
  node.connectToPeer(liveNode);

  //Need to wait a few ms for the Live node to connect else it loses the first bunch of messages 
  delay(100);

  //Get the song layout from so we can grab track colours
  Map<String, Object> songArgs = new HashMap<String, Object>();
  songArgs.put("category", 0);  //The category lets us switch between normal tracks and return tracks
  ZstMethod layoutMethod = node.updateRemoteMethod(liveNode.getMethod("get_tracks"), songArgs);

  //Convert the track layout from JSON
  JSONObject parser = new JSONObject();
  trackList = parser.parse(layoutMethod.getOutput().toString()).getJSONArray("tracklist");
  trackColors = new color[trackList.size()];

  //Convert int colours to colour objects
  for (int i = 0; i < trackList.size (); i++) 
    trackColors[i] = intToColor(trackList.getJSONObject(i).getInt("color"));

  //This method will play the first clip on the first track
  Map<String, Object> clipArgs = new HashMap<String, Object>();
  clipArgs.put("trackindex", 0);
  clipArgs.put("clipindex", 0);
  node.updateRemoteMethod(liveNode.getMethod("fire_clip"), clipArgs);
}

void draw() {
  background(0);

  for (int i = 0; i < trackList.size (); i++) {   
    if (meterValues.size() > 0) {
      //Draw outlines
      stroke(80);
      strokeWeight(0.4);
      fill(0);
      rect(i * width/meterValues.size(), 0, width/meterValues.size(), height);

      //Draw interiors
      noStroke();
      fill(trackColors[i]);
      rect(i * width/meterValues.size(), height, width/meterValues.size(), meterValues.get(i) * -height);
    }
  }
}

void displayMeter(ZstMethod methodData) {
  //Parse meter data
  JSONArray meterData = new JSONArray();
  meterData = meterData.parse(methodData.getOutput().toString());

  for (int i =0; i < meterData.size (); i++)
    meterValues.put(i, meterData.getFloat(i));

  print("Delta: ");
  println(millis() - lastTime);
  lastTime = millis();
}

color intToColor(int col)
{
  int r = (col >> 16) & 0xFF;  // Faster way of getting red(argb)
  int g = (col >> 8) & 0xFF;   // Faster way of getting green(argb)
  int b = col & 0xFF;          // Faster way of getting blue(argb)

  return color(r, g, b);
}
