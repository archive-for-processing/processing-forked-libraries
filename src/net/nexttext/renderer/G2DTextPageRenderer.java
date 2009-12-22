package net.nexttext.renderer;

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.util.Stack;

import processing.core.PApplet;
import processing.core.PVector;
import net.nexttext.Book;
import net.nexttext.TextObject;
import net.nexttext.TextObjectGlyph;
import net.nexttext.TextObjectGroup;
import net.nexttext.TextPage;

/**
 * 
 * Renders the text stored in a text page using a Graphics2D object.
 * 
 * <p>
 * This TextPage renderer is based on the Java2D API.
 * </p>
 * 
 */
public abstract class G2DTextPageRenderer extends TextPageRenderer {
    protected Graphics2D g2;

    /**
     * Builds a TextPageRenderer.
     * 
     * @param p the parent PApplet
     */
    public G2DTextPageRenderer(PApplet p) {
        super(p);    
    }
    
    /**
     * Traverse the TextObject tree and render all of its glyphs.
     */
    protected void traverse(TextObject root) {
        // The tree is traversed using a variable to point at the current node
        // being processed. TextObjects specify their rotation and position
        // relative to their parent, which is handled by registering coordinate
        // system changes with the Graphics2D object as the tree is traversed.

        // Currently rendering is not synchronized with modifications to the
        // TextObjectTree. This is dodgy, but gives a performance boost, so
        // will stay that way for the moment. However, it affects this method,
        // because we can't assume that the tree is always well structured.

        // Transformations are stored in a stack so that they can be undone as
        // needed. It is not appropriate to use the position of the TextObject
        // to undo the transformation, because this may have changed due to the
        // lack of synchronization.

        TextObject current = root;
        Stack coordTransforms = new Stack();
        do {
            // Draw any glyphs.
            if (current instanceof TextObjectGlyph) {
                enterCoords(coordTransforms, current);
                renderGlyph((TextObjectGlyph) current);
                exitCoords(coordTransforms);
            }

            // Descend to process any children
            if (current instanceof TextObjectGroup) {
                TextObjectGroup tog = (TextObjectGroup) current;
                TextObject child = tog.getLeftMostChild();
                if (child != null) {
                    enterCoords(coordTransforms, current);
                    current = child;
                    continue;
                }
            }

            // Processing of this node is complete, so move on to siblings.
            // Since a node may not have siblings, a search is made up the tree
            // for the first appropriate sibling. The search ends if a sibling
            // is found, or if it reaches the top of the tree.
            while (current != root) {
                TextObject sibling = current.getRightSibling();
                if (sibling != null) {
                    current = sibling;
                    break;
                } else {
                    current = current.getParent();
                    if (current == null) {
                        // Aaarghh, we were detached from the tree mid-render,
                        // so just abort the whole process.
                        return;
                    }
                    exitCoords(coordTransforms);
                }
            }
        } while (current != root);
    }

    /**
     * Transform the Graphics2D into the coordinates of the given TextPage.
     */
    protected void enterCoords(TextPage page) {
    	PVector pos = page.getPosition().get();
    	PVector rot = page.getRotation().get();

    	g2.translate(pos.x, pos.y);
        g2.translate(p.width/2.0f, p.height/2.0f);
		g2.rotate(rot.z);
    	g2.translate(-p.width/2.0f, -p.height/2.0f);
    }

    /**
     * Transform the Graphics2D back into the coordinates given TextPage.
     */
    protected void exitCoords(TextPage page) {
    	PVector pos = page.getPosition().get();
    	PVector rot = page.getRotation().get();

        g2.translate(p.width/2.0f, p.height/2.0f);
		g2.rotate(-rot.z);
    	g2.translate(-p.width/2.0f, -p.height/2.0f);

    	g2.translate(-pos.x, -pos.y);
    }
    
    /**
     * Transform the Graphics2D into the coordinates of the given TextObject.
     * 
     * <p>
     * Once this transformation is done, the TextObject and any of it's children
     * can be written directly to the Graphics2D without having to handle
     * position or rotation.
     * </p>
     */
    protected void enterCoords(Stack ct, TextObject node) {
    	PVector pos = node.getPosition().get();
        g2.translate(pos.x, pos.y);
        ct.push(pos);

        float rotation = node.getRotation().get();
        g2.rotate(rotation);
        ct.push(new Float(rotation));
    }

    /**
     * Transform the Graphics2D out of the coordinates on top of the stack.
     * 
     * <p>
     * This undoes the change of enterCoords().
     * </p>
     */
    protected void exitCoords(Stack ct) {
        float rotation = ((Float) ct.pop()).floatValue();
        g2.rotate(-rotation);

        PVector pos = (PVector) ct.pop();
        g2.translate(-pos.x, -pos.y);
    }

    /**
     * Renders a TextObjectGlyph using quads, either as an outline or as a
     * filled shape.
     * 
     * @param glyph
     *            The TextObjectGlyph
     */
    protected void renderGlyph(TextObjectGlyph glyph) {
        // ////////////////////////////////////
        // Optimize based on presence of DForms and of outlines
        if (glyph.isDeformed() || glyph.isStroked()) {

            // ////////////////////////////////
            // Render glyph using vertex list

            // Use the cached path if possible.
            GeneralPath gp = glyph.getOutline();

            // draw the outline of the shape
            if (glyph.isStroked()) {
                g2.setColor(glyph.getStrokeColorAbsolute());
                g2.setStroke(glyph.getStrokeAbsolute());
                g2.draw(gp);
            }

            // fill the shape
            if (glyph.isFilled()) {
                g2.setColor(glyph.getColorAbsolute());
                g2.fill(gp);
            }
        }
        //if the PFont size and the set font size are the same then use the
        //text function to draw bitmaps.
        //this will create better results at small sizes.
        else if ((glyph.getFont().getFont() == null) ||
        		 (glyph.getFont().size == glyph.getFont().getFont().getSize())) {
        	//set the color
        	p.fill(glyph.getColorAbsolute().getRGB());
        	//set the font
        	p.textFont(glyph.getFont());
        	//draw the glyph
        	p.text(glyph.getGlyph(), 0, 0);
        }
        //if the set font size is not the same as the PFont then draw using
        //the outlines so as not to get a pixelated scaling effect.
        else {
            // /////////////////////////////////////////
            // Render glyph using Graphics.drawString()
            g2.setColor(glyph.getColorAbsolute());
            // set the font
            g2.setFont(Book.loadFontFromPFont(glyph.getFont()));
            // draw the glyph
            g2.drawString(glyph.getGlyph(), 0, 0);
        }

    } // end renderGlyph
}
