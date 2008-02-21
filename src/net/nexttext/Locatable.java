package net.nexttext;

// $CVSHeader: obx/NextText/src/net/nexttext/Locatable.java,v 1.1.2.2 2005/04/13 15:00:18 dissent Exp $  

/**
 * An object with a position.
 */
public interface Locatable {
	
	/**
	 * @return The object's location in absolute coordinates.
	 */
	public Vector3 getLocation();
}
