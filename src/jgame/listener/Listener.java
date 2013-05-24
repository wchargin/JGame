package jgame.listener;

import jgame.Context;
import jgame.GObject;

/**
 * A listener. Each listener is checked every frame, and if it
 * {@linkplain #isValid(GObject, Context) is valid}, then the listener will be
 * {@linkplain #invoke(GObject, Context) invoked}.
 * 
 * @author William Chargin
 */
public interface Listener {

	/**
	 * Invokes the listener in the specified context on the specified object.
	 * 
	 * @param target
	 *            the target of the listener
	 * @param context
	 *            the context in which to invoke the listener
	 */
	public void invoke(GObject target, Context context);

	/**
	 * Determines whether the listener is valid and should be invoked in the
	 * given context.
	 * 
	 * @param target
	 *            the target of the listener
	 * @param context
	 *            the context to test
	 * @return {@code true} if the listener should be invoked, or {@code false}
	 *         if it should not
	 */
	public boolean isValid(GObject target, Context context);

}
