/*
  This file is part of the NextText project.
  http://www.nexttext.net/

  Copyright (c) 2004-08 Obx Labs / Jason Lewis

  NextText is free software: you can redistribute it and/or modify it under
  the terms of the GNU General Public License as published by the Free Software 
  Foundation, either version 2 of the License, or (at your option) any later 
  version.

  NextText is distributed in the hope that it will be useful, but WITHOUT ANY
  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
  A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with 
  NextText.  If not, see <http://www.gnu.org/licenses/>.
*/

package net.nexttext;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import net.nexttext.property.Property;
import net.nexttext.property.PropertyChangeListener;
import net.nexttext.property.Vector3Property;
import net.nexttext.property.Vector3PropertyList;
import net.nexttext.property.ColorProperty;

import processing.core.PFont;

/**
 * TextObjectGlyph represents an individual glyph and its vectorial outline in 
 * a data structure that is understood by the Renderer.
 *
 * <p>A Glyph's outline is represented by a list of control points forming a hull
 * (vertices) around the glyph.  The outline can be drawn using successive quads 
 * where anchors are being interpolated from each set of 3 adjacent control 
 * points. This allows for continuous curves even when the control points 
 * are being displaced.  </p>
 *
 * <p>Glyphs are built out of one or more contours.  Contours are represented
 * by a list of hull points forming continuous quadratic curves.  The
 * coordinates for each of these hull points are stored in the Control Points list.
 * Because some glyphs are represented by more than one shape (holes, dot on
 * the i, etc.), each shape is defined as an array of indices into the Control Points
 * list.  These shape arrays are stored in a list called Contours. This list
 * should not be modified to distort the glyph.  Instead, the behaviours will
 * alter the Control Points directly without affecting the order in which they are
 * drawn.  </p>
 *
 * <p>TextObjectGlyph objects also have two "special" properties, glyph and font,
 * which are not accessed using the standard getProperty mechanism.  The reason 
 * for this is that the list of vertices and contours forming a glyph is 
 * dependent on them, and must be rebuilt if these properties are changed.
 * Therefore, we provided specific get/set methods to access and modify them. 
 * 
 * <p>The glyph's ColorProperty is inherited by default. </p>
 *
 * @see TextObject
 */
/* $Id$ */ 
public class TextObjectGlyph extends TextObject {
	
    // The FontRenderContext is necessary for determining glyph outlines, but is
    // never modified.
    static FontRenderContext frc = new FontRenderContext(null, false, false);

	/** The character for this object.  This is a special property that is 
	 * handled through its own get/set methods */
	protected String glyph;
	protected PFont pfont;
	protected Font font;
	
	/** A vector containing int[] arrays with indices to the Control Points property
	 list.  These contours define the shape of the glyph */
	public Vector 		contours;   	
   	 
   	/** 
   	 * This rectangle stores the glyph's "logical bounds", including proper
   	 * character spacing considering the glyph is in an undeformed state.
   	 */
   	private Rectangle2D logicalBounds;
   	
    // Track glyph deformations to allow for faster rendering.
    private boolean deformed = false;

    /**
     * A flag to indicate if this glyph has been deformed.
     *
     * <p>False means that the glyph and font information is sufficient to draw
     * the glyph.  True means that the "Control Points" property is needed to
     * define the shape of the glyph.  </p>
     */
    public boolean isDeformed() { return deformed; }
    public void setDeformed(boolean df) { deformed = df; }

    /**
     * This object can be used by Renderers to cache information about
     * TextObjectGlyphs.  If the glyph is deformed this cache object will be
     * reset to null.
     */
    public Object rendererCache = null;

	/**
	 * Default constructor.  Position is set to (0,0,0) by default, and color
	 * is inherited from the parent.
	 *
	 * @param glyph		A one character-long string 
	 * @param pfont     A processing.core.PFont object 
	 */
	public TextObjectGlyph(String glyph, PFont pfont) {
 		this(glyph, pfont, new Vector3(0,0,0));
	}
	
	/**
	 * Constructor with a specific position.
	 *
	 * @param glyph		A one character-long string 
	 * @param pfont     A processing.core.PFont object 
	 * @param position  A Vector3 representing the glyph's relative position
	 */
    public TextObjectGlyph(String glyph, PFont pfont, Vector3 position) {
        this(glyph, pfont, new HashMap<String, Property>(0), position);
    }

	/**
	 * Constructor with extra properties and a specific position.
	 *
	 * @param glyph		A one character-long string 
	 * @param pfont     A processing.core.PFont object 
	 * @param pos       A Vector3 representing the glyph's relative position
	 * @param props     Initial properties for the glyph.
	 */
	public TextObjectGlyph(String glyph, PFont pfont, Map<String, Property> props, Vector3 pos) {
        super(props, pos);

	 	this.glyph 	= glyph;
		this.pfont 	= pfont;
		font = Book.loadFontFromPFont(pfont);

        properties.init("Control Points", new Vector3PropertyList());
        
        glyphChanged();

        // When the control points change, the renderer cache is no longer
        // valid, and the glyph has been deformed.
        getControlPoints().addChangeListener(new PropertyChangeListener() {
                public void propertyChanged(Property propertyThatChanged) {
                    glyphDeformed();
                }
            });
	}
	
	/**
	 *  Copy Constructor.
	 * 
	 *  @param glyph	A glyph to copy
	 */
	public TextObjectGlyph(TextObjectGlyph glyph) {
		this(glyph.toString(), glyph.getFont(), glyph.properties.properties, glyph.getPosition().get());
	}
	
    /**
     * Get the greatest number of layers between this TextObject and the leaves
     * of the tree.
     */
    public int getHeight() { return 0; }

    /** Get the glyph of this object as a string of length 1. */
    public String getGlyph() { return glyph; }
	
	/**
	 * Rebuild the internal representation of the glyph based on the specified
	 * character.  This operation is rather costly, so it should be used 
	 * accordingly.
	 *
	 * NOTE: While a glyph is represented by a single character, the parameter
	 * here is of type string for two reasons:
	 *
	 * 1. String is the type used by the Font class in order to generate 
	 * shapes.
	 * 2. We have been un-officially allowing more than one character per glyph
	 * for development purposes.  So far it does not seem to cause any problem,
	 * however the proper way to represent words is to use the TextObjectBuilder.
	 */
	public void setGlyph( String glyph ) {
		
		// XXXBUG: should we even allow setGlyph?... Issues with character spacing are
		// coming to mind.   Maybe notify the parent so it can layout other
		// characters accordingly.
		this.glyph = glyph;

        glyphChanged();
	}
	
	/**
	 * Rebuild the internal representation of the glyph according to the 
	 * properties of the newly specified Font object.  This operation is rather
	 * costly, so it should be used accordingly.
	 */
	public void setFont(PFont pfont) {
        // Ideally setFont would attempt to preserve deformations.  However
        // that's a lot of work, so we have deferred it.
		this.pfont = pfont;
		font = Book.loadFontFromPFont(pfont);
        glyphChanged();
	}
	
	/**
	 * Returns this TextObjectGlyph's font attribute.
	 */
	public PFont getFont() {
		return this.pfont;
	}
	
	/**
	 * Returns this glyph's logical bounds information used for spacing.
	 */
	public Rectangle2D getLogicalBounds() {
	    return logicalBounds;
	}

    /**
     * Convenience accessor for the control points.
     */
    public Vector3PropertyList getControlPoints() {
        return (Vector3PropertyList) getProperty("Control Points");
    }
	
	////////////////////////////////////////////////////////////////////////////
	// protected methods

    /**
     * Set the flag colour of a glyph.
     * @param newColProp the color property that was changed
     */
    protected void colourFlagChanged(ColorProperty newColProp) {
        if (newColProp.getName() == "StrokeColor")
            stroked = newColProp.get().getAlpha()>0;
        else if (newColProp.getName() =="Color")
            filled = newColProp.get().getAlpha()>0;
    }
    
    /**
     * Reset any internally cached information that becomes invalid because the
     * glyph has changed.
     */
    protected void glyphChanged() {
        buildControlPoints();
        rendererCache = null;
        invalidateLocalBoundingPolygon();
    }

    /**
     * Reset any internally cached information that becomes invalid because the
     * glyph has deformed.
     */
    protected void glyphDeformed() {
        deformed = true;
        rendererCache = null;
        invalidateLocalBoundingPolygon();
    }

	/**
	 * This method uses the Java AWT Font methods to create a vector outline of 
	 * the glyph.
	 */
	protected void buildControlPoints() {
	
		// create a Vector3PropertyList object to store the vertices
        Vector3PropertyList vertices = getControlPoints();
		
		// clear previously stored vertices
		vertices.clear();

		// create a Vector to store the list of contours
		this.contours = new Vector();
		
		// vertice array index (used to associate more than one contour point
		// with the same vertex)
		int vertexIndex = 0;
	
		// a temporary list to store vertex indices for each contour (once 
		// the contour is closed, this Vector will be converted to an array
		// and stored into the Contour list.
		Vector<Integer> tmpContour = new Vector<Integer>();
				
		// used to receive the list of points from PathIterator.currentSegment()
		double 	points[] = new double[6];  
		// used to receive the segment type from PathIterator.currentSegment()
		// segmentType can be SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CLOSE
		int 	segmentType	= 0; 
		// remember the previously identified type
		int 	previousType= 0;
		// used to remember the previously calculated Anchor and ControlPoint.
		// for a more detailed description of what an anchor and controlpoint are,
		// see the architecture document.
		Vector3	lastAnchor = new Vector3();
		Vector3 lastControlPoint = new Vector3();
		// in some cases the algorithm needs to backtrack and insert an point in
		// the contour lists a few positions behind.  This variable is used to
		// remember such a position in the array.
		int		anchorInsertionPoint = 0;
		
		// get the Shape for this glyph
		GlyphVector gv = font.createGlyphVector( frc, this.glyph );
		Shape outline = gv.getOutline();
		
		// store the glyph's logical bounds information
		logicalBounds = gv.getLogicalBounds();
		
	 	// no flattening done at the moment, just iterate through all the 
	 	// segments of the outline.  For more details see Javadoc for
	 	// java.awt.geom.PathIterator
		PathIterator pit = outline.getPathIterator(null);
	
		while ( !pit.isDone() ) {
			
		 	segmentType = pit.currentSegment( points ); 
		 	 	
			switch( segmentType ) {
			
				case PathIterator.SEG_MOVETO:
					
					// start a new tmpContour vector
					tmpContour = new Vector<Integer>();
				 	// get the starting point for this contour	
					Vector3 startingPoint = new Vector3( points[0], points[1] );
					// store the point in the list of vertices
					vertices.add( new Vector3Property( startingPoint) );
					// store this point in the current tmpContour and increment
					// the vertices index
					tmpContour.add( vertexIndex );
					vertexIndex++;
					// update temporary variables used for backtracking
					lastAnchor = startingPoint;
					lastControlPoint = startingPoint;
					previousType = segmentType;
					anchorInsertionPoint = 0;
					break;
					
				case PathIterator.SEG_LINETO:
				
					// lines are get converted to quads.
				 
					// since a line begins at lastAnchor, add this value as
					// a control point for the contour.  Note that we add
					// the value twice, but that it refers to the same vertex.
					// The reason behind this is to create sharp edges when
					// required.
					
					// also, only add the first anchor if the previous segment
					// was NOT a line (to avoid adding this control point four
					// times)
					if ( previousType != PathIterator.SEG_LINETO ) {
						vertices.add( new Vector3Property(lastAnchor) );
						tmpContour.add( vertexIndex );
						tmpContour.add( vertexIndex );
						vertexIndex++;	
					}
				 
					// then, we must find the middle of the line and use it as 
					// control point in order to allow smooth deformations
					Vector3 endPoint = new Vector3( points[0], points[1] );
					Vector3 midPoint = new Vector3( (lastAnchor.x + endPoint.x)/2, 
								 			  		(lastAnchor.y + endPoint.y)/2  );
					vertices.add( new Vector3Property( midPoint) );
					tmpContour.add( vertexIndex );
					vertexIndex++;
					
					// finally, we must add the endPoint twice to the contour
					// to preserve sharp corners
					vertices.add( new Vector3Property( endPoint) );
					tmpContour.add( vertexIndex );
					tmpContour.add( vertexIndex );
					vertexIndex++;
				 	
				 	// update variables used for backtracking
					lastAnchor = endPoint;
					lastControlPoint = midPoint;
					previousType = segmentType;
					break;
					
				case PathIterator.SEG_QUADTO:
					
					Vector3 controlPoint = new Vector3( points[0], points[1] );
					Vector3 anchorPoint = new Vector3( points[2], points[3] );
					
					// first, we must handle the case where two quads form 
					// sharp corners.   if this is the case, then the anchor
					// for that quad must added to the list of contours (twice),
					// just like for lines.
					//
					// in order to figure out sharp corners, we look at the line
					// formed by the last two control points and the last anchor 
					// point.  If the distance between them is big enough, then
					// there should be a sharp edge.
					//
					// to find out if the anchor point is "far enough", I've 
					// empirically determined that the distance/font size ratio
					// should be smaller than 21 in order to consider a quad
					// anchor as part of a sharp edge
					
					if ( previousType != PathIterator.SEG_LINETO ) {
						Vector3 mid = new Vector3( (controlPoint.x + 
													lastControlPoint.x)/2, 
											   	   (controlPoint.y + 
											   	    lastControlPoint.y)/2 );
						double dx, dy, dist;
						dx = mid.x - lastAnchor.x;
						dy = mid.y - lastAnchor.y;
						dist = Math.sqrt((dx*dx) + (dy*dy));
						// TODO make sure this getSize() returns the correct size when the PFont size and the actual size don't match
				 	 	if ( (font.getSize()/dist) < 21 ) {
					  		// if this is the case, then the lastAnchor has to be
					 		// added as a control point.  However it has to be
					 		// inserted in the correct order in the list.
					  		vertices.add( new Vector3Property( lastAnchor) );
					   		tmpContour.add( anchorInsertionPoint, vertexIndex );
					 		tmpContour.add( anchorInsertionPoint, vertexIndex );
					 		vertexIndex++;
					 	} 
					}
					
					// because the calculations above can only be determined
					// by looking at the lastAnchor point, remember where those
					// should be inserted in the array.
					anchorInsertionPoint = tmpContour.size()+1;
					
					// Otherwise, we are only interested in storing the control 
					// point for the quad.  The actual anchor points will be 
					// interpolated at runtime to preserve curve continuity.
					vertices.add( new Vector3Property( controlPoint) );
					tmpContour.add( vertexIndex );
					vertexIndex++;
						
					// update temporary variables used for backtracking					
					lastAnchor = anchorPoint;
					lastControlPoint = controlPoint;
					previousType = segmentType;
					break;	
				 
				case PathIterator.SEG_CLOSE:
					
					// A SEG_CLOSE signifies the end of a contour, therefore
					// convert tmpContour into a new array of correct size
					int contour[] = new int[tmpContour.size()];
					Iterator<Integer> it = tmpContour.iterator();
					int i = 0;
					while( it.hasNext() ) {
						contour[i] = it.next();
						i++;	
					}
					
					// add the newly created contour array to the contour list
					contours.add(contour);
					break;
					
				case PathIterator.SEG_CUBICTO:
				
                    getBook().log("TextObjectGlyph: cubic segment unsupported");
					break;
						
			} // end switch	
	
			pit.next();
		} // end while	
	 }
	
    /**
     * See TextObject's getLocalBoundingPolygon() description for details.  
     * 
     * <p>Do not modify the returned Polygon, because it may be cached. </p>
     *
     * <p>XXXBUG this method always returns a rectangle until we write an
     * algorithm to calculate the convex hull of the set of control points.</p>
     * 
     * @see net.nexttext.TextObject#getLocalBoundingPolygon()   
     */
    public synchronized Polygon getLocalBoundingPolygon() {

        if (localBoundingPolygonValidToFrame >= getFrameCount()) {
            return localBoundingPolygon;
        }
        localBoundingPolygonValidToFrame = Long.MAX_VALUE;

        // Find the smallest box enclosing all the object's Control Points by 
        // computing the min/max
        
        double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;

        // Spaces are calculated differently because they don't have control
        // points in the same way as other glyphs.
        if ( getGlyph().equals(" ") ) {
            Rectangle2D sb = Book.loadFontFromPFont(pfont).getStringBounds(" ", frc);
            minX = sb.getMinX();
            minY = sb.getMinY();
            maxX = sb.getMaxX();
            maxY = sb.getMaxY();

        } else {
            Vector3PropertyList vertices = getControlPoints();

            for ( Iterator<Vector3Property> i = vertices.iterator(); i.hasNext(); ) {
                Vector3Property vertex = i.next();
                minX = Math.min(vertex.getX(), minX);
                minY = Math.min(vertex.getY(), minY);
                maxX = Math.max(vertex.getX(), maxX);
                maxY = Math.max(vertex.getY(), maxY);
            }
        }
        
        // return the box as a polygon object
        
		int[] x = new int[] { (int)minX, (int)maxX, (int)maxX, (int)minX };
		int[] y = new int[] { (int)minY, (int)minY, (int)maxY, (int)maxY };

        localBoundingPolygon = new Polygon( x, y, 4 );
        return localBoundingPolygon;
    }
    
    public String toString() { 
        return getGlyph();        
    }
}
