 //<>//
/*
  Example of using Projection Matirx class
 
 This class generates a View*Projection (view multiplied by projection) matrix based on
 point correspondences between a 3D model and it's projected coordinate on a projector view.
 
 it can be used to calibrate a kinect projector system
 
 This example shows a calibration model (Cube) using the minimal (6) point-calibration method.
 Hold keys 1 to 6 to change the calibration points and see how it affects the projection.
 
 */

import bontempos.ProjectionMatrix.*;
import java.util.HashSet;

//import peasy.*;
//PeasyCam cam;

ArrayList<PVector> objVtxList;

PVector [] p3d = new PVector[6];
PVector [] p2d = new PVector[6];
PShape model3d;

PMatrix3D model = new PMatrix3D(); // world 3D matrix



PMatrix3D viewProjMatrix;

int selVtx = 0;

void setup() {
  //fullScreen(P3D);
  size(600, 300, P3D);

  //cam = new PeasyCam(this, 100);
  //cam.beginHUD();

  initKinect();

  ViewProjection.enableUtils(this); //enables shortcut for calibration and save/load
  ViewProjection.flipped(true);

  ViewProjection.setModelScale(20); //center to wall dist. A box() obj must be double this size
  p3d = ViewProjection.useCubeCalibrationModel();

  p2d[0]=  new PVector(199, 113 );
  p2d[1]=  new PVector( 195, 181 );
  p2d[2]=  new PVector( 255, 145 ); 
  p2d[3]=  new PVector( 253, 220 ); 
  p2d[4]=  new PVector( 318, 102 ); 
  p2d[5]=  new PVector( 326, 185 );


  viewProjMatrix = ViewProjection.get(p2d, p3d);
  
  
  model3d = loadShape("box10.obj");
  buildObjVertexList(model3d);

  //cam.endHUD();
}



void draw() {
  background(0);
  //identity matrix to represent the model default transformations (translation, rotation, scale)
  model = new PMatrix3D();

  //ZOOM IN
 if (keyPressed && key == 'h') {
    model.scale(1.1);
  }

  //press 0 to rotate the model
  if (keyPressed && key == '0') {
    model.rotateY( radians(1));
  }
  if (keyPressed && key == '9') {
    model.rotateY( radians(-1));
  }


  if (keyPressed && key == 'O') {
    model.rotateZ( radians(1));
  }
  if (keyPressed && key == 'I') {
    model.rotateZ( radians(-1));
  }

  if (keyPressed && key == 'A') {
    model.translate( -1, 0, 0);
  }
  if (keyPressed && key == 'D') {
    model.translate( 1, 0, 0);
  }
  if (keyPressed && key == 'W') {
    model.translate( 0, 0, -1);
  }
  if (keyPressed && key == 'S') {
    model.translate( 0, 0, 1);
  }


  //update 2d points and recalibrate matrix
  else if (keyPressed && key >= '1' && key <= '6') {
    viewProjMatrix = ViewProjection.updateP2D();
  }else if (keyPressed && key == 'l'){
    viewProjMatrix = ViewProjection.updateP2D();
  }


  //move mouse left to right to change light orientation
  //directionalLight(255, 255, 255, map(mouseX, 0, width, -1, 1), 0, 1); // invert last parameter if all black depending on the model normals


  viewProjMatrix.apply(model);
  
  pushMatrix();
  applyMatrix(viewProjMatrix);

  
  noFill();
  strokeWeight(1);
  box(40);
  drawKMatrix();
  // shape(model3d, 0, 0);
  buildPointCloud();  
  popMatrix();  
  // display2DprojectedVertices();

  
}


void display2DprojectedVertices() {
  /*
  displays the position of vertex of a 3D model in its original position (not considering model transformation);
   */
  hint(DISABLE_DEPTH_TEST);
  for (int i = 0; i < 6; i++) {
    PVector p0 = ViewProjection.solve( p3d[i], viewProjMatrix);
    stroke(-1);
    strokeWeight(2);
    point(p0.x, p0.y);
  }
  hint(ENABLE_DEPTH_TEST);
}





PVector getObjVertex(PShape obj, int index) {
  for (int i = 0; i < obj.getChildCount(); i++) {
    PShape child = obj.getChild(i);
    for (int f = 0; f< child.getVertexCount(); f++) {
      if (f == index) return child.getVertex(f);
    }
  }
  return new PVector();
}

PVector getObjVertex(int index) {
  for (int f = 0; f< objVtxList.size(); f++) {
    if (f == index) return objVtxList.get(index);
  }
  return new PVector();
}

void buildObjVertexList(PShape obj) {
  objVtxList = new ArrayList<PVector>();
  for (int i = 0; i < obj.getChildCount(); i++) {
    PShape child = obj.getChild(i);
    for (int f = 0; f< child.getVertexCount(); f++) {
      objVtxList.add( child.getVertex(f) );
    }
  }
  HashSet noDups = new HashSet(objVtxList);
  objVtxList.clear();
  objVtxList.addAll(noDups);
}




void show2Dpoints() {
  stroke(-1);
  strokeWeight(2);
  for (int i = 0; i < 6; i++) {        
    point( p2d[i].x, p2d[i].y );
    //println("point ", i, "["+p2d[i].x, ",", p2d[i].y+"]");
  }
}




void output2Dpoints() {
  println("------ 2D points ------");
  for (int i = 0; i < 6; i++) {        
    println("point ", i, "["+p2d[i].x, ",", p2d[i].y+"]");
  }
}


void keyPressed() {
  if (key == '.') selVtx++;
  else if ( key ==  ESC) {
    output2Dpoints();
  }
}