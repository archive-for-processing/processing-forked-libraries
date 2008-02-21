//
//Copyright 2004 Jason Lewis
//

package net.nexttext.behaviour.physics;

import java.awt.Polygon;

import net.nexttext.TextObject;
import net.nexttext.Vector3;
import net.nexttext.Vector3ArithmeticException;
import net.nexttext.property.Vector3Property;

/**
 * This action performs collision response on two objects by moving them apart
 * and reflecs their velocity.
 */
public class Collide extends PhysicsAction {
    
    static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/behaviour/physics/Collide.java,v 1.4 2005/05/26 14:07:42 david_bo Exp $";
    
    private double elasticity;
    private double absorbEnergy;
    
    /**
     * @param elasticity The elasticity of collision
     * @param absorbEnergy Objects absorb energy from each other if non zero
     */
    public Collide( double elasticity, double absorbEnergy ) {        
        this.elasticity = elasticity;
        this.absorbEnergy = absorbEnergy;
    }
           
    /**
     * Performs collision response on toA and toB by pushing them apart an 
     * reflecting their velocity.
     * 
     * <p>Objects will not collide if one is a child of the other. </p>
     */    
    public ActionResult behave(TextObject toA, TextObject toB) {
        
        // make sure one is not the parent of the other
    	if ( toA.getParent() == toB || toB.getParent() == toA ) {
	 	    return new ActionResult(false, false, false);
		}
    	
    	// ** COLLISION DETECTION ** 
    	
    	// the polygons used in the collision response
    	Polygon A = toA.getBoundingPolygon();
    	Polygon B = toB.getBoundingPolygon();
        	
    	
    	// get the minimum translation vector between A and B.  
        Vector3 mtd = getVectorToSeparateAFromB(A, B);
    	  
        if ( mtd.isZero() ) {
            // objects do not interesect, so do nothing.
	 	    return new ActionResult(false, false, false);
        }
        
        // ** COLLISION RESPONSE **
       
        // see if B has a velocity property
        Vector3Property velPropB = getVelocity(toB);
        
        // if it doesnt, then B is not affected by a Physics action.  In this
        // case, treat it as an unmmovable object.
        if ( velPropB == null ) {
            // move A the full distance
            translate( getPosition(toA), mtd );
            // reflect the velocity of A using the inversed mtd
            mtd.scalar(-1);
            reflectVelocity( getVelocity(toA), mtd );
        }
        // otherwise, have each object move half the distance and reflect 
        // their velocities.
        else {
            // have each object move half the distance
            mtd.scalar(0.5);  
            translate( getPosition(toA), mtd );
            
            // to reflect velocity, we need the inverse mtd, so reflect
            // B's velocity now
            reflectVelocity( velPropB, mtd );
            
            // inverse the mtd for toB          
            mtd.scalar(-1);
            translate( getPosition(toB), mtd );
            
            // now that mtd has been inversed, we can reflect A's velocity.
            reflectVelocity( getVelocity(toA), mtd );            
        }           
        return new ActionResult(false, false, true);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Helper methods
    
    /**
     * Moves translates a Position property by the given distance
     */
    protected void translate( Vector3Property posProp, Vector3 distance ) {       
        Vector3 pos = posProp.get();
        pos.add( distance );
        posProp.set( pos );
    }
    
    /**
     * Reflects a Velocity property using the Minimum Translation Distance to
     * calculate the plane of collision.
     */
    protected void reflectVelocity( Vector3Property velProp, Vector3 mtd ) {
           	
        try {
            // the unit normal to the plane of collision corresponds to the 
            // normalized minimum translation distance.
            Vector3 N = new Vector3( mtd.x*-1, mtd.y*-1 );
            N.normalize();
        
	        // get the velocity vector	        
	        Vector3 V = velProp.get();
	    	
	        // Only reflect the velocity if the object is travelling
	        // into the collision.
	        if ((N.theta(V)) > (Math.PI / 2)) {
	            
	            // reflected velocity formula:
	            // V - ( (1 + elasticity) * N.V )N
	            double dot = N.dot(V);
	            dot *= 1 + elasticity;
	            N.scalar(dot);
	            V.sub(N);
	            
	            // finally, update velocity
	            velProp.set(V);
	        }
    	}
        catch ( Vector3ArithmeticException v3ae ) {
            // This means mtd was zero, so just ignore it.
            // Technically this shouldnt happen, we already returned if mtd == 0
            return;
        }
    }    
    
    /**
     * Returns the smallest vector which pushes A away from B.
     *
     * <p>Even though odds are we'll be dealing mostly with boxes, this method 
     * supports arbitrary convex polygons. In order to do so, we need to test
     * with the axis perpenticular to every edge of each polygon. </p>
     *
     * <p>Note that it returns a zero vector if A and B do not intersect. </p>
     */
    protected Vector3 getVectorToSeparateAFromB( Polygon A, Polygon B ) {
    	
		Vector3 fromA = getVectorToSeparateAFromBInner( A, B );
		Vector3 fromB = getVectorToSeparateAFromBInner( B, A );
		
		// change vector fromB to push A
		fromB.scalar(-1);
		
		// XXXBug:
		// There seems to be a bug in the getVectorToSeperateAFromBInner 
		// (see Bugzilla #55)
		// The code below is a temporary patch to catch those cases and return 
		// the non-zero vector out of fromA and fromB.  
		// This prevents objects from overlapping, however the collision 
		// response is often excessive and doesn't look right, due to the
		// fact that vector returned may not be the smallest push
		// vector.
		if ( fromA.length() == 0 && fromB.length() != 0 ) {
		    return fromB;
		}		
		if ( fromB.length() == 0 && fromA.length() != 0 ) {
		    return fromA;
		}
		   
	    return fromA.length() < fromB.length() ? fromA : fromB ;
    }
    
    /**
     * Returns the smallest vector which pushes A away from B and is perpendicular to
     * one of A's edges.
     */
    protected Vector3 getVectorToSeparateAFromBInner( Polygon A, Polygon B ) {

	    Vector3 smallestVector = null;
	    	    
	    int numEdgesA = A.npoints;
    	for ( int i=0; i <= numEdgesA ; i++) {
    		// find the vector formed by each edge by going from the current 
    		// vertex to the next.
    		int vX = A.xpoints[i%numEdgesA] - A.xpoints[ (i+1)%numEdgesA ];
    		int vY = A.ypoints[i%numEdgesA] - A.ypoints[ (i+1)%numEdgesA ];
    		// find the axis perpendicular to this edge
    		Vector3 axis = new Vector3( -vY, vX, 0 );
            try {
                axis.normalize();
            } catch (Vector3ArithmeticException v3ae) {
                // Ignore zero-length edges
                continue;
            }
     		
     		// see if this axis separates the polygons.  if it doesnt, this
     		// method will return a vector representing the intersection
     		// projected on that specific axis
     		Vector3 pushVector = AxisSeparatesPolygons( axis, A, B );
     		
     		if ( (smallestVector == null) || 
     			 (pushVector.length() < smallestVector.length()) ) {
	     		smallestVector = pushVector;
     		}     		     		
    	}
 		
    	if ( smallestVector == null ) smallestVector = new Vector3();    	
    	return smallestVector;	    
    }
   
 		
    /**
     * Return a vector to move Polygon A away from Polygon B on the given axis,
     * or the zero vector if they don't intersect.
     */
    protected Vector3 AxisSeparatesPolygons( Vector3 axis, Polygon A, Polygon B ) {
    	
    	// project each polygon onto the axis
    	Interval intervalA = calculateInterval( axis, A );
     	Interval intervalB = calculateInterval( axis, B );
      	
        if (   (intervalA.min > intervalB.max)
            || (intervalB.min > intervalA.max)) {
            return new Vector3();
        }

        // The vector to return is parallel to the axis unit vector,
        Vector3 ret = new Vector3(axis);

        // and scaled by the overlap, with the correct sign.
        if (intervalA.max > intervalB.max) {
            ret.scalar(intervalB.max - intervalA.min);
        } else {
            ret.scalar(intervalB.min - intervalA.max);
        }
        return ret;
    }
    
    /**
     * Projects a Polygon P onto axis N and returns the min/max interval of the
     * projection. 
     */
    protected Interval calculateInterval( Vector3 axis, Polygon P ) {
    	
    	// for each vertex
    	int numEdges = P.npoints;
    	
     	Interval interval = new Interval(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
    
    	for ( int i=0; i < numEdges ; i++) {
    		
    		// project the vertex into the axis
    		double vX = P.xpoints[i];
    		double vY = P.ypoints[i];

    		Vector3 vertex = new Vector3( vX, vY, 0 );
      		
      		double dot = axis.dot( vertex );
      		 
    	 	if ( dot < interval.min ) interval.min = dot;
    		else if ( dot > interval.max ) interval.max = dot;
    	}	
    	return interval;
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // Inner classes
    
    protected class Interval {
	 	
	    double min;
	    double max;
	    
	    Interval(double min, double max) {
		 	this.min = min;
		 	this.max = max;   
	    }	    
    }
    
}
