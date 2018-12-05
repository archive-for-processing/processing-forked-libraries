import stlr.library.*;
import peasy.*;

PShape curve;
PShape sheath;
BufferedReader in;
PeasyCam cam;
STLr stlr;
ArrayList<PVector> points;

void setup() {
  size(640, 480, P3D);
  cam = new PeasyCam(this, 300);
  points = new ArrayList<PVector>();
  in = createReader("data/curve.txt");
  curve = createShape();
  curve.beginShape();
  curve.noFill();
  curve.stroke(255);
  curve.strokeWeight(3);
  curve.strokeCap(ROUND);
  String line;
  try {
    while ((line = in.readLine()) != null) {
      String[] pieces = split(line, TAB);
      float x = 100*float(pieces[0]);
      float y = 100*float(pieces[1]);
      float z = 100*float(pieces[2]);
      curve.curveVertex(x, y, z);
      points.add(new PVector(x, y, z));
    }
    in.close();
  } catch (IOException e) {
    e.printStackTrace();
  }
  curve.endShape();
  stlr = new STLr(this);
  sheath = stlr.noodlize(curve, 13, 5, 10);
}

void keyPressed() {
  if(key == 's') {
    println("Creating Ascii File...");
    stlr.generateAsciiSTL(sheath,"data/noodle");
    println("File done.");
    exit();
  }
}

void draw() {
  background(128);
  lights();
  shape(curve);
  shape(sheath);
  strokeWeight(10);
  for(PVector p : points)
    point(p.x, p.y, p.z);
}
