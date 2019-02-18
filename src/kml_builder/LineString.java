package kml_builder;

import java.util.ArrayList;

import processing.data.*;

/**
 * Defines a connected set of line segments. Use <\LineStyle>\ to specify the
 * color, color mode, and width of the line. When a LineString is extruded, the
 * line is extended to the ground, forming a polygon that looks somewhat like a
 * wall or fence. For extruded LineStrings, the line itself uses the current
 * LineStyle, and the extrusion uses the current PolyStyle. See the KML Tutorial
 * for examples of LineStrings (or paths).
 *
 * @author  Liam James (liam@minimaximize.com)
 * @version 0.1
 * @see     Geometry
 * @since   15-04-2018
 */
public class LineString extends Geometry<LineString> {
  private boolean                extrude    = false;
  private boolean                tessellate = false;
  private AltitudeMode           altitudeMode;
  private ArrayList<Coordinates> coordinates;

  /**
   * Basic Document Constructor to be used by other class objects Initializes
   * internal XML objects
   *
   * @param parent parent KML Object
   */
  protected LineString(Coordinates c0, Coordinates c1, Coordinates... cn) {
    super("LineString");
    coordinates = new ArrayList<Coordinates>();

    coordinates.add(c0);
    coordinates.add(c1);
    for (Coordinates c : cn) {
      coordinates.add(c);
    }
  }

  /**
   * sets the Extrude status for a given Line String.
   * 
   * Specifies whether to connect the LineString to the ground. To extrude a
   * LineString, the altitude mode must be either relativeToGround,
   * relativeToSeaFloor, or absolute. The vertices in the LineString are extruded
   * toward the center of the Earth's sphere.
   *
   * @param  status boolean Specifies whether to connect the LineString to the
   *                ground
   * @return        this object
   */
  public LineString setExtrude(boolean status) {
    this.extrude = status;

    return this;
  }

  /**
   * sets the Tessellation mode for a given Line String.
   * 
   * Specifies whether to allow the LineString to follow the terrain. To enable
   * tessellation, the altitude mode must be clampToGround or clampToSeaFloor.
   * Very large LineStrings should enable tessellation so that they follow the
   * curvature of the earth (otherwise, they may go underground and be hidden).
   *
   * @param  status boolean specifies whether to allow the LineString to follow
   *                the terrain.
   * @return        this object
   */
  public LineString setTessellate(Boolean status) {
    this.tessellate = status;

    return this;
  }

  /**
   * Specifies how altitude components in the <\coordinates\> element are
   * interpreted.
   * 
   * @see                 AltitudeMode
   *
   * @param  altitudeMode altitude Mode for this Line String.
   * @return              this object
   */
  public LineString setAltitudeMode(AltitudeMode altitudeMode) {
    this.altitudeMode = altitudeMode;

    return this;
  }

  @Override
  protected XML build(XML base) {
    base = addAttribute(base, "extrude", extrude);
    base = addAttribute(base, "tesselate", tessellate);
    base = addChildElement(base, altitudeMode);

    StringBuilder sb = new StringBuilder();
    for (Coordinates c : coordinates) {
      sb.append(c);
      sb.append(" ");
    }
    String cString = sb.toString();
    cString.trim();
    base = addAttribute(base, "coordinates", cString);
    return base;
  }
}