package com.mondonerd.arsenico;

/*

 This file is part of ARsenico copyright by Massimo Avvisati (dw@mondonerd.com)
 It is released under Free Software license GPL v3
 
 Please visit http://mondonerd.com for credits and informations.
 
 */

import processing.core.*;
import jp.nyatla.nyar4psg.MultiMarker;
import jp.nyatla.nyar4psg.NyAR4PsgConfig;
import java.lang.reflect.*;

public class AugmentedReality {

  PApplet parent;


  boolean updating = false;
  MultiMarker nya;
  int totalCount = 0;

  PMatrix3D globalMatrix;
  PMatrix3D[] localMatrix;

  public boolean debug = true;
  static final int DEFAULT_WIDTH = 640;
  static final int DEFAULT_HEIGHT = 480;

  Method displayMethod;
  private boolean mirroredInput = false;

  boolean[] activeMarker;

  /**
   * Create an Augmented Reality environment.
   * 
   * @param _parent
   *            Your sketch. For example inside {@code setup()} you can use
   *            {@code AugmentedReality(this);}
   */
  public AugmentedReality(PApplet _parent) {
    this(_parent, DEFAULT_WIDTH, DEFAULT_HEIGHT);
  }

  /**
   * Create an Augmented Reality environment.
   * 
   * width and height are used to create the offscreen buffer used to store
   * input for markers detection. They shouldn't be bigger than the input
   * image width and height. You may specify a renderer (P3D, OPENGL...)
   * 
   * @param _parent
   * @param width
   *            input width
   * @param height
   *            input width
   * @param renderer
   *            {@code P2D}, {@code P3D}, {@code OPENGL} or any valid renderer
   * 
   * @see PGraphics
   */

  public AugmentedReality(PApplet _parent, int w, int h) {

    parent = _parent;

    nya = new MultiMarker(parent, w, h, "camera_para.dat", 
    NyAR4PsgConfig.CONFIG_PSG);

    try {
      displayMethod = parent.getClass().getMethod("displayScene", 
      new Class[] { 
        int.class
      }
      );
    } 
    catch (Exception e) {
      // no such method, or an error.. which is fine, just ignore

        debug(e.getMessage());
    }

    reality = parent.createImage(w, h, PApplet.RGB);
  }

  /**
   * Load markers from .patt and .png files.
   * 
   * @param markersFilenames
   *            One or more filenames with markers.
   */

  public void loadMarkers(String... markersFilenames) {
    for (int i = 0; i < markersFilenames.length; i++) {
      if (markersFilenames[i].indexOf(".png") >= 0) {
        PImage markerTemp = parent.loadImage(markersFilenames[i]);
        if (markerTemp != null)
          addMarker(markerTemp);
      } else {
        addMarker(markersFilenames[i]);
      }
    }
  }

  /*
   * private void initOffscreen(int w, int h) { offscreen =
   * parent.createGraphics(w, h, PConstants.P3D); debug("initOffscreen(" + w +
   * "," + h + ") = " + offscreen.width + "x" + offscreen.height); }
   */

  protected void addMarker(String markerFilename) {
    if (nya == null) {
      debug("You are trying to addMarker(" + markerFilename
        + ") before to call initAR()");
      return;
    }
    try {
      nya.addARMarker(markerFilename, 80);
    } 
    catch (Exception ex) {
      debug("You are trying to addMarker(" + markerFilename
        + ") but it doesn't seem to be a valid filename");
    } 
    finally {
      totalCount++;
    }
  }

  protected void addMarker(PImage markerImage) {
    if (nya == null) {
      debug("You are trying to addMarker() before to call initAR()");
      return;
    }
    try {
      nya.addARMarker(markerImage, 16, 25, 80);
    } 
    catch (Exception ex) {
      debug("You are trying to addMarker() but it doesn't seem to be a valid file");
    } 
    finally {
      totalCount++;
    }
  }

  /**
   * If your users face a webcam like a mirror you may want to "flip" the
   * input image.
   * 
   * @return
   * @see #getReality()
   */

  public boolean toggleMirror() {
    mirroredInput = !mirroredInput;
    return mirroredInput;
  }

  /**
   * A PImage of your reality. We will use it to detect markers
   * 
   * @param inputImg
   *            It can be a PImage or Capture or Movie or whatever derives
   *            from PImage
   * 
   * @see getReality();
   */

  PImage reality;

  PImage getReality() {
    return reality.get();
  }

  public synchronized void refreshInput(PImage inputImg) {


    if (nya != null && inputImg != null) {
      try {
        //nya.detect(inputImg);

        reality.set(0, 0, inputImg.get());
        nya.detectWithoutLoadPixels(reality);
      } 
      catch (Exception ex) {
        debug("Error while detecting");
        return;
      }
    } else {
      debug("Error while detecting");
      return;
    }

    PMatrix3D projectMatrix = nya.getProjectionMatrix();

    // if (projectMatrix != null)
    globalMatrix = projectMatrix.get();
    // TODO
    // setPerspective(m, pg);

    localMatrix = new PMatrix3D[totalCount];
    activeMarker = new boolean[totalCount];

    for (int i = 0; i < totalCount; i++) {
      if (nya.isExistMarker(i)) {
        localMatrix[i] = nya.getMarkerMatrix(i);
        activeMarker[i] = true;
      } else {
        activeMarker[i] = false;
        localMatrix[i] = new PMatrix3D();
      }
    }
  }

  private void displayEvent(PGraphics pg, int i) {
    if (displayMethod != null) {
      try {
        displayMethod.invoke(parent, i, pg);
      } 
      catch (Exception e) {
        debug("Disabling displayMethod() because of an error.");
        // e.printStackTrace();
        displayMethod = null;
      }
    }
  }

  private void displayEvent(int i) {
    if (displayMethod != null) {
      try {
        displayMethod.invoke(parent, i);
      } 
      catch (Exception e) {
        debug("Disabling displayMethod() because of an error.");
        // e.printStackTrace();
        displayMethod = null;
      }
    }
  }

  /**
   * Specify how strict is the markers detection.
   * 
   * @param threshold
   *            A value between 0 and 256 or a -1 for automatic value
   * @param confidenceThreshold
   *            Between 0.0 and 1.0
   * @param lostDelay
   *            1 or more
   */
  public void setParameters(int threshold, double confidenceThreshold, 
  int lostDelay) {
    nya.setThreshold(threshold);
    nya.setConfidenceThreshold(confidenceThreshold);
    nya.setLostDelay(lostDelay);
  }

  public int getThreshold() {
    return nya.getCurrentThreshold();
  }

  /**
   * Get current "confidence" for a specific marker
   * 
   * @param id
   * @return
   */
  public double getConfidence(int id) {
    return nya.getConfidence(id);
  }

  /**
   * In order to display your augmented reality you must implement a
   * displayScene(int markerID) method inside your sketch
   * 
   * void displayScene(int markerID) { //here you are using the marker
   * //projection matrix }
   * 
   */
  public void display() {
    display(parent.g);
  }

  /**
   * Display your augmented reality on a different PGraphics object (an
   * offscreen buffer for example)
   * 
   * @param offscreen
   *            buffer or other PGraphics
   */
  public synchronized void display(PGraphics pg) {
    if (globalMatrix == null || localMatrix == null || activeMarker ==
      null)
      return;

    pg.hint(PApplet.ENABLE_DEPTH_TEST);
    //pg.hint(PApplet.DISABLE_DEPTH_TEST);
    pg.pushMatrix();

    pg.setMatrix(globalMatrix);
    if (mirroredInput) {
      pg.scale(-1, 1, 1);
      PApplet.println("mirrored");
    }
    setPerspective(globalMatrix, pg);

    for (int i = 0; i < totalCount; i++) {
      if (activeMarker[i]) {
        pg.pushMatrix();
        if (localMatrix[i] != null)
          pg.setMatrix(localMatrix[i]); // load Marker matrix

        if (pg == parent.g)
          displayEvent(i);
        else
          displayEvent(pg, i);

        // displayScene(i);
        pg.popMatrix();
      }
    }
    pg.popMatrix();
    pg.perspective();
  }

  private void setPerspective(PMatrix3D i_projection, PGraphics pg) {
    // Projection frustum
    float far = i_projection.m23 / (i_projection.m22 + 1);
    float near = i_projection.m23 / (i_projection.m22 - 1);
    pg.frustum((i_projection.m02 - 1) * near / i_projection.m00, 
    (i_projection.m02 + 1) * near / i_projection.m00, 
    (i_projection.m12 - 1) * near / i_projection.m11, 
    (i_projection.m12 + 1) * near / i_projection.m11, near, far);
    return;
  }

  private void debug(String message) {
    if (!debug)
      return;
    PApplet.println(message);
  }
}