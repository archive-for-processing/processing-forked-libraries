package manoloide.Gui;

import java.util.ArrayList;
import manoloide.Color.Paleta;
import manoloide.Input.Input;
import processing.core.*;

public class Gui {
	private ArrayList<Contenedor> contenedores;
	public Input input;
	public int x, y;
	public Paleta paleta;
	public PApplet applet;
	public PFont font;
	public Gui(PApplet p){
		this.applet = p; 
		paleta = new Paleta(applet.color(5,(int)(255*0.2)), applet.color(250));
		contenedores = new ArrayList<Contenedor>();
		x = 0;
		y = 0;

		applet.registerMethod("pre", this);
		applet.registerMethod("draw", this);
		applet.registerMethod("post", this);


		input = new Input(p);
	}

	public void pre(){
	}
	public void draw(){
		update();
		applet.println(applet.frameCount, "gui");
		applet.pushStyle();
		for(int i = 0; i < contenedores.size(); i++){
			Contenedor c = contenedores.get(i);
			c.draw(x, y);
		}
		applet.popStyle();
	}
	public void post(){
	}
	public void update(){
		for(int i = 0; i < contenedores.size(); i++){
			Contenedor c = contenedores.get(i);
			c.update(x, y);
		}
	}
	public void add(Contenedor c){
		c.gui = this;
		contenedores.add(c);
	}
	public Contenedor get(int i){
		Contenedor con = contenedores.get(i);
		return con;
	}
	public Contenedor get(String name){
		Contenedor con = null;
		for(int i = 0; i < contenedores.size(); i++){
			Contenedor aux = contenedores.get(i);
			if(aux.name.equals(name)){
				con = aux;
				break;
			}
		}
		return con;
	}
}
