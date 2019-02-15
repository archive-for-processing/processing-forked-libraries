package kml_builder;

import processing.data.XML;

public class LookAt extends AbstractView<LookAt> {
	protected LookAt(KMLRoot parent) {
		super(parent,"LookAt");
	}
	
	public LookAt setLocation(Coordinates coords) {
	    XML cursor;

	    // Set Location Content
	    cursor = getValidChild(this.cursor, "longitude");
	    cursor.setContent(Double.toString(coords.getLongitude()));

	    cursor = getValidChild(this.cursor, "latitude");
	    cursor.setContent(Double.toString(coords.getLatitude()));

	    cursor = getValidChild(this.cursor, "altitude");
	    cursor.setContent(Double.toString(coords.getAltitude()));

		
		return this;
	}
	
	  /**
	   * sets the orientation information for a given Camera.
	   *
	   * @param heading rotation of the model object on the z (up) axis
	   * @param tilt rotation of the model object on the x(toward) axis
	   * @param roll Orientation of the model object on the y(right) axis
	   * @return parent model
	   */
	  public LookAt setOrientation(float heading, float tilt, float roll) {
	    XML cursor;

	    // Set Orientation Content
	    cursor = getValidChild(this.cursor, "heading");
	    cursor.setContent(Float.toString(heading));

	    cursor = getValidChild(this.cursor, "tilt");
	    cursor.setContent(Float.toString(tilt));

	    cursor = getValidChild(this.cursor, "roll");
	    cursor.setContent(Float.toString(roll));

	    return this;
	  }
	  
	  public LookAt setAltitudeMode(String mode) {
		  XML cursor;
		  
		  cursor = getValidChild(this.cursor,"altitudeMode");
		  cursor.setContent(mode);
		  
		  return this;
	  }
	  
	  public LookAt setgxAltitudeMode(String gxmode) {
		  XML cursor;

		  
		  cursor = getValidChild(this.cursor,"gx:altitudeMode");
		  cursor.setContent(gxmode);
		  
		  return this;
	  }
}
