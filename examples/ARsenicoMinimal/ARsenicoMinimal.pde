import com.mondonerd.ARsenico.*;

AugmentedReality ar;
PImage testInput;

void setup() {
  testInput = loadImage("foto.jpg");
  //this starts everything and must be called first but after the size()
  ar = new AugmentedReality(this); //have a look at the reference for more options
  //ar = new AugmentedReality(this, 320, 240); // for better performances
  
  //load markers from .png or .patt files
  ar.loadMarkers("hiro.png", "marker3.png");
  // hiro.png has ID = 0, marker3.png ID = 1...
  
  //we use a still image as input but can be any PImage compatible object like Capture or Movie or Ketai ;-)
  
  ar.refreshInput(testInput);

  noStroke();
}

String sketchRenderer() {
 return P3D; 
}

void mousePressed() {
  ar.toggleMirror();
  ar.refreshInput(testInput);
}

void draw() {
  //if your input changes you need to refresh it for detection
  //ar.refreshInput(testInput);
  background(0);
  lights();
  image(ar.getReality(), 0, 0, width, height);
  ar.display();
}

/**
 Each marker will "call" this function passing its ID. This way you can use different implementation or 3D scene for each marker.
 ID is given to markers at loading time so it depends on what you write inside your loadMarkers() function.
 **/

void displayScene(int markerID) {
  translate(0, 0, 90 + sin(radians(millis() * .1)) * 50);
  box(60);
}