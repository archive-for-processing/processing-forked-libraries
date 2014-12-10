import patterning.library.Patterning;
import patterning.library.IPattern;

Patterning P;
IPattern myPattern;

void setup() {
  size(600,600);
  P = new Patterning(this);
}

void draw() {
  background(255);

  IPattern myPattern = P.poly(0, P.zx(4), 0.4, 3+P.mouseY(8),            
           P.style(5,P.pColor(P.mouseX(255),255,100),
                      P.pColor(200,P.mouseY(255),100,200))  );
                      
  myPattern = P.clock((int)P.mouseX(9), myPattern);
  myPattern = P.rotate(PI/4,myPattern);
  myPattern = P.grid(5,myPattern);
  
  P.draw(myPattern);  
}


void keyPressed() { 
  P.keyPressed(key); 
  println(P.zx());
}
