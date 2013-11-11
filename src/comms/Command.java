package comms;

import java.util.ArrayList;
import java.util.List;

public class Command {
	private String name;
	private List<Object> params = new ArrayList<Object>(4);
	private String suffix = CommandStatics.SUFFIX;
	private String sep = CommandStatics.SEP;
	private int checksum = 0;
	
	public Command(String name, List<Object> params) {
		this.name = name;
		this.params = params;
	}
	
	public String toString() {
		StringBuilder c = new StringBuilder();
		c.append(this.name).append(this.sep);
		for (Object p : this.params)
			c.append(p.toString()).append(this.sep);
		c.append(this.suffix);
		return c.toString();
	}
}
