package kml_builder;

import processing.data.*;

/**
 * Color Style abstract class
 * Used to create an XML node with Geometry formatting
 * Contains common formatting used by objects that extend the Color Style Class
 *
 * @author       Liam James (liam@minimaximize.com)
 * @version      0.1
 * @see          IconStyle
 * @see          LabelStyle
 * @see          LineStyle
 * @see          PolyStyle
 * @since        15-04-2018
 */
public abstract class ColorStyle<T extends KMLRoot> extends KMLRoot<T> {

  /**
   * Basic Geometry Constructor to interface with the KML constructor
   *
   * @param parent parent KML Object
   * @param name name of the Node type
   */
  protected ColorStyle(KMLRoot parent, String name) {
    super(parent, name);
  }

  /**
   * Adds Color object to the Structure
   *
   * @param aabbggrr colour string for the object colour in an aabbggrr format
   * @return Object extended by ColorStyle
   */
  public T setColor(String aabbggrr) {
    XML field;
    // Check if Scale tags exist
    field = getValidChild(this.cursor, COLOR);

    // Set Orientation Content
    field.setContent(aabbggrr);   

    return (T)this;
  }

  /**
   * Adds ColorMode object to the Structure
   *
   * @param colorMode of the object
   * @return Object extended by ColorStyle
   */
  public T setColorMode(String colorMode) {
    XML field;
    // Check if Scale tags exist
    field = getValidChild(this.cursor, COLORMODE);

    // Set Orientation Content
    field.setContent(colorMode);   

    return (T)this;
  }
}