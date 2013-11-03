import template.library.*;

SeeneLibrary seeneLibrary;
SeeneObject seeneObject;
PShape seeneShape;

void setup() 
{
  size(700,700,P3D);
  seeneLibrary = new SeeneLibrary(this);
  seeneObject = seeneLibrary.createSeeneObjectFromURL("http://seene.co/s/rUaySD");
  seeneShape = seeneObject.getShape();
}

void draw() 
{
  background(0);
  fill(0);
  stroke(255);
  translate(width/2,height/2,-500);
  pushMatrix();
  scale(400);
  rotateZ(-PI/2);
  rotateY(millis()/1000.f);
  beginShape(POINTS);
  for(int i = 0; i < seeneShape.getVertexCount(); i++)
  {
    PVector shapeVect = seeneShape.getVertex(i);
    vertex(shapeVect.x,shapeVect.y,shapeVect.z);
  }
  endShape();
  popMatrix();
  
 
}