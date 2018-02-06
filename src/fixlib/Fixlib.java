package fixlib;

import processing.core.*;
import java.util.ArrayList;


/**
 * FixLib is your new utility library.  House all your helper code here, and
 * keep the main sketch.pde as light as possible ( setup, draw, exit, artDaily )
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
	public static void alpha(int newAlf) {
		alf = newAlf;
	}

	



	////////////////////////////////////////////////////
	//  Make a shape out of four vertices.
	//  @MODE : POINTS, LINES, TRIANGLES, TRIANGLE_FAN, TRIANGLE_STRIP, QUADS, QUAD_STRIP
	//  *NOTE : P2D, P3D, and OPENGL renderer settings allow stroke() and fill() settings to be altered
	public void makeShape( float v1, float v2, float v3, float v4, float v5, float v6, float v7, float v8, int MODE ) {

		app.g.beginShape(MODE);
			app.g.vertex( v1, v2 );
			app.g.vertex( v3, v4 );
			app.g.vertex( v5, v6 );
			app.g.vertex( v7, v8 );
		app.g.endShape();
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

//	TODO: fix app.frameCount to ACTUALLY grab the frameCount of this lib's parent -> app
		app.g.ellipse( x, y, app.frameCount, app.frameCount%(app.height/2) );
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
			app.g.smooth();

			app.g.ellipse( x, y, sz, sz );

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
	
	public void drawLissajous(float a, float b, float amp )
	{
		//  float amp = 33;
		float x, y;
		float sz = amp / PI;  //TWO_PI;

		for ( float t = 0; t <= 360; t += 1)
		{
			x = a - amp * app.sin(a * t * PI/180);
			y = b - amp * app.sin(b * t * PI/180);
			app.g.noFill();
			app.g.ellipse(x, y, sz, sz);
		}
	}



	//////////////////////////////////////////////////////
	//  Pass in a Color, and this will fill even frames with 255,
	//  odd frames with clr
	
	public void evenOddFill(int clr ) {
		if ( app.frameCount % 2 == 0 ) {
			app.g.fill(255);
		}
		else {
			app.g.fill(clr);
		}
	}
	//////////////////////////////////////////////////////
	//  Pass in a Color, and this will fill even frames with 255,
	//  odd frames with clr
	//  * INVERTED for evenOddFill() pleasure
	public void evenOddStroke(int clr ) {
		if ( app.frameCount % 2 == 0 ) {
			app.g.stroke(clr, alf);
		}
		else {
			app.g.stroke(0, alf);
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
		app.g.strokeWeight(0.5f);

		//  GRID
		if (grid) {
			app.g.stroke(0xEFEFEF);//, 50);

			for ( int xx = 0 ; xx <= 13; xx++ ) {

				app.g.line( x+(blockSize*xx), 0, x+(blockSize*xx), app.g.height );
				app.g.line( 0, y+(blockSize*xx), app.g.width, y+(blockSize*xx) );
			}
		}
		//  GRID



		//  HEART
		app.g.stroke(0x333333);

		//  white blocks
		app.g.fill(255);
		app.g.rect( x+(blockSize*2), y+blockSize, blockSize, blockSize );
		app.g.rect( x+(blockSize), y+(blockSize*2), blockSize, blockSize );

		app.g.fill(0);
		// TODO: make this smarter
		app.g.rect( x+(blockSize*2), y, blockSize, blockSize );
		app.g.rect( x+(blockSize*3), y, blockSize, blockSize );
		app.g.rect( x+(blockSize*4), y, blockSize, blockSize );

		app.g.rect( x+(blockSize*8), y, blockSize, blockSize );
		app.g.rect( x+(blockSize*9), y, blockSize, blockSize );
		app.g.rect( x+(blockSize*10), y, blockSize, blockSize );

		app.g.rect( x+(blockSize), y+blockSize, blockSize, blockSize );

		app.g.fill(0xEF0000);
		app.g.rect( x+(blockSize*3), y+blockSize, blockSize, blockSize );
		app.g.rect( x+(blockSize*4), y+blockSize, blockSize, blockSize );
		app.g.rect( x+(blockSize*2), y+(blockSize*2), blockSize, blockSize );
		app.g.rect( x+(blockSize*3), y+(blockSize*2), blockSize, blockSize );
		app.g.rect( x+(blockSize*4), y+(blockSize*2), blockSize, blockSize );
		app.g.rect( x+(blockSize*5), y+(blockSize*2), blockSize, blockSize );
		app.g.rect( x+(blockSize*7), y+(blockSize*2), blockSize, blockSize );
		app.g.rect( x+(blockSize*8), y+(blockSize*2), blockSize, blockSize );
		app.g.rect( x+(blockSize*9), y+(blockSize*2), blockSize, blockSize );
		app.g.rect( x+(blockSize*10), y+(blockSize*2), blockSize, blockSize );
		app.g.rect( x+(blockSize*11), y+(blockSize*2), blockSize, blockSize );

		app.g.rect( x+(blockSize), y+(blockSize*3), blockSize, blockSize );
		app.g.rect( x+(blockSize*2), y+(blockSize*3), blockSize, blockSize );
		app.g.rect( x+(blockSize*3), y+(blockSize*3), blockSize, blockSize );
		app.g.rect( x+(blockSize*4), y+(blockSize*3), blockSize, blockSize );
		app.g.rect( x+(blockSize*5), y+(blockSize*3), blockSize, blockSize );
		app.g.rect( x+(blockSize*6), y+(blockSize*3), blockSize, blockSize );
		app.g.rect( x+(blockSize*7), y+(blockSize*3), blockSize, blockSize );
		app.g.rect( x+(blockSize*8), y+(blockSize*3), blockSize, blockSize );
		app.g.rect( x+(blockSize*9), y+(blockSize*3), blockSize, blockSize );
		app.g.rect( x+(blockSize*10), y+(blockSize*3), blockSize, blockSize );
		app.g.rect( x+(blockSize*11), y+(blockSize*3), blockSize, blockSize );

		app.g.rect( x+(blockSize), y+(blockSize*4), blockSize, blockSize );
		app.g.rect( x+(blockSize*3), y+(blockSize*4), blockSize, blockSize );
		app.g.rect( x+(blockSize*4), y+(blockSize*4), blockSize, blockSize );
		app.g.rect( x+(blockSize*5), y+(blockSize*4), blockSize, blockSize );
		app.g.rect( x+(blockSize*6), y+(blockSize*4), blockSize, blockSize );
		app.g.rect( x+(blockSize*2), y+(blockSize*4), blockSize, blockSize );
		app.g.rect( x+(blockSize*7), y+(blockSize*4), blockSize, blockSize );
		app.g.rect( x+(blockSize*8), y+(blockSize*4), blockSize, blockSize );
		app.g.rect( x+(blockSize*9), y+(blockSize*4), blockSize, blockSize );
		app.g.rect( x+(blockSize*10), y+(blockSize*4), blockSize, blockSize );
		app.g.rect( x+(blockSize*11), y+(blockSize*4), blockSize, blockSize );

		app.g.rect( x+(blockSize*2), y+(blockSize*5), blockSize, blockSize );
		app.g.rect( x+(blockSize*3), y+(blockSize*5), blockSize, blockSize );
		app.g.rect( x+(blockSize*4), y+(blockSize*5), blockSize, blockSize );
		app.g.rect( x+(blockSize*5), y+(blockSize*5), blockSize, blockSize );
		app.g.rect( x+(blockSize*6), y+(blockSize*5), blockSize, blockSize );
		app.g.rect( x+(blockSize*7), y+(blockSize*5), blockSize, blockSize );
		app.g.rect( x+(blockSize*8), y+(blockSize*5), blockSize, blockSize );
		app.g.rect( x+(blockSize*9), y+(blockSize*5), blockSize, blockSize );
		app.g.rect( x+(blockSize*10), y+(blockSize*5), blockSize, blockSize );

		app.g.rect( x+(blockSize*2), y+(blockSize*6), blockSize, blockSize );
		app.g.rect( x+(blockSize*3), y+(blockSize*6), blockSize, blockSize );
		app.g.rect( x+(blockSize*4), y+(blockSize*6), blockSize, blockSize );
		app.g.rect( x+(blockSize*5), y+(blockSize*6), blockSize, blockSize );
		app.g.rect( x+(blockSize*6), y+(blockSize*6), blockSize, blockSize );
		app.g.rect( x+(blockSize*7), y+(blockSize*6), blockSize, blockSize );
		app.g.rect( x+(blockSize*8), y+(blockSize*6), blockSize, blockSize );
		app.g.rect( x+(blockSize*9), y+(blockSize*6), blockSize, blockSize );
		app.g.rect( x+(blockSize*10), y+(blockSize*6), blockSize, blockSize );

		app.g.rect( x+(blockSize*3), y+(blockSize*7), blockSize, blockSize );
		app.g.rect( x+(blockSize*4), y+(blockSize*7), blockSize, blockSize );
		app.g.rect( x+(blockSize*5), y+(blockSize*7), blockSize, blockSize );
		app.g.rect( x+(blockSize*6), y+(blockSize*7), blockSize, blockSize );
		app.g.rect( x+(blockSize*7), y+(blockSize*7), blockSize, blockSize );
		app.g.rect( x+(blockSize*8), y+(blockSize*7), blockSize, blockSize );
		app.g.rect( x+(blockSize*9), y+(blockSize*7), blockSize, blockSize );

		app.g.rect( x+(blockSize*4), y+(blockSize*8), blockSize, blockSize );
		app.g.rect( x+(blockSize*5), y+(blockSize*8), blockSize, blockSize );
		app.g.rect( x+(blockSize*6), y+(blockSize*8), blockSize, blockSize );
		app.g.rect( x+(blockSize*7), y+(blockSize*8), blockSize, blockSize );
		app.g.rect( x+(blockSize*8), y+(blockSize*8), blockSize, blockSize );

		app.g.rect( x+(blockSize*5), y+(blockSize*9), blockSize, blockSize );
		app.g.rect( x+(blockSize*6), y+(blockSize*9), blockSize, blockSize );
		app.g.rect( x+(blockSize*7), y+(blockSize*9), blockSize, blockSize );

		app.g.rect( x+(blockSize*6), y+(blockSize*10), blockSize, blockSize );

		app.g.fill(0);




		app.g.rect( x+(blockSize*5), y+blockSize, blockSize, blockSize );
		app.g.rect( x+(blockSize*6), y+(blockSize*2), blockSize, blockSize );
		app.g.rect( x+(blockSize*7), y+(blockSize), blockSize, blockSize );


		app.g.fill(0xEF0000);
		app.g.rect( x+(blockSize*4), y+blockSize, blockSize, blockSize );
		app.g.rect( x+(blockSize*8), y+blockSize, blockSize, blockSize );
		app.g.rect( x+(blockSize*9), y+blockSize, blockSize, blockSize );
		app.g.rect( x+(blockSize*10), y+blockSize, blockSize, blockSize );
		app.g.rect( x+blockSize, y+(blockSize*5), blockSize, blockSize );
		app.g.rect( x+(blockSize*2), y+(blockSize*7), blockSize, blockSize );
		app.g.rect( x+(blockSize*3), y+(blockSize*8), blockSize, blockSize );
		app.g.rect( x+(blockSize*4), y+(blockSize*9), blockSize, blockSize );
		app.g.rect( x+(blockSize*5), y+(blockSize*10), blockSize, blockSize );
		app.g.rect( x+(blockSize*6), y+(blockSize*11), blockSize, blockSize );
		app.g.rect( x+(blockSize*7), y+(blockSize*10), blockSize, blockSize );
		app.g.rect( x+(blockSize*8), y+(blockSize*9), blockSize, blockSize );
		app.g.rect( x+(blockSize*9), y+(blockSize*8), blockSize, blockSize );
		app.g.rect( x+(blockSize*10), y+(blockSize*7), blockSize, blockSize );
		app.g.rect( x+(blockSize*11), y+(blockSize*5), blockSize, blockSize );

		app.g.fill(0);
		app.g.rect( x+(blockSize*11), y+blockSize, blockSize, blockSize );
		app.g.rect( x, y+(blockSize*2), blockSize, blockSize );
		app.g.rect( x, y+(blockSize*3), blockSize, blockSize );
		app.g.rect( x, y+(blockSize*4), blockSize, blockSize );
		app.g.rect( x, y+(blockSize*5), blockSize, blockSize );
		app.g.rect( x+blockSize, y+(blockSize*6), blockSize, blockSize );
		app.g.rect( x+blockSize, y+(blockSize*7), blockSize, blockSize );
		app.g.rect( x+(blockSize*2), y+(blockSize*8), blockSize, blockSize );
		app.g.rect( x+(blockSize*3), y+(blockSize*9), blockSize, blockSize );
		app.g.rect( x+(blockSize*4), y+(blockSize*10), blockSize, blockSize );
		app.g.rect( x+(blockSize*5), y+(blockSize*11), blockSize, blockSize );
		app.g.rect( x+(blockSize*6), y+(blockSize*12), blockSize, blockSize );
		app.g.rect( x+(blockSize*7), y+(blockSize*11), blockSize, blockSize );
		app.g.rect( x+(blockSize*8), y+(blockSize*10), blockSize, blockSize );
		app.g.rect( x+(blockSize*9), y+(blockSize*9), blockSize, blockSize );
		app.g.rect( x+(blockSize*10), y+(blockSize*8), blockSize, blockSize );
		app.g.rect( x+(blockSize*11), y+(blockSize*7), blockSize, blockSize );
		app.g.rect( x+(blockSize*11), y+(blockSize*6), blockSize, blockSize );

		app.g.rect( x+(blockSize*12), y+(blockSize*5), blockSize, blockSize );
		app.g.rect( x+(blockSize*12), y+(blockSize*4), blockSize, blockSize );
		app.g.rect( x+(blockSize*12), y+(blockSize*3), blockSize, blockSize );
		app.g.rect( x+(blockSize*12), y+(blockSize*2), blockSize, blockSize );
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

			app.g.beginShape();
			for (int i = 0; i < 2 * n; i++)
			{
				dw = w;
				dh = h;
				if (i % 2 == 1) // for odd vertices, use short radius
				{
					dw = w * proportion;
					dh = h * proportion;
				}
				app.g.vertex(cx + dw * app.cos(startAngle + angle * i),
						cy + dh * app.sin(startAngle + angle * i));
			}
			app.g.endShape(CLOSE);
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

	//////////////////////////
	//  Calculate max loop count
	
	public float getMax(float shapeSize ) {
		return ( ( app.g.width * app.g.height ) / shapeSize );
	}

	/**
	 *
	 * @param img PImage to retrieve colors from
	 * @return ArrayList of integer colors harvested from @img pixels
	 */
	public ArrayList getImgColors(PImage img)
	{
		// 	NOTE: leave FALSE, if user wants true, use getImgColors( img, true );
		return getImgColors(img, false);
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//  pull Colors out of image and return int[]
	//  http://forum.processing.org/topic/extract-Colors-from-images
	//  * UPDATE : GToLT = BOOLEAN controlling the Color comparison before
	//  adding to the ArrayList.
	
	public ArrayList getImgColors(PImage img, Boolean GToLT )
	{
		ArrayList<Integer> alColors = new ArrayList<>();

		img.loadPixels();

		int Color1, Color2;
		// TODO: what's a good way to pull DISTINCT Colors with a int[]?
		for ( int c = 0; c < img.pixels.length; c++ )
		{
			if ( alColors.size() == 0 ) {
				alColors.add( img.pixels[c] );
			}
			else
			{

				if ( ! alColors.contains( img.pixels[ c ] ) )
				{
					Color1 = alColors.get( alColors.size()-1 );
					Color2 = img.pixels[c];

					if ( GToLT ) {
						if ( Color1 > Color2 )
							alColors.add( img.pixels[ c ] );
					}
					else {
						if ( Color1 < Color2 )
							alColors.add( img.pixels[ c ] );
					}
				}
			}
		}
		return alColors;
	}



	///////////////////////////////////////////////////////
	//  Make grid of shapes filled with each Color in supplied
	//  int[]
	public void paletteGrid( ArrayList pall ) {

		float xx = 0;
		float yy = 0;
		float sz = 30;
		int tmp;
		// debug
		//text( pall.size() + " Colors ", sz, sz );

		for (Object aPall : pall) {

			app.g.noStroke();
			tmp = (int)aPall;
			app.g.fill(tmp, alf * 4);
			app.g.rect(xx, yy, sz, sz);

			if (xx < app.g.width) {
				xx += (sz * 1.25);
			} else {
				xx = 0;
				yy += (sz * 1.25);
			}
		}

		app.g.textFont( app.createFont( "Georgia", 222 ) );
		app.g.fill(app.random(alf));
		app.g.text( pall.size(), app.random( alf, app.g.width/3 ), app.random(app.g.height) );
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

		for ( int gg = 0; gg < (app.g.height+app.g.width)/2; gg++ ) {


			app.g.noStroke();
			tmp = pall.get(clridx);
			app.g.fill( tmp, alf*3 );

			app.g.rect( xx, yy, sz, sz );

			if ( xx < app.g.width ) {
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

				x = startX - app.cos(app.radians(angle)) * w;
				y = startY - app.sin(app.radians(angle)) * w;

				app.g.smooth();
				app.g.ellipse( x, y, w, h );
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

				x = startX - app.cos(app.radians(angle)) * w;
				y = startY - app.sin(app.radians(angle)) * w;

				app.g.smooth();
				app.g.ellipse( x, y, w, h );
			}
			angle++;
		}
	}

	//////////////////////////////////////////////////////////////////////////
	//  HEXAGON inspired by http://www.rdwarf.com/lerickson/hex/index.html
	public void hexagon( float startX, float startY, float shapeSize ) {

		app.g.line( startX, (float)(startY+(shapeSize*.5)), (float)(startX+(shapeSize*.25)), startY );
		app.g.line( (float)(startX+(shapeSize*.25)), startY, (float)(startX+(shapeSize*.75)), startY );
		app.g.line( (float)(startX+(shapeSize*.75)), startY, startX+(shapeSize), (float)(startY+(shapeSize*.5)) );

		app.g.line( startX+(shapeSize), (float)(startY+(shapeSize*.5)), (float)(startX+(shapeSize*.75)), startY+shapeSize );
		app.g.line( (float)(startX+(shapeSize*.75)), startY+shapeSize, (float)(startX+(shapeSize*.25)), startY+shapeSize );
		app.g.line( (float)(startX+(shapeSize*.25)), startY+shapeSize, startX, (float)(startY+(shapeSize*.5)) );
	}

	////////////////////////////////////////////////////
	//  Return a random Color from supplied palette
	
	public int getRanColor(ArrayList palette)
	{
		return (int)palette.get( (int)app.random( palette.size()-1) );
	}


	////////////////////////////////////////////////////
	//  Randomly stroke using image from Color list
	public void ranPalStroke(int[] palette)
	{
		// palette
		app.g.stroke( palette[ (int)( app.random( palette.length-1 ) ) ] , alf );
	}
	
	public void ranPalStroke(ArrayList palette)
	{
		// palette
		app.g.stroke( (int)palette.get( (int)app.random( palette.size()-1 ) ), alf );
	}
	
	public void ranPalStroke100(int[] palette)
	{
		// palette
		app.g.stroke( palette[ (int) (app.random( palette.length-1 )) ], 100 );
	}
	
	public void ranPalStroke100(ArrayList palette)
	{
		// palette
		app.g.stroke( (int)palette.get( (int)app.random( palette.size()-1 ) ), 100 );
	}
	public void ranPalFill(int[] palette)
	{
		app.g.fill( palette[ (int)(app.random( palette.length-1 )) ], alf );
	}
	
	public void ranPalFill(ArrayList palette)
	{
		// palette
		app.g.fill( (int)palette.get( (int)app.random( palette.size()-1 ) ), alf );
	}
	
	public void ranPalFill100(int[] palette)
	{
		// palette
		app.g.fill( palette[ (int) app.random( palette.length-1 ) ], 100 );
	}
	
	public void ranPalFill100(ArrayList palette)
	{
		// palette
		app.g.fill( (int)palette.get( (int)app.random( palette.size()-1 ) ), 100 );
	}

	///////////////////////////////////////////////////////////
	//  Helper to random(255) stroke
	public void randFill() {
		app.g.fill( app.random(255), app.random(255), app.random(255), alf );
	}
	
	public void randFill100() {
		app.g.fill( app.random(255), app.random(255), app.random(255), 100 );
	}
	
	public void randStroke() {
		app.g.stroke( app.random(255), app.random(255), app.random(255), alf );
	}
	
	public void randStroke100() {
		app.g.stroke( app.random(255), app.random(255), app.random(255), 100 );
	}


	
	public String getTimestamp() {
		//  Calendar now = Calendar.getInstance();
		//  return String.format("%1$ty%1$tm%1$td_%1$tH%1$tM", now);


		return ""+ app.month()+ app.day()+ app.year()+ app.hour()+ app.second()+app.millis();
	}


	//////////////////////////////////////////////////////
	//  Returns the file name that is calling this function ( minus the .pde )
	
	public String pdeName() {
		String[] pde = app.sketchPath().split( "/");
		return pde[pde.length-1];
	}



}

