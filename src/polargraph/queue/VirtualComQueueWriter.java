package polargraph.queue;

import processing.serial.Serial;
public class VirtualComQueueWriter extends QueueWriter {

	private boolean connected = false;
	private boolean running = false;
	
	private Serial port = null;
	
	public static final Integer NO_PORT = -1;

	/**
	 * Constructor that takes a serial port as an argument.
	 * 
	 * @param portNumber
	 */
	public VirtualComQueueWriter(Serial port) {
		super();
		this.port = port;
	}
	
	/**
	 * MEthod that starts the queue.  This means that it will send a command every time it's client broadcasts that
	 * it is ready for one.
	 */
	@Override
	public void run() {
		this.running = true;
	}
	
	public void pause() {
		this.running = false;
	}
	
}
