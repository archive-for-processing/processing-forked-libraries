package triss.colourlovers;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class ColourLoversPalette {
	// just required to get at Processing's unhex command	
	PApplet parent;
	
	// fields for some of the colour lover's palette info
	private int id;
	private String title;
	private String userName;
	private int numViews;
	private int numVotes;
	private int numComments;
	private int numHearts;
	
	int colours[];
	
	public ColourLoversPalette(JSONObject json, PApplet theParent) {
		parent = theParent;
		
		JSONArray jsonColours = json.getJSONArray("colors");

		// make storage space for processing colours
		colours = new int[jsonColours.size()];

		// extract each of the colours
		for(int i = 0; i < jsonColours.size(); i++) {
			// convert from json colour to processing color
			colours[i] = parent.unhex(jsonColours.getString(i)) - 16777216;
		}
		
		// extract other info from json
		id = json.getInt("id");
		title = json.getString("title");
		userName = json.getString("userName");
		numViews = json.getInt("numViews");
		numVotes = json.getInt("numVotes");
		numComments = json.getInt("numComments");
		numHearts = json.getInt("numHearts");
	}

	public int getId() { return id;	}
	public String getTitle() { return title; }
	public String getUserName() { return userName; }
	public int getNumViews() { return numViews; }
	public int getNumVotes() { return numVotes;	}
	public int getNumComments() { return numComments; }
	public int getNumHearts() {	return numHearts; }
	public int[] getColours() {	return colours;	}
}
