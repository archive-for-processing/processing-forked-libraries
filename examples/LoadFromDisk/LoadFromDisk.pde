import algAndBeau.seenelib.*;

SeeneLibrary seeneLibrary;
SeeneObject seeneObject;
void setup() 
{
  size(400,400,P3D);
  
  seeneLibrary = new SeeneLibrary(this);
  seeneObject = seeneLibrary.createSeeneObjectFromFile("scene (5).oemodel",
                                                       "poster (2).jpg");
}

void draw() 
{
  background(0);
  fill(255);
  seeneObject.draw(mouseX,mouseY,150);
}