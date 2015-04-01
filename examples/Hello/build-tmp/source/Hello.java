import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import lazer.viz.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Hello extends PApplet {



LazerController controller;
LazerSyphon send;


public void setup() {
  size(400,400,P3D);
  smooth();

  controller = new LazerController(this);
  setControls();
  send = new LazerSyphon(this, 1024, 768, P3D);

  PFont font = createFont("",40);
  textFont(font);
}

public void draw() {
  background(0);
  fill(255);
  text(controller.get("seaHeight"), 40, 200);
}

public void setControls() {
  controller.setMapping("seaHeight", controller.SLIDER1, 20);
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Hello" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
