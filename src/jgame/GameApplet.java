package jgame;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JApplet;

/**
 * The web applet implementation of the GameLibrary game.
 * 
 * @author William Chargin
 * @see BaseGame
 */
public class GameApplet extends JApplet {

	/**
	 * The applet internal implementation of the GameLibrary game.
	 * 
	 * @author William Chargin
	 * @see BaseGame
	 */
	protected final class GameAppletCore extends BaseGame {

		/**
		 * Creates the game.
		 */
		public GameAppletCore() {
			super();
		}

		@Override
		protected void cleanupGUI() {
			// Nothing to do here.
		}

		@Override
		protected String getCursorName() {
			return getCursor().getName();
		}

		@Override
		protected void setupGUI(String title) {
			// Add the canvas to the frame as the first (0th) object.
			add(canvas, 0);

			// Set cursor (or blank if null).
			if (getCursor() == null) {
				setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
						new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
						new Point(0, 0), null));
			} else {
				setCursor(getCursor());
			}

			// Size the frame to fit the canvas.
			setSize(canvas.getSize());

			// Display the frame. This must be called before creating the buffer
			// strategy.
			GameApplet.this.setVisible(true);

			// Give focus to the canvas.
			canvas.requestFocusInWindow();
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The game applet core.
	 */
	protected GameAppletCore core;

	/**
	 * Creates the applet.
	 */
	public GameApplet() {
		core = new GameAppletCore();
	}

	@Override
	public void init() {
		core.startGame();
	}

	@Override
	public void stop() {
		super.stop();
		core.stopGame();
	}

}