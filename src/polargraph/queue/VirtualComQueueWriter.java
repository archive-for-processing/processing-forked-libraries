package polargraph.queue;

import geomerative.RPoint;

import java.util.Iterator;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import polargraph.Polargraph;
import polargraph.comms.Command;
import polargraph.comms.IncomingEventHandler;
import processing.serial.Serial;
public class VirtualComQueueWriter extends QueueWriter {

	private boolean connected = false;
	private boolean sending = false;
	
	private Polargraph parentMachine = null;
	
	private Serial port = null;
	private boolean drawbotReady = false;
	private IncomingEventHandler incomingEventHandler = null;
	
	/**
	 * Constructor that takes a serial port as an argument.
	 * 
	 * @param portNumber
	 */
	public VirtualComQueueWriter(Serial port) {
		super();
		this.port = port;
	}
	
	public VirtualComQueueWriter() {
		super();
		this.port = null;
	}
	
	public void addParentMachine(Polargraph m) {
		this.parentMachine = m;
	}
	
	/**
	 * MEthod that starts the queue.  This means that it will send a command every time it's client broadcasts that
	 * it is ready for one.
	 */
	public void run() {
		this.sending = true;
	}
	
	public void pause() {
		this.sending = false;
	}

	static public Checksum buildChecksum(String command) {
		Checksum crc = new CRC32();
	    crc.update(command.getBytes(), 0, command.length());
	    return crc;
	}
	
	public IncomingEventHandler getIncomingEventHandler() {
		if (this.incomingEventHandler == null)
			this.incomingEventHandler = new IncomingEventHandler(this.parentMachine);
		return this.incomingEventHandler;
	}
	public void handleIncomingEvent() 
	{
		String incoming = this.port.readStringUntil('\n');
		if (incoming != null) {
			this.port.clear();
			this.getIncomingEventHandler().handle(incoming);
		}
	}
//
//	void dispatchCommandQueue()
//	{
//	  if (isDrawbotReady() 
//	    && (!commandQueue.isEmpty() || !realtimeCommandQueue.isEmpty())
//	    && commandQueueRunning)
//	  {
//	    if (pixelTimerRunning)
//	    {
//	      timeLastPixelStarted = new Date();
//	      numberOfPixelsCompleted++;
//	    }
//
//	    if (!realtimeCommandQueue.isEmpty())
//	    {
//	      String command = realtimeCommandQueue.get(0);
//	      lastCommand = command;
//	      realtimeCommandQueue.remove(0);
//	      println("Dispatching PRIORITY command: " + command);
//	    }
//	    else
//	    {
//	      String command = commandQueue.get(0);
//	      lastCommand = command;
//	      commandQueue.remove(0);
//	      println("Dispatching command: " + command);
//	    }
//
//	    println("Last command:" + lastCommand);
//	    myPort.write(lastCommand);
//	    drawbotReady = false;
//	  }
//	  else if (commandQueue.isEmpty())
//	  {
//	    stopPixelTimer();
//	  }  
//	}	
	
	
}
