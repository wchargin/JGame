package jgame.controller;

import jgame.Context;
import jgame.GObject;

/**
 * A controller that causes an object to pulsate according to a sine curve.
 * 
 * @author William Chargin
 * 
 */
public class PulsateController extends AbstractIntensityController {

	/**
	 * The center of the controller's pulsation. Default is {@code 0.5}.
	 */
	private double center = 0.5;

	/**
	 * The range of the controller's pulsation. Default is {@code 0.25}.
	 */
	private double range = 0.25;

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
	public PulsateController(double center, double range, int period) {
		super();
		this.center = center;
		this.range = range;
		this.period = period;
	}

	@Override
	public double getFactor(GObject target, Context context) {
		return center
				+ range
				* Math.sin((double) (t = (++t % period)) / (double) period
						* Math.PI * 2);
	}

}
