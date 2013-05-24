package jgame.controller;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import jgame.Context;
import jgame.GObject;

/**
 * A controller to cause the object to rotate to follow the mouse.
 * 
 * @author William Chargin
 * 
 */
public class MouseRotationController implements Controller {

	/**
	 * The angle shift.
	 */
	private int angleShift = 0;

	/**
	 * The list of previous transforms.
	 */
	private List<Point2D> previousTransforms = new LinkedList<Point2D>();

	/**
	 * The count for moving averages of the rotation transform. A maximum of
	 * this many previous transformations will be averaged to find the proper
	 * rotation. This ensures a smooth transition of rotations. The larger this
	 * number, the smoother the transition, but also the longer it will take. A
	 * value of around 10 (assuming a frame rate of 30 fps) is recommended. This
	 * class uses a simple (unweighted) moving average.
	 * <p>
	 * For more information, consider reading the <a
	 * href="http://en.wikipedia.org/wiki/Moving_average">Wikipedia article on
	 * moving averages</a>.
	 */
	private int movingAverageCount = 10;

	/**
	 * Creates the rotation controller with no angular shift.
	 */
	public MouseRotationController() {
		this(0);
	}

	/**
	 * Creates the rotation controller with the given angular shift.
	 * 
	 * @param angleShift
	 *            the angular shift, in degrees
	 */
	public MouseRotationController(int angleShift) {
		super();
		this.angleShift = angleShift;
	}

	@Override
	public void controlObject(GObject target, Context context) {
		// If the mouse isn't in the screen, we'll exit now.
		if (!context.isMouseInScreen()) {
			return;
		}

		// Store mouse position.
		final Point m = context.getMouseRelative();

		// Define values.
		final Point2D p = target.getAnchorPoint();
		final double x = p.getX();
		final double y = p.getY();

		// Are the mouse position and object coincident?
		if (m.x == x && m.y == y) {
			// Don't want to divide by zero. Plus, nothing to do, really.
			return;
		}

		// Calculate the transform.
		Point2D transform = new Point2D.Double(m.x - x, m.y - y);

		// Add this transform to the list.
		previousTransforms.add(transform);

		// If we're too long...
		if (previousTransforms.size() > getMovingAverageCount()) {
			// ... take off the front.
			previousTransforms.remove(0);
		}

		// Define starting average points.
		double newDx = 0, newDy = 0;

		// Calculate the average transform...
		for (Point2D point : previousTransforms) {
			// Increment totals.
			newDx += point.getX();
			newDy += point.getY();
		}

		// Then rotate.
		target.setRotation(Math.toDegrees(Math.atan2(newDy, newDx))
				+ angleShift);
	}

	/**
	 * Gets the moving average count for the rotation transform.
	 * 
	 * @return the moving average count
	 */
	public int getMovingAverageCount() {
		return movingAverageCount;
	}

	/**
	 * Sets the moving average count for the rotation transform.
	 * 
	 * @param movingAverageCount
	 *            the new moving average count
	 * @throws IllegalArgumentException
	 *             if {@code movingAverageCount <= 0}
	 * @see #movingAverageCount
	 */
	public void setMovingAverageCount(int movingAverageCount)
			throws IllegalArgumentException {
		// Make sure it's positive.
		if (movingAverageCount <= 0) {
			// It's not positive.
			throw new IllegalArgumentException("movingAverageCount <= 0 ("
					+ movingAverageCount + ")");
		}
		this.movingAverageCount = movingAverageCount;
	}
}
