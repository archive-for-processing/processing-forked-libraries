package kml_builder;

import processing.data.*;

/**
 * Point class
 * Used to create an XML node with Point formatting
 * Additionally contains helper Functions to ensure Point is formatted correctly
 *
 * @author       Liam James (liam@minimaximize.com)
 * @version      0.1
 * @see          Geometry
 * @since        15-04-2018
 */
public class Point extends Geometry<Point> {

  /**
   * sets the Extrude status for a given Point.
   *
   * @param status the Extrude status for the point
   * @return this object
   */
  public Point setExtrude(Boolean status) {
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
   * sets the Altitude Mode status for a given Point.
   *
   * @param altitudeMode altitude Mode for this point
   * @return this object
   */
  public Point setAltitudeMode(String altitudeMode) {
    XML Mode;
    // Check if Location tags exist
    Mode = getValidChild(this.cursor, ALTITUDE_MODE);

    Mode.setContent(altitudeMode);
    return this;
  }

  /**
   * sets the point coordinates string for this Point.
   *
   * @param coordTuples tuple array of Coordinate Objects of Longitude, Latitude, [altitude]
   * @return this object
   */
  public Point setPointCoords(Coordinates[] coordTuples) {
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
  public Point(KMLRoot parent) {
    super(parent, POINT);
  }
}