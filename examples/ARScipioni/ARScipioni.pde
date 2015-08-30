/*
ARsenico
 
 Released under GPL v3
 
 */

KetaiCamera cam;
boolean touched;
UI ui;
int lastAR = 0;
void onCameraPreviewEvent() {
  cam.read();
  if (ar != null && millis() - lastAR > 100) {
    ar.refreshInput(cam);
    lastAR = millis();
    
  }
}

AugmentedReality ar;

int cameraFrameRate = 30;
int cameraWidth = 352;
int cameraHeight = 288;
String[] markersFilenames = new String[] { 
  "4x4_1.patt", "4x4_2.patt", "4x4_3.patt", "4x4_4.patt", "4x4_5.patt", "4x4_6.patt", "4x4_7.patt", "4x4_8.patt", "4x4_9.patt", 
};
String[] modelsFilenames = new String[] {
  "Olla01.obj", "Olla01.obj", "Olla01.obj", "Olla01.obj", "Olla01.obj", "Olla01.obj", "Olla01.obj", "Olla01.obj", "Olla01.obj", "Olla01.obj",
};
int counter = 0;
PShape models;
boolean record = false;

static final int LIGHT_LOW = 0;
static final int LIGHT_MID = 1;
static final int LIGHT_HIGH = 2;
int currentWeather = LIGHT_LOW;

void setup() {
  fullScreen(P3D);
  background(255, 255);
  PImage logo = loadImage("logo.png");
  imageMode(CENTER);
  image(logo, width / 2, height / 2);
  cam = new KetaiCamera(this, cameraWidth, cameraHeight, cameraFrameRate);
  cam.start();
  ar = new AugmentedReality(this, cam.width, cam.height);
  ar.loadMarkers(markersFilenames);
  registerMethod("exit", this);

  noStroke();
  imageMode(CORNERS);
  textureWrap(REPEAT);
  println("DISPLAY WIDTH " + displayWidth);
  ui = new UI(this);
  ui.add("changeSettings", "button2.png", 40, 40, displayWidth/10, displayWidth/10);
  textFont(createFont("Robota", 100), 100);
  textAlign(LEFT, TOP);
  thread("loadAsset");
}
boolean ready = false;
PShape[] modelsArray;
void loadAsset() {
  ready = false;
  models = createShape(GROUP);
  modelsArray = new PShape[modelsFilenames.length];
  for (int i = 0; i < modelsFilenames.length; i++) {
    PShape cube = loadShape(modelsFilenames[i]);

    //cube.setStroke(false);
    modelsArray[i] = cube;
    cube.rotateX(radians(90));
    cube.scale(80);
    println("added " + modelsFilenames[i]);
    //models.rotateX(radians(90));
    //models.scale(80);
  }
  ready = true;
}

void exit() {
  println("disposed");
  if (cam != null) {
    cam.stop();
    cam.dispose();

    println("disposed webcam");
  }
  super.exit();
}
void changeSettings(UI.Button button, int action, int x, int y) {
  println(button + ", " + action + " at " + x + "," + y);
  if (action==2){
    if (currentWeather == LIGHT_LOW){ 
      currentWeather = LIGHT_MID;
      cam.disableFlash();
      button.setImage("button2.png");
    } else if (currentWeather == LIGHT_MID){ //night
      currentWeather = LIGHT_LOW;
      cam.enableFlash();
      button.setImage("button1.png");
    }
  }
}

void draw() {
  if (!ready || !touched)
    return;
  
  image(cam, 0, 0, width, height);
  noLights();
  ar.display();
  hint(DISABLE_DEPTH_TEST);
  ui.display();
  fill(255, 0, 0);
  text(frameRate, 200, 10);
}

void mousePressed() {
    touched = true;
}

void displayScene(int markerID) {
  //translate(0, 0, 40);
  
  shape(modelsArray[markerID % modelsArray.length]);
}