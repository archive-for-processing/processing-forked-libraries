void chKeyCommands() {
  if (keyPressed ) {
    if ( key >= 1 + '0' && key <= 6 + '0') {
      int index = (key - '0') - 1;
      p2d[index] = new PVector(mouseX, mouseY);      
      viewProjMatrix = ViewProjection.get(p2d, p3d);
    }
    show2Dpoints();
  }
}

void show2Dpoints() {
  stroke(-1);
  strokeWeight(2);
  for (int i = 0; i < 6; i++) {        
    point( p2d[i].x, p2d[i].y );
    //println("point ", i, "["+p2d[i].x, ",", p2d[i].y+"]");
  }
}