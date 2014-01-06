package polargraph.test;

import processing.serial.Serial;

import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

import polargraph.Polargraph;
import polargraph.PolargraphDrawing;
import polargraph.comms.Command;
import polargraph.comms.CommandFactory;
import polargraph.queue.QueueWriter;
import polargraph.queue.QueuedCommand;
import polargraph.queue.VirtualComQueueWriter;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

import geomerative.*;
import processing.core.PApplet;
import processing.core.PVector;

public class Comms extends PApplet {

//  These variables are set up in the setup() method.
private Polargraph machine = null;
private PolargraphDrawing drawing = null;
private VirtualComQueueWriter queue = null;

// a shape to draw
private RShape rshape;

/*
 * Set up the panels that we'll use for input.  These are visual only, and their sizes, and positions will
 * be mapped onto the Polargraph model that we're using.
 */
private PVector panelOrigin = new PVector(10,10);
private float panelWidthOnScreen = 485;  // width to display the panel on the screen
private Rectangle2D machinePanel = null; 
private Rectangle2D inputPanel = null;

/*
 * Set up a handful of position vectors
 */
private PVector mousePos = new PVector(0,0);     // the position of the mouse in the sketch
private PVector machinePos = new PVector(0,0);     // the position of the mouse relative to the machinePanel
private PVector inputPos = new PVector(0,0);     // the position of the mouse relative to the inputPanel
private PVector cartesianPos = new PVector(0,0); // the position of the mouse, if it were projected onto the machine (cartesian)
private PVector nativePos = new PVector(0,0);    // the position of the mouse on the machine, in native units and coordinates.
	
public void setup() {
	println("Running!");
	//frame.setResizable(true);
	size(1000, 600, JAVA2D);

	/* create a polargraph object.  In this case, parameters here are
	 * 1. Width in mm,
	 * 2. Height in mm,
	 * 3. Drawable area in mm, specified using a Rectangle2D object (eg origin, width and height).
	 * 4. Motor step size per mm
	 */
	this.machine = new Polargraph(10, 10, new Rectangle2D.Float(1,1,8,8), 1.0f);
	
	/*
	 * Now choose a area to restrict your drawing to.  This might be the same as your 
	 * drawable area, but probably not.  Parameters are
	 * 1. a name - useful if you have multiple drawing areas.
	 * 2. an extent, specified using a Rectangle2D object (origin, width, height)
	 */
	drawing = this.machine.createNewDrawing("main", new Rectangle2D.Float(5,5,4,4));
	
	/*
	 * Connect up a queue writer to a particular com port.
	 * The parameter here is the Serial object to communicate on.
	 */
	queue = new VirtualComQueueWriter(getPort("COM8"));

	// set up some panel sizes for the display areas
	// work out the scaling factor between the machine model and the on-screen panel
	float scaling = panelWidthOnScreen  / (float) machine.getExtent().getWidth();
	machinePanel = new Rectangle2D.Float(panelOrigin.x, panelOrigin.y, (float) machine.getExtent().getWidth() * scaling, (float) machine.getExtent().getHeight() * scaling);
	inputPanel = new Rectangle2D.Float((float) drawing.getExtent().getX() * scaling, 
									   (float) drawing.getExtent().getY() * scaling, 
									   (float) drawing.getExtent().getWidth() * scaling, 
									   (float) drawing.getExtent().getHeight() * scaling);
	
	drawing.setInputScaling(inputPanel);
	
	// make a piece of geometry using geomerative
	RG.init(this);
	RG.ignoreStyles();
	RG.setPolygonizer(RG.ADAPTATIVE);
	
	// make up a shape
	rshape = new RShape();
	rshape.addMoveTo(100, 50);
	rshape.addLineTo(300, 50);
	rshape.addLineTo(300, 150);
	rshape.addLineTo(100, 150);
	rshape.addLineTo(100, 50);
	
}

/**
 * Gets a serial connection, or null if the one asked for didn't work. 
 * @return
 */
public Serial getPort(String portName) {
	String[] serialPorts = Serial.list();
	println("Serial ports available on your machine:");
	println(serialPorts);
	Serial port = null;
	try {
		port = new Serial(this, portName, 57600);
		println("Port selected: " + portName + ", " + port.toString());
	}
	catch (Exception e) {
		// not using a serial port!
		println("Exception.");
		port = null;
	}
	return port;
}

public void serialEvent(Serial port) {
	try {
		this.queue.handleIncomingEvent();
	}
	catch (Exception e) {
//		println("Exception during serial event: " + e.toString());
//		e.printStackTrace();
	}
}

public void keyPressed() {
	println("RENDER!");
	// I'm going to send some commands!
	
	// first make a shape
	
	fill(100,255,100);
	RShape cShape = drawing.getAsCartesian(this.rshape);
	cShape.draw();
	
	fill(100,100,255);
	RShape nShape = drawing.getAsNative(this.rshape);
	nShape.draw();
	
	RPoint[][] pointPaths = nShape.getPointsInPaths();
    if (pointPaths != null)
    {
      for(int i = 0; i<pointPaths.length; i++)
      {
        if (pointPaths[i] != null) 
        {
        	println("P: " + pointPaths[i].toString());
        	//pen up, move to point
			beginShape();
			Command c = CommandFactory.newCommand(CommandFactory.CMD_PEN_UP);
			this.queue.add(c);
			 println(c.toString());
			c = CommandFactory.newCommand(CommandFactory.CMD_CHANGELENGTH)
			  .addParam("a", pointPaths[i][0].x)
			  .addParam("b", pointPaths[i][0].y);
			this.queue.add(c);
			println(c.toString() + ", valid: " + c.isValid());
			c = CommandFactory.newCommand(CommandFactory.CMD_PEN_DOWN);
			this.queue.add(c);
			println(c.toString());
			for (int j = 0; j<pointPaths[i].length; j++)
			{
				c = CommandFactory.newCommand(CommandFactory.CMD_CHANGELENGTH)
					  .addParam("a", pointPaths[i][j].x)
					  .addParam("b", pointPaths[i][j].y);
				this.queue.add(c);
				println(c.toString() + ", valid: " + c.isValid());
				println(c.getDefinition().getPattern().pattern());
			}
			c = CommandFactory.newCommand(CommandFactory.CMD_PEN_UP);
			this.queue.add(c);
			println(c.toString() + ", valid: " + c.isValid());
			endShape();
        }
      }
    }		
	
	for (RPoint p : nShape.getPoints()) {
		System.out.println("p: "+ p.x + ", "+p.y);
		ellipse(p.x, p.y, 10, 10);
	}
	
	for (QueuedCommand c : this.queue.iterator()) {
		println("...." + c);
	}
}

public void draw() {
	background(85);
	// draw some panels
	fill(110);
	noStroke();
	
	// draw the machine panel
	rect((float)this.machinePanel.getX(), (float)this.machinePanel.getY(), (float)this.machinePanel.getWidth(), (float)this.machinePanel.getHeight());
	strokeWeight(2);
	stroke(0,0,0,128);
	line(485f/2f, panelOrigin.y, 485f/2f, panelOrigin.y+485.0f);
	stroke(255);
	// draw the input (drawing) area panel
	rect((float)this.inputPanel.getX(), (float)this.inputPanel.getY(), (float)this.inputPanel.getWidth(), (float)this.inputPanel.getHeight());
	
	// get some mouse input
	if (this.machinePanel.contains(mouseX, mouseY)) {
		mousePos.x = mouseX;
		mousePos.y = mouseY;
	}
	
	// Draw the hanging lines
	stroke(255,255,255, 128);
	strokeWeight(3);
	line((int)this.machinePanel.getX(), (int)this.machinePanel.getY(), mousePos.x, mousePos.y);
	line((int)this.machinePanel.getMaxX(), (int)this.machinePanel.getY(), mousePos.x, mousePos.y);
	
	// draw a gondola
	noFill();
	ellipse(mousePos.x, mousePos.y, 20, 20);
	
	// work out the positions of the "gondola" over the panels on the screen
	machinePos = PVector.sub(mousePos, panelOrigin);
	inputPos = PVector.sub(machinePos, new PVector((float)this.inputPanel.getX(), (float)this.inputPanel.getY()));
	inputPos.add(panelOrigin);
	
	/* Now, the actual machine is not the same size as the rectangle on the screen, so we'll do a bit of scaling
	   to figure out the position on the machine that this screen pos should be mapped to.  Strictly speaking, this
	   is nothing to do with Polargraph, but it's a common thing to have to do, so there are some built-in features to
	   help us do it.
	 */
	RPoint cartPoint = drawing.getAsCartesian(new RPoint(inputPos.x, inputPos.y));
	cartesianPos = new PVector(cartPoint.x, cartPoint.y);
	
	
	nativePos = this.machine.convertToNative(cartesianPos);
	
	// now write out the queue
	int rowHeight = 14;
	int rowNo = 0;
	textSize(rowHeight-2);
	fill(255, 255, 255, 255);
	PVector textPos = new PVector((float)this.machinePanel.getMaxX()+20, (float)this.machinePanel.getMinY()+rowHeight);
	
	for (QueuedCommand c : this.queue.iterator()) {
		text("Command: " + c, textPos.x, textPos.y + (rowHeight * rowNo++));
	}

}

	
}
