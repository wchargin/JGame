package jgame;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.imageio.ImageIO;

/**
 * A cache for images in a game.
 * 
 * @author William Chargin
 * 
 */
public class ImageCache {

	/**
	 * The map of classes to caches.
	 */
	private static final Map<Class<?>, ImageCache> caches = new HashMap<Class<?>, ImageCache>();

	/**
	 * Creates and returns an image cache.
	 * 
	 * @param clazz
	 *            the main game class to which this cache applies
	 * @return the new cache
	 * @throws IllegalArgumentException
	 *             if the class is {@code null}
	 */
	public static ImageCache create(Class<?> clazz)
			throws IllegalArgumentException {
		return create(clazz, new String());
	}

	/**
	 * Creates and returns an image cache with the given filename prefix.
	 * 
	 * @param clazz
	 *            the main game class to which this cache applies
	 * @param prefix
	 *            the filename prefix
	 * @return the new cache
	 * @throws IllegalArgumentException
	 *             if the class is {@code null}
	 */
	public static ImageCache create(Class<?> clazz, String prefix)
			throws IllegalArgumentException {
		// Create the cache.
		ImageCache cache = new ImageCache(clazz, prefix);

		// Add this to the list of caches.
		caches.put(clazz, cache);

		// Return the cache.
		return cache;
	}

	/**
	 * Gets the image cache for the given class.
	 * 
	 * @param clazz
	 *            the class to fetch the image cache for
	 * @return the image cache, or {@code null} if none exists
	 */
	public static ImageCache forClass(Class<?> clazz) {
		return caches.get(clazz);
	}

	/**
	 * The cache of file names to objects.
	 */
	private final WeakHashMap<String, BufferedImage> cache;

	/**
	 * The main game class.
	 */
	protected Class<?> clazz;

	/**
	 * The prefix for filenames passed to this cache.
	 */
	private String prefix;

	/**
	 * Creates the image cache with no prefix.
	 * 
	 * @param clazz
	 *            the main game class to which this cache applies
	 * @throws IllegalArgumentException
	 *             if the class is {@code null}
	 */
	private ImageCache(Class<?> clazz) throws IllegalArgumentException {
		this(clazz, new String());
	}

	/**
	 * Creates the image cache with the given filename prefix.
	 * 
	 * @param clazz
	 *            the main game class to which this cache applies
	 * @param prefix
	 *            the filename prefix
	 * @throws IllegalArgumentException
	 *             if the class is {@code null}
	 */
	private ImageCache(Class<?> clazz, String prefix)
			throws IllegalArgumentException {
		// Is the class null?
		if (clazz == null) {
			// Yes.
			throw new IllegalArgumentException("clazz == null");
		}

		// Cache the class.
		this.clazz = clazz;

		// Create the map.
		cache = new WeakHashMap<String, BufferedImage>();

		// Set the prefix.
		setPrefix(prefix == null ? new String() : prefix);
	}

	/**
	 * Gets the cached object with the given filename. If the object does not
	 * yet exist, it will be cached and returned.
	 * 
	 * @param fileName
	 *            the relevant file name
	 * @return the generated object
	 */
	public BufferedImage get(String fileName) {
		// Do we have it already?
		// Define the key.
		final String fullPath = getPrefix() + fileName;

		// Check to see what we have first.
		BufferedImage image = cache.get(fullPath);

		// Do we already have something?
		if (image != null) {
			// No need for further action.
			return image;
		}

		// Create the URL.
		URL url = clazz.getResource(fullPath);

		// Is it valid?
		if (url == null) {
			// No.
			throw new IllegalArgumentException("Unknown resource "
					+ getPrefix() + fileName);
		}

		// Create an image, then.
		try {
			image = ImageIO.read(url);
		} catch (IOException e) {
			// That's weird.
			e.printStackTrace();
			return null;
		}

		// Add it to the map.
		cache.put(fullPath, image);

		// Return the object.
		return image;
	}

	/**
	 * Gets the filename prefix for this cache.
	 * 
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Gets the images in sequential order. This method does not include leading
	 * zeroes. For example, a set of images named {@code image1.png},
	 * {@code image2.png}, and {@code image3.png} could be retrieved with a call
	 * to:
	 * 
	 * <pre>
	 * getSequential(&quot;image&quot;, 1, 3, &quot;.png&quot;);
	 * </pre>
	 * 
	 * This can be passed directly to a {@link GSprite}
	 * {@linkplain GSprite#GSprite(List) constructor} to create an animated
	 * sprite:
	 * 
	 * <pre>
	 * GSprite spr = new GSprite(cache.getSequential(&quot;image&quot;, 1, 3, &quot;.png&quot;));
	 * </pre>
	 * 
	 * @param prefix
	 *            the prefix before the numbers
	 * @param start
	 *            the number to start at (inclusive)
	 * @param end
	 *            the number to end at (inclusive)
	 * @param suffix
	 *            the suffix after the numbers (should include file extension)
	 * @return a list of the images matching the criteria
	 * @throws IllegalArgumentException
	 *             if any of the images could not be found
	 * @since 1.2
	 */
	public List<Image> getSequential(String prefix, int start, int end,
			String suffix) throws IllegalArgumentException {
		// Create the list to be returned.
		List<Image> result = new ArrayList<Image>();

		// Process each one.
		for (int i = start; i <= end; i++) {
			result.add(get(prefix + Integer.toString(i) + suffix));
		}

		// Return.
		return result;
	}

	/**
	 * Removes the image with the given name from the cache.
	 * 
	 * @param fileName
	 *            the name of the file to remove
	 * @return the image that was removed, or {@code null} if none existed
	 */
	public Image remove(String fileName) {
		return cache.remove(getPrefix() + fileName);
	}

	/**
	 * Sets the filename prefix for this cache. Imagehis will only affect cache
	 * objects that have not yet begun loading.
	 * 
	 * @param prefix
	 *            the new prefix, or {@code null} to clear the prefix
	 */
	public void setPrefix(String prefix) {
		// Perform a null check.
		if (prefix == null) {
			// Use a blank string.
			setPrefix(new String());
			return;
		}

		// Set the prefix.
		this.prefix = prefix;
	}

}
