import visualizer.library.*;

Visualizer visual;

void setup() {
  size(400,400);
  smooth();
  
  visual = new Visualizer(this);
  
  PFont font = createFont("",40);
  textFont(font);
}

void draw() {
  background(0);
  fill(255);
  text(visual.sayHello(), 40, 200);
}
