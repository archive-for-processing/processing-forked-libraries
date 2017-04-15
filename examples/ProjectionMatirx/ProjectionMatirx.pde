/* //<>//
  Example of using Projection Matirx class
 
 This class generates a View*Projection (view multiplied by projection) matrix based on
 point correspondences between a 3D model and it's projected coordinate on a projector view.
 
 I am finally able to use it with the Processing native PGraphics3D!
 
 This example shows a calibration model (Cube) using the minimal (6) point-calibration method.
 Hold keys 1 to 6 to change the calibration points and see how it affects the projection.
 
 */

import bontempos.ProjectionMatrix.*;
import java.util.HashSet;

ArrayList<PVector> objVtxList;


PVector [] p3d = new PVector[6];
PVector [] p2d = new PVector[6];
PVector center = new PVector();
PShape model3d;
float mouseZ;


PMatrix3D viewProjMatrix;

int selVtx = 0;

void setup() {
  size(600, 300, P3D);

  ViewProjection.flipped(true);


  //default correspondent points of a cube
  float s = 5; //scale
  p3d[0]=  new PVector( s, -s, s);
  p3d[1]=  new PVector( s, s, s );
  p3d[2]=  new PVector( -s, -s, s); //in this 3D model, this is point index = 0 (-5-5 5)
  p3d[3]=  new PVector( -s, s, s );
  p3d[4]=  new PVector( -s, -s, -s );
  p3d[5]=  new PVector( -s, s, -s );


  p2d[0]=  new PVector(199, 113 );
  p2d[1]=  new PVector( 195, 181 );
  p2d[2]=  new PVector( 255, 145 ); 
  p2d[3]=  new PVector( 253, 220 ); 
  p2d[4]=  new PVector( 318, 102 ); 
  p2d[5]=  new PVector( 326, 185 );


  viewProjMatrix = ViewProjection.get(p2d, p3d);

  /*the above line is creating this matrix
   viewProjMatrix = new PMatrix3D(
   -004.7986, -002.4045, -002.3774, 253.9886, 
   -002.8312, 006.1314, 006.2057, 219.9871, 
   0f, 0f, -1, 1f, 
   000.0059, -000.0094, 000.0147, 1
   );
   */

  model3d = loadShape("box10.obj");
  buildObjVertexList(model3d);
}


void keyPressed() {
  if (key == '.') selVtx++;
}

void draw() {

  //identity matrix to represent the model default transformations (translation, rotation, scale)
  PMatrix3D model = new PMatrix3D();


  //press 0 to rotate the model
  if (keyPressed && key == '0')model.rotateY(radians(0.5));


  background(50);


  //move mouse left to right to change light orientation
  directionalLight(255, 255, 255, map(mouseX, 0, width, -1, 1), 0, 1); // invert last parameter if all black depending on the model normals

  chKeyCommands();

  viewProjMatrix.apply(model);
  pushMatrix();
  applyMatrix(viewProjMatrix);
  shape(model3d, 0, 0);

  noStroke();
  fill(#ff0000);
  pushMatrix();
  translate(20, 0);
  //processing default box object can also be loaded and used on the projection
  box(10);
  popMatrix();

  stroke(-1);
  strokeWeight(5);
  PVector pp =  getObjVertex(selVtx);
  point(pp.x, pp.y, pp.z);
  println("Point", selVtx, pp);
  popMatrix();

  display2DprojectedVertices();
}


void display2DprojectedVertices() {
  /*
  displays the position of vertex of a 3D model in its original position (not considering model transformation);
  */
  hint(DISABLE_DEPTH_TEST);
  for (int i = 0; i < 6; i++) {
    PVector p0 = ViewProjection.solve( p3d[i], ViewProjection.get(p2d, p3d));
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