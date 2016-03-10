package SmartGrid;

import java.util.ArrayList;

import processing.core.*;

class SmartGrid
{
  SmartGrid parent;
  private ArrayList<SmartGrid> children;
  public boolean isDirty=true;
  private PVector size=new PVector(0,0);
  private PVector position=new PVector(0,0);
  private int pCol,pRow,pColSpan,pRowSpan;
  public GapDefinitions columnDefinitions;
  public GapDefinitions rowDefinitions;
  private PVector[][] cellPosition;
  private PVector[][] cellSize;
  private PVector projectScale;
  private int previousWidth,previousHeight;
  private int previousMouseX,previousMouseY;
  public boolean mouseProjectionInboundCheck=false;
  public boolean mouseProjectionSync=false;
  public PVector projectToRect;
  public boolean drawEnabled=true;
  //public String name;
  GridDrawBase drawingObject;
  private PApplet app;
  
  public SmartGrid(PApplet applet)
  {
    app=applet;
    columnDefinitions=new GapDefinitions();
    rowDefinitions=new GapDefinitions();
    children =new ArrayList<SmartGrid>();
    projectScale=new PVector();
  }
  
  public void setDrawingObject(GridDrawBase draw)
  {
    if (drawingObject!=null)
    {
      drawingObject.detach();
    }
    draw.attach(this,app);
    drawingObject=draw;
    
  }
  
  public void setParent(SmartGrid p,int column, int row, int columnSpan, int rowSpan)
  {
    if(parent!=null)
    {
      parent.children.remove(this);
    }
    parent=p;
    parent.children.add(this);
    this.pCol=column;
    this.pRow=row;
    this.pColSpan=columnSpan;
    this.pRowSpan=rowSpan;
    isDirty=true;
  }
  
  public PVector getPosition()
  {
    updateLayout();
    return position;
  }
  
  public PVector getCellPosition(int col,int row,boolean isRelative)
  {
    updateLayout();
    PVector result=cellPosition[row][col].copy();
    if(isRelative)
    {
      result.set(result.x*projectScale.x,result.y*projectScale.y);
    }
    else
    {
      result.add(position);
    }
    return result;
  }
  
  public PVector getCellSize(int col, int row,boolean isRelative)
  {
    updateLayout();
    PVector result=cellSize[row][col].copy();
    if(isRelative)
    {
      result.set(result.x*projectScale.x,result.y*projectScale.y);
    }
    return result;
  }
  
  public int getColumns()
  {
    updateLayout();
    return columnDefinitions.size();
  }
  
  public int getRows()
  {
    updateLayout();
    return rowDefinitions.size();
  }
  
  public void updateLayout()
  {
    updateLayout(false);
  }
  
  public void updateLayout(boolean forceUpdate)
  {
    if (isDirty||forceUpdate)
    {
      //find the target value
      refreshRegion();
      refreshCells();
      for(SmartGrid item : children)
      {
        item.isDirty=true;
      }
    }
    isDirty=false;
  }
  private void refreshRegion()
  {
    if(parent==null)//we're the root
    {
      position.x=0;
      position.y=0;
      size.x=app.width;
      size.y=app.height;
    }
    else
    {
      PVector pos1=parent.getCellPosition(pCol,pRow,false);//top left cell XY
      PVector pos2=parent.getCellPosition(pCol+pColSpan,pRow+pRowSpan,false);//bottom right cell XY
      PVector size2=parent.getCellSize(pCol+pColSpan,pRow+pRowSpan,false);//bottom right cell size
      position=pos1;
      size=pos2.add(size2).sub(pos1);
    }
  }
  private void refreshCells()
  {
    columnDefinitions.setTotal(size.x);
    rowDefinitions.setTotal(size.y);
    int rows=columnDefinitions.size();
    int cols=rowDefinitions.size();
    cellPosition=new PVector[rows][cols];
    cellSize=new PVector[rows][cols];
    float[] dataX=columnDefinitions.getData();
    float[] dataY=rowDefinitions.getData();
    for (int r=0;r<rows;r++)
    {
      for(int c=0;c<cols;c++)
      {
        cellPosition[r][c]=new PVector(dataX[r],dataY[c]);
        cellSize[r][c]=new PVector(dataX[r+1],dataY[c+1]).sub(cellPosition[r][c]);
      }
    }
  }

  public void keyPressed()
  {
    drawingObject.keyPressed();
  }
  
  public void project()
  {
    app.pushStyle();
    projectXY();
    if(projectToRect!=null)
    {
      projectScale.set(size.x/projectToRect.x,size.y/projectToRect.y);
      app.scale(projectScale.x,projectScale.y);
    }
    else
    {
      projectScale.set(1,1,0);
    }
    projectScale.set(1,1,1);
    projectMouse();
    resizeScreen();
  }
  
  private void projectXY()
  {
    saveScreenSize();
    app.pushMatrix();
    
    app.translate(getPosition().x,getPosition().y);
    
    
  }
  
  private void saveScreenSize()
  {
    previousWidth=app.width;
    previousHeight=app.height;
  }
  
  private void resizeScreen()
  {
    if(projectToRect!=null)
    {
      app.width=(int)projectToRect.x;
      app.height=(int)projectToRect.y;
    }
    else
    {
      app.width=(int)(size.x);
      app.height=(int)(size.y);   
    }
  }
  
  private void projectMouse()
  {
    previousMouseX=app.mouseX;
    previousMouseY=app.mouseY;
    PVector result=getMouseXY(mouseProjectionInboundCheck,mouseProjectionSync);
    app.mouseX=(int)(result.x);
    app.mouseY=(int)(result.y);
  }
  
  public void unProject()
  {
    app.width=previousWidth;
    app.height=previousHeight;
    app.mouseX=previousMouseX;
    app.mouseY=previousMouseY;
    app.popMatrix();   
    app.popStyle();
  }
  
  public PVector getMouseXY(boolean checkInbound,boolean sync)
  {
    PVector result=new PVector(app.mouseX,app.mouseY);
    if(checkInbound && !(result.x>=position.x && result.x<=position.x+size.x && result.y>=position.y && result.y<=position.y+size.y))
    {
      return new PVector();
    }
    else
    {
      if(sync)
      {
        result.x=app.map(result.x,0,app.width,0,size.x);
        result.y=app.map(result.y,0,app.height,0,size.y);
        result.set(result.x/projectScale.x,result.y/projectScale.y);
        return result;
      }
      else
      {
        result=result.sub(position);
        result.set(result.x/projectScale.x,result.y/projectScale.y);
        return result;
      }
    }
    

  }
  
}