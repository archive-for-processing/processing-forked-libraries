package kml_builder;

import processing.data.XML;

/**
 * <h1>Camera Class</h1> Used to control the style of Camera in a KML file
 * </p>
 * Defines the virtual camera that views the scene. This element defines the
 * position of the camera relative to the Earth's surface as well as the viewing
 * direction of the camera. The camera position is defined by longitude,
 * latitude, altitude, and either altitudeMode or gx:altitudeMode. The viewing
 * direction of the camera is defined by heading, tilt, and roll. Camera can be
 * a child element of any Feature or of NetworkLinkControl. A parent element
 * cannot contain both a Camera and a LookAt at the same time.
 * 
 * Camera provides full six-degrees-of-freedom control over the view, so you can
 * position the Camera in space and then rotate it around the X, Y, and Z axes.
 * Most importantly, you can tilt the camera view so that you're looking above
 * the horizon into the sky.
 * 
 * Camera can also contain a TimePrimitive (TimeSpan or gx:TimeStamp). Time
 * values in Camera affect historical imagery, sunlight, and the display of
 * time-stamped features. For more information, read Time with AbstractViews in
 * the Time and Animation chapter of the Developer's Guide.
 * 
 * @author  Liam James (liam@minimaximize.com)
 * @version 0.1
 * @see     AbstractView
 * @since   20-04-2018
 */
public class Camera extends AbstractView<Camera> {

  double latitude  = 0;
  double longitude = 0;
  double altitude  = 0;

  double heading = 0;
  double tilt    = 0;
  double roll    = 0;

  AltitudeMode altitudeMode = AltitudeMode.CLAMP_TO_GROUND;

  /**
   * basic constructor for the Camera to be used by other objects
   * 
   * @param parent the parent object instantiating this Camera
   */
  protected Camera() {
    super("Camera");
  }

  /**
   * Sets the Longitude, latitude and Altitude for the Camera position
   * 
   * @param  location String containing comma separated tuple containing lon, lat
   *                  [, alt] coordinates
   * @return          This object
   */
  public Camera setLocation(String location) {
    String[] coords = location.split(",");
    
    Double lon;
    Double lat;
    Double alt;
    
    try {
      lon = Double.parseDouble(coords[0]);
      lat = Double.parseDouble(coords[1]);
      alt = coords.length > 2 ? Double.parseDouble(coords[2]):0;
      
      longitude = lon;
      latitude = lat;
      altitude = alt;
      
    } catch (NumberFormatException e) {
      System.err.println("lon, lat, altitude tuple could not be converted to double");
      e.printStackTrace();
      
    }catch (Exception e) {
      e.printStackTrace();
    }

    return this;
  }

  /**
   * Sets the Heading, Tilt and Roll of the camera
   * 
   * 
   * @param orientation
   * @return
   */
  public Camera setOrientation(String orientation) {
    String[] coords = orientation.split(",");
    
    Double heading;
    Double tilt;
    Double roll;
    
    try {
      heading = Double.parseDouble(coords[0]);
      tilt = Double.parseDouble(coords[1]);
      roll = coords.length > 2 ? Double.parseDouble(coords[2]):0;
      
      this.heading = heading;
      this.tilt = tilt;
      this.roll = roll;
      
    } catch (NumberFormatException e) {
      System.err.println("heading, tilt, roll tuple could not be converted to double");
      e.printStackTrace();
      
    }catch (Exception e) {
      e.printStackTrace();
    }

    return this;
  }

  /**
   * method for setting the altitude mode for this Camera
   * </p>
   * Specifies how the altitude specified for the Camera is interpreted. Possible
   * values are as follows:
   * <ul>
   * <li>relativeToGround - (default) Interprets the altitude as a value in meters
   * above the ground. If the point is over water, the altitude will be
   * interpreted as a value in meters above sea level. See gx:altitudeMode below
   * to specify points relative to the sea floor.</li>
   * <li>clampToGround - For a camera, this setting also places the camera
   * relativeToGround, since putting the camera exactly at terrain height would
   * mean that the eye would intersect the terrain (and the view would be
   * blocked).</li>
   * <li>absolute - Interprets the altitude as a value in meters above sea
   * level.</li>
   * <ul>
   * </p>
   * 
   * @param  mode could be relativeToGround, clampToGround or absolute
   * @return      this Camera object
   */
  public Camera setAltitudeMode(AltitudeMode mode) {
    this.altitudeMode = mode;

    return this;
  }
}
