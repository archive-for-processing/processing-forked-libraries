package kml_builder;

/**
 * Document class
 * Used to create an XML node with Document formatting
 * Additionally contains helper Functions to ensure Document is formatted correctly
 *
 * @author       Liam James (liam@minimaximize.com)
 * @version      0.1
 * @see          Feature
 * @since        15-04-2018
 */
public class Document extends Container<Document>{
  
  /**
   * Basic Document Constructor to be used by other class objects
   * Initialises internal XML objects
   *
   * @param parent parent KML Object
   */
  protected Document(KMLRoot parent){
    super("Document");
  }
}