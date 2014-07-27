package ZstProcessing;

public class Launcher {
	public static void main(String[] args) {
		Showtime s = new Showtime("teSGsd", "tcp://curiosity.soad.vuw.ac.nz:6000");
		s.requestRegisterNode();
	}
}
