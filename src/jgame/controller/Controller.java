package jgame.controller;

import jgame.Context;
import jgame.GObject;

/**
 * An object that can control a game object based on user interaction.
 * 
 * @author William Chargin
 * 
 */
public interface Controller {

	/**
	 * Performs one iteration of controlling the specified object in the given
	 * context.
	 * 
	 * @param target
	 *            the object to control
	 * @param context
	 *            the relevant context
	 */
	public void controlObject(GObject target, Context context);

}
