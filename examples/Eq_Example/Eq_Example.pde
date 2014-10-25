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
  equal.eq();//SAME
  equal.eq("MIX");//SAME
  equal.eq("LEFT", 0, height/2);
  equal.eq("RIGHT", width/2, height/2 - 2, 0, 0, 255);
  equal.eq("MIX", 30, 20, 255, 255, 0, 100, 20);
  equal.eq("MIX", 50, 100, 100, 100, 100, 10, 20, 10);
  }

