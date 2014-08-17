package ZstProcessing;

import java.util.Map;

import com.sun.corba.se.impl.orbutil.graph.Node;

import ZST.*;

public class Launcher {
	public static void main(String[] args) {
		Showtime s = new Showtime("javatest", "127.0.0.1:6000");
		s.requestRegisterNode();
		Map<String, ZstPeerlink> peers = s.requestNodePeerlinks();
		System.out.println(peers);
		//s.subscribe(peers.get("LiveNode").getMethod("output_meter");
	}
}
