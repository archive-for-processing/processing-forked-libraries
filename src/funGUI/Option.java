package funGUI;
import processing.core.*;

public class Option extends ListItem implements PConstants {
	//String label;
	//public float x, y;
	//r1, r2;
	//public float wratio, hratio;
	//public boolean selected = false;
	//PFont font;
	//Display d;
	//int numColumns;

	//float beginningW;
	//float beginningH;
	boolean testing = false;

	Option(PApplet p, float ex, float why, String label, Display d, int numColumns) {
		super(p, ex, why, label, d, numColumns);
		r1 = (float) 10.0;
		r2 = r1 + 5;
		wratio = (float) 1.0;
		hratio = (float) 1.0;
		this.numColumns = numColumns;
		font = p.createFont("FZLTXHK--GBK1-0", 200, true);
	}
	
	public void record(String s) {
		if (testing) {
			PApplet.println("Option Class: " + s);
		}
	}

	void update() {
		record("update()");
		wratio = (float) (p.width / beginningW);
		hratio = (float) (p.height / beginningH);
		record("update() - done");
	}
	
	@Override
	public void display() {
		record("display()");
		update();
		
		record("display() - g.noStroke()");
		g.noStroke();
		record("display() - g.noStroke() - done");
		record("display() - g.fill()");
		g.fill(220, 220, 220);
		record("display() - g.fill() - done");
		record("display() - shapeDisplay()");
		shapeDisplay(r2 * wratio);
		record("display() - shapeDisplay() - done");
		record("display() - g.fill()");
		g.fill(90, 90, 90);
		record("display() - g.fill() - done");
		
		record("display() - selected()");
		selected();
		record("display() - selected() - done");

		selectionDisplay();
		record("selectionDisplay() - done");
		
		record("display(); - g.fill()");
		g.fill(0);
		record("display() - g.fill() - done");
		g.textAlign(LEFT, CENTER);
		//g.textFont(font, limitFont(font, 12, label, (float) ((d.w() / numColumns) / 1.25)));
		g.textFont(font, 12);
		record("display() - textFont - done");
		g.text(label, wratio * (x + 20), hratio * y);
		record("display() - done");
	}
	
	public void selectionDisplay() {
		record("selectionDisplay()");
		if (selected) {
			shapeDisplay(radius());
		}
		record("selectioinDisplay() - done");
	}
	
	public void shapeDisplay(float radius) {
		record("shapeDisplay()");
		g.ellipse(x * wratio, y * hratio, radius, radius);
	}

	void selected() {
		record("selected()");
		if (!pressed && p.mousePressed && clicked())
			selected = !selected;
		pressed = p.mousePressed;
		record("selected() - done");
	}

	float radius() {
		record("radius()");
		return(r1 * wratio + PApplet.constrain((float) ((r2 - r1) * wratio * .5), 0f, (float) ( (r2 - r1) * .5) ));
	}
	
	@Override
	public boolean clicked() {
		record("clicked()");
		return(p.mousePressed && (p.mouseX < wratio * (x + r2) && p.mouseX > wratio * (x - r2)) && (p.mouseY < hratio * (y + r2) && p.mouseY > hratio * (y - r2)));
	}
}
