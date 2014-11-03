package visualizer.library;

import processing.core.PApplet;
import ddf.minim.analysis.*;

public class Eq {
	/**
	 * Minim reference + var name
	 */
	FFT fft;

	/**
	 * Default for the restriction in the x
	 */
	int sizex = Visualizer.width;

	/**
	 * stroke color(int r, int g, int b) changes the default stroke color These
	 * are the default stroke colors
	 */
	private int rstroke = 0;
	private int gstroke = 255;
	private int bstroke = 0;

	private boolean error = false;

	/**
	 * Constructor
	 */
	public Eq() {
		fft = new FFT(Visualizer.song.bufferSize(),
				Visualizer.song.sampleRate());
	}

	/**
	 * Default visual equalizer
	 */
	public void eq() {
		Visualizer.Parent.rectMode(1);
		sizex = Visualizer.width;
		fft.linAverages(200);
		fft.forward(Visualizer.song.mix);
		Visualizer.Parent.fill(0);
		Visualizer.Parent.stroke(rstroke, gstroke, bstroke);
		float w = (this.sizex / fft.avgSize());
		if (w == 0) {
			if (error != true) {
				PApplet.println("ERROR: X restriction is too small either increase window size");
				PApplet.println("FIXED: X restriction change to"
						+ fft.avgSize());
				w = 1;
				error = true;
			}
		}
		for (int i = 1; i < fft.avgSize(); i++) {
			Visualizer.Parent.rect(i * w, Visualizer.height - 1, i * w,
					Visualizer.height - (fft.getAvg(i) * 5));
		}
	}

	/**
	 * eq
	 * 
	 * @param input
	 *            LEFT RIGHT or MIX(the audio to analyze with fft)
	 */
	public void eq(String input) {
		Visualizer.Parent.rectMode(1);
		sizex = Visualizer.width;
		fft.linAverages(200);
		leftRight(input);
		Visualizer.Parent.fill(0);
		Visualizer.Parent.stroke(rstroke, gstroke, bstroke);
		float w = (this.sizex / fft.avgSize());
		if (w == 0) {
			if (error != true) {
				PApplet.println("ERROR: X restriction is too small either increase window size");
				error = true;
			}
		}
		for (int i = 1; i < fft.avgSize(); i++) {
			Visualizer.Parent.rect(i * w, Visualizer.height - 1, i * w,
					Visualizer.height - (fft.getAvg(i) * 5));
		}
	}

	/**
	 * eq
	 * 
	 * @param input
	 *            LEFT RIGHT or MIX(the audio to analyze with fft)
	 * @param x
	 *            Xposition (from left corner)
	 * @param y
	 *            Ypostition "               "
	 */
	public void eq(String input, int x, int y) {
		Visualizer.Parent.rectMode(1);
		sizex = Visualizer.width - x;
		fft.linAverages(100);
		leftRight(input);
		Visualizer.Parent.fill(0, 255, 0);
		Visualizer.Parent.stroke(rstroke, gstroke, bstroke);
		float w = (this.sizex / fft.avgSize());
		if (w == 0) {
			if (error != true) {
				PApplet.println("ERROR: X restriction is too small either increase window size");
				error = true;
			}
		}
		for (int i = 1; i < fft.avgSize(); i++) {
			Visualizer.Parent.rect(i * w + x, y, i * w + x, y
					- (fft.getAvg(i) * 5));
		}
	}

	/**
	 * eq
	 * 
	 * @param input
	 *            LEFT RIGHT or MIX(the audio to analyze with fft)
	 * @param x
	 *            Xposition (from left corner)
	 * @param y
	 *            Ypostition "               "
	 * @param r
	 *            red fill value
	 * @param g
	 *            green "      "
	 * @param b
	 *            blue " 	  "
	 */
	public void eq(String input, int x, int y, int r, int g, int b) {
		Visualizer.Parent.rectMode(1);
		sizex = Visualizer.width - x;
		fft.linAverages(100);
		leftRight(input);
		Visualizer.Parent.fill(r, g, b);
		Visualizer.Parent.stroke(r, g, b);
		float w = (this.sizex / fft.avgSize());
		if (w == 0) {
			if (error != true) {
				PApplet.println("ERROR: X restriction is too small either increase window size");
				error = true;
			}
		}
		for (int i = 1; i < fft.avgSize(); i++) {
			Visualizer.Parent.rect(i * w + x, y, i * w + x, y
					- (fft.getAvg(i) * 5));
		}
	}

	/**
	 * eq
	 * 
	 * @param input
	 *            LEFT RIGHT or MIX(the audio to analyze with fft)
	 * @param x
	 *            Xposition (from left corner)
	 * @param y
	 *            Ypostition "               "
	 * @param r
	 *            red fill value
	 * @param g
	 *            green "      "
	 * @param b
	 *            blue " 	  "
	 * @param sizex
	 *            Length restriciton
	 * @param sizey
	 *            Height restriction (Does not scale down, just sets a max where
	 *            the line cuts off)
	 */
	public void eq(String input, int x, int y, int r, int g, int b, int sizex,
			int sizey) {
		this.sizex = sizex;
		fft.linAverages(100);
		Visualizer.Parent.rectMode(1);
		fft.forward(Visualizer.song.mix);
		float w = (this.sizex / fft.avgSize());
		if (w == 0) {
			if (error != true) {
				PApplet.println("ERROR: X restriction is too small either increase window size");
				error = true;
			}
		}
		for (int i = 1; i < fft.avgSize(); i++) {
			if (fft.getAvg(i) * 5 > sizey) {
				Visualizer.Parent.fill(255, 0, 0);
				Visualizer.Parent.stroke(255, 0, 0);
				Visualizer.Parent.rect(i * w + x, y, i * w + x, y - sizey);
			} else {
				Visualizer.Parent.fill(r, g, b);
				Visualizer.Parent.stroke(r, g, b);
				Visualizer.Parent.rect(i * w + x, y, i * w + x,
						y - (fft.getAvg(i) * 5));
			}
		}
	}

	/**
	 * eq
	 * 
	 * @param input
	 *            LEFT RIGHT or MIX(the audio to analyze with fft)
	 * @param x
	 *            Xposition (from left corner)
	 * @param y
	 *            Ypostition "               "
	 * @param r
	 *            red fill value
	 * @param g
	 *            green "      "
	 * @param b
	 *            blue " 	  "
	 * @param sizex
	 *            Length restriciton
	 * @param sizey
	 *            Height restriction (Does not scale down, just sets a max where
	 *            the line cuts off)
	 * @param amount
	 *            amount of lines (tells minim how many lines the fft should be
	 *            averaged to)
	 */
	public void eq(String input, int x, int y, int r, int g, int b, int sizex,
			int sizey, int amount) {
		this.sizex = sizex;
		fft.linAverages(amount);
		Visualizer.Parent.rectMode(1);
		leftRight(input);
		float w = (this.sizex / fft.avgSize());
		/**
		 * Dont know how to use Exception Handling yet so this will do for now
		 */
		if (w == 0) {
			if (error != true) {
				PApplet.println("ERROR: X restriction is too small either increase window size");
				error = true;
			}
		}
		for (int i = 1; i < fft.avgSize(); i++) {
			if (fft.getAvg(i) * 5 > sizey) {
				Visualizer.Parent.fill(255, 0, 0);
				Visualizer.Parent.stroke(255, 0, 0);
				Visualizer.Parent.rect(i * w + x, y, i * w + x, y - sizey);
			} else {
				Visualizer.Parent.fill(r, g, b);
				Visualizer.Parent.stroke(r, g, b);
				Visualizer.Parent.rect(i * w + x, y, i * w + x,
						y - (fft.getAvg(i) * 5));
			}
		}
	}

	/**
	 * Takes the string input from the overloaded method above and sets up the
	 * fft
	 * 
	 * @param LEFT
	 *            RIGHT or MIX
	 */
	private void leftRight(String input) {
		if (error != true) {
			switch (input) {
			case "LEFT":
				fft.forward(Visualizer.song.left);
				break;

			case "RIGHT":
				fft.forward(Visualizer.song.right);
				break;

			case "MIX":
				fft.forward(Visualizer.song.mix);
				break;

			default:
				PApplet.println("ERROR: The argument " + input
						+ "is not valid. Try 'LEFT' 'RIGHT' or 'MIX'");
				error = true;
				break;
			}
		}
	}
}