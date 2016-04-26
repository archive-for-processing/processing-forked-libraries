import com.curious.tiger.*;

CircleEmitter m;
ParticleSystem ps;
PVector mGravity;

public void setup() {
  size(600, 400);
  m = new CircleEmitter(this);
  m.setLocation(new PVector(width / 2, height / 2));

  ps = new ParticleSystem(m);
  mGravity = new PVector(0, 0.05f);
}

public void draw() {
  background(255);
  ps.run();
  ps.addParticle();
  ps.applyForce(mGravity);
}

