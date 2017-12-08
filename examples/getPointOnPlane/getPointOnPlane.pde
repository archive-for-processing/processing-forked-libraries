import bontempos.ProjectionMatrix.*; //<>//

void setup() {
  size( 500, 500, P3D);
}

void draw() {
  background(200);

  // camera view
  camera( 1160, -1960, 1730, 890, -1200, 1200, 0, 1, 0 );

  // Calculate the coordinates on the floor corresponding to the cursor position
  PVector floorPos = new PVector( 500, 300, 100 ); 
  PVector floorDir = new PVector( 0, -1, 0 );      
  PVector mousePos = UnprojectedView.getPointOnPlane( this, mouseX, mouseY, floorPos, floorDir );
  
  // floor
  pushMatrix();
    translate( floorPos.x, floorPos.y, floorPos.z );
    fill(255);
    box( 2000, 1, 2000 );
  popMatrix();

  // Draw a cube at the cursor position
  pushMatrix();
    translate( mousePos.x, mousePos.y, mousePos.z );
    fill(255, 0, 0);
    box(200);
  popMatrix();
}