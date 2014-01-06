package polargraph.test;

import java.awt.geom.Rectangle2D;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import polargraph.Polargraph;
import polargraph.comms.IncomingEventHandler;

public class TestIncomingEventHandler {

	public Polargraph machine = null;
	
	@Before
	public void setupMachine() {
		this.machine = new Polargraph(10, 10, new Rectangle2D.Float(1,1,8,8), 1.0f);
	}
	
	@Test
	public void testConstructor() {
		IncomingEventHandler ieh = new IncomingEventHandler(this.machine);
		Polargraph m = ieh.getMachine();
		assertEquals(m, this.machine);
	}
	
}
