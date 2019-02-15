package kml_builder;

/**
 * <h1>PolyStyle Class</h1> Used to control the style of PolyStyle in a KML file
 * </p>
 * Specifies the drawing style for all polygons, including polygon extrusions
 * (which look like the walls of buildings) and line extrusions (which look like
 * solid fences).
 * 
 * @author Liam James (liam@minimaximize.com)
 * @version 0.1
 * @see ColorStyle
 * @since 20-04-2018
 */
public class PolyStyle extends ColorStyle<PolyStyle> {

	/**
	 * Basic Constructor for PolyStyle to be used by parent objects
	 * 
	 * @param parent
	 *            parent calling the constructor
	 */
	protected PolyStyle(KMLRoot parent) {
		super(parent, "PolyStyle");
	}

	/**
	 * sets the fill tags for this PolyStyle object
	 * </p>
	 * Boolean value. Specifies whether to fill the polygon.
	 * 
	 * @param parent
	 *            parent calling the constructor
	 * @return this PolyStyle object
	 */
	public PolyStyle setFill(boolean state) {
		getValidChild(this.cursor, "fill").setContent(state ? "1" : "0");

		return this;
	}

	/**
	 * sets the outline tags for this PolyStyle object
	 * </p>
	 * Boolean value. Specifies whether to outline the polygon. Polygon outlines use
	 * the current LineStyle.
	 * 
	 * @param parent
	 *            parent calling the constructor
	 * @return this PolyStyle object
	 */
	public PolyStyle setoutline(boolean state) {
		getValidChild(this.cursor, "fill").setContent(state ? "1" : "0");

		return this;
	}
}
