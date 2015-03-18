package lazer.viz;

import toxi.util.events.*;
import processing.core.*;

public class LazerBeatManager {
	EventDispatcher<LazerBeatListener> listeners = new EventDispatcher<LazerBeatListener>();

	int count = 0;
	int msecsFirst = 0;
	int msecsPrevious = 0;

	LazerBeatThread b;

	private PApplet p;

	public LazerBeatManager(PApplet p) {
		b = new LazerBeatThread(this, p.millis(), 60000/120);
		this.p = p;
	}


	public void resetCount() {
		count = 0;
	}

	public void start() {
		b.start();
	}

	public void setBeat() {
		b.stopit();
		int msecs = p.millis();


		if ((msecs - msecsPrevious) > 2000) {
			resetCount();
		}

		if (count == 0) {
			msecsFirst = msecs;
			count = 1;
		} else {

			int bpmAvg = 60000 * count / (msecs - msecsFirst);

			b = new LazerBeatThread(this, msecs, 60000 / bpmAvg);
			b.start();
			count++;
		}


		msecsPrevious = msecs;

	}
	
	
	public void register(LazerBeatListener l) {
		listeners.addListener(l);
	}

	//trigger
	public void broadcastEvent(LazerBeatThread t) {
		for (LazerBeatListener l : listeners) {
			l.beat();
		}
	}

	public void beat() {
		for (LazerBeatListener l : listeners) {
			l.beat();
		}
	}



}

