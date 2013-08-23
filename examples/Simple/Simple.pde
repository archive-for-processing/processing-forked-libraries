/**
 * @author Nicolas Clavaud <antiplastik@gmail.com>
 */

import picking.*;

Picker picker;
float a = 0.0;

void setup() {
  size(200, 150, P3D);
  smooth();
  picker = new Picker(this);
}

void draw() {
  a += 0.01;

  lights();
  clear();

  picker.start(0);
  drawBox(80, 75, 50, #ff8800);

  picker.start(1);
  drawBox(140, 55, 20, #eeee00);

  picker.stop();

  // get picker value at mouse coordinates
  int id = picker.get(mouseX, mouseY);

  color c = 0;
  if (id == 0) {
    c = #ff8800; 
  }
  else if (id == 1) {
    c = #eeee00;
  }

  if (id > -1) {
    drawBox(142, 93, 12, c);
  }
}

void drawBox(int x, int y, int s, color c) {
  stroke(0);
  fill(c);
  pushMatrix();
    translate(x, y);
    rotateX(a); rotateY(a);
    box(s);
  popMatrix();
}
