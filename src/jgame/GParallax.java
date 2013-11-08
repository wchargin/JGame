package jgame;

import jgame.controller.ConstantMovementController;

/**
 * A parallax layer pane. This component holds multiple layers of sprites that
 * can glide back and forth, automatically looping.
 * 
 * @author William Chargin
 * 
 */
public class GParallax extends GContainer {

	/**
	 * The list of layers for this parallax container.
	 */
	private GTiledSprite[] layers;

	/**
	 * Creates the parallax pane with the given number of layeres.
	 * 
	 * @param layerCount
	 *            the number of layers
	 */
	public GParallax(int layerCount) {
		layers = new GTiledSprite[layerCount];
		for (int i = 0; i < layers.length; i++) {
			layers[i] = new GTiledSprite();
			layers[i].setAnchorTopLeft();
			add(layers[i]);
		}
	}

	/**
	 * Sets the sprite for the given layer.
	 * 
	 * @param index
	 *            the index of the layer to modify
	 * @param sprite
	 *            the sprite to use
	 */
	public void setLayerSprite(int index, GSprite sprite) {
		layers[index].initializeForSprite(sprite);
	}

	@Override
	public void setHeight(double h) throws IllegalArgumentException {
		super.setHeight(h);
		for (GContainer layer : layers) {
			layer.setHeight(h);
		}
	}

	@Override
	public void setWidth(double w) throws IllegalArgumentException {
		super.setWidth(w);
		for (GContainer layer : layers) {
			layer.setWidth(w);
		}
	}

	/**
	 * A tiled sprite container that facilitates autotile scroloing.
	 * 
	 * @author William Chargin
	 * 
	 */
	private static class GTiledSprite extends GContainer {
		
		/**
		 * The number of tiles to use on each side.
		 */
		private static final int SIDE_LENGTH = 3;

		/**
		 * The sprites used for tiling.
		 */
		private GSprite[] sprites = new GSprite[SIDE_LENGTH * SIDE_LENGTH];

		/**
		 * The scroll controller for this container.
		 */
		private ScrollController scroller;

		/**
		 * A controller that allows scrolling of a tiled instance, with
		 * automatic resets when the object's position leaves its origin
		 * bounding box.
		 * 
		 * @author William Chargin
		 * 
		 */
		private static class ScrollController extends
				ConstantMovementController {

			/**
			 * Creates the controller with the given x and y velocities.
			 * 
			 * @param velocityX
			 *            the x velocity
			 * @param velocityY
			 *            the y velocity
			 */
			public ScrollController(double velocityX, double velocityY) {
				super(velocityX, velocityY);
			}

			@Override
			public void controlObject(GObject target, Context context) {
				super.controlObject(target, context);

				final double tx = target.getX();
				final double ty = target.getY();
				
				final double iw = target.getWidth() / SIDE_LENGTH;
				final double ih = target.getHeight() / SIDE_LENGTH;

				final double minX = -iw;
				final double minY = -ih;

				if (tx > 0 || tx < minX) {
					double x = minX + tx % iw;
					if (x < minX) {
						x += iw;
					}
					target.setX(x);
				}
				if (ty > 0 || ty < minY) {
					double y = minY + ty % ih;
					if (y < minY) {
						y += ih;
					}
					target.setY(y);
				}
			}
		}

		/**
		 * Initializes this tiled sprite for the given sprite.
		 * 
		 * @param s
		 *            the sprite to use
		 */
		public void initializeForSprite(GSprite s) {
			removeAllChildren();
			setSize(s.getWidth() * SIDE_LENGTH, s.getHeight() * SIDE_LENGTH);
			setLocation(-s.getWidth(), -s.getHeight());
			for (int i = 0; i < sprites.length; i++) {
				GSprite si = sprites[i] = new GSprite(s);
				si.setAnchorTopLeft();
				si.setX((i / SIDE_LENGTH) * s.getWidth());
				si.setY((i % SIDE_LENGTH) * s.getHeight());
				add(si);
			}
		}

		/**
		 * Causes the tiled sprite to scroll at the given velocity.
		 * 
		 * @param vx
		 *            the x-velocity
		 * @param vy
		 *            the y-velocity
		 */
		public void scrollAt(double vx, double vy) {
			if (scroller != null) {
				removeController(scroller);
			}
			scroller = new ScrollController(vx, vy);
			addController(scroller);
		}

	}

	/**
	 * Sets the given layer's scrolling speed.
	 * 
	 * @param index
	 *            the index of the layer to scroll
	 * @param vx
	 *            the velocity at which to scroll the layer in the x-direction
	 * @param vy
	 *            the velocity at which to scroll the layer in the y-direction
	 */
	public void scrollLayerAt(int index, double vx, double vy) {
		layers[index].scrollAt(vx, vy);
	}
}
