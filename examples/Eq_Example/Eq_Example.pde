import visualizer.library.*;

Visualizer visual;
Eq equal;

void setup() {
  size(500, 500);
  //visualizer setup has to be first
  visual = new Visualizer(this);
  visual.songSet("Pamgaea.mp3");
  //Has to be after the song is set
  equal = new Eq();
}

void draw() {
  background(0);
  equal.eq();
  equal.eq(250, 250);
  equal.eq(10, 250, 250, 200);
  equal.eq(250, height - 50, 30, 50, 30);
}

