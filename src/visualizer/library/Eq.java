package visualizer.library;

import ddf.minim.analysis.*;


public class Eq {

	/**
	 * Minim reference + var name
	 */
	FFT fft;
	/**
	  * Constructor
	  */
	public Eq(){
		}	
	
	public void eq(){
		fft = new FFT(Visualizer.song.bufferSize(), Visualizer.song.sampleRate());
		fft.forward(Visualizer.song.mix);
		fft.linAverages(100);
		Visualizer.Parent.fill(0);
		Visualizer.Parent.stroke(0,255,0);
		float w = ((Visualizer.width / fft.avgSize())/2);
		for(int i = 1; i<fft.avgSize(); i++){
			Visualizer.Parent.rect(i*w, Visualizer.height- 1, i*w,Visualizer.height - fft.getAvg(i)*3 -1);
		}
	}


}
