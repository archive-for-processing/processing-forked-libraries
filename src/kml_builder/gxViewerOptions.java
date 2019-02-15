package kml_builder;

import processing.data.*;

/**
 * <h1>gxViewerOptions</h1> Used to control the style of gxViewerOptions in a
 * KML file
 * </p>
 * This element enables special viewing modes in Google Earth 6.0 and later. It
 * has one or more gx:option child elements. The gx:option element has a name
 * attribute and an enabled attribute. The name specifies one of the following:
 * Street View imagery ("streetview"), historical imagery ("historicalimagery"),
 * and sunlight effects for a given time of day ("sunlight"). The enabled
 * attribute is used to turn a given viewing mode on or off.
 * 
 * @author Liam James (liam@minimaximize.com)
 * @version 0.1
 * @see ColorStyle
 * @since 20-04-2018
 */
public class gxViewerOptions extends KMLRoot<gxViewerOptions> {

	/**
	 * basic constructor for the gxViewerOptions to be used by other classes
	 * 
	 * @param parent
	 *            parent object instantiating this gxViewerOptions
	 */
	protected gxViewerOptions(KMLRoot parent) {
		super(parent, "gxViewerOptions");
	}

	/**
	 * Method for getting the option set to StreetView
	 * 
	 * @param parent
	 *            parent object instantiating this gxViewerOptions
	 * @return new gxViewerOptions with option set to StreetView
	 */
	public static gxViewerOptions streetView(KMLRoot parent) {
		gxViewerOptions out = new gxViewerOptions(parent);
		out.addOption(STREETVIEW, true);
		return out;
	}

	/**
	 * Method for getting the option set to historicalImagery
	 * 
	 * @param parent
	 *            parent object instantiating this gxViewerOptions
	 * @return new gxViewerOptions with option set to historicalImagery
	 */
	public static gxViewerOptions historicalImagery(KMLRoot parent) {
		gxViewerOptions out = new gxViewerOptions(parent);
		out.addOption(HISTORICALIMEGRY, true);
		return out;
	}

	/**
	 * Method for getting the option set to sunlight
	 * 
	 * @param parent
	 *            parent object instantiating this gxViewerOptions
	 * @return new gxViewerOptions with option set to sunlight
	 */
	public static gxViewerOptions sunlight(KMLRoot parent) {
		gxViewerOptions out = new gxViewerOptions(parent);
		out.addOption(SUNLIGHT, true);
		return out;
	}

	/**
	 * Helper Method for changing the settings for a gxViewerOptions object
	 * 
	 * @param parent
	 *            parent object instantiating this gxViewerOptions
	 * @return new gxViewerOptions with option set to Street View
	 */
	private void addOption(String name, boolean enabled) {
		XML option;
		option = this.cursor.addChild("option");
		option.setString("name", name);
		option.setString("name", Boolean.toString(enabled));
	}

	private static final String STREETVIEW = "streetview";
	private static final String HISTORICALIMEGRY = "historicalimagery";
	private static final String SUNLIGHT = "sunlight";

}
