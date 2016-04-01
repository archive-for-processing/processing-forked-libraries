package funGUI;
import processing.core.*;
import processing.data.*;

import java.util.ArrayList;

import funGUI.StatsCalculator.DataType;

public class Graph extends Window {
	StatsCalculator dataX = new StatsCalculator(DataType.FLOAT);
	FloatList outliers = new FloatList();
	StatsCalculator dataY = new StatsCalculator(DataType.FLOAT);
	ArrayList<FloatList> data = new ArrayList<FloatList>();
	DataDimension dimension;
	GraphType type = null;
	DotPlot dots;
	BoxPlot boxes;
	
	
	public Graph(PApplet p, float x, float y, float w, float h, DataDimension dimension, GraphType type) {
		super(p, x, y, w, h);
		this.dimension = dimension;
		this.type = type;
		init();
	}
	
	
	public Graph(PApplet p, float x, float y, float w, float h, DataDimension dimension) {
		super(p, x, y, w, h);
		this.dimension = dimension;
		init();
	}
	
	public Graph(PApplet p, float x, float y, float w, float h) {
		super(p, x, y, w, h);
		this.dimension = DataDimension.DATA_1D;
		init();
	}
	
	void addDataX(float f) {
		dataX.add(f);
	}
	
	void addDataX(float [] f) {
		for (int i = dataX.size(); i < f.length; i++) {
			dataX.add(f[i]);
		}
	}
	
	void addDataX(FloatList f) {
		for (int i = dataX.size(); i < f.size(); i++) {
			dataX.add(f.get(i));
		}
	}
	
	void addDataY(float f) {
		dataY.add(f);
	}
	
	void addDataY(float [] f) {
		for (int i = dataY.size(); i < f.length; i++) {
			dataY.add(f[i]);
		}
	}
	
	void addDataY(FloatList f) {
		for (int i = dataY.size(); i < f.size(); i++) {
			dataY.add(f.get(i));
		}
	}
	
	void addData(ArrayList<FloatList> f) {
		data.clear();
		for (int i = 0; i < f.size(); i++) {
			data.add(new FloatList());
			for (int j = 0; j < f.get(i).size(); j++) {
				data.get(i).append(f.get(i).get(j));
			}
		}
	}
	
	@Override
	public void draw() {
		drawGraph();
	}

	void drawBox() {
		outliers = dataX.outliers();
		//wra = wratio;
		//hra = hratio;
		if (dataX.range() > 0) {
			float increment = (range() > 20) ? Math.round(dataX.range() / 20) : dataX.range() / 20;
			float r = w / 2;
			for (int i = 0; i < 21; i++) {
				g.stroke(0);
				int s = 1;
				if ((i) % (4) == 0) {
					g.fill(0);
					String time = PApplet.nfc((float) (((i * increment) + dataX.min()) * .001), 2);
					g.textAlign(CENTER, CENTER);
					g.textFont(font, REGTXTSIZE);
					if (remap((i * increment) + dataX.min(), x, r) > x + r - g.textWidth(time)) {
						g.textAlign(RIGHT, CENTER);
					} else if (i == 0)
						g.textAlign(LEFT, CENTER);
					g.text(time, remap((i * increment) + dataX.min(), x, r), y + 15f);
					s = 2;
				}
				g.strokeWeight(s);
				g.line(remap((i * increment) + dataX.min(), x, r), y + 8, remap((i * increment) + dataX.min(), x, r), y + 6);
			}
		}
	}

	float remap(float n, float x, float w) {
		return(PApplet.map(n, dataX.min(), dataX.max(), (x - w), (x + w)));
	}

	float remap(float n, float shrink, float x, float w) {
		return(PApplet.map(n / shrink, dataX.min(), dataX.max(), (x - w), (x + w)));
	}

	public float range() {
		float range = 0;
		switch(dimension) {
		case DATA_1D:
			range = dataX.max() - dataX.min();
			break;
		case DATA_2D:
			range = dataX.max() - dataX.min();
			break;
		case DATA_3D:
			drawAlternativeGraph();
			break;
		default:
			break;
		}
		return(range);
	}
	
	void drawGraph() {
		switch(dimension) {
		case DATA_1D:
			draw1DGraph();
			break;
		case DATA_2D:
			drawXYGraph();
			break;
		case DATA_3D:
			drawAlternativeGraph();
			break;
		default:
			break;
		}
	}
	
	void draw1DGraph() {
		switch(type) {
		case DOTPLOT:
			dots.draw();
			break;
		case BOXPLOT:
			boxes.draw(dataX.outliers());
			break;
		default:
			break;
		}
	}
	
	void drawXYGraph() {
		
	}
	
	void drawAlternativeGraph() {
		
	}
	
	void init() {
		dots = new DotPlot(p, x, y, p.createFont(REG_SANSS_TXT, REGTXTSIZE), REGTXTSIZE);
		boxes = new BoxPlot(p, x, y, p.createFont(REG_SANSS_TXT, REGTXTSIZE), REGTXTSIZE);
		assignType();
	}
	
	void assignType() {
		switch(dimension) {
		case DATA_1D:
			if (type == null || type.d() != 1) {
				// If type has not yet been assigned or the number of dimensions for type is not 1
				// Because the dimensionality of the graph and that of the plot must match
				type = GraphType.DOTPLOT;
			}
			break;
		case DATA_2D:
			if (type == null || type.d() != 2) {
				// If typue has not yet been assigned or the number of dimensions for type is not 2
				type = GraphType.SCATTERPLOT;
			}
			break;
		case DATA_3D:
			// TBD
			break;
		default:
			break;
		}
	}
}
