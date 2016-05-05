package com.curious.tiger;

import processing.core.PApplet;
import processing.core.PVector;

public class EggEmitter extends Particle implements Emitter {

	private PVector mOriginVelocity;
	private float mOriginRadius = Float.MIN_VALUE;

	public EggEmitter(PApplet p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	public EggEmitter(PApplet p, PVector loc, PVector vel, PVector acc) {
		super(p, loc, vel, acc);

	}

	public void restore() {
		super.restore();
		mVelocity.mult(0);
		mVelocity.add(mOriginVelocity);
		mOriginRadius = mOriginVelocity.mag();
	}

	@Override
	public Particle emit() {

		CircleParticle cp = CircleParticle.get();
		if (cp == null) {
			cp = new CircleParticle(mApplet, mLocation, new PVector(), new PVector());
		} else {
			cp.getVelocity().mult(0);
			cp.getLocation().mult(0);
			cp.getLocation().add(mLocation);
		}

		cp.setFillColor(mFillColor);

		if (mOriginRadius == Float.MIN_VALUE || mOriginRadius < 0.01) {
			mOriginVelocity = mVelocity.get();
		} else {
			mOriginRadius *= 0.99;
		}
		cp.setRadius(mOriginRadius);

		return cp;
	}

	public void update() {

		super.update();
		if (mVelocity.x <= 0) {
			restore();
		}

	}

	public PVector getEggSize() {

		return null;
	}

	@Override
	public void display() {
		// TODO Auto-generated method stub

	}

}
