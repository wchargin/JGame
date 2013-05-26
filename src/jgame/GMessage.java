package jgame;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

/**
 * A simple text message in a game.
 * 
 * @author William Chargin
 * 
 */
public class GMessage extends GObject {

	/**
	 * The color of this message.
	 */
	private Color color;

	/**
	 * The font for this message.
	 */
	private Font font;

	/**
	 * The text of this message.
	 */
	private String text;

	/**
	 * The horizontal alignment. {@code 0} is left; {@code 1} is right.
	 */
	private double alignmentX;

	/**
	 * The vertical alignment. {@code 0} is top; {@code 1} is bottom.
	 */
	private double alignmentY;

	/**
	 * Creates a message with the default settings.
	 */
	public GMessage() {
		super();
		font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
		color = Color.BLACK;
		text = new String();
	}

	/**
	 * Creates a message with the given text.
	 * 
	 * @param text
	 *            the text for this message
	 */
	public GMessage(String text) {
		this();
		this.text = text;
	}

	/**
	 * Gets the horizontal alignment.
	 * 
	 * @return the horizontal alignment
	 */
	public double getAlignmentX() {
		return alignmentX;
	}

	/**
	 * Gets the vertical alignment.
	 * 
	 * @return the vertical alignment
	 */
	public double getAlignmentY() {
		return alignmentY;
	}

	/**
	 * Gets the font color used by this message.
	 * 
	 * @return the font color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Gets the font used by this message.
	 * 
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * Gets the font size used by this message.
	 * 
	 * @return the font size
	 * @see Font#getSize()
	 */
	public int getSize() {
		return font.getSize();
	}

	/**
	 * Gets the font style used by this message.
	 * 
	 * @return the font style
	 * @see Font#getStyle()
	 */
	public int getStyle() {
		return font.getStyle();
	}

	/**
	 * Gets the text of this message.
	 * 
	 * @return the text of this message
	 */
	public String getText() {
		return text;
	}

	@Override
	public void paint(Graphics2D g) {
		// Set up graphics for text rendering.
		g.setColor(color);
		g.setFont(font);

		// Get metrics.
		FontMetrics fm = g.getFontMetrics();

		// Calculate dimensions.
		int w = fm.stringWidth(text);
		int h = fm.getAscent() * 2 / 3;

		// How much extra space is there?
		double extraX = getWidth() - w;
		double extraY = getHeight() - h;

		// Adjust for alignment.
		double x = extraX * alignmentX;
		double y = extraY * alignmentY + h; // add h because it's baseline-based

		// Paint.
		g.drawString(text, (int) x, (int) y);

		// Paint children.
		super.paint(g);
	}

	@Override
	public void preparePaint(Graphics2D g) {
		super.preparePaint(g);
		antialias(g);
	}

	/**
	 * Sets the horizontal alignment. {@code 0} is left; {@code 1} is right.
	 * 
	 * @param alignmentX
	 *            the new horizontal alignment
	 */
	public void setAlignmentX(double alignmentX) {
		this.alignmentX = alignmentX;
	}

	/**
	 * Sets the vertical alignment. {@code 0} is top; {@code 1} is bottom.
	 * 
	 * @param alignmentY
	 *            the new vertical alignment
	 */
	public void setAlignmentY(double alignmentY) {
		this.alignmentY = alignmentY;
	}

	/**
	 * Sets the font color used by this message.
	 * 
	 * @param color
	 *            the new font color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Sets the font used by this message.
	 * 
	 * @param font
	 *            the new font
	 */
	public void setFont(Font font) {
		this.font = font;
	}

	/**
	 * Sets the font size used by this message.
	 * 
	 * @param size
	 *            the new font size
	 * @see Font#deriveFont(float)
	 */
	public void setFontSize(float size) {
		setFont(font.deriveFont(size));
	}

	/**
	 * Sets the font style used by this message.
	 * 
	 * @param style
	 *            the new font style
	 * @see Font#deriveFont(int)
	 */
	public void setFontStyle(int style) {
		setFont(font.deriveFont(style));
	}

	/**
	 * Sets the text of this message.
	 * 
	 * @param text
	 *            the new text
	 */
	public void setText(String text) {
		this.text = text;
	}

}
