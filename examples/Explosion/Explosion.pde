import com.curious.tiger.*;

Emitter m;
ParticleSystem ps;
PVector mGravity;


public void setup() {
  size(600, 400);
  m = new Emitter(this);
  m.setLocation(new PVector(width / 2, height /2 ));

  ps = new CircleParticleSystem(m);
  mGravity = new PVector(0, 1);
}



public void draw() {
  ps.run();
  ps.addParticle();
  ps.applyForce(mGravity);
}

