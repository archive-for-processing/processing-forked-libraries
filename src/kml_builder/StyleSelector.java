package kml_builder;
/**
 * Feature Abstract class
 * Used to create an XML node with Feature formatting
 * Contains common formatting used by objects that extend the Feature Class
 *
 * @author       Liam James (liam@minimaximize.com)
 * @version      0.1
 * @see          Container
 * @see          Overlay
 * @see          Placemark
 * @since        15-04-2018
 */
public abstract class StyleSelector<T extends KMLObject> extends KMLObject<T> {  
  /**
   * Basic Container Feature to interface with the KML constructor
   *
   * @param parent parent KML Object
   * @param name name of the Node type
   */
  protected StyleSelector(String tag) {
    super(tag);
  }
}