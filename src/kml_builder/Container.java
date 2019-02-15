package kml_builder;

import java.util.ArrayList;

import processing.data.XML;

/**
 * Container Abstract class Used to create an XML node with Container formatting
 * Contains common formatting used by objects that extend the Container Class
 *
 * @author  Liam James (liam@minimaximize.com)
 * @version 0.1
 * @see     Feature
 * @see     Document
 * @see     Folder
 * @since   15-04-2018
 */
public abstract class Container<T extends KMLObject> extends Feature<T> {

  protected ArrayList<Feature> contents;

  /**
   * Basic Container Constructor to interface with the KML constructor
   *
   * @param parent parent KML Object
   * @param name   name of the Node type
   */
  protected Container(String tag) {
    super(tag);
    contents = new ArrayList<Feature>();
  }

  /**
   * Add a feature to this Container
   * 
   * @param  feature the feature object to be added
   * @return         this object
   */
  public T addFeature(Feature feature) {
    contents.add(feature);
    return (T) this;
  }

  protected XML build(XML base) {
    base = super.build(base);
    for (Feature f : contents) {
      base = addChildElement(base, f);
    }
    return base;
  }

}