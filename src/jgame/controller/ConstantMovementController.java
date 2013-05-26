package jgame.controller;

import jgame.Context;
import jgame.GObject;

/**
 * A simple controller that continuously moves the target at a constant
 * velocity.
 * 
 * @author William Chargin
 * 
 */
public class ConstantMovementController implements Controller {

	/**
	 * Creates a {@code ConstantMotionController} according to the given polar
	 * vector <span style="font-family: serif">(<em>r</em>, <em>&theta;</em>
	 * )</span> (or, equivalently, <span style="font-family: serif">(<em>v</em>,
	 * <em>&theta;</em>)</span>). The generated controller will cause the target
	 * to move at the given speed in the given direction.
	 * 
	 * @param speed
	 *            the speed at which to move
	 * @param angle
	 *            the angle at which to move
	 * @return a new {@code ConstantMovementController}
	 */
	public static ConstantMovementController createPolar(double speed,
			double angle) {
		double angle_rad = Math.toRadians(angle);
		return new ConstantMovementController(speed * Math.cos(angle_rad),
				speed * Math.sin(angle_rad));
	}

	/**
	 * Creates a {@code ConstantMotionController} according to the given polar
	 * vector <span style="font-family: serif">(<em>r</em>, <em>&theta;</em>
	 * )</span> (or, equivalently, <span style="font-family: serif">(<em>v</em>,
	 * <em>&theta;</em>)</span>). The generated controller will cause the target
	 * to move at the given speed in the given direction, with the given
	 * damping.
	 * 
	 * @param speed
	 *            the speed at which to move
	 * @param angle
	 *            the angle at which to move
	 * @param damping
	 *            the damping factor
	 * @return a new {@code ConstantMovementController}
	 */
	public static ConstantMovementController createPolar(double speed,
			double angle, double damping) {
		double angle_rad = Math.toRadians(angle);
		return new ConstantMovementController(speed * Math.cos(angle_rad),
				speed * Math.sin(angle_rad), damping);
	}

	/**
	 * The velocity at which the controller will move the object along the
	 * x-axis (in px/frame).
	 */
	private double velocityX;

	/**
	 * The velocity at which the controller will move the object along the
	 * y-axis (in px/frame).
	 */
	private double velocityY;

	/**
	 * The damping value.
	 */
	private double damping = 1.0;

	/**
	 * Creates the controller with the given x and y velocities.
	 * 
	 * @param velocityX
	 *            the x velocity
	 * @param velocityY
	 *            the y velocity
	 */
	public ConstantMovementController(double velocityX, double velocityY) {
		this(velocityX, velocityY, 1.0);
	}

	/**
	 * Creates the controller with the given x and y velocities and damping.
	 * 
	 * @param velocityX
	 *            the x velocity
	 * @param velocityY
	 *            the y velocity
	 * @param damping
	 *            the damping factor
	 */
	public ConstantMovementController(double velocityX, double velocityY,
			double damping) {
		super();
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.damping = damping;
	}

	@Override
	public void controlObject(GObject target, Context context) {
		target.setX(target.getX() + velocityX);
		target.setY(target.getY() + velocityY);
		velocityX *= damping;
		velocityY *= damping;
	}

	/**
	 * Gets the damping of this controller.
	 * 
	 * @return the new damping
	 */
	public double getDamping() {
		return damping;
	}

	/**
	 * Gets the x-velocity for this controller.
	 * 
	 * @return the x-velocity, in px/frame
	 */
	public double getVelocityX() {
		return velocityX;
	}

	/**
	 * Gets the y-velocity for this controller.
	 * 
	 * @return the y-velocity, in px/frame
	 */
	public double getVelocityY() {
		return velocityY;
	}

	/**
	 * Sets the damping of this controller.
	 * 
	 * @param damping
	 *            the new damping value
	 */
	public void setDamping(double damping) {
		this.damping = damping;
	}

	/**
	 * Sets the x-velocity for this controller.
	 * 
	 * @param velocityX
	 *            the new x-velocity, in px/frame
	 */
	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}

	/**
	 * Sets the y-velocity for this controller.
	 * 
	 * @param velocityY
	 *            the new y-velocity, in px/frame
	 */
	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}

}
