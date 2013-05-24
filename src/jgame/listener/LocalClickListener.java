package jgame.listener;

import java.awt.event.MouseEvent;

import jgame.Context;
import jgame.GObject;

/**
 * A listener that listens for clicks on the object.
 * 
 * @author William Chargin
 * 
 */
public abstract class LocalClickListener implements Listener {

	@Override
	public boolean isValid(GObject target, Context context) {
		return context.getMouseButtonMask() != MouseEvent.NOBUTTON
				&& target.getTransformedBoundingShape().contains(
						context.getMouseRelative());
	}

}
