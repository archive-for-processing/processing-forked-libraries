//import bontempos.ProjectionMatrix.HomographyMatrix; //<>//
PImage picture ; // original image file
PVector mouseOffset;
boolean mouseStarts = false;


//for each different rectangle , you have 4 points like below:
//THIS IS EXAMPLE FOR "BG"
PVector[] bgVtx = new PVector[4]; //<---  this is the position of vertices in 2D space (in screen coordinate) 
PVector[] bgUV = new PVector[4];  //<---  this is the position of vertices (UV) in 2D space (in the picture coordinates)

//THIS IS EXAMPLE FOR "TRAIN"
PVector[] trainVtx = new PVector[4]; //<---  this is the position of vertices in 2D space (in screen coordinate) 
PVector[] trainUV = new PVector[4];  //<---  this is the position of vertices (UV) in 2D space (in the picture coordinates)

//THIS IS EXAMPLE FOR "ROOF"
PVector[] roofVtx = new PVector[4]; //<---  this is the position of vertices in 2D space (in screen coordinate) 
PVector[] roofUV = new PVector[4];  //<---  this is the position of vertices (UV) in 2D space (in the picture coordinates)





void setup() {
  size( 800, 600, P3D);

  picture = loadImage( "homographyBGtest.jpg" );  // you are loading the VIDEO

  // BG   //--------------------------------------------------------
  //initially screen corners
  bgVtx [0] = new PVector(79, 50);
  bgVtx [1] = new PVector( 531, 40 );
  bgVtx [2] = new PVector( 599, 190 );
  bgVtx [3] = new PVector( 61, 225 );

  //## BG get UV from pictures (careful with the order)
  bgUV [0] = new PVector( 16, 8);
  bgUV [1] = new PVector( 481, 8 );
  bgUV [2] = new PVector( 481, 162 );
  bgUV [3] = new PVector( 16, 162 );

  //----------------------------------------------------------------



  // TRAIN   //--------------------------------------------------------
  //initially screen corners
  //this 4 points are the values you get from mouse position after calibrating
  trainVtx [0] = new PVector();
  trainVtx [1] = new PVector( width, 0 );
  trainVtx [2] = new PVector( width, height );
  trainVtx [3] = new PVector( 0, height );

  //## BG get UV from pictures (careful with the order) (you get from photoshop)
  trainUV [0] = new PVector( 16, 8);
  trainUV [1] = new PVector( 481, 8 );
  trainUV [2] = new PVector( 481, 162 );
  trainUV [3] = new PVector( 16, 162 );

  //----------------------------------------------------------------



  // ROOF   //--------------------------------------------------------
  //initially screen corners
  //this 4 points are the values you get from mouse position after calibrating
  roofVtx [0] = new PVector();
  roofVtx [1] = new PVector( width, 0 );
  roofVtx [2] = new PVector( width, height );
  roofVtx [3] = new PVector( 0, height );

  //## BG get UV from pictures (careful with the order) (you get from photoshop)
  roofUV [0] = new PVector( 16, 8);
  roofUV [1] = new PVector( 481, 8 );
  roofUV [2] = new PVector( 481, 162 );
  roofUV [3] = new PVector( 16, 162 );
  //----------------------------------------------------------------
  
  
}



void draw() {

  background( 0 ); 

  mouseDetect(); //for calibration (you need to change the name of the object you want to calibrate)

  //here renders every part of the background in different rectangles

  //----------------------------------------------------------------
  //####  BG
  beginShape();
  texture(picture);
  for (int i = 0; i < 4; i++) {
    vertex(bgVtx[i].x, bgVtx[i].y, bgUV[i].x, bgUV[i].y); //<---------- just change the names here
  }
  endShape();
  //----------------------------------------------------------------


  //####  TRAIN
  beginShape();
  texture(picture);
  for (int i = 0; i < 4; i++) {
    vertex(trainVtx[i].x, trainVtx[i].y, trainUV[i].x, trainUV[i].y); //<---------- just change the names here
  }
  endShape();
  //----------------------------------------------------------------
  
  
  
  //####  ROOF
  beginShape();
  texture(picture);
  for (int i = 0; i < 4; i++) {
  vertex(roofVtx[i].x, roofVtx[i].y, roofUV[i].x, roofUV[i].y); //<---------- just change the names here
  }
  endShape();
  //----------------------------------------------------------------


  if (mousePressed) displayMousePos();

  fill(-1);
  text( frameRate, 10, 20 );
}


//this is to detect motion from mouse and keyboard inputs
void mouseDetect() {
  if (mousePressed) {
    if (!mouseStarts) {
      mouseStarts = true;
      mouseOffset = new PVector(mouseX, mouseY);
    }
    if (keyPressed) {
      if (key >= '1' && key < '5') {
        int index = key - '0' - 1;

        trainVtx[ index ] = new PVector(mouseX, mouseY); //<-------- HERE, you change the name of the object to calibrate
      }
    }
  }
}



/*
/this is jus to show the mouse position while pressing
 */
void displayMousePos() {
  fill(0);
  noStroke();
  ellipse ( mouseX, mouseY, 10, 10);
  rect(mouseX + 10, mouseY - 12, 70, 20);
  fill(-1);
  text(mouseX + ", " + mouseY, mouseX + 12, mouseY + 2);
  println( mouseX, mouseY );
}


//this restarts the mouse position 
void mouseReleased() {
  mouseStarts = false;
}