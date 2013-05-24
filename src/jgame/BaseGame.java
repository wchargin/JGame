package jgame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * The main game class for GameLibrary games. This class bridges the gap between
 * the simplicity of GameLibrary and the perils of Swing, allowing game
 * developers to focus on game content instead of display code.
 * <p>
 * This class's ultrafast display code has been adapted from <a
 * href="http://stackoverflow.com/users/170224/ivo-wetzel">Ivo Wetzel</a>'s <a
 * href="http://stackoverflow.com/a/1963684/732016">StackOverflow post on the
 * subject</a>. Thanks, Ivo.
 * 
 * @author William Chargin
 * @author Ivo Wetzel
 * 
 */
public abstract class BaseGame extends Thread {

	/**
	 * The adapter to listen for key presses on the canvas.
	 * 
	 * @author William Chargin
	 * 
	 */
	protected final class CanvasKeyAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			keyCodes.add(e.getKeyCode());
		}

		@Override
		public void keyReleased(KeyEvent e) {
			keyCodes.remove(e.getKeyCode());
		}

	}

	/**
	 * The adapter to listen for mouse movement on the canvas.
	 * 
	 * @author William Chargin
	 * 
	 */
	final class CanvasMouseAdapter extends MouseAdapter {
		@Override
		public void mouseDragged(MouseEvent e) {
			currentMouseLocation = e.getPoint();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			currentMouseLocation = e.getPoint();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			currentMouseLocation = null;
			currentMouseButton = MouseEvent.NOBUTTON;
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			currentMouseLocation = e.getPoint();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			currentMouseLocation = e.getPoint();
			currentMouseButton |= e.getModifiers();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			currentMouseButton &= ~e.getModifiers();
		}
	}

	/**
	 * The adapter to run shutdown code.
	 * 
	 * @author William Chargin
	 * 
	 */
	protected final class FrameClose extends WindowAdapter {
		@Override
		public void windowClosing(final WindowEvent e) {
			stopGame();
		}
	}

	/**
	 * A list of instances of a class. This class's {@link #equals(Object)} and
	 * {@link #hashCode()} methods are based solely on the
	 * {@link InstanceList#clazz} variable.
	 * 
	 * @author William Chargin
	 * 
	 * @param <T>
	 *            the type for which this instance list is applicable
	 */
	protected static final class InstanceList<T extends GPaintable> extends
			ArrayList<T> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * The class.
		 */
		public final Class<T> clazz;

		/**
		 * Creates the list with the given class.
		 * 
		 * @param clazz
		 *            the class
		 */
		public InstanceList(Class<T> clazz) {
			super();
			this.clazz = clazz;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!super.equals(obj)) {
				return false;
			}
			if (!(obj instanceof InstanceList)) {
				return false;
			}
			InstanceList<?> other = (InstanceList<?>) obj;
			if (clazz == null) {
				if (other.clazz != null) {
					return false;
				}
			} else if (!clazz.equals(other.clazz)) {
				return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
			return result;
		}

	}

	/**
	 * Whether the game is currently running.
	 */
	private boolean gameRunning;

	/**
	 * The canvas object used for graphics rendering.
	 */
	protected Canvas canvas;

	/**
	 * The root container for the game.
	 */
	private GRootContainer rootContainer;

	/**
	 * The buffer strategy used for rendering.
	 */
	private BufferStrategy strategy;

	/**
	 * The background image.
	 */
	private BufferedImage background;

	/**
	 * The graphics object for drawing the background.
	 */
	private Graphics2D backgroundGraphics;

	/**
	 * The graphics object for drawing the game.
	 */
	private Graphics2D graphics;

	/**
	 * The current location of the mouse pointer with respect to the game area.
	 */
	private Point currentMouseLocation = null;

	/**
	 * The number of mouse buttons currently pressed.
	 */
	private int currentMouseButton = MouseEvent.NOBUTTON;

	/**
	 * The set of key codes currently pressed.
	 */
	private final Set<Integer> keyCodes;

	/**
	 * The list of instances of a class.
	 */
	private final List<InstanceList<? extends GObject>> instances;

	/**
	 * The width of the game area. This is overwritten in
	 * {@link #setRootContainer(GRootContainer)}.
	 */
	private int width = 320;

	/**
	 * The height of the game area. This is overwritten in
	 * {@link #setRootContainer(GRootContainer)}.
	 */
	private int height = 240;

	/**
	 * The scale of the game area.
	 */
	private int scale = 1;

	/**
	 * The target frame rate, in frames per second (hertz, Hz).
	 */
	private int targetFPS = 30;

	/**
	 * The time in nanoseconds it took for the last frame to render.
	 */
	private long lastFrameRenderNanos = 0;

	/**
	 * The number of objects rendered on the last frame.
	 */
	private int lastFrameObjCount = 0;

	/**
	 * The decimal format for the frame rate.
	 */
	protected final DecimalFormat df = new DecimalFormat("###.00");

	/**
	 * The graphics configuration for this game.
	 */
	private GraphicsConfiguration config = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getDefaultScreenDevice()
			.getDefaultConfiguration();

	/**
	 * The cursor to be shown during the game.
	 */
	private Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);

	/**
	 * Creates the game and initializes required values.
	 */
	public BaseGame() {
		// Initialize the set of key codes.
		keyCodes = new LinkedHashSet<Integer>();

		// Initialize the list of instance lists.
		instances = new ArrayList<Game.InstanceList<? extends GObject>>();
	}

	/**
	 * Cleans up the GUI after the game has exited.
	 */
	protected abstract void cleanupGUI();

	/**
	 * Creates a hardware-accelerated image with the given properties.
	 * 
	 * @param width
	 *            the width of the image to create
	 * @param height
	 *            the height of the image to create
	 * @param alpha
	 *            whether the image should have an alpha channel ({@code true})
	 *            or not ({@code false}).
	 * @return the new image
	 */
	private final BufferedImage create(final int width, final int height,
			final boolean alpha) {
		return config.createCompatibleImage(width, height,
				alpha ? Transparency.TRANSLUCENT : Transparency.OPAQUE);
	}

	/**
	 * Creates a {@link Context} for the given object.
	 * 
	 * @param object
	 *            the object for which to create the context
	 * @return the context
	 */
	private Context createContext(final GObject object) {
		// Define the point variables.
		final Point mouseAbsolute;
		final Point mouseRelative;

		// Is the mouse in the screen?
		if (currentMouseLocation != null) {
			// Get the mouse's current location relative to the game.
			mouseAbsolute = new Point(currentMouseLocation);

			// To calculate the relative position, we need to make sure that the
			// object is non-null.
			if (object != null) {
				// Get the mouse's current location relative to the parent.
				// Start by cloning the absolute point.
				mouseRelative = new Point(mouseAbsolute);

				// Start with this object's parent.
				GObject parent = object.getParent();

				// Traverse the parent hierarchy until we get to null.
				// This could even be the very first one.
				while (parent != null) {

					if (parent.getParent() != null) {
						// Take off x for the parent's x.
						mouseRelative.x -= (parent.getX() - parent.getWidth()
								* parent.getAnchorWeightX());
						mouseRelative.y -= (parent.getY() - parent.getHeight()
								* parent.getAnchorWeightY());
					}
					parent = parent.getParent();
				}
			} else {
				// The object is null.
				mouseRelative = null;
			}
		} else {
			// The mouse is not in the screen.
			mouseAbsolute = null;
			mouseRelative = null;
		}

		// Account for scale.
		if (mouseAbsolute != null) {
			mouseAbsolute.x /= scale;
			mouseAbsolute.y /= scale;
		}
		if (mouseRelative != null) {
			mouseRelative.x /= scale;
			mouseRelative.y /= scale;
		}

		// Generate the set of unmodifiable codes.
		final Set<Integer> unmodifiableKeyCodes = Collections
				.unmodifiableSet(keyCodes);

		// Create and return the context.
		return new Context() {

			/**
			 * This recursive helper method for the
			 * {@link #getInstancesOfClass(Class)} method adds all children of
			 * the given object that are of the given type and hitting the
			 * target object to the given list.
			 * 
			 * @param list
			 *            the list to which to add the results
			 * @param clazz
			 *            the class of objects to test
			 * @param o
			 *            the object whose children to test
			 */
			@SuppressWarnings("unchecked")
			private <T extends GPaintable> void addToList(List<T> list,
					Class<T> clazz, Object o) {
				// If it's not a container, there's nothing to do here.
				if (!(o instanceof GObjectHolder)) {
					// Move along.
					return;
				}

				// Otherwise, make the cast.
				GObjectHolder holder = (GObjectHolder) o;

				// Loop over each object.
				for (GPaintable thisObject : holder.getObjects()) {
					// If the object is of the right type...
					if (clazz.isInstance(thisObject)) {
						// ... add it to the list.
						list.add((T) thisObject);
					}

					// And recurse.
					addToList(list, clazz, thisObject);
				}
			}

			@Override
			public Rectangle2D getGameBounds() {
				return new Rectangle2D.Double(0, 0, width, height);
			}

			@SuppressWarnings("unchecked")
			@Override
			public <T extends GPaintable> List<T> getInstancesOfClass(
					Class<T> clazz) {
				// Do we have a cached list?
				for (InstanceList<?> list : instances) {
					// Perform a class test.
					if (list.clazz.equals(clazz)) {
						// It's a match.
						return Collections
								.unmodifiableList((List<? extends T>) list);
					}
				}

				// If we got here we need to make a list.
				InstanceList<T> list = new InstanceList<T>(clazz);

				// Populate.
				addToList(list, clazz, rootContainer);

				return list;
			}

			@Override
			public Set<Integer> getKeyCodesPressed() {
				return unmodifiableKeyCodes;
			}

			@Override
			public Point getMouseAbsolute() {
				return mouseAbsolute;
			}

			@Override
			public int getMouseButtonMask() {
				return currentMouseButton;
			}

			@Override
			public Point getMouseRelative() {
				return mouseRelative;
			}

			@Override
			public <T extends GObject> List<T> hitTestClass(Class<T> clazz) {
				// Create the list.
				List<T> list = new ArrayList<T>(getInstancesOfClass(clazz));

				// Iterate.
				Iterator<T> it = list.iterator();
				while (it.hasNext()) {
					// Check for hit.
					if (!it.next().preciseHitTest(object)) {
						// No go.
						it.remove();
					}
				}

				// Back to you.
				return list;
			}

			@Override
			public boolean isMouseInScreen() {
				return mouseAbsolute != null;
			}

			@Override
			public void setCurrentGameView(Object viewName) {
				rootContainer.setCurrentView(viewName);
			}

			@Override
			public void stopGame() {
				BaseGame.this.stopGame();
			}

		};
	}

	/**
	 * Gets the graphics object on which to draw, and initializes it if it has
	 * not yet been initialized.
	 * 
	 * @return the graphics context
	 */
	private Graphics2D getBuffer() {
		// Make sure the graphics object has been initialized.
		if (graphics == null) {
			// It hasn't.
			try {
				// Create a new graphics context on which to draw.
				graphics = (Graphics2D) strategy.getDrawGraphics();
			} catch (IllegalStateException e) {
				// Something's gone wrong. Fallback.
				return null;
			}
		}

		// Return the graphics object.
		return graphics;
	}

	/**
	 * Gets the cursor for the game.
	 * 
	 * @return the cursor, or {@code null} if no cursor is set
	 */
	public Cursor getCursor() {
		return cursor;
	}

	/**
	 * Gets the name of the currently set cursor.
	 * 
	 * @return the cursor
	 */
	protected abstract String getCursorName();

	/**
	 * Gets the current root container.
	 * 
	 * @return the root container
	 */
	public GRootContainer getRootContainer() {
		return rootContainer;
	}

	/**
	 * Gets the target FPS rate for the game.
	 * 
	 * @return the target FPS
	 */
	public int getTargetFPS() {
		return targetFPS;
	}

	/**
	 * Creates and returns an exception to indicate that the game is already
	 * running.
	 * 
	 * @return the exception
	 */
	private IllegalStateException newGameRunningException() {
		return new IllegalStateException("The game is already running");
	}

	/**
	 * Renders the current frame of the game on the given context.
	 * 
	 * @param g
	 *            the graphics context on which to render
	 */
	public void renderGame(Graphics2D g) {
		g.clearRect(0, 0, width, height);
		rootContainer.paint(g);

		// Can we display debug info?
		if (keyCodes.contains(KeyEvent.VK_BACK_QUOTE)) {
			g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
			FontMetrics fm = g.getFontMetrics();
			final String SEPARATOR = " | ";
			String text = "FPS: "
					+ (lastFrameRenderNanos <= 0 ? "\u221e" : df
							.format(1e9d / (double) lastFrameRenderNanos));
			text += SEPARATOR
					+ (currentMouseLocation == null ? "Mouse offscreen"
							: ("Mouse: (" + currentMouseLocation.x + ", "
									+ currentMouseLocation.y + ")"));
			text += SEPARATOR + "Cursor: " + getCursor();
			text += SEPARATOR + "Objects: " + lastFrameObjCount;
			int theight = fm.getAscent();
			g.setColor(new Color(0f, 0f, 0f, 0.5f));
			g.fillRect(0, height - theight - 2, width, theight + 4);
			g.setColor(Color.WHITE);
			g.drawString(text, 2, height - 2);
		}
	}

	@Override
	public void run() {
		// Get the graphics context for the background.
		backgroundGraphics = (Graphics2D) background.getGraphics();

		// Determine how many milliseconds to wait in between drawings.
		long fpsWait = (long) (1.0 / targetFPS * 1000);
		main: while (gameRunning) {
			long renderStart = System.nanoTime();
			updateGame();

			// Update graphics.
			do {
				// Get the background graphics to render on.
				Graphics2D bg = getBuffer();

				// If the game isn't running, exit.
				if (!gameRunning) {
					break main;
				}

				// Render it.
				renderGame(backgroundGraphics); // this calls your draw method

				// Account for scale.
				if (scale != 1) {
					bg.drawImage(background, 0, 0, width * scale, height
							* scale, 0, 0, width, height, null);
				} else {
					bg.drawImage(background, 0, 0, null);
				}
				bg.dispose();
			} while (!updateScreen());

			// Calculate the time it took to render this frame for FPS limiting.
			long renderTime = (System.nanoTime() - renderStart) / 1000000;

			// Limit FPS so as not to exceed superhuman speeds.
			try {
				// Sleep if possible. If rendering took longer than expected (so
				// the second argument is negative), don't sleep at all.
				Thread.sleep(Math.max(0, fpsWait - renderTime));
			} catch (InterruptedException e) {
				// If we were interrupted, we can go back to the main loop.
				// Clear the interrupted status.
				Thread.interrupted();
				break;
			}

			// If needed:
			lastFrameRenderNanos = (long) (System.nanoTime() - renderStart);
		}

		// When done, dispose of the frame.
		cleanupGUI();
	}

	/**
	 * Sets the cursor to be shown during the game.
	 * 
	 * @param cursor
	 *            the cursor, or {@code null} to hide the cursor
	 * @throws IllegalStateException
	 *             if the game has already started
	 */
	public void setCursor(Cursor cursor) throws IllegalStateException {
		// Has the game already started?
		if (gameRunning) {
			// The game has already started.
			throw newGameRunningException();
		}

		// Set the cursor.
		this.cursor = cursor;
	}

	/**
	 * Sets the root container. This method must be invoked prior to
	 * {@link #startGame()}.
	 * 
	 * @param rootContainer
	 *            the new root container
	 * @throws IllegalArgumentException
	 *             if {@code rootContainer} is {@code null}
	 * @throws IllegalStateException
	 *             if the game has already started
	 */
	public void setRootContainer(GRootContainer rootContainer)
			throws IllegalArgumentException, IllegalStateException {
		// Make sure the container is non-null.
		if (rootContainer == null) {
			// The container is null.
			throw new IllegalArgumentException("rootContainer == null");
		}

		// Make sure the game hasn't started yet.
		if (gameRunning) {
			// The game has already started.
			throw newGameRunningException();
		}

		// Set the container.
		this.rootContainer = rootContainer;

		// Set width and height.
		width = rootContainer.getIntWidth();
		height = rootContainer.getIntHeight();
	}

	/**
	 * Sets the target FPS rate for the game.
	 * 
	 * @param targetFPS
	 *            the new target FPS rate
	 * @throws IllegalArgumentException
	 *             if {@code targetFPS <= 0}
	 */
	public void setTargetFPS(int targetFPS) throws IllegalArgumentException {
		// Perform a bounds check.
		if (targetFPS <= 0) {
			// Invalid.
			throw new IllegalArgumentException("targetFPS <= 0 (" + targetFPS
					+ ")");
		}

		// Set the value.
		this.targetFPS = targetFPS;
	}

	/**
	 * Performs set up actions on the GUI components.
	 * 
	 * @param title
	 *            the title for the GUI, if needed
	 */
	protected abstract void setupGUI(String title);

	/**
	 * Starts the game with the class name as the window title.
	 * 
	 * @see #startGame(String)
	 */
	public void startGame() {
		startGame(getClass().getSimpleName());
	}

	/**
	 * Starts the game with the given frame title.
	 * 
	 * @param title
	 *            the frame title
	 * @throws IllegalStateException
	 *             if the game has already been started or there is no root
	 *             container set
	 */
	public void startGame(String title) throws IllegalStateException {
		// Make sure we're not already running.
		if (gameRunning) {
			// The game is already running.
			throw newGameRunningException();
		}

		// Make sure there's a root container set.
		if (rootContainer == null) {
			// There is no root container set.
			throw new IllegalStateException("There is no root container set.");
		}

		// Determine width and height constants.
		width = rootContainer.getIntWidth();
		height = rootContainer.getIntHeight();

		// Create the canvas.
		canvas = new Canvas(config);

		// Add the mouse listener for the canvas.
		MouseAdapter ma = new CanvasMouseAdapter();
		canvas.addMouseListener(ma);
		canvas.addMouseMotionListener(ma);

		// Add the key listener for the canvas.
		KeyAdapter ka = new CanvasKeyAdapter();
		canvas.addKeyListener(ka);

		// Set the canvas size.
		canvas.setSize(width * scale, height * scale);

		// Create the background image.
		background = create(width, height, false);

		// Set up the GUI.
		setupGUI(title);

		// Create the multi-buffering strategy.
		canvas.createBufferStrategy(2);

		// Wait until the buffer strategy has been created.
		do {
			strategy = canvas.getBufferStrategy();
		} while (strategy == null);

		// Mark the game as running.
		gameRunning = true;

		// Finally, start the thread.
		start();
	}

	/**
	 * Gracefully stops the game.
	 */
	protected void stopGame() {
		// Stop all sounds.
		SoundManager.stopAllSounds();

		// Stop the game from running (kill the while loop).
		gameRunning = false;
	}

	/**
	 * Updates the given component (if possible) and its children (if any).
	 * Recursive.
	 * 
	 * @param object
	 *            the object to update
	 */
	public void updateComponent(GObject object) {
		// Increment.
		lastFrameObjCount++;

		// Can we advance this object?
		if (object instanceof GAnimatable) {
			// Then do so.
			((GAnimatable) object).nextFrame();
		}

		// Does this object have children?
		if (object instanceof GObjectHolder) {
			// Clone the array for concurrency.
			HashSet<GObject> children = new HashSet<GObject>();
			children.addAll(((GObjectHolder) object).getObjects());

			// Loop over each one.
			for (GObject subobject : children) {
				// Recurse.
				updateComponent(subobject);
			}
		}

		// Create a context for the controllers.
		Context controllerContext = createContext(object);

		// Invoke said controllers with the newly generated context.
		object.invokeControllers(controllerContext);

		// Create a context for the listeners.
		Context listenerContext = createContext(object);

		// Invoke said listeners with the newly generated context.
		object.invokeListeners(listenerContext);

	}

	/**
	 * Updates the game for this frame.
	 */
	public void updateGame() {
		lastFrameObjCount = 0;
		updateComponent(rootContainer.getCurrentView());
	}

	/**
	 * Updates the screen for the current frame.
	 * 
	 * @return whether the drawing buffer is still applicable
	 */
	private boolean updateScreen() {
		// Dispose of the current graphics object.
		graphics.dispose();

		// Clear the memory reference to it so it can be garbage collected.
		graphics = null;

		try {
			// Show the new graphics context.
			strategy.show();

			// Synchronize the context with the view.
			Toolkit.getDefaultToolkit().sync();

			// Test if the contents have been lost, and return the opposite.
			return (!strategy.contentsLost());
		} catch (NullPointerException e) {
			return true;
		} catch (IllegalStateException e) {
			return true;
		}
	}
}