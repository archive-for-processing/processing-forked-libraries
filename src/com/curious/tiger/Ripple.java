package com.curious.tiger;

import processing.core.PApplet;
import processing.core.PVector;

public class Ripple extends Particle {

	protected float mRadius;

	public Ripple(PApplet p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	public Ripple(PApplet p, PVector loc, float radius, int color) {
		this(p);

		mLocation = loc.get();
		mRadius = radius;

		mFillColor = color;
	}

	public void update() {

		if (isDead()) {
			return;
		}

		mLife--;
		mRadius += 3.0f;
		mFillColor = mApplet.color(mFillColor, mLife);
	}

	public float getRadius() {
		return mRadius;
	}

	public void setRadius(float mRadius) {
		this.mRadius = mRadius;
	}

	@Override
	public void display() {
		// TODO Auto-generated method stub
		mApplet.pushMatrix();
		mApplet.translate(mLocation.x, mLocation.y);
		mApplet.noStroke();
		mApplet.fill(mFillColor);

		mApplet.ellipse(0, 0, mRadius * 2, mRadius * 2);
		mApplet.popMatrix();

	}

	public void recycle() {
		Recycler.recycle(this);
	}

	public static Ripple get() {
		Particle p = Recycler.get();
		return p == null ? null : (Ripple) p;
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
