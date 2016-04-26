package com.curious.tiger;

import processing.core.PApplet;
import processing.core.PVector;

public class CircleEmitter extends Mover implements Emitter {

	public CircleEmitter(PApplet p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Particle emit() {
		CircleParticle cp = CircleParticle.get();

		if (cp == null) {
			cp = new CircleParticle(getPApplet(), mLocation, PVector.random2D(), new PVector(0, 0));
		} else {
			cp.setLocation(mLocation).setVelocity(PVector.random2D()).getAcceleration().mult(0);
			cp.setFillColor(mApplet.color((int) (Math.random() * 255), (int) (Math.random() * 255),
					(int) (Math.random() * 255)));
		}

		return cp;
	}

}
