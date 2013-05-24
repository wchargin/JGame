package jgame.listener;

import java.awt.event.MouseEvent;

import jgame.Context;
import jgame.GObject;

/**
 * A listener that listens for clicks anywhere in the game area.
 * 
 * @author William Chargin
 * 
 */
public abstract class GlobalClickListener implements Listener {

	@Override
	public boolean isValid(GObject target, Context context) {
		return context.getMouseButtonMask() != MouseEvent.NOBUTTON;
	}

}
