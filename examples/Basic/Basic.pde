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
  // call getNormalizedDiscreteStepValue() to get discrete value, useful for interfaces that have buttons or keys
  // the interface that sends OSC should send normalized discrete step values to /dancing-drawings-discrete
  float step = dancingDrawings.getNormalizedDiscreteStepValue();
  ellipse(
    step * width,
    step * height,
    40,
    40
  );
}
