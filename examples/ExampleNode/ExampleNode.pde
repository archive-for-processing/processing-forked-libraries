import ZstProcessing.*;
import ZST.*;
import java.util.Map;

Showtime node;

void setup() {
  size(400,400);
  smooth();
  String[] args = {"arg1"};
  node = new Showtime("processing","tcp://curiosity.soad.vuw.ac.nz:6000");
  node.registerMethod("testCallback", ZstMethod.WRITE, this, args);
  Map<String, ZstPeerlink> peers = node.requestNodePeerlinks();
  
  node.subscribe(peers.get("LiveNode").getMethods().get("fire_clip"), "testSubscriber", this);
}

void draw() {
  background(0);
  fill(255);
}

void testSubscriber(ZstMethod methodData){
  println(methodData.getOutput());
}

void testCallback(ZstMethod methodData){
  println(methodData.getArgs().get("arg1"));
}