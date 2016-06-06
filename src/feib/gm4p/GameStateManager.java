package feib.gm4p;
import java.util.HashMap;

import processing.core.*;

public class GameStateManager {
	private PApplet p;
	private HashMap<String, GameObject> gameStates;
	public GameObject activeGameState;
	
	public GameStateManager(PApplet applet){
		p = applet;
		gameStates = new HashMap<String, GameObject>();
		
		if (GM.debugMode) 
			PApplet.println("AssetManager created");
	}
	
	public void addGameState(String gameStateName, GameObject gameState){
		gameStates.put(gameStateName, gameState);
		if (gameStates.size() == 1)
			activeGameState = gameState;
	}
}
