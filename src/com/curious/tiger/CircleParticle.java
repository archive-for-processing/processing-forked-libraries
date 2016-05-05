package com.curious.tiger;

import processing.core.PApplet;
import processing.core.PVector;

public class CircleParticle extends Particle {

	protected float mRadius = 5;

	public CircleParticle(PApplet p, PVector loc, PVector vel, PVector acc) {
		super(p, loc, vel, acc);
	}

	public CircleParticle(PApplet p, PVector loc, PVector vel, PVector acc, float radius) {
		super(p, loc, vel, acc);
		mRadius = radius;
	}

	public CircleParticle(PApplet p, float mass, PVector loc, PVector vel, PVector acc, float radius) {
		super(p, mass, loc, vel, acc);
		mRadius = radius;
	}

	public void update() {
		super.update();
		mLife -= 2;
	}

	@Override
	public void display() {

		mApplet.pushMatrix();
		mApplet.translate(mLocation.x, mLocation.y);
		mApplet.noStroke();
		mApplet.fill(mApplet.color(mFillColor, mLife));
		// mApplet.stroke(0, mLife);
		mApplet.ellipse(0, 0, mRadius * 2, mRadius * 2);
		mApplet.popMatrix();

	}

	public float getRadius() {
		return mRadius;
	}

	public void setRadius(float mRadius) {
		this.mRadius = mRadius;
	}

	public void recycle() {
		Recycler.recycle(this);
	}

	public static CircleParticle get() {
		Particle p = Recycler.get();
		return p == null ? null : (CircleParticle) p;
	}

	private static class Recycler {
		private final static Particle mCycleBin = new Particle(null) {
			@Override
			public void display() {
			}
		};

		static {
			mCycleBin.mCurrent = mCycleBin;
		}

		public static void recycle(Particle p) {
			p.restore();
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

	}

}
