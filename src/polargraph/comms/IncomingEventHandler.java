package polargraph.comms;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import geomerative.RPoint;
import polargraph.Polargraph;

/**
 * This is a bit of a utility class, but it rolls up all of the code that deals with
 * decoding the incoming events. These are things like READY, SYNC, PGx etc.
 * 
 * Most of these events require an interaction with the machine itself, writing values
 * back, so for that reason, this class takes an instance of the machine as a parameter
 * in it's constructor.
 * 
 * @author Sandy Noble
 */
public class IncomingEventHandler {
	private static final int HISTORY_MAX_SIZE = 200;
	private Polargraph machine = null;
	private Map<String, SortedMap<Date, String>> history =  new HashMap<String, SortedMap<Date, String>>(13);
	
	public IncomingEventHandler(Polargraph machine) {
		this.machine = machine;
	}
	
	public Polargraph getMachine() {
		return this.machine;
	}

	public IncomingEventHandler handle(String incoming) {
		
		// if you got any bytes other than the linefeed:
		incoming = incoming.trim();
		System.out.println("Incoming: " + incoming);
	  
		if (incoming.startsWith("READY"))
		{
			this.addToHistory("READY", incoming);
			Integer hardwareVersion = this.parseHardwareVersionFromReady(incoming);
			this.getMachine().setHardwareVersion(hardwareVersion);
		}
		else if (incoming.startsWith("SYNC")) {
			this.addToHistory("SYNC", incoming);
			getMachine().getTool().setPosition(this.parseSync(incoming));
		}
	//  else if (incoming.startsWith("CARTESIAN"))
	//    readCartesianMachinePosition(incoming);
	//  else if (incoming.startsWith("PGNAME"))
	//    readMachineName(incoming);
	//  else if (incoming.startsWith("PGSIZE"))
	//    readMachineSize(incoming);
	//  else if (incoming.startsWith("PGMMPERREV"))
	//    readMmPerRev(incoming);
	//  else if (incoming.startsWith("PGSTEPSPERREV"))
	//    readStepsPerRev(incoming);
	//  else if (incoming.startsWith("PGSTEPMULTIPLIER"))
	//    readStepMultiplier(incoming);
	//  else if (incoming.startsWith("PGLIFT"))
	//    readPenLiftRange(incoming);
	//  else if (incoming.startsWith("PGSPEED"))
	//    readMachineSpeed(incoming);
	//    
	//  else if ("RESEND".equals(incoming))
	//    resendLastCommand();
	//  else if ("DRAWING".equals(incoming))
	//    drawbotReady = false;
	//  else if (incoming.startsWith("MEMORY"))
	//    extractMemoryUsage(incoming);
	//
	//  if (drawbotReady)
	//    drawbotConnected = true;
	  
		return this;
	}
	/**
	 * Keeps a history of the incoming events, in a couple of sorted treemaps, datestamped.
	 * It should limit the size of the collections to the value of HISTORY_MAX_SIZE 
	 * automatically.
	 * 
	 * @param key the "type" of message, eg READY, or SYNC, or "UNKNOWN"
	 * @param incoming the value to store in the history
	 */
	private void addToHistory(String key, String incoming) {
		if (!this.history.containsKey(key)) {
			this.history.put(key, new TreeMap<Date, String>());
		}
		System.out.println("Historicising " + key);
		this.history.get(key).put(new Date(), incoming);
		
		// remove the first key if the collection is getting too big
		if (this.history.get(key).size() > HISTORY_MAX_SIZE) {
			this.history.get(key).remove(this.history.get(key).firstKey());
		}
	}

	/**
	 * Extracts the hardware version message from the regular "READY" transmission from 
	 * the polargraph machine, returns it as an Integer.
	 * 
	 * @param readyString eg something like "READY_200"
	 */
	public Integer parseHardwareVersionFromReady(String readyString)
	{
		Integer newHardwareVersion = Polargraph.HARDWARE_VER_UNO;
		if ("READY".equals(readyString))
		{
			newHardwareVersion = Polargraph.HARDWARE_VER_UNO;
		}
		else
		{
			String ver = readyString.substring(6);
			Integer verInt = Polargraph.HARDWARE_VER_UNO;
			try
			{
				verInt = Integer.parseInt(ver);
			}
			catch (NumberFormatException nfe)
			{
				System.out.println("Bad format for hardware version - defaulting to ATMEGA328 (Uno)");
				verInt = Polargraph.HARDWARE_VER_UNO;
			}

			if (Polargraph.HARDWARE_VER_MEGA == verInt || Polargraph.HARDWARE_VER_MEGA_POLARSHIELD == verInt)
				newHardwareVersion = verInt;
			else
				newHardwareVersion = Polargraph.HARDWARE_VER_UNO;
		}
		return newHardwareVersion;
	}
	/**
	 * Splits and parses a SYNC message, converts it into a RPoint.
	 * 
	 * @param incoming
	 * @return RPoint object 
	 */
	private RPoint parseSync(String incoming) {
		String[] splitted = incoming.split(",");
		if (splitted.length == 4)
		{
			String currentAPos = splitted[1];
			String currentBPos = splitted[2];
			Double a = Double.valueOf(currentAPos);
			Double b = Double.valueOf(currentBPos);
			RPoint rp = new RPoint(a, b);
			rp = this.getMachine().convertToCartesian(rp);
			return rp;
		}
		else
			throw new IllegalArgumentException("Incoming did not contain a proper machine position reference: " +incoming);
	}
	

}
