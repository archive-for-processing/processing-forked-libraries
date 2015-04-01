import lazer.viz.*;

LazerController controller;
LazerSyphon send;


void setup() {
  size(400,400,P3D);
  smooth();

  controller = new LazerController(this);
  setControls();
  send = new LazerSyphon(this, 1024, 768, P3D);

  PFont font = createFont("",40);
  textFont(font);
}

void draw() {
  background(0);
  fill(255);
  text(controller.get("seaHeight"), 40, 200);
}

void setControls() {
  controller.setMapping("seaHeight", controller.SLIDER1, 20);
}