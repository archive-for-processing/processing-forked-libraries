import template.library.*;
import oscP5.*;

DancingDrawings dancingDrawings;

void setup() {
  size(360, 560);
  smooth();
  dancingDrawings = new DancingDrawings(this, "/custom-address");
}

void draw() {
  background(85, 75, 99);
  fill(204);
  ellipse(180, 280, 200, 300); 

  fill(102);
  rect(111, 181, 63, 63);

  fill(255);
  ellipse(222, 244, 72, 72);

  fill(233, 222, 222);
  quad(189, 264, 216, 264, 216, 310, 144, 310);

  fill(150);
  quad(
    100,
    360,
    250,
    360,
    220,
    390 + dancingDrawings.getValue()*2,
    100, 390 + dancingDrawings.getValue()*2
  );
}
