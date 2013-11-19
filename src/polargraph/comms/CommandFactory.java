package polargraph.comms;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory extends Command {
	
	// Basic command decoration
	public static final String SEP = ",";
	public static final String SUFFIX = "END";
	
	// names for different types
	public static final String T_INT = "<INT>";
	public static final String T_NUM = "<NUMBER>";
	public static final String T_SNUM = "<SIGNED_NUMBER>";
	public static final String T_STR = "<STRING>";
	
	// Patterns to match the types of parameters
	public static final String P_INT = "\\d+";
	public static final String P_NUM = "\\d*\\.?\\d+";
	public static final String P_SNUM = "[-+]?\\d*\\.?\\d+";
	public static final String P_STR = "\\w+";
	public static final String P_SEP = ", +";
	public static final String P_SUFFIX = SUFFIX;

	public static final Map<String, String> patterns;
	static {
		patterns = new HashMap<String, String>();
		patterns.put(T_INT, P_INT);
		patterns.put(T_NUM, P_NUM);
		patterns.put(T_SNUM, P_SNUM);
		patterns.put(T_STR, P_STR);
		patterns.put(SEP, P_SEP);
		patterns.put(SUFFIX, P_SUFFIX);
	}
	
	// Commands
	public static final String CMD_CHANGELENGTH = "C01";
	public static final String CMD_SET_PEN_WIDTH = "C02";
	public static final String CMD_DRAW_PIXEL = "C05";
	public static final String CMD_DRAW_SCRIBBLE_PIXEL = "C06";
	public static final String CMD_DRAW_RECT = "C07";
	public static final String CMD_SET_DRAWING_DIRECTION = "C08,";
	public static final String CMD_SET_POSITION = "C09";
	public static final String CMD_TEST_PATTERN = "C10";
	public static final String CMD_TEST_PEN_WIDTH_SQUARE = "C11";
	public static final String CMD_TEST_PEN_WIDTH_SCRIBBLE = "C12";
	public static final String CMD_PEN_DOWN = "C13";
	public static final String CMD_PEN_UP = "C14";
	public static final String CMD_DRAW_SAW_PIXEL = "C15";
	public static final String CMD_DRAW_ROUND_PIXEL = "C16";
	public static final String CMD_CHANGELENGTHDIRECT = "C17";
	public static final String CMD_STARTROVE = "C19";
	public static final String CMD_STOPROVE = "C20";
	public static final String CMD_SET_ROVE_AREA = "C21";
	public static final String CMD_LOAD_IMAGE_FILE = "C23";
	public static final String CMD_SET_MACHINE_SIZE = "C24";
	public static final String CMD_SET_MACHINE_NAME = "C25";
	public static final String CMD_REQUEST_MACHINE_SIZE = "C26";
	public static final String CMD_RESET_MACHINE = "C27";
	public static final String CMD_DRAW_DIRECTION_TEST = "C28";
	public static final String CMD_SET_MACHINE_MM_PER_REV = "C29";
	public static final String CMD_SET_MACHINE_STEPS_PER_REV = "C30";
	public static final String CMD_SET_MOTOR_SPEED = "C31";
	public static final String CMD_SET_MOTOR_ACCEL = "C32";
	public static final String CMD_MACHINE_MODE_STORE_COMMANDS = "C33";
	public static final String CMD_MACHINE_MODE_EXEC_FROM_STORE = "C34";
	public static final String CMD_MACHINE_MODE_LIVE = "C35";
	public static final String CMD_RANDOM_DRAW = "C36";
	public static final String CMD_SET_MACHINE_STEP_MULTIPLIER = "C37";
	public static final String CMD_START_TEXT = "C38";
	public static final String CMD_DRAW_SPRITE = "C39";
	public static final String CMD_CHANGE_LENGTH_RELATIVE = "C40";
	public static final String CMD_SWIRLING = "C41";
	public static final String CMD_DRAW_RANDOM_SPRITE = "C42";
	public static final String CMD_DRAW_NORWEGIAN = "C43";
	public static final String CMD_DRAW_NORWEGIAN_OUTLINE = "C44";
	public static final String CMD_SETPENLIFTRANGE = "C45";
	public static final String CMD_SELECT_ROVE_SOURCE_IMAGE = "C46";
	public static final String CMD_RENDER_ROVE = "C47";
	
	// Initialise the collection of available commands
	public static final Map<String, CommandDefinition> cmds;
	static {
		cmds = new HashMap<String, CommandDefinition>(50);
		
		cmds.put(CMD_CHANGELENGTH, new CommandDefinition(CMD_CHANGELENGTH, new String[]{"a", "b"}, new String[]{T_NUM, T_NUM}));
		cmds.put(CMD_SET_PEN_WIDTH, new CommandDefinition(CMD_SET_PEN_WIDTH, new String[]{"penWidth"}, new String[]{T_SNUM}));
		
		cmds.put(CMD_DRAW_PIXEL, new CommandDefinition(CMD_DRAW_PIXEL, new String[]{"aOrigin", "bOrigin", "size", "density"}, new String[]{T_NUM, T_NUM, T_NUM, T_NUM}));
		cmds.put(CMD_DRAW_SCRIBBLE_PIXEL, new CommandDefinition(CMD_DRAW_SCRIBBLE_PIXEL, new String[]{"aOrigin", "bOrigin", "size", "density"}, new String[]{T_NUM, T_NUM, T_NUM, T_NUM}));
		cmds.put(CMD_DRAW_RECT, new CommandDefinition(CMD_DRAW_RECT, new String[]{"aTopLeft", "bTopLeft", "aBotRight", "bBotRight"}, new String[]{T_NUM, T_NUM, T_NUM, T_NUM}));
		cmds.put(CMD_SET_DRAWING_DIRECTION, new CommandDefinition(CMD_SET_DRAWING_DIRECTION, new String[]{"drawDir"}, new String[]{T_INT}));
		
		cmds.put(CMD_SET_POSITION, new CommandDefinition(CMD_SET_POSITION, new String[]{"a", "b"}, new String[]{T_NUM, T_NUM}));
		cmds.put(CMD_TEST_PATTERN, new CommandDefinition(CMD_TEST_PATTERN, new String[]{"size", "startWidth", "endWidth", "incSize"}, new String[]{T_NUM, T_NUM, T_NUM, T_NUM}));
		
		cmds.put(CMD_PEN_DOWN, new CommandDefinition(CMD_PEN_DOWN, new String[]{"pos"}, new String[]{T_INT}));
		cmds.get(CMD_PEN_DOWN).setOptional(true, false, false, false);
		cmds.put(CMD_PEN_UP, new CommandDefinition(CMD_PEN_UP, new String[]{"pos"}, new String[]{T_INT}));
		cmds.get(CMD_PEN_UP).setOptional(true, false, false, false);
		cmds.put(CMD_DRAW_SAW_PIXEL, new CommandDefinition(CMD_DRAW_SAW_PIXEL, new String[]{"aOrigin", "bOrigin", "size", "density"}, new String[]{T_NUM, T_NUM, T_NUM, T_NUM}));
		cmds.put(CMD_DRAW_ROUND_PIXEL, new CommandDefinition(CMD_DRAW_ROUND_PIXEL, new String[]{"aOrigin", "bOrigin", "size", "density"}, new String[]{T_NUM, T_NUM, T_NUM, T_NUM}));
		
		cmds.put(CMD_CHANGELENGTHDIRECT, new CommandDefinition(CMD_CHANGELENGTHDIRECT, new String[]{"x", "y"}, new String[]{T_NUM, T_NUM}));
		cmds.put(CMD_STARTROVE, new CommandDefinition(CMD_STARTROVE));
		cmds.put(CMD_STOPROVE, new CommandDefinition(CMD_STOPROVE));
		
		cmds.put(CMD_SET_ROVE_AREA, new CommandDefinition(CMD_SET_ROVE_AREA, new String[]{"xTopLeft", "yTopLeft", "xBotRight", "yBotRight"}, new String[]{T_NUM, T_NUM, T_NUM, T_NUM}));
		cmds.put(CMD_LOAD_IMAGE_FILE, new CommandDefinition(CMD_LOAD_IMAGE_FILE, new String[]{"filename"}, new String[]{T_STR}));
		cmds.put(CMD_SET_MACHINE_SIZE, new CommandDefinition(CMD_SET_MACHINE_SIZE, new String[]{"width", "height"}, new String[]{T_NUM, T_NUM}));
		cmds.put(CMD_SET_MACHINE_NAME, new CommandDefinition(CMD_SET_MACHINE_NAME, new String[]{"name"}, new String[]{T_STR}));
		
		cmds.put(CMD_REQUEST_MACHINE_SIZE, new CommandDefinition(CMD_REQUEST_MACHINE_SIZE));
		cmds.put(CMD_RESET_MACHINE, new CommandDefinition(CMD_RESET_MACHINE));
		cmds.put(CMD_DRAW_DIRECTION_TEST, new CommandDefinition(CMD_DRAW_DIRECTION_TEST));
		cmds.put(CMD_SET_MACHINE_MM_PER_REV, new CommandDefinition(CMD_SET_MACHINE_MM_PER_REV, new String[]{"unitsPerRev"}, new String[]{T_NUM}));
		
		cmds.put(CMD_SET_MACHINE_STEPS_PER_REV, new CommandDefinition(CMD_SET_MACHINE_STEPS_PER_REV, new String[]{"stepsPerRev"}, new String[]{T_NUM}));
		cmds.put(CMD_SET_MOTOR_SPEED, new CommandDefinition(CMD_SET_MOTOR_SPEED, new String[]{"maxSpeed"}, new String[]{T_NUM}));
		cmds.put(CMD_SET_MOTOR_ACCEL, new CommandDefinition(CMD_SET_MOTOR_ACCEL, new String[]{"acceleration"}, new String[]{T_NUM}));
		cmds.put(CMD_MACHINE_MODE_STORE_COMMANDS, new CommandDefinition(CMD_MACHINE_MODE_STORE_COMMANDS, new String[]{"filename"}, new String[]{T_STR}));
		
		cmds.put(CMD_MACHINE_MODE_EXEC_FROM_STORE, new CommandDefinition(CMD_MACHINE_MODE_EXEC_FROM_STORE, new String[]{"filename"}, new String[]{T_STR}));
		cmds.put(CMD_MACHINE_MODE_LIVE, new CommandDefinition(CMD_MACHINE_MODE_LIVE));
		cmds.put(CMD_RANDOM_DRAW, new CommandDefinition(CMD_RANDOM_DRAW));
		cmds.put(CMD_SET_MACHINE_STEP_MULTIPLIER, new CommandDefinition(CMD_SET_MACHINE_STEP_MULTIPLIER, new String[]{"multiplier"}, new String[]{T_INT}));
		
		cmds.put(CMD_START_TEXT, new CommandDefinition(CMD_START_TEXT, new String[]{"a", "b"}, new String[]{T_NUM, T_NUM}));
		cmds.put(CMD_DRAW_SPRITE, new CommandDefinition(CMD_DRAW_SPRITE, new String[]{"size", "filename"}, new String[]{T_NUM, T_STR}));
		cmds.put(CMD_CHANGE_LENGTH_RELATIVE, new CommandDefinition(CMD_CHANGE_LENGTH_RELATIVE, new String[]{"a", "b"}, new String[]{T_SNUM, T_SNUM}));
		cmds.put(CMD_SWIRLING, new CommandDefinition(CMD_SWIRLING));
		
		cmds.put(CMD_DRAW_RANDOM_SPRITE, new CommandDefinition(CMD_DRAW_RANDOM_SPRITE));
		cmds.put(CMD_DRAW_NORWEGIAN, new CommandDefinition(CMD_DRAW_NORWEGIAN));
		cmds.put(CMD_DRAW_NORWEGIAN_OUTLINE, new CommandDefinition(CMD_DRAW_NORWEGIAN_OUTLINE));
		cmds.put(CMD_SETPENLIFTRANGE, new CommandDefinition(CMD_SETPENLIFTRANGE));
		
		cmds.put(CMD_SELECT_ROVE_SOURCE_IMAGE, new CommandDefinition(CMD_SELECT_ROVE_SOURCE_IMAGE));
		cmds.put(CMD_RENDER_ROVE, new CommandDefinition(CMD_RENDER_ROVE));
	}
	
	private CommandFactory(){
		super();
	}
	
	/*
	 * Command maker.  It creates a new Command, with the definition loaded.
	 */
	public static Command getCommand(String name) {
		if (cmds.keySet().contains(name)) {
			Command c = new Command(cmds.get(name));
			return c;
		}
		else
			throw new RuntimeException ("No command found for " + name);
	}
}
