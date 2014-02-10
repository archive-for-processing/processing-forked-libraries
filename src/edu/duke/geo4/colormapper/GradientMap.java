package edu.duke.geo4.colormapper;

import processing.core.*;

/**
 * Provides a simple interface to correlate a range of continuous data to a colormap, interpolating
 * between colors when necessary to provide a smooth gradient.
 * 
 * @author Grant Oakley
 * 
 */
public class GradientMap {

  private static final String mapData = "colormaps.json";

  private final ColorSelector mySelector;
  private final ColorMap myMap;
  private float myMin = 0.0f;
  private float myMax = 1.0f;

  public GradientMap(PApplet parent, ColorMap map) {
    mySelector = ColorSelector.loadColorSelector(parent, mapData, map);
    myMap = map;
  }

  /**
   * @param value the data to correlate to a color in the loaded colormap
   * @return the corresponding color this value's relative location in the set range
   */
  public int getColor(float value) {
    if (myMin > myMax) {
      throw new RuntimeException("Min value must be less than the max value");
    }
    if (value < myMin || value > myMax) {
      throw new RuntimeException("Value to be color mapped must be in the range [" + myMin + ", "
          + myMax + "]");
    }

    value = PApplet.map(value, myMin, myMax, 0.0f, 1.0f);
    return mySelector.getColor(value);
  }

  /**
   * @param min the lowest value in the range to be mapped (0.0 by default)
   */
  public void setMinValue(float min) {
    myMin = min;
  }

  /**
   * @param max the highest value in the range to be mapped (1.0 by default)
   */
  public void setMaxValue(float max) {
    myMax = max;
  }

  @Override
  public String toString() {
    return "Colormap [\"" + myMap.name() + "\"]";
  }
}
