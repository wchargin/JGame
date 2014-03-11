package jgame.controller.platform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jgame.Context;
import jgame.GObject;
import jgame.controller.Controller;

public class PlatformController implements Controller {

	/**
	 * The control scheme used for this controller.
	 */
	private final PlatformControlScheme cs;

	/**
	 * The object controlled by this platform controller.
	 */
	private GObject object;

	/**
	 * The acceleration due to gravity of the environment, in
	 * <sup>px</sup>&frasl;<sub>frame<sup>2</sup></sub>. The default value is
	 * {@code 1.0}.
	 */
	private double gravity = 1.0;

	/**
	 * The horizontal drag value &ndash; how difficult is it to move
	 * horizontally in midair? A value of {@code 0.0} indicates that moving
	 * horizontally in midair is impossible, while a value of {@code 1.0}
	 * indicates that it is no more difficult than moving on the ground. This
	 * value should always be between {@code 0.0} and {@code 1.0}. The default
	 * value is {@code 0.75}.
	 */
	private double drag = 0.75;

	/**
	 * The maximum speed in the {@code x}-direction.
	 */
	private double maxSpeed = 5;

	/**
	 * The maximum initial jump velocity (in the {@code y}-direction).
	 */
	private double maxJump = 5;

	/**
	 * The current componentwise velocity of the target object.
	 */
	private double vx, vy;

	/**
	 * Creates the {@code PlatformController} with the given control scheme.
	 * 
	 * @param cs
	 *            the control scheme to use
	 */
	public PlatformController(PlatformControlScheme cs) {
		super();
		this.cs = cs;
	}

	@Override
	public void controlObject(GObject target, Context context) {
		if (object == null) {
			object = target;
		} else if (object != target) {
			throw new IllegalArgumentException(
					"PlatformController cannot be shared");
		}

		final int horizontal;
		final boolean jump;
		{
			int h = 0;
			boolean j = false;
			for (int key : context.getKeyCodesPressed()) {
				if (key == cs.jump)
					j = true;
				else if (key == cs.lt)
					h--;
				else if (key == cs.rt)
					h++;
			}
			jump = j;
			horizontal = h;
		}

		List<Ground> grounds = getPointsOfContact(context);
		double adjustedGravity = gravity;
		if (!grounds.isEmpty()) {
			double maxSolidity = 0;
			for (Ground ground : grounds) {
				maxSolidity = Math.max(maxSolidity, ground.getPhysicalData()
						.getSolidity());
			}
			adjustedGravity *= (1 - maxSolidity);
		}
		vy += adjustedGravity;

		if (jump) {
			double maxSpringiness = 0;
			for (Ground ground : grounds) {
				maxSpringiness = Math.max(maxSpringiness, ground
						.getPhysicalData().getSpringiness());
			}
			vy -= maxJump * maxSpringiness;
		}

		if (horizontal != 0) {
			double maxTraction = 0;
			for (Ground ground : grounds) {
				maxTraction = Math.max(maxTraction, ground.getPhysicalData()
						.getTraction());
			}
			vx += maxTraction * maxSpeed * horizontal;
		} else {
			double maxFriction = 0;
			for (Ground ground : grounds) {
				maxFriction = Math.max(maxFriction, ground.getPhysicalData()
						.getFriction());
			}
		}

		if (Math.abs(vy) > object.getHeight()) {
			vy = Math.copySign(object.getHeight() - 0.5, vy);
		}
		object.setLocation(object.getX() + vx, object.getY() + vy);
	}

	/**
	 * Gets the drag value. See {@link #drag} for more information.
	 * 
	 * @return the current drag value
	 */
	public double getDrag() {
		return drag;
	}

	/**
	 * Gets the gravity of this platform controller, in in
	 * <sup>px</sup>&frasl;<sub>frame<sup>2</sup></sub>.
	 * 
	 * @return the current value of acceleration due to gravity
	 */
	public double getGravity() {
		return gravity;
	}

	public double getMaxJump() {
		return maxJump;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	/**
	 * Gets a list of all points of contact that the object is currently
	 * touching.
	 * 
	 * @param context
	 *            the context in which to evaluate the request
	 * @return a list of all ground objects touching the object
	 */
	private List<Ground> getPointsOfContact(Context context) {
		List<Ground> grounds = new ArrayList<Ground>(
				context.getInstancesOfClass(Ground.class));
		double myRadius = Math.sqrt(Math.pow(object.getWidth(), 2)
				+ Math.pow(object.getHeight(), 2));
		Iterator<Ground> it = grounds.iterator();
		while (it.hasNext()) {
			Ground ground = it.next();
			if (!(ground instanceof GObject)) {
				continue; // weird
			}
			GObject gground = (GObject) ground;
			if (object.distanceTo(gground) - myRadius > 0.001) {
				it.remove();
				continue; // too far away, don't bother with a slow hit test
			}
			if (!object.hitTest(gground)) {
				it.remove();
			}
		}
		return grounds;
	}

	/**
	 * Sets the drag value. See {@link #drag} for more information.
	 * 
	 * @param drag
	 *            the new drag value
	 */
	public void setDrag(double drag) {
		if (drag < 0 || drag > 1) {
			throw new IllegalArgumentException(
					"drag must be between 0 and 1, but is " + drag);
		}
		this.drag = drag;
	}

	/**
	 * Sets the gravity of this platform controller, in
	 * <sup>px</sup>&frasl;<sub>frame<sup>2</sup></sub>.
	 * 
	 * @param gravity
	 *            the new value of acceleration due to gravity
	 */
	public void setGravity(double gravity) {
		this.gravity = gravity;
	}

	public void setMaxJump(double maxJump) {
		this.maxJump = maxJump;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

}
