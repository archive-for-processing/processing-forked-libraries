import com.dhchoi.CountdownTimer;

CountdownTimer timer;
String timerCallbackInfo = "";

void setup() {
   size(300, 300);

   // create and start a timer that has been configured to trigger onTickEvents every 10 ms and run for 5000 ms
   timer = CountdownTimer.getNewCountdownTimer(this).configure(10, 5000).start();
}

void draw() {
  background(255);
  fill(0);
  textAlign(LEFT, TOP);

  // show the status of the timer
  text("timer.isRunning():" + timer.isRunning(), 0, 0);
  text("timer.isPaused():" + timer.isPaused(), 0, 15);

  // show the info of event callbacks
  textAlign(CENTER, CENTER);
  text(timerCallbackInfo, width/2, height/2);
}

void onTickEvent(int timerId, long timeLeftUntilFinish) {
  timerCallbackInfo = "[timerId:" + timerId + "] tick - timeLeft:" + timeLeftUntilFinish;
  println(timerCallbackInfo);
}

void onFinishEvent(int timerId) {
  timerCallbackInfo = "[timerId:" + timerId + "] finished";
  println(timerCallbackInfo);
}

void keyPressed() {
  // user interface for operating the timer
  switch(key) {
    case 'a':
      println("Starting timer...");
      timer.start();
      break;
    case 's':
      println("Stopping timer...");
      timer.stop();
      break;
    case 'r':
      println("Resetting timer...");
      timer.reset();
      break;
  }
}