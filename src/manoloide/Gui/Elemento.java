package manoloide.Gui;

import manoloide.Color.Paleta;
import manoloide.Input.Input;
import processing.core.*;

public class Elemento {
	public boolean visible, render;
	public boolean sobre, mover; 
	public int x, y, w, h;
	public Gui gui;
	public String name;
	public Elemento(Gui gui, String name, int x, int y, int w, int h){
		this.gui = gui; 
		this.name = name;
		this.x = x; 
		this.y = y; 
		this.w = w; 
		this.h = h;
		visible = true;
	}
	public void update(int x, int y){
	}
	public void draw(int x, int y){
	}
	
}
