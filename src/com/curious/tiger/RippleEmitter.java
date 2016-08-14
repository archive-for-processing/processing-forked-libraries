package com.curious.tiger;

import processing.core.PApplet;

public class RippleEmitter extends Mover implements Emitter {

	public RippleEmitter(PApplet p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Particle emit() {
		// TODO Auto-generated method stub
		Ripple ripple = Ripple.get();

		int color = mApplet.color((int) (Math.random() * 255), (int) (Math.random() * 255),
				(int) (Math.random() * 255));

		if (ripple == null) {
			ripple = new Ripple(getPApplet(), mLocation, 8.0f, color);
		} else {
			ripple.restore();
			ripple.setFillColor(color);
			ripple.setRadius(8.0f);

		}

		return ripple;
	}

}
