package kml_builder;

/**
 * <h1>LineStyle Class</h1> Used to control the style of LineStyle in a KML file
 * </p>
 * Specifies the drawing style (color, color mode, and line width) for all line
 * geometry. Line geometry includes the outlines of outlined polygons and the
 * extruded "tether" of Placemark icons (if extrusion is enabled).
 * 
 * @author Liam James (liam@minimaximize.com)
 * @version 0.1
 * @see ColorStyle
 * @since 20-04-2018
 */
public class LineStyle extends ColorStyle<LineStyle> {

	/**
	 * Basic Constructor for LineStyle to be used by parent objects
	 * 
	 * @param parent
	 *            parent calling the constructor
	 */
	protected LineStyle(KMLRoot parent) {
		super(parent, "LineStyle");
	}

	/**
	 * sets and adds the width tags for this LineStyle
	 * </p>
	 * Width of the line, in pixels.
	 * 
	 * @param width
	 *            Width of the line, in pixels.
	 * @return this LineStyle object
	 */
	public LineStyle setWidth(float width) {
		getValidChild(this.cursor, "width").setContent(Float.toString(width));

		return this;
	}

	/**
	 * sets and adds the outerColor tags for this LineStyle
	 * </p>
	 * Color of the portion of the line defined by gx:outerWidth. Note that the
	 * gx:outerColor and gx:outerWidth elements are ignored when LineStyle is
	 * applied to Polygon and LinearRing.
	 * 
	 * @param color
	 *            Color (as hex) of the portion of the line defined by gx:outerWidth
	 * @return this LineStyle object
	 */
	public LineStyle setOuterColor(String color) {
		getValidChild(this.cursor, "gx:outerColor").setContent(color);

		return this;
	}

	/**
	 * sets and adds the outerWidth tags for this LineStyle
	 * </p>
	 * A value between 0.0 and 1.0 that specifies the proportion of the line that
	 * uses the gx:outerColor. Only applies to lines setting width with
	 * gx:physicalWidth; it does not apply to lines using width. See also
	 * gx:drawOrder in LineString. A draw order value may be necessary if
	 * dual-colored lines are crossing each other—for example, for showing freeway
	 * interchanges.
	 * 
	 * @param width
	 *            A value between 0.0 and 1.0 that specifies the proportion of the
	 *            line that uses the gx:outerColor
	 * @return this LineStyle object
	 */
	public LineStyle setOuterWidth(float width) {
		getValidChild(this.cursor, "gx:outerWidth").setContent(Float.toString(width));

		return this;
	}

	/**
	 * sets and adds the PhysicalWidth tags for this LineStyle
	 * </p>
	 * Physical width of the line, in meters.
	 * 
	 * @param width
	 *            Physical width of the line, in meters.
	 * @return this LineStyle object
	 */
	public LineStyle setPhysicalWidth(float width) {
		getValidChild(this.cursor, "gx:physicalWidth").setContent(Float.toString(width));

		return this;
	}

	/**
	 * sets and adds the Visibility tags for this LineStyle
	 * </p>
	 * A boolean defining whether or not to display a text label on a LineString. A
	 * LineString's label is contained in the name element that is a sibling of
	 * LineString (i.e. contained within the same Placemark element).
	 * </p>
	 * Google Earth version 6.1+ does not display labels by default; they must be
	 * enabled for each LineString by setting <gx:labelVisibility> to 1.
	 * 
	 * @param labelVisibility
	 *            A boolean defining whether or not to display a text label on a
	 *            LineString.
	 * @return this LineStyle object
	 */
	public LineStyle setLabelVisibility(boolean labelVisibility) {
		getValidChild(this.cursor, "gx:labelVisibility").setContent(labelVisibility ? "1" : "0");

		return this;
	}

}
