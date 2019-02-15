package kml_builder;

import processing.data.XML;

/**
 * <h1>GroundOverlay Class</h1> Used to control the style of groundOverlays in a
 * KML file
 * 
 * @author  Liam James (liam@minimaximize.com)
 * @version 0.1
 * @see     Overlay
 * @since   20-04-2018
 */
public class GroundOverlay extends Overlay<GroundOverlay> {
  protected double       altitude;
  protected AltitudeMode altitudeMode;
  protected LatLonBox    latLonBox;

  /**
   * Default constructor for Ground Overlays This element draws an image overlay
   * draped onto the terrain. The <\href>\ child of <\Icon>\ specifies the image
   * to be used as the overlay. This file can be either on a local file system or
   * on a web server. If this element is omitted or contains no <\href>\, a
   * rectangle is drawn using the color and LatLonBox bounds defined by the ground
   * overlay.
   */
  protected GroundOverlay() {
    super("GroundOverlay");
  }

  /**
   * SSpecifies the distance above the earth's surface, in meters, and is
   * interpreted according to the altitude mode.
   * 
   * @param  altitude The altitude of this Ground Overlay
   * @return          This object
   */
  public GroundOverlay setAltitude(double altitude) {
    this.altitude = altitude;

    return this;
  }

  /**
   * Specifies how the <altitude>is interpreted. Possible values are 
   * <ul>
   * <li> clampToGround (default) Indicates to ignore the altitude specification and drape the
   * overlay over the terrain.</li>
   * <li>absolute - Sets the altitude of the overlay
   * relative to sea level, regardless of the actual elevation of the terrain
   * beneath the element. For example, if you set the altitude of an overlay to 10
   * meters with an absolute altitude mode, the overlay will appear to be at
   * ground level if the terrain beneath is also 10 meters above sea level. If the
   * terrain is 3 meters above sea level, the overlay will appear elevated above
   * the terrain by 7 meters.</li>
   * </ul>
   * 
   * @param  mode The desired altitude mode
   * @return this object with the desired changes
   */
  public GroundOverlay setAltitudeMode(AltitudeMode mode) {
    this.altitudeMode = mode;

    return this;
  }
  
  /**
   * Sets the latLonBox for this Ground Overlay
   * @see LatLonBox
   * 
   * @param latLonBox a LatLonBox with the desired changes
   * @return This Object
   */
  public GroundOverlay setLatLonBox(LatLonBox latLonBox) {
    this.latLonBox = latLonBox;

    return this;
  }

  protected XML build(XML base) {
    base = super.build(base);

    base = addAttribute(base, "altitude", Double.toString(altitude));
    base = addChildElement(base, altitudeMode);
    base = addChildElement(base, latLonBox);

    return base;
  }
}
