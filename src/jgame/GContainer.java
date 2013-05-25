package jgame;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * A simple container.
 * 
 * @author William Chargin
 * 
 */
public class GContainer extends GObject implements GObjectHolder, GAnimatable {

	/**
	 * The background sprite to paint.
	 */
	private GSprite backgroundSprite;
	/**
	 * The background color to paint.
	 */
	private Color backgroundColor;

	/**
	 * Creates the container.
	 */
	public GContainer() {
		super();
	}

	/**
	 * Creates the container with the given background color.
	 * 
	 * @param color
	 *            the background color
	 */
	public GContainer(Color color) {
		this();
		setBackgroundColor(color);
	}

	/**
	 * Creates the container with the given background color and sprite.
	 * 
	 * @param color
	 *            the background color
	 * @param sprite
	 *            the background sprite
	 */
	public GContainer(Color color, GSprite sprite) {
		this();
		setBackgroundColor(color);
		setBackgroundSprite(sprite);
	}

	/**
	 * Creates the container with the given background sprite.
	 * 
	 * @param sprite
	 *            the sprite
	 */
	public GContainer(GSprite sprite) {
		this();
		setBackgroundSprite(sprite);
	}

	/**
	 * Gets the current backgroundColor color of this component.
	 * 
	 * @return the backgroundColor color
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Gets the background sprite for this object
	 * 
	 * @return the background sprite, or {@code null} if none has been set.
	 */
	public GSprite getBackgroundSprite() {
		return backgroundSprite;
	}

	@Override
	public boolean isPlaying() {
		return backgroundSprite != null && backgroundSprite.isPlaying();
	}

	@Override
	public void nextFrame() {
		// Perform a null-check.
		if (backgroundSprite != null) {
			// Delegate to sprite.
			backgroundSprite.nextFrame();
		}
	}

	@Override
	public void paint(Graphics2D g) {
		// Do we have a background color?
		if (backgroundColor != null) {
			// Yes. Set it.
			g.setColor(backgroundColor);

			// Paint it.
			g.fillRect(0, 0, getIntWidth(), getIntHeight());
		}

		// Do we have a background sprite?
		if (backgroundSprite != null) {
			// Yes. Paint it.
			backgroundSprite.paint(g);
		}

		// Call super paint.
		super.paint(g);
	}

	@Override
	public void previousFrame() {
		// Perform a null-check.
		if (backgroundSprite != null) {
			// Delegate to sprite.
			backgroundSprite.previousFrame();
		}
	}

	/**
	 * Sets the background color of this component. Use {@code null} to indicate
	 * a transparent background color.
	 * 
	 * @param backgroundColor
	 *            the new background color
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Sets the background sprite for this object.
	 * 
	 * @param backgroundSprite
	 *            the new sprite, or {@code null} to clear the background sprite
	 */
	public void setBackgroundSprite(GSprite backgroundSprite) {
		// Assign.
		this.backgroundSprite = backgroundSprite;

		// Perform a null-check.
		if (backgroundSprite != null) {
			// Link sizes.
			setSize(backgroundSprite.getWidth(), backgroundSprite.getHeight());
		}
	}

	@Override
	public void setFrameNumber(int frameNumber)
			throws IndexOutOfBoundsException {
		// Perform a null-check.
		if (backgroundSprite != null) {
			// Delegate to sprite.
			backgroundSprite.setFrameNumber(frameNumber);
		}
	}

	@Override
	public void setHeight(double h) throws IllegalArgumentException {
		super.setHeight(h);

		// Perform a null-check.
		if (backgroundSprite != null) {
			// Update background.
			backgroundSprite.setHeight(getHeight());
		}
	}

	@Override
	public void setPlaying(boolean playing) {
		// Perform a null-check.
		if (backgroundSprite != null) {
			// Delegate to sprite.
			backgroundSprite.setPlaying(playing);
		}
	}

	@Override
	public void setWidth(double w) throws IllegalArgumentException {
		super.setWidth(w);

		// Perform a null-check.
		if (backgroundSprite != null) {
			// Update background.
			backgroundSprite.setWidth(getWidth());
		}
	}

}
