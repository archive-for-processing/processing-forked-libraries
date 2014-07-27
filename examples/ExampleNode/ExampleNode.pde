import ZstProcessing.*;

Showtime node;

void setup() {
  size(400,400);
  smooth();
  
  node = new Showtime("testNode","curiosity.soad.vuw.ac.nz:6000");
}

void draw() {
  background(0);
  fill(255);
}
