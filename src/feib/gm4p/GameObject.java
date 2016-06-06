package feib.gm4p;
import processing.core.*;

public class GameObject {	
	public PVector position, velocity;
	
	public GameObject(){
		position = new PVector(0,0);
		velocity = new PVector(0,0);
		
		PApplet.println("GameObject created:" + this.toString());
	}
	
	public void integrate(int elapsedTimeMs){
		position = PVector.add(position, PVector.div(velocity, elapsedTimeMs));
	}
	
	public void update(int elapsedTimeMs){}
	public void draw(PGraphics ctx){}
	public void reset(){}
	public void handleInput(InputHelper inputHelper) {}	
}
