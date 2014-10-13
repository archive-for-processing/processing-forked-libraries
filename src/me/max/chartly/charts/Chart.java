package me.max.chartly.charts;

import me.max.chartly.components.color.ColorScheme;
import me.max.chartly.components.data.DataSet;

public interface Chart {

	public void draw(float x, float y);
	public void refresh();
	
	public Chart setData(DataSet data);
	public DataSet getData();
	
	public ColorScheme getColorScheme();
	public Chart setColorScheme(ColorScheme scheme);
	
}
