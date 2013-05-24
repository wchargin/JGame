package jgame.controller;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import jgame.Context;
import jgame.GObject;

/**
 * An abstract controller that allows both movement and rotation to follow the
 * path tangent as a moving average. Subclasses need only define the desired
 * displacement, and this class will handle the rest of the movement.
 * 
 * @author William Chargin
 * 
 */
public abstract class AbstractLocRotController implements Controller {
	/**
	 * The maximum speed. A value of {@code -1} indicates unbounded speed.
	 */
	private double maxSpeed = -1;

	/**
	 * Whether to rotate the object to follow the cursor.
	 */
	private boolean rotateToFollow;

	/**
	 * The rotation offset. This has no effect if {@link #rotateToFollow} is
	 * disabled.
	 */
	private double rotationOffset;

	/**
	 * The list of previous transforms.
	 */
	private List<Point2D> previousTransforms = new LinkedList<Point2D>();

	/**
	 * The count for moving averages of the rotation transform. A maximum of
	 * this many previous transformations will be averaged to find the proper
	 * rotation. This ensures a smooth transition of rotations. The larger this
	 * number, the smoother the transition, but also the longer it will take. A
	 * value of around 15 (assuming a frame rate of 30 fps) is recommended. This
	 * class uses a simple (unweighted) moving average.
	 * <p>
	 * For more information, consider reading the <a
	 * href="http://en.wikipedia.org/wiki/Moving_average">Wikipedia article on
	 * moving averages</a>.
	 */
	private int movingAverageCount = 15;

	/**
	 * Creates the controller with the default settings.
	 */
	public AbstractLocRotController() {
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
	public AbstractLocRotController(boolean rotateToFollow, int rotationOffset) {
		setRotateToFollow(true);
		setRotationOffset(rotationOffset);
	}

	/**
	 * Calculates the required control the given object with the given context.
	 * This method will only be invoked after an immediately preceding call to
	 * {@link #canControl(GObject, Context)} with the same parameters, and only
	 * if said method call returns {@code true}.
	 * 
	 * @param target
	 *            the object to control
	 * @param context
	 *            the context
	 * @return a point representing the displacement as a result of the
	 *         transform in the form {@code new Point(dx, dy)}, where {@code dx}
	 *         is the displacement in the positive x direction and {@code dy} is
	 *         the displacement in the positive y direction
	 */
	protected abstract Point2D calculateControl(GObject target, Context context);

	/**
	 * Determines whether the given target can be controlled in the given
	 * context.
	 * 
	 * @param target
	 *            the target
	 * @param context
	 *            the context
	 * @return {@code true} if the target can be controlled, or {@code false} if
	 *         it cannot
	 */
	protected abstract boolean canControl(GObject target, Context context);

	@Override
	public void controlObject(GObject target, Context context) {
		// Can we control the target?
		if (!canControl(target, context)) {
			previousTransforms.clear();
			return;
		}

		// Perform the calculation.
		// (This is an abstract method delegated to subclasses.)
		Point2D transform = calculateControl(target, context);

		// Create variables.
		final double dx = transform.getX();
		final double dy = transform.getY();

		// Move the object.
		target.setX(target.getX() + dx);
		target.setY(target.getY() + dy);

		// Did we move, and should we rotate?
		if (isRotateToFollow() && (dx != 0 || dy != 0)) {

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
					+ rotationOffset);
		}
	}

	/**
	 * Gets the maximum speed.
	 * 
	 * @return the maximum speed, or {@code -1} to indicate unbounded speed
	 */
	public double getMaxSpeed() {
		return maxSpeed;
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
	 * Gets the offset for the rotation. This will have no effect if
	 * {@link #rotateToFollow} is disabled.
	 * 
	 * @return the offset
	 */
	public double getRotationOffset() {
		return rotationOffset;
	}

	/**
	 * Determines whether the controller will cause the object to rotate to
	 * follow the path.
	 * 
	 * @return {@code true} if rotation is enabled, or {@code false} otherwise
	 */
	public boolean isRotateToFollow() {
		return rotateToFollow;
	}

	/**
	 * Sets the maximum speed.
	 * 
	 * @param maxSpeed
	 *            the new maximum speed, or {@code -1} to indicate unbounded
	 *            speed
	 */
	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
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

	/**
	 * Determines whether the controller will cause the object to rotate to
	 * follow the path.
	 * 
	 * @param rotateToFollow
	 *            {@code true} if rotation should be enabled, or {@code false}
	 *            if it should be disabled
	 */
	public void setRotateToFollow(boolean rotateToFollow) {
		this.rotateToFollow = rotateToFollow;
	}

	/**
	 * Sets the offset for the rotation. This method will have no effect if
	 * {@link #rotateToFollow} is disabled; you can enable it with
	 * {@link #setRotateToFollow(boolean) setRotateToFollow(true)}.
	 * 
	 * @param rotationOffset
	 *            the new offset
	 */
	public void setRotationOffset(double rotationOffset) {
		this.rotationOffset = rotationOffset;
	}
}
