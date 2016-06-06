package feib.gm4p;
import processing.core.*;

/**
 * This is a template class and can be used to start a new processing Library.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own Library naming convention.
 * 
 * (the tag example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 * @example GameEnvironment 
 */

public class GM {
	
	// myParent is a reference to the parent sketch
	static public PApplet p;
	static private long gameTimeMs, lastFrameTimeMs;
	static private int elapsedTimeMs;
	
	static public GameStateManager gamestateManager;
	static public InputHelper inputHelper;
	static public AssetManager assetManager;
	
	static public boolean debugMode;
	
	static public void begin(PApplet applet){
		p = applet;
		gamestateManager = new GameStateManager(p);
		assetManager = new AssetManager(p);
		inputHelper = new InputHelper(p);		
		
		p.registerMethod("mouseEvent", inputHelper);
		p.registerMethod("keyEvent", inputHelper);		
	}
		
	static private void update(int elapsedTimeMs){	
		gamestateManager.activeGameState.update(elapsedTimeMs);
	}
	
	static private void draw(PGraphics ctx){
		gamestateManager.activeGameState.draw(ctx);
	}
	
	static private void integrate(float gameTimeMs){	
		gamestateManager.activeGameState.integrate(elapsedTimeMs);
	}
	
	static private void handleInput(){
		gamestateManager.activeGameState.handleInput(inputHelper);
	}
	
	static private void reset(){
		gamestateManager.activeGameState.reset();
	}
	
	static public void execute(){
		if (gamestateManager.activeGameState != null){
			gameTimeMs = p.millis();
			elapsedTimeMs =  (int)(gameTimeMs - lastFrameTimeMs);
			lastFrameTimeMs = gameTimeMs;
			
			inputHelper.update();
			handleInput();
			integrate(elapsedTimeMs);
			update(elapsedTimeMs);
			draw(p.g);
		}
	
	}		
			
}

