package kml_builder;

import processing.data.*;
/**
 * Style class
 * Used to create an XML node with Style formatting
 * Additionally contains helper Functions to ensure Style is formatted correctly
 *
 * @author       Liam James (liam@minimaximize.com)
 * @version      0.1
 * @see          StyleSelector
 * @since        15-04-2018
 */
public class Style extends StyleSelector<Style> {

  /**
   * Basic Style Constructor to be used by other class objects
   * Initialises internal XML objects
   *
   * @param parent parent KML Object
   */
  protected Style(KMLRoot parent) {
    super(parent, STYLE);
  }
  
  public Style QuickListStyle(String bgC, String ListItemTypeEnum) {
	  XML LS = this.cursor.addChild("ListStyle");
	  LS.addChild("bgColor").setContent(bgC);
	  LS.addChild("listItemType").setContent(ListItemTypeEnum);
	  return this;
  }
  
}