package kml_builder;

import processing.data.XML;

public class LookAt extends AbstractView<LookAt> {
  private Location     location     = new Location();
  private Orientation  orientation  = new Orientation();
  private Double       range        = null;
  private AltitudeMode altitudeMode = AltitudeMode.CLAMP_TO_GROUND;

  protected LookAt() {
    super("LookAt");
  }

  /**
   * Sets the Location Coordinates of this View
   * 
   * @param coords Coordinates representing the location of this object
   * @return This LookAt object, with the new location specified by coords
   * 
   * @since 2018-04-15
   * @depreciated use @link{#setLocation(Location location)}
   */
  public LookAt setLocation(Coordinates coords) {
    this.location = (Location)coords; 

    return this;
  }

  /**
   * sets the orientation information for a given Camera.
   *
   * @param  heading rotation of the model object on the z (up) axis
   * @param  tilt    rotation of the model object on the x(toward) axis
   * @param  roll    Orientation of the model object on the y(right) axis
   * @return         parent model
   * 
   * @since 2018-04-15
   */
  public LookAt setOrientation(float heading, float tilt, float roll) {
    this.orientation = new Orientation(heading, tilt, roll);

    return this;
  }

  public LookAt setAltitudeMode(String mode) {
    XML cursor;

    cursor = getValidChild(this.cursor, "altitudeMode");
    cursor.setContent(mode);

    return this;
  }

  public LookAt setgxAltitudeMode(String gxmode) {
    XML cursor;

    cursor = getValidChild(this.cursor, "gx:altitudeMode");
    cursor.setContent(gxmode);

    return this;
  }
}
