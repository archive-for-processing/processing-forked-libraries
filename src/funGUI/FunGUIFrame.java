package funGUI;

/**
 * The FunGUIFrame is the less-abstract version of Frame. Both
 * are meant to reserve certain spots for objects. Only this one is
 * meant to be implemented. FunGUIFrames help a FunGUI interface keep track
 * of what goes where. FunGUIFrames are meant to be used in columns/rows,
 * keeping your content neat and orderly.
 * @author audrey
 */
public class FunGUIFrame extends Frame implements FunGUIObject {
	public FunGUIFrame(float x, float y) {
	}

	@Override
	public float x() {
		// TODO Auto-generated method stub
		return(this.x);
	}

	@Override
	public float y() {
		// TODO Auto-generated method stub
		return(this.y);
	}
	
	public String returnName() {
		return("FunGUIFrame");
	}
}
