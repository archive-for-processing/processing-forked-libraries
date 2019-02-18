package kml_builder;

import processing.data.XML;

public class Location extends Coordinates implements Buildable {

  /**
   * Basic Constructor for the Location class Initialized by default with a
   * Latitude Longitude and Altitude of 0
   */
  public Location() {
  }

  /**
   * Basic Constructor for the Location Class Allows Longitude and Latitude to be
   * set on Initialization
   * 
   * @param longitude Double representing the longitudinal coordinate of this
   *                  location between -180 and 180 degrees
   * @param latitude  Double representing the latitudinal coordinate of this
   *                  location between -90 and 90 degrees
   */
  public Location(double longitude, double latitude) {
    super(longitude, latitude);
  }

  /**
   * Complete constructor for the Location Class Allows Longitude, Latitude and
   * Altitude to be set on Ininitialization
   * 
   * @param longitude Double representing the longitudinal coordinate of this
   *                  location between -180 and 180 degrees
   * @param latitude  Double representing the latitudinal coordinate of this
   *                  location between -90 and 90 degrees
   * @param altitude  Double representing the altitude of this coordinate in
   *                  meters.
   * @see             AltitudeMode
   */
  public Location(double longitude, double latitude, double altitude) {
    super(longitude, latitude, altitude);
  }

  @Override
  public XML build() {
    XML out = new XML("Location");
    out.addChild("longitude").setDoubleContent(getLongitude());
    out.addChild("latitude").setDoubleContent(getLatitude());
    out.addChild("altitude").setDoubleContent(getAltitude());

    return out;
  }
}
