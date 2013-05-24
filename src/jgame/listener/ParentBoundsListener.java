package jgame.listener;

import java.awt.geom.Rectangle2D;

import jgame.Context;
import jgame.GObject;

/**
 * A listener that is valid when the target object is not completely inside the
 * parent's bounding box. Touching the bounding box (e.g., at position
 * {@code (0, 0)}) will cause the listener to be invoked.
 * 
 * @author William Chargin
 * 
 */
public abstract class ParentBoundsListener implements Listener {

	/**
	 * Whether the vertical dimension should contribute to the result. Default
	 * is {@code true}.
	 */
	private boolean validateVertical = true;

	/**
	 * Whether the horizontal dimension should contribute to the result. Default
	 * is {@code true}.
	 */
	private boolean validateHorizontal = true;

	@Override
	public boolean isValid(GObject target, Context context) {
		Rectangle2D box = target.getTransformedBoundingShape().getBounds2D();
		GObject parent = target.getParent();
		if (parent == null) {
			return false;
		}
		boolean verticalInvalid = validateVertical
				&& (box.getY() < 0 || box.getY() + box.getHeight() > parent
						.getHeight());
		boolean horizontalInvalid = validateHorizontal
				&& (box.getX() < 0 || box.getX() + box.getWidth() > parent
						.getWidth());
		return verticalInvalid || horizontalInvalid;
	}

	/**
	 * Determines whether this listener validates the horizontal dimension.
	 * 
	 * @return {@code true} if the horizontal dimension contributes to the
	 *         result, or {@code false} otherwise
	 */
	public boolean isValidateHorizontal() {
		return validateHorizontal;
	}

	/**
	 * Determines whether this listener validates the vertical dimension.
	 * 
	 * @return {@code true} if the vertical dimension contributes to the result,
	 *         or {@code false} otherwise
	 */
	public boolean isValidateVertical() {
		return validateVertical;
	}

	/**
	 * Sets whether this listener validates the horizontal dimension.
	 * 
	 * @param validateHorizontal
	 *            {@code true} if the horizontal dimension should contribute to
	 *            the result, or {@code false} if it should not
	 */
	public void setValidateHorizontal(boolean validateHorizontal) {
		this.validateHorizontal = validateHorizontal;
	}

	/**
	 * Sets whether this listener validates the vertical dimension.
	 * 
	 * @param validateVertical
	 *            {@code true} if the vertical dimension should contribute to
	 *            the result, or {@code false} if it should not
	 */
	public void setValidateVertical(boolean validateVertical) {
		this.validateVertical = validateVertical;
	}
}
