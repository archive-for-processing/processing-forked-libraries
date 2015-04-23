import manoloide.ImageProcessor.ImageProcessor;

ImageProcessor ip;
void setup() {
	size(600, 600);
	colorMode(HSB, 255, 255, 255);
	for (int j = 0; j < height; j++) {
		for (int i = 0; i < width; i++) {
			color col = color(i*1./height*255, 255, 255);
			set(i, j, col);
		}
	}
	
	ip = new ImageProcessor(this);
	ip.vignette(1);
}
