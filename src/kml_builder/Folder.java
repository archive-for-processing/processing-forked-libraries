package kml_builder;

public class Folder extends Container<Folder> {
	  
	  /**
	   * Basic Document Constructor to be used by other class objects
	   * Initialises internal XML objects
	   *
	   * @param parent parent KML Object
	   */
	  protected Folder(){
	    super("Folder");
	  }
}
