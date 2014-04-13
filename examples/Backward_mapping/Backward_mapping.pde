/**
 * This is a simple example of how to use the Keystone library.
 *
 * To use this example in the real world, you need a projector
 * and a surface you want to project your Processing sketch onto.
 *
 * Simply drag the corners of the CornerPinSurface so that they
 * match the physical surface's corners. The result will be an
 * undistorted projection, regardless of projector position or
 * orientation.
 *
 * You can also create more than one Surface object, and project
 * onto multiple flat surfaces using a single projector.
 *
 * This extra flexbility can comes at the sacrifice of more or
 * less pixel resolution, depending on your projector and how
 * many surfaces you want to map.
 */

import keystoned.*;

Keystone ks;
CornerPinSurface inSurface, outSurface;


PImage img;

void setup() {

  img = loadImage("image_small.png");

  size(img.width*2, img.height, OPENGL);

  ks = new Keystone(this);
  inSurface = ks.createCornerPinSurface(width/2, height, 10);
  inSurface.setGridColor(color(255, 0, 0));
  inSurface.setControlPointsColor(color(255, 0, 0));

  // outputsurface doesn't have to match res
  outSurface = ks.createCornerPinSurface(width/2, height, 1);
  outSurface.moveTo(width/2, 0);
  outSurface.setGridColor(color(0, 255, 0));
  outSurface.setControlPointsColor(color(0, 255, 0));

}

void draw() {
  background(0);

  image(img, 0, 0, img.width, img.height);

  // render without a texture
  // if toggleCalibration is off
  // then you won't be able to see this
  inSurface.render();

  outSurface.render(img, inSurface);
  //outSurface.render(this.g, img, 0, 0, img.width, img.height ,inSurface);

}

void keyPressed() {
  switch(key) {
  case 'c':
    // enter/leave calibration mode, where surfaces can be warped
    // and moved

    // should be possible to choose which one to calibrate (usefull on overlap)
    ks.toggleCalibration();
    break;

  case 'l':
    // loads the saved layout
    ks.load();
    break;

  case 's':
    // saves the layout
    ks.save();
    break;
  }
}