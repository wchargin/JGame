package jgame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The root container for a game.
 * 
 * @author William Chargin
 * 
 */
public class GRootContainer implements GPaintable, GObjectHolder {

	/**
	 * Creates and returns a root container with a single view.
	 * 
	 * @param view
	 *            the view to use
	 * @param color
	 *            the background color to use
	 * @return the root container
	 */
	public static GRootContainer createSingleViewRootContainer(GObject view,
			Color color) {
		GRootContainer container = new GRootContainer(color);
		container.addView(view, view);
		return container;
	}

	/**
	 * The map of card names to views.
	 */
	private Map<Object, GObject> views;

	/**
	 * The name of the current view.
	 */
	private Object currentViewName;

	/**
	 * The background color for this container.
	 */
	private Color color;

	/**
	 * Creates the root container.
	 * 
	 * @param color
	 *            the background color
	 */
	public GRootContainer(Color color) {
		super();
		this.color = color;
		views = new ConcurrentHashMap<Object, GObject>();
	}

	/**
	 * Adds the given view with the name as the same object. This method is
	 * discouraged.
	 * <p>
	 * Equivalent to {@link #addView(Object, GObject) addView(object, object)}.
	 * 
	 * @param object
	 *            the view to be added
	 * @see jgame.GObjectHolder#add(jgame.GObject)
	 */
	@Override
	public void add(GObject object) {
		// Use the object as both name and view.
		addView(object, object);
	}

	/**
	 * Adds the given view to the list of views under the given name. If a view
	 * of the same name already exists, it will be replaced.
	 * <p>
	 * If the list of views is empty before calling this method, then the new
	 * view will be set as the current view. Otherwise, the current view will
	 * remain unchanged.
	 * 
	 * @param viewName
	 *            the view name
	 * @param view
	 *            the view
	 * @throws IllegalArgumentException
	 *             if {@code viewName} or {@code view} is {@code null}
	 */
	public void addView(Object viewName, GObject view)
			throws IllegalArgumentException {
		// Make sure the name is non-null.
		if (viewName == null) {
			throw new IllegalArgumentException("viewName == null");
		}

		// Make sure the view itself is non-null;
		if (view == null) {
			throw new IllegalArgumentException("view == null");
		}

		// Is it the first?
		boolean first = views.isEmpty();

		// Add this to the map of view names to views.
		views.put(viewName, view);

		// Set this as the current view if none is set.
		if (first) {
			setCurrentView(viewName);
		}
	}

	/**
	 * Adds the given view under the given name, and sets it as the current
	 * view.
	 * <p>
	 * This method is equivalent to invoking first
	 * {@link #addView(Object, GObject)} and then
	 * {@link #setCurrentView(Object)}.
	 * 
	 * @param viewName
	 *            the view name
	 * @param view
	 *            the view
	 */
	public void addViewAndSetCurrent(Object viewName, GObject view) {
		addView(viewName, view);
		setCurrentView(viewName);
	}

	@Override
	public void componentRemoved(GPaintable object) {
		// Get an iterator.
		Iterator<Entry<Object, GObject>> it = views.entrySet().iterator();

		// Iterate over each key-value pair.
		while (it.hasNext()) {
			// Get the current key-value pair.
			Entry<Object, GObject> pair = it.next();

			// Does this have the object we want to remove?
			if (pair.getValue().equals(object)) {
				// Remove this pair.
				it.remove();
			}
		}
	}

	/**
	 * Gets the current view.
	 * 
	 * @return the current view
	 */
	public GObject getCurrentView() {
		return views.get(currentViewName);
	}

	/**
	 * Gets the height of this component.
	 * 
	 * @return the maximum height of all the views of this component
	 */
	public double getHeight() {
		// The final height should be the maximum of any of the heights.
		double max = 0;

		// Test each object.
		for (GObject view : views.values()) {
			// Make sure it's non-null.
			if (view != null) {
				// Update the maximum, if necessary.
				max = Math.max(max, view.getHeight());
			}
		}

		// Return the answer.
		return max;
	}

	/**
	 * Returns the integer height of this object. The integer height is the
	 * {@linkplain Math#ceil(double) ceiling value} of the height property.
	 * 
	 * @return the integer height
	 */
	public int getIntHeight() {
		return (int) Math.ceil(getHeight());
	}

	/**
	 * Returns the integer width of this object. The integer width is the
	 * {@linkplain Math#ceil(double) ceiling value} of the width property.
	 * 
	 * @return the integer width
	 */
	public int getIntWidth() {
		return (int) Math.ceil(getWidth());
	}

	@Override
	public Collection<GObject> getObjects() {
		return Collections.unmodifiableCollection(views.values());
	}

	/**
	 * Gets the width of this component.
	 * 
	 * @return the maximum width of all the views of this component
	 */
	public double getWidth() {
		// The final height should be the maximum of any of the heights.
		double max = 0;

		for (GObject view : views.values()) {
			// Make sure it's non-null.
			if (view != null) {
				// Update the maximum, if necessary.
				max = Math.max(max, view.getWidth());
			}
		}

		// Return the answer.
		return max;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void paint(Graphics2D g) {

		// Set the background color.
		g.setBackground(color);

		// Clear with background color.
		g.clearRect(0, 0, (int) Math.ceil(getWidth()),
				(int) Math.ceil(getHeight()));

		// Paint the current view, if any.
		if (currentViewName != null) {
			// We may have a view.
			GPaintable view = views.get(currentViewName);

			// Make sure it's non-null and paintable.
			if (view != null) {

				// Go ahead and paint.
				view.paint(g);
			}
		}
	}

	@Override
	public void preparePaint(Graphics2D g) {
		// Nothing to do here. We don't transform anything.
	}

	@Override
	public void remove(GObject object) {
		// Clear the parent.
		// This will invoke `componentRemoved`, which will remove the object
		// from the map.
		object.setParent(null);
	}

	/**
	 * Sets the current view to that with the given name.
	 * 
	 * @param viewName
	 *            the target view's name, as determined in the previous call to
	 *            {@link #addView(Object, GObject)}
	 * @throws NoSuchElementException
	 *             if there is no view by the given name
	 */
	public void setCurrentView(Object viewName) throws NoSuchElementException {
		// Make sure we have the view.
		if (!views.containsKey(viewName)) {
			// Let the developer know that we don't.
			throw new NoSuchElementException("There is no view by the name: "
					+ viewName);
		}

		// Are they the same?
		boolean same = viewName == currentViewName;

		// If not...
		if (!same) {
			// ... and the old one is non-null ...
			if (currentViewName != null && views.get(currentViewName) != null) {
				// ... let it know it's gone.
				views.get(currentViewName).viewHidden();
			}
		}

		// Set the current view name to the new view name.
		currentViewName = viewName;

		// If it's not the same...
		if (!same) {
			// ... let it know it's shown.
			views.get(currentViewName).viewShown();
		}
	}

}
