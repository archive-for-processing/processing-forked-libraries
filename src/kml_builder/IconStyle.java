package kml_builder;

import processing.data.*;

/**
 * <h1>IconStyle Class</h1>
 * Used to control the style of IconStyle in a KML file 
 * 
 * @author       Liam James (liam@minimaximize.com)
 * @version      0.1
 * @see          ColorStyle
 * @since        20-04-2018
 * */
public class IconStyle extends ColorStyle<IconStyle> {

  /**
   * Adds Scale object to the Structure
   *
   * @param scale float scale of the Icon Style
   * @return this object
   */
  public IconStyle setScale(Float scale) {
    XML field;
    // Check if Scale tags exist
    field = getValidChild(this.cursor, "scale");

    // Set Orientation Content
    field.setContent(Float.toString(scale));   

    return this;
  }

  /**
   * Direction (that is, North, South, East, West), in degrees. Default=0 (North).
   * Values range from 0 to 360 degrees.
   *
   * @param heading Direction in degrees of this Icon Object
   * @return this object
   */
  public IconStyle setHeading(Float heading) {
    XML field;
    // Check if Scale tags exist
    field = getValidChild(this.cursor, "heading");

    // Set Orientation Content
    field.setContent(Float.toString(heading));   

    return this;
  }

  /**
   * A custom Icon. In IconStyle, the only child element of Icon is href
   *
   * @param Path file path to Icon image
   * @return this object
   */
  public IconStyle setIcon(String Path) {
    XML Home = this.cursor;

    // Check if Scale tags exist
    this.cursor = getValidChild(this.cursor, "Icon");
    this.cursor = getValidChild(this.cursor, "href");
    // Set Orientation Content
    this.cursor.setContent(Path);   
    this.cursor = Home;

    return this;
  }
  
  
  /**
   * adds and sets the HotSpot tags for the IconStyle element
   * </p>
   * Specifies the position within the Icon that is "anchored" to the Point specified in the Placemark. 
   * The x and y values can be specified in three different ways: as pixels ("pixels"), as fractions of the icon ("fraction"), or as inset pixels ("insetPixels"), which is an offset in pixels from the upper right corner of the icon. 
   * The x and y positions can be specified in different ways—for example, x can be in pixels and y can be a fraction. 
   * The origin of the coordinate system is in the lower left corner of the icon.
   * </p>
   * @param x Either the number of pixels, a fractional component of the icon, or a pixel inset indicating the x component of a point on the icon.
   * @param y Either the number of pixels, a fractional component of the icon, or a pixel inset indicating the y component of a point on the icon.
   * @param xunits Units in which the x value is specified. A value of fraction indicates the x value is a fraction of the icon. A value of pixels indicates the x value in pixels. 
   * A value of insetPixels indicates the indent from the right edge of the icon.
   * @param yunits Units in which the y value is specified. A value of fraction indicates the y value is a fraction of the icon. A value of pixels indicates the y value in pixels. 
   * A value of insetPixels indicates the indent from the top edge of the icon.
   * @return this object
   */
  public IconStyle setHotSpot(double x, double y, String xunits, String yunits) {
	  
	  
	  return this;
  }
  

  /**
   * Basic Constructor for the IconStyle class
   * </p>
   * <b>IconStyle</b> Specifies how icons for point Placemarks are drawn, both in the Places panel and in the 3D viewer of Google Earth.
   *  The Icon element specifies the icon image. The scale element specifies the x, y scaling of the icon.
   * The color specified in the color element of IconStyle is blended with the color of the Icon.
   *
   * @param parent parent KML Object
   */
  protected IconStyle(KMLRoot parent) {
    super(parent, BALLOONSTYLE);
  }
}