package jgame.controller.platform;

/**
 * A physical object that can act as ground in a physics-powered game.
 * 
 * @author William Chargin
 * 
 */
public interface Ground {

	/**
	 * Gets the {@code y}-position for the top of the surface at a given
	 * {@code x}-position. Essentially, this means, "if I am standing at this
	 * {@code x} position, how high is the ground at this point, relative to the
	 * ground's anchor point?" Here are some examples:
	 * 
	 * <ul>
	 * <li>For a ground object that is flat, this method should always return
	 * the y-coordinate of the top of the block. If the block has centered
	 * anchor weights, then this should be the negative of half the height. If
	 * it is anchored at top left (or top anything), then this should return
	 * {@code 0}.</li>
	 * <li>For a triangular block sloping upward at a 45&deg; angle, the block
	 * should return the given {@code x} position, because the equation of the
	 * block's boundary is <em>y</em> = <em>x</em>. A block sloping upward at
	 * 63&deg; should return {@code 2 * x}.</li>
	 * </ul>
	 * 
	 * @return the {@code y}-coordinate of the top of the ground at a given
	 *         {@code x}-coordinate
	 */
	public double getSurfaceHeight(double x);

	/**
	 * Gets the physical data of this object.
	 * 
	 * @return the physical data
	 */
	public PhysicalData getPhysicalData();

}
