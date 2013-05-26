# JGame Changelog
This file describes the changes made to the JGame game library, available at <http://www.github.com/wchargin/jgame/>.

## JGame 1.2
* Parenting system revamped: `GObject`s can have children.
* Added `DelayListener`.
* Added visibility in addition to alpha.
* Added the ability to set anchor points based on x/y instead of percentage.
* Added tweens.
* Added `getSequential` method to `ImageCache`.
* `ImageCache`s use `BufferedImage`s.
* Improved `ButtonListener` state logic.
* Added `snapChildToCenter` (in addition to `snapChild`, which uses the anchor point).
* Paint clipping updated for non-rectangular shapes.
* `GContainer`s will automatically set size in `setBackgroundSprite` to match the sprite.
* `GObject` can run `distanceTo` on a single point.
* Changelog updated.

## JGame 1.1
* Sound implemented.
* Scaling implemented.
* Various bug fixes.
* `ButtonListener` and `GButton` added.
* Changelog updated.

## JGame 1.0
Initial release.