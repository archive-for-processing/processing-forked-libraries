package manoloide.ImageProcessor;

import processing.core.*;

public class ImageProcessor implements PConstants{
	
	PApplet a;
	public ImageProcessor(PApplet applet){
		this.a = applet;
	}

	public void displace(float dr, float dg, float db){
		a.image(displace(a.g.get(), dr, dg, db), 0, 0);
	}

	public PImage displace(PImage ori, float dr, float dg, float db){
		a.pushStyle();
		a.colorMode(RGB);
		PImage aux = a.createImage(ori.width, ori.height, RGB);
		aux.loadPixels();
		for(int j = 0; j < ori.height; j++){
			for(int i = 0; i < ori.width; i++){
				float r = a.red(ori.get((int)(i+dr),j));//int(i-dr), j);
				float g = a.green(ori.get((int)(i+dg),j));//int(i-dg), j);
				float b = a.blue(ori.get((int)(i+db),j));//int(i-db), j);
				int col = a.color(r, g, b); 
				aux.set(i, j, col);
			}
		}
		aux.updatePixels();
		a.popStyle();
		return aux;
	}

	public PImage noise(float amount){
		return noise(a.g, amount, false);
	}

	public PImage noise(PImage ori, float amount){
		return noise(ori, amount, false);
	}

	public PImage noise(float amount, boolean color){
		return noise(a.g, amount, color);
	}

	public PImage noise(PImage ori, float amount, boolean color){
		a.pushStyle();
		a.colorMode(RGB);
		int w = ori.width;
		int h = ori.height;
		ori.loadPixels();
		for(int i = 0; i < ori.pixels.length; i++){
			int col;
			if(color){
				col = a.color(a.random(255),a.random(255),a.random(255));
			}else{
				col = a.color(a.random(255));
			}
			ori.pixels[i] = a.lerpColor(ori.pixels[i], col, amount);
		}
		ori.updatePixels();
		a.popStyle();
		return ori;
	}

	public void vignette(float inte){
		a.image(vignette(a.g.get(), inte), 0, 0);
	}

	public void vignette(float inte, int nc){
		a.image(vignette(a.g.get(), inte, nc), 0, 0);
	}

	public PImage vignette(PImage ori, float inte){
		return vignette(a.g.get(), inte, a.color(0));
	}

	public PImage vignette(PImage ori, float inte, int nc){
		a.pushStyle();
		a.colorMode(RGB);
		ori.loadPixels();
		float cx = ori.width/2;
		float cy = ori.height/2;
		int cc = a.color(a.red(nc), a.green(nc), a.blue(nc));
		float diag = a.dist(0, 0, cx, cy);
		diag *= diag;
		for(int j = 0; j < ori.height; j++){
			for(int i = 0; i < ori.width; i++){
				float v = a.pow(cx-i, 2)+a.pow(cy-j, 2);
				int col = a.lerpColor(ori.get(i, j), a.color(cc), a.map(v, 0, diag, 0, inte)); 
				ori.set(i, j, col);
			}
		}
		ori.updatePixels();
		a.popStyle();

		return ori;
	}
}
