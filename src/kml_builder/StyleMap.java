package kml_builder;

import processing.data.XML;

public class StyleMap extends StyleSelector<StyleMap> {
	protected StyleMap(KMLRoot parent) {
		super(parent, "StyleMap");
	}
	
	public StyleMap() {
		this.cursor = new XML("StyleMap");
	}
	
	public StyleMap addPair(String id, String key, String styleURL) {
		XML cursor, pair;
		pair = getValidChild(this.cursor, "Pair");
		pair.setString("id", id);
		
		// Set internal Content
		cursor = getValidChild(pair, "key");
		cursor.setContent(key);
		cursor = getValidChild(pair, "styleUrl");
		cursor.setContent(styleURL);
		
		return this;
	}
	
	public StyleMap addPair(String id, String key, Style style) {
		XML cursor, pair;
		pair = getValidChild(this.cursor, "Pair");
		pair.setString("id", id);
		
		// Set internal Content
		cursor = getValidChild(pair, "key");
		cursor.setContent(key);
		pair.addChild(style.cursor);
		
		return this;
	}
}
