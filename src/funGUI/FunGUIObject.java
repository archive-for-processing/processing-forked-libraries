package funGUI;
import processing.core.*;

interface FunGUIObject extends PConstants {
	public static final float REGTXTSIZE = 13;
	public static final float SMLTXTSIZE = 10;
	public static final float MEDTXTSIZE = 15;
	public static final float LGTXTSIZE = 20;
	public static final String REG_SERIF_TXT = "Times-New-Roman";
	public static final String REG_SANSS_TXT = "Helvetica";
	public static final String FANCIEST_TXT =  "Baskerville-Italic";
	public static final String LIGHTEST_TXT = "Avenir-Light";
	public static final String LIGHT_TXT = "Avenir-Book";

	public float x();
	public float y();
	public float h();
	public float w();
	public String returnName();
}
