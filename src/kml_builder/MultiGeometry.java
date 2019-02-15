package kml_builder;
/**
 * Multi Geometry class
 * Used to create an XML node with Multi Geometry formatting
 * Additionally contains helper Functions to ensure Multi Geometry is formatted correctly
 *
 * @author       Liam James (liam@minimaximize.com)
 * @version      0.1
 * @see          Geometry
 * @since        15-04-2018
 */
public class MultiGeometry extends Geometry<MultiGeometry> {

  /**
   * Basic MultiGeometry Constructor to be used by other class objects
   * Initialises internal XML objects
   *
   * @param parent parent KML Object
   */
  protected MultiGeometry(KMLRoot parent) {
    super(parent, MULTIGEOMETRY);
  }
}