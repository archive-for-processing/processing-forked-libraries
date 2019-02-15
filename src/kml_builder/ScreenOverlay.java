package kml_builder;


/**
 * <h1>ScreenOverlay Class</h1>
 * Used to control the style of ScreenOverlay in a KML file 
 * 
 * @author       Liam James (liam@minimaximize.com)
 * @version      0.1
 * @see          Overlay
 * @since        20-04-2018
 * */
public class ScreenOverlay extends Overlay<ScreenOverlay> {

	protected ScreenOverlay(KMLRoot parent) {
		super(parent, "ScreenOverlay");
	}

	/**
	 * Sets OverlayXY for this ScreenOverlay
	 * 
	 * @param x Either the number of pixels, a fractional component of the image, or a pixel inset indicating the x component of a point on the overlay image.
	 * @param y Either the number of pixels, a fractional component of the image, or a pixel inset indicating the y component of a point on the overlay image.
	 * @param xunits Units in which the x value is specified. A value of "fraction" indicates the x value is a fraction of the image.
	 *  A value of "pixels" indicates the x value in pixels. A value of "insetPixels" indicates the indent from the right edge of the image.
	 * @param yunits Units in which the y value is specified. A value of "fraction" indicates the y value is a fraction of the image.
	 *  A value of "pixels" indicates the y value in pixels. A value of "insetPixels" indicates the indent from the right edge of the image.
	 *  
	 * @return this ScreenOverlay
	 * */
	public ScreenOverlay setOverlayXY(double x, double y, String xunits, String yunits) {

		return this;
	}

	/**
	 * Sets setScreenXY for this ScreenOverlay
	 * 
	 * @param x Either the number of pixels, a fractional component of the image, or a pixel inset indicating the x component of a point on the overlay image.
	 * @param y Either the number of pixels, a fractional component of the image, or a pixel inset indicating the y component of a point on the overlay image.
	 * @param xunits Units in which the x value is specified. A value of "fraction" indicates the x value is a fraction of the image.
	 *  A value of "pixels" indicates the x value in pixels. A value of "insetPixels" indicates the indent from the right edge of the image.
	 * @param yunits Units in which the y value is specified. A value of "fraction" indicates the y value is a fraction of the image.
	 *  A value of "pixels" indicates the y value in pixels. A value of "insetPixels" indicates the indent from the right edge of the image.
	 *  
	 * @return this ScreenOverlay
	 * */
	public ScreenOverlay setScreenXY(double x, double y, String xunits, String yunits) {

		return this;
	}

	/**
	 * Sets RotationXY for this ScreenOverlay
	 * 
	 * @param x Either the number of pixels, a fractional component of the image, or a pixel inset indicating the x component of a point on the overlay image.
	 * @param y Either the number of pixels, a fractional component of the image, or a pixel inset indicating the y component of a point on the overlay image.
	 * @param xunits Units in which the x value is specified. A value of "fraction" indicates the x value is a fraction of the image.
	 *  A value of "pixels" indicates the x value in pixels. A value of "insetPixels" indicates the indent from the right edge of the image.
	 * @param yunits Units in which the y value is specified. A value of "fraction" indicates the y value is a fraction of the image.
	 *  A value of "pixels" indicates the y value in pixels. A value of "insetPixels" indicates the indent from the right edge of the image.
	 *  
	 * @return this ScreenOverlay
	 * */
	public ScreenOverlay setRotationXY(double x, double y, String xunits, String yunits) {

		return this;
	}

	/**
	 * Sets the Size tags for this ScreenOverlay Object
	 * </p>
	 * <b>Size</b> Specifies the size of the image for the screen overlay, as follows:
	 * A value of −1 indicates to use the native dimension
	 * A value of 0 indicates to maintain the aspect ratio
	 * A value of n sets the value of the dimension
	 * 
	 * @param x size along the X axis
	 * @param y size along the Y axis
	 * @param xunits Units in which the x value is specified. A value of "fraction" indicates the x value is a fraction of the image.
	 *  A value of "pixels" indicates the x value in pixels. A value of "insetPixels" indicates the indent from the right edge of the image.
	 * @param yunits Units in which the y value is specified. A value of "fraction" indicates the x value is a fraction of the image.
	 *  A value of "pixels" indicates the y value in pixels. A value of "insetPixels" indicates the indent from the right edge of the image.
	 *  
	 * @return this ScreenOverlay
	 * */
	public ScreenOverlay setSize(double x, double y, String xunits, String yunits) {

		return this;
	}

	/**
	 * Indicates the angle of rotation of the parent object. A value of 0 means no rotation.
	 *  The value is an angle in degrees counterclockwise starting from north. 
	 *  Use +/-180 to indicate the rotation of the parent object from 0.
	 *  The center of the < rotation >, if not (.5,.5), is specified in < rotationXY >.
	 * 
	 * @param rotation
	 * 
	 * @return this ScreenOverlay
	 * */
	public ScreenOverlay setRotation(float rotation) {
		getValidChild(this.cursor, "rotation").setContent(Float.toString(rotation));

		return this;
	}
}
