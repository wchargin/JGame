/**
 * The {@code jgame.listener} package contains the framework for JGame's listener system. Every frame, listeners are tested with the {@link jgame.listener.Listener#isValid(jgame.GObject, jgame.Context)} method, and, if the call returns {@code true}, invoked with the {@link jgame.listener.Listener#invoke(jgame.GObject, jgame.Context)} method. Provided with a context to interact with the rest of the game, the built-in listener types often implement the validity check method and allow developers to create the invocation code.
 * 
 * @author William Chargin
 * @since 1.0
 *
 */
package jgame.listener;