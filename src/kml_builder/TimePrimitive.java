package kml_builder;

/**
 * Geometry Abstract class
 * Used to create an XML node with Geometry formatting
 * Contains common formatting used by objects that extend the Geometry Class
 *
 * @author       Liam James (liam@minimaximize.com)
 * @version      0.1
 * @see          Point
 * @see          LineString
 * @see          LinearRing
 * @see          Polygon
 * @see          MultiGeometry
 * @see          Model
 * @since        15-04-2018
 */
public abstract class TimePrimitive<T extends KMLObject> extends KMLObject<T> {
	  /**
	   * Basic Geometry Constructor to interface with the KML constructor
	   *
	   * @param parent parent KML Object
	   * @param name name of the Node type
	   */
	  protected TimePrimitive(String tag) {
	    super(tag);
	  }
}
