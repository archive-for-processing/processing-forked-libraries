import algAndBeau.seenelib.*;

SeeneLibrary seeneLibrary;
SeeneObject seeneObject;

void setup() 
{
  size(700,700,P3D);
  seeneLibrary = new SeeneLibrary(this);
  seeneObject = seeneLibrary.createSeeneObjectFromURL("http://seene.co/s/78e5xo");
}

void draw()
{
  background(0);

  translate(width/2,height/2,-100);

  for(int i = 0; i < 50; i++)
  {
    translate(mouseX/2,mouseY/2-height/4,-500);
    rotateY(PI*(mouseX*1.f/width-1/2));
    seeneObject.draw();
  }
}