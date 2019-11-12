import template.library.*;
import oscP5.*;

DancingDrawings dancingDrawings;

void setup() {
  size(360, 560);
  smooth();
  dancingDrawings = new DancingDrawings(this);
}

void draw() {
  background(255, 212, 128);
  fill(204);
  strokeWeight(5);
  ellipse(
    180,
    // call getNormalizedContinuousValue() to get a value that has been set by a continuous controller
    // such as mod-wheel or aftertouch.
    280 + dancingDrawings.getNormalizedContinuousValue()*10.0,
    200,
    300
  ); 

  fill(102);
  rect(111, 181, 63, 63);

  fill(255);
  ellipse(
    222,
    244,
    72,
    // call getNormalizedDiscreteStepValue() to get a value that has been set by a
    // discrete-type of interface input, such as a keyboard key
    // or guitar-controller note
    dancingDrawings.getNormalizedDiscreteStepValue() * 72
  );

  fill(233, 222, 222);
  quad(189, 264, 216, 264, 216, 310, 144, 310);

  fill(150);
  quad(
    100,
    360,
    250,
    360,
    220,
    // call getNormalizedContinuousValue() to get a value that has been set by a continuous controller
    // such as mod-wheel or aftertouch.
    390 + dancingDrawings.getNormalizedContinuousValue() * 64,
    100, 390 + dancingDrawings.getNormalizedContinuousValue() * 64
  );
}
