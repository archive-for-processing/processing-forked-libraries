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

  // build the text
  book.addText("NextText", width/2, height/2);
}

void draw() {
  background(0);
  book.stepAndDraw();
}




