package kml_builder;

import kml_builder.namespace.gx.Tour;
import processing.data.XML;

/**
 * Feature Abstract class Used to create an XML node with Feature formatting
 * Contains common formatting used by objects that extend the Feature Class
 *
 * @author  Liam James (liam@minimaximize.com)
 * @version 0.1
 * @see     Container
 * @see     Overlay
 * @see     Placemark
 * @see     Tour
 * @since   15-04-2018
 */
abstract class Feature<T extends KMLObject> extends KMLObject<T> {

  // Feature fields
  protected String  name;
  protected boolean visibility = true;
  protected boolean open       = false;
//	protected XML atomAuthor;
//	protected XML atomLink;
  protected String address;
//	protected XML xalAddressDetails;
  protected String phoneNumber;
//	protected Snippet snippet;
  protected String        description;
  protected AbstractView  abstractView;
  protected TimePrimitive timePrimitive;
  protected String        styleURL;
  protected StyleSelector styleSelector;
//	protected XML extendedData

  /**
   * Basic Container Feature to interface with the KML constructor
   *
   * @param parent parent KML Object
   * @param tag    tag for this Node
   */
  Feature(String tag) { super(tag); }

  /**
   * Adds name object to the Structure
   *
   * @param  name name of the selected object
   * @return      Object extended by Feature
   */
  public T setName(String name) { this.name = name; return (T) this; }

  /**
   * Adds Visibility object to the Structure
   *
   * @param  status boolean to set the Visibility status of the object
   * @return        Object extended by Feature
   */
  public T setVisibility(Boolean status) {
    visibility = status;
    return (T) this;
  }

  /**
   * Adds Open object to the Structure
   *
   * @param  status boolean to set the Open status of the object
   * @return        Object extended by Feature
   */
  public T setOpen(Boolean status) {
    open = status;

    return (T) this;
  }

  /**
   * Adds Address object to the Structure
   *
   * @param  address address to be added to the selected object
   * @return         Object extended by Feature
   */
  public T setAddress(String address) {
    this.address = address;

    return (T) this;
  }

  /**
   * Adds phoneNumber object to the Structure
   *
   * @param  phoneNumber phone number to be added to the selected object
   * @return             Object extended by Feature
   */
  public T setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;

    return (T) this;
  }

  /**
   * Adds Snippet object to the Structure
   *
   * @param  text text to be shown in the snippet
   * @return      Object extended by Feature
   */
  public T setSnippet(String text, int maxLines) {
    // TODO: Snippet code

    return (T) this;
  }

  /**
   * Adds Description object to the Structure
   *
   * @param  description description to be added to the selected object
   * @param  mode        sets if the object uses CDATA tags
   * @return             Object extended by Feature
   */
  public T setDescription(String description, String mode) {
    this.description = description;

    return (T) this;
  }

  /**
   * Adds an Abstract View to this object
   * 
   * @param  view the abstract view
   * @return      this object
   */
  public T setAbstractView(AbstractView view) {
    this.abstractView = view;

    return (T) this;
  }

  /**
   * Adds a time primitive to this object
   * 
   * @param  time the time primitive object
   * @return      this object
   */
  public T setTimePrimitive(TimePrimitive time) {
    this.timePrimitive = time;

    return (T) this;
  }

  /**
   * Adds style URL object to the Structure
   *
   * @param  urlID URL content for the styleURL object
   * @return       Object extended by Feature
   */
  public T setStyleURL(String urlID) {
    this.styleURL = urlID;

    return (T) this;
  }

  /**
   * Adds a style selector to this Object
   * 
   * @param  style the Style selector being added to this feature
   * @return       This object
   */
  public T setStyleSelector(StyleSelector style) {
    this.styleSelector = style;

    return (T) this;
  }

  protected XML build(XML base) {
    base = addAttribute(base, "name", name);
    base = addAttribute(base, "visibility", visibility ? "1" : "0");
    base = addAttribute(base, "visibility", open ? "1" : "0");

//    protected XML atomAuthor;
//    protected XML atomLink;
    base = addAttribute(base, "address", address);
//    protected XML xalAddressDetails;
    base = addAttribute(base, "phoneNumber", phoneNumber);
//    protected Snippet snippet;
    base = addAttribute(base, "description", description);
    base = addChildElement(base, abstractView);
    base = addChildElement(base, timePrimitive);
    base = addAttribute(base, "styleUrl", styleURL);
    base = addChildElement(base, styleSelector);

    return base;
  }
}