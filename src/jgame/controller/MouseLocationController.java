package jgame.controller;

import java.awt.Point;
import java.awt.geom.Point2D;

import jgame.Context;
import jgame.GObject;

/**
 * A simple controller that causes the object to follow the mouse.
 * 
 * @author William Chargin
 * 
 */
public class MouseLocationController extends AbstractLocRotController {

	/**
	 * Whether the control should be limited to the bounds of the container.
	 */
	private boolean limitToBounds = true;

	/**
	 * Creates the controller with the default settings.
	 */
	public MouseLocationController() {
		super();
	}

	/**
	 * Creates the controller with the specified rotation settings.
	 * 
	 * @param rotateToFollow
	 *            whether the controller should rotate the object to follow the
	 *            path
	 * @param rotationOffset
	 *            the rotation offset (default is {@code 0})
	 */
	public MouseLocationController(boolean rotateToFollow, int rotationOffset) {
		super(rotateToFollow, rotationOffset);
	}

	@Override
	protected Point2D calculateControl(GObject target, Context context) {
		// Set up a convenience variable.
		final Point m = context.getMouseRelative();

		// Get the anchor point.
		Point2D anchor = target.getAnchorPoint();

		// The anchor point offset (O) is:
		// O_x = anchor.x - target.x
		// O_y = anchor.y - target.y

		// Set up targets.
		double tx = m.x - (anchor.getX() - target.getX());
		double ty = m.y - (anchor.getY() - target.getY());

		// Set up displacement trackers for later.
		double dx = 0, dy = 0;

		// Are we there already?
		if (target.getX() == tx && target.getY() == ty) {
			return new Point(0, 0);
		}

		// Find the distance of travel.
		double distance = new Point2D.Double(tx, ty).distance(target.getX(),
				target.getY());

		// Are we allowed to "snap"?
		if (distance <= getMaxSpeed() || getMaxSpeed() == -1) {
			// Yes.
			return new Point2D.Double(tx - target.getX(), ty - target.getY());
		} else {
			// We'll have to go in that direction.
			// Find the angle of travel.
			double angle = Math.atan2(ty - target.getY(), tx - target.getX());

			// Set displacements with polar coordinate transforms.
			dx = getMaxSpeed() * Math.cos(angle);
			dy = getMaxSpeed() * Math.sin(angle);

			return new Point2D.Double(dx, dy);
		}
	}

	@Override
	protected boolean canControl(GObject target, Context context) {
		return context.isMouseInScreen()
				&& ((limitToBounds && target.hasParent()) ? target.getParent()
						.getTransformedBoundingShape()
						.contains(context.getMouseAbsolute()) : true);
	}

	/**
	 * Determines whether the control is limited to the bounds of the container.
	 * 
	 * @return {@code true} if it is limited, or {@code false} if it is not
	 */
	public boolean isLimitToBounds() {
		return limitToBounds;
	}

	/**
	 * Sets whether the control is limited to the bounds of the container.
	 * 
	 * @param limitToBounds
	 *            {@code true} if it should be limited, or {@code false} if it
	 *            should not
	 */
	public void setLimitToBounds(boolean limitToBounds) {
		this.limitToBounds = limitToBounds;
	}
}
