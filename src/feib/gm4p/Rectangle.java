package feib.gm4p;
import processing.core.*;

public class Rectangle {
	public PVector position, dimension;
	
	public Rectangle(){
		position = new PVector(0, 0);
		dimension = new PVector(0, 0);
		if (GM.debugMode) 
			PApplet.println("Rectangle created");
	}
	
	public Rectangle(PVector position, PVector dimension){
		this.position = position.copy();
		this.dimension = dimension.copy();
	}
	
	public float getTop(){
		return position.y;
	}
	
	public float getBottom(){
		return position.y + dimension.y;
	}
	
	public float getLeft(){
		return position.x;
		
	}
	
	public float getRight(){
		return position.x + dimension.x;
		
	}
	
	public PVector getLocalCenter(){
		return PVector.div(dimension, 2);		
	}
	
	public PVector getCenter(){
		return PVector.add(position, getLocalCenter());
	}
}
