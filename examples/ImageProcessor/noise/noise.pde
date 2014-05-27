import manoloide.ImageProcessor.ImageProcessor;

ImageProcessor ip;
PImage img; 
void setup(){
    img = loadImage("http://media-cache-ec0.pinimg.com/736x/dc/91/84/dc91846a4c7ae6015ecee9f1f4450e87.jpg");
    size(img.width, img.height);
    ip = new ImageProcessor(this);
    PImage pro = ip.noise(img, 0.8, false);
    image(pro, 0, 0);
}
