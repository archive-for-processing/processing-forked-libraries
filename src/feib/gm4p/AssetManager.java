package feib.gm4p;
import java.io.File;
import java.util.HashMap;
import processing.core.*;

/*==================================================================================================
AssetManager
==================================================================================================*/
public class AssetManager {

	HashMap<String, PImage> images;
	PApplet p;

	// Constructor
	public AssetManager(PApplet applet) {
		p = applet;
		images = new HashMap<String, PImage>();
		loadAllImages();
		
		if (GM.debugMode) 
			PApplet.println("AssetManager created:" + this.toString());
	}

	void add(String imageFileName) {
		PImage newImage = GM.p.loadImage(imageFileName);
		images.put(imageFileName, newImage);       
	}

	public PImage get(String imageName) {
		if (images.containsKey(imageName)) 
			return images.get(imageName);

		return null;
	}

	void loadAllImages() {

		File dataFolder = new File(p.dataPath(""));
		System.out.println(dataFolder);
		File [] files = dataFolder.listFiles();

		for (File file : files)
			if (file.getName().toLowerCase().endsWith(".png"))
				add(file.getName());
	}
}
