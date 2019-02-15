package kml_builder;

import processing.data.*;
/**
 * Line String class
 * Used to create an XML node with Line String formatting
 * Additionally contains helper Functions to ensure Line String is formatted correctly
 *
 * @author       Liam James (liam@minimaximize.com)
 * @version      0.1
 * @see          Geometry
 * @since        15-04-2018
 */
public class LineString extends Geometry<LineString> {

  /**
   * sets the Extrude status for a given Line String.
   *
   * @param status the Extrude status for the Line String
   * @return this object
   */
  public LineString setExtrude(Boolean status) {
    XML stat;
    // Check if Scale tags exist
    stat = getValidChild(this.cursor, EXTRUDE);

    // Set Orientation Content
    stat.setContent(
      (status? "1":"0")
      );   

    return this;
  }
  
  /**
   * sets the Tessellation mode for a given Line String.
   *
   * @param status the Tessellation mode for the Line String
   * @return this object
   */
  public LineString setTessellate(Boolean status) {
    XML stat;
    // Check if Scale tags exist
    stat = getValidChild(this.cursor, TESSELLATE);

    // Set Orientation Content
    stat.setContent(
      (status? "1":"0")
      );   

    return this;
  }

  /**
   * sets the Altitude Mode status for a given Line String.
   *
   * @param altitudeMode altitude Mode for this Line String.
   * @return this object
   */
  public LineString setAltitudeMode(String altitudeMode) {
    XML Mode;
    // Check if Location tags exist
    Mode = getValidChild(this.cursor, ALTITUDE_MODE);

    Mode.setContent(altitudeMode);
    return this;
  }

  /**
   * Adds a coordinate string to this Line String object
   *
   * @param coordTuples tuple array of Line String Objects of Longitude, Latitude, [altitude]
   * @return this object
   */
  public LineString setPointCoords(Coordinates[] coordTuples) {
    XML coords;

    // Check if Location tags exist
    coords = getValidChild(this.cursor, COORDINATES);

    String coordString = "";
    // Set Location Content
    for (Coordinates tuple : coordTuples) {
      coordString += tuple.toString() + " ";
    }
    coords.setContent(coordString);

    return this;
  }

  /**
   * Basic Document Constructor to be used by other class objects
   * Initialises internal XML objects
   *
   * @param parent parent KML Object
   */
  public LineString(KMLRoot Parent) {
    super(Parent, LINESTRING);
  }
}