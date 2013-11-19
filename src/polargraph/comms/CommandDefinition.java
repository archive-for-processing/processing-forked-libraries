package polargraph.comms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class CommandDefinition{

	protected String name;
	private List<String> paramTypes = null;
	private List<String> paramNames = null;
	private List<String> paramPatts = null;
	private Boolean[] optionalParams = null;
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
		this.buildPattern();
	}
	
	/**
	 * Builds and compiles a regex that will check this command for validity.
	 * @return
	 */
	private void buildPattern() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getName());
		for (int i = 0; i < this.getNumberOfParams(); i++) {
			sb.append(CommandFactory.patterns.get(CommandFactory.SEP))
			  .append(CommandFactory.patterns.get(this.getParamTypes().get(i)));
		}
		sb.append(CommandFactory.patterns.get(CommandFactory.SEP))
		  .append(CommandFactory.patterns.get(CommandFactory.SUFFIX));
		
		String regex = sb.toString();
		Pattern p = Pattern.compile(regex); 
		this.pattern = p;
	}
	
	public Pattern getPattern() {
		return this.pattern;
	}

	/*
	 * Use to set which params are marked as optional.
	 */
	public void setOptional(boolean b, boolean c, boolean d, boolean e) {
		this.optionalParams = new Boolean[] {b, c, d, e};
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
	
	public int getNumberOfParams() {
		return this.paramNames.size();
	}
	
}
