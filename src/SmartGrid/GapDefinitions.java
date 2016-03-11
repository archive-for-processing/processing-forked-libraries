package SmartGrid;

import java.util.ArrayList;

class GapItem
{
  boolean isDynamic;
  float value;
}
/**
 * 
 * @author qian.fan.jiang
 * defines a line with several points
 * <p>
 * this class is the code concept of smart grid system
 * to divide a line into several gaps, a dynamic length system is imported from XAML 
 * <p>
 * fixed length: a positive float represents the fixed gap size
 * <p>
 * dynamic length: a positive float ends with "*" means a dynamic length, the actual size is based on dynamic size unit.
 * <p>
 * example:
 * <p>
 * "*": a line with 1 gap, the gap size is equal to line size
 * <p>
 * "*,*,*": a line with 3 gaps, each gap is 1/3 of line size
 * <p>
 * "*,2*": a line with 2 gaps, first gap has 1/3 line size, second is 2/3 line size
 * <p>
 * "100,2*,*": a line with 3 gaps, first gap is 100 width(fixed), second is (line size-100)/3*2 ,3rd gap is (line size-100)/3*1
 * 
 */
public class GapDefinitions
{
  private float total;
  private ArrayList<GapItem> items;
  private float[] data;
  private boolean isDirty=true;
  
  public GapDefinitions()
  {
    items=new ArrayList<GapItem>();
  }
  
  public void parse(String s)
  {
    clear();
    String[] items= s.split(",");
    for(String item : items)
    {
      append(item);
    }
  }
  
  public void setTotal(float value)
  {
    if(value!=total)
    {
      total=value;
      isDirty=true;
    }
  }
  
  /**
   * get the points of the gap.<br>
   * number of points=gaps.length+1 as the first point is always 0.
   * @return float array represents the points of gaps
   */
  public float[] getData()
  {
    refresh();
    return data;
  }
  
  public int size()
  {
    return items.size();
  }
  
  public void set(int index,GapItem item)
  {
    items.set(index,item);
    isDirty=true;
  }
  
  public void append(String item)
  {
    item=item.trim();
    GapItem o=new GapItem();
    if(item.endsWith("*"))
    {
      if(item.equals("*"))//shall we handle the empty string? not yet :)
      {
        item="1*";
      }
      o.isDynamic=true;
      item=item.substring(0,item.length()-1);
    }
    o.value= Float.parseFloat(item);
    items.add(o);
    
    
    isDirty=true;
  }
  public void remove(int index)
  {
    items.remove(index);
    isDirty=true;
  }
  
  public void clear()
  {
    items.clear();
    isDirty=true;
  }
  
  private void refresh()
  {
    if(isDirty)
    {
      float fixedLength=0;
      float dynamicTotal=0;
      data=new float[items.size()+1];
      for(GapItem item : items)//calculate totals
      {
        if(item.isDynamic)
        {
          dynamicTotal+=item.value;
        }
        else
        {
          fixedLength+=item.value;
        }
      }
      float dynamicUnit=0;
      if (dynamicTotal!=0 && fixedLength<total)
        {
          dynamicUnit=(total-fixedLength)/dynamicTotal;
        }
        
      float current=0;
      data[0]=0;
      int i=1;
      for(GapItem item : items)//calculate real values
      {
        if(item.isDynamic)
        {
          current+=item.value*dynamicUnit;
        }
        else
        {
          current+=item.value;
        }
        data[i++]=current;
      }
      isDirty=false;
    }

    
  }
}
