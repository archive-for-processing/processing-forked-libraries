package polargraph.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import polargraph.comms.Command;
import polargraph.comms.CommandFactory;

public class TestCommand {

	private Command noParamsOptionalInt = null;
	
	@Before
	public void createCommands() {
		this.noParamsOptionalInt = CommandFactory.newCommand(CommandFactory.CMD_PEN_UP);
	}
	
	@After
	public void clearCommands() {
		this.noParamsOptionalInt = null;
	}
	

	/* Test adding params */
	
	@Test
	public void testIsValid_noParamsOptional() {
		assertTrue(this.noParamsOptionalInt.isValid());
	}
	@Test
	public void testIsValid_singleOptionalInt() {
		this.noParamsOptionalInt.addParam("pos", 180);
		assertTrue(this.noParamsOptionalInt.isValid());
	}
	@Test
	public void testIsValid_singleOptionalInt_float() {
		this.noParamsOptionalInt.addParam("pos", 180.1234f);
		assertTrue(this.noParamsOptionalInt.isValid());
	}
	@Test(expected=IllegalArgumentException.class)
	public void testIsValid_singleOptionalInt_negNumber() {
		this.noParamsOptionalInt.addParam("pos", -180.1234f);
		assertTrue(this.noParamsOptionalInt.isValid());
	}
	@Test(expected=IllegalArgumentException.class)
	public void testIsValid_singleOptionalInt_wrongParamName() {
		this.noParamsOptionalInt.addParam("testing_is_amazing", 180);
		assertTrue(this.noParamsOptionalInt.isValid());
	}
	@Test(expected=IllegalArgumentException.class)
	public void testAddParam_StringInsteadOfNumber() {
		this.noParamsOptionalInt.addParam("pos", "abc");
	}

	/* Test Creating commands */
	@Test
	public void testConstructor() {
		Command c = CommandFactory.newCommand(CommandFactory.CMD_CHANGELENGTH);
		assertTrue(c instanceof Command);
		assertEquals(c.getName(), CommandFactory.CMD_CHANGELENGTH);
	}	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructor_badCommand() {
		Command c = CommandFactory.newCommand("Z"+CommandFactory.CMD_CHANGELENGTH);
	}	
	
	@Test
	public void testGetAsCommandStringNoParam() {
		assertEquals(this.noParamsOptionalInt.toString(),
				"C14,END");
	}
	@Test
	public void testGetAsCommandString() {
		assertNotEquals(this.noParamsOptionalInt.toString(),
				"C13,END");
	}

	@Test
	public void testToStringNoParam() {
		assertEquals(this.noParamsOptionalInt.toString(),
				"C14,END");
	}
	@Test
	public void testToStringWrongCommand() {
		assertNotEquals(this.noParamsOptionalInt.toString(),
				"C13,END");
	}

}
