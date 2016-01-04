public class Bubble {
  public PVector position;
  public float startlife = 48.0;
  public float life = 0.0;
  public float decay = 0.1;
  Bubble(PVector p, float duration){
    life = duration * 12 + 20;
    position = p;
  }
  
  public void update(){
    fill(255);
    noStroke();

    ellipse(position.x, position.y, life/startlife * 40, life/startlife * 40);
    life -= decay;
  }
}
