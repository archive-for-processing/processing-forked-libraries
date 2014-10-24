## Chartly

A Processing Chart-Building Library.

### Simple. Powerful. Dynamic. Customizable. 
Chartly was built with the intention to allow users to display data quickly, dynamicly, and simply, without having to worry about complex data storage, sorting, and shape-rendering.

It is still in a heavy stage of early-developement.

### About
I started developement on Chartly as a School Project for Coding as Art
It is forked from the Processing Library template.
Submitted: October 24, 2014

Chartly was designed to allow users to quickly represent 
their data in a way that is both easy to understand
and largely customizable.

Data given is transformed into customizable charts
Each Chart type has its description of how it is drawn.

The library, while far from being finished, as I plan to continue working on it, already encompasses most if not allof the features I intended to include. In the future I might attempt to add more chart types.

Through documenation, refactoring, and examples I tried to make the 
charts easier to use. I plan on finding ways in the future  to further reduce the learning curve.

For charts with axes, ```x``` and ```y``` refer to the bottom-left corner of the
chart. For piecharts, it is the center.

All labeling and titles are automatically sized based on the
Chart's size.

### Usage (This will be moved to the github page in the future

#####Step 1: Initialize the library

```
Chartly chartly = new Chartly(this);
```

If you do not need to store the Chartly object, you can simply write

```
new Chartly(this);
```

#####Step 2: Data
Chartly does not provide you with ways of gathering data at the moment. In order to use the library, you must have data that can be expressed in the form of ```Label:Value```. Once you have that, you can create a new DataSet
Object to store you data in.

```
DataSet data = new DataSet();
data.setData(labels[], values[]);
```

or

```
DataSet data = new DataSet(labels[], values[]);
```

#####Step 3: Looks (Optional)
If you do not care about how your chart looks, you may simply
allow the program to use its defaults.

Otherwise, see Looks's documentation.

#####Step 4: Choose a chart type
Once you have chosen between the current chart types availible
- BarChart
- LineChart
- PieChart
You can use the DataSet's to Chart methods to get a new Chart Object

```
BarChart barchart = data.toBarChart(width, height);
LineChart linechart = data.toLineChart(width, height);
PieChart piechart = data.toPieChart(radius);
```

Alternatively, you can use the constructor and add the data later.
Example:
```
BarChart barchart = new BarChart(x, y); // Works with all charts, not just BarCharts
bar.setData(data);
```

#####Step 5: Draw
In order to draw your chart, use:
```yourChart.draw(x, y)```
For charts with axes, ```x``` and ```y``` refer to the bottom-left corner of the chart. For piecharts, it is the center. 
All labeling and titles are automatically sized based on the Chart's size.
 
