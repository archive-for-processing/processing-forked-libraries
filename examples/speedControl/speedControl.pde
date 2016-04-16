import funGUI.*;

Slider controller;

void setup() {
  size(300, 300);
  controller = new Slider(this, width / 2, height - 20, -100, 100, "Speed");
}
float x = 0;
float y = height / 2;
void draw() {
  background(255);
  x+= controller.p();
  rect(x, y, 20, 10);
  if (x > width + 20) x = -20;
  if (x < -20) x = width + 20;
  controller.draw();
}