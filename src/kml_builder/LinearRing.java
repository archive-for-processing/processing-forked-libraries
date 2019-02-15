package kml_builder;

import java.util.ArrayList;

import processing.data.*;

/**
 * Linear Ring class Used to create an XML node with Linear Ring formatting
 * Additionally contains helper Functions to ensure Linear Ring is formatted
 * correctly
 *
 * @author  Liam James (liam@minimaximize.com)
 * @version 0.2
 * @see     Geometry
 * @since   2019-14-02
 */
public class LinearRing extends Geometry<LinearRing> {
  protected boolean                extrude    = false;
  protected boolean                tessellate = false;
  protected AltitudeMode           altitudeMode;
  protected ArrayList<Coordinates> coordinates;

  /**
   * Default constructor for Linear ring. Defines a closed line string, typically
   * the outer boundary of a Polygon. Optionally, a LinearRing can also be used as
   * the inner boundary of a Polygon to create holes in the Polygon. A Polygon can
   * contain multiple <\LinearRing>\ elements used as inner boundaries.
   * 
   * This requires a minimum of 3 coordinates, with one coordinate representing
   * the start and end of the polygon
   * 
   * @param c0 Coordinate representing the first and last point in the ring
   * @param c1 Second coordinate in the ring
   * @param c2 Third coordinate in the ring
   * @param cn Any additional coordinates in the ring
   */
  public LinearRing(Coordinates c0, Coordinates c1, Coordinates c2, Coordinates... cn) {
    super("LinearRing");
    coordinates = new ArrayList<Coordinates>();
    coordinates.add(c0);
    coordinates.add(c1);
    coordinates.add(c2);
    for (Coordinates c : cn) { coordinates.add(c); }
    coordinates.add(c0);
  }

  /**
   * sets the Extrude status for a given Line Ring.
   *
   * @param  status the Extrude status for the Line Ring
   * @return        this object
   */
  public LinearRing setExtrude(Boolean status) {

    return this;
  }

  /**
   * sets the Altitude Mode status for a given Line Ring.
   *
   * @param  altitudeMode altitude Mode for this Line Ring.
   * @return              this object
   */
  public LinearRing setAltitudeMode(AltitudeMode altitudeMode) {
    this.altitudeMode = altitudeMode;

    return this;
  }

  @Override
  protected XML build(XML base) {

    base = addAttribute(base, "extrude", extrude ? "1" : "0");
    base = addAttribute(base, "tesselate", tessellate ? "1" : "0");
    base = addChildElement(base, altitudeMode);

    String cString = "";
    for (Coordinates c : coordinates) {
      cString += c.toString() + "\n";
    }
    base = addAttribute(base, "coordinates", cString);

    // TODO Auto-generated method stub
    return null;
  }
}