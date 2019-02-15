package kml_builder;

import processing.data.XML;

/**
 * <h1>Overlay Abstract Class</h1> contains the common elements for objects
 * inheriting from the Overlay class
 * 
 * @author  Liam James (liam@minimaximize.com)
 * @version 0.1
 * @see     Feature
 * @see     PhotoOverlay
 * @see     ScreenOverlay
 * @see     GroundOverlay
 * @since   20-04-2018
 */
public abstract class Overlay<T extends KMLObject> extends Feature<T> {
  protected String color     = "ffffffff";
  protected int    drawOrder = 0;
  protected String iconHref  = null;

  /**
   * Default constructor for Geometry objects
   * 
   * @param tag String representing the tag for objects extending Overlay
   */
  protected Overlay(String tag) {
    super(tag);
  }

  /**
   * Sets the color for this Overlay
   * 
   * @param  color a hexedecimal string representing the color as aabbggrr
   * @return       this object
   */
  public T setColor(String color) {
    this.color = color;

    return (T) this;
  }

  /**
   * defines the stacking order for the images in overlapping overlays. Overlays
   * with higher draw orders are drawn above smaller draw orders
   * 
   * @param  order The draw order of this object
   * @return       This Object
   */
  public T setDrawOrder(int order) {
    this.drawOrder = order;

    return (T) this;
  }

  /**
   * Defines the image associated with the Overlay
   * 
   * @param  path String representing the relative or absolute path to the overlay
   *              icon
   * @return      This Object
   */
  public T setIcon(String path) {
    iconHref = path;

    return (T) this;
  }
  
  protected XML build(XML base) {
    base = super.build(base);
    base = addAttribute(base, "color", color);
    base = addAttribute(base, "drawOrder", Integer.toString(drawOrder));
    base.addChild("Icon").addChild("href").setContent(iconHref);
    return base;
  }
}
