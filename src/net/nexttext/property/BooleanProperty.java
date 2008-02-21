//
// Copyright (C) 2004,2005,2006 Jason Lewis
//

package net.nexttext.property;

/**
 * A boolean property type. 
 * 
 */
public class BooleanProperty extends Property {

    private boolean original;
    private boolean value;
    
    public BooleanProperty(boolean value){
        this.original = value;
        this.value = value;
    }
    
    public boolean get(){
        return value;
    }
    
    public boolean getOriginal(){
        return original;        
    }
    
    public void set(boolean value){
        this.value = value;
    }    
        
    public void reset() {
        value = original;
    }

}
