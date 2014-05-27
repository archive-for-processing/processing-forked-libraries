package manoloide.ImageProcessor;

import processing.core.*;

public class ImageProcessor implements PConstants{
	PApplet applet;
	public ImageProcessor(PApplet applet){
		this.applet = applet;
	}
	public PImage noise(PImage ori, float cant){
		return noise(ori, cant, false);
	}
	public PImage noise(PImage ori, float cant, boolean color){
		int w = ori.width;
		int h = ori.height;
		PImage aux = applet.createImage(w, h, ARGB);
		aux.loadPixels();
		ori.loadPixels();
		for(int i = 0; i < ori.pixels.length; i++){
			int col;
			if(color){
				col = applet.color(applet.random(255),applet.random(255),applet.random(255));
			}else{
				col = applet.color(applet.random(255));
			}
			aux.pixels[i] = applet.lerpColor(ori.pixels[i], col, cant);
		}
		ori.updatePixels();
		aux.updatePixels();
		return aux;
	}
}
