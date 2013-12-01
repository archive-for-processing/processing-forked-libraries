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
public class QueueWriter implements Runnable, Iterator<Command>, Iterable<Command> {
	
	private Queue<QueuedCommand> queue = new ArrayDeque<QueuedCommand>(1000);
	private Queue<QueuedCommand> priorityQueue = new ArrayDeque<QueuedCommand>(20);
	private Command current = null;
	private Queue<Command> history = new ArrayDeque<Command>(1000);
	
	// iterators for the queues, to allow this class to be iterator-like
	private Iterator<QueuedCommand> qit = queue.iterator();
	private Iterator<QueuedCommand> pqit = priorityQueue.iterator();
	private boolean currentIteratorIsPqit = true;
	
	public boolean isReady() {
		return false;
	}
	
	/**
	 * Returns true if there is a command waiting in a queue.
	 * This command also sets the flag that controls which iterator is current.
	 * @return
	 */
	public boolean hasNext() {
		System.out.println("hasnext");
		if (qit.hasNext()){
			this.currentIteratorIsPqit = false;
			return true;
		}
		else if (pqit.hasNext()) {
			this.currentIteratorIsPqit = true;
			return true;
		}
		else 
			return false;
	}
	
	public Command next() {
		Command result = (this.currentIteratorIsPqit) ?	result = pqit.next() : qit.next();
		this.hasNext();
		return result;
	}
	@Override
	public void remove() {
		if (this.currentIteratorIsPqit) 
			this.pqit.remove();
		else
			this.qit.remove();
	}
	
	public void add(Command command) {
		queue.add(new QueuedCommand(command));
	}
	public void addPriority(Command command) {
		priorityQueue.add(new QueuedCommand(command).promote());
	}

	@Override
	public Iterator<Command> iterator() {
		System.out.println("Hello");
		return this;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}


}
