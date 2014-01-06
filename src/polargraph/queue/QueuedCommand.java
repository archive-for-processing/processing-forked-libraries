package polargraph.queue;

import java.util.zip.CRC32;
import java.util.zip.Checksum;

import polargraph.comms.Command;

public class QueuedCommand extends Command {
	private Boolean priority = false;
	private Checksum checksum;
	
	public QueuedCommand(Command c) {
		super(c);
	}
	
	public QueuedCommand promote() {
		this.priority = true;
		return this;
	}
	public Boolean isPriority() {
		return this.isPriority();
	}
	
	public String getAsCommandString() {
		String result = super.getAsCommandString() + ":" + this.checksum.getValue();
		return result;
	}
	
}
