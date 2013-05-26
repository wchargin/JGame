package jgame;

import java.awt.Graphics2D;

/**
 * Any object that can be painted in a game.
 * 
 * @author William Chargin
 * 
 */
public interface GPaintable {

	/**
	 * Determines whether this object is visible. Neither an invisible object
	 * nor its children will be painted.
	 * 
	 * @return {@code true} if this object is visible, or {@code false} if it is
	 *         not
	 */
	public boolean isVisible();

	/**
	 * Paints the object given the specified graphics context.
	 * 
	 * @param g
	 *            the graphics context on which to paint
	 */
	public void paint(Graphics2D g);

	/**
	 * Prepares the canvas for painting by performing any necessary
	 * modifications to the graphics context.
	 * 
	 * @param g
	 *            the graphics context to prepare
	 */
	public void preparePaint(Graphics2D g);

}
