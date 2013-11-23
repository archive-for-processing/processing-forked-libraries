package polargraph.test;

import processing.core.PApplet;

import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

import polargraph.Polargraph;
import polargraph.PolargraphDrawing;
import polargraph.comms.Command;
import polargraph.comms.CommandFactory;
import polargraph.queue.QueueWriter;
import polargraph.queue.VirtualComQueueWriter;

import java.awt.geom.Rectangle2D;

import geomerative.*;

public class TestApplet extends PApplet {
		
	private RShape rshape;
	private PolargraphDrawing drawing;
	private QueueWriter queue;
	
	public void setup() {
		println("Running!");
//		frame.setResizable(true);
		size(1000, 600, JAVA2D);
		
		RG.init(this);
		RG.ignoreStyles();
		RG.setPolygonizer(RG.ADAPTATIVE);
		
		rshape = new RShape();
		rshape.addMoveTo(100, 50);
		rshape.addLineTo(300, 50);
		rshape.addLineTo(300, 150);
		rshape.addLineTo(100, 150);
		rshape.addLineTo(100, 50);
		
		Polargraph machine = new Polargraph(700, 500, new Rectangle2D.Float(0,0,350,500), 1.0D);
		drawing = machine.createNewDrawing("main", new Rectangle2D.Float(100,100,100,100));
		queue = new VirtualComQueueWriter("COM 14");
	
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
		
		while (this.queue.hasNext()) {
			println(this.queue.next());
		}
	}
	
	public void draw() {
		fill(255,100,100);
//		this.rshape.draw();
		
		noFill();
		stroke(255,255,0);
		for (RPoint p : this.rshape.getPoints()) {
		  ellipse(p.x, p.y, 10, 10);
		}
		
	}
}
