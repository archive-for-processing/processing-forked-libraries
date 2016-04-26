package com.curious.tiger;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class Particle extends Mover implements Alive {
	final static int DEFAULT_LIFE = 255;
	protected int mLife = DEFAULT_LIFE;

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

	@Override
	public boolean isDead() {
		if (mLife <= 0) {
			return true;
		} else
			return false;
	}

	@Override
	public int getLifeTime() {
		return mLife;
	}

	@Override
	public void setLifeTime(int life) {
		mLife = life;

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
