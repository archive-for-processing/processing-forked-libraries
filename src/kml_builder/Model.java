package kml_builder;

import processing.data.*;

/**
 * Model class
 * Used to create an XML node with Model formatting
 * Additionally contains helper Functions to ensure Model is formatted correctly
 *
 * @author       Liam James (liam@minimaximize.com)
 * @version      0.1
 * @see          Geometry
 * @since        15-04-2018
 */
public class Model extends Geometry<Model> {

  public Model setAltitudeMode(String altitudeMode) {
    XML mode;

    // Check if Location tags exist
    mode = getValidChild(this.cursor, ALTITUDE_MODE);

    // Set altitude mode Content
    mode.setContent(altitudeMode);

    return this;
  }

  /**
   * sets the location information for a given model.
   *
   * @param Model the parent XML object containing <Model> tags
   * @param longitude longitude position of the model object
   * @param latitude latitude position of the model object
   * @param altitude altitude position of the model object
   * @return parent model
   */
  public Model setLocation(double longitude, double latitude, double altitude) {
    XML cursor, location;

    // Check if Location tags exist
    location = getValidChild(this.cursor, LOCATION);

    // Set Location Content
    cursor = getValidChild(location, "longitude");
    cursor.setContent(Double.toString(longitude));

    cursor = getValidChild(location, "latitude");
    cursor.setContent(Double.toString(latitude));

    cursor = getValidChild(location, "altitude");
    cursor.setContent(Double.toString(altitude));

    return this;
  }

  /**
   * sets the location information for a given model.
   *
   * @param Coords Coordinates tuple containing information for Longitude, latitude, [altitude]
   * @return parent model
   */
  public Model setLocation(Coordinates Coords) {
    XML cursor, location;

    // Check if Location tags exist
    location = getValidChild(this.cursor, LOCATION);

    // Set Location Content
    cursor = getValidChild(location, "longitude");
    cursor.setContent(Double.toString(Coords.getLongitude()));

    cursor = getValidChild(location, "latitude");
    cursor.setContent(Double.toString(Coords.getLatitude()));

    cursor = getValidChild(location, "altitude");
    cursor.setContent(Double.toString(Coords.getAltitude()));

    return this;
  }

  /**
   * sets the orientation information for a given model.
   *
   * @param Model the parent XML object containing <Model> tags
   * @param heading rotation of the model object on the z (up) axis
   * @param tilt rotation of the model object on the x(toward) axis
   * @param roll otation of the model object on the y(rigth) axis
   * @return parent model
   */
  public Model setOrientation(float heading, float tilt, float roll) {
    XML cursor, orientation;

    // Check if Location tags exist
    orientation = getValidChild(this.cursor, ORIENTATION);

    // Set Orientation Content
    cursor = getValidChild(orientation, "heading");
    cursor.setContent(Float.toString(heading));

    cursor = getValidChild(orientation, "tilt");
    cursor.setContent(Float.toString(tilt));

    cursor = getValidChild(orientation, "roll");
    cursor.setContent(Float.toString(roll));

    return this;
  }

  /**
   * sets the scale information for a given model.
   *
   * @param Model the parent XML object containing <Model> tags
   * @param x scale of the model object on the x (toward) axis
   * @param y scale of the model object on the y (right) axis
   * @param z scale of the model object on the z (up) axis
   * @return parent model
   */
  public Model setScale(float x, float y, float z) {
    XML cursor, Scale;

    // Check if Scale tags exist
    Scale = getValidChild(this.cursor, SCALE);

    // Set Orientation Content
    cursor = getValidChild(Scale, "x");
    cursor.setContent(Float.toString(x));

    cursor = getValidChild(Scale, "y");
    cursor.setContent(Float.toString(y));

    cursor = getValidChild(Scale, "z");
    cursor.setContent(Float.toString(z));

    return this;
  }

  /**
   * sets the orientation information for a given model.
   *
   * @param Model the parent XML object containing <Model> tags
   * @param Path relative file path to the
   * @return Model parent model
   */
  public Model setLink(String Path) {
    XML cursor, Link;

    // Check if Scale tags exist
    Link = getValidChild(this.cursor, LINK);

    // Set Link Content
    cursor = getValidChild(Link, "href");
    cursor.setContent(Path);

    return this;
  }

  /**
   * sets the scale information for a given model.
   *
   * @param Model the parent XML object containing <Model> tags
   * @param x scale of the model object on the x (toward) axis
   * @param y scale of the model object on the y (right) axis
   * @param z scale of the model object on the z (up) axis
   * @return parent model
   */
  public Model addResourceMap(String target, String source) {
    XML cursor, Resource;

    Resource = getValidChild(this.cursor, RESOURCEMAP);
    Resource = getValidChild(Resource, ALIAS);

    // Set Orientation Content
    cursor = Resource.addChild("targetHref");
    cursor.setContent(target);

    cursor = Resource.addChild("sourceHref");
    cursor.setContent(source);

    return this;
  }

  /**
   * Basic Model Constructor to be used by other class objects
   * Initialises internal XML objects
   *
   * @param parent parent KML Object
   */
  protected Model(KMLRoot parent) {
    super(parent, MODEL);
  }
}