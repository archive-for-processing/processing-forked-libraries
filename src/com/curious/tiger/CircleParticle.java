package com.curious.tiger;

import processing.core.PApplet;
import processing.core.PVector;

public class CircleParticle extends Particle implements Alive {

	private float mRadius = 5;
	private int mLife = 255;

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
		mApplet.fill(125, mLife);
		mApplet.stroke(0, mLife);
		mApplet.ellipse(0, 0, mRadius * 2, mRadius * 2);
		mApplet.popMatrix();

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
		// TODO Auto-generated method stub
		return mLife;
	}

	@Override
	public void setLifeTime(int life) {
		mLife = life;

	}

}
