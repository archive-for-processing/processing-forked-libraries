package com.curious.tiger;

import processing.core.PApplet;
import processing.core.PVector;

public class StarParticle extends CircleParticle {

	protected float mAngle = 0;

	private float mOldRadius = Float.MIN_VALUE;
	private PVector[] mPoints = new PVector[5];

	public StarParticle(PApplet p, float mass, PVector loc, PVector vel, PVector acc, float radius, float angle) {
		super(p, mass, loc, vel, acc, radius);
		mAngle = angle;

	}

	public StarParticle(PApplet p, PVector loc, PVector vel, PVector acc, float radius, float angle) {
		super(p, loc, vel, acc, radius);
		mAngle = angle;

	}

	@Override
	public void display() {

		calculate();
		mApplet.ellipseMode(PApplet.CENTER);
		mApplet.pushMatrix();
		mApplet.translate(mLocation.x, mLocation.y);
		mApplet.rotate(mAngle);

		mApplet.stroke(0, 167, 0);
		mApplet.line(-mRadius, 0, mRadius, 0);
		mApplet.line(0, -mRadius, 0, mRadius);

		mApplet.stroke(mApplet.color(255, 0, 0));
		mApplet.ellipse(0, 0, mRadius * 2, mRadius * 2);

		mApplet.stroke(mApplet.color(0, 127));
		mApplet.beginShape();
		for (int i = 0; i < 5; i++) {
			mApplet.vertex(mPoints[i].x, mPoints[i].y);
		}
		mApplet.vertex(mPoints[0].x, mPoints[0].y);
		mApplet.endShape();

		mApplet.popMatrix();

	}

	private void calculate() {
		if (mOldRadius == mRadius) {
			return;
		}

		for (int i = 0; i < 5; i++) {
			if (mPoints[i] == null) {
				mPoints[i] = new PVector();
			} else {
				mPoints[i].mult(0);
			}
			float angle = 3 * PApplet.PI / 10 + i * PApplet.PI * 2 / 5;
			mPoints[i].set((float) (mRadius * mApplet.cos(angle)), (float) (mRadius * Math.sin(angle)));

		}

		mOldRadius = mRadius;

	}

}
