package comms;

import java.util.ArrayList;
import java.util.List;

public class CommandDefinition {
	protected String name;
	private List<String> paramPatterns = new ArrayList<String>(4);
	private Boolean[] optionalParams = {false, false, false, false};
	
	public CommandDefinition(String name) {
		this.name = name;
	}
	public CommandDefinition(String name, String p1) {
		this(name);
		this.paramPatterns.add(0, p1);
	}
	public CommandDefinition(String name, String p1, String p2) {
		this(name, p1);
		this.paramPatterns.add(1, p2);
	}
	public CommandDefinition(String name, String p1, String p2, String p3) {
		this(name, p1, p2);
		this.paramPatterns.add(2, p3);
	}
	public CommandDefinition(String name, String p1, String p2, String p3, String p4) {
		this(name, p1, p2, p3);
		this.paramPatterns.add(3, p4);
	}

	public String getName() {
		return this.name;
	}
	public List<String> getParamPatterns() {
		return this.paramPatterns;
	}
	public int getNumberOfParams() {
		return this.paramPatterns.size();
	}
	
	public void setOptional(boolean b, boolean c, boolean d, boolean e) {
		this.optionalParams[0] = b;
		this.optionalParams[1] = c;
		this.optionalParams[2] = d;
		this.optionalParams[3] = e;
	}
}
