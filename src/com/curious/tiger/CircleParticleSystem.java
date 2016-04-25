package com.curious.tiger;

import processing.core.PVector;

public class CircleParticleSystem extends ParticleSystem {

	public CircleParticleSystem(Mover m) {
		super(m);
	}

	@Override
	public void addParticle() {
		CircleParticle cp = CircleParticle.get();
		System.out.println("cp = " + cp);
		if (cp == null) {
			cp = new CircleParticle(mover.getPApplet(), mover.mLocation, PVector.random2D(), new PVector(0, 0));
		} else {
			cp.setLocation(mover.mLocation).setVelocity(PVector.random2D()).getAcceleration().mult(0);
		}

		mParticles.add(cp);
	}

}
