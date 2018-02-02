import org.kiml.*;

WootingManager wManager;

void setup()
{
  wManager = new WootingManager(this);
  wManager.openWootingDevices();
  
  // tracking All of key
  wManager.setAllMode();          
  
  // tracking Alphabet, number, arrow and function key
  //wManager.setTextMode();
}

void draw()
{
}

void onReadEvent(byte[] buffer) {
  println(wManager.parseLog(buffer));  // print Raw Data

  HashMap<KeyValue, Integer> parsedData = wManager.parseData(buffer);
  int a = parsedData.get(KeyValue.A);
  int b = parsedData.get(KeyValue.B);
  println(String.format("%s:%d, %s:%d", "A", a, "B", b));  // print Data of selected Key 
}