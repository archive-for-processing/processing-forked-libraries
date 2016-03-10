package SmartGrid;

import java.util.ArrayList;

import processing.core.PApplet;

public class SmartGridManager
{
  PApplet app;
  ArrayList<SmartGrid> grids;
  Object[] dataSlot;
  public SmartGridManager(PApplet parent)
  {
    app=parent;
    grids=new ArrayList<SmartGrid>();
  }
  
  
  
  public SmartGrid CreateGrid()
  {
    return CreateGrid("*,*,*,*,*,*,*,*,*,*,*,*","*,*,*,*,*,*,*,*,*,*,*,*");//default root grid with 12*12 cells
  }
  
  public SmartGrid CreateGrid(String columns,String rows)
  {
    //String name="grid_"+str(random(1000));
    return CreateGrid(columns,rows,true);
  }
  
  public SmartGrid CreateGrid(SmartGrid parent,int col, int row, int colSpan,int rowSpan)
  {
    SmartGrid result=CreateGrid("*","*");
    result.setParent(parent,col,row,colSpan,rowSpan);
    return result;
  }
  
  public SmartGrid CreateGrid(String columns,String rows,boolean cbShowPosition,int...cbColors)//core method
  {
    SmartGrid result=new SmartGrid(app);
    //result.name=name;
    result.columnDefinitions.parse(columns);
    result.rowDefinitions.parse(rows);
    GridDrawChessBoard drawObj=new GridDrawChessBoard();
    drawObj.showPosition=cbShowPosition;
    result.setDrawingObject(drawObj);
    if (cbColors.length>0)//set custom color
    {
      drawObj.colors=cbColors;
    }
    grids.add(result);
    return result;
  }
  void setup()
  {
    for(SmartGrid item:grids)
      {
        item.drawingObject.setup();
      }
  }
  
  public void draw(boolean enableResizeDetect)
  {
    if(enableResizeDetect)
    {
      for(SmartGrid item:grids)
      {
        if(item.parent==null)
        {
          item.updateLayout(true);
        }
      }
    }
    for(SmartGrid item:grids)
    {
      if(item.drawEnabled && item.drawingObject.enableBeforeDraw)
      {
        item.project();
        item.drawingObject.beforeDraw();
        item.unProject();
      }
    }
    for(SmartGrid item:grids)
    {
      if(item.drawEnabled)
      {
        item.project();
        item.drawingObject.draw();
        item.unProject();
      }      
    }
    for(SmartGrid item:grids)
    {
      if(item.drawEnabled && item.drawingObject.enableAfterDraw)
      {
        item.project();
        item.drawingObject.afterDraw();
        item.unProject();
      }
    }
  }
  
  public void keyPressed()
  {
    for(SmartGrid item:grids)
    {
      item.drawingObject.keyPressed();
    }
  }
  

  
}