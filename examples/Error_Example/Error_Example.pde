import me.max.chartly.*;
import me.max.chartly.exceptions.*;
import me.max.chartly.charts.*;
import me.max.chartly.components.data.*;
import me.max.chartly.components.color.*;

void setup() {
  // Calls a KeyValueAmountException
  DataSet data = new DataSet(new String[]{"a"}, new float[]{1,2});
  
  // Will fix the above error
  data = new DataSet(new String[]{"a","b"}, new float[]{1,2});

  // Calls an InputException due to Chartly being uninitialized
  BarChart incomplete = new BarChart(100,100);
  
  // Will correct the above error
  Chartly chartly = new Chartly(this);
  
  // Will call a no data provided InputException
  incomplete = new BarChart(100,100);
  incomplete.draw(100, 100);
  
  // Will fix the above error
  incomplete.setData(data);
  
  // Will call a no labels provided InputException
  incomplete.draw(100,100);
  
  // Will fix the above issue
  incomplete.setYLabels(10,1);
}
