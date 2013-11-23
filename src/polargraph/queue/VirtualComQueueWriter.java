package polargraph.queue;

import processing.serial.*;
public class VirtualComQueueWriter extends QueueWriter {

	private String portName = null;
	private Integer portNumber = null;
	private boolean connected = false;
	private boolean running = false;
	
	private Serial port = null;
	private int baudRate = 57600;
	
	public static final Integer NO_PORT = -1;

	/**
	 * Constructor that takes a port NAME, eg "COM-14" or "usb-tty-02". 
	 * 
	 * @param portName
	 */
	public VirtualComQueueWriter(String portName) {
		super();
		this.portName = portName;
		this.portNumber = null;
	}
	/**
	 * Constructor that takes a port number.  The port number is the index in the list of serial
	 * ports.  This is useful when you always want to connect to the first port (for instance),
	 * and don't care about that the port's actual name is.
	 * 
	 * @param portNumber
	 */
	public VirtualComQueueWriter(Integer portNumber) {
		super();
		this.portNumber = portNumber;
		this.portName = null;
	}
	
	public static String[] getPorts() {
		String[] serialPorts = Serial.list();
		System.out.println("Serial ports available on your machine:");
		System.out.println(serialPorts);
		return serialPorts;
	}
	
	private boolean usingPortNumber() {
		return (portNumber != null);
	}
	
	private void connect() {
		try {
			if (this.usingPortNumber()) {
				System.out.println("Get serial port no: "+this.portNumber);
				String portName = getPorts()[portNumber];
			}
			
			port = new Serial(this, portName, getBaudRate());
			//	read bytes into a buffer until you get a linefeed (ASCII 10):
			port.bufferUntil('\n');
			useSerialPortConnection = true;
			println("Successfully connected to port " + portName);
		}
		catch (Exception e) {
			println("Attempting to connect to serial port " 
					+ portName + " in slot " + getSerialPortNumber() 
					+ " caused an exception: " + e.getMessage());
		}
		
		if (this.usingPortNumber()) {
			
		}
	}

	private int getBaudRate() {
		return this.baudRate ;
	}
	public boolean isConnected() {
		return this.connected;
	}
	
	/**
	 * MEthod that starts the queue.  This means that it will send a command every time it's client broadcasts that
	 * it is ready for one.
	 */
	public void run() {
		if (! this.isConnected()) {
			// connect
			this.connect();
		}
		this.running = true;
	}
	
	public void pause() {
		this.running = false;
	}
	
}
