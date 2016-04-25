package com.curious.tiger;

import processing.core.PApplet;
import processing.core.PVector;

public class Mover {

	protected PApplet mApplet;

	protected PVector mLocation = new PVector();
	protected PVector mVelocity = new PVector();
	protected PVector mAcceleration = new PVector();

	protected float mass = 1.0f;

	public Mover(PApplet p) {
		mApplet = p;
	}

	public Mover(PApplet p, PVector loc, PVector vel, PVector acc) {
		this(p);
		mLocation = loc.get();
		mVelocity = vel.get();
		mAcceleration = acc.get();
	}

	public void update() {
		mLocation.add(mVelocity);
		mVelocity.add(mAcceleration);
		mAcceleration.mult(0);
	}

	public void applyForce(PVector force) {
		PVector a = force.get();
		a.div(mass);
		mAcceleration.add(a);

	}

	public PApplet getPApplet() {
		return mApplet;
	}

	public PVector getLocation() {
		return mLocation.get();
	}

	public Mover setLocation(PVector mLocation) {
		if (this.mLocation == null)
			this.mLocation = mLocation.get();
		else {
			this.mLocation.mult(0);
			this.mLocation.add(mLocation);
		}
		return this;
	}

	public PVector getVelocity() {
		return mVelocity.get();
	}

	public Mover setVelocity(PVector mVelocity) {
		if (this.mVelocity == null)
			this.mVelocity = mVelocity.get();
		else {
			this.mVelocity.mult(0);
			this.mVelocity.add(mVelocity);
		}
		return this;
	}

	public PVector getAcceleration() {
		return mAcceleration.get();
	}

	public Mover setAcceleration(PVector mAcceleration) {
		if (this.mAcceleration == null)
			this.mAcceleration = mAcceleration.get();
		else {
			this.mAcceleration.mult(0);
			this.mAcceleration.add(mAcceleration);
		}
		return this;
	}

}
