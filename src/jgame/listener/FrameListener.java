package jgame.listener;

import jgame.Context;
import jgame.GObject;

/**
 * A listener that is run every frame (i.e., {@link #isValid(GObject, Context)}
 * always returns {@code true}).
 * 
 * @author William Chargin
 * 
 */
public abstract class FrameListener implements Listener {

	@Override
	public boolean isValid(GObject target, Context context) {
		// Frame listeners are always valid.
		return true;
	}

}
