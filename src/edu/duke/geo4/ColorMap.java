package edu.duke.geo4;

import processing.core.*;

public class ColorMap {

  public enum Type {
    HOT, COOL
  }

  private static final String mapData = "colormaps.json";

//  private PApplet myParent;
  private ColorSelector mySelector;
  private float myMin = 0.0f;
  private float myMax = 1.0f;

  public ColorMap(PApplet parent, Type type) {
//    myParent = parent;
    mySelector = ColorSelector.loadColorSelector(parent, mapData, type);
  }

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

  public void setMinValue(float min) {
    myMin = min;
  }

  public void setMaxValue(float max) {
    myMax = max;
  }
}
