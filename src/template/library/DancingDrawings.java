package template.library;


import processing.core.*;
import oscP5.*;

/**
 * This is a template class and can be used to start a new processing Library.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own Library naming convention.
 * 
 * (the tag example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 * @example Hello 
 */

public class DancingDrawings {
	
	// myParent is a reference to the parent sketch
	PApplet myParent;
	OscP5 oscClient;
	OscProperties properties;

	float discreteValue = 0;
	float continuousValue = 0;
	String listeningAddress = null;
	
	public final static String VERSION = "##library.prettyVersion##";
	

	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the Library.
	 * 
	 * @example Hello
	 * @param theParent the parent PApplet
	 */
	public DancingDrawings(PApplet theParent) {
		myParent = theParent;
		OscProperties properties = new OscProperties();
		properties.setRemoteAddress("127.0.0.1", 12003);
		properties.setListeningPort(12002);
		properties.setSRSP(OscProperties.ON);
		System.out.println(properties.toString());

		oscClient = new OscP5(this, properties);
		discreteValue = 0;
		welcome();
	}
	
	public DancingDrawings(PApplet theParent, String address) {
		listeningAddress = address;
		myParent = theParent;
		OscProperties properties = new OscProperties();
		properties.setRemoteAddress("127.0.0.1", 12003);
		properties.setListeningPort(12002);
		properties.setSRSP(OscProperties.ON);
		System.out.println(properties.toString());

		oscClient = new OscP5(this, properties);
		discreteValue = 0;
		welcome();
	}
	
	
	private void welcome() {
		System.out.println("##library.name## ##library.prettyVersion## by ##author##");
	}
	
	
	public float getNormalizedDiscreteStepValue() {
		return discreteValue;
	}
	public float getNormalizedContinuousValue() {
		return continuousValue;
	}
	/**
	 * return the version of the Library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}
	
	public void oscEvent(OscMessage m) {
		String listenTo = "/dancing-drawings";
		if(listeningAddress != null) {
			listenTo = listeningAddress;
		}
		
		if (m.checkAddrPattern(listenTo + "/discrete")) {
			discreteValue = m.get(0).floatValue();
		}
		
		if (m.checkAddrPattern(listenTo + "/continuous")) {
			continuousValue = m.get(0).floatValue();
		}
	}
}

