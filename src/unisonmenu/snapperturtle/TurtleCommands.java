package unisonmenu.snapperturtle;

public interface TurtleCommands{
        
    public void forward(float size);
    public void back(float size);
    
    public void arcLeft(float arcLength, float widthToHeightRatio);
    public void arcRight();
    
    public void degrees();
    public void radians();
    
    public void left(float angle);
    public void right (float angle);    
    
    public void setBearing(float angle);
    public float getBearing();
       
    public boolean penUp();
    public boolean penDown();
    public void togglePen();
            
    public void origin();
    
    public void pushLocation();
    public Vector popLocation();        
    public void setLocation(Vector location);
    public Vector getLocation();
    
    public String getName();
    public void setName(String name);
    
    
    
}
