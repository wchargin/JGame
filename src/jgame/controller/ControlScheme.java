package jgame.controller;

import java.awt.event.KeyEvent;

/**
 * A control scheme to map keys to directions.
 * 
 * @author William Chargin
 * 
 */
public final class ControlScheme {

	/**
	 * The control scheme using the arrow keys.
	 */
	public static ControlScheme ARROW_KEYS = new ControlScheme(KeyEvent.VK_UP,
			KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT);

	/**
	 * The control scheme using the W, A, S, and D keys.
	 */
	public static ControlScheme WASD = new ControlScheme(KeyEvent.VK_W,
			KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D);

	/**
	 * The control scheme using the I, J, K, and L keys.
	 */
	public static ControlScheme IJKL = new ControlScheme(KeyEvent.VK_I,
			KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L);

	/**
	 * The key code for upward movement.
	 */
	public final int up;

	/**
	 * The key code for leftward movement.
	 */
	public final int lt;

	/**
	 * The key code for downward movement.
	 */
	public final int dn;

	/**
	 * The key code for rightward movement.
	 */
	public final int rt;

	/**
	 * Creates the control scheme with the given parameters.
	 * 
	 * @param up
	 *            the key code for upward movement
	 * @param lt
	 *            the key code for leftward movement
	 * @param dn
	 *            the key code for downward movement
	 * @param rt
	 *            the key code for rightward movement
	 */
	public ControlScheme(int up, int lt, int dn, int rt) {
		this.up = up;
		this.lt = lt;
		this.dn = dn;
		this.rt = rt;
	}

}