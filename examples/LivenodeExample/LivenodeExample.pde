import ZstProcessing.*;
import ZST.*;
import java.util.Map;


Showtime node;
HashMap<Integer, Float> meterValues;
JSONObject songLayout;

void setup() {
  size(400, 400);

  meterValues = new HashMap<Integer, Float>();
  //Create a new Showtime node and connect it to a stage node.
  //Here I have it connecting a Python instance running the zst_stage.py script
  node = new Showtime("processing", "tcp://curiosity.soad.vuw.ac.nz:6000");

  //Get a list of nodes connected to the stage
  Map<String, ZstPeerlink> peers = node.requestNodePeerlinks();

  //Subscribe to outgoing messages from a Showtime-Live node called 'LiveNode' running in Ableton Live.
  ZstPeerlink liveNode = peers.get("LiveNode");
  node.subscribeToNode(liveNode);

  //Register a local callback for 'output_meter' messages being sent from Live
  node.subscribe(liveNode.getMethod("output_meter"), "displayMeter", this);

  //Connect to Live node so that it can receive commands from us
  node.connectToPeer(liveNode);

  //Need to wait a few ms for the Live node to connect else we lose the first lot of messages
  delay(100);

  //Build the arguments we want to send to Live. 
  //This command will play the first clip on the first track
  Map<String, Object> clipArgs = new HashMap<String, Object>();
  clipArgs.put("trackindex", 0);
  clipArgs.put("clipindex", 0);
  node.updateRemoteMethod(liveNode.getMethod("fire_clip"), clipArgs);
  
  //Map<String, Object> songArgs = new HashMap<String, Object>();
  //songArgs.put("category", 0);

  //Send the remote message to Live
  //songLayout = new JSONObject();
  //ZstMethod layoutMethod = node.updateRemoteMethod(liveNode.getMethod("get_tracks"), songArgs);
  //println(layoutMethod);
  //songLayout = songLayout.parse(layoutMethod.getOutput().toString());
}
void draw() {
}
void displayMeter(ZstMethod methodData) {
  //Print any output meter messages we receive from Live
  JSONObject d = new JSONObject();
  d = d.parse(methodData.getOutput().toString());

  meterValues.put(d.getInt("trackindex"), d.getFloat("value"));

  //Draw meters
  background(0);

  fill(255, 0, 0);
  int count = 0;
  for (Map.Entry meter : meterValues.entrySet ()) {
    rect(count * width/meterValues.size(), height, width/meterValues.size(), (Float)meter.getValue() * -height);
    count++;
  }
}
