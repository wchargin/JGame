package jgame.controller;

import java.awt.Polygon;
import java.awt.geom.Point2D;

import jgame.Context;
import jgame.GObject;

/**
 * A controller allowing an object to follow a polygon. This controller is
 * irregular in that its {@linkplain #setMaxSpeed(double) max speed} actually
 * determines its slowness; more precisely, the value of {@link #getMaxSpeed()}
 * is the number of frames needed to traverse each edge of the polygon.
 * 
 * @author William Chargin
 * 
 */
public class PolygonController extends AbstractLocRotController {

	/**
	 * The polygon used to traverse the path.
	 */
	private Polygon p;

	/**
	 * The current point index.
	 */
	private int currentPoint = 0;

	/**
	 * The current step between points.
	 */
	private int currentStep = 0;

	/**
	 * Creates the polygon controller.
	 * 
	 * @param p
	 *            the polygon used for the controller
	 */
	public PolygonController(Polygon p) {
		super();
		setPolygon(p);
		setMaxSpeed(12);
	}

	/**
	 * Creates the polygon controller with the specified rotation settings.
	 * 
	 * @param p
	 *            the polygon used for the controller
	 * @param rotateToFollow
	 *            whether the controller should rotate the object to follow the
	 *            path
	 * @param rotationOffset
	 *            the rotation offset (default is {@code 0})
	 */
	public PolygonController(Polygon p, boolean rotateToFollow,
			int rotationOffset) {
		super(rotateToFollow, rotationOffset);
		setPolygon(p);
		setMaxSpeed(12);
	}

	@Override
	protected Point2D calculateControl(GObject target, Context context) {
		// If we're at the very first point, jump.
		if (currentStep == 0 && currentPoint == 0) {
			target.setX(p.xpoints[0]);
			target.setY(p.ypoints[0]);
			// Increment:
			if (++currentStep >= getMaxSpeed()) {
				currentStep = 0;
				currentPoint++;
			}
			return new Point2D.Double(0, 0);
		}
		// Target:
		double factor = currentStep / getMaxSpeed();
		double targetX = p.xpoints[currentPoint] + factor
				* (p.xpoints[currentPoint + 1] - p.xpoints[currentPoint]);
		double targetY = p.ypoints[currentPoint] + factor
				* (p.ypoints[currentPoint + 1] - p.ypoints[currentPoint]);

		// Displacement:
		Point2D point = new Point2D.Double(targetX - target.getX(), targetY
				- target.getY());

		// Increment:
		if (++currentStep >= getMaxSpeed()) {
			currentStep = 0;
			currentPoint++;
		}

		return point;
	}

	@Override
	protected boolean canControl(GObject target, Context context) {
		return (currentPoint + 1) < p.npoints;
	}

	/**
	 * Gets the polygon used by this controller.
	 * 
	 * @return the polygon
	 */
	public Polygon getPolygon() {
		return p;
	}

	/**
	 * Resets this controller to the beginning of the polygon.
	 */
	public void reset() {
		currentPoint = 0;
		currentStep = 0;
	}

	/**
	 * Sets the polygon used by this controller and {@linkplain #reset() resets}
	 * the controller.
	 * 
	 * @param p
	 *            the new polygon
	 */
	public void setPolygon(Polygon p) {
		this.p = p;
		reset();
	}
}
