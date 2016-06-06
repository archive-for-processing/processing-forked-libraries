import feib.gm4p.*;

class Bird extends SpriteGameObject {
  Bird(){
    super("spr_bird.png");    
  }
  
  public void update(int elapsedTimeMs){
    velocity.y += 10;
  }
}

class PlayingState extends GameObjectList {
	PlayingState(){
		this.add(new SpriteGameObject("spr_background.png"));
	}
}

void setup() {
  size(640, 480); 
  
  GM.debugMode = true;
  GM.begin(this);  
  GM.gamestateManager.addGameState("playingState", new PlayingState());
   
}

void draw() {
  GM.execute();
}