package com.curious.tiger;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class Particle {

	protected Particle mNext = null;
	protected Particle mCurrent = null;

	protected PApplet mApplet;

	protected PVector mLocation;
	protected PVector mVelocity;
	protected PVector mAcceleration;

	protected float mass = 1.0f;

	public Particle(PApplet p) {
		mApplet = p;
	}

	public Particle(PApplet p, float mass) {
		mApplet = p;
		this.mass = mass;
	}

	public Particle(PApplet p, PVector loc, PVector vel, PVector acc) {
		this(p);
		mLocation = loc.get();
		mVelocity = vel.get();
		mAcceleration = acc.get();
	}

	public Particle(PApplet p, float mass, PVector loc, PVector vel, PVector acc) {
		this(p);
		mLocation = loc.get();
		mVelocity = vel.get();
		mAcceleration = acc.get();
		this.mass = mass;
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

	public abstract void display();

	public void recycle() {
		Recycler.recycle(this);
	}

	public static Particle get() {
		return Recycler.get();

	}

	private static class Recycler extends Particle {
		private final static Particle mCycleBin = new Recycler(null);

		public Recycler(PApplet p) {
			super(p);
			mCycleBin.mCurrent = this;
		}

		public static void recycle(Particle p) {
			p.mNext = mCycleBin.mCurrent;
			mCycleBin.mCurrent = p;
		}

		public static Particle get() {
			if (mCycleBin.mCurrent == mCycleBin) {
				return null;
			}

			Particle temp = mCycleBin.mCurrent;
			mCycleBin.mCurrent = temp.mNext;
			temp.mNext = null;

			return temp;
		}

		@Override
		public void display() {
		}

	}

}
