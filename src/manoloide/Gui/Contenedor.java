package manoloide.Gui;

import java.util.ArrayList;
import manoloide.Color.Paleta;
import manoloide.Input.Input;
import processing.core.*;

public class Contenedor extends Elemento{
	ArrayList<Elemento> elementos;
	public Contenedor(String name, int x, int y, int w, int h){
		super(null, name, x, y, w, h);
		elementos = new ArrayList<Elemento>();
	}
	public Contenedor(Gui gui, String name, int x, int y, int w, int h){
		super(gui, name, x, y, w, h);
		elementos = new ArrayList<Elemento>();
	}
	public void update(int x, int y){
	}
	public void draw(int x, int y){
	}
	public void add(Elemento e){
		e.gui = gui;
		elementos.add(e);
	}
	public Elemento get(int i){
		Elemento con = elementos.get(i);
		return con;
	}
	public Elemento get(String name){
		Elemento con = null;
		for(int i = 0; i < elementos.size(); i++){
			Elemento aux = elementos.get(i);
			if(aux.name.equals(name)){
				con = aux;
				break;
			}
		}
		return con;
	}
}
