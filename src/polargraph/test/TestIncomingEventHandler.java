package polargraph.test;

import geomerative.RPoint;

import java.awt.geom.Rectangle2D;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import polargraph.Polargraph;
import polargraph.comms.IncomingEventHandler;

public class TestIncomingEventHandler {

	public Polargraph machine = null;
	private IncomingEventHandler ieh = null;
	
	@Before
	public void setupMachine() {
		this.machine = new Polargraph(10, 10, new Rectangle2D.Float(1,1,8,8), 1.0f);
		this.ieh  = new IncomingEventHandler(this.machine);
	}
	
	@Test
	public void testConstructor() {
		this.ieh = new IncomingEventHandler(this.machine);
		Polargraph m = this.ieh.getMachine();
		assertEquals(m, this.machine);
	}
	
	@Test
	public void testParseHardwareVersionFromReady() {
		assertEquals(Polargraph.HARDWARE_VER_UNO, this.ieh.parseHardwareVersionFromReady("READY"));
		assertEquals(Polargraph.HARDWARE_VER_MEGA, this.ieh.parseHardwareVersionFromReady("READY_100"));
		assertEquals(Polargraph.HARDWARE_VER_MEGA_POLARSHIELD, this.ieh.parseHardwareVersionFromReady("READY_200"));
		
		// some nonsense that should be getting read as defaults
		assertEquals(Polargraph.HARDWARE_VER_UNO, this.ieh.parseHardwareVersionFromReady("READY_123"));
		assertEquals(Polargraph.HARDWARE_VER_UNO, this.ieh.parseHardwareVersionFromReady("READY_201"));
		
		// values that aren't numbers
		assertEquals(Polargraph.HARDWARE_VER_UNO, this.ieh.parseHardwareVersionFromReady("READY_199.9"));
		assertEquals(Polargraph.HARDWARE_VER_UNO, this.ieh.parseHardwareVersionFromReady("READY_BANANA"));
	}
	
	@Test
	public void testParseSync() {
		RPoint p = this.ieh.parseSync("SYNC,5,5,END");
		RPoint p2 = new RPoint(5,0);
		assertTrue(p.x == p2.x && p.y == p2.y);

		p = this.ieh.parseSync("SYNC,10,10,END");
		p2 = new RPoint(5,8.660254038);
		assertTrue(p.x == p2.x && p.y == p2.y);

		p = this.ieh.parseSync("SYNC,14.14213562,10,END");
		p2 = new RPoint(10,10);
		assertTrue(p.x == p2.x && p.y == p2.y);

		p = this.ieh.parseSync("SYNC,10,14.14213562,END");
		p2 = new RPoint(0,10);
		System.out.println("P: " + p.x + ", " + p.y);
		System.out.println("P2: " + p2.x + ", " + p2.y);
		assertTrue(p.x == p2.x && p.y == p2.y);
		
	}
	
}
