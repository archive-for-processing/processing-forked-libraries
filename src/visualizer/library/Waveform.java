package visualizer.library;

import processing.core.PApplet;

public class Waveform {

	public Waveform(){
		
	}
	public void wf(){
		Visualizer.Parent.stroke(0,255,0);
		for(int i = 0; i < Visualizer.song.bufferSize() - 1;i++){
			float x1 = PApplet.map(i, 0, Visualizer.song.bufferSize(), 0, Visualizer.width);
			float x2 = PApplet.map(i+1, 0, Visualizer.song.bufferSize(), 0, Visualizer.width);
			Visualizer.Parent.line(x1, 50 + Visualizer.song.mix.get(i) * 50, x2, 50 + Visualizer.song.left.get(i + 1) * 50);
		}
	}
	
	public void wf(String channel){
		Visualizer.Parent.stroke(0,255,0);
		for(int i = 0; i < Visualizer.song.bufferSize() - 1;i++){
			float x1 = PApplet.map(i, 0, Visualizer.song.bufferSize(), 0, Visualizer.width);
			float x2 = PApplet.map(i+1, 0, Visualizer.song.bufferSize(), 0, Visualizer.width);
			Visualizer.Parent.line(x1, 50 + Visualizer.song.mix.get(i) * 50, x2, 50 + Visualizer.song.left.get(i + 1) * 50);
		}
	}
	
	
	public void wf(String channel, int y){
		Visualizer.Parent.stroke(0,255,0);
		for(int i = 0; i < Visualizer.song.bufferSize() - 1;i++){
			float x1 = PApplet.map(i, 0, Visualizer.song.bufferSize(), 0, Visualizer.width);
			float x2 = PApplet.map(i+1, 0, Visualizer.song.bufferSize(), 0, Visualizer.width);
			Visualizer.Parent.line(x1, y + Visualizer.song.mix.get(i) * 50, x2, y + Visualizer.song.left.get(i + 1) * 50);
		}
	}
}
