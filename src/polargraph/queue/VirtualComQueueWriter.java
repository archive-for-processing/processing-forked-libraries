package polargraph.queue;

public class VirtualComQueueWriter extends QueueWriter {

	private String portName;

	public VirtualComQueueWriter(String portName) {
		this.portName = portName;
	}
	
}
