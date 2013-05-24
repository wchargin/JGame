package jgame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

/**
 * A cache for sounds.
 * 
 * @author William Chargin
 * 
 */
public class SoundManager {

	/**
	 * A sound reference that can be stopped by the user.
	 * 
	 * @author William Chargin
	 * 
	 */
	public static class Sound {

		/**
		 * The list of all sounds, as weak references.
		 */
		private static final Vector<WeakReference<Sound>> allSounds = new Vector<WeakReference<Sound>>();

		/**
		 * The clip for this sound.
		 */
		private Clip clip;

		/**
		 * Creates the sound with the given clip.
		 * 
		 * @param clip
		 *            the clip for this sound.
		 * @throws IllegalArgumentException
		 *             if {@code clip} is {@code null}
		 */
		private Sound(Clip clip) throws IllegalArgumentException {
			// Perform a null-check.
			if (clip == null) {
				// No.
				throw new IllegalArgumentException("clip == null");
			}

			// Set the clip.
			this.clip = clip;

			// Add to the sound list.
			allSounds.add(new WeakReference<Sound>(this));

			// Drain when done.
			clip.addLineListener(new LineListener() {

				@Override
				public void update(LineEvent le) {
					if (le.getType() == LineEvent.Type.STOP) {
						if (le.getLine() instanceof Clip) {
							Clip clip = (Clip) le.getLine();
							clip.drain();
							clip.close();
							Sound.this.clip = null;
						}
					}
				}
			});
		}

		/**
		 * Mutes or unmutes this sound.
		 * 
		 * @param mute
		 *            {@code true} to mute or {@code false} to unmute
		 */
		public void setMute(boolean mute) {
			// Catch any errors.
			try {
				// Can we mute?
				if (clip.isControlSupported(BooleanControl.Type.MUTE)) {
					// Get the control.
					BooleanControl muteControl = (BooleanControl) clip
							.getControl(BooleanControl.Type.MUTE);

					// Mute or unmute.
					muteControl.setValue(mute);
				}
			} catch (Exception e) {
				// The get control or cast operation failed.
				// That's okay, but let them know.
				e.printStackTrace();
			}
		}

		/**
		 * Stops this sound if it is playing.
		 * 
		 * @throws IllegalStateException
		 *             if the clip has already stopped
		 */
		public void stop() throws IllegalStateException {
			// Make sure we can stop.
			if (clip == null) {
				// Already stopped.
				throw new IllegalStateException(
						"The sound has already stopped.");
			}
			clip.stop();
			clip.drain();
		}
	}

	/**
	 * Gets the sound manager for the given class.
	 * 
	 * @param clazz
	 *            the class to fetch the sound manager for
	 * @return the sound manager, or {@code null} if none exists
	 */
	public static SoundManager forClass(Class<?> clazz) {
		return caches.get(clazz);
	}

	/**
	 * Stops all sounds currently playing.
	 */
	public static void stopAllSounds() {
		// Get an iterator.
		Iterator<WeakReference<Sound>> iterator = Sound.allSounds.iterator();

		// Loop.
		while (iterator.hasNext()) {
			// Get the reference.
			WeakReference<Sound> soundReference = iterator.next();

			// Is it non-null?
			if (soundReference != null) {
				// Get the referenced object.
				Sound sound = soundReference.get();

				// Is it still referenced?
				if (sound != null && sound.clip != null) {
					try {
						// Stop it.
						sound.stop();
					} catch (IllegalStateException ise) {
						// Just in case. That's okay.
						ise.printStackTrace();
					}
				}
			}

			// In any case, remove it from the list.
			iterator.remove();
		}
	}

	/**
	 * The byte cache.
	 */
	private Map<String, ByteArrayInputStream> byteCache;

	/**
	 * The main game class.
	 */
	protected Class<?> clazz;

	/**
	 * The prefix for filenames passed to this cache.
	 */
	private String prefix;

	/**
	 * The map of classes to caches.
	 */
	private static final Map<Class<?>, SoundManager> caches = new HashMap<Class<?>, SoundManager>();

	/**
	 * Creates and returns a sound manager.
	 * 
	 * @param clazz
	 *            the main game class to which this cache applies
	 * @return the new cache
	 * @throws IllegalArgumentException
	 *             if the class is {@code null}
	 */
	public static SoundManager create(Class<?> clazz)
			throws IllegalArgumentException {
		return create(clazz, new String());
	}

	/**
	 * Creates and returns a sound manager with the given filename prefix.
	 * 
	 * @param clazz
	 *            the main game class to which this cache applies
	 * @param prefix
	 *            the filename prefix
	 * @return the new cache
	 * @throws IllegalArgumentException
	 *             if the class is {@code null}
	 */
	public static SoundManager create(Class<?> clazz, String prefix)
			throws IllegalArgumentException {
		// Create the cache.
		SoundManager cache = new SoundManager(clazz, prefix);

		// Add this to the list of caches.
		caches.put(clazz, cache);

		// Return the cache.
		return cache;
	}

	/**
	 * Creates the sound cache with no prefix.
	 * 
	 * @param clazz
	 *            the main game class to which this cache applies
	 * @throws IllegalArgumentException
	 *             if the class is {@code null}
	 */
	private SoundManager(Class<?> clazz) {
		this(clazz, new String());
	}

	/**
	 * Creates the sound manager with the given filename prefix.
	 * 
	 * @param clazz
	 *            the main game class to which this cache applies
	 * @param prefix
	 *            the filename prefix
	 * @throws IllegalArgumentException
	 *             if the class is {@code null}
	 */
	private SoundManager(Class<?> clazz, String prefix)
			throws IllegalArgumentException {
		this.clazz = clazz;
		setPrefix(prefix);
		byteCache = new HashMap<String, ByteArrayInputStream>();
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
	 * Starts a sound with the given name. The sound will loop forever.
	 * 
	 * @param name
	 *            the filename of the clip to play
	 * @return a sound reference; you can use this to stop the sound
	 */
	public Sound loopForever(String name) {
		// Get the sound.
		Sound sound = play(name);

		// Loop.
		sound.clip.loop(Clip.LOOP_CONTINUOUSLY);

		// Return.
		return sound;
	}

	/**
	 * Starts a sound with the given name. The sound will play once.
	 * 
	 * @param name
	 *            the filename of the clip to play
	 * @return a sound reference; you can use this to stop the clip before it
	 *         has finished
	 */
	public Sound play(String name) {
		try {
			// Create a clip.
			Clip clip = AudioSystem.getClip();

			// Find the full name.
			final String fullPath = prefix + name;

			// See what we have already.
			ByteArrayInputStream theseBytes = byteCache.get(fullPath);

			// Have we found the bytes yet?
			if (theseBytes == null) {
				// Nope. Read it in.
				InputStream is = clazz.getResourceAsStream(fullPath);

				// Output to a temporary stream.
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				// Loop.
				for (int b; (b = is.read()) != -1;) {
					// Write it.
					baos.write(b);
				}

				// Close the input stream now.
				is.close();

				// Create a byte array.
				theseBytes = new ByteArrayInputStream(baos.toByteArray());

				// Put in map for later reference.
				byteCache.put(fullPath, theseBytes);
			}

			// Get a BAIS.
			ByteArrayInputStream bais = theseBytes;
			bais.reset();
			// Convert to an audio stream.
			AudioInputStream ais = AudioSystem.getAudioInputStream(bais);

			// Open the clip.
			clip.open(ais);

			// Close the stream to prevent a memory leak.
			ais.close();

			// Start the clip.
			clip.start();

			// Create a new Sound and return it.
			return new Sound(clip);
		} catch (Exception e) {
			// If they're watching, let them know.
			e.printStackTrace();

			// Nothing to do here.
			return null;
		}
	}

	/**
	 * Sets the filename prefix for this cache. This will only affect cache
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
