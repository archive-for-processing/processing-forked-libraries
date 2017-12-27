import bontempos.ProjectionMatrix.*; //<>//
import java.util.HashSet;

ArrayList<PVector> objVtxList;
PShape obj3D;

PMatrix cameraMatrix;

void setup() {
  size( 500, 500, P3D);
  obj3D = loadShape("box10.obj");
  //setCamera(); // if camera is fixed on run
}

void setCamera() {
  camera( 50, -50, 50, 0, 0, 0, 0, 1, 0 );
  cameraMatrix = getMatrix((PMatrix3D)null);
  UnprojectedView.setWindow3DPicking(this);
}

void draw() {
  background(200);
  setCamera(); // if camera suppose to change on run
  shape(obj3D, 0, 0);
  pickingVtx();
}

void pickingVtx() {
  buildObjVertexList(obj3D);
  stroke(#ff0000);
  strokeWeight(5);
  println("list-------");
  
  //converting 3D point on object to 2D view position
  //this conversion depends on current camera settings
  PVector[] to2D = new PVector[objVtxList.size()];
  for ( int i = 0; i < objVtxList.size(); i++) {
    to2D[i] = UnprojectedView.solve( objVtxList.get(i) );
  }

  //reset matrix:
  camera();
  hint(DISABLE_DEPTH_TEST);
  for ( PVector v : to2D) {
    point(v.x, v.y);
  }
  hint(ENABLE_DEPTH_TEST);
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