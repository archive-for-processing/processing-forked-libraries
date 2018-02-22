/**
 * Wooting One keyboard visual demonstration
 * @author Sunjun Kim
 */

import org.kiml.*;

WootingManager wManager;
Layout keyboard;
HashMap<KeyValue, Integer> parsedData = null;

float keyUnit = 40; // key pitch in pixel
float gap = 5;      // gap between keys in pixel

void setup()
{
  wManager = new WootingManager(this);
  wManager.openWootingDevices();
  
  // tracking All the keys
  wManager.setAllMode();
  
  // Select the keyboard layout you use: ANSI / ISO
  keyboard = new Layout(LayoutType.ISO);
  //keyboard = new Layout(LayoutType.ANSI);
  
  size(760, 310); // (keyUnit * 19, keyUnit * 7 + 30)
  rectMode(CORNER);
  strokeWeight(1);
}

void draw()
{
  // Black background
  background(0);
  
  String list = "Keys: ";
  
  // Iterate over all keys on the layout
  for(Object k : keyboard.getKeys().values()) {
    SingleKey sk = (SingleKey)k;
    fill(10);
    stroke(127);
  
    // draw keyboard only if data is available --> otherwise, just return here.
    if(parsedData == null)
      return;
      
    // get the amount of the key depression.
    int depressedAmount = parsedData.get(sk.keyName);
    // if depressed, color them with a green gradient color.
    if(depressedAmount > 0)
    {
      fill(10, depressedAmount/4*3 + 256/4, 0);
      list += sk.keyName + "("+ depressedAmount + ")  "; 
    }
    
    // draw a key shape
    rect(keyUnit*(sk.xpos) + 10, keyUnit*(sk.ypos+1.5) + 10, keyUnit*sk.w - gap, keyUnit*sk.h - gap, 5);
  }
  
  fill(255);
  textSize(20);
  text(list, 10, keyUnit*7 + 20);
}

void onReadEvent(byte[] buffer) {
  parsedData = wManager.parseData(buffer);
}