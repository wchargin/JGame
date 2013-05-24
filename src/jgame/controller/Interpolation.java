package jgame.controller;

/**
 * The various types of interpolation for a tween.
 * 
 * @author William Chargin
 * 
 */
public enum Interpolation {

	/**
	 * Indicates that a tween should hold at a constant value until the end
	 * frame is reached. This will cause the tween to function rather like a
	 * {@link jgame.listener.DelayListener DelayListener}.
	 */
	CONSTANT,

	/**
	 * Indicates that a tween should move from the start value to the end value
	 * in a linear fashion.
	 */
	LINEAR,

	/**
	 * Indicates that a tween should accelerate as it moves from the start value
	 * to the end value.
	 */
	EASE_IN,

	/**
	 * Indicates that a tween should decelerate as it moves from the start value
	 * to the end value.
	 */
	EASE_OUT,

	/**
	 * Indicates that a tween should accelerate as it moves from the start value
	 * to the center, and decelerate as it moves from the center to the end.
	 */
	EASE;

	/**
	 * Calculates the required interpolation along a linear path given this
	 * interpolation type.
	 * 
	 * @param tick
	 *            the number of frames elapsed
	 * @param max
	 *            the maximum number of frames
	 * @return the linear-curve interpolation value, as a percentage
	 * @throws IllegalArgumentException
	 *             if {@code max} is less than or equal to zero
	 */
	public double calculateInterpolation(int tick, int max) {
		// Account for abnormalities.
		if (max <= 0) {
			// ABORT
			throw new NullPointerException("max <= 0 : " + max);
		}

		// Determine the percentage.
		double t = ((double) tick) / ((double) max);

		switch (this) {
		case CONSTANT:
			// We're there or we're not.
			return tick >= max ? 1d : 0d;
		case LINEAR:
			// A simple division will do the trick.
			return ((double) tick / (double) max);
		case EASE:
			// The integral of the piecewise function given by:
			//
			// 0, t < 0
			// 4(t), 0 <= t < max/2
			// 4(1 - t), max/2 <= t < max
			// 0, t >= max
			//
			// results in the following:
			if (tick < 0) {
				// 0
				return 0;
			} else if (tick <= max / 2) {
				// 2t^2
				return 2d * Math.pow(t, 2);
			} else if (tick < max) {
				// 4t - 2t^2 - 1
				return 4d * t - 2d * Math.pow(t, 2) - 1d;
			} else {
				return 1;
			}
		case EASE_IN:
			// The integral of 2(t) from t = 0...1 is:
			// t^2
			return Math.pow(t, 2);
		case EASE_OUT:
			// The integral of 2(1-t) from t = 0...1 is:
			// 2t - t^2
			return 2d * t - Math.pow(t, 2);
		default:
			throw new IllegalArgumentException("Unknown interpolation " + this);
		}
	}
}
