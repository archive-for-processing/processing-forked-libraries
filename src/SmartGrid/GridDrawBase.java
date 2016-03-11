package SmartGrid;

import processing.core.PApplet;

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
  protected void setData(int index, Object value)
  {
    SharedData.data[index]=value;
  }
  
  protected void saveData(int index, Object value)
  {
	  SharedData.persistData[index]=value;
  }
  
  protected Object getData(int index,Object defaultValue)
  {
    if (SharedData.data[index]==null)
    {
      return defaultValue;
    }
    return SharedData.data[index];
  }
  
  protected Object loadData(int index,Object defaultValue)
  {
	  if(SharedData.persistData[index]==null)
	  {
		  return defaultValue;
	  }
	  return SharedData.persistData[index];
  }
  
  public abstract void setup();
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
  public static class SharedData
  {
    static Object[] data=new Object[100];
    static Object[] persistData=new Object[10];
    static void Clear()
    {
    	data=new Object[data.length];
    }
  }
}


