package unisonmenu.snapperturtle;

import static java.lang.Math.*;

public class Vector {
    private float x, y, z;
    private final float PI=(float)Math.PI;

    /**Creates a vector object with specified x and y components.
     * @param x
     * @param y
     */
    public Vector(float x, float y){
        z=0;
        this.x=x;
        this.y=y;
    }


    /**Creates a vector object with specified x and y components.
     *
     * @param x
     * @param y
     * @param z
     */
    public Vector(float x, float y, float z){
        this.z=z;
        this.x=x;
        this.y=y;
    }

    /**Creates a null vector.
     *
     */
    public Vector(){
        x=0;
        y=0;
        z=0;
    }

    /**Sets the values of this vector to that of an existing vector
     *
     * @param vect
     */
    public Vector(Vector vect){
        this.x=vect.x;
        this.y=vect.y;
        this.z=vect.z;
    }

    /**
     *
     * @return The x-component of the vector
     */
    public float getX() {
        return (x);
    }

    /**
     *
     * @return The y-component of the vector
     */
    public float getY() {
        return y;
    }


     /**
     *
     * @return The z-component of the vector
     */
    public float getZ() {
        return (z);
    }

    public void setX(float x) {
        this.x=x;
    }

    public void setY(float y) {
        this.y=y;
    }

    public void setZ(float z) {
        this.z=z;
    }

    /**Returns
     *
     * @param vector2 The second vector with which to calculate the dot-product
     * @return The scalar dot product between two vectors
     */
    public float dot(Vector vector2) {
        return ((x*vector2.x)+(y*vector2.y)+(z*vector2.z));
    }

    /**Adds each component of the parametrised vectors together and returns this as a new vector
     *
     * @param vector1
     * @param vector2
     * @return The sum of two vectors
     */

    public static Vector add(Vector vector1, Vector vector2) {
        return new Vector(vector1.x+vector2.x,vector1.y+vector2.y ,vector1.z+vector2.z);
    }

    /**Adds a second vector to this vector
     *
     * @param vector2
     */
    public void add(Vector vector2) {
        x+=vector2.x;
        y+=vector2.y;
        z+=vector2.z;
    }

    /**Scales this vector by the paramtrised factor
     *
     * @param factor
     */
    public void mult(float factor) {
        x*=factor;
        y*=factor;
        z*=factor;
    }

    /**Multiplies factor1, the vector by factor2,
     * a float and returns the value as a vector
     *
     * @param factor1
     * @param factor2
     * @return The product of factor1 and factor2
     */
    public static Vector mult(Vector factor1,float factor2) {
        Vector copy=new Vector(factor1);
        copy.mult(factor2);
        return copy;
    }

    /**Shrinks this vector by the paramtrised denominator
     *
     * @param denominator
     */
    public void div(float denominator) {
        x/=denominator;
        y/=denominator;
        z/=denominator;
    }

    public static Vector div(Vector numerator, float denominator) {
        Vector copy=new Vector(numerator);
        copy.div(denominator);
        return copy;
    }

    public void subtract(Vector vector2) {
        x-=vector2.x;
        y-=vector2.y;
        z-=vector2.z;
    }

    /**
     *
     * @return The pythagorean magnitude of this vector
     */
    public float magnitude(){
        return (float)(sqrt(x * x + y * y + z * z));
    }
    /** This method is useful for comparisons because if the magnitude of vector1>vector2,
     * then vector1^2>vector2^2. Many formulas also require the square of the magnitude.
     * This function is more efficient than calculating the magnitude as it does not use the expensive
     * Square Root function.
     *
     * @return The square of this vector's magnitude
     */
    public float sqrMagnitude(){
        return (x*x+ y*y+z*z);
    }

    /**Converts this vector into a unit vector
     *
     */
    public void normalise(){
        float magnitude=magnitude();
        x/=magnitude;
        y/=magnitude;
        z/=magnitude;
    }

    /**Returns a vector which is the normal of the parametrised vector, without changing the value of the parameter.
     *
     * @param vect
     * @return a vector which is a unit vector of vect.
     */
    public static Vector normal(Vector vect){
        Vector retVect=new Vector(vect);
        retVect.normalise();
        return retVect;
    }

    /**
     * Creates a vector by resolving the parametrised magnitude and direction into respective x and y components
     *
     * @param magnitude The magnitude or size of the vector
     * @param direction The direction in which the vector acts, measured in radians counter-clockwise from the positive
     *                  x-axis
     * @return A resolved 2-d vector object
     */
    public static Vector createVectorMagDir(float magnitude, float direction) {
        return new Vector(resolveX(magnitude, direction), resolveY(magnitude, direction));
    }

    private static float resolveX(float magnitude, float angle) {
        return (magnitude *(float) cos(angle));
    }

    private static float resolveY(float magnitude, float angle) {
        return (magnitude * (float) sin(angle));
    }

    /**
     * @return The direction in which the vector acts, measured in radians counter-clockwise from the positive x-axis
     * @throws IllegalStateException This exception is thrown when the magnitude of the vector is zero, as logically a vector
     *                        of magnitude zero cannot have a direction.
     */
    public float direction() throws IllegalStateException{

        float angle = 0;


        if (y != 0 || x != 0) {
            //Produces an angle ranging between -PI/2 and PI/2
            angle = (float) Math.asin(y / magnitude());
        } else {
            System.err.println("A vector without magnitude cannot have an angle");
            return 0;
        }

        /*Quadrant One (0-PI/2)
        No further manipulation is needed. Size of angle is already in the correct format
            x=>0:
            y>0:
        */

        /* The sin function produces a negative angle if in the third/ fourth quadrant, convenietly allowing us to
            combine two angle manipulations into one, that of quadrant two and three

            Quadrant Two: (PI/2-PI) Equivalent to PI-Angle
            x<0:
            y>0:

            Quadrant Three: (PI/2-PI) Equivalent to PI+Angle
            But negative w/ sin, thus equivalent to PI-Angle= PI+(-Angle)
            x<0:
            y>0:
        */

        if (x < 0) {
            angle = PI-angle;
        }
        /*Quadrant Four (PI+PI/2-2PI) Equivalent to 2*PI-Angle
            x>0
            y<0
        */
        else if (y < 0) {
            angle = (2*PI) + angle;
        }

        return angle;
    }


    public static  Vector cross(Vector vector, Vector vector2){
        return new Vector(vector.y*vector2.z-vector.z*vector2.y,vector.z*vector2.x-vector.x*vector2.y,vector.x*vector.y-vector.y*vector.x);
    }

    public static Vector subtract(Vector vector1, Vector vector2) {
        return new Vector(vector1.x-vector2.x,vector1.y-vector2.y,vector1.z-vector2.z);
    }



    /**
     * Returns a copy of <i>this</i> vector
     *
     * @return a copy of <i>this</i> vector
     */
    @Override
    public Vector clone() throws CloneNotSupportedException{
        super.clone();
        return new Vector(x, y);
    }

    @Override
    public boolean equals(Object vector) {
        if (vector instanceof Vector){
            Vector vect=(Vector) vector;
        return x == vect.getX() && y==vect.getY();
        }
        else{
            return false;
        }

    }

    public String toString(){
        if (z!=0){
            return "("+x+", "+y+", "+z+")";
        }
        else {
            return "("+x+", "+y+")";
        }
    }
}

