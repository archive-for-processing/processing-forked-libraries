import funGUI.*;

float theta = 0;

ToggleButton showingWave;
Slider amplitude;
Slider period;
SliderOutput ampOutput;

void setup() {
  size(400, 400);
  showingWave = new ToggleButton(this, width / 2, height - 40, "Show Wave", Direction.RIGHT);
  amplitude = new Slider(this, width / 2 - 70, height - 70, 100, "Amplitude");
  amplitude.alignText(Direction.LEFT);
  amplitude.assignLowHigh(-1, 1);
  period = new Slider(this, width / 2, height - 100, 0, 100 * PI, "Period");
  period.alignText(Direction.LEFT);
  ampOutput = new SliderOutput(width / 2 + 120, height - 70, 100, 20, amplitude);
}

void draw() {
  background(255);
  
  showingWave.draw();
  amplitude.draw();
  ampOutput.draw();
  period.draw();
  
  
  drawWave();
}

void drawWave() {
  if (showingWave.on()) {
    float increment = TWO_PI / width;
    float yi = height / 2 - 30;
    
    for (float th = increment; th < TWO_PI; th += increment) {
      float x0 = map(th, 0, TWO_PI, 0, width);
      float x1 = map(th - increment, 0, TWO_PI, 0, width);
      float y0 = yi + 100 * amplitude.p() * sin(period.p() * (theta + th));
      float y1 = yi + 100 * amplitude.p() * sin(period.p() * (theta + (th - increment)));
      
      if (th % (increment * 8) < increment * .5) {
        stroke(200);
        strokeWeight(1);
        float x1a = x1;
        float x0a = x0;
        line(x0a, yi, x1a, yi);
      }
      
      stroke(2);
      strokeWeight(1);
      line(x1, y1, x0, y0);
    }
    theta += PI / 100;
  }
}