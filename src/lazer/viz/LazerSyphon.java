package lazer.viz;

import codeanticode.syphon.*;
import processing.core.*;

/**
 * A thin wrapper around SyphonServer that provides methods to
 * draw to an external buffer and send it out through Syphon
 * @author dario
 *
 */
public class LazerSyphon {
	public PGraphics g;
	private SyphonServer server;
	
	/**
	 * 
	 * @param p the PApplet object (the parent)
	 * @param width the width of the created buffer
	 * @param height the height of the created buffer
	 * @param rendererType the renderer type
	 */
	public LazerSyphon(PApplet p, int width, int height, String rendererType) {
		g = p.createGraphics(width, height, rendererType);
		server = new SyphonServer(p, "LazerSyphon");
	}
	
	/**
	 * sends the image through syphon
	 */
	public void send() {
		server.sendImage(g);
	}
	
	/**
	 * begins the render, setting some LazerSausage defaults
	 */
	public void begin() {
		g.beginDraw();
		g.background(0);
		g.colorMode(g.HSB, 127);
	}
	
	/**
	 * finish the draw
	 */
	public void end() {
		g.endDraw();
	}
	
	/**
	 * 
	 * @return PGraphics object to draw to
	 */
	public PGraphics getGraphics() {
		return g;
	}
}
