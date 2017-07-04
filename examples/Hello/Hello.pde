import tramontana.library.*;
import websockets.*;

Tramontana t;

void setup(){
 size(480,240);
 t = new Tramontana(this,"192.168.1.10");
}
void draw(){
  background(255);
  fill(128);
  text("Hello Tramontana!",width/2-(textWidth("Hello Tramontana!")/2),height/2);
}
void mousePressed(){
  t.makeVibrate();
  
}