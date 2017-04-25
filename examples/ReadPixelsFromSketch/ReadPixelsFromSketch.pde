import tactilegraphics.concept.*;
ReadPixelsFromSketch reader;

void setup() {
  size(10, 10);
  reader = new ReadPixelsFromSketch(this);
  rect(0, 0, 5, 5);
}