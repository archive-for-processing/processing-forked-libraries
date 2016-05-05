package com.curious.tiger;

import java.util.Random;

import processing.core.PApplet;
import processing.core.PVector;

public class CircleEmitter extends Mover implements Emitter {

	protected Random mRandom = new Random();

	public CircleEmitter(PApplet p) {
		super(p);
	}

	@Override
	public Particle emit() {
		CircleParticle cp = CircleParticle.get();

		if (cp == null) {
			cp = new CircleParticle(getPApplet(), mLocation, PVector.random2D(), new PVector(0, 0));
			cp.setRadius(getRandomSize().x);
		} else {
			cp.setLocation(mLocation).setVelocity(PVector.random2D()).getAcceleration().mult(0);
			cp.setFillColor(mApplet.color((int) (Math.random() * 255), (int) (Math.random() * 255),
					(int) (Math.random() * 255)));
			cp.setRadius(getRandomSize().x);
		}

		return cp;
	}

	public PVector getRandomSize() {
		int size = (int) (mRandom.nextGaussian() * 5 + 2);
		return new PVector(size, size);
	}

}
