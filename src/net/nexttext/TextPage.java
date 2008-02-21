package net.nexttext;

import java.awt.Component;
import java.awt.Graphics2D;

import net.nexttext.renderer.Java2DTextPageRenderer;
import net.nexttext.renderer.TextPageRenderer;

/**
 * 
 * A page responsible for storing and displaying text
 * 
 * <p>The actual rendering of the text is delegated to a TextPageRender 
 * to support separation of model from view and to promote modularity.<p>
 *
 */
public class TextPage implements Page{

	//The root of the text hierarchy that this page
	//is responsible for rendering.
    protected TextObjectGroup textRoot;
    protected TextPageRenderer textPageRenderer;

    public TextPage(Book book){
        this.textRoot = new TextObjectGroup();
        book.getTextRoot().attachChild(textRoot);
        this.textPageRenderer = new Java2DTextPageRenderer();
    }
    
    public TextPage(Book book, TextPageRenderer t){
        this.textRoot = new TextObjectGroup();
        book.getTextRoot().attachChild(textRoot);
        this.textPageRenderer = t;
    }

    public void render(Graphics2D g2, Component c) {        
        textPageRenderer.render(this, g2, c);
    }

    public TextObjectGroup getTextRoot() {
        return textRoot;
    }   
}
