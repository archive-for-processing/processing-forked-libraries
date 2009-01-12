import net.nexttext.*;
import net.nexttext.behaviour.*;
import net.nexttext.behaviour.control.*;
import net.nexttext.behaviour.dform.*;
import net.nexttext.behaviour.physics.*;
import net.nexttext.behaviour.standard.*;

/**
 * A simple NextText sketch.
 *
 * <p>by Elie Zananiri | Obx Labs | January 2009</p>
 */

// global attributes
Book book;
PFont font;
String word = "NextText";

void setup() {
  size(700, 240);
  smooth();

  // create the Book
  book = new Book(this);

  // load and set the font
  font = createFont("GeometricBlack.ttf", 48);
  textFont(font);
  textAlign(CENTER);
  fill(255);
  stroke(96);
  strokeWeight(5);

  // create the follow mouse Behaviour
  AbstractAction follow;
  Behaviour followBehaviour;
  for (int i=0; i < word.length(); i++) {
    // instantiate and add the Behaviour
    follow = new Repeat(new MoveTo(Book.mouse, i+1), 0);
    followBehaviour = follow.makeBehaviour();
    book.addGlyphBehaviour(followBehaviour);

    // build the text
    book.addText(word.substring(i, i+1), width/2, height/2);

    // remove the Behaviour so that it it not applied to the rest of the Book
    book.removeGlyphBehaviour(followBehaviour);
  } 
}

void draw() {
  background(0);
  book.stepAndDraw();
}






