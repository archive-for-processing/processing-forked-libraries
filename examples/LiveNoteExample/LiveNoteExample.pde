import ZstProcessing.*;
import ZST.*;
import java.util.Map;
import java.util.Queue;
import java.util.Iterator;
import java.util.concurrent.*;


Showtime node;
HashMap<Integer, Float> meterValues;
JSONArray trackList;
color[] trackColors;
float lastTime = 0;
ConcurrentLinkedQueue<JSONArray> notes;
ArrayList<Bubble> bubbles;

float scrollspeed = 6.0;

void setup() {
  size(1280, 720);
  frameRate(120);
  
  notes = new ConcurrentLinkedQueue<JSONArray>();
  bubbles= new ArrayList<Bubble>();
  meterValues = new HashMap<Integer, Float>();
  //Create a new Showtime node and connect it to a stage node.
  //Here I have it connecting a Python instance running the zst_stage.py script
  node = new Showtime("processing", "127.0.0.1:6000");
  
  //Get a list of nodes connected to the stage
  Map<String, ZstPeerlink> peers = node.requestNodePeerlinks();

  //Subscribe to outgoing messages from a Showtime-Live node called 'LiveNode' running in Ableton Live.
  ZstPeerlink liveNode = peers.get("LiveNode");
  node.subscribeToNode(liveNode);

  //Register a local callback to catch 'meters_updated' messages being sent from Live
  node.subscribe(liveNode.getMethod("track_playing_notes"), "displayNotes", this);

  //Tell the Ableton Live Showtime node to listen to our messages
  node.connectToPeer(liveNode);

  //Need to wait a few ms for the Live node to connect else it loses the first bunch of messages 
  delay(100);
}

void draw() {
  background(0);

    
  while(notes.size() > 0){
    JSONArray notedata = notes.poll();
    int note = notedata.getInt(0);
    float duration = notedata.getFloat(2);
    bubbles.add(new Bubble(new PVector(note/127.0*width, height + (frameCount * scrollspeed) - 20), duration)); 
  }
  
  translate(0.0, frameCount * -scrollspeed);
  
  Iterator<Bubble> i = bubbles.iterator();
  while(i.hasNext()){
    Bubble b = i.next();
    b.update();
    if(b.life < 0.0){
      i.remove();
    }
  }
}

void displayNotes(ZstMethod methodData) {
  //Parse meter data
  JSONObject parser = new JSONObject();
  JSONArray incomingnotes = parser.parse(methodData.getOutput().toString()).getJSONArray("value");

  for (int i =0; i < incomingnotes.size (); i++){
    String noteStatus = incomingnotes.getJSONObject(i).getString("status");
    JSONArray noteData = incomingnotes.getJSONObject(i).getJSONArray("note");
    if(noteStatus.equals("on")){
      notes.add(noteData);
    }
  }  
}

