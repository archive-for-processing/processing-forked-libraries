package com.curious.tiger;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PVector;

public abstract class ParticleSystem {

	protected ArrayList<Particle> mParticles = new ArrayList<Particle>();
	protected Mover mover;

	public ParticleSystem(Mover m) {
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

	private void appendParticle(Particle p) {
		mParticles.add(p);
	}

	public abstract void addParticle();

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

}
