package template.library;


import processing.core.*;
	
	
	 void welcome() {
		System.out.println("MotionGame 0.0 by Shrey; github:@itsShreyArora");
		 System.out.println("processing library for posenet and runway ml");
	}
	
	void health(a){
	float health = a;
	}
void hit(x){

}
score(){
score = health**1/2;
	return score;

}

	
	void drawPolygon(float cenx, float ceny, float radius, int numVertices) {
  drawPolygon(cenx, ceny, radius, radius, numVertices, 0);
}


void drawPolygon(float cenx, float ceny, float radius, int numVertices, float startTheta) {
  drawPolygon(cenx, ceny, radius, radius, numVertices, startTheta);
}


void makeatracefunction(){}
//TRACE BODY!


void drawPolygon(float cenx, float ceny, float xRadius, float yRadius, int numVertices, float startTheta) {
  float theta = startTheta;
  float dTheta = TWO_PI / numVertices;
  beginShape();
  for (int i=0; i<numVertices; i++) {
    vertex(cenx + xRadius*cos(theta), 
      ceny + yRadius*sin(theta));
    theta += dTheta;
  }
  endShape(CLOSE);
}


void drawPolygon(float cenx, float ceny, float radius, float[] angles) {
  drawPolygon(cenx, ceny, radius, radius, angles);
}




void drawPolygon(float cenx, float ceny, float xRadius, float yRadius, float[] angles) {
  beginShape();
  for (int i=0; i<angles.length; i++) {
    vertex(cenx + xRadius*cos(angles[i]), 
      ceny + yRadius*sin(angles[i]));
  }
  endShape(CLOSE);
	
	
	// runway connect-disconnect
	
	void connect() {
  OscMessage m = new OscMessage("/server/connect");
  oscP5.send(m, myBroadcastLocation);
}

void disconnect() {
  OscMessage m = new OscMessage("/server/disconnect");
  oscP5.send(m, myBroadcastLocation);
}

	
}

