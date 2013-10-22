package unisonmenu.snapperturtle;

public class SnapperTurtle implements TurtleCommands {
    private boolean penDown=true;
    private String name="default";
    public SnapperTurtle(){
    
    }

    @Override
    public void forward(float size) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void back(float size) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void arcLeft(float arcLength, float widthToHeightRatio) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void arcRight() {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void degrees() {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void radians() {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void left(float angle) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void right(float angle) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void setBearing(float angle) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public float getBearing() {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public boolean penUp() {
	return penDown;
	
    }

    @Override
    public boolean penDown() {
	return penDown;
	
    }

    @Override
    public void togglePen() {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void origin() {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void pushLocation() {
	// TODO Auto-generated method stub
	
    }

    @Override
    public Vector popLocation() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void setLocation(Vector location) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public Vector getLocation() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public void setName(String name) {
	this.name=name;	
    }
}
