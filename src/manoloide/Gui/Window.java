package manoloide.Gui;

import manoloide.Color.Paleta;
import manoloide.Input.Input;
import processing.core.*;

public class Window extends Contenedor{
	public Window(String name, int x, int y, int w, int h){
		super(null, name, x, y, w, h);
	}
	public Window(Gui gui, String name, int x, int y, int w, int h){
		super(gui, name, x, y, w, h);
	}
	public void update(int x, int y){
		x = this.x + x;
		y = this.y + y;
		int mouseX = gui.applet.mouseX;
		int mouseY = gui.applet.mouseY;
		int pmouseX = gui.applet.pmouseX;
		int pmouseY = gui.applet.pmouseY;
		if(!visible) return;
		if(mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h) {
			sobre = true;
		} else{
			sobre = false;
		}
		if(gui.input.released) {
			mover = false;
		}
		if(mover) move(mouseX-pmouseX, mouseY-pmouseY);
		if(sobre && gui.input.click && mouseY < y+20) {
			mover = true;
		}
		y += 20;
		for(int i = 0; i < elementos.size(); i++){
			Elemento e = elementos.get(i);
			e.update(x, y);
		}
	}
	public void draw(int x, int y){
		x = this.x + x;
		y = this.y + y;
		if(!visible) return;
		gui.applet.noStroke();
		gui.applet.fill(gui.paleta.get(0));
		gui.applet.rect(x, y, w, h);
		gui.applet.rect(x, y, w, 20);
		gui.applet.fill(gui.paleta.get(1));
		gui.applet.text(name, x+4, y+16);
		y += 20;
		for(int i = 0; i < elementos.size(); i++){
			Elemento e = elementos.get(i);
			e.draw(x, y);
		}
	}
	public void move(int dx, int dy){
		x += dx;
		y += dy;
	}
}
