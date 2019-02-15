package kml_builder;

import processing.data.XML;

//import processing.data.*;

/**
 * Geometry Abstract class Used to create an XML node with Geometry formatting
 * Contains common formatting used by objects that extend the Geometry Class
 *
 * @author Liam James (liam@minimaximize.com)
 * @version 0.1
 * @see Point
 * @see LineString
 * @see LinearRing
 * @see Polygon
 * @see MultiGeometry
 * @see Model
 * @since 15-04-2018
 */
public abstract class AbstractView<T extends KMLObject> extends KMLObject<T> {

  protected TimePrimitive timePrimitive = null;
  
	/**
	 * Basic Geometry Constructor to interface with the KML constructor
	 *
	 * @param parent
	 *            parent KML Object
	 * @param tag
	 *            name of the Node type
	 */
	protected AbstractView(String tag) {
		super(tag);
	}

	/**
	 * adds TimePrimitive to object extending AbstractView
	 * 
	 * @param tp
	 *            TimeSpan or Timestamp object using the gx namespace to be added to
	 *            the object Extending AbstractView
	 * @see TimePrimitive
	 * @return object extending AbstractView
	 */
	public T addTimePrimitive(TimePrimitive tp) {
	  this.timePrimitive = tp;
	  
		return (T) this;
	}
	
	public XML build(XML base) {
	  base = addChildElement(base, timePrimitive);
	  
	  return base;
	}


//	/**
//	 * Inserts a ViewerOption element to the object extending AbstractView
//	 * </p>
//	 * This element enables special viewing modes in Google Earth 6.0 and later. It
//	 * has one or more <gx:option> child elements. The <gx:option> element has a
//	 * name attribute and an enabled attribute. The name specifies one of the
//	 * following: Street View imagery ("streetview"), historical imagery
//	 * ("historicalimagery"), and sunlight effects for a given time of day
//	 * ("sunlight"). The enabled attribute is used to turn a given viewing mode on
//	 * or off.
//	 * 
//	 * @param option
//	 *            the ViewerOption to be inserted into the object extending the
//	 *            AbstractView
//	 * @return object extending AbstractView
//	 */
//	public T setViewerOption(gxViewerOptions option) {
//		this.cursor.addChild(option.cursor);
//		// XML viewOptions;
//		// viewOptions = this.getValidChild(this.cursor, "gx:ViewerOptions");
//		// viewOptions.addChild(option.cursor);
//
//		return (T) this;
//	}
}