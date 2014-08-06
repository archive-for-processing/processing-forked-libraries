import GLSLShaders.library.*;


GLSLShader myShader;
GLSLShader myFilter;
int i;

PGraphics pg;

void setup(){
  size(800,600,P2D);
  myShader = new GLSLShader(this,"warp");
  pg = createGraphics(800, 600, P2D);
  myFilter = new GLSLShader(this,"invert");
  
}

void draw() {
     myShader.setParameter("complexity",2);
     myShader.setParameter("detail", 0.002);
    // myShader.setParameter("resolution", width,height);
     myShader.setParameter("time", (float)millis()/1000);
  

  pg.beginDraw();
  pg.shader(myShader.shader);
  pg.rect(0, 0, pg.width, pg.height);
  pg.endDraw();
  
  fill(0);
//  rect(0, 0, 480, height);
  image(pg, 0,0); 

// myFilter.setParameter("sigma", 1000 );
 // myFilter.setParameter("resolution", width,height );
             
 filter(myFilter.shader);
 
}
