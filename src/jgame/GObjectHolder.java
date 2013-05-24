package jgame;

import java.util.Collection;

/**
 * An object that can hold other {@link GObject}s.
 * 
 * @author William Chargin
 * 
 */
public interface GObjectHolder {

	/**
	 * Adds the given object to the list of components.
	 * 
	 * @param object
	 *            the object to add
	 */
	public void add(GObject object);

	/**
	 * Invoked when an object is removed from a container.
	 * 
	 * @param object
	 *            the object that has been removed
	 */
	public void componentRemoved(GPaintable object);

	/**
	 * Gets the objects contained by this object. The returned collection will
	 * probably be unmodifiable.
	 * 
	 * @return the sub-objects
	 */
	public Collection<GObject> getObjects();

	/**
	 * Removes the given object from the list of objects.
	 * 
	 * @param object
	 *            the object to remove
	 */
	public void remove(GObject object);

}
