package manoloide.Color;

import processing.core.*;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class Paleta {
	private PApplet myParent;
	private int colores[];
	public int size;
	public String name;

	public Paleta() {
		myParent = new PApplet();
		size = 0;
		name = "";
	}

	public Paleta(int... colores) {
		this.colores = colores;
		size = colores.length;
		name = "";
	}

	public Paleta(String nombre) {
		myParent = new PApplet();
		size = 0;
		this.name = nombre;
	}

	public Paleta(String nombre, int... colores) {
		this.colores = colores;
		size = colores.length;
		this.name = nombre;
	}

	public void add(int c) {
		if (colores == null) {
			colores = new int[1];
			colores[0] = c;
		} else {
			int l = colores.length;
			int[] tempArray = new int[l + 1];
			for (int i = 0; i < l; i++) {
				tempArray[i] = colores[i];
			}
			colores = tempArray;
			colores[l] = c;
		}
		size = colores.length;
	}

	public int get(int i) {
		return colores[i];
	}

	public void set(int i, int c) {
		colores[i] = c;
	}

	public void remove(int c) {
		int l = colores.length;
		int aux[] = new int[l - 1];
		for (int i = 0; i < l; i++) {
			if (i < c)
				aux[i] = colores[i];
			if (i > c)
				aux[i - 1] = colores[i];
		}
		colores = aux;
		size = colores.length;
	}

	public int rcol() {
		int r = (int) myParent.random(colores.length);
		return colores[r];
	}

	public boolean load(String src) {
		String extension = src
				.substring(src.lastIndexOf(".") + 1, src.length());
		if (!extension.equals("plt") && !extension.equals("PLT")) {
			return false;
		}
		JSONObject paleta = myParent.loadJSONObject(src);
		name = paleta.getString("name");
		JSONArray aux = paleta.getJSONArray("colours");
		colores = new int[aux.size()];
		for (int i = 0; i < aux.size(); i++) {
			colores[i] = aux.getInt(i);
		}
		size = colores.length;
		return true;
	}
	public void save(String src) {
		JSONObject paleta = new JSONObject();
		paleta.setString("name", name);
		JSONArray aux = new JSONArray();
		for (int j = 0; j < colores.length; j++) {
			aux.append(colores[j]);
		}
		paleta.setJSONArray("colours", aux);
		myParent.saveJSONObject(paleta, src);
	}
}
