/**
 * 
 */
package kml_builder;

/**
 * <h1>LabelStyle Class</h1> Used to control the style of LabelStyle in a KML
 * file
 * </p>
 * Specifies how the <name> of a Feature is drawn in the 3D viewer. A custom
 * color, color mode, and scale for the label (name) can be specified.
 * 
 * @author Liam James (liam@minimaximize.com)
 * @version 0.1
 * @see ColorStyle
 * @since 20-04-2018
 */
public class LabelStyle extends ColorStyle<LabelStyle> {
	protected LabelStyle(KMLRoot parent) {
		super(parent, "LabelStyle");
	}

	/**
	 * creates and sets content for the scale tags in this LabelStyle
	 * </p>
	 * Resizes the label.
	 * 
	 * @param scale
	 *            size of the label
	 * @return this Label Style object
	 */
	LabelStyle addScale(float scale) {
		getValidChild(this.cursor, "scale").setContent(Float.toString(scale));

		return this;
	}
}
