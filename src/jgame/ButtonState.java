package jgame;

/**
 * The possible states of a button.
 * 
 * @author William Chargin
 * 
 */
public enum ButtonState {
	/**
	 * Indicates that the mouse is nowhere near the button.
	 */
	NONE,

	/**
	 * Indicates that the mouse is hovering the button.
	 */
	HOVERED,

	/**
	 * Indicates that the mouse is pressed on the button (but not released).
	 */
	PRESSED;
}