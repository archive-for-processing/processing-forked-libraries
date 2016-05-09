import com.curious.tiger.*;
StarEmitter m;
ParticleSystem ps;
PVector mGravity;

public void setup() {
  size(400, 400);
  m = new StarEmitter(this);
  m.setLocation(new PVector(width / 2, height / 3));

  ps = new ParticleSystem(m);
  mGravity = new PVector(0, 0.05f);
}

public void draw() {
  background(255);
  ps.run();
  ps.addParticle();
  ps.applyForce(mGravity);
}

