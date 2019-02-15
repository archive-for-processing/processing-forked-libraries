package kml_builder;
import processing.data.XML;

interface Buildable {
  
  /**
   * Creates XML representing this object and returns it
   * 
   * @return This object as XML
   */
  public abstract XML build();
}
