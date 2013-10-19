package jgame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import org.newdawn.easyogg.OggClip;

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
		 * The clip for this sound, if it's an OGG file.
		 */
		private OggClip ogg;

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
		 * Creates the sound with the given OGG input stream.
		 * 
		 * @param in
		 *            the input stream
		 */
		private Sound(InputStream in) {
			try {
				ogg = new OggClip(in);
				allSounds.add(new WeakReference<Sound>(this));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Pauses or resumes this sound.
		 * 
		 * @param pause
		 *            {@code true} to pause or {@code false} to resume
		 */
		public void setPause(boolean pause) {
			if (ogg != null) {
				if (pause) {
					ogg.pause();
				} else {
					ogg.resume();
				}
			}

			if (clip != null) {
				// Catch any errors.
				try {
					// Can we mute?
					if (clip.isControlSupported(BooleanControl.Type.MUTE)) {
						// Get the control.
						BooleanControl muteControl = (BooleanControl) clip
								.getControl(BooleanControl.Type.MUTE);

						// Mute or unmute.
						muteControl.setValue(pause);
					}
				} catch (Exception e) {
					// The get control or cast operation failed.
					// That's okay, but let them know.
					e.printStackTrace();
				}
			}
		}

		/**
		 * Stops this sound if it is playing.
		 */
		public void stop() {
			if (ogg != null) {
				ogg.stop();
			}

			// Make sure we can stop.
			if (clip != null) {
				clip.stop();
				clip.drain();
			}
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
	 * Starts a sound with the given name from the default cache. The sound will
	 * loop forever.
	 * 
	 * @param name
	 *            the filename of the clip to play
	 * @return a sound reference; you can use this to stop the sound
	 */
	public static Sound loopSoundForever(String name) {
		return defaultCache.loopForever(name);
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
	 * The default sound manager, used for static convenience methods.
	 */
	private static SoundManager defaultCache;

	/**
	 * Creates and returns a sound manager, and sets the cache as the default
	 * cache.
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
	 * Creates and returns a sound manager with the given filename prefix, and
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
	 * Starts a sound with the given name from the default cache. The sound will
	 * play once.
	 * 
	 * @param name
	 *            the filename of the clip to play
	 * @return a sound reference; you can use this to stop the clip before it
	 *         has finished
	 */
	public static Sound playSound(String name) {
		return defaultCache.play(name);
	}

	/**
	 * Preloads the given sound from the default cache, but does not play it.
	 * The sound can be played later with the {@link #play(String)} method.
	 * 
	 * @param name
	 *            the filename of the clip to play
	 * @return {@code true} if the preloading succeeded or has already been
	 *         performed successfully, or {@code false} if it failed
	 */
	public static boolean preloadSound(String name) {
		return defaultCache.preload(name);
	}

	/**
	 * Preloads the given sound from the default cache asynchronously. This
	 * method will not block; that is, it will not wait for the sound to load
	 * before returning.
	 * 
	 * @param name
	 *            the filename of the sound to preload
	 */
	public static synchronized void preloadSoundAsync(final String name) {
		defaultCache.preloadAsync(name);
	}

	/**
	 * The list of sound names currently being loaded.
	 */
	private final Set<String> currentlyPreloading = new HashSet<String>();

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
	 * Creates the sound manager with the given filename prefix and sets this as
	 * the default cache.
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

		// Assign as default cache.
		defaultCache = this;
	}

	/**
	 * Gets a clip from the given file name.
	 * 
	 * @param name
	 *            the filename
	 * @return a clip reference
	 */
	private Clip createClip(String name) {
		// Make sure it's loaded first.
		preload(name);

		// Get a BAIS.
		ByteArrayInputStream bais = byteCache.get(prefix + name);
		bais.reset();

		try {
			// Get us a clip.
			Clip clip = AudioSystem.getClip();

			// Convert to an audio stream.
			AudioInputStream ais = AudioSystem.getAudioInputStream(bais);

			// Open the clip.
			clip.open(ais);

			// Close the stream to prevent a memory leak.
			ais.close();

			// Create a new Sound and return it.
			return clip;
		} catch (Exception e) {
			// Nothing really to do.
			e.printStackTrace();
			return null;
		}

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
		if (sound.clip != null) {
			sound.clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
		if (sound.ogg != null) {
			sound.ogg.loop();
		}

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
		if (name.toLowerCase().endsWith(".ogg")) {
			preload(name);
			return new Sound(byteCache.get(prefix + name));
		} else {
			Clip clip = createClip(name);
			clip.start();
			return new Sound(clip);
		}
	}

	/**
	 * Preloads the given sound, but does not play it. The sound can be played
	 * later with the {@link #play(String)} method.
	 * 
	 * @param name
	 *            the filename of the clip to play
	 * @return {@code true} if the preloading succeeded or has already been
	 *         performed successfully, or {@code false} if it failed
	 */
	public boolean preload(String name) {
		try {
			// Find the full name.
			final String fullPath = prefix + name;

			// Do we have this already?
			if (byteCache.containsKey(fullPath)) {
				// Already preloaded.
				return true;
			}
			// Not cached yet. Read it in.
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
			ByteArrayInputStream theseBytes = new ByteArrayInputStream(
					baos.toByteArray());

			// Put in map for later reference.
			byteCache.put(fullPath, theseBytes);

			// Success!
			return true;

		} catch (Exception e) {
			// If they're watching, let them know.
			e.printStackTrace();

			// Nothing to do here.
			return false;
		}
	}

	/**
	 * Preloads the given sound asynchronously. This method will not block; that
	 * is, it will not wait for the sound to load before returning.
	 * 
	 * @param name
	 *            the filename of the sound to preload
	 */
	public synchronized void preloadAsync(final String name) {
		if (!currentlyPreloading.contains(prefix + name)) {
			currentlyPreloading.add(prefix + name);
			new Thread(new Runnable() {
				@Override
				public void run() {
					preload(name);
					currentlyPreloading.remove(prefix + name);
				}
			}).start();
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
