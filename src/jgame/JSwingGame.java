package jgame;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A JGame panel for embedding in a Swing application.
 * 
 * @author William Chargin
 * 
 */
public class JSwingGame extends BaseGame {

	/**
	 * The panel used for display.
	 */
	private JPanel panel = new JPanel();

	/**
	 * Gets the component to be added to a Swing application.
	 * 
	 * @return the component
	 */
	public JComponent getSwingComponent() {
		return panel;
	}

	@Override
	protected void cleanupGUI() {
		panel.removeAll();
		panel = null;
	}

	@Override
	protected String getCursorName() {
		return panel.getCursor().getName();
	}

	@Override
	protected void setupGUI(String title) {
		panel.add(canvas, BorderLayout.CENTER);
		panel.setOpaque(true);
		panel.setPreferredSize(canvas.getSize());
		if (getCursor() == null) {
			panel.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
					new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
					new Point(0, 0), null));
		} else {
			panel.setCursor(getCursor());
		}
		canvas.requestFocusInWindow();
	}

}
