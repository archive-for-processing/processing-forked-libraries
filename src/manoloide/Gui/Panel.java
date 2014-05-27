package manoloide.Gui;

import manoloide.Color.Paleta;
import manoloide.Input.Input;
import processing.core.*;

public class Panel extends Contenedor{
	public Panel(String name, int x, int y, int w, int h){
		super(null, name, x, y, w, h);
	}
	public Panel(Gui gui, String name, int x, int y, int w, int h){
		super(gui, name, x, y, w, h);
	}
	public void update(int x, int y){
		x = this.x + x;
		y = this.y + y;
		for(int i = 0; i < elementos.size(); i++){
			Elemento e = elementos.get(i);
			e.update(x, y);
		}
	}
	public void draw(int x, int y){
		x = this.x + x;
		y = this.y + y;
		gui.applet.noStroke();
		gui.applet.fill(gui.paleta.get(0));
		gui.applet.rect(x, y, w, h);
		for(int i = 0; i < elementos.size(); i++){
			Elemento e = elementos.get(i);
			e.draw(x, y);
		}
	}
}
