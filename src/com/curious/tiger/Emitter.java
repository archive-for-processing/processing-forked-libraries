package com.curious.tiger;

import processing.core.PVector;

public interface Emitter {
	public Particle emit();

	public PVector getRandomSize();
}
