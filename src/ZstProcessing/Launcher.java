package ZstProcessing;

public class Launcher {
	public static void main(String[] args) {
		Showtime s = new Showtime("javatest", "tcp://curiosity.soad.vuw.ac.nz:6000");
		s.requestRegisterNode();
	}
}
