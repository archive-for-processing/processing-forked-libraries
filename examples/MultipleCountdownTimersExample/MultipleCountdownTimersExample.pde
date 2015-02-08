/**
 * Example of using multiple CountdownTimers in one application.
 */

import com.dhchoi.CountdownTimer;
import com.dhchoi.CountdownTimerService;

CountdownTimer redCircleTimer;
CountdownTimer blueCircleTimer;
float redCircleRadius = 0;
float blueCircleRadius = 0;
float maxCircleRadius = 150;

void setup() {
  size(500, 300);

  // create timers
  redCircleTimer = CountdownTimerService.getNewCountdownTimer(this).configure(100, 6000).start(); // this timer will have timerId = 0
  blueCircleTimer = CountdownTimerService.getNewCountdownTimer(this).configure(150, 9000).start(); // this timer will have timerId = 1
}

void draw() {
  background(255);
  noStroke();
  ellipseMode(CENTER);

  // show created timers
  fill(0);
  textAlign(CENTER, CENTER);
  text("Created timerIds: " + CountdownTimerService.getTimerIds(), width/2, height/2 - 100);
  
  // draw red circle
  fill(255, 0, 0);
  ellipse(width/3, height/2, redCircleRadius, redCircleRadius);

  // draw blue circle
  fill(0, 0, 255);
  ellipse(width*2/3, height/2, blueCircleRadius, blueCircleRadius);
}

void onTickEvent(int timerId, long timeLeftUntilFinish) {
  // change the radius of the circle based on which timer it was hooked up to
  switch (timerId) {
    case 0:
      redCircleRadius = map(timeLeftUntilFinish, 6000, 0, 0, maxCircleRadius);
      break;
    case 1:
      blueCircleRadius = map(timeLeftUntilFinish, 9000, 0, 0, maxCircleRadius);
      break;
    }
}

void onFinishEvent(int timerId) {
  // finalize any changes when the timer finishes
  switch (timerId) {
    case 0:
      redCircleRadius = maxCircleRadius;
      break;
    case 1:
      blueCircleRadius = maxCircleRadius;
      break;
  }

  println("[timerId:" + timerId + "] finished");
}
