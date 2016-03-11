import SmartGrid.*;
//this is a template file, please do not modify it
SmartGridManager manager;
SmartGrid rootGrid;
 void setup()
 {
   surface.setResizable(true);
   size(500,500);
   manager=new SmartGridManager(this);
   //create your grid here
   rootGrid=manager.CreateGrid();//default 12*12 root grid
   //add your code here
   
   //-----------------------
   manager.setup();
   frameRate(60);
 }
 
 void draw()
 {
   background(200);
   manager.draw(true);
 }
 
 void keyPressed()
 {
   manager.keyPressed();
 }
 