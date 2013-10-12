package jgame.controller;

import java.awt.geom.Point2D;

import jgame.Context;
import jgame.GObject;

/**
 * A tween that allows for size in two dimensions.
 * 
 * @author William Chargin
 * 
 */
public class SizeTween extends TweenController {

	/**
	 * The start dimensions, stored in a point for ease.
	 */
	private Point2D start;

	/**
	 * The total size displacement required. This isn't really a point; it's
	 * more like a two-dimensional size displacement vector.
	 */
	private final Point2D delta;

	/**
	 * Creates the size tween with the given duration and total displacement.
	 * The displacement values {@code dw} and {@code dh} represent the total
	 * displacement over the duration, not the displacement per frame. This
	 * tween will use linear interpolation.
	 * <p>
	 * The start point will be determined by the target's anchor point when the
	 * controller is first invoked.
	 * <p>
	 * The tween will use linear interpolation.
	 * 
	 * @param duration
	 *            the duration of the tween, in frames
	 * @param dw
	 *            the total width displacement
	 * @param dh
	 *            the total height displacement
	 */
	public SizeTween(int duration, double dw, double dh) {
		this(duration, Interpolation.LINEAR, dw, dh);
	}

	/**
	 * Creates the size tween with the given duration, starting size, and total
	 * size displacement. The displacement values {@code dw} and {@code dh}
	 * represent the total displacement over the duration, not the displacement
	 * per frame. This tween will use linear interpolation.
	 * 
	 * @param duration
	 *            the duration of the tween, in frames
	 * @param w0
	 *            the initial width
	 * @param h0
	 *            the initial height
	 * @param dw
	 *            the total width displacement
	 * @param dh
	 *            the total height displacement
	 */
	public SizeTween(int duration, double w0, double h0, double dw, double dh) {
		this(duration, Interpolation.LINEAR, w0, h0, dw, dh);
	}

	/**
	 * Creates the size tween with the given duration, interpolation type, and
	 * total displacement. The displacement values {@code dw} and {@code dh}
	 * represent the total displacement over the duration, not the displacement
	 * per frame.
	 * <p>
	 * The start point will be determined by the target's anchor point when the
	 * controller is first invoked.
	 * <p>
	 * The tween will use linear interpolation.
	 * 
	 * @param duration
	 *            the duration of the tween, in frames
	 * @param interpolationType
	 *            the interpolation type
	 * @param dw
	 *            the total width displacement
	 * @param dh
	 *            the total height displacement
	 */
	public SizeTween(int duration, Interpolation interpolationType, double dw,
			double dh) {
		super(duration, interpolationType);
		start = null;
		delta = new Point2D.Double(dw, dh);
	}

	/**
	 * Creates the size tween with the given duration, interpolation type,
	 * starting size, and total size displacement. The displacement values
	 * {@code dw} and {@code dh} represent the total displacement over the
	 * duration, not the displacement per frame.
	 * 
	 * @param duration
	 *            the duration of the tween, in frames
	 * @param interpolationType
	 *            the interpolation type
	 * @param w0
	 *            the initial width
	 * @param h0
	 *            the initial height
	 * @param dw
	 *            the total width displacement
	 * @param dh
	 *            the total height displacement
	 */
	public SizeTween(int duration, Interpolation interpolationType, double w0,
			double h0, double dw, double dh) {
		super(duration, interpolationType);
		this.start = new Point2D.Double(w0, h0);
		this.delta = new Point2D.Double(dw, dh);
	}

	@Override
	protected void interpolate(GObject target, Context context,
			double percentage) {
		// Was the start size pre-set?
		if (start == null) {
			// It hasn't been set yet. Use the starting size.
			start = new Point2D.Double(target.getWidth(), target.getHeight());
		}

		target.setSize(start.getX() + delta.getX() * percentage, start.getY()
				+ delta.getY() * percentage);
	}
}
