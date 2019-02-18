package kml_builder;

import processing.data.XML;

public class Orientation implements Buildable{
  double heading = 0d;
  double tilt    = 0d;
  double roll    = 0d;
  
  public Orientation() {}
  
  public Orientation(double heading, double tilt, double roll) {
    this.heading = heading;
    this.tilt = tilt;
    this.roll = roll;
  }
  
  @Override
  public XML build() {
    XML out = new XML("Orientation");
    
    return out;
  }

}
