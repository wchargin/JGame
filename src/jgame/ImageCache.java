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
	 * The default image cache, used for static convenience methods.
	 */
	private static ImageCache defaultCache;

	/**
	 * Creates and returns an image cache, and sets the cache as the default
	 * cache.
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
	 * Creates and returns an image cache with the given filename prefix, and
	 * sets the cache as the default cache.
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
	 * Gets the default image cache.
	 * 
	 * @return the default cache
	 */
	public static ImageCache getDefaultCache() {
		return defaultCache;
	}

	/**
	 * Gets the cached object with the given filename from the default cache. If
	 * the object does not yet exist, it will be cached and returned.
	 * 
	 * @param fileName
	 *            the relevant file name
	 * @return the generated object
	 */
	public static BufferedImage getImage(String fileName) {
		return defaultCache.get(fileName);
	}

	/**
	 * Gets images in sequential order from the default cache. This method does
	 * not include leading zeroes. For example, a set of images named
	 * {@code image1.png}, {@code image2.png}, and {@code image3.png} could be
	 * retrieved with a call to:
	 * 
	 * <pre>
	 * getSequentialImages(&quot;image&quot;, 1, 3, &quot;.png&quot;);
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
	public static List<Image> getSequentialImages(String prefix, int start,
			int end, String suffix) {
		return getSequentialImages(prefix, start, end, suffix, 0);
	}

	/**
	 * Gets the images in sequential order from the default cache. This method
	 * includes a given number of leading zeroes. For example, a set of images
	 * named {@code image09.png} , {@code image10.png}, and {@code image11.png}
	 * could be retrieved with a call to:
	 * 
	 * <pre>
	 * getSequential(&quot;image&quot;, 9, 11, &quot;.png&quot;, 1);
	 * </pre>
	 * 
	 * This can be passed directly to a {@link GSprite}
	 * {@linkplain GSprite#GSprite(List) constructor} to create an animated
	 * sprite:
	 * 
	 * <pre>
	 * GSprite spr = new GSprite(cache.getSequential(&quot;image&quot;, 9, 11, &quot;.png&quot;, 1));
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
	 * @param zeroDecimal
	 *            the decimal place to be zero-filled use this if you have
	 *            images labeled, for example, {@code image0001.png} instead of
	 *            just {@code image1.png}. The precise handling of leading
	 *            zeroes is such that the numbered portion of the file name will
	 *            always have length greater than this argument. For example,
	 *            with this argument set to {@code 2}, the number {@code 7}
	 *            would be transformed to {@code 007} and {@code 81} would
	 *            become {@code 081}, while {@code 123} and {@code 5678} would
	 *            be unaffected. You can think of this argument as the number of
	 *            zeroes to prepend to any single digit number to force the
	 *            resultant string into a valid format.
	 * @return a list of the images matching the criteria
	 * @throws IllegalArgumentException
	 *             if any of the images could not be found
	 * @since 1.2
	 */
	public static List<Image> getSequentialImages(String prefix, int start,
			int end, String suffix, int zeroDecimal) {
		return defaultCache.getSequential(prefix, start, end, suffix,
				zeroDecimal);
	}

	/**
	 * Creates a sprite with the images in sequential order. This method does
	 * not include leading zeroes. For example, a sprite with the set of images
	 * named {@code image1.png}, {@code image2.png}, and {@code image3.png}
	 * could be retrieved with a call to:
	 * 
	 * <pre>
	 * getSequentialSprite(&quot;image&quot;, 1, 3, &quot;.png&quot;);
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
	public static GSprite getSequentialSprite(String prefix, int start,
			int end, String suffix) {
		return getSequentialSprite(prefix, start, end, suffix, 0);
	}

	/**
	 * Creates a sprite with the images in sequential order. This method does
	 * not include leading zeroes. For example, a sprite with the set of images
	 * named {@code image1.png}, {@code image2.png}, and {@code image3.png}
	 * could be retrieved with a call to:
	 * 
	 * <pre>
	 * getSequentialSprite(&quot;image&quot;, 1, 3, &quot;.png&quot;);
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
	 * @param zeroDecimal
	 *            the decimal place to be zero-filled use this if you have
	 *            images labeled, for example, {@code image0001.png} instead of
	 *            just {@code image1.png}. The precise handling of leading
	 *            zeroes is such that the numbered portion of the file name will
	 *            always have length greater than this argument. For example,
	 *            with this argument set to {@code 2}, the number {@code 7}
	 *            would be transformed to {@code 007} and {@code 81} would
	 *            become {@code 081}, while {@code 123} and {@code 5678} would
	 *            be unaffected. You can think of this argument as the number of
	 *            zeroes to prepend to any single digit number to force the
	 *            resultant string into a valid format.
	 * @return a list of the images matching the criteria
	 * @throws IllegalArgumentException
	 *             if any of the images could not be found
	 * @since 1.2
	 */
	public static GSprite getSequentialSprite(String prefix, int start,
			int end, String suffix, int zeroDecimal) {
		return new GSprite(getSequentialImages(prefix, start, end, suffix,
				zeroDecimal));
	}

	/**
	 * Craetes a sprite with the image with the given filename from the default
	 * cache. If the object does not yet exist, it will be cached and returned.
	 * 
	 * @param fileName
	 *            the relevant file name
	 * @return the generated object
	 */
	public static GSprite getSprite(String fileName) {
		return new GSprite(defaultCache.get(fileName));
	}

	/**
	 * Sets the default image cache.
	 * 
	 * @param defaultCache
	 *            the default image cache
	 */
	public static void setDefaultCache(ImageCache defaultCache) {
		ImageCache.defaultCache = defaultCache;
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
	 * Creates the image cache with no prefix, and sets the cache as the default
	 * cache.
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
	 * Creates the image cache with the given filename prefix, and sets the
	 * cache as the default cache.
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

		// Assign as default cache.
		defaultCache = this;
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
	 * zeroes and is equivalent to calling
	 * {@link #getSequential(String, int, int, String, int)} with a
	 * {@code zeroDecimal} argument of {@code 0}. For example, a set of images
	 * named {@code image1.png}, {@code image2.png}, and {@code image3.png}
	 * could be retrieved with a call to:
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
		return getSequential(prefix, start, end, suffix, 0);
	}

	/**
	 * Gets the images in sequential order. This method includes a given number
	 * of leading zeroes. For example, a set of images named {@code image09.png}
	 * , {@code image10.png}, and {@code image11.png} could be retrieved with a
	 * call to:
	 * 
	 * <pre>
	 * getSequential(&quot;image&quot;, 9, 11, &quot;.png&quot;, 1);
	 * </pre>
	 * 
	 * This can be passed directly to a {@link GSprite}
	 * {@linkplain GSprite#GSprite(List) constructor} to create an animated
	 * sprite:
	 * 
	 * <pre>
	 * GSprite spr = new GSprite(cache.getSequential(&quot;image&quot;, 9, 11, &quot;.png&quot;, 1));
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
	 * @param zeroDecimal
	 *            the decimal place to be zero-filled use this if you have
	 *            images labeled, for example, {@code image0001.png} instead of
	 *            just {@code image1.png}. The precise handling of leading
	 *            zeroes is such that the numbered portion of the file name will
	 *            always have length greater than this argument. For example,
	 *            with this argument set to {@code 2}, the number {@code 7}
	 *            would be transformed to {@code 007} and {@code 81} would
	 *            become {@code 081}, while {@code 123} and {@code 5678} would
	 *            be unaffected. You can think of this argument as the number of
	 *            zeroes to prepend to any single digit number to force the
	 *            resultant string into a valid format.
	 * @return a list of the images matching the criteria
	 * @throws IllegalArgumentException
	 *             if any of the images could not be found
	 * @since 1.2
	 */
	public List<Image> getSequential(String prefix, int start, int end,
			String suffix, int zeroDecimal) throws IllegalArgumentException {
		// Create the list to be returned.
		List<Image> result = new ArrayList<Image>();

		// Process each one.
		for (int i = start; i <= end; i++) {
			String numberPortion = Integer.toString(i);

			// Prepend leading zeroes.
			while (numberPortion.length() <= zeroDecimal) {
				numberPortion = '0' + numberPortion;
			}

			result.add(get(prefix + numberPortion + suffix));
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
