package me.max.chartly;

import me.max.chartly.components.color.Looks;
import processing.core.PFont;

/**
 * Class used by the library to store
 * default settings. Changing these can
 * decrease the code needed to create similar graphs.
 * 
 * @author Max Johnson
 */
public class Defaults {
	private static PFont font;
	private static Looks looks;
	
	/**
	 * Loads the default defaults.
	 */
	public static void create() {
		setFont(Chartly.app.createFont("Helvetica", 12));
		setLooks(new Looks(0, new int[] {
					100, 110, 120, 130, 140, 150, 160, 170, 180, 190
				}).setFont(font));
	}

	public static PFont getFont() {
		return font;
	}

	public static void setFont(PFont font) {
		Defaults.font = font;
	}

	public static Looks getLooks() {
		return looks;
	}

	public static void setLooks(Looks looks) {
		Defaults.looks = looks;
	}
}
