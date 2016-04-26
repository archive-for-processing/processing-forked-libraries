package com.curious.tiger;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PVector;

public class ParticleSystem {

	protected ArrayList<Particle> mParticles = new ArrayList<Particle>();
	protected Emitter mover;

	public ParticleSystem(Emitter m) {
		mover = m;
	}

	public void applyForce(PVector force) {
		if (mParticles.isEmpty()) {
			return;
		}

		for (Particle p : mParticles) {
			p.applyForce(force);
		}
	}

	public void run() {
		if (mParticles.isEmpty()) {
			return;
		}

		Iterator<Particle> iterator = mParticles.iterator();
		while (iterator.hasNext()) {
			Particle p = iterator.next();
			if (p instanceof Alive) {

				Alive a = (Alive) p;
				if (a.isDead()) {
					iterator.remove();
					p.recycle();
					continue;
				}

			}

			p.display();
			p.update();
		}

	}

	public void addParticle() {
		mParticles.add(mover.emit());

	}

}
