import bontempos.ProjectionMatrix.*; //<>//
import java.util.HashSet;

ArrayList<PVector> objVtxList;
PShape obj3D;

PMatrix originalMatrix;

void setup() {
  size( 500, 500, OPENGL);
  obj3D = loadShape("box10.obj");
  originalMatrix = getMatrix((PMatrix3D)null);
}

void draw() {
  background(200);
  hint(ENABLE_DEPTH_SORT);
  // camera view
  camera( 50, -50, 50, 0, 0, 0, 0, 1, 0 );


  // Calculate the coordinates on the floor corresponding to the cursor position
  PVector floorPos = new PVector( 0, 5, 0 ); 
  PVector floorDir = new PVector( 0, -1, 0 );      
  PVector mousePos = UnprojectedView.getPointOnPlane( this, mouseX, mouseY, floorPos, floorDir );


  stroke(0);
  strokeWeight(1);


  // floor
  pushMatrix();
  translate( floorPos.x, floorPos.y, floorPos.z );
  fill(255);
  //box( 50, 1, 50 );
  popMatrix();

  // Draw a cube at the cursor position
  pushMatrix();
  translate( mousePos.x, mousePos.y, mousePos.z );
  fill(255, 0, 0);
  //box(10);
  popMatrix();

  shape(obj3D, 0, 0);


  pickingVtx();
}

void pickingVtx() {
  buildObjVertexList(obj3D);

  PVector floorDir = new PVector( 0, 0, -1 );    
  println("list-------");
  for ( PVector v : objVtxList) {
    PVector vtxPos2D = UnprojectedView.solve(this, v);
    println(vtxPos2D);
    stroke(#ff0000);
    strokeWeight(5);
    pushMatrix();
    hint(DISABLE_DEPTH_TEST);
    resetMatrix();
    applyMatrix(originalMatrix);
    point(vtxPos2D.x, vtxPos2D.y);
    hint(ENABLE_DEPTH_TEST);
    popMatrix();
  }
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