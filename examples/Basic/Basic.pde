import template.library.*;
import oscP5.*;

DancingDrawings dancingDrawings;

void setup() {
  size(400,400);
  smooth();
  dancingDrawings = new DancingDrawings(this);
}

void draw() {
  background(100, 30, 30);
  fill(255);
  ellipse(width/2 + dancingDrawings.getValue(), height/2 + dancingDrawings.getValue(), 40, 40);
}
