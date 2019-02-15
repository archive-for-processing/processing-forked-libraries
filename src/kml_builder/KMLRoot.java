package kml_builder;
import processing.data.*;

/**
 * KML Editor Class
 * Used to create an XML object with KML formatting
 * Additionally contains helper functions for adding additional data.
 *
 * @author       Liam James (liam@minimaximize.com)
 * @version      0.1
 * @since        13-04-2018
 */
public class KMLRoot <T extends KMLRoot> {
  // Private Class Variables
  protected XML root;
  protected XML cursor;
  private String fileName = "default.kml";

  /**
   * adds Document tags to the KML Object
   *
   * @param ID id of the document object
   * @return Document Object
   */
  public Document addDocument(String ID) {
    Document out = new Document(this);
    return out.setString("id", ID);
  }

  /**
   * adds Document tags to the KML Object
   *
   * @return ID documnt ID
   */
  public Document addDocument() {
    Document out = new Document(this);
    return out;
  }

  /**
   * adds Document tags to the KML Object
   *
   * @param ID id of the document object
   * @return Document Object
   */
  public Placemark addPlacemark(String ID) {
    Placemark out = new Placemark(this);
    return out.setString("id", ID);
  }

  /**
   * returns the root XML object
   *
   * @return root XML node
   */
  public XML getRootNode() {
    return root;
  }

  protected T setString(String name, String value) {
    this.cursor.setString(name, value);
    return (T)this;
  }

  public String getFileName() {
	  return this.fileName;
    //return root.getChild(DOCUMENT).getChild("name").getContent();
  }
 
  // -------------------------------------------- Constructors ------------------------------------------

  /**
   * Basic KML constructor
   * Sets default namespaces and uses the default.kml filename
   */
  public KMLRoot() {
    root = new XML("kml");
    init();
  }

  /**
   * Basic KML constructor
   * Sets default namespaces and uses the filename as the name of the file
   * @param filename the name of the KML file
   */
  public KMLRoot(String fileName) {
    root = new XML("kml");
    this.fileName = fileName;
    init();
  }

  /**
   * KML child node constructor
   * Sets default namespaces and uses the filename as the name of the file
   * @param filename the name of the KML file
   */
  public KMLRoot(KMLRoot parent, String name) {
    this.root = parent.root;
    this.cursor = parent.cursor.addChild(name);
    this.fileName = parent.fileName;
  }

  // ------------------------------------------ Helper Methods ------------------------------------------

  /**
   * Function to initialise the kml document with default namespaces and formatting and sets the root node.
   */
  private void init() {
    // Initialize Namespaces
    root.setString("xmlns", KML_NAMESPACE);
    root.setString("xmlns:gx", G_KML_NAMESPACE);
    root.setString("xmlns:kml", KML_NAMESPACE);
    root.setString("xmlns:atom", ATOM_NAMESPACE);

    // Initialize Document
    cursor = root;
  }

  /**
   * gets or adds a child node to the given node
   *
   * @param Node the parent node of the desired child
   * @param childname relative file path to the 
   * @return XML object childname in the parent Node obejct or a new child with childname
   */
  protected XML getValidChild(XML Node, String childName) {
    if (Node.getChild(childName)!=null) {
      return Node.getChild(childName);
    } else {
      return Node.addChild(childName);
    }
  }

  /**
   * sets the ID for the node
   *
   * @param id the id being added to the object
   * @return XML object childname in the parent Node obejct or a new child with childname
   */
  protected T addID(String id) {
    this.cursor.setString("id", id);
    return (T)this;
  };
  
//------------------------------------------ Static Strings ------------------------------------------
//Namespaces
final static String KML_NAMESPACE = "http://www.opengis.net/kml/2.2";
final static String G_KML_NAMESPACE = "http://www.google.com/kml/ext/2.2";
final static String ATOM_NAMESPACE = "http://www.w3.org/2005/Atom";

final static String XSD_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
final static String XSI_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";
final static String XAL_NAMESPACE = "urn:oasis:names:tc:ciq:xsdschema:xAL:2.0";

//Tags
final static String DOCUMENT = "Document";

final static String NAME = "name";
final static String VISIBILITY = "visibility";
final static String OPEN = "open";
final static String ADDRESS = "address";
final static String PHONE = "phoneNumber";
final static String SNIPPET = "Snippet";
final static String DESCRIPTION = "description";
final static String ABSTRACT_VIEW = "AbstractView";
final static String TIME_PRIMITIVE = "TimePrimitive";
final static String STYLE_URL = "styleUrl";
final static String STYLE_SELECTOR = "StyleSelector";
final static String REGION = "Region";
final static String METADATA = "Metadata";
final static String EXTENDED_DATA = "ExtendedData";

final static String EXTRUDE = "extrude";
final static String TESSELLATE = "tessellate";
final static String LINESTRING = "LineString";
final static String LINEAR_RING = "LinearRing";
final static String POLYGON = "Polygon";

final static String OUTER_BOUNDARY_IS = "outerBoundaryIs";
final static String INNER_BOUNDARY_IS = "innerBoundaryIs";

final static String MULTIGEOMETRY = "MultiGeometry";


final static String BALLOONSTYLE = "BalloonStyle";
final static String BGCOLOR = "bgColor";
final static String TEXTCOLOR = "textColor";
final static String TEXT = "text";
final static String DISPLAY_MODE = "displayMode";
final static String COLOR = "color";
final static String COLORMODE = "colorMode";

final static String STYLE = "Style";
final static String ICON_STYLE = "IconStyle";
final static String ICON = "Icon";
final static String PLACEMARK = "Placemark";
final static String POINT = "Point";
final static String COORDINATES = "coordinates";
final static String ALTITUDE_MODE = "altitudeMode";
final static String MODEL = "Model";
final static String LOCATION = "Location";
final static String ORIENTATION = "Orientation";
final static String SCALE = "Scale";
final static String LINK = "Link";
final static String HREF = "href";
final static String RESOURCEMAP = "ResourceMap";
final static String ALIAS = "Alias";


//Helper
final static String CDATA_OPEN = "<![CDATA[\n";
final static String CDATA_CLOSE = "\n]]>";
}