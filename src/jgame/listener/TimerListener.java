package jgame.listener;

import jgame.Context;
import jgame.GObject;

/**
 * A listener that is valid periodically, after a certain number of frames.
 * 
 * @author William Chargin
 * 
 */
public abstract class TimerListener implements Listener {

	/**
	 * The delay interval for this timer.
	 */
	private int interval = 30;

	/**
	 * The current timer value.
	 */
	private int t = 0;

	/**
	 * Creates the timer with the given delay interval.
	 * 
	 * @param interval
	 *            the delay interval
	 * @throws IllegalArgumentException
	 *             if {@code interval} is less than {@code 0}; see
	 *             {@link #setInterval(int)}
	 */
	public TimerListener(int interval) throws IllegalArgumentException {
		super();
		setInterval(interval);
	}

	/**
	 * Gets the interval for the delay for this timer.
	 * 
	 * @return the interval
	 */
	public int getInterval() {
		return interval;
	}

	@Override
	public boolean isValid(GObject target, Context context) {
		return (t = (++t % interval)) == 1;
	}

	/**
	 * Resets this timer. It will be valid next frame.
	 */
	public void reset() {
		t = 0;
	}

	/**
	 * Sets the interval for delay for this timer.
	 * 
	 * @param interval
	 *            the new interval, in frames
	 * @throws IllegalArgumentException
	 *             if {@code interval} is less than {@code 0}
	 */
	public void setInterval(int interval) throws IllegalArgumentException {
		// Perform a bounds check.
		if (interval < 0) {
			// Invalid.
			throw new IllegalArgumentException("interval < 0 : " + interval);
		}

		// Assign.
		this.interval = interval;
	}

}
