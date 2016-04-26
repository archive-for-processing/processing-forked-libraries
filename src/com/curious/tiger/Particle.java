package com.curious.tiger;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class Particle extends Mover {

	protected Particle mNext = null;
	protected Particle mCurrent = null;

	protected int mFillColor = 125;

	public Particle(PApplet p) {
		super(p);
	}

	public Particle(PApplet p, float mass) {
		super(p);
		this.mass = mass;
	}

	public Particle(PApplet p, PVector loc, PVector vel, PVector acc) {
		super(p, loc, vel, acc);
	}

	public Particle(PApplet p, float mass, PVector loc, PVector vel, PVector acc) {
		super(p, loc, vel, acc);
		this.mass = mass;
	}

	public abstract void display();

	public void restore() {

	}

	public int getFillColor() {
		return mFillColor;
	}

	public void setFillColor(int mFillColor) {
		this.mFillColor = mFillColor;
	}

	public void recycle() {
		Recycler.recycle(this);
	}

	public static Particle get() {
		return Recycler.get();
	}

	private static class Recycler {
		private final static Particle mCycleBin = new Particle(null) {
			@Override
			public void display() {
			}
		};

		public Recycler() {
		}

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
