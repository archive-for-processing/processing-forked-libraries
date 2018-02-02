import org.kiml.*;

WootingManager wManager;

int[] result = new int[4];
boolean isDraw;

void setup()
{
  size(550, 450);
  background(255);
  wManager = new WootingManager(this);
  wManager.openWootingDevices();

  // tracking selected keys(A, S, D, F)
  ArrayList<KeyValue> custom = new ArrayList<KeyValue>();
  custom.add(KeyValue.A);
  custom.add(KeyValue.S);
  custom.add(KeyValue.D);
  custom.add(KeyValue.F);
  wManager.setCustomMode(custom);  

  // tracking All of key
  //wManager.setAllMode();          

  // tracking Alphabet, number, arrow and function key
  //wManager.setTextMode();
}

void draw()
{
  if (isDraw) {
    background(255);

    textSize(20);
    fill(0);
    text("A: " + result[0], 20, 30);
    text("S: " + result[1], 20, 130);
    text("D: " + result[2], 20, 230);
    text("F: " + result[3], 20, 330);
    
    for(int i = 0; i < 4; i++){
      stroke(0);
      fill(183, 21, 21);
      rect(20, i * 100 + 50, 200, 50);
      
      stroke(0);
      fill(183, 183, 21);
      rect(220, i * 100 + 50, 200, 50);
      
      stroke(0);
      fill(21, 183, 21);
      rect(420, i * 100 + 50, 100, 50);
      
      stroke(0);
      fill(255);
      rect(20 + result[i] * 2, i * 100 + 50, 500 - result[i] * 2, 50);
    }
    isDraw = false;
  }
}

void onReadEvent(byte[] buffer) {
  HashMap<KeyValue, Integer> parsedData = wManager.parseData(buffer);
  result[0] = parsedData.get(KeyValue.A);
  result[1] = parsedData.get(KeyValue.S);
  result[2] = parsedData.get(KeyValue.D);
  result[3] = parsedData.get(KeyValue.F);
  println(String.format("%s:%d, %s:%d, %s:%d, %s:%d", "A", result[0], "S", result[1], "D", result[2], "F", result[3]));  // print Data of selected Key 
  isDraw = true;
}