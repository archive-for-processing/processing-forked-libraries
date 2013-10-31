import template.library.*;

SeeneLibrary seeneLibrary;
SeeneObject seeneObject;
void setup() {
  size(400,400,P3D);
  
  seeneLibrary = new SeeneLibrary(this);
  seeneObject = seeneLibrary.createSeeneObjectFromFile("/Users/admin/processing-library-template/seene/data/scene (5).oemodel",
                                                       "/Users/admin/processing-library-template/seene/data/poster (2).jpg");}

void draw() {
  background(0);
  fill(255);
  seeneObject.draw(mouseX,mouseY,150);
}