import net.nexttext.Book;
import net.nexttext.TextObjectBuilder;
import net.nexttext.TextPage;
import net.nexttext.Vector3;
import net.nexttext.behaviour.Behaviour;
import net.nexttext.behaviour.AbstractBehaviour;
import net.nexttext.behaviour.control.Chain;
import net.nexttext.behaviour.control.Delay;
import net.nexttext.behaviour.control.Repeat;
import net.nexttext.behaviour.control.Timer;
import net.nexttext.behaviour.dform.ChaosPull;
import net.nexttext.behaviour.dform.Throb;
import net.nexttext.behaviour.physics.PhysicsFactory;
import net.nexttext.behaviour.standard.Kill;
import net.nexttext.behaviour.standard.MoveTo;
import net.nexttext.behaviour.standard.RandomMotion;
import net.nexttext.property.ColorProperty;
import net.nexttext.renderer.Java2DRenderer;
import net.nexttext.renderer.pages.ImagePage;
import net.nexttext.renderer.pages.RenderBoundingBoxes;
import net.nexttext.renderer.pages.RenderVelocity;
import net.obxlabs.util.FontManager;

import java.awt.Color;

public class NextTextExample{

    public static void main(String[] args) {
        int width = 800;
        int height = 600;
        
        //Create a rendering surface
        Java2DRenderer renderer = new Java2DRenderer(width, height);        
        renderer.setBackground(Color.WHITE);
        
        //Create a book
        Book book = new Book(renderer);
      
        //Create Some Pages
        
        //A page to draw an image
        //The top half of this image happens to be transparent
        ImagePage bip = new ImagePage("logoTranslucent.gif");        
        //Two text pages
        TextPage textPage = new TextPage(book);        
        TextPage textPage2 = new TextPage(book);
        //Pages that render the velocity of the TextObjects as lines
        RenderVelocity velRender = new RenderVelocity(textPage.getTextRoot(),Color.BLUE,15);
        RenderVelocity velRender2 = new RenderVelocity(textPage2.getTextRoot(),Color.RED,15);
        //This page will process the text stored on all the TextPages because it uses the Book's TextObjectRoot.
        RenderBoundingBoxes bbRenderAll = new RenderBoundingBoxes(book.getTextRoot(),Color.GREEN,true,false);
        
        //Add them to the book in reverse order of how you want them rendered.
        //i.e. the first page is below the second and so on.
        book.addPage("textLayer1 ",textPage2);
        book.addPage("textLayer1Velocity", velRender2);
        book.addPage("imageRenderer", bip);        
        book.addPage("textLayer2", textPage);
        book.addPage("textLayerVelocity", velRender);
        book.addPage("boundingBoxLayer", bbRenderAll);
        
        //Create some behaviours and add them to the book
        AbstractBehaviour move = PhysicsFactory.move();
        book.addBehaviour(move);
       
        AbstractBehaviour stayInWindow = PhysicsFactory.stayInWindow();
        book.addBehaviour(stayInWindow);

        AbstractBehaviour explode = PhysicsFactory.explode();
        book.addBehaviour(explode);
        
        Behaviour throb = new Behaviour(new Throb(2,50));
        book.addBehaviour(throb);
        
        Chain myActionChain = new Chain();
        myActionChain.add(new Timer(new RandomMotion(), 10));        
        myActionChain.add(new MoveTo(new Vector3(400, 50), 5));
        myActionChain.add(new Repeat(new ChaosPull(new Vector3(400,50)), 800));
        myActionChain.add(new Delay(new Kill(), 5));
        
        AbstractBehaviour shakeIt = new Behaviour(myActionChain);
        book.addBehaviour(shakeIt);
      
        //Create some textObjects on each of the TextPages
        TextObjectBuilder tob = new TextObjectBuilder(book);
        tob.setParent(textPage.getTextRoot());
        tob.setFont(FontManager.createFont("Tahoma", 0, 48));        
        tob.addGroupProperty("Color", new ColorProperty(Color.BLUE.darker()));
        tob.addGlyphBehaviour(move);                
        tob.addGlyphBehaviour(stayInWindow);
        tob.addGlyphBehaviour(explode);
        tob.addGlyphBehaviour(throb);        
        tob.build("Foreground", new Vector3(450, 300));
       
        TextObjectBuilder tob2 = new TextObjectBuilder(book);
        tob2.setParent(textPage2.getTextRoot());
        tob2.setFont(FontManager.createFont("Arial", 0, 48));        
        tob2.addGroupProperty("Color", new ColorProperty(Color.RED.darker()));
        tob2.addGlyphBehaviour(move);
        tob2.addGlyphBehaviour(stayInWindow);
        tob2.addGlyphBehaviour(explode);        
        tob2.build("Behind The Scenes", new Vector3(450, 300));      
        
        TextObjectBuilder tob3 = new TextObjectBuilder(book);
        tob3.setParent(textPage.getTextRoot());  
        tob3.setFont(FontManager.createFont("Arial", 0, 32));        
        tob2.addGroupProperty("Color", new ColorProperty(Color.DARK_GRAY));
        tob3.addGlyphBehaviour(shakeIt);
        tob3.addGlyphBehaviour(move);
        tob3.build("ShakeIt", new Vector3(650, 350));

        //Start the Simulator
        book.getSimulator().start();  
    }
}