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

/**
 * A CoordinateSystem describe a set of three Axes positioned at some arbitrary 
 * origin in space.
 * 
 * <pre>
 *  (origin)_ _ _ _ x
 *    		|\
 *    		| \
 *    		|  \z
 *    		y
 * </pre>
 */
/* $Id$ */
public class CoordinateSystem {
   
    private Vector3 origin;
    private Axes axes;
    
    /**
     * Creates a "default" CoordinateSystem where the origin is (0,0,0) and each
     * axes is a unit vector.
     */
    public CoordinateSystem() {       
        this.origin = new Vector3();
        this.axes = new Axes();
    }
    
    /**
     * Creates a CoordinateSystem with the specified origin and rotation
     */
    public CoordinateSystem( Vector3 origin, double rotation ) {

        this.origin = origin;        
        this.axes = new Axes();
        axes.rotate( rotation );        
    }
    
    /**
     * Creates a CoordinateSystem with the specified origin and rotation, then 
     * transforms it by the specified parent system.
     * 
     * XXXBUG This description is not explicit enough.  
     */
    public CoordinateSystem( Vector3 origin, double rotation, CoordinateSystem parentSystem ) {
        
        this.origin = origin;
        
        this.axes = new Axes();
        axes.rotate( rotation );
        
        // transform this system by the parent system, ie the origin and
        // axes are now expressed in terms of the parent system.
        
        this.origin = parentSystem.transform( this.origin );
        this.axes = parentSystem.axes.transform( this.axes );    
    }

    /**
     * Returns a copy of the origin vector.
     */
    public Vector3 getOrigin() {
        return new Vector3(origin);
    }
    
    /**     
     * Takes a vector local to this coordinate system and returns an equivalent
     * vector transformed "out of" this system such that the vector is now 
     * relative to the parent system.
     * 
     * <p>Transforms a vector local to this coordinate system by aligning it with
     * this system's axes and translating by this system's origin. </p>  
     * 
     * @param inV the input vector; the vector will remain unchanged.
     * @return the equivalent vector relative to the parent system.  
     */
    public Vector3 transform( Vector3 inV ) {
        
        Vector3 outV = axes.transform( inV );        
        outV.add( origin );
        return outV;        
    }
    
    /**
     * Transforms a polygon out of this coordinate system.
     *
     * <p>Polygons are 2D, so the transformation is done with the assumption
     * that all Z values are 0.  </p>
     *
     * @return a new Polygon object.
     */
    public Polygon transform(Polygon inPoly) {
        Polygon outPoly = new Polygon();
        for (int i = 0; i < inPoly.npoints; i++) {
            double x = axes.transformX(inPoly.xpoints[i], inPoly.ypoints[i], 0);
            double y = axes.transformY(inPoly.xpoints[i], inPoly.ypoints[i], 0);
            outPoly.addPoint((int)(x + origin.x), (int)(y + origin.y));
        }
        return outPoly;
    }

    /**
     * Transforms a vector from the parent system such that it is now relative to
     * this system.
     * 
     * @param inV the input vector; the vector will remain unchanged.
     * @return the transformed vector
     */
    public Vector3 transformInto( Vector3 inV ) {
        
        Vector3 outV = new Vector3( inV );
        outV.sub( this.origin );       
        outV = axes.transformInto( outV ); 
        return outV;
    }
    
    public String toString() {
        return "Origin: " + this.origin + "\n" + axes.toString();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // INNER CLASSES
    
    /**
     * This inner class represents the X/Y/Z axes as one unit. 
     */
    class Axes {
        
        Vector3 xAxis;
        Vector3 yAxis;
        Vector3 zAxis;
        
        /**
         * Creates default Axes using right-handed coordinates.
         */
        public Axes() {            
            // xAxis is pointing left
            this.xAxis = new Vector3(1.0f , 0.0f, 0.0f);
            // yAxis is pointing downwards            
            this.yAxis = new Vector3(0.0f , 1.0f, 0.0f);
            // positive Z values go "inside" the screen             
            this.zAxis = new Vector3(0.0f , 0.0f, 1.0f);
        }
        
        public Axes( Vector3 xAxis, Vector3 yAxis, Vector3 zAxis ) {            
            this.xAxis = xAxis;
            this.yAxis = yAxis;
            this.zAxis = zAxis;
        }
        
        /**
         * 2D Rotation (ie: around the Z-axis). 
         * 
         * <p>Rotates both xAxis and yAxis by the same amount</p>.
         */
        public void rotate( double radians ) {
            xAxis.rotate( radians );
            yAxis.rotate( radians );
        }
        
        /**
         * Outbound vector transformation.
         */
        public Vector3 transform( Vector3 inV ) {
            return new Vector3( (inV.x*xAxis.x + inV.y*yAxis.x + inV.z*zAxis.x),
    							(inV.x*xAxis.y + inV.y*yAxis.y + inV.z*zAxis.y),
    							(inV.x*xAxis.z + inV.y*yAxis.z + inV.z*zAxis.z) );        
        }
        
        // Perform outbound transformations without using vector objects.
        // These methods save the additional overhead of creating multiple
        // Vector3 objects.  They are used in the calculation of bounding
        // polygons, which is done quite often.  A separate method is used for
        // each axis because methods can only have a single return value.
        double transformX(double x, double y, double z) {
            return (x * xAxis.x + y * yAxis.x + z * zAxis.x);
        }

        double transformY(double x, double y, double z) {
            return (x * xAxis.y + y * yAxis.y + z * zAxis.y);
        }

        double transformZ(double x, double y, double z) {
            return (x * xAxis.z + y * yAxis.z + z * zAxis.z);
        }

        /**
         * Inbound vector transformation.
         */
        public Vector3 transformInto( Vector3 inV ) {
            return new Vector3( inV.dot(xAxis), 
                    			inV.dot(yAxis), 
                    			inV.dot(zAxis) );        
        }
        
        /**
         * Axes to Axes outbound transform.
         */
        public Axes transform( Axes axes ) {            
            Vector3 xA = axes.transform( xAxis );
            Vector3 yA = axes.transform( yAxis );
            Vector3 zA = axes.transform( zAxis );
            return new Axes(xA, yA, zA);
        }
        
        /**
         * Axes to Axes inbound transform.
         */        
        public Axes transformInto( Axes axes ) {           
            Vector3 xA = axes.transformInto( xAxis );
            Vector3 yA = axes.transformInto( yAxis );
            Vector3 zA = axes.transformInto( zAxis );
            return new Axes(xA, yA, zA);
        }
        
        public String toString() {
            return "X axis: " + xAxis + "\n" +
            	   "Y axis: " + yAxis + "\n" +
            	   "Z axis: " + zAxis;                	
        }
    }   
}
