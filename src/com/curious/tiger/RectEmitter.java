package com.curious.tiger;

import java.util.Random;

import processing.core.PApplet;
import processing.core.PVector;

public class RectEmitter extends Mover implements Emitter {
	protected Random mRandom = new Random();

	public RectEmitter(PApplet p) {
		super(p);
	}

	public RectEmitter(PApplet p, PVector loc, PVector vel, PVector acc) {
		super(p, loc, vel, acc);
	}

	public PVector getRandomSize() {
		int width = (int) (mRandom.nextGaussian() * 5 + 2);
		int height = (int) (mRandom.nextGaussian() * 5 + 3);
		return new PVector(width, height);

	}

	@Override
	public Particle emit() {
		RectParticle rp = RectParticle.get();
		if (rp == null) {
			rp = new RectParticle(getPApplet(), mLocation, PVector.random2D(), new PVector(0, 0), getRandomSize());

		} else {
			rp.setLocation(mLocation).setVelocity(PVector.random2D()).getAcceleration().mult(0);
			rp.getSize().mult(0);
			rp.getSize().add(getRandomSize());
			rp.setFillColor(mApplet.color((int) (Math.random() * 255), (int) (Math.random() * 255),
					(int) (Math.random() * 255)));
		}

		return rp;
	}

}
