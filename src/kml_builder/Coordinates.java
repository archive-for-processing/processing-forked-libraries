package kml_builder;

import processing.data.XML;

/**
 * Coordiantes tuple class Stores a Coordinates Tuple containing Longitude,
 * Latitude and Altitude
 *
 * @author Liam James (liam@minimaximize.com)
 * @version 1.0
 * @since 15-04-2018
 */
public class Coordinates {
	private double longitude = 0d;
	private double latitude = 0d;
	private double altitude = 0d;

	/**
	 * Wraps values around minimum and maximum values
	 *
	 * @return String version of tuple in the form longitude,latitude,altitude
	 */
	public String toString() {
		String out = longitude + "," + latitude + (altitude == 0 ? "" : "," + altitude);
		return out;
	}
	

	/**
	 * get the object longitude
	 *
	 * @return get object longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * get the object latitude
	 *
	 * @return get object longitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * get the object altitude
	 *
	 * @return get object longitude
	 */
	public double getAltitude() {
		return altitude;
	}
	
	 /**
   * Basic constructor for the Coordinates tuple
   *
   * @param longitude
   *            Longitude of the virtual camera (eye point). Angular distance in
   *            degrees, relative to the Prime Meridian. Values west of the
   *            Meridian range from −180 to 0 degrees. Values east of the Meridian
   *            range from 0 to 180 degrees.
   * @param latitude
   *            Latitude of the virtual camera. Degrees north or south of the
   *            Equator (0 degrees). Values range from −90 degrees to 90 degrees.
   * @return true if the file was saved successfully
   */
	public Coordinates() {}

	/**
	 * Basic constructor for the Coordinates tuple
	 *
	 * @param longitude
	 *            Longitude of the virtual camera (eye point). Angular distance in
	 *            degrees, relative to the Prime Meridian. Values west of the
	 *            Meridian range from −180 to 0 degrees. Values east of the Meridian
	 *            range from 0 to 180 degrees.
	 * @param latitude
	 *            Latitude of the virtual camera. Degrees north or south of the
	 *            Equator (0 degrees). Values range from −90 degrees to 90 degrees.
	 * @return true if the file was saved successfully
	 */
	public Coordinates(double longitude, double latitude) {
		this.longitude = wrapValue(longitude, -180d, 180d);
		this.latitude = wrapValue(latitude, -90d, 90d);
	}

	/**
	 * Basic constructor for the Coordinates tuple with altitude value
	 *
	 * @param longitude
	 *            Longitude of the virtual camera (eye point). Angular distance in
	 *            degrees, relative to the Prime Meridian. Values west of the
	 *            Meridian range from −180 to 0 degrees. Values east of the Meridian
	 *            range from 0 to 180 degrees.
	 * @param latitude
	 *            Latitude of the virtual camera. Degrees north or south of the
	 *            Equator (0 degrees). Values range from −90 degrees to 90 degrees.
	 * @param Distance
	 *            of the camera from the earth's surface, in meters. Interpreted
	 *            according to the Camera's altitudeMode or gx:altitudeMode.
	 * @return true if the file was saved successfully
	 */
	public Coordinates(double longitude, double latitude, double altitude) {
		this.longitude = wrapValue(longitude, -180d, 180d);
		this.latitude = wrapValue(latitude, -90d, 90d);
		this.altitude = altitude;
	}

	/**
	 * Wraps values around minimum and maximum values
	 *
	 * @param in
	 *            input number
	 * @param from
	 *            minimum value
	 * @param to
	 *            maximum value
	 * @return true if the file was saved successfully
	 */
	private double wrapValue(double in, double from, double to) {
		if (in < from) {
			in = in - from;
			in = to + in;
		}

		if (in > to) {
			in = in - to;
			in = in + from;
		}
		return in;
	}
}
