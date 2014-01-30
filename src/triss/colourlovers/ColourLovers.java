/**
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

package triss.colourlovers;


import processing.core.*;
import processing.data.JSONArray;
import processing.data.JSONObject;

/**
 * Provides access to colourlovers.com palettes
 * 
 * @example getRandomPalette 
 * 
 */

public class ColourLovers {
	
	// myParent is a reference to the parent sketch
	PApplet parent;
	
	public final static String VERSION = "##library.prettyVersion##";

	/**
	 * Construct a ColourLovers object that can access colour lovers's palettes
	 * 
	 * @example getRandomPalette
	 * @param theParent - the sketch that's using this plugin
	 */
	public ColourLovers(PApplet theParent) {
		parent = theParent;
	}
	
	/**
	 * Download a palette with id from colour lovers and return
	 * it as ColourLoversPalette
	 * 
	 * @return a ColourLoversPalette with a particular id
	 */
	public ColourLoversPalette getPaletteById(int id) {
		// download blob of json from colour lovers
		String[] colourStrings = parent.loadStrings(
			"http://www.colourlovers.com/api/palette/" + id +"?format=json"
		);
		
		return extractPaletteFromJsonArray(colourStrings[0], 0);
	}
	
	/**
	 * Download a random palette from colour lovers and return
	 * it as a array of processing colours
	 * 
	 * @return a random ColourLoversPalette 
	 */
	public ColourLoversPalette getRandomPalette() {
		// download blob of json from colour lovers
		String[] colourStrings = parent.loadStrings(
			"http://www.colourlovers.com/api/palettes/random?format=json"
		);
		
		return extractPaletteFromJsonArray(colourStrings[0], 0);
	}
	
	/**
	 * Download a the nth top palette from colour lovers and return
	 * it as a array of processing colours
	 * 
	 * @param n the number of the palette in the charts you wish to 
	 * download n must be between 0 and 19, since you only get top 20
	 * palettes back
	 * 
	 * @return the Nth top palette
	 */
	public ColourLoversPalette getNthTopPalette(int n) {
		if(n < 0 || n > 19) {
			throw new IllegalArgumentException(
				"getNthTopPalette(n) only returns 20 top pallettes. n must be between 0 and 19."
			);
		}
		
		// download blob of json from colour lovers
		String[] colourStrings = parent.loadStrings(
			"http://www.colourlovers.com/api/palettes/top?format=json"
		);
		
		return extractPaletteFromJsonArray(colourStrings[0], n);
	}
	
	/**
	 * Download a the nth top palette from colour lovers and return
	 * it as a array of processing colours
	 * 
	 * @param n the number of the palette in the charts you wish to 
	 * download n must be between 0 and 19, since only the 20 newest
	 * palettes are returned 
	 * 
	 * @return the Nth newest palette
	 */
	public ColourLoversPalette getNthNewPalette(int n) {
		if(n < 0 || n > 19) {
			throw new IllegalArgumentException(
				"getNthTopPalette(n) only returns 20 top pallettes. n must be between 0 and 19."
			);
		}
		
		// download blob of json from colour lovers
		String[] colourStrings = parent.loadStrings(
			"http://www.colourlovers.com/api/palettes/new?format=json"
		);
		
		return extractPaletteFromJsonArray(colourStrings[0], n);
	}
	
	/**
	 * Takes json array as a a string and parses out a set of colours from it
	 * 
	 * @param jsonString string downloaded from colourlovers
	 * @return an array of colors 
	 */
	private ColourLoversPalette extractPaletteFromJsonArray(String jsonString, int n) {
		// parse the json string that comes back
		JSONArray jsonArray = JSONArray.parse(jsonString);
		JSONObject jsonObject = jsonArray.getJSONObject(n);

		return new ColourLoversPalette(jsonObject, parent);
	}

}

