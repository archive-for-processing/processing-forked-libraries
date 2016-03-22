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
  
  
  /**
   * create a grid with 12*12 cells
   * this method is equal to CreateGrid("*,*,*,*,*,*,*,*,*,*,*,*","*,*,*,*,*,*,*,*,*,*,*,*")
   * @return new instance of SmartGrid
   */
  public SmartGrid CreateGrid()
  {
    return CreateGrid("*,*,*,*,*,*,*,*,*,*,*,*","*,*,*,*,*,*,*,*,*,*,*,*");//default root grid with 12*12 cells
  }
  
  /**
   * create a grid with certain rows and columns
   * @param columns the columns inside the grid
   * @param rows    the rows inside the grid
   * @return new instance of SmartGrid
   */
  public SmartGrid CreateGrid(String columns,String rows)
  {
    //String name="grid_"+str(random(1000));
    return CreateGrid(columns,rows,true);
  }
  
  /**
   * create a child grid belongs to an existing grid instance.
   * new grid will have 1*1 cell by default.
   * the position of this grid is judged by the cell,row parameters.
   * @param parent the parent grid instance
   * @param col the column in parent grid 
   * @param row the row in parent grid
   * @param colSpan column span in parent grid
   * @param rowSpan row span in parent grid
   * @return new instance of SmartGrid
   */
  public SmartGrid CreateGrid(SmartGrid parent,int row, int col, int rowSpan,int colSpan)
  {
    SmartGrid result=CreateGrid("*","*");
    result.setParent(parent,row,col,rowSpan,colSpan);
    return result;
  }
  /**
   * create a new grid
   * @param columns column definition
   * @param rows row definition
   * @param cbShowPosition show position while drawing, only works with default drawObject
   * @param cbColors the colors to draw the chessBoard, only works with default drawObject
   * @return new instance of SmartGrid
   */
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
  public void setup()
  {
    for(SmartGrid item:grids)
      {
        item.drawingObject.setup();
      }
  }
  
  /**
   * draw all the grid instance.
   * grids are draw by creation order, grid.drawEnabled will be checked.
   * @param enableResizeDetect check if application is resized
   */
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
      item.drawingObject.update();
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
  
  /**
   * should be called in parent application. 
   * this method will route keyPressed() event to the drawing object attached in each grids
   */
  public void keyPressed()
  {
    for(SmartGrid item:grids)
    {
      item.drawingObject.keyPressed();
    }
  }
  

  
}