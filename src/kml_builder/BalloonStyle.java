package kml_builder;

import processing.data.*;

/**
 * BalloonStyle class
 * Used to create an XML node with Placemark formatting
 * Additionally contains Functions exclusive to Placemark
 *
 * @author       Liam James (liam@minimaximize.com)
 * @version      0.1
 * @see          Feature
 * @since        15-04-2018
 */
public class BalloonStyle extends KMLRoot {

  /**
   * Adds setBG Colour for BalloonStyle object to the Structure
   *
   * @param aabbggrr bg colour in aabbggrr format
   * @return this BalloonStyle object
   */
  public BalloonStyle setBgColor(String aabbggrr) {
    XML field;
    // Check if Location tags exist
    field = getValidChild(this.cursor, BGCOLOR);

    field.setContent(aabbggrr);
    return this;
  }
  
  /**
   * Adds tect Colour for BalloonStyle object to the Structure
   *
   * @param aabbggrr text colour in aabbggrr format
   * @return this BalloonStyle object
   */
  public BalloonStyle setATextColor(String aabbggrr) {
    XML field;
    // Check if Location tags exist
    field = getValidChild(this.cursor, TEXTCOLOR);

    field.setContent(aabbggrr);
    return this;
  }
  
    /**
   * Adds Text for BalloonStyle object to the Structure
   *
   * @param text text for the Baloon Style Object
   * @return this BalloonStyle object
   */
  public BalloonStyle setText(String text) {
    XML field;
    // Check if Location tags exist
    field = getValidChild(this.cursor, TEXT);

    field.setContent(text);
    return this;
  }
  
    /**
   * Adds setBG Colour for BalloonStyle object to the Structure
   *
   * @param displayMode KML Display Mode ENUM
   * @return this BalloonStyle object
   */
  public BalloonStyle setdDisplayMode(String displayMode) {
    XML field;
    // Check if Location tags exist
    field = getValidChild(this.cursor, DISPLAY_MODE);

    field.setContent(displayMode);
    return this;
  }

  /**
   * Basic BalloonStyle Constructor to be used by other class objects
   * Initialises internal XML objects
   *
   * @param parent parent KML Object
   */
  protected BalloonStyle(KMLRoot<?> parent) {
    super(parent, BALLOONSTYLE);
  }
}