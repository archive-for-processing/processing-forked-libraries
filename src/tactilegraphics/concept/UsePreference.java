package tactilegraphics.concept;

import processing.core.PApplet;

public class UsePreference {
  private PApplet applet;
  private String nameToPrint;

  public UsePreference(PApplet applet, String nameToPrint) {
    this.applet = applet;
    this.nameToPrint = nameToPrint;
    this.applet.registerMethod("draw", this);
  }


  public void draw() {
    System.out.println("Hello " + nameToPrint);
  }
}
