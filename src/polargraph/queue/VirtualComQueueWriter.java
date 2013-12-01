package polargraph.queue;

import java.util.Iterator;

import polargraph.comms.Command;
import processing.serial.Serial;
public class VirtualComQueueWriter extends QueueWriter {

	private boolean connected = false;
	private boolean sending = false;
	
	private Serial port = null;
	
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

	@Override
	public Iterator<Command> iterator() {
		return super.iterator();
	}
	
}
