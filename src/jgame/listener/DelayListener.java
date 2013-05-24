package jgame.listener;

import jgame.Context;
import jgame.GObject;

/**
 * A listener that will be invoked once after a given number of frames. The
 * listener can be reused by calling {@link #setDelay(int)} with a nonnegative
 * value.
 * 
 * @author William Chargin
 * 
 */
public abstract class DelayListener implements Listener {

	/**
	 * The delay for this listener. When this is zero, the listener will run.
	 */
	private int timer = 0;

	/**
	 * Creates the listener with the given delay.
	 * 
	 * @param delay
	 *            the delay, in frames
	 */
	public DelayListener(int delay) {
		setDelay(delay);
	}

	/**
	 * Gets the current delay.
	 * 
	 * @return the delay, in frames
	 */
	public int getDelay() {
		return timer;
	}

	@Override
	public boolean isValid(GObject target, Context context) {
		final int oldTimer = timer;
		timer = Math.max(-1, --timer);
		return oldTimer == 0;
	}

	/**
	 * Sets the delay.
	 * 
	 * @param delay
	 *            the new delay, in frames
	 */
	public void setDelay(int delay) {
		// Clamp to -1 to avoid underflow.
		this.timer = (delay < 0 ? -1 : delay);
	}

}
