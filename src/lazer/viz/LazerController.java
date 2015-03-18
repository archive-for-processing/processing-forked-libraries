package lazer.viz;

import java.util.HashMap;
import themidibus.*;

import processing.core.*;

public class LazerController implements LazerConstants {

	 int[] midiState;
	 HashMap<String,Integer> mappings;
	 HashMap<Integer,String> noteControls;
	 MidiBus nanoKontrol;
	 MidiBus vdmxKontrol;
	 
	 PApplet p;

	 public LazerBeatManager beatManager; 
	 
	 public LazerController(PApplet p) {
		 this.p = p;
		 midiState = new int[128]; 
		 mappings = new HashMap<String, Integer>();
		 noteControls = new HashMap<Integer, String>();
		 nanoKontrol = new MidiBus(this, "SLIDER/KNOB", "CTRL", "nanoKontrol");
		 vdmxKontrol = new MidiBus(this, "From VDMX", "To VDMX", "vdmxKontrol");
		 beatManager = new LazerBeatManager(p);
	 }


	 public int get(String mapping) {

	   try {
	     return midiState[mappings.get(mapping)];
	   }
	   catch (Exception e) {
	     return -1;
	   }

	 }


	 public void handleMidiEvent(int channel, int number, int value) {
	   if (number >= 0) {
	     midiState[number] = value;
	   }

	 }

	 public void setNoteControl(String name, int control) {
		 noteControls.put(control, name);
	 }
	 

	 public void setControlValueFromNote(String name, int value) {
	   midiState[mappings.get(name)] = value;
	 }


	 public void setMapping(String name, int control) {
	   mappings.put(name, control);
	   midiState[control] = 0;
	 }

	 public void setMapping(String name, int control, int initialValue) {
	  mappings.put(name, control);
	  midiState[mappings.get(name)] = initialValue;
	 }


	 public void printMappings() {
	   int i = 1;
	   p.pushMatrix();
	   p.pushStyle();
	   p.translate(0, 0);
	   p.fill(0, 0, 0, 80);
	   p.strokeWeight(1);
	   p.stroke(0, 0, 0);
	   p.rect(0, 0, 200, p.height);

	   p.text("Mappings", 10, 10);
	   for (String key : mappings.keySet()) {
	      drawMapping(key, ++i);
	   }

	   p.popStyle();
	   p.popMatrix();


	 }

	public void drawMapping(String key, int i) {

	   int x = 10;
	   int y = (i * 15);

	   p.fill(255, 150, 200, 100);
	   p.rect(x - 1, y-10, this.get(key), 14);
	   p.fill(255, 0, 255);
	   p.text(key + " = " + this.get(key), x, y);
	}
	
	 
	 // listeners 
	public void controllerChange(int channel, int number, int value, long timestamp, String bus_name) {

		if (bus_name == "nanoKontrol") {
			handleMidiEvent(channel, number, value);
		}

		if (bus_name == "vdmxKontrol") {

		}

	}


	public void noteOn(int channel, int pad, int velocity, long timestamp, String bus_name) {
	
		if (channel == VDMX_BEAT) {
			beatManager.beat();
		}
		
		try {
			String mapping = noteControls.get(channel);
			setControlValueFromNote(mapping, pad);
		} catch (Exception e) {
			
		}
	  

	}
}
