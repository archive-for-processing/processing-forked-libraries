package polargraph.comms;

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
	private Polargraph machine = null;
	
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
			System.out.println("Dealing with READY.");
			Integer hardwareVersion = this.getMachine().decodeHardwareVersionFromReady(incoming);
			this.getMachine().setHardwareVersion(hardwareVersion);
		}
		else if (incoming.startsWith("SYNC")) {
			System.out.println("Dealing with SYNC.");
			getMachine().getTool().setPosition(this.unpackSync(incoming));
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
	private RPoint unpackSync(String incoming) {
		String[] splitted = incoming.split(",");
		if (splitted.length == 4)
		{
			String currentAPos = splitted[1];
			String currentBPos = splitted[2];
			Double a = Double.valueOf(currentAPos);
			Double b = Double.valueOf(currentBPos);
			RPoint rp = new RPoint(a, b);
			return rp;
		}
		else
			throw new IllegalArgumentException("Incoming did not contain a proper machine position reference: " +incoming);
	}
	

}
