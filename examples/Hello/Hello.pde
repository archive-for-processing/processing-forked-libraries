import template.library.*;

SeeneLibrary seeneLibrary;
SeeneObject so;
void setup() {
  size(400,400,P3D);
  smooth();
  
  seeneLibrary = new SeeneLibrary(this);
  println(seeneLibrary);
  try{
  so = seeneLibrary.createSeeneObjectFromFile("/Users/admin/processing-library-template/seene/data/scene (4).oemodel",
                                        "/Users/admin/processing-library-template/seene/data/poster (2).jpg");
  }
  catch(Exception e)
  {
    e.printStackTrace();
  }
  PFont font = createFont("",40);
  textFont(font);
}

void draw() {
  background(0);
  fill(255);
  so.draw();
  text(seeneLibrary.sayHello(), 40, 200);
}