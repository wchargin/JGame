package jgame.controller;

import java.awt.geom.Point2D;

import jgame.Context;
import jgame.GObject;

/**
 * A tween that allows for movement in two dimensions.
 * 
 * @author William Chargin
 * 
 */
public class MovementTween extends TweenController {

	/**
	 * The start point
	 */
	private Point2D start;

	/**
	 * The total displacement required.. This isn't really a point; it's more
	 * like a two-dimensional displacement vector.
	 */
	private final Point2D delta;

	/**
	 * Creates the movement tween with the given duration and total
	 * displacement. The displacement values {@code dx} and {@code dy} represent
	 * the total displacement over the duration, not the displacement per frame.
	 * <p>
	 * The start point will be determined by the target's anchor point when the
	 * controller is first invoked.
	 * <p>
	 * The tween will use linear interpolation.
	 * 
	 * @param duration
	 *            the duration of the tween, in frames
	 * @param dx
	 *            the total displacement in the x-direction
	 * @param dy
	 *            the total displacement in the y-direction
	 */
	public MovementTween(int duration, double dx, double dy) {
		this(duration, (Point2D) null, dx, dy);
	}

	/**
	 * Creates the movement tween with the given duration, interpolation type,
	 * start point, and total displacement. The displacement values {@code dx}
	 * and {@code dy} represent the total displacement over the duration, not
	 * the displacement per frame.
	 * <p>
	 * The tween will use linear interpolation.
	 * 
	 * @param duration
	 *            the duration of the tween, in frames
	 * @param x0
	 *            the initial x position
	 * @param y0
	 *            the initial y position
	 * @param dx
	 *            the total displacement in the x-direction
	 * @param dy
	 *            the total displacement in the y-direction
	 */
	public MovementTween(int duration, double x0, double y0, double dx,
			double dy) {
		super(duration);
		this.start = new Point2D.Double(x0, y0);
		this.delta = new Point2D.Double(dx, dy);
	}

	/**
	 * Creates the movement tween with the given duration, interpolation, and
	 * total displacement. The displacement values {@code dx} and {@code dy}
	 * represent the total displacement over the duration, not the displacement
	 * per frame.
	 * <p>
	 * The start point will be determined by the target's anchor point when the
	 * controller is first invoked.
	 * 
	 * @param duration
	 *            the duration of the tween, in frames
	 * @param interpolationType
	 *            the interpolation type
	 * @param dx
	 *            the total displacement in the x-direction
	 * @param dy
	 *            the total displacement in the y-direction
	 */
	public MovementTween(int duration, Interpolation interpolationType,
			double dx, double dy) {
		this(duration, interpolationType, null, dx, dy);
	}

	/**
	 * Creates the movement tween with the given duration, interpolation type,
	 * start point, and total displacement. The displacement values {@code dx}
	 * and {@code dy} represent the total displacement over the duration, not
	 * the displacement per frame.
	 * 
	 * @param duration
	 *            the duration of the tween, in frames
	 * @param interpolationType
	 *            the interpolation type
	 * @param x0
	 *            the initial x position
	 * @param y0
	 *            the initial y position
	 * @param dx
	 *            the total displacement in the x-direction
	 * @param dy
	 *            the total displacement in the y-direction
	 */
	public MovementTween(int duration, Interpolation interpolationType,
			double x0, double y0, double dx, double dy) {
		super(duration, interpolationType);
		this.start = new Point2D.Double(x0, y0);
		this.delta = new Point2D.Double(dx, dy);
	}

	/**
	 * Creates the movement tween with the given duration, interpolation type,
	 * start point, and total displacement. The displacement values {@code dx}
	 * and {@code dy} represent the total displacement over the duration, not
	 * the displacement per frame.
	 * <p>
	 * You can obtain the starting point with GObject's
	 * {@link GObject#getAnchorPoint()} method.
	 * 
	 * @param duration
	 *            the duration of the tween, in frames
	 * @param interpolationType
	 *            the interpolation type
	 * @param start
	 *            the start point
	 * @param dx
	 *            the total displacement in the x-direction
	 * @param dy
	 *            the total displacement in the y-direction
	 */
	public MovementTween(int duration, Interpolation interpolationType,
			Point2D start, double dx, double dy) {
		super(duration, interpolationType);
		this.start = start;
		this.delta = new Point2D.Double(dx, dy);
	}

	/**
	 * Creates the movement tween with the given duration, interpolation type,
	 * and start and end points.
	 * 
	 * @param duration
	 *            the duration for the tween, in frames
	 * @param interpolationType
	 *            the interpolation type
	 * @param start
	 *            the start point
	 * @param end
	 *            the end point
	 */
	public MovementTween(int duration, Interpolation interpolationType,
			Point2D start, Point2D end) {
		super(duration, interpolationType);
		this.start = start;
		this.delta = new Point2D.Double(end.getX() - start.getX(), end.getY()
				- start.getY());
	}

	/**
	 * Creates the movement tween with the given duration, start point, and
	 * total displacement. The displacement values {@code dx} and {@code dy}
	 * represent the total displacement over the duration, not the displacement
	 * per frame.
	 * <p>
	 * You can obtain the starting point with GObject's
	 * {@link GObject#getAnchorPoint()} method.
	 * <p>
	 * The tween will use linear interpolation.
	 * 
	 * @param duration
	 *            the duration of the tween, in frames
	 * @param start
	 *            the start point
	 * @param dx
	 *            the total displacement in the x-direction
	 * @param dy
	 *            the total displacement in the y-direction
	 */
	public MovementTween(int duration, Point2D start, double dx, double dy) {
		super(duration);
		this.start = start;
		this.delta = new Point2D.Double(dx, dy);
	}

	/**
	 * Creates the movement tween with the given duration and start and end
	 * points.
	 * <p>
	 * The tween will use linear interpolation.
	 * 
	 * @param duration
	 *            the duration for the tween, in frames
	 * @param start
	 *            the start point
	 * @param end
	 *            the end point
	 */
	public MovementTween(int duration, Point2D start, Point2D end) {
		super(duration);
		this.start = start;
		this.delta = new Point2D.Double(end.getX() - start.getX(), end.getY()
				- start.getY());
	}

	@Override
	protected void interpolate(GObject target, Context context,
			double percentage) {
		// Was the start point pre-set?
		if (start == null) {
			// It hasn't been set yet. Use the starting point.
			start = target.getAnchorPoint();
		}

		target.setLocation(start.getX() + delta.getX() * percentage,
				start.getY() + delta.getY() * percentage);
	}
}
