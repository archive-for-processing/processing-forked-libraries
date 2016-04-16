import funGUI.*;

// Different FunGUI classes needed
RectButton spinButton;
Timer timer;
NumOutput speed;
TextOutput result;

float theta = (int(random(0, 4))) * HALF_PI;
float omega = .01; // the angular speed
color [] wheel = {color(255, 0, 0), color(255, 255, 0), color(125, 255, 0), color(0, 0, 255)};
String [] colorNames = {"Red", "Yellow", "Green", "Blue"};

void setup() {
  size(400, 400);
  spinButton = new RectButton(this, 200, 350, 200, 60, "Press to Spin");
  timer = new Timer(this);
  speed = new NumOutput(this, width / 2, 20, 100, 20, omega);
  speed.label("Spinning RPM");
  result = new TextOutput(this, width / 2, 50, 100, 20, "N/A", "Result");
}

void draw() {
  background(255);
  spinButton.draw();
  drawSpinner();
  speed.assign(omega / TWO_PI);
  speed.draw();
  result.draw();
}

void drawSpinner() {
  // The radius of the spinning disc
  float r = 100;
  
  if (spinButton.clicked()) {
    // If the spinButton is clicked, we will increase the angle
    // and also increase the angular speed
    theta += omega;
    omega += .01;
    timer.reset(0);
  } else if (omega > .01) {
    // If it isn't clicked and the angluar speed is greater than .01,
    // We will still increase the angle
    theta += omega;
    // But we will also decrease the angular speed, so
    // angular_speed_final = angular_speed_initial - .0005 * timer.absTimeLeft()
    // (the amount of time that has passed since resetting the timer in milliseconds)
    omega -= .01 * .05 * timer.absTimeLeft();
    timer.reset(0);
  }
  
  // Get the angle for each of the arcs, one arc for each color in wheel array
  float dth = TWO_PI / wheel.length;
  for (int i = 0; i < wheel.length; i++) {
    fill(wheel[i]);
    noStroke();
    // The starting angle
    float angle1 = theta + (i * dth);
    // The ending angle
    float angle2 = theta + ((i + 1) * dth);
    if (omega <= .01 && onTarget(angle1, angle2)) {
      result.assignText(colorNames[i]);
    }
    arc(width / 2, 190, 2 * r, 2 * r, angle1, angle2);
  }
  
  // Coordinates of the triangle that points at the color that the spinner
  // lands on.
  float x = (width / 2) + r * cos(3 * (HALF_PI));
  float y = ((height / 2) - 10) + r * sin(3 * (HALF_PI));
  fill(0);
  noStroke();
  // Need to make a triangle using beginShape() and endShape()
  beginShape();
  // The tip of the triangle
  vertex(x, y + 10);
  // The left top vertex
  vertex(x - 10, y - 16);
  // The right top vertex
  vertex(x + 10, y - 16);
  endShape(CLOSE);
}

boolean onTarget(float angle1, float angle2) {
  float targetAngle = 3 * HALF_PI;
  angle1 %= TWO_PI;
  angle2 %= TWO_PI;
  return(angle1 < targetAngle && angle2 > targetAngle);
}