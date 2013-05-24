package jgame.listener;

import jgame.Context;
import jgame.GObject;

/**
 * A listener that listens for hit-tests of the given object or class.
 * 
 * @author William Chargin
 * 
 */
public abstract class HitTestListener implements Listener {

	/**
	 * The class to perform hit-tests on.
	 */
	private final Class<? extends GObject> clazz;

	/**
	 * The instance to perform hit-tests on, or {@code null} to specify a
	 * class-wide hit-test.
	 */
	private GObject instance;

	/**
	 * Creates the listener with the given target class.
	 * 
	 * @param clazz
	 *            the target class
	 */
	public HitTestListener(Class<? extends GObject> clazz) {
		super();
		this.clazz = clazz;
		this.instance = null;
	}

	/**
	 * Creates the listener with the given target class and instance.
	 * 
	 * @param clazz
	 *            the target class
	 * @param instance
	 *            the target instance
	 */
	public HitTestListener(Class<? extends GObject> clazz, GObject instance) {
		super();
		this.clazz = clazz;
		this.instance = instance;
	}

	@Override
	public boolean isValid(GObject target, Context context) {
		return instance == null ? (!context.hitTestClass(clazz).isEmpty())
				: instance.preciseHitTest(target);
	}

}
