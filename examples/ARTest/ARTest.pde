import com.mondonerd.ARsenico.*;
// 1)
//import processing.video.*;

import ketai.camera.*;
int PHOTO = 0;
int WEBCAM = 1;

int mode = PHOTO;

AugmentedReality ar;
PImage photo;
// 2)
//Capture cam;

KetaiCamera cam;

PFont currentFont;
int selectedMarkerID = 0;



void onCameraPreviewEvent()
{
  cam.read();
}


void setup() {

  currentFont = loadFont("elegant.vlw");
  textFont(currentFont, 23);
  photo = loadImage("arsenico_test.png");
  // 3)
  //cam = new Capture(this, 320, 240);
  cam = new KetaiCamera(this, 320, 240, 24);
  ar = new AugmentedReality(this, 320, 240);
  ar.loadMarkers("markers/4x4_1.patt", "markers/4x4_2.patt", "markers/4x4_3.patt", "markers/4x4_4.patt", "markers/4x4_5.patt", "markers/4x4_6.patt", "markers/4x4_7.patt", "markers/4x4_8.patt", "markers/4x4_9.patt", "markers/4x4_10.patt");
  noStroke();
  colorMode(HSB, 360, 100, 100);
  registerMethod("exit", this);
}

public String sketchRenderer() {
  return P3D;
}

void exit() {
  if (cam != null)
    cam.stop();

  //println("Exit");
  super.exit();
}

void draw() {
  if (mode == PHOTO) {
    // ar.refreshInput(photo);
    background(255);
    imageMode(CENTER);
    image(photo, width / 2, height / 2);
    imageMode(CORNER);
    return;
  } 
  else if (mode == WEBCAM) {
    // 4)
    //if (cam.available())
    //  onCameraPreviewEvent();

    ar.refreshInput(cam);
  } 

  hint(PApplet.DISABLE_DEPTH_TEST);
  image(ar.getReality(), 0, 0, width, height);
  lights();
  ar.display();
  if (ar.debug) {
    fill(255, 100);
    text("FPS: " + int(frameRate), 20, 30);
  } 
  else {

    textFont(currentFont, 22);
    fill(255, 255);
    text("Augmented Reality by mondonerd.com under GPL v3\nCopyright 2013", 20, 30);
    if (mode == PHOTO) {

      textFont(currentFont, 26);
      fill(255, 200);
      text("ARsenico Test", 20, height - 80);
    }
  }
}

void displayScene(int markerID) {
  translate(0, 0, 90 + sin(radians(millis() * .1)) * 50);
  fill(markerID * 36, 100, 100);
  box(80);
}

void mousePressed() {
  if (mode == PHOTO) {
    // ar.toggleMirror();
    cam.start();
    mode = WEBCAM;
  } 
  else if (mode == WEBCAM) {
    // ar.toggleMirror();
    cam.stop();
    mode = PHOTO;
  }
}
// 5)
//*

void keyPressed() {
  if (key == CODED) {
    if (keyCode == MENU) {
      if (cam.isFlashEnabled())
        cam.disableFlash();
      else
        cam.enableFlash();
    }
  }
}
//*/

