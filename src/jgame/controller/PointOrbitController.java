package jgame.controller;

import java.awt.Point;
import java.awt.geom.Point2D;

import jgame.Context;
import jgame.GObject;

/**
 * A controller that controls an object's location with polar coordinates with
 * the origin at a specified point or the mouse pointer. The keys of the
 * {@link ControlScheme} are mapped as follows:
 * 
 * <ul>
 * <li>{@link ControlScheme#up} &ndash; radially inward, toward the mouse
 * pointer</li>
 * <li>{@link ControlScheme#dn} &ndash; radially outward, away from the mouse
 * pointer</li>
 * <li>{@link ControlScheme#lt} &ndash; clockwise</li>
 * <li>{@link ControlScheme#rt} &ndash; counterclockwise</li>
 * </ul>
 * 
 * <strong>Note</strong>: This controller will <em>not</em> rotate the object to
 * follow the mouse cursor, and calls to {@link #setRotateToFollow(boolean)}
 * will have no effect. It is recommended that you add a
 * {@link MouseRotationController} for that purpose.
 * 
 * @author William Chargin
 * 
 */
public class PointOrbitController extends AbstractLocRotController {

	/**
	 * The control scheme for this keyboard controller.
	 */
	private final ControlScheme controlScheme;

	/**
	 * The point to orbit, or {@code null} to indicate the mouse should be
	 * orbited.
	 */
	private Point2D center;

	/**
	 * Creates the controller with the given control scheme. This controller
	 * will orbit around the mouse pointer.
	 * 
	 * @param controlScheme
	 *            the control scheme to use
	 * @param speed
	 *            the speed at which to move (in px/frame)
	 */
	public PointOrbitController(ControlScheme controlScheme, double speed) {
		this(controlScheme, speed, null);
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
	public PointOrbitController(ControlScheme controlScheme, double speed,
			boolean rotateToFollow, int rotationOffset) {
		super(rotateToFollow, rotationOffset);
		this.controlScheme = controlScheme;
		setMaxSpeed(speed);
	}

	/**
	 * Creates the controller with the given control scheme, orbiting around the
	 * given point.
	 * 
	 * @param controlScheme
	 *            the control scheme to use
	 * @param speed
	 *            the speed at which to move (in px/frame)
	 * @param center
	 *            the point to orbit
	 */
	public PointOrbitController(ControlScheme controlScheme, double speed,
			Point center) {
		super();
		this.controlScheme = controlScheme;
		this.center = center;
		setMaxSpeed(speed);
	}

	@Override
	protected Point2D calculateControl(GObject target, Context context) {
		// Create variables to check for horizontal and vertical movement.
		int orbit = 0;
		int radial = 0;

		// Check the context for relevant keys.
		for (Integer i : context.getKeyCodesPressed()) {
			// Check the code.

			if (((Integer) controlScheme.up).equals(i)) {
				// We want to go in. That's -r.
				radial--;
			}
			if (((Integer) controlScheme.dn).equals(i)) {
				// We want to go out. That's +r.
				radial++;
			}
			if (((Integer) controlScheme.rt).equals(i)) {
				// That's +theta.
				orbit++;
			}
			if (((Integer) controlScheme.lt).equals(i)) {
				// That's -theta.
				orbit--;
			}
		}

		// Now we calculate. Start with the center location.
		Point2D pole = center == null ? context.getMouseRelative() : center;

		// First, can we move?
		final double anchorX = target.getAnchorPoint().getX();
		final double anchorY = target.getAnchorPoint().getY();
		if (!pole.equals(new Point2D.Double(anchorX, anchorY))) {

			// Calculate target transform in coordinates.
			final double tx = pole.getX() - anchorX;
			final double ty = pole.getY() - anchorY;

			// Can we jump?
			if (pole.distance(anchorX, anchorY) <= getMaxSpeed()) {
				// Do so.
				// However, actually jumping screws up rotation with
				// MouseRotationController.
				// Call it "close enough."
				return new Point2D.Double(0, 0);
			}

			// Find current radius.
			double r = pole.distance(anchorX, anchorY);
			double r_0 = r;

			// Find current theta.
			double theta = Math.atan2(ty, tx);
			double theta_0 = theta;

			// Determine the speed.
			double speed = getMaxSpeed();

			// If we're moving radially AND orbitally, we'll have to split that.
			if (radial != 0 && orbit != 0) {
				// Because radial and polar axes are, at infinitesimal levels,
				// always tangent, we can use the square root of two as our
				// factor (from 45-45-90 theorem).
				speed /= Math.sqrt(2);
			}

			// Move radially.
			r += radial * speed;

			// Orbit.
			// We want to move as much as we can while not exceeding the speed.
			// The distance we travel for an angle displacement dtheta is
			// Circumference * (dtheta / a full circle)
			// = 2*pi*r * dtheta / (2*pi)
			// = r * dtheta
			// We want this to be the speed, so
			// speed = r * dtheta
			// dtheta = speed / r
			theta += orbit * (speed / r);

			// Calculate back to cartesian.
			double dx = r_0 * Math.cos(theta_0) - r * Math.cos(theta);
			double dy = r_0 * Math.sin(theta_0) - r * Math.sin(theta);

			// Calculate back to cartesian.
			return new Point2D.Double(dx, dy);
		} else {
			return new Point2D.Double(0, 0);
		}
	}

	@Override
	protected boolean canControl(GObject target, Context context) {
		return context.isMouseInScreen();
	}

	/**
	 * Gets the center of the polar coordinate system used by this controller.
	 * 
	 * @return the center, or {@code null} to indicate the mouse pointer
	 */
	public Point2D getCenter() {
		return center;
	}

	@Override
	public boolean isRotateToFollow() {
		return false;
	}

	/**
	 * Sets the center of the polar coordinate system used by this controller.
	 * 
	 * @param center
	 *            the new center, or {@code null} to indicate the mouse pointer
	 */
	public void setCenter(Point2D center) {
		this.center = center;
	}
}
