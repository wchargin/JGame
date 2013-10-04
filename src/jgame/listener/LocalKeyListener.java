package jgame.listener;

import java.util.Collection;

import jgame.Context;
import jgame.GObject;

/**
 * A listener that listens for a key being pressed on this object.
 * 
 * @author William Chargin
 * 
 */
public abstract class LocalKeyListener extends GlobalKeyListener {

	/**
	 * Creates the key listener with the specified key codes.
	 * 
	 * @param keyCodes
	 *            the keys to listen for
	 */
	public LocalKeyListener(Collection<Integer> keyCodes) {
		super(keyCodes);
	}

	/**
	 * Creates the key listener with the specified key codes.
	 * 
	 * @param keyCodes
	 *            the keys to listen for
	 */
	public LocalKeyListener(int... keyCodes) {
		super(keyCodes);
	}

	@Override
	public boolean isValid(GObject target, Context context) {
		return super.isValid(target, context)
				&& target.getTransformedBoundingShape().contains(
						context.getMouseRelative());
	}

}
