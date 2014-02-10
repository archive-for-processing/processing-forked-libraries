package edu.duke.geo4.colormapper;


import processing.core.*;

/**
 * Provides an interface to map discrete values to colormap. The number of different colors the map
 * is divided into are referred to as steps, and can be changed after the map is initialized.
 * 
 * @author Grant Oakley
 * 
 */
public class DiscreteMap {

  private final GradientMap myMap;
  private int mySteps;

  public DiscreteMap(PApplet parent, ColorMap map, int steps) {
    myMap = new GradientMap(parent, map);
    setNumSteps(steps);
  }

  public void setNumSteps(int steps) {
    if (steps < 1) {
      throw new RuntimeException("Colormap must have at least one step");
    }
    myMap.setMaxValue(steps - 1);
    mySteps = steps;
  }

  public int getColor(int index) {
    if (index < 0 || index >= mySteps) {
      throw new RuntimeException("Index must be in the range [0," + mySteps + "]");
    }

    return myMap.getColor(index);
  }

  @Override
  public String toString() {
    return myMap.toString();
  }

}
