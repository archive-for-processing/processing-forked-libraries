import algAndBeau.seenelib.*;

SeeneLibrary seeneLibrary;
SeeneObject seeneObject;
PImage seeneTexture;

void setup() 
{
  size(700,700,P3D);
  seeneLibrary = new SeeneLibrary(this);
  seeneObject = seeneLibrary.createSeeneObjectFromFile("3dstuff.oemodel",
                                                       "poster.jpg");
  seeneTexture = seeneObject.getTextureImage();
}

void draw() 
{
  background(0);
  
  translate(width/2,height/2,-500);
  rotateY(mouseX*TWO_PI/width);
  rotateX(mouseY*TWO_PI/height);
  rotateZ(-PI/2);
  translate(-seeneTexture.width/2,-seeneTexture.height/2);
  
  image(seeneTexture,0,0);
}