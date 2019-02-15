package kml_builder;

import processing.data.*;

/**
 * Polygon class
 * Used to create an XML node with Point formatting
 * Additionally contains helper Functions to ensure Point is formatted correctly
 *
 * @author       Liam James (liam@minimaximize.com)
 * @version      0.1
 * @see          Geometry
 * @since        15-04-2018
 */
public class Polygon extends Geometry<Polygon> {

    /**
   * sets the Extrude status for a given Polygon.
   *
   * @param status the Extrude status for the Polygon
   * @return this object
   */
  public Polygon setExtrude(Boolean status) {
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
   * sets the Tessellation mode for a given Polygon Object.
   *
   * @param status the Tessellation mode for the Polygon
   * @return this object
   */
  public Polygon setTessellate(Boolean status) {
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
   * sets the Altitude Mode status for a given Polygon Object.
   *
   * @param altitudeMode altitude Mode for this Polygon.
   * @return this object
   */
  public Polygon setAltitudeMode(String altitudeMode) {
    XML Mode;
    // Check if Location tags exist
    Mode = getValidChild(this.cursor, ALTITUDE_MODE);

    Mode.setContent(altitudeMode);
    return this;
  }

  /**
   * Adds an outerBoundary and Linear String containing a coordinates string
   *
   * @param coords tuple array of Coordinates containing Longitude, Latitude, [altitude]
   * @return this object
   */
  public Polygon addOuterBoundary(Coordinates[] coords) {
    XML Home = this.cursor;

    // Create Linear Ring Object
    this.cursor = getValidChild(this.cursor, OUTER_BOUNDARY_IS);

    // set the linear ring
    LinearRing outerBoundary = new LinearRing(this);
    outerBoundary.setPointCoords(coords);

    // Return Cursor to home
    this.cursor = Home;

    return this;
  }
  
  /**
   * Adds an innerBoundary and Linear String containing a coordinates string
   *
   * @param coords tuple array of Coordinates containing Longitude, Latitude, [altitude]
   * @return this object
   */
  public Polygon setInnerBoundary(Coordinates[] coords) {
    XML Home = this.cursor;

    // Create Linear Ring Object
    this.cursor = getValidChild(this.cursor, INNER_BOUNDARY_IS);
    
    // set the linear ring
    LinearRing innerBoundary = new LinearRing(this);
    innerBoundary.setPointCoords(coords);

    // Return Cursor to home
    this.cursor = Home;

    return this;
  }

  /**
   * Basic Document Constructor to be used by other class objects
   * Initialises internal XML objects
   *
   * @param parent parent KML Object
   */
  public Polygon(KMLRoot Parent) {
    super(Parent, POLYGON);
  }
}