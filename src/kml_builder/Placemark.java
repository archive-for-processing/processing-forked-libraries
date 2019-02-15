package kml_builder;

/**
 * Placemark class
 * Used to create an XML node with Placemark formatting
 * Additionally contains Functions exclusive to Placemark
 *
 * @author       Liam James (liam@minimaximize.com)
 * @version      0.1
 * @see          Feature
 * @since        15-04-2018
 */
public class Placemark extends Feature<Placemark> {

  /**
   * Adds Point object to the Structure
   *
   * @param ID id of the Point Object
   * @return Point Object
   */
  public Point addPoint(String ID) {
    Point out = new Point(this);
    return out.setString("id", ID);
  }

  /**
   * Adds Line String object to the Structure
   *
   * @param ID id of the Line String Object
   * @return Line String Object
   */
  public LineString addLineString(String ID) {
    LineString out = new LineString(this);
    return out.setString("id", ID);
  }

  /**
   * Adds Point Line Ring to the Structure
   *
   * @param ID id of the Line Ring Object
   * @return Line Ring Object
   */
  public LinearRing addLinearRing(String ID) {
    LinearRing out = new LinearRing(this);
    return out.setString("id", ID);
  }

  /**
   * Adds Polygon object to the Structure
   *
   * @param ID id of the Polygon Object
   * @return Polygon Object
   */
  public Polygon addPolygon(String ID) {
    Polygon out = new Polygon(this);
    return out.setString("id", ID);
  }

  /**
   * Adds Multi Geometry object to the Structure
   *
   * @param ID id of the Multi Geometry Object
   * @return Multi Geometry Object
   */
  public MultiGeometry addMultiGeometry(String ID) {
    MultiGeometry out = new MultiGeometry(this);
    return out.setString("id", ID);
  }

  /**
   * Adds Model object to the Structure
   *
   * @param ID id of the Model Object
   * @return Model Object
   */
  public Model addModel(String ID) {
    Model out = new Model(this);
    return out.setString("id", ID);
  }

  /**
   * Basic Placemark Constructor to be used by other class objects
   * Initialises internal XML objects
   *
   * @param parent parent KML Object
   */
  protected Placemark(KMLRoot parent) {
    super(parent, PLACEMARK);
  }
}