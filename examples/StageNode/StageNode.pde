import ZstProcessing.*;
import ZST.*;
import java.util.Map;

Showtime node;

void setup() {
  size(400,400);
  smooth();
  node = new Showtime("stage",6000);
}

void draw() {
  background(0);
  fill(255);
}