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

package visualizer.library;

import processing.core.*;
import ddf.minim.*;


public class Visualizer {
	
	/**
	 * myParent is a reference to the parent sketch
	 */
	public static PApplet Parent;	
	
	/**
	 * Minim reference + var name
	 */
	public static Minim minim;
	
	/**
	 * Minim reference + var name
	 */
	public static AudioPlayer song;
	
	/**
	 * Height of proccesing window
	 */
	public static int height;
	
	/** 
	 * Width of proccesing window
	 */
	public static int width;
	
	/**
	* Defaults
	* Amount of data points/second given
	*/
	public static int sampleRate = 44100;
	
	/**
     *Amount of points remembered (Needs to be larger for slower computers)
	 */
	public static int bufferSize = 2048;
	
	public final static String VERSION = "##library.prettyVersion##";
	
	

	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the library.
	 * 
	 * @param theParent The project that created the class
	 */
	public Visualizer(PApplet theParent) {
		Parent = theParent;
		minim = new Minim(Parent);
		height = Parent.height;
		width = Parent.width;
		welcome();
	}
	
	
	private void welcome() {
		System.out.println("##library.name## ##library.prettyVersion## by ##author##");
	}
	
	
	public String sayHello() {
		return "hello library.";
	}
	
	/**
	 * @param song to play and be analyzed, amount of points remembered, dataPoints/second
	 */
	public void songSet(String songName){
		song = minim.loadFile(songName, bufferSize);
		song.play();
	}
	public void songSet(String songName, int bufferSize){
		Visualizer.bufferSize = bufferSize;
		song = minim.loadFile(songName,Visualizer.bufferSize);
		song.play();
	}
}
