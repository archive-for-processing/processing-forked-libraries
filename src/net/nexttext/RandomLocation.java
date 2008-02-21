//
// Copyright (C) 2005,2006 Jason Lewis
//

package net.nexttext;

import java.awt.Rectangle;
import java.awt.Shape;

/**
 * A locatable which responds with a random location.
 *
 * <p>The location returned will be inside the given shape.  The bounds of the
 * shape are checked on each call, so mutable shapes will be properly
 * supported.  </p>
 *
 * <p>Because of the way getLocation() is implemented, very thin shapes with
 * large bounding boxes may cause it to lock up in a loop trying to find a
 * point inside.  </p>
 */

public class RandomLocation implements Locatable  {

    static final String REVISION = "$CVSHeader: NextText/src/net/nexttext/RandomLocation.java,v 1.2 2005/05/16 16:55:46 dissent Exp $";

    Shape shape;

    public RandomLocation(Shape shape) {
        this.shape = shape;
    }

    public Vector3 getLocation() {

        double x,y;
        do {
            Rectangle bounds = shape.getBounds();
            x = bounds.x + Math.random() * bounds.width;
            y = bounds.y + Math.random() * bounds.height;
        } while (!shape.contains(x, y));

        return new Vector3(x, y);
    }
}
