import funGUI.*;

// This float controls where the sin function starts
float theta = 0;

// These FunGUI objects help control/show different aspects of the wave
ToggleButton showingWave;
Slider amplitude;
Slider period;
SliderOutput ampOutput;

void setup() {
  size(400, 400);
  
  // Initialize the different items
  //                                 PApplet, x, y, label, text position relative to button
  showingWave = new ToggleButton(this, width / 2, height - 40, "Show Wave", Direction.RIGHT);
  //                    PApplet, x, y, width, label
  amplitude = new Slider(this, width / 2 - 70, height - 70, 100, "Amplitude");
  // Assign it a text position relative to the silder
  amplitude.alignText(Direction.LEFT);
  // Assign it its low, high values
  amplitude.assignLowHigh(-1, 1);
  //                  PApplet, x, y, low value, high value, label
  period = new Slider(this, width / 2, height - 100, 0, 100 * PI, "Period");
  // Assign its relative text position
  period.alignText(Direction.LEFT);
  //                                x, y, width, height, slider object
  ampOutput = new SliderOutput(width / 2 + 120, height - 70, 100, 20, amplitude);
}

void draw() {
  background(255);
  
  // Draw all of the FunGUI Objects
  showingWave.draw();
  amplitude.draw();
  ampOutput.draw();
  period.draw();
  
  // Draw the wave
  drawWave();
}

void drawWave() {
  // If the button is toggled on
  if (showingWave.on()) {
    // The value to increment by
    float increment = TWO_PI / width;
    // The value of y that is in the middle of the wave
    float yi = height / 2 - 30;
    
    for (float th = increment; th < TWO_PI; th += increment) {
      // The x-coordinates
      float x0 = map(th, 0, TWO_PI, 0, width);
      float x1 = map(th - increment, 0, TWO_PI, 0, width);
      // The y-coordinates
      float y0 = yi + 100 * amplitude.p() * sin(period.p() * (theta + th));
      float y1 = yi + 100 * amplitude.p() * sin(period.p() * (theta + (th - increment)));
      
      // If this is one of every 8 loops
      if (th % (increment * 8) < increment * .5) {
        // Draw a short line in the middle
        stroke(200);
        strokeWeight(1);
        float x1a = x1;
        float x0a = x0;
        line(x0a, yi, x1a, yi);
      }
      
      // Draw a line connecting the two points along the sine curve
      stroke(2);
      strokeWeight(1);
      line(x1, y1, x0, y0);
    }
    // Increment the theta
    theta += PI / 100;
  }
}