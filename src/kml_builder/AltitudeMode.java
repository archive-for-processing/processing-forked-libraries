package kml_builder;

import processing.data.XML;

/**
 * Specifies how altitude components in the <\coordinates\> element are interpreted. 
 * 
 * @author Liam James (liam@minimaximize.com)
 * @version 0.2
 * @since 2019-02-15
 */
public enum AltitudeMode implements Buildable {
  /**
   * Indicates to ignore an altitude specification (for example, in the
   * <\coordinates>\ tag).
   */
  CLAMP_TO_GROUND("clampToGround"),

  /**
   * Sets the altitude of the element relative to the actual ground elevation of a
   * particular location.
   */
  RELATIVE_TO_GROUND("relativeToGround"),

  /**
   * Sets the altitude of the coordinate relative to sea level, regardless of the
   * actual elevation of the terrain beneath the element.
   */
  ABSOLUTE("absolute");

  private final String tagContent;

  AltitudeMode(String tagContent) {
    this.tagContent = tagContent;
  }

  public XML build() {
    XML out = new XML("altitudeMode");
    out.setContent(tagContent);

    return out;
  }

}
