import me.max.chartly.*;
import me.max.chartly.exceptions.*;
import me.max.chartly.charts.*;
import me.max.chartly.components.data.*;
import me.max.chartly.components.color.*;

import javax.swing.JOptionPane; 

/**
 * This is an example sketch showing Chartly's charts.
 * @author Max Johnson
 */

// Declare the chart objects
LineChart line;
BarChart bar;
PieChart pie;
DataSet data;

void setup() {
  size(800,400);
  frameRate(1);
  
  // Initialize the library
  Chartly chartly = new Chartly(this);
  
  // Create a new dataset, then randomize the contents
  data = new DataSet();
  randomize();
  
  // Create the colorscheme
  Looks scheme = new Looks().setAxisColor(100).setChartColors(#AADBFF, #F1AAFF, #FFBBAA);
  
  // Define the charts from the data.
  line = data.toLineChart(200, 100).setYLabels(100,10).setTitles("Labels", "Units", "A Graph - By Max Johnson").setLooks(scheme);
  bar = data.toBarChart(200, 100).setYLabels(100,10).setTitles("Labels", "Units", "A Graph - By Max Johnson").setLooks(scheme);
  pie = data.toPieChart(100).setLooks(scheme);
  
  // Draw them for the first time, defining x and y coords.
  line.draw(70, 200);
  bar.draw(width-230,200);
  pie.draw(width/2, height - 120);
}

void draw() {
  // Cover up old charts
  background(255);
    
  //Redraws using the previous x and y
  line.refresh();
  bar.refresh();
  pie.refresh();
    
  // Re-randomize the data
  randomize();
}

void randomize() {
  // Make sure they add up to 100, just as an example.
  float a = random(100);
  float b = random(100-a);
  float c = 100 - (a+b);
  
  // Set the data. It is synchronized across the charts, so this will change
  // What is stored in them.
  data.setData(new String[]{"A", "B", "C"}, new Float[]{a, b, c}); 
}


