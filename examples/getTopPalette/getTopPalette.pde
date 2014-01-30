import triss.colourlovers.*;

void setup() {
  size(640, 480);
  noLoop(); 
}

void draw() {
  // create a ColourLovers object cl  
  ColourLovers cl = new ColourLovers(this);
 
  // get the top Colour Lover's palette
  // you can access the first twenty top palettes by changing 0 for 0-19. 
  ColourLoversPalette palette = cl.getNthTopPalette(0);
  
  // print some info about the palette
  println(palette.getId());
  println(palette.getTitle());
  println(palette.getNumHearts());
  
  // get the list of colours from the palette
  color[] colours = palette.getColours();
  
  // work out how wide each rectangle should be
  float rectWidth = width / colours.length;
  
  noStroke();
  for(int i = 0; i < colours.length; i++) {
    fill(colours[i]);
    rect(i * rectWidth, 0 , (i + 1) * rectWidth, height);
  }
}
