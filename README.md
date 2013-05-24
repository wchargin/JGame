jgame
=====

JGame is a library for developing basic 2D games in Java. It is directed at computer science students who have some familiarity with computer programming but are unfamiliar with GUI design. JGame supplies a simple hierarchy that is bridged behind-the-scenes with the AWT (Abstract Window Toolkit) technologies to create a GUI for the user.

JGame's features include:

 * parent-child relationships
 * listeners:
   * timers
   * hit-test listeners
   * click listeners
   * key listeners
   * custom listeners, implementing the `jgame.listener.Listener` interface
 * controllers:
   * keyboard controllers, with a `ControlScheme` (arrows, WASD, IJKL, or custom)
   * mouse-following controllers
   * a "waypoint"/polygon-following controller
 * chainable tweens for appearances/exits
 * card-style views (menu, help, play game, etc.)
 * fast rendering
 * debug information with the <kbd>~</kbd> key

The simplest JGame program requires the following:

 * a main class that extends `jgame.Game`
 * a `jgame.GRootContainer` with at least one view with non-zero size

This could be implemented as:

    import jgame.*;
    
    public class MyGame extends Game {
    
    	public MyGame() {
    		GRootContainer root = new GRootContainer(Color.WHITE); // background
    		setRootContainer(root);
    		
    		GContainer c = new GContainer();
    		c.setSize(640, 480);
    		root.add("main view", c);
    	}
    	
    	public static void main(String[] args) {
    		MyGame g = new MyGame();
    		g.startGame("window title"); // omit title to use class name
    	}
    }
