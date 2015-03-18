import lazer.viz.*;

LazerController controller;

void setup() {
  size(400,400);
  smooth();
  
  controller = new LazerController(this);
  
  PFont font = createFont("",40);
  textFont(font);
}

void draw() {
  background(0);
  fill(255);
  text(controller.get("seaHeight"), 40, 200);
}
