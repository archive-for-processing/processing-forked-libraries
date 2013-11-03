import algAndBeau.seenelib.*;

SeeneLibrary seeneLibrary;
SeeneObject seeneObject;
void setup() 
{
  size(800,400,P3D);
  
  seeneLibrary = new SeeneLibrary(this);
  seeneObject = seeneLibrary.createSeeneObjectFromFile("pennies.oemodel",
                                                       "poster.jpg");
}

void draw() 
{
  background(0);

  translate(-700,
            0,
            mouseY*-750.f/height);
  int count = 10;
  for(int i = 0; i < count; i++)
  { 
    pushMatrix();
    
    translate(300*i,
              height/2);
    rotateY(mouseX*PI/width + -i*TWO_PI/count);
    seeneObject.draw();
    
    popMatrix();
  }
}