import algAndBeau.seenelib.*;

SeeneLibrary seeneLibrary;
SeeneObject seeneObject;
PShape seeneShape;

void setup() 
{
  size(500,500,P3D);
  seeneLibrary = new SeeneLibrary(this);
  seeneObject = seeneLibrary.createSeeneObjectFromFile("3dstuff.oemodel",
                                                       "poster.jpg");
  seeneShape = seeneObject.getShape();
}

void draw() 
{
  background(0);

  translate(width/2,height/2,-100);
  scale(600);
  rotateZ(-PI/2);
  rotateY(millis()/1000.f);
  
  stroke(255);
  beginShape(POINTS);
  for(int i = 0; i < seeneShape.getVertexCount(); i++)
  {
    PVector shapeVect = seeneShape.getVertex(i);
    vertex(shapeVect.x,shapeVect.y,shapeVect.z);
  }
  endShape();
}