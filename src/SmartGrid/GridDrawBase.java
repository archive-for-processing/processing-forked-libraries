package SmartGrid;

import processing.core.PApplet;

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
  
  protected Object getData(int index,Object defaultValue)
  {
    if (SharedData.data[index]==null)
    {
      return defaultValue;
    }
    return SharedData.data[index];
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
  }
}


