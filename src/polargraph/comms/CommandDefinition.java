package polargraph.comms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class CommandDefinition{

	
	protected String name;
	
	/* Using Lists here rather than maps, because the sequence is important.  */
	private List<String> paramTypes = null;
	private List<String> paramNames = null;
	private List<String> paramPatts = null;
	private List<Boolean> optionalParams = null;

	/* Regex that the finished, populated command must match in order to be valid. */
	private Pattern pattern;
	
	
	/*
	 * No-access constructor!
	 */
	private CommandDefinition() {}
	
	/*
	 * Constructor that takes name, and a list each of command parameter NAMES
	 * and TYPES.
	 */
	protected CommandDefinition(String name, String[] paramNames, String[] paramTypes) {
		this(name);
		for (int i = 0; i<paramNames.length; i++) {
			this.paramNames.add(paramNames[i]);
			this.paramTypes.add(paramTypes[i]);
		}
	}
	/* 
	 * NAme-only constructor - used when the command has no parameters
	 */
	protected CommandDefinition(String name) {
		this.name = name;
		this.paramTypes = new ArrayList<String>(4);
		this.paramNames = new ArrayList<String>(4);
		this.setOptional(false, false, false, false);
		this.pattern = null;
	}
	
	/*
	 * Builds and compiles a regex that will check this command for validity.
	 * @return
	 */
	private void buildPattern() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getName());
		for (int i = 0; i < this.getMaxNumberOfParams(); i++) {
			if (this.optionalParams.get(i)) sb.append("(");
			sb.append(CommandFactory.patterns.get(CommandFactory.SEP))
				.append(CommandFactory.patterns.get(this.getParamTypes().get(i)));
			if (this.optionalParams.get(i)) sb.append(")?");
		}
		sb.append(CommandFactory.patterns.get(CommandFactory.SEP))
		  .append(CommandFactory.patterns.get(CommandFactory.SUFFIX));
		Pattern p = Pattern.compile(sb.toString()); 
		this.pattern = p;
	}
	
	public Pattern getPattern() {
		if (this.pattern == null)
			this.buildPattern();
		return this.pattern;
	}

	/*
	 * Use to set which params are marked as optional.
	 */
	public void setOptional(boolean b, boolean c, boolean d, boolean e) {
		this.optionalParams = Arrays.asList(b, c, d, e);
	}
	public List<Boolean> getOptionalParams() {
		return this.optionalParams;
	}
	
	/*
	 * Getter for name.
	 */
	public String getName() {
		return this.name;
	}
	
	/*
	 * Getter for paramTypes
	 */
	public List<String> getParamTypes() {
		return this.paramTypes;
	}
	/*
	 * Getter for paramNames
	 */
	public List<String> getParamNames() {
		return this.paramNames;
	}
	
	public int getMaxNumberOfParams() {
		return this.paramNames.size();
	}
	/*
	 * Returns the minimum number of params that the command may have
	 */
	public int getMinNumberOfParams() {
		return this.getMaxNumberOfParams() - this.getNumberOfOptionalParams();
	}
	
	/* Returns the number of optional params that the command has
	 */
	public int getNumberOfOptionalParams() {
		int optionalParams = 0;
		for (int i = 0; i < this.optionalParams.size(); i++ ) {
			if (this.optionalParams.get(i))
				optionalParams++;
		}
		return optionalParams;
	}

	public Pattern getPatternForNumberOfParameters(int size) {
		return null;
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
		Map<String, String> map = new HashMap<String, String>(4);
		for (int i=0; i<this.getParamNames().size(); i++) {
			map.put(this.getParamNames().get(i), this.getParamTypes().get(i));
		}
		return map;
	}	
}
