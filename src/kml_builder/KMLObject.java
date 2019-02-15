package kml_builder;

import processing.data.XML;

abstract class KMLObject<T extends KMLObject> implements Buildable{
  protected final String nodeTag;
  protected String       id     = null;
  protected String       target = null;

  /**
   * Default constructor for KML objects to be used by child classes
   * 
   * @param nodeTag a String representing the tag for this KML Object
   */
  protected KMLObject(String nodeTag) {
    this.nodeTag = nodeTag;
  }

  /**
   * Set the ID attribute of this object
   * 
   * @param  id string which allows unique identification of a KML element
   * @return    This object
   */
  public T setID(String id) {
    this.id = id;
    return (T) this;
  }

  /**
   * Set the target ID attribute of this object
   * 
   * @param  target string used to reference objects that have already been loaded
   *                into Google Earth
   * @return        This object
   */
  public T setTargetID(String target) {
    this.target = target;
    return (T) this;
  }

  /**
   * Returns the ID attribute for this element
   * 
   * @return a string representing the ID of this object, or null if not set
   */
  public String getID() { return id; }

  /**
   * Returns the Target ID attribute for this element
   * 
   * @return a string referencing an existing object int he scene or null if not
   *         set.
   */
  public String getTargetID() { return target; }

  /**
   * Creates XML representing this object and returns it
   * 
   * @return This object as XML
   */
  public final XML build() {
    XML out = new XML(nodeTag);
    // object has ID
    out = addAttribute(out, "id", id);
    out = addAttribute(out, "targetId", target);

    out = build(out);

    // Return this object as XML
    return out;
  }
  
  /**
   * The child specific implementation of 'Build' This method is called in the KML
   * object build method.
   * 
   * @param  base The prepared XML representation of this object
   * @return      The processed XML representation of this object
   */
  protected abstract XML build(XML base);


  /**
   * Helper method for adding attributes during build
   * 
   * @param  base The base XML object used in build
   * @param  tag  The String representing the tag of the added attribute
   * @param  val  The string representation of the value being added.
   * @return      XML with the attribute element added or base if val was null
   */
  protected final XML addAttribute(XML base, String tag, String val) {
    if (val != null) {
      base.addChild(tag).setContent(val);
    }

    return base;
  }

  /**
   * Helper methods for cleaning up the addition of Child elements
   * 
   * @param  base the base XML object used in build
   * @param  elem the child element being added to this object
   * @return      XML with the child element added or base if elem is null
   */
  protected final XML addChildElement(XML base, Buildable elem) {
    if (elem != null) {
      XML child = elem.build();
      base.addChild(child);
    }
    return base;
  }
}
