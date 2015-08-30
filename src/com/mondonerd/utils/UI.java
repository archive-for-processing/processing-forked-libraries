package com.mondonerd.utils;
import java.lang.reflect.Method;
import processing.core.*;
import processing.event.MouseEvent;
import java.util.ArrayList;

/*

This file is part of ARsenico copyright by Massimo Avvisati (dw@mondonerd.com)
It is released under Free Software license GPL v3

Please visit http://mondonerd.com for credits and informations.

*/

public class UI {
  public static final int RELEASE = MouseEvent.RELEASE;
  public static final int PRESS = MouseEvent.PRESS;
  public static final int CLICK = MouseEvent.CLICK;
  public static final int DRAG = MouseEvent.DRAG;
  ArrayList<Button> elements = new ArrayList<Button>();
  PApplet parent;
  UI(PApplet parent) {
    this.parent = parent;
    parent.registerMethod("mouseEvent", this);
    //parent.registerMethod("draw", this);
  }


  public void display() {

    //parent.hint(parent.DISABLE_DEPTH_TEST);
    for (Button element : elements) {
      element.display(parent.g);
    }
    prune();
  }
  
  private void prune() {
    for (int i = elements.size() -1; i >= 0; i--) {
     Button element = elements.get(i);
     if (element.dead)
       elements.remove(i);
    }
  }
  Button add(String methodBaseName, String filename, float x, float y) {
    return add( methodBaseName, x, y, 1, 1).setImage(filename, true);
  }
  Button add(String methodBaseName, String filename, float x, float y, float w, float h) {
    return add( methodBaseName, x, y, w, h).setImage(filename, false);
  }

  Button add(String methodBaseName, float x, float y, float w, float h) {
    Button element = new Button(parent, methodBaseName, (int) x, (int) y, (int) w, (int) h);
    elements.add(element);
    return element;
  }
  Button dragged = null;
  public void mouseEvent(MouseEvent event) {
    int x = event.getX();
    int y = event.getY();


    switch (event.getAction()) {

    case MouseEvent.RELEASE:
    case MouseEvent.PRESS:
    case MouseEvent.CLICK:
    dragged = null;
    boolean eventCatched = false;
      for (int i = elements.size () -1; i >= 0; i--) {
        Button element = elements.get(i);
//(element.commandMethod != null)
        if (!eventCatched && element.visible && element.isInside(x, y)) {
          eventCatched = true;
          if (dragged == null && event.getAction() == MouseEvent.PRESS)
            dragged = element;
            
          try {
            element.commandMethod.invoke(parent, element, event.getAction(), x - element.x, y - element.y);
            
          } 
          catch (Exception ex) {
          }
          
        }
      }

      break;

    case MouseEvent.DRAG:
      if (dragged != null && dragged.visible)
      try {
        dragged.commandMethod.invoke(parent, dragged, event.getAction(), x - dragged.x, y - dragged.y);
      } 
      catch (Exception ex) {
      }
      break;
    case MouseEvent.MOVE:
      // umm... forgot

      break;
    }
  }

  public class Button {
    int x, y, w, h;
    Method commandMethod;
    public int buttonColor = 255;
    PShape buttonShape;
    PImage buttonImage;
    boolean dead = false;
    

    protected Button(PApplet sketch, String method, int x, int y, int w, int h) {
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
      setMethod(method, sketch);
    }
    
    public void destroy() {
     dead = true; 
      
    }

    public boolean isInside(int _x, int _y) {
      return ( _x > x && _x < x + w && _y > y && _y < y + h);
    }
    private void setMethod(String method, PApplet sketch) {
      try {
        commandMethod = sketch.getClass().getMethod(method, //questo definisce il metodo necessario nello sketch
        new Class[] { 
          Button.class, 
          int.class, 
          int.class, 
          int.class
        }
        );
      } 
      catch (Exception ex) {
        // no such method, or an error.. which is fine, just ignore
        commandMethod = null;
        PApplet.println(ex + "\nPlease implement a " + method + " method in your main sketch if you want to be informed");
      }
    }


    Button setImage(String filename) {
      return setImage(filename, false);
    }
    Button setImage(String filename, boolean resize) {
      if (filename.indexOf(".svg") > 0) {
        buttonShape = parent.loadShape(filename);
        if (resize) {
          w = (int) buttonShape.width;
          h = (int) buttonShape.height;
        }
      } else {
        buttonImage = parent.loadImage(filename);
        if (resize) {
          w = (int) buttonImage.width;
          h = (int) buttonImage.height;
        }
      }
      return this;
    }
    private int alignment = PApplet.LEFT;
    void setAlign(int alignment) {
      if (this.alignment == alignment)
        return;
      if (this.alignment == PApplet.LEFT && alignment == PApplet.CENTER)
        x -= w/ 2;
      else if (this.alignment == PApplet.LEFT && alignment == PApplet.RIGHT)
        x -= w;
      else if (this.alignment == PApplet.CENTER && alignment == PApplet.LEFT)
        x += w / 2;
      else if (this.alignment == PApplet.CENTER && alignment == PApplet.RIGHT)
        x -= w / 2;
      else if (this.alignment == PApplet.RIGHT && alignment == PApplet.LEFT)
        x += w;
      else if (this.alignment == PApplet.RIGHT && alignment == PApplet.CENTER)
        x += w / 2;

      this.alignment = alignment;
    }

    boolean visible = true; 
    public void hide() {
      visible = false;
    }

    public void show() {
      visible = true;
    }

    void display(PGraphics pg) {
      if (!visible)
        return;
      if (buttonImage != null) {
        pg.image(buttonImage, x, y, w, h);
      } else if (buttonShape == null) {
        pg.noStroke();
        pg.fill(buttonColor);
        pg.rect(x, y, w, h);
      } else {
        pg.shape(buttonShape, x, y, w, h);
      }
    }
  }
}