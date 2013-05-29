package jgame.controller;

import jgame.Context;
import jgame.GObject;

/**
 * A controller that allows for rotational oscillation about an object's anchor
 * point, according to a sinusoidal function.
 * 
 * @author William Chargin
 * 
 */
public class OscillationController implements Controller {

	/**
	 * The center of the controller's oscillation, in degrees. Default is
	 * {@code 0}.
	 */
	private double center = 0;

	/**
	 * The range of the controller's oscillation, in degrees. Default is
	 * {@code 90}.
	 */
	private double range = 90;

	/**
	 * The full period, in frames. Default is {@code 30}.
	 */
	private int period = 30;

	/**
	 * The current time in the period.
	 */
	private int t;

	/**
	 * Creates the controller with the specified center, range, and period.
	 * 
	 * @param center
	 *            the center of oscillation
	 * @param range
	 *            the range of oscillation
	 * @param period
	 *            the period, in frames, of a complete oscillation
	 */
	public OscillationController(double center, double range, int period) {
		super();
		this.center = center;
		this.range = range;
		this.period = period;
	}

	@Override
	public void controlObject(GObject target, Context context) {
		target.setRotation(center
				+ range
				* Math.sin((double) (t = (++t % period)) / (double) period
						* Math.PI * 2));
	}
}
