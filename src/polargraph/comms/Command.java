package polargraph.comms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command {
	private int checksum = 0;
	private CommandDefinition def = null;
	private List<Object> params = new ArrayList<Object>(4);
	
	// default constructor is hidden
	protected Command() {} 
	protected Command(CommandDefinition def) {
		this.def = def;
	}
	
	/*
	 * Checks the types and number of the parameters loaded
	 */
	public boolean isValid() {
		boolean valid = true;
		if (def.getNumberOfParams() != this.params.size())
			return false;
		else {
			Pattern pattern = this.def.getPattern();
			String command = this.toString();
			if (!pattern.matcher(command).matches()) {
				return false;
			}
		}
		return valid;
	}
	
	public String getAsCommand() {
		if (this.isValid())
			return this.toString();
		else
			throw new RuntimeException("Command is not valid.");
	}
	
	/**
	 * Concatenates and returns a stringified version of this command.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.def.getName());
		for (int i = 0; i < this.def.getNumberOfParams(); i++) {
			Object p = this.params.get(i);
			
			sb.append(CommandFactory.SEP);
			if (CommandFactory.T_INT.equals(this.def.getParamTypes().get(i))) {
				// cast to int
				sb.append((Integer) p);
			}
			else if (CommandFactory.T_SNUM.equals(this.def.getParamTypes().get(i))) {
				// also cast to int
				sb.append((Integer) p);
			}
			else if (CommandFactory.T_NUM.equals(this.def.getParamTypes().get(i))) {
				// deal with decimal numbers
				Float pf = (Float) p;
				String formatted = String.format("%.3f", p);
				sb.append(formatted);
			}
			else {
				sb.append(p.toString());
			}
		}
		sb.append(CommandFactory.patterns.get(CommandFactory.SEP))
		  .append(CommandFactory.patterns.get(CommandFactory.SUFFIX));
		
		return sb.toString();
	}
	
}
