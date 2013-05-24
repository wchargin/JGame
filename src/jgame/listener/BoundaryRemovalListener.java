package jgame.listener;

import jgame.Context;
import jgame.GObject;

/**
 * Removes the target component if it is outside of the absolute game bounds.
 * 
 * @author William Chargin
 * 
 */
public class BoundaryRemovalListener implements Listener {

	@Override
	public void invoke(GObject target, Context context) {
		target.removeSelf();
	}

	@Override
	public boolean isValid(GObject target, Context context) {
		return !target.getRelativeToAbsoluteTransform()
				.createTransformedShape(target.getTransformedBoundingShape())
				.intersects(context.getGameBounds());
	}

}
