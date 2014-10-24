package visualizer.library;

import processing.core.PApplet;
import ddf.minim.analysis.*;



public class Eq {
	/**
	 * Minim reference + var name
	 */
	FFT fft;
	
	int sizex = Visualizer.width; 
	/**
	  * Constructor
	  */
	public Eq(){
		fft = new FFT(Visualizer.song.bufferSize(), Visualizer.song.sampleRate());
		fft.linAverages(100);
		}	
	/**
	 * Basic visual equalizer
	 */
	public void eq(){
		Visualizer.Parent.rectMode(1);
		sizex = Visualizer.width;
		fft.linAverages(200);
		fft.forward(Visualizer.song.mix);
		Visualizer.Parent.fill(0);
		Visualizer.Parent.stroke(0,255,0);
		float w = (this.sizex / fft.avgSize());
		if( w == 0){
			PApplet.println("ERROR: X restriction is too small either increase window size");
		}
		for(int i = 1; i < fft.avgSize(); i++){
			Visualizer.Parent.rect(i*w, Visualizer.height- 1, i*w,Visualizer.height - (fft.getAvg(i)*5));
		}
	}
	public void eq(String input){
		Visualizer.Parent.rectMode(1);
		sizex = Visualizer.width;
		fft.linAverages(200);
		leftRight(input);
		Visualizer.Parent.fill(0);
		Visualizer.Parent.stroke(0,255,0);
		float w = (this.sizex / fft.avgSize());
		if( w == 0){
			PApplet.println("ERROR: X restriction is too small either increase window size");
		}
		for(int i = 1; i < fft.avgSize(); i++){
			Visualizer.Parent.rect(i*w, Visualizer.height- 1, i*w,Visualizer.height - (fft.getAvg(i)*5));
		}
	}
	
	/**
	 * Eq with xy positions
	 * @param x xposition
	 * @param y yposition
	 */
	public void eq(String input, int x, int y){
		Visualizer.Parent.rectMode(1);
		sizex = Visualizer.width - x;
		fft.linAverages(100);
		leftRight(input);
		Visualizer.Parent.fill(0);
		Visualizer.Parent.stroke(0,255,0);
		float w = (this.sizex / fft.avgSize());
		if( w == 0){
			PApplet.println("ERROR: X restriction is too small either increase window size");
		}
		for(int i = 1; i < fft.avgSize(); i++){
		Visualizer.Parent.rect(i*w + x, y, i*w + x,y -(fft.getAvg(i)*5));
	}
  }
	/**
	 * Eq
	 * @param x xposition
	 * @param y yposition
	 * @param sizex restriction on drawing passed this x value(added to xposition)
	 * @param sizey restriction to height(added with y)
	 */
	public void eq(String input, int x, int y, int sizex, int sizey){
		this.sizex = sizex;
		fft.linAverages(100);
		Visualizer.Parent.rectMode(1);
		fft.forward(Visualizer.song.mix);
		Visualizer.Parent.fill(0);
		Visualizer.Parent.stroke(0,255,0);
		float w = (this.sizex / fft.avgSize());
		if( w == 0){
			PApplet.println("ERROR: X restriction is too small either decrease Lin Average size or increase X restriction");
		}
		for(int i = 1; i < fft.avgSize(); i++){
			if(fft.getAvg(i)* 5 > sizey){
				Visualizer.Parent.fill(255,0,0);
				Visualizer.Parent.stroke(255,0,0);
				Visualizer.Parent.rect(i*w + x, y, i*w + x, y - sizey);
			}else{
				Visualizer.Parent.fill(0,255,0);
				Visualizer.Parent.stroke(0,255,0);
				Visualizer.Parent.rect(i*w + x, y, i*w + x, y - (fft.getAvg(i)*5));	
		}
	}
  }
	/**
	 * 
	 * @param x xposition
	 * @param y yposition
	 * @param sizex restriction on drawing passed this x value(added to xposition)
	 * @param sizey restriction to height(added with y)
	 * @param amount Amount of eq lines (Minim sets the number to bundle together with the number put here)
	 */
	public void eq(String input, int x, int y, int sizex, int sizey, int amount){
		this.sizex = sizex;
		fft.linAverages(amount);
		Visualizer.Parent.rectMode(1);
		leftRight(input);
		Visualizer.Parent.fill(0);
		Visualizer.Parent.stroke(0,255,0);
		float w = (this.sizex / fft.avgSize());
		/**
		 * Dont know how to use Exception Handling yet so this will do for now
		 */
		if( w == 0){
			PApplet.println("ERROR: X restriction is too small either decrease Lin Average size or increase X restriction");
		}
		for(int i = 1; i < fft.avgSize(); i++){
			if(fft.getAvg(i)* 5 > sizey){
				Visualizer.Parent.fill(255,0,0);
				Visualizer.Parent.stroke(255,0,0);
				Visualizer.Parent.rect(i*w + x, y, i*w + x, y - sizey);
			}else{
				Visualizer.Parent.fill(0,255,0);
				Visualizer.Parent.stroke(0,255,0);
				Visualizer.Parent.rect(i*w + x, y, i*w + x, y -(fft.getAvg(i)*5));	
		}
	}
  }
	private void leftRight(String input){
		switch(input){
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
		PApplet.println("The argument " + input + "is not valid. Try 'LEFT' 'RIGHT' or 'MIX'");
		break;
		}		
	}
}