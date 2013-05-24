package jgame;

/**
 * Any animated object that can go to the next and previous frames.
 * 
 * @author William Chargin
 * 
 */
public interface GAnimatable {

	/**
	 * Determines whether or not the animation is currently playing. The default
	 * value is {@code true}.
	 * 
	 * @return {@code true} if the animation is playing, or {@code false} if it
	 *         is not
	 */
	public boolean isPlaying();

	/**
	 * Advances this object to the next frame.
	 * <p>
	 * If the animation is already on the last frame, it will loop from the
	 * first frame.
	 * <p>
	 * If the animation contains no frames, no action will be taken and no error
	 * will be thrown.
	 * <p>
	 * If the animation is not playing, no action will be taken and no error
	 * will be thrown.
	 */
	public void nextFrame();

	/**
	 * Takes this object to the previous frame.
	 * <p>
	 * If the animation is already on the first frame, it will loop from the
	 * last frame.
	 * <p>
	 * If the animation contains no frames, no action will be taken and no error
	 * will be thrown.
	 * <p>
	 * If the animation is not playing, no action will be taken and no error
	 * will be thrown.
	 */
	public void previousFrame();

	/**
	 * Takes this object to the specified frame number.
	 * 
	 * @param frameNumber
	 *            the new frame number
	 * @throws IndexOutOfBoundsException
	 *             if the frame number is out of bounds for this animation
	 */
	public void setFrameNumber(int frameNumber)
			throws IndexOutOfBoundsException;

	/**
	 * Sets whether or not the animation is currently playing. The default value
	 * is {@code true}.
	 * 
	 * @param playing
	 *            {@code true} if the animation should play, or {@code false} if
	 *            it should not
	 */
	public void setPlaying(boolean playing);

}
