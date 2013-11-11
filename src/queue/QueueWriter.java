package queue;

import java.util.ArrayList;
import java.util.List;

import comms.Command;

public class QueueWriter {
	
	private List<Command> commands = new ArrayList<Command>();
	
	public boolean isReady() {
		return false;
	}
	
	public String sendNextCommand() {
		if (this.isReady())
			return "sent: " + this.commands.get(0).toString();
		else
			return null;
	}
	
	public void add(Command command) {
		commands.add(command);
	}

}
