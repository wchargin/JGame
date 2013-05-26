package jgame.controller;

import java.awt.geom.Point2D;

import jgame.Context;
import jgame.GObject;

/**
 * A controller that controls an objects location based on keyboard input
 * defined by the given {@link ControlScheme}.
 * 
 * @author William Chargin
 * 
 */
public class KeyboardLocationController extends AbstractLocRotController {

	/**
	 * The control scheme for this keyboard controller.
	 */
	private final ControlScheme controlScheme;

	/**
	 * Whether this controller allows vertical movement.
	 */
	private boolean verticalAllowed = true;

	/**
	 * Whether this controller allows horizontal movement.
	 */
	private boolean horizontalAllowed = true;

	/**
	 * Creates the controller with the given control scheme.
	 * 
	 * @param controlScheme
	 *            the control scheme to use
	 * @param speed
	 *            the speed at which to move (in px/frame)
	 */
	public KeyboardLocationController(ControlScheme controlScheme, double speed) {
		super();
		this.controlScheme = controlScheme;
		setMaxSpeed(speed);
	}

	/**
	 * Creates the controller with the given parameters.
	 * 
	 * @param controlScheme
	 *            the control scheme to use
	 * @param speed
	 *            the speed at which to move (in px/frame)
	 * @param rotateToFollow
	 *            whether the controller should rotate the object to follow the
	 *            path
	 * @param rotationOffset
	 *            the rotation offset (default is {@code 0})
	 */
	public KeyboardLocationController(ControlScheme controlScheme,
			double speed, boolean rotateToFollow, int rotationOffset) {
		super(rotateToFollow, rotationOffset);
		this.controlScheme = controlScheme;
		setMaxSpeed(speed);
	}

	@Override
	protected Point2D calculateControl(GObject target, Context context) {
		// Create variables to check for horizontal and vertical movement.
		int horizontal = 0;
		int vertical = 0;

		// Check the context for relevant keys.
		for (Integer i : context.getKeyCodesPressed()) {
			// Check the code.

			if (i.equals(controlScheme.up)) {
				// We want to go up. That's -Y.
				vertical--;
			}
			if (i.equals(controlScheme.dn)) {
				// We want to go down. That's +Y.
				vertical++;
			}
			if (i.equals(controlScheme.rt)) {
				// We want to go right. That's +X.
				horizontal++;
			}
			if (i.equals(controlScheme.lt)) {
				// We want to go left. That's -X.
				horizontal--;
			}
		}

		// Follow the rules.
		if (!verticalAllowed) {
			vertical = 0;
		}
		if (!horizontalAllowed) {
			horizontal = 0;
		}

		// Find the speed.
		double speed = getMaxSpeed();

		// Are we in both directions?
		if (horizontal > 0 && vertical > 0) {
			// We want to compensate for that. Pythagoras's theorem gives us:
			speed /= Math.sqrt(2);
		}

		return new Point2D.Double(speed * horizontal, speed * vertical);
	}

	@Override
	protected boolean canControl(GObject target, Context context) {
		return true;
	}

	/**
	 * Determines whether this controller allows horizontal movement.
	 * 
	 * @return {@code true} if this controller allows horizontal movement, or
	 *         {@code false} if it does not
	 */
	public boolean isHorizontalAllowed() {
		return horizontalAllowed;
	}

	/**
	 * Determines whether this controller allows vertical movement.
	 * 
	 * @return {@code true} if this controller allows vertical movement, or
	 *         {@code false} if it does not
	 */
	public boolean isVerticalAllowed() {
		return verticalAllowed;
	}

	/**
	 * Sets whether this controller allows horizontal movement.
	 * 
	 * @param horizontal
	 *            {@code true} if this controller should allow horizontal
	 *            movement, or {@code false} if it should not
	 */
	public void setHorizontalAllowed(boolean horizontal) {
		this.horizontalAllowed = horizontal;
	}

	/**
	 * Sets whether this controller allows vertical movement.
	 * 
	 * @param vertical
	 *            {@code true} if this controller should allow vertical
	 *            movement, or {@code false} if it should not
	 */
	public void setVerticalAllowed(boolean vertical) {
		this.verticalAllowed = vertical;
	}
}
