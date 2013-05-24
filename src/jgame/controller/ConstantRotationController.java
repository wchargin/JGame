package jgame.controller;

import jgame.Context;
import jgame.GObject;

/**
 * A simple controller that continuously rotates the target at a constant
 * angular speed.
 * 
 * @author William Chargin
 * 
 */
public class ConstantRotationController implements Controller {

	/**
	 * The speed at which the controller will rotate the object (in
	 * degrees/frame).
	 */
	private double speed;

	/**
	 * Creates the controller with the given speed.
	 * 
	 * @param speed
	 *            the speed
	 */
	public ConstantRotationController(double speed) {
		this.speed = speed;
	}

	@Override
	public void controlObject(GObject target, Context context) {
		target.setRotation((target.getRotation() + speed) % 360);
	}

	/**
	 * Gets the rotation speed for this controller.
	 * 
	 * @return the rotation speed, in degrees/frame
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Sets the rotation speed for this controller.
	 * 
	 * @param speed
	 *            the new rotation speed, in degrees/frame
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

}
