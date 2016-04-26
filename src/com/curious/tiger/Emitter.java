package com.curious.tiger;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class Emitter extends Mover {

	public Emitter(PApplet p) {
		super(p);
	}

	public Emitter(PApplet p, PVector loc, PVector vel, PVector acc) {
		super(p, loc, vel, acc);
	}

	public abstract Particle emit();
}
