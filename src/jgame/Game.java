package jgame;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * The desktop Swing implementation of the GameLibrary game.
 * 
 * @author William Chargin
 * @see BaseGame
 */
public class Game extends BaseGame {

	/**
	 * The frame containing the game.
	 */
	private JFrame frame;

	/**
	 * Creates the game.
	 */
	public Game() {
		super();
	}

	@Override
	protected void cleanupGUI() {
		frame.dispose();
	}

	@Override
	protected String getCursorName() {
		return frame.getCursor().getName();
	}

	@Override
	protected void setupGUI(String title) {

		// Create the frame with the given title.
		frame = new JFrame(title);

		// Add the listener for shutdown code.
		frame.addWindowListener(new FrameClose());

		// Disable automatic shutdown on exit (so we can run our own code).
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// Don't allow resizing.
		frame.setResizable(false);

		// Add the canvas to the frame as the first (0th) object.
		frame.add(canvas, 0);

		// Set cursor (or blank if null).
		if (getCursor() == null) {
			frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
					new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
					new Point(0, 0), null));
		} else {
			frame.setCursor(getCursor());
		}

		// Size the frame to fit the canvas.
		frame.pack();

		// Center the frame.
		frame.setLocationRelativeTo(null);

		// Display the frame. This must be called before creating the buffer
		// strategy.
		frame.setVisible(true);

		// Give focus to the canvas.
		canvas.requestFocusInWindow();
	}

}