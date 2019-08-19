package template.library;


import processing.core.*;

public class HelloLibrary {
	

	PApplet myParent;

	int myVariable = 0;
	
	public final static String VERSION = "##library.prettyVersion##";
	

	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the Library.
	 * 
	 * @example Hello
	 * @param theParent the parent PApplet
	 */
	public HelloLibrary(PApplet theParent) {
		myParent = theParent;
		welcome();
	}
	
	
	private void welcome() {
		System.out.println("##library.name## ##library.prettyVersion## by ##author##");
	}
	
	
	public String sayHello() {
		return "hello library.";
	}
	
	public static String version() {
		return VERSION;
	}


	public void setVariable(int theA, int theB) {
		myVariable = theA + theB;
	}


	public int getVariable() {
		return myVariable;
	}
	
	void drawPolygon(float cenx, float ceny, float radius, int numVertices) {
  drawPolygon(cenx, ceny, radius, radius, numVertices, 0);
}


void drawPolygon(float cenx, float ceny, float radius, int numVertices, float startTheta) {
  drawPolygon(cenx, ceny, radius, radius, numVertices, startTheta);
}


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

