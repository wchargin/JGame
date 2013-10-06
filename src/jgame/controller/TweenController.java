package jgame.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jgame.Context;
import jgame.GObject;

/**
 * A controller that allows tweening between two states. Tweens can be chained
 * with the {@link #chain(TweenController)} method to allow consecutive
 * tweening. Tweens also support the various types of interpolation as defined
 * in the {@link Interpolation} enumeration.
 * 
 * @author William Chargin
 * 
 */
public abstract class TweenController extends Object implements Controller {

	/**
	 * The number of frames that have gone by since the controller began.
	 */
	private int tick;

	/**
	 * The total duration of the controller.
	 */
	private int duration;

	/**
	 * The type of interpolation used for this tween.
	 */
	private Interpolation interpolationType;

	/**
	 * The controller that will be attached after this one.
	 */
	private TweenController next;

	/**
	 * The list of tween controllers that will be run with (beginning at the
	 * same time as) this one.
	 */
	private List<TweenController> with = new ArrayList<TweenController>();

	/**
	 * Creates the controller with the given duration and a linear interpolation
	 * type.
	 * 
	 * @param duration
	 *            the duration, in frames
	 */
	public TweenController(int duration) {
		this(duration, Interpolation.LINEAR);
	}

	/**
	 * Creates the controller with the given duration and interpolation type.
	 * 
	 * @param duration
	 *            the duration, in frames
	 * @param interpolationType
	 *            the interpolation type
	 */
	public TweenController(int duration, Interpolation interpolationType) {
		super();
		this.duration = duration;
		this.interpolationType = interpolationType;
	}

	/**
	 * Calls {@link #chain(TweenController)} on all items in the given list.
	 * Note that any duplicate items will cause an
	 * {@code IllegalArgumentException} to be thrown, because an infinite loop
	 * will be created.
	 * 
	 * @param chain
	 *            the list of controllers to chain
	 */
	public void chain(List<TweenController> chain) {
		for (TweenController c : chain) {
			chain(c);
		}
	}

	/**
	 * Adds the given controller to the end of the chain. If an infinite loop is
	 * detected, this method will fail and throw an exception.
	 * 
	 * @param end
	 *            the new end controller
	 * @throws IllegalArgumentException
	 *             if an infinite loop is detected
	 */
	public void chain(TweenController end) throws IllegalArgumentException {
		// Is this the first one?
		if (next == null) {
			// Simple assignment; do it.
			next = end;
			return;
		}

		// Find the current last node by traversing the linked list.
		TweenController currentLast = next;

		// Keep track of visited nodes to see if we've found a loop.
		final Set<TweenController> visited = new HashSet<TweenController>();

		// Keep going until we're at the end of the road.
		while (currentLast.next != null) {
			visited.add(currentLast);
			currentLast = currentLast.next;
			if (visited.contains(currentLast)) {
				// We've recursed.
				throw new IllegalArgumentException(
						"Infinite loop detected while chaining.");
			}
		}

		// Add to the end.
		currentLast.next = end;
	}

	/**
	 * Calls {@link #chain(TweenController)} on all given items. Note that any
	 * duplicate items will cause an {@code IllegalArgumentException} to be
	 * thrown, because an infinite loop will be created.
	 * 
	 * @param chain
	 *            the controllers to chain
	 */
	public void chain(TweenController... chain) {
		for (TweenController c : chain) {
			chain(c);
		}
	}

	@Override
	public void controlObject(GObject target, Context context) {
		if (tick > duration) {
			// We've been reused. Get out.
			return;
		}

		// Is this our first run?
		if (tick == 0) {
			// We may have other tweens that should be run at the same time.
			for (TweenController tween : with) {
				// Add the controller.
				target.addController(tween);

				// Since it won't be invoked until next frame (and will be one
				// frame off), invoke it manually to get it on track.
				tween.controlObject(target, context);
			}
		}

		// Interpolate!
		interpolate(target, context,
				interpolationType.calculateInterpolation(tick, duration));

		// Advance.
		tick++;

		// Are we done now?
		if (tick > duration) {
			// We're done. Remove ourself.
			target.removeController(this);

			// Do we chain?
			if (next != null) {
				// Do so.
				target.addController(next);
			}
		}
	}

	/**
	 * Gets the next link in the chain, or {@code null} if no chain is set.
	 * 
	 * @return the next link in the chain
	 */
	public TweenController getNext() {
		return next;
	}

	/**
	 * Interpolates the tween for the given value.
	 * 
	 * @param target
	 *            the object to control
	 * @param context
	 *            the relevant context
	 * @param percentage
	 *            the percentage of completion of the tween
	 */
	protected abstract void interpolate(GObject target, Context context,
			double percentage);

	/**
	 * Sets the next link in the chain. To add to the end of the chain easily,
	 * use the {@link #chain(TweenController)} method. Passing {@code null} will
	 * have the same effect as calling {@link #unchain()}.
	 * 
	 * @param next
	 *            the new next link
	 */
	public void setNext(TweenController next) {
		this.next = next;
	}

	/**
	 * Removes the chain from this controller. This will not break the rest of
	 * the chain. For example, if {@code a} is chained to {@code b} and
	 * {@code b} is chained to {@code c}, calling {@code a.unchain()} will leave
	 * {@code b} chained to {@code c}.
	 * <p>
	 * This method is identical to {@link #setNext(TweenController)
	 * setChainStart(null)}.
	 */
	public void unchain() {
		next = null;
	}

	/**
	 * Calls {@link #with(TweenController)} on all given items. This sets each
	 * given tween to start with this tween. That is, when this tween starts,
	 * the other tweens will start as well.
	 * 
	 * @param tweens
	 *            a list of tween that should start with this tween
	 */
	public void with(List<TweenController> tweens) {
		for (TweenController tween : tweens) {
			with(tween);
		}
	}

	/**
	 * Sets the given tween to start with this tween. That is, when this tween
	 * starts, the other tween will start as well.
	 * 
	 * @param tween
	 *            a tween that should start with this tween
	 */
	public void with(TweenController tween) {
		// Check if we already have it.
		if (!with.contains(tween)) {
			// Add.
			with.add(tween);
		}
	}

	/**
	 * Calls {@link #with(TweenController)} on all the given items. This sets
	 * each given tween to start with this tween. That is, when this tween
	 * starts, the other tweens will start as well.
	 * 
	 * @param tweens
	 *            an array of tweens that should start with this tween
	 */
	public void with(TweenController... tweens) {
		for (TweenController tween : tweens) {
			with(tween);
		}
	}

}
