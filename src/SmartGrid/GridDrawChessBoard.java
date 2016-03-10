package SmartGrid;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class GridDrawChessBoard extends GridDrawBase
{
  int[] colors;
  boolean showPosition;
  public GridDrawChessBoard()
  {
    colors=new int[]{0xFFE0E0E0,0xFFFFFFFF};
  }
  public void setup()
  {
  }
  public void draw()
  {
    int rows=grid.getRows();
    int cols=grid.getColumns();
    for(int i=0;i<rows;i++)
    {
      for(int j=0;j<cols;j++)
      {
        app.pushMatrix();
        int c=colors[(i+j)%colors.length];
        PVector pos=grid.getCellPosition(i,j,true);
        //println("cell pos",pos);
        PVector s=grid.getCellSize(i,j,true);
        //println("cell size",s);
        app.translate(pos.x,pos.y);
        app.fill(c);
        app.rect(0,0,s.x,s.y);
        if(showPosition)
        {
          int r = (c >> 16) & 0xFF;  // Faster way of getting red(argb)
          int g = (c >> 8) & 0xFF;   // Faster way of getting green(argb)
          int b = c & 0xFF;          // Faster way of getting blue(argb)
          r=255-r;
          g=255-g;
          b=255-b;
          c=0xFF000000 | r<<16 |g<<8|b; //reverse the color
          app.fill(c);
          app.textAlign(PConstants.CENTER,PConstants.CENTER);
          app.text(PApplet.str(i)+","+PApplet.str(j),s.x/2,s.y/2);
        }
        app.popMatrix();
      }
    }
  }
}
