package feib.gm4p;
import processing.core.*;

public class SpriteGameObject extends GameObject {	
	private PImage spriteImage;
	private SpriteSheet spriteSheet;
	public float angle;
	public PVector origin, dimension;

	public SpriteGameObject(String imageName){
		super();
		setImage(imageName);		
		origin = new PVector(0,0);

		if (GM.debugMode) 
			PApplet.println("SpriteGameObject created:" + this.toString());

	}

	public void setImage(String imageName) {


		if (imageName.matches(".*@X(\\d*)N(\\d*).png"))
			spriteSheet = new SpriteSheet(imageName);
		else
			spriteImage = GM.assetManager.get(imageName);    

		if (spriteImage != null)
			dimension = new PVector(spriteImage.width, spriteImage.height);
		else if (spriteSheet != null)
			dimension = new PVector(spriteSheet.frameWidth, spriteSheet.frameHeight);
	}

	void update() {
		if (spriteSheet != null)      
			spriteSheet.update();
	}

	PVector getCenter() {
		return new PVector(position.x + dimension.x/2, position.y + dimension.y/2);
	}

	public void draw(PGraphics ctx) {

		ctx.pushMatrix();
		{
			ctx.translate(position.x, position.y);
			ctx.pushMatrix();
			{
				ctx.rotate(angle);
				ctx.pushMatrix();
				{
					ctx.translate(-origin.x, -origin.y);

					if (spriteImage != null)
						ctx.image(spriteImage, 0, 0);
					else {
						ctx.translate(dimension.x/2, dimension.y/2);
						spriteSheet.draw(ctx);
					}
					ctx.popMatrix();
				}
				ctx.popMatrix();
			}
		}
		ctx.popMatrix();
	}

	void setAngularDirection(PVector angularDirection) {
		angularDirection.normalize();
		angle = PApplet.atan2(angularDirection.y, angularDirection.x);
	}

	public Rectangle getBoundingBox(){
		return new Rectangle(position, dimension);
	};

}
