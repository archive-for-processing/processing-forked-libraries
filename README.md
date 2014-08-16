Showtime-Processing
===================

Showtime was designed to let multiple programs running in multiple languages talk to each other whilst trying to cut down on the clutter required in setting up connections and discovering each other. 

The project originated from the hassles I underwent trying to hook the music software Ableton Live up to Unity using OSC. I wrote the first version of this library using Python and C# to let Unity control Ableton Live through its underlying Python API, without needing to use any MIDI or OSC whatsoever, and that eventually evolved into the Java and Processing ports as well.

Installation
------------

Grab a copy of the lastest build from the [releases](https://github.com/Mystfit/Showtime-Processing/releases) page and unzip into your libraries directory.

Usage
-----

Here's an example of how to use the library where we're connecting to a [Showtime-Live node](https://github.com/Mystfit/Showtime-Live) hooked up to Ableton Live. The full version of this example can be found in the Examples window underneath `Contributed Libraries->Showtime Processing Bridge`

### Setup ###
 - Set up the Stage node. This is to provide a fixed point that all nodes need to register their addresses and methods to upon startup, so that they can be discovered by other nodes. *TODO create stage node processing sketch.* In the meantime, I'd recommend using the zst_stage.py script from [Showtime-Python](https://github.com/Mystfit/Showtime-Python) to create a stage.
 - Import the libraries.
```
import ZstProcessing.*;  // Processing specific lib
import ZST.*;
```
 - Create a new node with a unique name and the address/port of the stage.
```
Showtime node = new Showtime("processing", "127.0.0.1:6000");
```


### Exploring the stage ### 
 - Ask the stage for a list of all the nodes available. 
```
Map<String, ZstPeerlink> peers = node.requestNodePeerlinks();
```

### Listening to other nodes ###
 - If we want to listen to messages being sent from another node, subscribe to the node and assign local callback functions that will run when messages of that type arrive.
```
  ZstPeerlink liveNode = peers.get("LiveNode");
  node.subscribeToNode(liveNode);
  node.subscribe(liveNode.getMethod("meters_updated"), "displayMeter", this);
```
 - The callback function needs to accept a single ZstMethod argument, which is the method object sent by the remote node.
```
void displayMeter(ZstMethod methodData) {
 println(meterData.getName());        // Name of method
 println(meterData.getNode());        // Name of origin node this method belongs to
 println(meterData.getAccessMode());  // Type of method: read, write or responder
 println(meterData.getArgs());        // Arguments remote method accepts. Map of String/Objects.
 println(meterData.getOutput();       // Output of remote method. 
}
```

### Controlling remote nodes ###
 - If we want to control a remote node, we have to ask it to listen to messages that we send its way.
```
node.connectToPeer(liveNode);

// Need to wait a few ms for the remote node to connect
// else it loses the first bunch of messages 
delay(100);
```
 - When calling a remote method, we need to provide a Map of Strings/Objects containing the arguments that we're sending, followed by the call to the remote method.
```
Map<String, Object> clipArgs = new HashMap<String, Object>();
clipArgs.put("trackindex", 0);
clipArgs.put("clipindex", 0);
node.updateRemoteMethod(liveNode.getMethod("fire_clip"), clipArgs);
```
 - When working with methods where the accessmode is a responder, the `updateRemoteMethod()` call will return a ZstMethod containing the output of the method. Something to be aware of is that if the remote node doesn't send back a response, the program may wait indefinitely. This will be fixed in a future release.
```
Map<String, Object> songArgs = new HashMap<String, Object>();
songArgs.put("category", 0);  // The category lets us switch between normal tracks and return tracks
ZstMethod layoutMethod = node.updateRemoteMethod(liveNode.getMethod("get_tracks"), songArgs);

// Convert the track layout from JSON
JSONObject parser = new JSONObject();
trackList = parser.parse(layoutMethod.getOutput().toString()).getJSONArray("tracklist");
trackColors = new color[trackList.size()]; 
```

### Registering local methods ###
 - To register a local method for other nodes to control, you need to provide the method name, the accessmode, the arguments required when calling the method, the object to run the callback method from, and the name of the callback method to run. The callback needs to accept a ZstMethod object in order to access incoming arguments.
```
String[] args = {"arg1"};
node.registerMethod("testCallback", ZstMethod.WRITE, this, args);

void testCallback(ZstMethod methodData){
  println(methodData.getArgs().get("arg1"));
}
```

### Updating local methods ###
- When we've run a local method, we need to let any listening nodes know that the method was called.
```
node.registerMethod("foo", ZstMethod.READ, this, args);

void foo(String someMessage){
 node.updateLocalMethod(node.getMethods().get("foo"), someMessage);
}
```
- We can update local methods by name as well.
```
node.updateLocalMethodByName("foo", someMessage);
```


### Accessmode types ###
- `ZstMethod.READ`: Can subscribe to messages from this remote method, but cannot call it.
- `ZstMethod.WRITE`: Can call this remote method but cannot listen to messages it sends.
- `ZstMethod.RESPONDER`: Can call this remote method and receive a response. This is the most similar to how you would usually call a local method.


Contributing
------------
If you want to modify/compile the library iteslf then feel free! Instructions for compiling under Eclipse are located [here.](https://github.com/processing/processing-library-template)
If you decide to use this library or Showtime in general then feel free to flick me a message, I'd appreciate any feedback!
