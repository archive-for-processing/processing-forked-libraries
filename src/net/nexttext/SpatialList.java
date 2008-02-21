//
// Copyright (C) 2004,2005,2006 Jason Lewis
//

package net.nexttext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
 
/**
 * The SpatialList class is used to keep track of the TextObjects in a spatially
 * organised fashion in order to facililate proximity and collision queries.
 *
 * <p>At the moment, the list will only contain Glyph objects and no groups.  Group
 * behaviour based on proximity will have to be deduced from the collisions 
 * between the different glyphs.</p>
 *
 * <p>The SpatialList is updated once each frame by the Simulator, meaning that
 * as objects move during the behaviours step of the simulation the SpatialList
 * becomes unsorted.  For this reason, queries for possibly colliding objects
 * will not always be completely accurate.  However, this has not proved to be
 * problematic in practice.  </p>
 *
 * <p>The spatial list is maintained using a sweep and prune collision 
 * approximation algorithm which use dynamic AABBs (Axis-Aligned Bounding Boxes)
 * to determine proximity or overlap between two objects.</p>
 *
 * <p>This algorithm is optimised based on the assumption that objects maintain
 * their spatial coherence from frame to frame (ie: object don't travel very far
 * within the span of one frame).  As such, insertion of objects in the 
 * spatial list is costly (because the list has to be resorted), however once 
 * objects have been inserted, maintaining a sorted order is done in nearly 
 * O(n) most of the time.</p>
 *
 * <p>Add description of how to use the class</p>
 *
 * TO DO: Add/Remove function for Groups
 */
 
public class SpatialList {
	
	static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/SpatialList.java,v 1.16 2005/05/16 16:55:46 dissent Exp $";

    static final int LEFT = 0, RIGHT = 1, TOP = 2, BOTTOM = 3;

    // Each object's bounding box is projected on the X and Y axis and the
    // endpoints of the resulting interval are stored in these sorted lists.
    LinkedList xAxis = new LinkedList();
    LinkedList yAxis = new LinkedList();

    /**
     * An edge of a TextObject.
     */
    class Edge implements Comparable {

        int position;
        TextObjectGlyph to;

        Edge(TextObjectGlyph to, int position) {
            this.to = to;
            this.position = position;
        }

        double getValue() {
            if (position == LEFT)
                return to.getBounds().getMinX();
            if (position == RIGHT)
                return to.getBounds().getMaxX();
            if (position == TOP)
                return to.getBounds().getMinY();
            if (position == BOTTOM)
                return to.getBounds().getMaxY();
            throw new RuntimeException("Invalid Position: " + position);
        }

        public int compareTo(Object o) {
            return Double.compare(getValue(), ((Edge) o).getValue());
        }
    }

	// Each object has an entry (the key is the object itself) in these data 
	// structure leading to a HashSet of objects it collides with on each axis.
	HashMap xCollisions = new HashMap();
	HashMap yCollisions = new HashMap();
	
	// These two values are used to maintain an average number of collision
	// tests for each frame.  They are mainly provide statistical information
	// to evaluate the algorithm's performance.
	int tests = 0;
	int avrg = 0;
	
	/**
	 * Sorts the X and Y axis interval lists. 
	 */
	public void update() {
		// sort each Edge list
		sort( xAxis, 0 );
 		sort( yAxis, 1 );
		// calculate stats
		avrg += tests;
		avrg /= 2;
		tests = 0;
	}
	
	/**
     * Get position in the xAxis and yAxis lists for each edge of a TextObject.
     *
     * <p>An array of 4 elements is returned, each of which is an index within
     * the axes (xAxis and yAxis) where the corresponding edge should be
     * inserted.  The returned array is indexed using the static finals 'LEFT',
     * 'RIGHT', 'TOP', and 'BOTTOM'.  </p>
     *
     * <p>The returned indices are determined from the current state of the
     * list.  When both edges have been added to the list in their correct
     * places, the positions of the RIGHT, and BOTTOM edges will be off by one
     * from the returned values, because the LEFT and TOP edges have also been
     * inserted in the lists in a lower position.  </p>
	 */
	private int[] getPosition( TextObjectGlyph to ) {
		int[] position = {0,0,0,0};	// left, right, top, and bottom position
		
		// not an empty list for xAxis
		if (xAxis.size() != 0)
		{
			// binary search for the position
            position[LEFT] = binarySearch(xAxis, to.getBounds().getMinX());
            position[RIGHT] = binarySearch(xAxis, to.getBounds().getMaxX());
		}
		
		// not an empty list for yAxis
		if (yAxis.size() != 0)
		{
            position[TOP] = binarySearch(yAxis, to.getBounds().getMinY());
            position[BOTTOM] = binarySearch(yAxis, to.getBounds().getMaxY());
		}
		
		return position;
	}
	
	/**
     * Determine the index in the provided list where an Edge with the given
     * value should be placed.
	 */
    private int binarySearch(LinkedList list, double value) {
		int lower = 0, middle, upper = list.size() - 1;
		
		while ( upper >= lower )
		{
			middle = ( upper + lower ) / 2;
            int result = Double.compare(value, ((Edge)list.get(middle)).getValue());
			if ( result > 0 )
				lower = middle + 1;
			else if ( result < 0 )
				upper = middle - 1;
			else return middle;
		}
		
		return lower;
	}
	
	/**
	 * Adds a single TextObjectGlyph to the spatial list
	 */
	public void add( TextObjectGlyph to ) {
		
	    if (to.toString().equals(" ")) {
	        // dont add spaces..
	        return;
	    }
		
        // Add the object's 4 edges to the Axis lists, and calculate its
        // collisions.  In order to maintain the xCollisions and yCollisions
        // data structures, sort() is used to get the edges to the right place
        // in the lists.  One edge from each axis is added to the correct place
        // in the list, the other is added to an end of the list so that the
        // new edges are out of order (eg. RIGHT before LEFT in the list).
        // This guarantees that sort() will be forced to swap edges with all
        // overlapping objects.

		int[] position = getPosition( to );
		
		// left point is closer to the end of the list
		if (position[RIGHT] >= xAxis.size() - position[LEFT]) {
            xAxis.add(position[RIGHT], new Edge(to, RIGHT));
            xAxis.add(new Edge(to, LEFT));
		}
		else {	// right point is closer to the beginning of the list
            xAxis.add(position[LEFT], new Edge(to, LEFT));
            xAxis.addFirst(new Edge(to, RIGHT));
		}
		
		// top point is closer to the end of the list
		if (position[BOTTOM] >= yAxis.size() - position[TOP]) {
            yAxis.add(position[BOTTOM], new Edge(to, BOTTOM));
            yAxis.add(new Edge(to, TOP));
		}
		else {	// bottom point is closer to the end of the list
            yAxis.add(position[TOP], new Edge(to, TOP));
            yAxis.addFirst(new Edge(to, BOTTOM));
		}
		
		// add an entry in each hash table for that object
		xCollisions.put( to, new HashSet() );
		yCollisions.put( to, new HashSet() );
		
		// re-sort the lists.
		sort( xAxis, 0 );
		sort( yAxis, 1 );
	}
	
	/**
	 * Removes an object from the spatial list
	 */
	public void remove( TextObjectGlyph to ) {

        Iterator ei = xAxis.iterator();
        while (ei.hasNext()) { if (((Edge)ei.next()).to == to) ei.remove(); }
        ei = yAxis.iterator();
        while (ei.hasNext()) { if (((Edge)ei.next()).to == to) ei.remove(); }
		
		// see with which other objects this object used to collide
		HashSet xcol = (HashSet)xCollisions.get(to);
		HashSet ycol = (HashSet)yCollisions.get(to);
		
		// remove all references to this object from any colliding objects
		if ( xcol != null ) {
			for ( Iterator i = xcol.iterator(); i.hasNext(); ) {
				HashSet temp = ((HashSet)xCollisions.get( i.next() ));
				temp.remove(to);
			}
		}
		
		if ( ycol != null ) {
			for ( Iterator i = ycol.iterator(); i.hasNext(); ) {
				((HashSet)yCollisions.get( i.next() )).remove(to);
			}
		}
		
		// finally, remove the object from the collisions lists
		xCollisions.remove(to);
		yCollisions.remove(to);	
	}
	
	/**
	 * Adds all the glyphs part of a TextObjectGroup to the spatial list.
	 */
	public void add( TextObjectGroup tog ) {
		
		TextObjectIterator toi = new TextObjectIterator( tog );
		
		while ( toi.hasNext() ) {
			TextObject to = toi.next();
			if ( to instanceof TextObjectGlyph ) {
				add( (TextObjectGlyph)to );	
			}	
		}
	}
	
	/**
	 * Adds a TextObject to the spatial list.  Use this method to avoid casting
	 * the object as group or a glyph
	 */
	public void add( TextObject to ) {
		if ( to instanceof TextObjectGlyph ) {
			add( (TextObjectGlyph)to );
		}
		if ( to instanceof TextObjectGroup ) {
			add( (TextObjectGroup)to );	
		}	
	}
	
	/**
	 * Removes a TextObject to the spatial list.  Use this method to avoid casting
	 * the object as group or a glyph
	 */
	public void remove( TextObject to ) {
		if ( to instanceof TextObjectGlyph ) {
			remove( (TextObjectGlyph)to );
		}
		if ( to instanceof TextObjectGroup ) {
			remove( (TextObjectGroup)to );	
		}	
	}
	 
	
	/**
	 * Removes all the glyphs part of a TextObjectGroup from the spatial list
	 */
	public void remove( TextObjectGroup tog ) {
		
		TextObjectIterator toi = new TextObjectIterator( tog );
		
		while ( toi.hasNext() ) {
			TextObject to = toi.next();
			if ( to instanceof TextObjectGlyph ) {
				remove( (TextObjectGlyph)to );	
			}	
		}
	}
	
	/**
	 * Redirects to the proper implementation of getPotentialCollisions based
	 * on type (TextObjectGlyph or TextObjectGroup)
	 */
	public HashSet getPotentialCollisions( TextObject to ) {
		
		if ( to instanceof TextObjectGlyph ) {
			return getPotentialCollisions( (TextObjectGlyph)to );
		}
		if ( to instanceof TextObjectGroup ) {
			
			return getPotentialCollisions( (TextObjectGroup)to );	
		}
		
		// anything else return null.
		return null;	
	}
	
 	/**
	 * Given a TextObjectGlyph, get a list of objects which's bounding box
	 * are overlapping.  It returns an empty hashset if there is none. 
	 *
	 * @param to  A TextObjectGlyph to test for collisions
	 */
	public HashSet getPotentialCollisions( TextObjectGlyph to ) {
		
		// create a new set to store potential collisions
		HashSet collisions = new HashSet();
		
		HashSet xCol = (HashSet)xCollisions.get(to);
		HashSet yCol = (HashSet)yCollisions.get(to);
		
		// if the HashSets are non-existant, it means we queried an object which
		// was not part of the spatial list
		if (xCol == null || yCol == null) {
            String msg = "Collisions query for object not in SpatialList: " + to;
			throw new ObjectNotFoundException(msg);
		}
			
		// if the collision sets are empty for either axis, then the object is
		// in the list but cannot be colliding with any other object
		if (xCol.size() == 0 || yCol.size() == 0) 
			return collisions;
		
		// for every element in xCol, see if that element is yCol.  it is, then
		// the boxes are overlapping
		Iterator i = xCol.iterator();
		while (i.hasNext()) {	
			TextObjectGlyph someTo = (TextObjectGlyph)i.next();
			if (yCol.contains(someTo)) {
				collisions.add(someTo);	
			}	
		}	
		
		// otherwise returns the list of object's colliding for this query
		return collisions;
	}
	
	/**
	 * Given a TextObjectGroup, find all the glyphs which's bounding boxes are 
	 * overlapping with any of the given group's glyphs.  Returns an empty set if 
	 * no objects are colliding with the group.
	 */
	public HashSet getPotentialCollisions( TextObjectGroup tog ) {
		
		HashSet collisions = new HashSet();
		
		TextObjectIterator toi = new TextObjectIterator( tog ) ;
		
		while ( toi.hasNext() ) {	
			TextObject to = toi.next();
			if ( to instanceof TextObjectGlyph ) {
				// add all collisions for this glyph to the collider's set
				collisions.addAll( getPotentialCollisions( (TextObjectGlyph)to ) );
			}	
		}
		return collisions;		
	}
		
	/**
	 * Returns the average number of collision tests performed by the sorting
	 * function
	 */
	public int getNumCollisionTests() {
		return avrg;
	}

	///////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	
	/**
	 * Sort objects  in a list using  insertion sort.
	 *
	 * Normally insertion sort has O(n2) running time, however because of 
	 * spatial coherence we can expect the lists to be almost sorted, resulting
	 * in an expected O(n) running time.
	 *
	 * Every time a swap is peformed, update the "overlap" status for the two
	 * objects invovled is updated.
	 * 
	 * @param list a list of edges to sort
	 * @param axis 0-X axis, 1-Y axis
	 */
	
    private void sort(LinkedList list, int axis) {
	
		int n = list.size();
		int j = 0;
		for (int i=1; i < n; i++) {
			// check from the first element to the end of the sorted section
			j = i-1;
            Edge a = (Edge)list.get(i);
		 	// bump down the list until we find the correct place to 
			// insert Edge a
			while( j >= 0 && ( a.compareTo(list.get(j)) < 0 ) ) {	
		 		swap( list, j+1, j, axis );
				j--;
				// ## debug count the number of swaps for each sort
				tests++;
			}				
		}
	}
	
	/**
	 * swaps two elements i and j in the interval list.  updates their overlap 
	 * status to reflect the new changes
	 */
    private void swap(LinkedList list, int i, int j, int axis) {
	
	 	// swap elements at i and j
		list.set( i, (list.set(j, list.get(i))) );
		
		// based on the two edges, find out if we should add (or remove) an 
		// overlap for these two objects
		
        TextObjectGlyph glyphA = ((Edge)list.get(i)).to;
        TextObjectGlyph glyphB = ((Edge)list.get(j)).to;
	 	
	 	// BUGFIX:
	 	// This check was added to prevent detecting an overlap between edges
	 	// belonging to the same object.
	 	// Failing to do this could result in an object colliding with itself,
	 	// and subsequently into concurrent modifications in the xColl and yColl
	 	// HashSets when trying to remove the object from the spatial list.
	 	if ( glyphA.parent == glyphB.parent ) {
	 		// do nothing if the edges belong to the same object.
	 		return;
	 	}
	 	
	 	double s1, e1;   // start-endpoints
	 	double s2, e2;	 
	 	
	 	// 
	 	// Ugly code follows:
	 	//
	 	
	 	// Short of a more elegant solutions, if/else statements are used to
	 	// determine which axis we are sorting on, since different values must
	 	// be taken into account ( left/right edges or top/bottom) as well as
	 	// insertion into different HashMaps (xCollisions/yCollisions).
	 
	 	if (axis == 0) {
            s1 = glyphA.getBounds().getMinX();
            e1 = glyphA.getBounds().getMaxX();
            s2 = glyphB.getBounds().getMinX();
            e2 = glyphB.getBounds().getMaxX();
	 	}
	 	else {
            s1 = glyphA.getBounds().getMinY();
            e1 = glyphA.getBounds().getMaxY();
            s2 = glyphB.getBounds().getMinY();
            e2 = glyphB.getBounds().getMaxY();
	 	}
	 	
		if ( intervalOverlap( s1, e1, s2, e2 ) ) {
			
			if ( axis == 0 ) {			  	
				((HashSet)xCollisions.get( glyphA )).add( glyphB );	
				((HashSet)xCollisions.get( glyphB )).add( glyphA );	
			}
			else {
				((HashSet)yCollisions.get( glyphA )).add( glyphB );	
				((HashSet)yCollisions.get( glyphB )).add( glyphA );	
			}
		}
		else {
			if ( axis == 0 ) {
				((HashSet)xCollisions.get( glyphA )).remove( glyphB );	
				((HashSet)xCollisions.get( glyphB )).remove( glyphA );
			}
			else {
				((HashSet)yCollisions.get( glyphA )).remove( glyphB );	
				((HashSet)yCollisions.get( glyphB )).remove( glyphA );
			}
		}
	}
	
	/**
	 * Determine if intervals [s1, e1] and [s2, e2] overlap or not
	 */
	private boolean intervalOverlap( double s1, double e1, double s2, double e2 ) {
		if ((e2 > s1) && (e1 > s2))	return true;
		return false;
	}
}
