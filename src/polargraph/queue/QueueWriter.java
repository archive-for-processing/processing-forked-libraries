package polargraph.queue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

import polargraph.comms.Command;

/**
 * This class has most of the functionality of the command queue system.  It rolls up a pair of queues,
 * one regular, and one priority that will always be promoted
 * @author sandy_000
 *
 */
public class QueueWriter implements Runnable {
	
	private List<QueuedCommand> queue = new ArrayList<QueuedCommand>(1000);
	private Command current = null;
	private List<Command> history = new ArrayList<Command>(1000);
	
	// iterators for the queues, to allow this class to be iterator-like
	private Iterator<QueuedCommand> qit = queue.iterator();
	
	public boolean isReady() {
		return false;
	}
	
	public void add(Command command) {
		queue.add(new QueuedCommand(command));
	}

	public Iterable<QueuedCommand> iterator() {
		System.out.println("Hello");
		return this.queue;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}


}
