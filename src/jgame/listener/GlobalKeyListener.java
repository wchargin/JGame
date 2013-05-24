package jgame.listener;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jgame.Context;
import jgame.GObject;

/**
 * A listener that listens for a key being pressed anywhere.
 * 
 * @author William Chargin
 * 
 */
public abstract class GlobalKeyListener implements Listener {

	/**
	 * The set of key codes on which to listen.
	 */
	private final Set<Integer> keyCodes;

	/**
	 * Creates the key listener with the specified key codes.
	 * 
	 * @param keyCodes
	 *            the keys to listen for
	 */
	public GlobalKeyListener(Collection<Integer> keyCodes) {
		// Set the field to the given collection as unmodifiable.
		this.keyCodes = Collections.unmodifiableSet(new HashSet<Integer>(
				keyCodes));
	}

	/**
	 * Creates the key listener with the specified key codes.
	 * 
	 * @param keyCodes
	 *            the keys to listen for
	 */
	public GlobalKeyListener(int... keyCodes) {
		// Create a temporary list.
		Set<Integer> keyCodesTemp = new HashSet<Integer>();

		// Loop over each key code.
		for (int keyCode : keyCodes) {
			// Add to the temp list.
			keyCodesTemp.add(keyCode);
		}

		// Now set the field to the temp list as unmodifiable.
		this.keyCodes = Collections.unmodifiableSet(keyCodesTemp);
	}

	@Override
	public boolean isValid(GObject target, Context context) {
		final Set<Integer> pressed = context.getKeyCodesPressed();
		return (keyCodes.isEmpty() && !pressed.isEmpty())
				|| !(Collections.disjoint(pressed, keyCodes));
	}

}
