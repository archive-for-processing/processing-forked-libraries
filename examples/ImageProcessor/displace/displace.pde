import manoloide.ImageProcessor.ImageProcessor;

ImageProcessor ip;
void setup() {
	size(600, 600);
	generate();
}

void draw(){
}

void keyPressed(){
	generate();
}

void generate(){
	colorMode(HSB, 255, 255, 255);
	for (int j = 0; j < height; j++) {
		for (int i = 0; i < width; i++) {
			color col = color(i*1./height*255, 255, 255);
			set(i, j, col);
		}
	}
	for (int i = 0; i < 100; i++) {
		float x = random(width);
		float y = random(height);
		float t = random(10, 100);
		ellipse(x, y, t, t);
	}

	float dr = random(-5, 5);
	float dg = random(-5, 5);
	float db = random(-5, 5);

	ip = new ImageProcessor(this);
	ip.displace(dr, dg, db);

}
