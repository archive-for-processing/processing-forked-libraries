package lazer.viz;

import java.util.HashMap;

import themidibus.*;
import controlP5.*;
import processing.core.*;

public class LazerController implements LazerConstants {

	
	 private static int CONTROL_HEIGHT = 15;
	 private static int CONTROL_MARGIN = 3;
	 private int[] midiState;
	 private HashMap<String,Integer> mappings;
	 private HashMap<Integer,String> reverses;
	 private HashMap<Integer,String> noteControls;
	 private MidiBus nanoKontrol;
	 private MidiBus vdmxKontrol;
	 private ControlP5 cp5;
	 private PApplet p;

	 public LazerBeatManager beatManager; 
	 
	 /**
	  * Constructor
	  * @param p The parent applet (i.e. this on your processing sketch)
	  */
	 public LazerController(PApplet p) {
		 this.p = p;
		 cp5 = new ControlP5(p);
		 cp5.setColor(new CColor(0xffaa0000, 0xff330000, 0xffff0000, 0xffffffff, 0xffffffff));
		 midiState = new int[128]; 
		 mappings = new HashMap<String, Integer>();
		 reverses = new HashMap<Integer, String>();
		 noteControls = new HashMap<Integer, String>();
		 nanoKontrol = new MidiBus(this, "SLIDER/KNOB", "CTRL", "nanoKontrol");
		 vdmxKontrol = new MidiBus(this, "From VDMX", "To VDMX", "vdmxKontrol");
		 beatManager = new LazerBeatManager(p);
	 }

	 /**
	  * Returns the current value for a mapping that you've set. Will return
	  * -1 if the mapping isn't set (it won't throw)
	  * @param mapping the mapping to retrieve
	  * @return an integer value for the current state of the controller
	  */
	 public int get(String mapping) {

	   try {
	     return midiState[mappings.get(mapping)];
	   }
	   catch (Exception e) {
	     return -1;
	   }

	 }

	 /**
	  * Handles a midi event from the controller, setting the state on the
	  * internal thingy
	  * @param channel what channel the event happened in
	  * @param number what control midi number
	  * @param value the value of the control
	  */
	 private void handleMidiEvent(int channel, int number, int value) {
	   if (number >= 0) {
	     setMidiState(number, value);
	   }
	 }

	 private void setMidiState(int number, int value) {
		 midiState[number] = value;
		 String name = reverses.get(number);
		 if (name != null) {
			 try {
				 cp5.getController(name).setValue(value);
			 } catch (Exception e) {
				 System.err.println("error updating slider for " + name);
			 }	 
		 }
	 }
	 
	 /**
	  * Allows you to attach a previously defined mapping to a note
	  * event (i.e. from VDMX) 
	  * @param name
	  * @param control constant name of the VDMX midi events
	  */
	 public void setNoteControl(String name, int control) {
		 noteControls.put(control, name);
	 }
	 
	 /**
	  * Allows to set the value of a control mapping given a name
	  * @param name the name of the control
	  * @param value
	  */
	 public void setControlValueByName(String name, int value) {
		 setMidiState(mappings.get(name), value);
	 }

	 /**
	  * allows you to setup a mapping from a name to one of the controls
	  * on the midi controller
	  * @param name name of the mapping to set
	  * @param control constant of one of the nanoKontrol midi events
	  */
	 public void setMapping(String name, int control) {
		 setMapping(name, control, 0);
	 }
	
	 /**
	  * Overloaded method, allowing you to set an initial value for the mapping
	  * @param name 
	  * @param control
	  * @param initialValue
	  */
	 public void setMapping(String name, int control, int initialValue) {
		 mappings.put(name, control);
		 reverses.put(control, name);
		 midiState[mappings.get(name)] = initialValue;
		 createControllerForMapping(name, initialValue);
	 }

	 
	 private void createControllerForMapping(String name) {
		 createControllerForMapping(name, 0);
	 }
	 
	 /**
	  * 
	  * @param name
	  */
	 private void createControllerForMapping(String name, int initialValue) {
		 Slider sd = cp5.addSlider(name)
	     	.setRange(0,127)
	     	.setValue(initialValue)
	     	.setHeight(CONTROL_HEIGHT)
	     	.setPosition(calculatePositionForMapping())
	     	.addCallback(new CallbackListener() {
	     		public void controlEvent(CallbackEvent theEvent) {
	     			if(theEvent.getAction()==ControlP5.ACTION_BROADCAST){
	     				Controller cont = theEvent.getController();
//	     				System.out.println(cont.getName() + ": " + cont.getValue());
	     				midiState[mappings.get(cont.getName())] = (int) cont.getValue(); 
	     			}
	     		}
	     	});
		 	
		 
	 }
	 
	 private PVector calculatePositionForMapping() {
		 float x = 10;
		 float y = CONTROL_MARGIN + (mappings.size() * (CONTROL_HEIGHT + CONTROL_MARGIN));
		 return new PVector(x, y);
	 }
	 

	 /**
	  * Draws the control mappings and their state to the screen
	  * @Deprecated
	  */
	 public void printMappings() {
	   // this is deprecated;

	 }

	/**
	 * Draws a single mapping
	 * @Deprecated
	 * @param key
	 * @param i
	 */
	private void drawMapping(String key, int i) {

	   int x = 10;
	   int y = (i * 15);

	   p.fill(255, 150, 200, 100);
	   p.rect(x - 1, y-10, this.get(key), 14);
	   p.fill(255, 0, 255);
	   p.text(key + " = " + this.get(key), x, y);
	}
	
	 
	/**
	 * Method called by themidibus when a midi controller change event happens
	 * @param channel
	 * @param number
	 * @param value
	 * @param timestamp
	 * @param bus_name
	 */
	public void controllerChange(int channel, int number, int value, long timestamp, String bus_name) {

		if (bus_name == "nanoKontrol") {
			handleMidiEvent(channel, number, value);
		}

		if (bus_name == "vdmxKontrol") {

		}

	}

	/**
	 * Method called by themidibus when a note event happens
	 * @param channel
	 * @param pad
	 * @param velocity
	 * @param timestamp
	 * @param bus_name
	 */
	public void noteOn(int channel, int pad, int velocity, long timestamp, String bus_name) {
	
		if (channel == VDMX_BEAT) {
			beatManager.beat();
		}
		
		try {
			String mapping = noteControls.get(channel);
			setControlValueByName(mapping, pad);
		} catch (Exception e) {
			
		}
	  

	}
}
