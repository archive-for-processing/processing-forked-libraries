package tactilegraphics.concept;

import cc.arduino.Arduino;
import java.io.IOException;
import processing.core.PApplet;

public class BlinkPixels {
  private PApplet applet;
  private Arduino arduino;
  private int ledPin;

  public BlinkPixels(PApplet applet) {
    this.applet = applet;
    this.applet.registerMethod("draw", this);

    arduino = new Arduino(applet, Arduino.list()[0], 57600);
    ledPin = 13;
    arduino.pinMode(ledPin, Arduino.OUTPUT);
  }

  public void draw() throws IOException, InterruptedException {
    arduino.digitalWrite(ledPin, Arduino.HIGH);
    Thread.sleep(2000);
    arduino.digitalWrite(ledPin, Arduino.LOW);
    Thread.sleep(1000);
  }

}
