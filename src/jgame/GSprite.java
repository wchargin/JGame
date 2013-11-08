package jgame;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * An object whose display and bounding box are determined by an image, or
 * multiple images for an animation.
 * 
 * @author William Chargin
 * 
 */
public class GSprite extends GObject implements GAnimatable, ImageObserver {

	/**
	 * A primitive representation for a sprite. This allows sprites to take the
	 * "approximate" shape of a more primitive object for more accurate
	 * collisions.
	 * 
	 * @author William Chargin
	 * 
	 */
	public interface Primitive {

		/**
		 * Gets the bounding shape for the given sprite, according to the rules
		 * of this primitive.
		 * 
		 * @param sprite
		 *            the relevant sprite
		 * @return the bounding shape for the sprite
		 */
		public Shape getBoundingShape(GSprite sprite);

	}

	/**
	 * A collection of basic primitive shapes.
	 * 
	 * @author William Chargin
	 * 
	 */
	public enum PrimitiveShape implements Primitive {
		/**
		 * A rectangular or square primitive shape.
		 */
		RECTANGLE,

		/**
		 * An elliptical or circular primitive shape.
		 */
		ELLIPSE,

		/**
		 * An empty primitive shape, for invisible sprites.
		 */
		EMPTY;

		/**
		 * An alias for the {@link #RECTANGLE} shape.
		 */
		public static final PrimitiveShape SQUARE = RECTANGLE;

		/**
		 * An alias for the {@link #ELLIPSE} shape.
		 */
		public static final PrimitiveShape CIRCLE = ELLIPSE;

		/**
		 * An alias for the {@link #ELLIPSE} shape.
		 */
		public static final PrimitiveShape OVAL = ELLIPSE;

		@Override
		public Shape getBoundingShape(GSprite sprite) {
			switch (this) {
			case RECTANGLE:
				// Return a rectangle with the correct dimensions.
				return new java.awt.geom.Rectangle2D.Double(0, 0,
						sprite.getWidth(), sprite.getHeight());

			case ELLIPSE:
				// Return an ellipse with the correct dimensions.
				return new java.awt.geom.Ellipse2D.Double(0, 0,
						sprite.getWidth(), sprite.getHeight());

			case EMPTY:
				// Return an empty rectangle.
				return new java.awt.Rectangle(0, 0);

			default:
				// Unsure.
				return null;
			}
		}
	}

	/**
	 * A primitive object based on a Java {@link java.awt.Shape}.
	 * 
	 * @author William Chargin
	 * 
	 */
	public class ShapePrimitive implements Primitive {

		/**
		 * The shape used for this primitive.
		 */
		private Shape shape;

		/**
		 * Creates the primitive with the given shape.
		 * 
		 * @param shape
		 *            the shape to use
		 * @throws IllegalArgumentException
		 *             if {@code shape} is {@code null}
		 */
		public ShapePrimitive(Shape shape) throws IllegalArgumentException {
			super();
			setShape(shape);
		}

		@Override
		public Shape getBoundingShape(GSprite sprite) {
			return shape;
		}

		/**
		 * Gets the shape. This is equivalent to
		 * {@link #getBoundingShape(GSprite)}.
		 * 
		 * @return the current shape
		 */
		public Shape getShape() {
			return shape;
		}

		/**
		 * Sets the shape to the given shape.
		 * 
		 * @param shape
		 *            the new shape
		 * @throws IllegalArgumentException
		 *             if {@code shape} is {@code null}
		 */
		public void setShape(Shape shape) throws IllegalArgumentException {
			// Perform a null check.
			if (shape == null) {
				// shape == null.
				throw new IllegalArgumentException("shape == null");
			}
			this.shape = shape;
		}

	}

	/**
	 * Attempts to load an image from the given file.
	 * 
	 * @param imageFile
	 *            the file containing the image to load
	 * @return the image, or {@code null} if the loading fails for any reason
	 */
	public static Image loadImageFromFile(File imageFile) {
		try {
			return ImageIO.read(imageFile);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
	}

	/**
	 * The center rectangle for nine-slice scaling.
	 */
	private Rectangle nineSliceCenter;

	/**
	 * The list of images that make up this sprite. This may contain one image
	 * (for a still sprite), more than one image (for an animated sprite), or no
	 * images at all (in which case the sprite will not be drawn and no error
	 * will be thrown).
	 */
	private final List<Image> images;

	/**
	 * The primitive shape used for this object's bounding shape calculations.
	 * Default is {@link PrimitiveShape#RECTANGLE}.
	 */
	private Primitive primitive = PrimitiveShape.RECTANGLE;

	/**
	 * The current frame index.
	 */
	private int frame;

	/**
	 * Whether or not the animation is currently playing.
	 */
	private boolean playing = true;

	/**
	 * Creates the sprite with a blank list of images.
	 */
	public GSprite() {
		images = new ArrayList<Image>();
	}

	/**
	 * Creates a new sprite by copying all the properties from another sprite.
	 * 
	 * @param other
	 *            the sprite whose properties to copy
	 */
	public GSprite(GSprite other) {
		super();
		this.images = new ArrayList<Image>(other.images);
		this.nineSliceCenter = other.nineSliceCenter == null ? null
				: new Rectangle(other.nineSliceCenter);
		this.primitive = other.primitive;
		this.frame = other.frame;
		this.playing = other.playing;
	}

	/**
	 * Creates the sprite with the given image.
	 * 
	 * @param image
	 *            the image for this sprite
	 * @see #setImage(Image)
	 */
	public GSprite(Image image) {
		this();
		setImage(image);
	}

	/**
	 * Creates the sprite with the given image sequence. This constructor is
	 * equivalent to {@link #GSprite(List)} except that it accepts an array
	 * instead of a collection.
	 * 
	 * @param sequence
	 *            the image sequence
	 */
	public GSprite(Image[] sequence) {
		this(Arrays.asList(sequence));
	}

	/**
	 * Creates the sprite with the given image sequence.
	 * <p>
	 * This constructor is equivalent to {@link #GSprite(Image[])} except that
	 * it accepts a collection instead of an array.
	 * 
	 * @param sequence
	 *            the image sequence
	 * @see #setImages(List)
	 */
	public GSprite(List<Image> sequence) {
		this();
		setImages(sequence);
	}

	@Override
	public Shape getBoundingShape() {
		// Can we delegate to the primitive?
		if (primitive != null) {
			// Do so.
			return primitive.getBoundingShape(this);
		} else {
			// For some reason, it's {@code null}. Delegate to super.
			return super.getBoundingShape();
		}
	}

	/**
	 * Gets the center rectangle for nine-slice scaling.
	 * 
	 * @return the center rectangle, or {@code null} if none has been set
	 */
	public Rectangle getNineSliceCenter() {
		return nineSliceCenter;
	}

	/**
	 * Gets the primitive shape used for this sprite's bounding shape
	 * calculations.
	 * 
	 * @return the primitive object
	 */
	public Primitive getPrimitive() {
		return primitive;
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		if (getWidth() <= 0 && (infoflags & WIDTH) != 0) {
			// The image's width has been finalized.
			setWidth(width);
		}
		if (getHeight() <= 0 && (infoflags & HEIGHT) != 0) {
			// The image's height has been finalized.
			setHeight(height);
		}
		return getWidth() > 0 && getHeight() > 0;
	}

	@Override
	public boolean isPlaying() {
		return playing;
	}

	@Override
	public void nextFrame() {
		if (images.size() <= 1 || !playing) {
			// There's no need to do anything.
			return;
		}
		// Increment the frame by one.
		// This is equivalent to:
		// frame += 1;
		// It is also equivalent to:
		// frame = frame + 1;
		frame++;

		// Take the modulo of the frame count.
		// This is equivalent to:
		// frame = frame % images.size();
		frame %= images.size();
	}

	@Override
	public void paint(Graphics2D g) {
		if (!images.isEmpty()) {
			// There is at least one image to paint.
			Image i = images.get(frame);

			// Make sure it's non-null.
			if (i != null) {
				// Is nine-slice scaling enabled?
				if (nineSliceCenter != null) {
					// Paint nine-slice scaling.
					paintNineSlice(g, i);
				} else {
					// Nine-slice scaling is disabled. Just draw the image.
					g.drawImage(i, 0, 0, null);
				}
			}
		}

		// Paint children.
		super.paint(g);
	}

	/**
	 * Paints nine-slice scaling for the given image onto the given graphics
	 * context.
	 * 
	 * @param g
	 *            the graphics context on which to paint
	 * @param i
	 *            the image to paint
	 */
	private void paintNineSlice(Graphics2D g, Image i) {
		// Get constants for starting positions.
		final int left = nineSliceCenter.x;
		final int top = nineSliceCenter.y;
		final int right = left + nineSliceCenter.width;
		final int bottom = top + nineSliceCenter.height;

		// Size: image, this.
		final int iWidth = i.getWidth(this);
		final int iHeight = i.getHeight(this);
		final int tWidth = getIntWidth();
		final int tHeight = getIntHeight();

		// Extent.
		final int rightWidth = iWidth - right;
		final int bottomHeight = iHeight - bottom;

		// Slices:
		//
		// 1|22222|3
		// -+-----+-
		// 4|55555|6
		// 4|55555|6
		// 4|55555|6
		// -+-----+-
		// 7|88888|9

		// Draw the corners.
		// Slice 1, top-left:
		g.drawImage(i, 0, 0, left, top, 0, 0, left, top, this);

		// Slice 3, top-right:
		g.drawImage(i, tWidth - rightWidth, 0, tWidth, top, right, 0, iWidth,
				top, this);

		// Slice 7, bottom-left:
		g.drawImage(i, 0, tHeight - bottomHeight, left, tHeight, 0, bottom,
				left, iHeight, this);

		// Slice 9, bottom-right:
		g.drawImage(i, tWidth - rightWidth, tHeight - bottomHeight, tWidth,
				tHeight, right, bottom, iWidth, iHeight, this);

		// Draw the edges.
		// Slice 4, left:
		g.drawImage(i, 0, top, left, tHeight - bottomHeight, 0, top, left,
				bottom, this);

		// Slice 2, top:
		g.drawImage(i, left, 0, tWidth - rightWidth, top, left, 0, right, top,
				this);

		// Slice 6, right:
		g.drawImage(i, tWidth - rightWidth, top, tWidth,
				tHeight - bottomHeight, right, top, iWidth, bottom, this);

		// Slice 8, bottom:
		g.drawImage(i, left, tHeight - bottomHeight, tWidth - rightWidth,
				tHeight, left, bottom, right, iHeight, this);

		// Slice 5: draw the center.
		g.drawImage(i, left, top, tWidth - rightWidth, tHeight - bottomHeight,
				left, top, right, bottom, null);
	}

	@Override
	public void preparePaint(Graphics2D g) {
		super.preparePaint(g);
		goodImageTransforms(g);
	}

	@Override
	public void previousFrame() {
		if (images.isEmpty() || !playing) {
			// There's no need to do anything.
		}
		// Decrement the frame by one.
		// This is equivalent to:
		// frame -= 1;
		// It is also equivalent to:
		// frame = frame - 1;
		frame--;

		// Take the modulo of the frame count.
		// This is equivalent to:
		// frame = frame % images.size();
		frame %= images.size();
	}

	@Override
	public void setFrameNumber(int frameNumber)
			throws IndexOutOfBoundsException {
		// Can't be more than or equal to the number of images.
		if (frameNumber >= images.size()) {
			throw new IndexOutOfBoundsException("frameNumber >= images.size ("
					+ frameNumber + " .= " + images.size() + ")");
		}

		// Can't be less than zero.
		if (frameNumber < 0) {
			throw new IndexOutOfBoundsException("frameNumber < 0 ("
					+ frameNumber + ")");
		}

		// Otherwise, okay.
		this.frame = frameNumber;
	}

	/**
	 * Sets this sprite's image to the given image. All other image(s), if any,
	 * will be removed from the list. The sprite will be resized to the size of
	 * the image. The frame number will also be set to {@code 0}.
	 * <p>
	 * All other constructors cascade to this constructor.
	 * 
	 * @param image
	 *            the new image
	 */
	public void setImage(Image image) {
		// Remove all other images from the list.
		images.clear();

		// Resize this object, but make sure we don't go negative.
		int w = image.getWidth(this);
		int h = image.getHeight(this);
		if (w > 0 && getWidth() <= 0) {
			setWidth(w);
		}
		if (h > 0 && getHeight() <= 0) {
			setHeight(h);
		}

		// Add this object to the list of images.
		images.add(image);

		// Reset the frame number to zero.
		frame = 0;
	}

	/**
	 * This method delegates capabilities to {@link #setImages(List)} using
	 * {@link Arrays#asList(Object...)}.
	 * <p>
	 * This method is equivalent to {@link #setImages(List)} except that it
	 * accepts an array instead of a collection.
	 * 
	 * @param sequence
	 *            the new image sequence
	 * @see #setImages(List)
	 */
	public void setImages(Image... sequence) {
		setImages(Arrays.asList(sequence));
	}

	/**
	 * Sets this sprite's image sequence to the given list of images. All other
	 * image(s), if any, will be removed from the list. The sprite will be
	 * resized to the size of the first image, if the list of images is
	 * non-empty. The frame number will also be set to {@code 0}.
	 * <p>
	 * This method is equivalent to {@link #setImages(Image[])} except that it
	 * accepts a collection instead of an array.
	 * 
	 * @param sequence
	 *            the new image sequence
	 */
	public void setImages(List<Image> sequence) {
		// First, use `setImage` to size the component to the first image, if
		// possible.
		if (!sequence.isEmpty()) {
			setImage(sequence.get(0));
		}

		// Now, remove all images from the list.
		images.clear();

		// Add all the images to the sequence.
		images.addAll(sequence);
	}

	/**
	 * Sets the center rectangle for nine-slice scaling.
	 * 
	 * @param nineSliceCenter
	 *            the new center, or {@code null} to disable nine-slice scaling
	 */
	public void setNineSliceCenter(Rectangle nineSliceCenter) {
		this.nineSliceCenter = nineSliceCenter;
	}

	@Override
	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	/**
	 * Sets the primitive shape used for this sprite's bounding shape
	 * calculations.
	 * 
	 * @param primitive
	 *            the primitive object
	 * @throws IllegalArgumentException
	 *             if {@code primitive} is {@code null}
	 */
	public void setPrimitive(Primitive primitive)
			throws IllegalArgumentException {
		// Make sure it's non-null.
		if (primitive == null) {
			// The argument is null.
			throw new IllegalArgumentException("primitive == null");
		}
		this.primitive = primitive;
	}
}
