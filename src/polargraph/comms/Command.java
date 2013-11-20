package polargraph.comms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command class that represents a Polargraph command.  
 * It takes a CommandDefinition as a constructor parameter, and has methods for adding and modifying parameters,
 * as well as checking for validity and returning a compiled "stringified" form.
 *   
 * 
 */
public class Command {
	
	/* The definition object that this command will be based on. */
	private CommandDefinition def = null;
	
	/* List representing up to four parameters */
	private Map<String, Object> params = new HashMap<String, Object>(4);
	
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
		if (this.params.size() > def.getMaxNumberOfParams())
			return false;
		else if (this.params.size() < def.getMinNumberOfParams())
			return false;
		else if (!this.def.getPattern().matcher(this.toString()).matches()) {
			return false;
		}
		return valid;
	}
	
	/* 
	 * Probably shouldn't make this public, but its been handy in debugging, so, well I trust you guys.
	 * @return The command definition that this command has been loaded with.
	 */
	public CommandDefinition getDefinition() {
		return this.def;
	}
	
	/** Returns a string form of the command, if it is valid
	 * 
	 * Throws a RuntimeException if invalid
	 */
	public String getAsCommandString() {
		if (this.isValid())
			return this.toString();
		else
			throw new RuntimeException("Command is not valid.");
	}
	
	/*
	 * Concatenates and returns a stringified version of this command.
	 * This also rounds numbers to int or to decimal places.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.def.getName());
		
		// Build the command in the proper parameter order
		for (int i = 0; i < this.def.getParamNames().size(); i++) {
			String name = this.def.getParamNames().get(i);
			Object value = null;

			// check and see if the parameter has been loaded
			if (this.params.containsKey(name))
				value = this.params.get(name);
			else // doesn't contain the key! Check if the parameter is optional, and skip over it if it is 
				if (this.def.getOptionalParams().get(i)) continue;
			
			
			sb.append(CommandFactory.SEP);
			if (CommandFactory.T_INT.equals(this.def.getParamTypes().get(i))) {
				// cast to int
				sb.append((Integer) value);
			}
			else if (CommandFactory.T_SNUM.equals(this.def.getParamTypes().get(i))) {
				// also cast to int
				sb.append((Integer) value);
			}
			else if (CommandFactory.T_NUM.equals(this.def.getParamTypes().get(i))) {
				// deal with decimal numbers
				Float pf = (Float) value;
				String formatted = String.format("%.4f", value);
				sb.append(formatted);
			}
			else {
				sb.append(value.toString());
			}
		}
		sb.append(CommandFactory.SEP)
		  .append(CommandFactory.SUFFIX);
		
		return sb.toString();
	}
	
	/**
	 * Returns a map of parameter names and types (type as a regex).  The keyset can be used to find out
	 * what commands are necessary to supply, and the values can be used to validate the parameter
	 * values.
	 * 
	 * In practice, you will know from looking at the CommandDefinition what the 
	 * @return
	 */
	public Map<String, String> getParamDefinitions() {
		return this.def.getParamDefinitions();
	}
	
	/**
	 * Add a particular named parameter to the command.
	 *  
	 * @param paramName
	 * @param paramValue
	 */
	public Command addParam(String paramName, String paramValue) {
		if (this.def.getParamNames().contains(paramName)) {
			int paramIndex = this.def.getParamNames().indexOf(paramName);
			if (CommandFactory.T_STR.equals(this.def.getParamTypes().get(paramIndex))) {
					this.params.put(paramName, paramValue);
			}
			else 
				throw new IllegalArgumentException("Parameter " + paramName + " must be a positive number.");
		}
		else {
			throw new RuntimeException("Parameter '" + paramName 
				+ "' is not valid for this command type - must be in list: " 
				+ this.def.getParamNames() + ").");
		}
		return this;
	}

	public Command addParam(String paramName, Number paramValue) {
		if (this.def.getParamNames().contains(paramName)) {
			int paramIndex = this.def.getParamNames().indexOf(paramName);

			// and check the type is ok too
			// T_NUM should be a positive number (including decimal)
			if (CommandFactory.T_NUM.equals(this.def.getParamTypes().get(paramIndex)) && paramValue.intValue() >= 0) {
				if (paramValue instanceof Float || paramValue instanceof Double)
					this.params.put(paramName, paramValue.doubleValue());
				else
					this.params.put(paramName, paramValue.intValue());
			}
			// T_INT should be a positive int
			else if (CommandFactory.T_INT.equals(this.def.getParamTypes().get(paramIndex)) && paramValue.intValue() >= 0) {
				this.params.put(paramName, paramValue.intValue());
			}
			// T_SNUM should be a signed number (inc decimal)
			else if (CommandFactory.T_SNUM.equals(this.def.getParamTypes().get(paramIndex))) {
				if (paramValue instanceof Float || paramValue instanceof Double)
					this.params.put(paramName, paramValue.doubleValue());
				else
					this.params.put(paramName, paramValue.intValue());
			}
			else 
				throw new IllegalArgumentException("Parameter " + paramName + " must be a number.");
			
		}
		else {
			throw new IllegalArgumentException("Parameter '" + paramName 
				+ "' is not valid for this command type - must be in list: " 
				+ this.def.getParamNames() + ").");
		}
		
		return this;
	}
	public String getName() {
		return this.def.getName();
	}
	
}
