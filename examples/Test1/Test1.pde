import template.library.*;

Patterning P;
IPattern myPattern;

void setup() {
  size(400,400);
  P.setup(this);
}

void draw() {
  background(255);

  IPattern myPat = P.poly(0, 0.5, 0.4, 3+P.mouseY(8),            
           P.style(5,P.pColor(P.mouseX(255),255,100),
                      P.pColor(200,P.mouseY(255),100,200))  );
                      
  meuPadrao = P.clock((int)P.mouseX(9), meuPadrao);
  meuPadrao = P.rotate(PI/4,meuPadrao);
  meuPadrao = P.grid(3,meuPadrao);
  
  P.draw(meuPadrao);  
}

