import funGUI.*;

Slider theta;

void setup() {
  size(300, 320);
  theta = new Slider(this, width / 2, height - 20, 100, "Theta");
}

void draw() {
  background(255);
  theta.draw();
  drawArrow();
}

void drawArrow() {
  float th = theta.p() * TWO_PI;
  float x = width / 2;
  float y = (height / 2) - 20;
  float r = 80;
  fill(0);
  noStroke();
  ellipse(x, y, 10, 10);
  stroke(0);
  line(x, y, x + cos(th) * r, y + sin(th) * r);
  
  float s = 10;
  noStroke();
  pushMatrix();
  translate(x + cos(th) * r, y + sin(th) * r);
  pushMatrix();
  //rotate(-(PI / 3));
  pushMatrix();
  beginShape();
  vertex(s * cos(th), s * sin(th));
  th += 2 * (PI / 3);
  vertex(s * cos(th), s * sin(th));
  th += 2 * (PI / 3);
  vertex(s * cos(th), s * sin(th));
  endShape(CLOSE);
  popMatrix();
  popMatrix();
  popMatrix();
  float r0 = r + 20;
  
  for (float i = 0; i < TWO_PI - .05; i+= QUARTER_PI) {
    textSize(15);
    if (i == 0) {
      textAlign(LEFT, CENTER);
      text("0, 2π", x + (cos(i) * r0), y + (sin(i) * r0));
    } else {
      if (i > QUARTER_PI && i <= QUARTER_PI + HALF_PI) {
        textAlign(CENTER, TOP);
      } else if (i > QUARTER_PI + HALF_PI && i <= PI + QUARTER_PI) {
        textAlign(RIGHT, CENTER);
      } else if (i > PI + QUARTER_PI && i <= TWO_PI - QUARTER_PI) {
        textAlign(CENTER, BOTTOM);
      } else {
        textAlign(LEFT, CENTER);
      }
      
      String angle = "";
      if (i % (.5 * PI) == 0) {
        angle = nfc(i / PI, 1) + "π";
      } else {
        angle = nfc(i / PI, 2) + "π";
      }
      text(angle, x + (cos(i) * r0), y + (sin(i) * r0));
    }
  }
}