package jgame.controller.platform;

import java.awt.event.KeyEvent;

/**
 * A control scheme to map keys to directions for a platform game.
 * 
 * @author William Chargin
 * 
 */
public final class PlatformControlScheme {

	/**
	 * The control scheme using the arrow keys, and space for jump.
	 */
	public static PlatformControlScheme ARROWS_SPACE = new PlatformControlScheme(
			KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE);

	/**
	 * The control scheme using the A and D keys, and space for jump.
	 */
	public static PlatformControlScheme WASD_SPACE = new PlatformControlScheme(
			KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);

	/**
	 * The key code for leftward movement.
	 */
	public final int lt;

	/**
	 * The key code for rightward movement.
	 */
	public final int rt;

	/**
	 * The key code for downward movement.
	 */
	public final int jump;

	/**
	 * Creates the control scheme with the given parameters.
	 * 
	 * @param lt
	 *            the key code for leftward movement
	 * @param rt
	 *            the key code for rightward movement
	 * @param jump
	 *            the key code for jumping
	 */
	public PlatformControlScheme(int lt, int rt, int jump) {
		this.lt = lt;
		this.rt = rt;
		this.jump = jump;
	}

}