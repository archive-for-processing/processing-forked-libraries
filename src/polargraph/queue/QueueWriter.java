package polargraph.queue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

import polargraph.comms.Command;

public class QueueWriter {
	
	private Queue<Command> queue = new ArrayDeque<Command>(1000);
	private Queue<Command> priorityQueue = new ArrayDeque<Command>(20);
	private Command current = null;
	private Queue<Command> history = new ArrayDeque<Command>(1000);
	
	public boolean isReady() {
		return false;
	}
	
	/**
	 * Returns true if there is a command waiting in a queue.
	 * @return
	 */
	public boolean hasNext() {
		return ! (queue.isEmpty() && priorityQueue.isEmpty());
	}
	/**
	 * Interesting method that removes a command from the top of either queue, puts it into
	 * the current command variable, and also pushes it onto the history queue.
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public Command next() throws NoSuchElementException {
		Command next = null;
		if (priorityQueue.isEmpty()) 
			next = queue.remove();
		else
			next = priorityQueue.remove();
		
		this.current = next;
		this.history.add(this.current);
		return this.current;
	}
	
	public String sendCommand() {
		if (this.hasNext()) {
			this.next();
			return "sent: " + this.current.toString();
		}
		else
			return null;
	}

	public void add(Command command) {
		queue.add(command);
	}
	public void addPriority(Command command) {
		priorityQueue.add(command);
	}

}
