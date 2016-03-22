package SmartGrid;

import processing.core.*;

public class GridDrawText extends GridDrawBase{

	public String text;
	public PFont font; 
	public float textSize;
	
	@Override
	public void setup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		if(text!=null)
		{
			app.pushStyle();
			app.textSize(textSize);
			app.textAlign(app.CENTER,app.CENTER);
			app.text(text, app.width/2, app.height/2);
			app.popStyle();
		}
	}

}
