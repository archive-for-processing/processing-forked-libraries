import com.curious.tiger.*;

RippleEmitter re;
ParticleSystem ps;

void setup(){

  size(600, 600);
  re = new RippleEmitter(this);
  re.setLocation(new PVector(width / 2, height / 2));
  
  ps = new ParticleSystem(re);
}


void draw(){
  background(255);
  ps.run();
  
  ps.addParticle();
  
}


