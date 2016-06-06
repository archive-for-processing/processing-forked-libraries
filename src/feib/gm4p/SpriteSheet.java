package feib.gm4p;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import feib.gm4p.*;
import processing.core.*;

public class SpriteSheet {

	// The image containing the frames and the image to draw
	PImage sourceImage, drawImage;
	int fps = 8;
	int updateInterval;  
	int framesDrawn = 0;
	int frame = 0;
	public int frameWidth;
	public int frameHeight;
	int nFrames = 0;
	int nx, ny;
	boolean loopAnim = true, ended = false;

	// Constructor takes name of source image and the amount of frames 
	public SpriteSheet(String imageName) {
		sourceImage = GM.assetManager.get(imageName);
		updateInterval = 30/fps;

		parseFileName(imageName);
		drawImage = new PImage();
		drawImage.width = frameWidth;
		drawImage.height = frameHeight;
		
		if (GM.debugMode) 
			PApplet.println("SpriteSheet created:" + this.toString());
	}

	void parseFileName(String fileName) {
		Pattern p = Pattern.compile(".*@X(\\d*)N(\\d*).png");
		Matcher matcher = p.matcher(fileName);

		while (matcher.find ()) {
			nx = Integer.parseInt(matcher.group(1));
			nFrames = Integer.parseInt(matcher.group(2));
		}

		ny = nFrames/nx + ((nFrames%nx>0) ? 1 : 0);
		frameWidth = sourceImage.width/nx;
		frameHeight = sourceImage.height/ny;
	}

	// update() selects the image to draw based on fps and frames already drawn
	public void update() {
		if (!ended) {
			if ((GM.p.frameCount % updateInterval) == 0) {    
				frame =  (frame + 1) % nFrames;
			}

			framesDrawn++;

			if ((framesDrawn > nFrames) && (loopAnim == false))
				ended = true;
		}
	}

	// draw the target image
	public void draw(PGraphics ctx) {
		ctx.copy(sourceImage, 
				(frame%nx)*frameWidth, (frame/nx)*frameHeight, frameWidth, frameHeight, 
				-frameWidth, -frameHeight, frameWidth, frameHeight);
	}
}