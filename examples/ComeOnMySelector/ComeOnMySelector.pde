import net.nexttext.*;
import net.nexttext.behaviour.*;
import net.nexttext.behaviour.control.*;
import net.nexttext.behaviour.physics.*;
import net.nexttext.behaviour.standard.*;
import net.nexttext.behaviour.dform.*;

/**
 * A NextText sketch where different behaviours can be applied to the text using the 1, 2, 3, 4, 5 keys.
 *
 * by Elie Zananiri | Obx Labs | June 2007
 */

// global attributes
Book book;
Selector selector;
PFont font;
String word = "NextText";

void setup() {
  size(800, 360);
  smooth();

  // create the Book
  book = new Book(this);

  // load and set the font
  font = createFont("GeometricBlack.ttf", 48, true);
  textFont(font);
  fill(255);
  stroke(241, 100, 34);
 
  // create the Selector and add all the Actions to it
  selector = new Selector();
  selector.add("Throb", new Throb(2, 100));
  selector.add("ChaosPull", new ChaosPull(Book.mouse)); 
  selector.add("Reform", new Reform());
  selector.add("RandomMotion", new RandomMotion());
  Chain colour = new Chain();
  colour.add(new Colorize(Color.BLACK, 5));
  colour.add(new Colorize(Color.WHITE, 5));
  selector.add("Colour", new Repeat(colour, 0));
  
  // add the selector Behaviour to the Book
  Behaviour selectorBehaviour = selector.makeBehaviour();
  book.addGlyphBehaviour(selectorBehaviour);
  selector.select("Throb");
  
  // build the text
  book.addText(word, 320, 200); 
}

void draw() {
  background(0);
  book.stepAndDraw();
}

void keyPressed() {
  switch (key) {
    case '1':
      selector.select("Throb");
      break;
    case '2':
      selector.select("ChaosPull");
      break;
    case '3':
      selector.select("Reform");
      break;
    case '4':
      selector.select("RandomMotion");
      break;
    case '5':
      selector.select("Colour");
      break;
  }
}
