package funGUI;

import processing.core.*;

/**
 * The Timer class acts like the traditional kitchen timer.
 * You give it an amount of time in milliseconds, and it
 * will go "off" at some time x that you assign it, except
 * it doesn't have a buzzer. It merely helps time some GUI
 * events, and may be the basis for an actual timer
 * graphical interface in the future.
 * @author Audrey Seo
 *
 */
public class Timer {
	private PApplet p;
	private int endtime;
	int dt;

	/**
	 * First constructor for the Timer class.
	 * 
	 * @param end
	 *            int, the amount of time to set the timer to in milliseconds.
	 * @param p
	 *            PApplet parent, usually "this."
	 */
	public Timer(int end, PApplet p) {
		dt = end;
		endtime = 0;
		this.p = p;
	}

	/**
	 * Second constructor for the Timer class.
	 * 
	 * @param p
	 *            PApplet parent, usually "this."
	 */
	public Timer(PApplet p) {
		this.p = p;
		dt = 0;
		endtime = 0;
	}

	/**
	 * Resets the timer back to the beginning, using the amount of time in
	 * milliseconds that was input with the Timer(int end, PApplet p)
	 * constructor.
	 */
	public void reset() {
		endtime = p.millis() + dt;
	}

	/**
	 * Starts the timer all over again. Resets the time when it is done to a
	 * given integer.
	 * 
	 * @param n
	 *            int, the amount of time to set the timer to in milliseconds.
	 */
	public void reset(int n) {
		endtime = p.millis() + n;
	}

	/**
	 * Gives the status of the timer. It returns a boolean based on the amount
	 * of time passed.
	 * 
	 * @return boolean, true if the amount of time has expired, and false if
	 *         there is still time left to go.
	 */
	public boolean done() {
		return (endtime < p.millis());
	}
	
	/**
	 * Notifies the user of the amount of time remaining before the timer is done.
	 * If the time has already elapsed, it returns 0.
	 * @return int, the amount of time in milliseconds left before the time runs out.
	 */
	public int timeLeft() {
		if (p.millis() < endtime)
			return (endtime - p.millis());
		return (0);
	}
	
	/**
	 * Returns the difference between the ending time and the current time.
	 * @return	int, the absolute number of milliseconds elapsed
	 */
	public int absTimeLeft() {
		return(Math.abs(endtime - p.millis()));
	}
}
