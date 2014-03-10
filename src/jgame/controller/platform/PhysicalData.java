package jgame.controller.platform;

/**
 * A data structure for holding physical data about an object. These include
 * coefficients such as friction, slipperiness, etc.
 * 
 * @author William Chargin
 * 
 */
public class PhysicalData {

	/**
	 * The frictional power of the surface of this object &ndash; how quickly do
	 * you stop when you're not trying to move? A frictional value of
	 * {@code 0.0} indicates no friction (sliding forever), while {@code 1.0}
	 * indicates full friction (stop immediately after movement). This value
	 * should always be between {@code 0.0} and {@code 1.0}, inclusive. The
	 * default value is {@code 1.0}.
	 */
	private double friction = 1.0;

	/**
	 * The springiness of this object &ndash; how easy is it to jump? A
	 * springiness value of of {@code 0.0} indicates that jumping is impossible
	 * from this object (think superglue); values between {@code 0.0} and
	 * {@code 1.0} indicate below average springiness (think sand or mud);
	 * {@code 1.0} indicates jumping is normal (think ground); and values
	 * greater than {@code 1.0} indicate extra springiness (think trampoline).
	 * This value should always be greater than or equal to {@code 0.0}. The
	 * default value is {@code 1.0}.
	 */
	private double springiness = 1.0;

	/**
	 * The traction of this object &ndash; how easy is it to walk? A traction
	 * value of of {@code 0.0} indicates that walking is impossible from this
	 * object (think superglue); values between {@code 0.0} and {@code 1.0}
	 * indicate below average traction (think a reverse treadmill); {@code 1.0}
	 * indicates traction is normal (think ground); and values greater than
	 * {@code 1.0} indicate extra traction (think accelerator strips). This
	 * value should always be greater than or equal to {@code 0.0}. The default
	 * value is {@code 1.0}.
	 */
	private double traction = 1.0;

	/**
	 * The solidity of this object &ndash; how easy is it to stand on this
	 * object? A solidity value of {@code 1.0} indicates that this is completely
	 * solid ground; a value between {@code 0.0} and {code 1.0} indicates that
	 * objects may fall slowly through this ground; and a solidity value of
	 * {@code 0.0} indicates that this ground does not spuport any weight.
	 * object? This value should always be between {@code 0.0} and {@code 1.0},
	 * inclusive. The default value is {@code 1.0}.
	 */
	private double solidity = 1.0;

	/**
	 * Gets the friction value of this surface. See {@link #friction} for
	 * details.
	 * 
	 * @return the friction
	 */
	public double getFriction() {
		return friction;
	}

	/**
	 * Gets the solidity value of this surface. See {@link #solidity} for
	 * details.
	 * 
	 * @return the solidity
	 */
	public double getSolidity() {
		return solidity;
	}

	/**
	 * Gets the springiness value of this surface. See {@link #springiness} for
	 * details.
	 * 
	 * @return the springiness
	 */
	public double getSpringiness() {
		return springiness;
	}

	/**
	 * Gets the traction value of this surface. See {@link #traction} for
	 * details.
	 * 
	 * @return the traction
	 */
	public double getTraction() {
		return traction;
	}

	/**
	 * Sets the friction value of this surface. See {@link #friction} for
	 * details.
	 * 
	 * @param friction
	 *            the new friction value
	 */
	public void setFriction(double friction) {
		this.friction = friction;
	}

	/**
	 * Sets the solidity value of this surface. See {@link #solidity} for
	 * details.
	 * 
	 * @param solidity
	 *            the new solidity value
	 */
	public void setSolidity(double solidity) {
		this.solidity = solidity;
	}

	/**
	 * Sets the springiness value value of this surface. See
	 * {@link #springiness} for details.
	 * 
	 * @param springiness
	 *            the new springiness value
	 */
	public void setSpringiness(double springiness) {
		this.springiness = springiness;
	}

	/**
	 * Sets the traction value of this surface. See {@link #traction} for
	 * details.
	 * 
	 * @param traction
	 *            the new traction value
	 */
	public void setTraction(double traction) {
		this.traction = traction;
	}

}
