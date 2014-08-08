import ZstProcessing.*;
import ZST.*;

Showtime node;

void setup() {
  size(400,400);
  smooth();
  String[] args = {"arg1"};
  node = new Showtime("processing","tcp://curiosity.soad.vuw.ac.nz:6000");
  node.registerMethod("testCallback", ZstMethod.WRITE, this, args);
}

void draw() {
  background(0);
  fill(255);
}

void testCallback(ZstMethod methodData){
  print("Hello world!");
}