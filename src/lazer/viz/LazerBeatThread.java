package lazer.viz;

//the beat clock thread
public class LazerBeatThread extends Thread {
  long timeStamp;
  long interval;
  int MINUTE = 60000;
  boolean running;
  LazerBeatManager parent;

  public LazerBeatThread(LazerBeatManager parent, long timeStamp, int interval) {
    this.parent = parent;
    this.timeStamp = timeStamp;
    this.interval = interval;
    this.running = true;
  }

  public void run() {
   while (running) {
      beat();
    }
  }

  void stopit() {
    this.running = false;
  }

  void beat() {
    try {
//      parent.broadcastEvent(this);
      Thread.sleep(interval);
    } catch(InterruptedException e) {
    }
  }



}
