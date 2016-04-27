package com.curious.tiger;

import processing.core.PApplet;
import processing.core.PVector;

public class RectParticle extends Particle {

	protected PVector mSize;

	public RectParticle(PApplet p, float mass, PVector loc, PVector vel, PVector acc, PVector size) {
		super(p, mass, loc, vel, acc);
		mSize = size;
	}

	public RectParticle(PApplet p, float mass, PVector size) {
		super(p, mass);
		mSize = size;
	}

	public RectParticle(PApplet p, PVector loc, PVector vel, PVector acc, PVector size) {
		super(p, loc, vel, acc);
		mSize = size;
	}

	public PVector getSize() {
		return mSize.get();
	}

	public void setSize(PVector size) {
		if (mSize == null) {
			this.mSize = size;
		} else {
			this.mSize.mult(0);
			this.mSize.add(size);
		}
	}

	public void update() {
		super.update();
		mLife -= 2;
	}

	@Override
	public void display() {
		float angle = mVelocity.heading();
		mApplet.pushMatrix();
		mApplet.rectMode(PApplet.CENTER);
		mApplet.translate(mLocation.x, mLocation.y);
		mApplet.rotate(angle);
		mApplet.noStroke();
		mApplet.fill(mFillColor, mLife);
		mApplet.rect(0, 0, mSize.x, mSize.y);
		mApplet.popMatrix();

	}

	public void recycle() {
		Recycler.recycle(this);
	}

	public static RectParticle get() {
		Particle p = Recycler.get();
		return p == null ? null : (RectParticle) p;
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
