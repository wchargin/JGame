package jgame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * A simple text message in a game.
 * <p>
 * The default {@linkplain #setAnchorWeight(double, double) anchor weight} for
 * this class is {@linkplain #setAnchorTopLeft() top left} ({@code 0.0, 0.0}).
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
	 * Creates a message with the default settings.
	 */
	public GMessage() {
		super();
		font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
		color = Color.BLACK;
		text = new String();
		setAnchorTopLeft();
	}

	/**
	 * Creates a message with the given text.
	 * 
	 * @param text
	 *            the tex for this message
	 */
	public GMessage(String text) {
		this();
		this.text = text;
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
		super.paint(g);
		g.setColor(color);
		g.setFont(font);
		g.drawString(text, 0, g.getFontMetrics().getAscent());
	}

	@Override
	public void preparePaint(Graphics2D g) {
		super.preparePaint(g);
		antialias(g);
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
