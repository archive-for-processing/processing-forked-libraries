package edu.duke.geo4;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

class ColorSelector {
  
  PApplet myParent;
  int[] myColorArray;

  private ColorSelector(PApplet parent, int[] colorArray) {
    myParent = parent;
    myColorArray = colorArray;
  }
  
  public int getColor(float value) {
    assert(value >= 0.0f && value <= 1.0f);
    
    float pos = PApplet.map(value, 0.0f, 1.0f, 0, myColorArray.length - 1);
    int lowerIdx = PApplet.max(PApplet.floor(pos), 0);
    int upperIdx = PApplet.min(PApplet.ceil(pos), myColorArray.length -1);
    
    assert(upperIdx - lowerIdx == 1 || upperIdx - lowerIdx == 0);
    
    return myParent.lerpColor(myColorArray[lowerIdx], myColorArray[upperIdx], pos - (float) lowerIdx);
  }
  
  public static ColorSelector loadColorSelector(PApplet parent, String filename, ColorMap.Mode mode) {
    JSONArray maps = parent.loadJSONObject(filename).getJSONArray("maps");
    String jsonKey = getJsonKey(mode);
    
    JSONObject selectedMap = null;
    for (int i = 0; i < maps.size(); i++) {
      JSONObject curr = maps.getJSONObject(i);
      if (curr.getString("mapName").equals(jsonKey)) {
        selectedMap = curr;
      }
    }
    if (selectedMap == null) {
      throw new RuntimeException("Map could not be found for the given name");
    }
    
    int[] colorArray = buildColorArray(parent, selectedMap.getJSONArray("data"));
    return new ColorSelector(parent, colorArray);
  }
  
  private static String getJsonKey(ColorMap.Mode mode) {
    switch (mode) {
      case HOT:
        return "hot";
      case COOL:
        return "cool";
      default:
        throw new RuntimeException("No colormap defined for the specified mode");
    }
  }
  
  private static int[] buildColorArray(PApplet parent, JSONArray data) {
    int[] res = new int[data.size()];
    for (int i = 0; i < data.size(); i++) {
      JSONObject o = data.getJSONObject(i);
      res[o.getInt("idx")] = parent.color(o.getInt("r"), o.getInt("g"), o.getInt("b"));
    }
    return res;
  }
}
