package polargraph;

import geomerative.RPoint;

/**
 * This is a kind of generic polargraph marking tool. In the absence of any other distinct
 * physical kinds, it's like a servo-lifted pen gondola.
 * 
 * The position is 
 * @author sandy_000
 *
 */
public class PolargraphTool {
	
	
	private Integer minDepth = 0;
	private Integer maxDepth = 255;
	private Integer markingDepth = 0; 

	private RPoint pos = new RPoint(0, 0);
	private Integer depth = 255;
	
	// This is a link to the parent machine, 
	private Polargraph parentMachine;

	public void setPosition(RPoint pos) {
		this.pos = pos;
	};
	public void setMarkingOn() {
		this.depth = this.minDepth;
	};
	public void setMarkingOff() {
		this.depth = this.maxDepth;
	};
	public RPoint getPosition() {
		return this.pos;
	};
	public Boolean isMarking() {
		return this.depth >= this.markingDepth; 
	}
	public void setParentMachine(Polargraph parentMachine) {
		this.parentMachine = parentMachine;
	};
}
