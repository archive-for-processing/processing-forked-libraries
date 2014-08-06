import GLSLShaders.library.*;


GLSLShader myShader;
GLSLShader myFilter;
int i;

PGraphics pg;

void setup(){
  size(800,600,P2D);
  myShader = new GLSLShader(this,"warp");
  pg = createGraphics(800, 600, P2D);
  myFilter = new GLSLShader(this,"threshold");
  
}

void draw() {
     myShader.setParameter("complexity",2);
     myShader.setParameter("detail", 0.002);
    // myShader.setParameter("resolution", width,height);
     myShader.setParameter("time", (float)millis()/10000);
  
  myFilter.setParameter("threshold", (float)mouseX/width );
  
  pg.beginDraw();
  pg.shader(myShader.shader);
  pg.rect(0, 0, pg.width, pg.height);
  pg.filter(myFilter.shader);
  pg.endDraw();
  
  tint(255,30);

  image(pg, 0,0);              
 
 
}
