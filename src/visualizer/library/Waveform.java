package visualizer.library;

import processing.core.PApplet;

public class Waveform {

	/**
	 * If a error is thrown this becomes true
	 */
	private boolean error = false;

	public Waveform() {

	}

	/**
	 * Shows the waveform of the song
	 */
	public void wf() {
		Visualizer.Parent.stroke(0, 255, 0);
		for (int i = 0; i < Visualizer.song.bufferSize() - 1; i++) {
			float x1 = PApplet.map(i, 0, Visualizer.song.bufferSize(), 0,
					Visualizer.width);
			float x2 = PApplet.map(i + 1, 0, Visualizer.song.bufferSize(), 0,
					Visualizer.width);
			Visualizer.Parent.line(x1, 50 + Visualizer.song.mix.get(i) * 50,
					x2, 50 + Visualizer.song.mix.get(i + 1) * 50);
		}
	}

	/**
	 * Shows the waveform of the song
	 * 
	 * @param channel
	 *            channel to analyze
	 */
	public void wf(String channel) {
		Visualizer.Parent.stroke(0, 255, 0);
		for (int i = 0; i < Visualizer.song.bufferSize() - 1; i++) {
			float x1 = PApplet.map(i, 0, Visualizer.song.bufferSize(), 0,
					Visualizer.width);
			float x2 = PApplet.map(i + 1, 0, Visualizer.song.bufferSize(), 0,
					Visualizer.width);
			switch (channel) {
			case "LEFT":
				Visualizer.Parent.line(x1,
						150 + Visualizer.song.left.get(i) * 50, x2,
						150 + Visualizer.song.left.get(i + 1) * 50);
				break;
			case "RIGHT":
				Visualizer.Parent.line(x1,
						150 + Visualizer.song.right.get(i) * 50, x2,
						150 + Visualizer.song.right.get(i + 1) * 50);
				break;
			case "MIX":
				Visualizer.Parent.line(x1,
						150 + Visualizer.song.mix.get(i) * 50, x2,
						150 + Visualizer.song.mix.get(i + 1) * 50);
				break;
			default:
				if (error != true) {
					PApplet.println("ERROR: Channel is not selected right try: LEFT RIGHT or MIX");
					error = true;
				}
				break;
			}
		}
	}

	/**
	 * Shows the waveform of the song
	 * 
	 * @param channel
	 *            channel to analyze
	 * @param y
	 *            y position of the waveform
	 */
	public void wf(String channel, int y) {
		Visualizer.Parent.stroke(0, 255, 0);
		for (int i = 0; i < Visualizer.song.bufferSize() - 1; i++) {
			float x1 = PApplet.map(i, 0, Visualizer.song.bufferSize(), 0,
					Visualizer.width);
			float x2 = PApplet.map(i + 1, 0, Visualizer.song.bufferSize(), 0,
					Visualizer.width);
			switch (channel) {
			case "LEFT":
				Visualizer.Parent.line(x1,
						y + Visualizer.song.left.get(i) * 50, x2, y
								+ Visualizer.song.left.get(i + 1) * 50);
				break;
			case "RIGHT":
				Visualizer.Parent.line(x1, y + Visualizer.song.right.get(i)
						* 50, x2, y + Visualizer.song.right.get(i + 1) * 50);
				break;
			case "MIX":
				Visualizer.Parent.line(x1, y + Visualizer.song.mix.get(i) * 50,
						x2, y + Visualizer.song.mix.get(i + 1) * 50);
				break;
			default:
				if (error != true) {
					PApplet.println("ERROR: Channel is not selected right try: LEFT RIGHT or MIX");
					error = true;
				}
				break;
			}
		}
	}

	/**
	 * Shows the waveform of the song
	 * 
	 * @param channel
	 *            channel to analyze
	 * @param y
	 *            y position of the waveform
	 * @param x
	 *            x position of the waveform
	 */
	public void wf(String channel, int y, int x) {
		Visualizer.Parent.stroke(0, 255, 0);
		for (int i = 0; i < Visualizer.song.bufferSize() - 1; i++) {
			float x1 = PApplet.map(i, 0, Visualizer.song.bufferSize(), x,
					Visualizer.width);
			float x2 = PApplet.map(i + 1, 0, Visualizer.song.bufferSize(), x,
					Visualizer.width);
			switch (channel) {
			case "LEFT":
				Visualizer.Parent.line(x1,
						y + Visualizer.song.left.get(i) * 50, x2, y
								+ Visualizer.song.left.get(i + 1) * 50);
				break;
			case "RIGHT":
				Visualizer.Parent.line(x1, y + Visualizer.song.right.get(i)
						* 50, x2, y + Visualizer.song.right.get(i + 1) * 50);
				break;
			case "MIX":
				Visualizer.Parent.line(x1, y + Visualizer.song.mix.get(i) * 50,
						x2, y + Visualizer.song.mix.get(i + 1) * 50);
				break;
			default:
				if (error != true) {
					PApplet.println("ERROR: Channel is not selected right try: LEFT RIGHT or MIX");
					error = true;
				}
				break;
			}
		}
	}

	/**
	 * Shows the waveform of the song
	 * 
	 * @param channel
	 *            channel to analyze
	 * @param y
	 *            y position of the waveform
	 * @param x
	 *            x position of the waveform
	 * @param xRestriction
	 *            Shrinks the waveform to fit within this x value from the x
	 *            position
	 */
	public void wf(String channel, int y, int x, int xRestriction) {
		Visualizer.Parent.stroke(0, 255, 0);
		for (int i = 0; i < Visualizer.song.bufferSize() - 1; i++) {
			float x1 = PApplet.map(i, 0, Visualizer.song.bufferSize(), x,
					xRestriction);
			float x2 = PApplet.map(i + 1, 0, Visualizer.song.bufferSize(), x,
					xRestriction);
			switch (channel) {
			case "LEFT":
				Visualizer.Parent.line(x1,
						y + Visualizer.song.left.get(i) * 50, x2, y
								+ Visualizer.song.left.get(i + 1) * 50);
				break;
			case "RIGHT":
				Visualizer.Parent.line(x1, y + Visualizer.song.right.get(i)
						* 50, x2, y + Visualizer.song.right.get(i + 1) * 50);
				break;
			case "MIX":
				Visualizer.Parent.line(x1, y + Visualizer.song.mix.get(i) * 50,
						x2, y + Visualizer.song.mix.get(i + 1) * 50);
				break;
			default:
				if (error != true) {
					PApplet.println("ERROR: Channel is not selected right try: LEFT RIGHT or MIX");
					error = true;
				}
				break;
			}
		}
	}
}
