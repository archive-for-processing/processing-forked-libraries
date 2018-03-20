package fixlib;

import processing.core.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;



/**
 * FixLib is your new utility library.  House all your helper code here, and
 * keep the main sketch.pde as light as possible ( setup, draw, exit, artDaily )
 *
 * NOTE: code dates to 2012
 * - a lot of "app." code in here, can't be a big static lib
 *
 */

public final class Fixlib implements PConstants {

	
	public final static String VERSION = "##library.prettyVersion##";

	//	https://processing.github.io/processing-javadocs/core/processing/core/app.html
	private static PApplet app;	// parent sketch
	private static Fixlib thisLib = null;
	private static int alf = 100;


	private Fixlib() {
		//	singleton pattern, prevents instantiation
		//	best advice is to keep the constructor private.
	}
	public static Fixlib init(PApplet applet) {

		// Create singleton
		if(thisLib==null)
			thisLib = new Fixlib();

		app = applet;
		app.registerMethod("dispose", thisLib);

		return thisLib;
	}

	public void dispose() {
		// Anything in here will be called automatically when
		// the parent sketch shuts down. For instance, this might
		// shut down a thread used by this library.
		app = null;
		thisLib = null;
	}

	/**
	 * Get the alpha
	 *
	 * @return int
	 */
	public static int alpha() {
		return alf;
	}

	/**
	 * Set alpha value to be used with stroke/fill type methods.
	 *
	 * @param newAlf integer value to be used for alpha parameter
	 */
	public void alpha(int newAlf) {
		alf = newAlf;
	}

	



	////////////////////////////////////////////////////
	//  Make a shape out of four vertices.
	//  @MODE : POINTS, LINES, TRIANGLES, TRIANGLE_FAN, TRIANGLE_STRIP, QUADS, QUAD_STRIP
	//  *NOTE : P2D, P3D, and OPENGL renderer settings allow stroke() and fill() settings to be altered
	public void makeShape( float v1, float v2, float v3, float v4, float v5, float v6, float v7, float v8, int MODE ) {

		app.beginShape(MODE);
			app.vertex( v1, v2 );
			app.vertex( v3, v4 );
			app.vertex( v5, v6 );
			app.vertex( v7, v8 );
		app.endShape();
	}


	////////////////////////////////////////////////////////////
	//  Call this to make your sketch window resizable ( * desktop Processing only )
	
	public void makeResizable() {
		if (app.frame != null) {
			app.frame.setResizable(true);
		}
	}

	////////////////////////////////////////////////////////////
	//
	//  Draw an organic ellipse growing outwards like the trunk of a tree
	//	TODO : is this used in any sketch?  seems dumb
	public void trunk(float x, float y ) {
		app.ellipse( x, y, app.frameCount%(app.width/2) , app.frameCount%(app.height/2) );
	}


	//////////////////////////////////////////////////////
	//  Draw grid of circles that grow as the page builds
	
	public void circleGrid(int gWidth, int gHeight )
	{
		float x = 0, y=0;
		float inc = PI*TWO_PI;  //  INCREMENTER
		float sz = inc;

		// circle grid
		for ( int a = 0; a < (gWidth*gHeight)/2; a+= inc ) {
			app.smooth();

			app.ellipse( x, y, sz, sz );

			if ( x < gWidth ) {
				x += sz;
			}
			else {
				x = 0;
				y += sz;
				sz += inc;
			}
		}
	}

	//////////////////////////////////////////////////////
	//
	// TODO: deprecate and replace w/ shapeJous
	public void drawLissajous(float a, float b, float amp )
	{
		//  float amp = 33;
		float x, y;
		float sz = amp / PI;  //TWO_PI;

		for ( float t = 0; t <= 360; t += 1)
		{
			x = a - amp * PApplet.sin(a * t * PI/180);
			y = b - amp * PApplet.sin(b * t * PI/180);
			app.noFill();
			app.ellipse(x, y, sz, sz);
		}
	}





	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Lissajous PShape maker
	 * @param a     X coordinate
	 * @param b     Y coordinate
	 * @param amp   Amplitude or size
	 * @param inc   Loop magic incrementer [ 1 - 36 supported ]. (360 / inc) = number of points in returned PShape
	 * @return  PShape containing vertices in the shape of a lissajous pattern
	 */
	public PShape shapeJous( float a, float b, float amp, int inc )
	{
		int juicePts = 160; //  160 is the NEW hotness -> slightly less points, no blank frames 9-36
		int z = 0;

		//  PROTOTYPING : trying to locate universal ideal INCrementor for lisajouss loop
		//  Ideal range is someplace between 1 and 36
		if( ( inc < 1 ) || ( inc > 36 ) ) {
			inc = 1;
		}

		PShape shp = app.createShape();
		shp.beginShape(POLYGON);

		float x, y;



		for ( int t = 0; t <= juicePts  ; t+=inc)
		{
			//  NEW HOTNESS!
			x = a - amp * PApplet.cos((a * t * TWO_PI)/360);
			y = b - amp * PApplet.sin((b * t * TWO_PI)/360);

			//	give shapes up and down Z-depth
			z = ( t < (juicePts*.5) ) ? t : juicePts-t;

			shp.vertex(x, y, z);
		}

		shp.endShape(CLOSE);

		return shp;
	}



	//////////////////////////////////////////////////////
	//  Pass in a Color, and this will fill even frames with 255,
	//  odd frames with clr
	
	public void evenOddFill(int clr ) {
		if ( app.frameCount % 2 == 0 ) {
			app.fill(255);
		}
		else {
			app.fill(clr);
		}
	}
	//////////////////////////////////////////////////////
	//  Pass in a Color, and this will fill even frames with 255,
	//  odd frames with clr
	//  * INVERTED for evenOddFill() pleasure
	public void evenOddStroke(int clr ) {
		if ( app.frameCount % 2 == 0 ) {
			app.stroke(clr, alf);
		}
		else {
			app.stroke(0, alf);
		}
	}

	/**
	 * evenOddStroke with alpha param
	 * @param clr Integer representation of color
	 * @param newAlf Pass a custom alpha setting for the stroke
	 */
	public void evenOddStroke(int clr, int newAlf ) {

		alpha(newAlf);
		evenOddStroke(clr);
	}



	/////////////////////////////////////////////////////////////////
	//  spit out an 8bit heart
	
	public void bitHeart(float x, float y, boolean grid ) {

		int blockSize = 25;
//		float htSize = 250;

//	TODO: when should you use app vs app?

		app.strokeWeight(0.5f);

		//  GRID
		if (grid) {
			app.stroke( hexToInt("#EFEFEF") );//, 50);

			for ( int xx = 0 ; xx <= 13; xx++ ) {

				app.line( x+(blockSize*xx), 0, x+(blockSize*xx), app.height );
				app.line( 0, y+(blockSize*xx), app.width, y+(blockSize*xx) );
			}
		}
		//  GRID



		//  HEART
		app.stroke( hexToInt("#333333") );

		//  white blocks
		app.fill(255);
		app.rect( x+(blockSize*2), y+blockSize, blockSize, blockSize );
		app.rect( x+(blockSize), y+(blockSize*2), blockSize, blockSize );

		app.fill(0);
		// TODO: make this smarter
		app.rect( x+(blockSize*2), y, blockSize, blockSize );
		app.rect( x+(blockSize*3), y, blockSize, blockSize );
		app.rect( x+(blockSize*4), y, blockSize, blockSize );

		app.rect( x+(blockSize*8), y, blockSize, blockSize );
		app.rect( x+(blockSize*9), y, blockSize, blockSize );
		app.rect( x+(blockSize*10), y, blockSize, blockSize );

		app.rect( x+(blockSize), y+blockSize, blockSize, blockSize );

		app.fill(hexToInt("#EF0000") );
		app.rect( x+(blockSize*3), y+blockSize, blockSize, blockSize );
		app.rect( x+(blockSize*4), y+blockSize, blockSize, blockSize );
		app.rect( x+(blockSize*2), y+(blockSize*2), blockSize, blockSize );
		app.rect( x+(blockSize*3), y+(blockSize*2), blockSize, blockSize );
		app.rect( x+(blockSize*4), y+(blockSize*2), blockSize, blockSize );
		app.rect( x+(blockSize*5), y+(blockSize*2), blockSize, blockSize );
		app.rect( x+(blockSize*7), y+(blockSize*2), blockSize, blockSize );
		app.rect( x+(blockSize*8), y+(blockSize*2), blockSize, blockSize );
		app.rect( x+(blockSize*9), y+(blockSize*2), blockSize, blockSize );
		app.rect( x+(blockSize*10), y+(blockSize*2), blockSize, blockSize );
		app.rect( x+(blockSize*11), y+(blockSize*2), blockSize, blockSize );

		app.rect( x+(blockSize), y+(blockSize*3), blockSize, blockSize );
		app.rect( x+(blockSize*2), y+(blockSize*3), blockSize, blockSize );
		app.rect( x+(blockSize*3), y+(blockSize*3), blockSize, blockSize );
		app.rect( x+(blockSize*4), y+(blockSize*3), blockSize, blockSize );
		app.rect( x+(blockSize*5), y+(blockSize*3), blockSize, blockSize );
		app.rect( x+(blockSize*6), y+(blockSize*3), blockSize, blockSize );
		app.rect( x+(blockSize*7), y+(blockSize*3), blockSize, blockSize );
		app.rect( x+(blockSize*8), y+(blockSize*3), blockSize, blockSize );
		app.rect( x+(blockSize*9), y+(blockSize*3), blockSize, blockSize );
		app.rect( x+(blockSize*10), y+(blockSize*3), blockSize, blockSize );
		app.rect( x+(blockSize*11), y+(blockSize*3), blockSize, blockSize );

		app.rect( x+(blockSize), y+(blockSize*4), blockSize, blockSize );
		app.rect( x+(blockSize*3), y+(blockSize*4), blockSize, blockSize );
		app.rect( x+(blockSize*4), y+(blockSize*4), blockSize, blockSize );
		app.rect( x+(blockSize*5), y+(blockSize*4), blockSize, blockSize );
		app.rect( x+(blockSize*6), y+(blockSize*4), blockSize, blockSize );
		app.rect( x+(blockSize*2), y+(blockSize*4), blockSize, blockSize );
		app.rect( x+(blockSize*7), y+(blockSize*4), blockSize, blockSize );
		app.rect( x+(blockSize*8), y+(blockSize*4), blockSize, blockSize );
		app.rect( x+(blockSize*9), y+(blockSize*4), blockSize, blockSize );
		app.rect( x+(blockSize*10), y+(blockSize*4), blockSize, blockSize );
		app.rect( x+(blockSize*11), y+(blockSize*4), blockSize, blockSize );

		app.rect( x+(blockSize*2), y+(blockSize*5), blockSize, blockSize );
		app.rect( x+(blockSize*3), y+(blockSize*5), blockSize, blockSize );
		app.rect( x+(blockSize*4), y+(blockSize*5), blockSize, blockSize );
		app.rect( x+(blockSize*5), y+(blockSize*5), blockSize, blockSize );
		app.rect( x+(blockSize*6), y+(blockSize*5), blockSize, blockSize );
		app.rect( x+(blockSize*7), y+(blockSize*5), blockSize, blockSize );
		app.rect( x+(blockSize*8), y+(blockSize*5), blockSize, blockSize );
		app.rect( x+(blockSize*9), y+(blockSize*5), blockSize, blockSize );
		app.rect( x+(blockSize*10), y+(blockSize*5), blockSize, blockSize );

		app.rect( x+(blockSize*2), y+(blockSize*6), blockSize, blockSize );
		app.rect( x+(blockSize*3), y+(blockSize*6), blockSize, blockSize );
		app.rect( x+(blockSize*4), y+(blockSize*6), blockSize, blockSize );
		app.rect( x+(blockSize*5), y+(blockSize*6), blockSize, blockSize );
		app.rect( x+(blockSize*6), y+(blockSize*6), blockSize, blockSize );
		app.rect( x+(blockSize*7), y+(blockSize*6), blockSize, blockSize );
		app.rect( x+(blockSize*8), y+(blockSize*6), blockSize, blockSize );
		app.rect( x+(blockSize*9), y+(blockSize*6), blockSize, blockSize );
		app.rect( x+(blockSize*10), y+(blockSize*6), blockSize, blockSize );

		app.rect( x+(blockSize*3), y+(blockSize*7), blockSize, blockSize );
		app.rect( x+(blockSize*4), y+(blockSize*7), blockSize, blockSize );
		app.rect( x+(blockSize*5), y+(blockSize*7), blockSize, blockSize );
		app.rect( x+(blockSize*6), y+(blockSize*7), blockSize, blockSize );
		app.rect( x+(blockSize*7), y+(blockSize*7), blockSize, blockSize );
		app.rect( x+(blockSize*8), y+(blockSize*7), blockSize, blockSize );
		app.rect( x+(blockSize*9), y+(blockSize*7), blockSize, blockSize );

		app.rect( x+(blockSize*4), y+(blockSize*8), blockSize, blockSize );
		app.rect( x+(blockSize*5), y+(blockSize*8), blockSize, blockSize );
		app.rect( x+(blockSize*6), y+(blockSize*8), blockSize, blockSize );
		app.rect( x+(blockSize*7), y+(blockSize*8), blockSize, blockSize );
		app.rect( x+(blockSize*8), y+(blockSize*8), blockSize, blockSize );

		app.rect( x+(blockSize*5), y+(blockSize*9), blockSize, blockSize );
		app.rect( x+(blockSize*6), y+(blockSize*9), blockSize, blockSize );
		app.rect( x+(blockSize*7), y+(blockSize*9), blockSize, blockSize );

		app.rect( x+(blockSize*6), y+(blockSize*10), blockSize, blockSize );

		app.fill(0);




		app.rect( x+(blockSize*5), y+blockSize, blockSize, blockSize );
		app.rect( x+(blockSize*6), y+(blockSize*2), blockSize, blockSize );
		app.rect( x+(blockSize*7), y+(blockSize), blockSize, blockSize );


		app.fill(hexToInt("#EF0000") );
		app.rect( x+(blockSize*4), y+blockSize, blockSize, blockSize );
		app.rect( x+(blockSize*8), y+blockSize, blockSize, blockSize );
		app.rect( x+(blockSize*9), y+blockSize, blockSize, blockSize );
		app.rect( x+(blockSize*10), y+blockSize, blockSize, blockSize );
		app.rect( x+blockSize, y+(blockSize*5), blockSize, blockSize );
		app.rect( x+(blockSize*2), y+(blockSize*7), blockSize, blockSize );
		app.rect( x+(blockSize*3), y+(blockSize*8), blockSize, blockSize );
		app.rect( x+(blockSize*4), y+(blockSize*9), blockSize, blockSize );
		app.rect( x+(blockSize*5), y+(blockSize*10), blockSize, blockSize );
		app.rect( x+(blockSize*6), y+(blockSize*11), blockSize, blockSize );
		app.rect( x+(blockSize*7), y+(blockSize*10), blockSize, blockSize );
		app.rect( x+(blockSize*8), y+(blockSize*9), blockSize, blockSize );
		app.rect( x+(blockSize*9), y+(blockSize*8), blockSize, blockSize );
		app.rect( x+(blockSize*10), y+(blockSize*7), blockSize, blockSize );
		app.rect( x+(blockSize*11), y+(blockSize*5), blockSize, blockSize );

		app.fill(0);
		app.rect( x+(blockSize*11), y+blockSize, blockSize, blockSize );
		app.rect( x, y+(blockSize*2), blockSize, blockSize );
		app.rect( x, y+(blockSize*3), blockSize, blockSize );
		app.rect( x, y+(blockSize*4), blockSize, blockSize );
		app.rect( x, y+(blockSize*5), blockSize, blockSize );
		app.rect( x+blockSize, y+(blockSize*6), blockSize, blockSize );
		app.rect( x+blockSize, y+(blockSize*7), blockSize, blockSize );
		app.rect( x+(blockSize*2), y+(blockSize*8), blockSize, blockSize );
		app.rect( x+(blockSize*3), y+(blockSize*9), blockSize, blockSize );
		app.rect( x+(blockSize*4), y+(blockSize*10), blockSize, blockSize );
		app.rect( x+(blockSize*5), y+(blockSize*11), blockSize, blockSize );
		app.rect( x+(blockSize*6), y+(blockSize*12), blockSize, blockSize );
		app.rect( x+(blockSize*7), y+(blockSize*11), blockSize, blockSize );
		app.rect( x+(blockSize*8), y+(blockSize*10), blockSize, blockSize );
		app.rect( x+(blockSize*9), y+(blockSize*9), blockSize, blockSize );
		app.rect( x+(blockSize*10), y+(blockSize*8), blockSize, blockSize );
		app.rect( x+(blockSize*11), y+(blockSize*7), blockSize, blockSize );
		app.rect( x+(blockSize*11), y+(blockSize*6), blockSize, blockSize );

		app.rect( x+(blockSize*12), y+(blockSize*5), blockSize, blockSize );
		app.rect( x+(blockSize*12), y+(blockSize*4), blockSize, blockSize );
		app.rect( x+(blockSize*12), y+(blockSize*3), blockSize, blockSize );
		app.rect( x+(blockSize*12), y+(blockSize*2), blockSize, blockSize );

	}


	public void star(int n, float cx, float cy, float r, float proportion)
	{
		star(n, cx, cy, (float)(2.0 * r), (float)(2.0 * r), 0.0f, proportion);
	}


	///////////////////////////////////////////////////////////
	//  draw a star
	
	public void star(int n, float cx, float cy, float w, float h, float startAngle, float proportion)
	{
		if (n > 2)
		{
			float angle = TWO_PI/ (2 *n);  // twice as many sides
			float dw; // draw width
			float dh; // draw height

			w = (float)(w / 2.0);
			h = (float)(h / 2.0);

			app.beginShape();
			for (int i = 0; i < 2 * n; i++)
			{
				dw = w;
				dh = h;
				if (i % 2 == 1) // for odd vertices, use short radius
				{
					dw = w * proportion;
					dh = h * proportion;
				}
				app.vertex(cx + dw * PApplet.cos(startAngle + angle * i),
						cy + dh * PApplet.sin(startAngle + angle * i));
			}
			app.endShape(CLOSE);
		}
	}


	/**
	 * Get the next number in a fibonacci sequence
	 *
	 * @param f2 Integer value
	 *
	 * @return an incremented version of passed f2
	 */
	
	public int nextFib(int f2)
	{
		int f0;
		int f1 = 1;
		//int f2 = 1;

		int result;
		// TODO: validate this works
		f0 = f1;
		f1 = f2;
		f2 = f0 + f1;
		result = f2;

		return result;
	}

	/**
	 * Get next fibonacci number after supplied number
	 * @param n
	 * @return
	 */
	public int fib(int n) {
		// TODO: validate this works
		//	TODO: how does this compare to nextFib()
		if (n <= 1) return n;
		return fib(n - 1) + fib(n - 2);
	}

	//////////////////////////
	//  Calculate max loop count
	
	public float getMax(float shapeSize ) {
		return ( ( app.width * app.height ) / shapeSize );
	}

	/**
	 *
	 * @param img PImage to retrieve colors from
	 * @return ArrayList of integer colors harvested from @img pixels
	 */
	public ArrayList<Integer> getImgColors(PImage img)
	{
		HashSet<Integer> hsColors = new HashSet<>();

		img.loadPixels();

		for ( int c = 0; c < img.pixels.length; c++ )
// NOTE: this method doesn't work?!?!?
//		for( int c : img.pixels )
		{
			// Math.abs to address -negative values
			if(img.pixels[c]<0)
				hsColors.add( Math.abs( img.pixels[c] ) );
			else
				hsColors.add( img.pixels[c] );

		}
		return new ArrayList<>(hsColors);
	}

	/**
	 * Make grid of shapes filled with each Color in supplied int[]
	 * @param pall
	 */
	public void paletteGrid( int[] pall ) {
		paletteGrid( new ArrayList<Integer>() {{ for (int i : pall) add(i); }} );
	}


	///////////////////////////////////////////////////////
	//  Make grid of shapes filled with each Color in supplied
	//  ArrayList
	public void paletteGrid( ArrayList<Integer> pall ) {

		float xx = 0;
		float yy = 0;
		float sz = 8;

		app.stroke(0xEFEFEF);

		for (Integer ii : pall) {
// TODO: this seems redundant
			app.fill( hexToInt("#"+ PApplet.hex(ii, 6)), alf);

			app.rect(xx, yy, sz, sz);

			if (xx < app.width) {
				xx += (sz * 1.11);
			} else {
				xx = 0;
				yy += (sz * 1.11);
			}
		}


		app.textFont( app.createFont( "Georgia", 222 ) );
		app.fill(app.random(alf));
		app.text( pall.size(), app.random( alf, app.width/3 ), app.random(app.height) );
	}




	///////////////////////////////////////////////////////
	//  Make grid of shapes filled with each Color in supplied
	//  int[], fills entire screen
	
	public void paletteGridFull(ArrayList<Integer> pall ) {

		float xx = 0;
		float yy = 0;
		float sz = 30;
//		float startX;  //  the X where the previous loop left off
//		float startY;  //  the Y where the previous loop left off
		int tmp;
		int clridx = 0;  // use this index to walk through the supplied ArrayList of Colors

		for ( int gg = 0; gg < (app.height+app.width)/2; gg++ ) {


			app.noStroke();
			tmp = pall.get(clridx);
			app.fill( tmp, alf*3 );

			app.rect( xx, yy, sz, sz );

			if ( xx < app.width ) {
				xx += (sz *1.25);
			}
			else {
				xx = 0;
				yy += (sz *1.25);
			}

			//  make sure the Color index doesn't get larger than the array
			if ( clridx == pall.size()-1 )
				clridx = 0;
			else
				clridx++;
		}
	}




	//////////////////////////////////////////////////////////////////////////
	//  Draw manual circle
	//  ellipse(x, y, width, height)
	public void circle( float startX, float startY, float w, float h ) {

		float angle = 0;
		float x, y;

		while ( angle < 360 ) {

			// make circle draw faster by skipping angles
			if ( angle % 10 == 0 ) {

				x = startX - PApplet.cos(PApplet.radians(angle)) * w;
				y = startY - PApplet.sin(PApplet.radians(angle)) * w;

				app.smooth();
				app.ellipse( x, y, w, h );
			}
			angle++;
		}
	}

	///////////////////////////////////////////////////////////////////////////
	//  draw a circle of circles
	
	public void circle(float startX, float startY, float w, float h, float modAngle ) {

		float angle = 0;
		float x, y;

		while ( angle < 360 ) {

			// make circle draw faster by skipping angles
			if ( angle % modAngle == 0 ) {

				x = startX - PApplet.cos(PApplet.radians(angle)) * w;
				y = startY - PApplet.sin(PApplet.radians(angle)) * w;

				app.smooth();
				app.ellipse( x, y, w, h );
			}
			angle++;
		}
	}

	/**
	 * lines and circles
	 * @param r1
	 * @param a1
	 * @param r2
	 * @param a2
	 */
	public void radialLine(float r1, float a1, float r2, float a2){
// TODO : smarten up
		float x1 = app.width/2  + PApplet.cos(a1)*r1;
		float y1 = app.height/2 + PApplet.sin(a1)*r1;
		float x2 = app.width/2  + PApplet.cos(a2)*r2;
		float y2 = app.height/2 + PApplet.sin(a2)*r2;

		app.line(x1, y1, x2, y2);
//    fill(255);
		app.noFill();
		app.ellipse(x1,y1, TWO_PI, TWO_PI);
		app.ellipse(x2,y2, TWO_PI, TWO_PI);

		//println([r1,a1,r2,a2]);
	}


	/**
	 * Draw a hexagon out of supplied starting point and size
	 *
	 * @see  @hexagon inspired by http://www.rdwarf.com/lerickson/hex/index.html
	 *
	 * @param startX
	 * @param startY
	 * @param shapeSize
	 */
	//	TODO: replace this with a PShape hexagon generator
	public void hexagon( float startX, float startY, float shapeSize ) {

		app.line( startX, (float)(startY+(shapeSize*.5)), (float)(startX+(shapeSize*.25)), startY );
		app.line( (float)(startX+(shapeSize*.25)), startY, (float)(startX+(shapeSize*.75)), startY );
		app.line( (float)(startX+(shapeSize*.75)), startY, startX+(shapeSize), (float)(startY+(shapeSize*.5)) );

		app.line( startX+(shapeSize), (float)(startY+(shapeSize*.5)), (float)(startX+(shapeSize*.75)), startY+shapeSize );
		app.line( (float)(startX+(shapeSize*.75)), startY+shapeSize, (float)(startX+(shapeSize*.25)), startY+shapeSize );
		app.line( (float)(startX+(shapeSize*.25)), startY+shapeSize, startX, (float)(startY+(shapeSize*.5)) );
	}

	/**
	 * Draw a hexagon w/ints
	 * @param startX
	 * @param startY
	 * @param shapeSize
	 */
	public void hexagon( int startX, int startY, int shapeSize ) {
		hexagon((float)startX, (float)startY, (float)shapeSize);
	}


	///////////////////////////////////////////////////////
// Generate a path between supplied points
	public ArrayList<PVector> GeneratePath( PVector startPt, PVector endPt, Integer step )
	{
		ArrayList<PVector>	path = new ArrayList<PVector>();

		PVector thisPt;
		PVector lastPt = new PVector(startPt.x, startPt.y);

		while( lastPt.x < endPt.x )
		{
// TODO: need better path generation logic
			thisPt = new PVector(
					lastPt.x+step,
					app.random(app.height-step) );

			path.add(thisPt);

			lastPt = thisPt;
		}

		return path;
	}

	////////////////////////////////////////////////////
	//  Return a random Color from supplied palette
	
	public int getRanColor(ArrayList<Integer> palette)
	{
		return (int)palette.get( (int)app.random( palette.size()-1) );
	}



	/**
	 * Return a random vector based on supplied x,y and sz
	 * @param x
	 * @param y
	 * @param sz
	 * @return PVector
	 */
	public PVector GetRandVector( float x, float y, float sz )
	{
		return new PVector(
				x - (int)( PApplet.cos(PApplet.radians( app.random(360) )) * sz ),
				y - (int)( PApplet.sin(PApplet.radians( app.random(360) )) * sz ),
				app.random(-sz,sz)
		);
	}


	////////////////////////////////////////////////////
	//  Randomly stroke using image from Color list
	public void ranPalStroke(int[] palette)
	{
		// palette
		app.stroke( palette[ (int)( app.random( palette.length-1 ) ) ] , alf );
	}
	
	public void ranPalStroke(ArrayList<Integer> palette)
	{
		// palette
		app.stroke( (int)palette.get( (int)app.random( palette.size()-1 ) ), alf );
	}
	
	public void ranPalStroke100(int[] palette)
	{
		// palette
		app.stroke( palette[ (int) (app.random( palette.length-1 )) ], 100 );
	}
	
	public void ranPalStroke100(ArrayList<Integer> palette)
	{
		// palette
		app.stroke( (int)palette.get( (int)app.random( palette.size()-1 ) ), 100 );
	}
	public void ranPalFill(int[] palette)
	{
		app.fill( palette[ (int)(app.random( palette.length-1 )) ], alf );
	}
	
	public void ranPalFill(ArrayList<Integer> palette)
	{
		// palette
		app.fill( (int)palette.get( (int)app.random( palette.size()-1 ) ), alf );
	}
	
	public void ranPalFill100(int[] palette)
	{
		// palette
		app.fill( palette[ (int) app.random( palette.length-1 ) ], 100 );
	}
	
	public void ranPalFill100(ArrayList<Integer> palette)
	{
		// palette
		app.fill( (int)palette.get( (int)app.random( palette.size()-1 ) ), 100 );
	}

	///////////////////////////////////////////////////////////
	//  Helper to random(255) stroke
	public void randFill() {
		app.fill( app.random(255), app.random(255), app.random(255), alf );
	}
	
	public void randFill100() {
		app.fill( app.random(255), app.random(255), app.random(255), 100 );
	}
	
	public void randStroke() {
		app.stroke( app.random(255), app.random(255), app.random(255), alf );
	}
	
	public void randStroke100() {
		app.stroke( app.random(255), app.random(255), app.random(255), 100 );
	}

	///////////////////////////////////////////////////////////////////////

	/**
	 * Draw a bullseye at specified x/y coordinates
	 * @param a
	 * @param b
	 * @param shapeSize
	 */
	public void bullsEye( float a, float b, float shapeSize ){

		int tmp = (int)shapeSize;

		while(  tmp >= 0 ) {

			app.strokeWeight(tmp/24);
			app.fill(0);
			app.stroke(255);

			app.ellipse( a, b, tmp, tmp);
			tmp -= (int)(shapeSize/6);
		}


	}

	/**
	 * Draw a rect eye ( bullseye w/rect ) at specified x/y coords
	 * @param a
	 * @param b
	 * @param shapeSize
	 */
	public void rectEye( float a, float b, float shapeSize ){

		int tmp = (int)shapeSize;

		while(  tmp >= 0 ) {

			app.strokeWeight(tmp/24);
			app.fill(0);
			app.stroke(255);

			app.rect( a, b, tmp, tmp, app.random(tmp) );

			tmp -= (int)(shapeSize/6);
		}


	}

	/**
	 *	Create a SWITCH based drawing system that accepts X, Y, and
	 *	randomly choose which movement system to fire
	 * @param x
	 * @param y
	 */
	public void systems( float x, float y )
	{
		int pick = PApplet.floor(app.random(0,3));

		app.fill(app.random(21,37),75);
		app.text( x + " " + y + " " + pick, app.width-x, app.height-y );
		app.noFill();

		switch( pick ){

			case 0:
			{
				x = PApplet.floor( (app.width/2)+PApplet.cos(PApplet.radians(app.frameCount))*(x-y) );
				y = PApplet.floor( (app.height/2)+PApplet.sin(PApplet.radians(app.frameCount))*(y-x) );
				app.point( x-PI, y+PI );
				app.point( y+PI, X-PI );
			}
			break;

			case 1:
			{
				app.ellipse( x*PApplet.cos(app.frameCount)*PApplet.radians(TWO_PI), y*PApplet.sin(app.frameCount)*PApplet.radians(TWO_PI), alf, alf );
				app.ellipse( y*PApplet.cos(app.frameCount)*PApplet.radians(TWO_PI), x*PApplet.sin(app.frameCount)*PApplet.radians(TWO_PI), alf, alf );
			}
			break;

			case 2:
			{
				app.strokeWeight(.75f);
				app.rect( x, y, TWO_PI+x*app.noise(app.frameCount), TWO_PI+x*app.noise(app.frameCount) );
				app.rect( y, x, TWO_PI+y*app.noise(app.frameCount), TWO_PI+y*app.noise(app.frameCount) );
			}
			break;

			case 3:
			{
				app.point( x * PApplet.cos(x)*app.frameCount, y*PApplet.sin(y)*app.frameCount );
				app.point( y * PApplet.cos(x)*app.frameCount, x*PApplet.sin(y)*app.frameCount );

				app.ellipse( x * PApplet.cos(x)*app.frameCount, y*PApplet.sin(y)*app.frameCount, app.frameCount, app.frameCount );
				app.ellipse( y * PApplet.cos(x)*app.frameCount, x*PApplet.sin(y)*app.frameCount, app.frameCount, app.frameCount );
			}
			break;

		}
	}


	public String getTimestamp() {
		//  Calendar now = Calendar.getInstance();
		//  return String.format("%1$ty%1$tm%1$td_%1$tH%1$tM", now);


		return ""+ PApplet.month()+ PApplet.day()+ PApplet.year()+ PApplet.hour()+ PApplet.second()+app.millis();
	}


	//////////////////////////////////////////////////////
	//  Returns the file name that is calling this function ( minus the .pde )
	public String pdeName() {
		String[] pde = app.sketchPath().split( "/");
		return pde[pde.length-1];
	}

	/**
	 * Convert hex color such as #333333 to int value
	 * @param hexColor
	 * @return int equivalent
	 */
	public int hexToInt( String hexColor ){
		return Color.decode(hexColor).getRGB();
	}


	/**
	 * draws circle from supplied x, y
	 * @param XX
	 * @param YY
	 * @param maxSize
	 */
	public void drawCore( int XX, int YY, int maxSize ) {

		float r = 1;  // 75;
		float theta = 2;

		int alf = 10;
		float x;
		float y;

		app.smooth();
		app.strokeWeight(.13f);

		while ( theta <= maxSize )
		{
			x = (PI*r) * PApplet.cos(theta);
			y = (PI*r) * PApplet.sin(theta);

			if (x%2==0)app.stroke(255, alf);
			else if (x%3==0) app.stroke(255, 0, 0, alf);
			else app.stroke( 109, 109, 109, alf);

			app.ellipse( (int)(XX+x), (int)(YY+y), x, x );

			heart( (int)(YY+y), (int)(XX+x), (int)(y), (int)(y) );

			theta+= 0.25;

			if ( app.frameCount%2==0) {
				r++;
				theta+=.5;
			}
		}
	}

	/**
	 * draw frame
	 */
	public void drawFrame() {

		app.rectMode(CORNER);

		app.strokeWeight( 100 );
		app.rect( 0, 0, app.width, app.height);

		app.strokeWeight( 5 );
		app.rect( 64, 65, app.width-128, app.height-129 );

		app.strokeWeight( 15 );
		app.rect( 84, 85, app.width-168, app.height-168 );

		//  dashed line
		app.strokeWeight(6);
		int x = 110;
		int y = 110;

		app.strokeCap(PROJECT);

		for ( int pp = 0; pp <= (app.width*app.height); pp++ ) {

			if ( x <= (app.width-110) ) {
				// top row
				app.point( x, y );
				//  bottom row
				app.point( x, 790 );

				x += 10;
			}
			else if ( y <= 780 ) {

				// left row
				app.point( (app.width-110), y );
				//  right row
				app.point( 110, y );
				y += 10;
			}
		}

		app.strokeWeight( 5 );
		app.rect( 122, 122, app.width-245, app.height-245 );
	}


	///////////////////////////////////////////////////////////
//
//  draw heart
	public void heart( int x, int y, int w, int h )
	{
		app.ellipseMode(RADIUS);
		app.smooth();

		//  stroke(#EF7519, alf);  // 37
		app.stroke(hexToInt("#EF1111"), alf);  // 37

		app.strokeWeight(2);
		//  noFill();

		//  bubbles
		app.ellipse( x-w, y, w, w);
		app.ellipse( x+w, y, w, w);
		//  ellipseMode(MODE)
		//  MODE	Either CENTER, RADIUS, CORNER, or CORNERS


		//  lines
		app.line( x-(w*2), y, x, y + w*PI);
		app.line( x+(w*2), y, x, y + w*PI);
	}


	/**
	 * Makes some text lines
	 */
	public void textLines() {

		app.textFont( app.createFont( "Helvetica", 300 ) );

		app.fill(10, PApplet.pow(alf, 1.5f) );//, (alf*4) );
		// MAKE TEXT BIG
		app.fill( hexToInt("#210000"), PApplet.pow(alf, 1.5f) );//, (alf*4) );
		app.text("lines.lines.lines.lines.lines", 0, app.height*.3f );
		app.text("lines.lines.lines.lines.lines", 0, app.height*.3f );
		//  & curve
		app.fill(hexToInt("#210000"), PApplet.pow(alf, 1.5f) );//, (alf*4) );
		app.text("lines.lines.lines.lines.lines", 0, app.height*.49f );
		app.fill(hexToInt("#000021"), PApplet.pow(alf, 1.5f) );//, (alf*4) );
		app.text("lines.lines.lines.lines.lines", 0, app.height*.49f );
		//  & quad
		app.fill(hexToInt("#210000"), PApplet.pow(alf, 1.5f) );//, (alf*4) );
		app.text("lines.lines.lines.lines.lines", 0, app.height*.65f );
		app.fill(hexToInt("#000021"), PApplet.pow(alf, 1.5f) );//, (alf*4) );
		app.text("lines.lines.lines.lines.lines", 0, app.height*.65f );

		//  & triangle
		app.fill(hexToInt("#210000"), PApplet.pow(alf, 1.5f) );//, (alf*4) );
		app.text("lines.lines.lines.lines.lines", 0, app.height*.85f );
		app.fill(hexToInt("#000021"), PApplet.pow(alf, 1.5f) );//, (alf*4) );
		app.text("lines.lines.lines.lines.lines", 0, app.height*.85f );
	}


	/**
	 * Creates a PVector at a given circle w/radius @radius, and center point @centerX / @centerY at @angle position
	 * @param centerX
	 * @param centerY
	 * @param radius
	 * @param angle
	 * @return
	 */
	public PVector circleXY( float centerX, float centerY, float radius, float angle )
	{
		return new PVector(
				centerX - (int)( PApplet.cos(PApplet.radians(angle)) * radius ),
			centerY - (int)( PApplet.sin(PApplet.radians(angle)) * radius )
                );
	}


	/**
	 * A utility function that returns true if x is perfect square
	 * @param x
	 * @return
	 */
	public boolean isPerfectSquare(float x)
	{
		int s = (int)PApplet.sqrt(x);
		return (s*s == x);
	}

	/**
	 * Returns true if n is a Fibinacci Number, else false
	 * @param n
	 * @return
	 */
	public boolean isFibonacci(float n)
	{
		// n is Fibinacci if one of 5*n*n + 4 or 5*n*n - 4 or both
		// is a perferct square
		return isPerfectSquare(5*n*n + 4) ||
				isPerfectSquare(5*n*n - 4);
	}

}

