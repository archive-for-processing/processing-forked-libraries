package funGUI;

public class SliderOutput extends NumOutput{
	Slider s;
	public SliderOutput(float x, float y, float w, float h, Slider slider) {
		super(slider.p, x, y, w, h, slider.p());
		this.s = slider;
	}
	
	@Override
	protected void display() {
		g.pushStyle();
		g.fill(255);
		g.stroke(0);
		g.rectMode(CENTER);
		g.rect(x, y, w, h);
		g.fill(30);
		g.textFont(font);
		g.textAlign(LEFT, CENTER);
		g.text(val(), x - (w * .45f), y);
		g.popStyle();
	}
	
	public float val() {
		return(s.p());
	}
	
}
