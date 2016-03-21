package SmartGrid;



import processing.core.PApplet;
import processing.core.PVector;

/**
 * user is able to create "owner draw" on each grid by extending 
 * this class and attach it to a grid.
 * 
 * @author qian.fan.jiang
 *
 */
public abstract class GridDrawBase
{
  protected SmartGrid grid;
  protected PApplet app;
  public boolean enableBeforeDraw=true;
  public boolean enableAfterDraw=true;
  void attach(SmartGrid g,PApplet a){
    grid=g;
    app=a;
  }
  void detach()
  {
    grid=null;
    app=null;
  }
  
  
  public abstract void setup();
  public void update(){};
  public void beforeDraw()
  {
    enableBeforeDraw=false;//shut down this feature by default
  }
  public abstract void draw();
  public void afterDraw()
  {
    enableAfterDraw=false;//shut down this feature by default
  }
  public void keyPressed()
  {
  }
  
  public void messageReceived(String title,Object message)
  {
	  
  }
  
  protected void background(int rgb)
  {
	  app.pushStyle();
	  PVector pos=grid.getPosition();
	  PVector s=grid.getSize();
	  app.fill(rgb);
	  app.rect(pos.x,pos.y,s.x,s.y);
	  app.popStyle();
  }

    
}


