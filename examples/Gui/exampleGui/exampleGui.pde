/*
 botonlabel
 */

import manoloide.Gui.*;

Gui gui;
Panel panel; 

void setup() {  
  size(800, 600); 
  gui = new Gui(this);
  panel = new Panel("panel", 10, 10, 120, 500);
  gui.add(panel);
  panel.add(new Slider("background", 10, 10, 100, 10, 0, 255, 128));
  panel.add(new Button("random", 10, 50, 20, 20));
  panel.add(new Toggle("holaa", 60, 50, 20, 20, false));
  panel.add(new Pad("pad", 10, 90, 100, 80, 0, width, 0, height));
  panel.add(new Slider("v slide", 10, 200, 10, 100, 0, 1000, 500));
}

void draw() {
  Slider s = (Slider) panel.get("background");
  if (((Button) panel.get("random")).click) {
    s.set(random(256));
  }
  background(s.getInt());
  Pad cor = (Pad) panel.get("pad");
  noStroke();
  fill(255-s.getInt());
  ellipse(cor.valx, cor.valy, 40, 40);
}