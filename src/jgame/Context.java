package jgame;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Set;

/**
 * The context for a controller or listener on a given object.
 * 
 * @author William Chargin
 * 
 */
public interface Context {

	/**
	 * Gets the game boundary rectangle.
	 * 
	 * @return the rectangle
	 */
	public Rectangle2D getGameBounds();

	/**
	 * Gets all instances of the given class currently present in the game.
	 * 
	 * @param clazz
	 *            the class for which to gather instances
	 * @return a list of all the instances
	 */
	public <T extends GPaintable> List<T> getInstancesOfClass(Class<T> clazz);

	/**
	 * Gets the key codes for keys currently pressed. The array contents should
	 * be the contents defined in {@link java.awt.event.KeyEvent}.
	 * 
	 * @return the key codes
	 */
	public Set<Integer> getKeyCodesPressed();

	/**
	 * Gets the mouse's position relative to the main game.
	 * 
	 * @return the mouse's position
	 */
	public Point getMouseAbsolute();

	/**
	 * Gets the mouse button mask. Use the constants defined in
	 * {@link java.awt.event.MouseEvent} to extract the individual buttons.
	 * 
	 * @return the mouse button pressed
	 */
	public int getMouseButtonMask();

	/**
	 * Gets the mouse's position relative to the top-left corner of object's
	 * parent.
	 * 
	 * @return the mouse's position
	 */
	public Point getMouseRelative();

	/**
	 * Tests whether the given object is touching any members of the given class
	 * or subclasses thereof.
	 * 
	 * @param clazz
	 *            the class to test
	 * @return a non-{@code null} but possibly empty collection containing each
	 *         object {@code o} in the current game for which
	 *         {@link GObject#hitTest(GObject) o.hitTest(t)} and
	 *         {@link Class#isInstance(Object) clazz.isInstance(o)} both
	 *         evaluate to {@code true}, where {@code t} is the object for which
	 *         this {@code ListenerContext} was created, and excluding {@code t}
	 *         from the list
	 */
	public <T extends GObject> List<T> hitTestClass(Class<T> clazz);

	/**
	 * Determines whether the mouse is currently in the game screen. If it is
	 * not, the values of the four mouse functions should be ignored as they are
	 * not reliable.
	 * 
	 * @return {@code true} if the mouse is in the game screen, or {@code false}
	 *         if it is not
	 */
	public boolean isMouseInScreen();

	/**
	 * Sets the game view to the given view.
	 * 
	 * @param viewName
	 *            the name of the view to set
	 * @see GRootContainer#setCurrentView(Object)
	 */
	public void setCurrentGameView(Object viewName);

	/**
	 * Stops the game at the end of the frame.
	 */
	public void stopGame();

}
