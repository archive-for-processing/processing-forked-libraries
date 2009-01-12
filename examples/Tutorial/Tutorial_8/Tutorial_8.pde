import processing.opengl.*;

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
  noStroke();

  // create and add the stay in window Behaviour
  AbstractAction stayInWindow = new StayInWindow(this);
  Behaviour stayInWindowBehaviour = stayInWindow.makeBehaviour();
  book.addGlyphBehaviour(stayInWindowBehaviour);

  // create the chaos pull Action
  AbstractAction chaosPull = new ChaosPull(new Vector3(width/2, height/2));

  // create the reform Action
  AbstractAction reform = new Reform();

  AbstractAction follow;
  Multiplexer followAndReform;
  AbstractAction followOrPullBack;
  Behaviour followOrPullBackBehaviour;
  for (int i=0; i < word.length(); i++) {
    // instantiate the follow mouse Action
    follow = new Repeat(new MoveTo(Book.mouse, i+1), 0);

    // instantiate and set the follow mouse and reform Multiplexer
    followAndReform = new Multiplexer();
    followAndReform.add(follow);
    followAndReform.add(reform);

    // instantiate and add the follow mouse or chaos pull to center Behaviour
    followOrPullBack = new OnMouseOverApplet(this, followAndReform, chaosPull);
    followOrPullBackBehaviour = followOrPullBack.makeBehaviour();
    book.addGlyphBehaviour(followOrPullBackBehaviour);

    // build the text
    book.addText(word.substring(i, i+1), width/2, height/2); 

    // remove the Behaviour so that it it not applied to the rest of the Book
    book.removeGlyphBehaviour(followOrPullBackBehaviour);
  }
}

void draw() {
  background(0);
  book.stepAndDraw();
}










