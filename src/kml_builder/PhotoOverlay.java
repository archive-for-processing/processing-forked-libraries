package kml_builder;

import processing.data.XML;

/**
 * <h1>PhotoOverlay Class</h1>
 * Used to control the style of PhotoOverlay in a KML file 
 * 
 * @author       Liam James (liam@minimaximize.com)
 * @version      0.1
 * @see          Overlay
 * @since        20-04-2018
 * */
public class PhotoOverlay extends Overlay<PhotoOverlay> {

	/**
	 * basic Constructor for the PhotoOverlay object to be used by objects in a parent class
	 * 
	 * @param parent parent object instanciating the PhotoOverlay
	 * @see Overlay
	 * @see Feature
	 * */
	protected PhotoOverlay(KMLRoot parent) {
		super(parent, "PhotoOverlay");
	}
	
	/**
	 * Method for inserting Point into this PhotoOverlay
	 * 
	 * @param point the Point to insert into this PhotoOverlay
	 * @see Point
	 * @return this object
	 * */
	public PhotoOverlay setRotation(double rotation) {
		getValidChild(this.cursor, "rotation")
		.setContent(Double.toString(rotation));;
		
		return this;
	}
	
	/**
	 * Used to add ViewVolume tags to this Photo Overlay
	 * <p/>
	 * <b>ViewVolume</b> Defines how much of the current scene is visible.
	 * Specifying the field of view is analogous to specifying the lens opening in a physical camera. 
	 * A small field of view, like a telephoto lens, focuses on a small part of the scene.
	 *  A large field of view, like a wide-angle lens, focuses on a large part of the scene.
	 * 
	 * @param fovLeft Angle, in degrees, between the camera's viewing direction and the left side of the view volume.
	 * @param fovRight Angle, in degrees, between the camera's viewing direction and the right side of the view volume.
	 * @param fovBottom Angle, in degrees, between the camera's viewing direction and the bottom side of the view volume.
	 * @param fovTop Angle, in degrees, between the camera's viewing direction and the top side of the view volume.
	 * @param near Measurement in meters along the viewing direction from the camera viewpoint to the PhotoOverlay shape.
	 * */
	public PhotoOverlay setViewVolume(double fovLeft, double fovRight, double fovBottom, double fovTop, double near) {
		XML volume = getValidChild(this.cursor, "ViewVolume");
		
		// Set Content Points
		getValidChild(volume, "leftFov")
		.setContent(Double.toString(fovLeft));
		
		getValidChild(volume, "rightFov")
		.setContent(Double.toString(fovRight));
		
		getValidChild(volume, "bottomFov")
		.setContent(Double.toString(fovBottom));
		
		getValidChild(volume, "topFov")
		.setContent(Double.toString(fovTop));
		
		getValidChild(volume, "near")
		.setContent(Double.toString(near));
		
		return this;
	}
	
	/**
	 * Method for adding Image Pyramid to this Photo Overlay
	 * 
	 * @param tileSize the Size of the tiles, in pixels. Tiles must be square, and < tileSize > 
	 * must be a power of 2. A tile size of 256 (the default) or 512 is recommended. 
	 * The original image is divided into tiles of this size, at varying resolutions.
	 * @param maxWidth Width in pixels of the original image.
	 * @param maxHeight Height in pixels of the original image.
	 * @param gridOrigin Specifies where to begin numbering the tiles in each layer of the pyramid. 
	 * A value of lowerLeft specifies that row 1, 
	 * column 1 of each layer is in the bottom left corner of the grid.
	 * @return this object
	 * */
	public PhotoOverlay setImagePyramid(int tileSize, int maxWidth, int maxHeight, String gridOrigin) {
		XML volume = getValidChild(this.cursor, "ImagePyramid");
		
		// Set Content Points
		getValidChild(volume, "tileSize")
		.setContent(Integer.toString(tileSize));
		
		getValidChild(volume, "maxWidth")
		.setContent(Integer.toString(maxWidth));
		
		getValidChild(volume, "maxHeight")
		.setContent(Integer.toString(maxHeight));
		
		getValidChild(volume, "gridOrigin")
		.setContent(gridOrigin);
		
		return this;
	}
	
	/**
	 * Method for inserting Point into this PhotoOverlay
	 * 
	 * @param point the Point to insert into this PhotoOverlay
	 * @see Point
	 * @return this object
	 * */
	public PhotoOverlay setPoint(Point point) {
		this.cursor.addChild(point.cursor);
		
		return this;
	}
	
	/**
	 * Method for defining Point tags for this PhotoOverlay
	 * 
	 * @param coordTuples array of Coordinates containing lat, long [altitude]
	 * @see Coordinates
	 * @return this object
	 * */
	public PhotoOverlay setPoint(Coordinates[] coordTuples) {
		
		String coordString = "";
		for(Coordinates coord: coordTuples) {
			coordString += coord.toString() + " ";
		}
		
		getValidChild(this.cursor, "Point")
		.setContent(coordString);
		
		return this;
	}
	
	/**
	 * Method for defining shape tags for this PhotoOverlay
	 * 
	 * @param shapeMode type of KML:shape (rectangle, cylinder, sphere)
	 * @return this object
	 * */
	public PhotoOverlay setShape(String shapeMode) {

		getValidChild(this.cursor, "shape")
		.setContent(shapeMode);
		
		return this;
	}
}
