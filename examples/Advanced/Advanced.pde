import template.library.*;
import oscP5.*;

DancingDrawings dancingDrawings;

void setup() {
  size(360, 560);
  smooth();
  dancingDrawings = new DancingDrawings(this, "/custom-address");
}

void draw() {
  background(255, 212, 128);
  fill(204);
  strokeWidth(32);
  ellipse(
    180,
    280 + dancingDrawings.getAftertouch()/32.0,
    200,
    300
  ); 

  fill(102);
  rect(111, 181, 63, 63);

  fill(255);
  ellipse(222, 244, 72,
    map(dancingDrawings.getValue(),
    1, 15, 1.0, 0.05) * 72);

  fill(233, 222, 222);
  quad(189, 264, 216, 264, 216, 310, 144, 310);

  fill(150);
  quad(
    100,
    360,
    250,
    360,
    220,
    390 + dancingDrawings.getAftertouch()/2,
    100, 390 + dancingDrawings.getAftertouch()/2
  );
}
