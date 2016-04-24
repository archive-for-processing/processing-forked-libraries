import com.curious.tiger.*;

private Particle mP;
public void setup() {
  size(400, 400);

  mP = new CircleParticle(this, 
  new PVector(width / 2, height/2), 
  PVector.random2D(), new PVector(0, 0));
}

public void draw() {
  background(255);
  mP.display();
  mP.applyForce(new PVector(0, 0.05f));
  mP.update();
}

