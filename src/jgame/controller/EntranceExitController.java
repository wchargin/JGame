package jgame.controller;

import jgame.Context;
import jgame.GObject;

/**
 * A controller that changes an object's alpha at a constant rate.
 * 
 * @author William Chargin
 * 
 */
public class EntranceExitController extends AbstractIntensityController {

	/**
	 * Creates a fade-in controller that adds the specified amount of property
	 * each frame.
	 * 
	 * @param perFrame
	 *            the amount of property to add per frame
	 * @return the controller
	 */
	public static EntranceExitController createEntranceController(
			double perFrame) {
		return new EntranceExitController(perFrame);
	}

	/**
	 * Creates a fade-out controller that removes the specified amount of
	 * property per frame.
	 * 
	 * @param perFrame
	 *            the amount of property to remove per frame
	 * @return the controller
	 */
	public static EntranceExitController createExitController(double perFrame) {
		return new EntranceExitController(-perFrame);
	}

	/**
	 * The amount of alpha to change after each frame.
	 */
	private double perFrame;

	/**
	 * Creates the controller with the given amount of property per frame.
	 * 
	 * @param perFrame
	 *            the channel displacement
	 */
	private EntranceExitController(double perFrame) {
		super();
		this.perFrame = perFrame;
	}

	@Override
	public double getFactor(GObject target, Context context) {
		return target.getAlpha() + perFrame;
	}

}
