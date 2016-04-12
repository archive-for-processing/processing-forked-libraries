package funGUI;

import processing.core.*;

public class PhotoButton extends RectButton {
	PImage face;

	public PhotoButton(PApplet p, float nx, float ny, float nw, float nh, int[] ncolors, String ntext) {
		super(p, nx, ny, nw, nh, ncolors, ntext);
		
		// Default case, need to get a default image
		face = p.loadImage("default.jpg");
		w = face.width;
		h = face.height;
	}
	
	public void draw() {
		g.pushStyle();
		g.imageMode(CENTER);
		g.image(face, x, y);
		g.popStyle();
	}

}
