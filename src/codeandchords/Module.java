package codeandchords;

import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import codeandchords.ModuleMenu;
import codeandchords.input.Input;
import codeandchords.input.RealTimeInput;
import codeandchords.input.RecordedInput;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;

/**
 * Aug. 16, 2017
 * 
 * Template for Modules
 * 
 * @author Dan Mahota, Emily Meuer
 */
public class Module
{

	/**	Input, because we are assuming that the whole point of a Module is to interact with an Input	*/
	protected	Input					input;

	private 	boolean 				isRealTime;

	private 	ControlP5				sciGirlsCP5;

	/**	This is the total number of possible inputs; *must* be initialized by child classes!	*/
	protected	int		totalNumInputs = 1;

	/**	This is the number of inputs currently displaying in the Module	*/
	protected	int		curNumInputs;

	protected	int[]	xVals;
	protected	int[]	yVals;
	protected	int[]	rectWidths;
	protected	int[]	rectHeights;

	protected	boolean	debugLegendColors	= false;

	//	protected	Shape			shape;
	//	protected	Shape[]			shapes;

	/**	For Modules with a Shape, this ShapeEditor provides Shape customization Controllers	*/
	//	protected 	ShapeEditor		shapeEditor;

	/**	"Sidebar" Menu, where most basic Controllers will be - global HSB and RGB modulation, etc.	*/
	protected	ModuleMenu		menu;

	protected	int		currentMenu;

	protected 	boolean		verticalBarsDemo = false;
	protected 	float[]		amplitude;

	/**	Used by legend() to determine which colors to select for the legend along the bottom	*/
	private	final	int[][] scaleDegrees = new int[][] {
		// major:
		new int[]  { 0, 2, 4, 5, 7, 9, 11
		},
		// minor:
		new int[]  { 0, 2, 3, 5, 7, 8, 10
		},
		// chromatic:
		new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11
		}
	}; // scaleDegrees

	protected PApplet parent;

	public Module(PApplet parent)
	{
		this.parent	= parent;

		this.parent.registerMethod("draw", this);
	}

	/**
	 * Sets the Module size.
	 */
	public void settings()
	{
		//fullscreen();
		this.parent.size(925, 520);
	}

	/*
	 * Called by the sketch immediately after draw;
	 * simply runs the Menu.
	 */
	public void draw()
	{
		this.menu.runMenu();
	}

	public void setupModule(int numInputs, boolean useRealTimeInput)
	{
		if(useRealTimeInput)
		{
			this.input  = new RealTimeInput(numInputs, false, this.parent);
		}
		else
		{
			this.input = new RecordedInput(this.parent, new String[] { 	
					"Lead_01.wav",
					"Beatbox_01.wav",
					"ALto b_01.wav",
					"Alto_01.wav",
					"Sop1_01.wav",
					"Sop1b_01.wav",
					"Sop2_01.wav",
					"Sop2b_01.wav" });
		}

		this.totalNumInputs  = this.input.getAdjustedNumInputs();
		this.curNumInputs  = this.totalNumInputs;

		this.menu  = new ModuleMenu(this.parent, this, this.input, 12);

		// Setup the menu:
		this.menu.addLandingMenu();
		this.menu.addSensitivityMenu(true);
		this.menu.addColorMenu();

		this.menu.getOutsideButtonsCP5().hide();

		this.menu.setCurKey("G", 0);

		for(int i = 0; i < this.curNumInputs; i++)
		{
			this.menu.setPianoThreshold(i, 10);
		}

	} // setupModule

	public void useRecordings()
	{

		this.input.stop();
		this.input = new RecordedInput(this.parent, new String[] { 	
				"ALto b_01.wav",
				"Alto_01.wav",
				"Beatbox_01.wav",
				"Lead_01.wav",
				"Sop1_01.wav",
				"Sop1b_01.wav",
				"Sop2_01.wav",
		"Sop2b_01.wav" });
		this.isRealTime = false;
	}

	/**
	 * Draws the legend at the bottom of the screen.
	 * 
	 * @param goalHuePos	current position, be that note or threshold level, in this Module's menu.colorSelect
	 */
	public void legend(int goalHuePos, int inputNum)
	{
		if(this.rectWidths == null)
		{
			this.curNumInputs		= Math.max(this.curNumInputs, 1);
			this.setSquareValues();
		}

		this.parent.textSize(Math.max(24 - (this.curNumInputs * 2), 8));

		String[]	legendText	= this.getLegendText();

		//		float	scale	= 1;
		//		if(this.menu.getIsRunning())	{	scale	= this.menu.getScale();	}
		float	scale	= this.menu.getCurrentScale();

		float	sideWidth1	=(this.rectWidths[inputNum] * scale) / legendText.length;
		float	sideHeight	= this.rectHeights[inputNum] / 10; //this.rectWidths[inputNum] / 12;	// pretty arbitrary
		float	addToLastRect	= (this.rectWidths[inputNum] * scale) - (sideWidth1 * legendText.length);
		float	sideWidth2	= sideWidth1;

		this.parent.noStroke();

		int	scaleDegree;
		float	xVal	= this.menu.mapCurrentXPos(this.xVals[inputNum]);
		float	yVal	= this.menu.mapCurrentYPos(this.yVals[inputNum] + this.rectHeights[inputNum]);

		for (int i = 0; i < legendText.length; i++)
		{
			if(i == legendText.length - 1)
			{
				sideWidth2	= sideWidth1 + addToLastRect;
			}

			// colors is filled all the way and only picked at the desired notes:
			scaleDegree	= this.scaleDegrees[this.menu.getMajMinChrom()][i];
			this.parent.fill(this.menu.getColors()[inputNum][scaleDegree][0], this.menu.getColors()[inputNum][scaleDegree][1], this.menu.getColors()[inputNum][scaleDegree][2]);

			if (i == goalHuePos) {
				this.parent.rect(xVal + (sideWidth1 * i), yVal - (sideHeight * 1.5f), sideWidth2, (sideHeight * 1.5f));
			} else {
				this.parent.rect(xVal + (sideWidth1 * i), yVal - sideHeight, sideWidth2, sideHeight);
			}

			this.parent.fill(0);			
			this.parent.text(legendText[i], (float) (xVal + (sideWidth1 * i) + (sideWidth1 * 0.3)), yVal - (sideHeight * 0.3f));

			if(debugLegendColors)
			{
				this.parent.textSize(12);
				this.parent.text(("r: " + this.menu.getColors()[inputNum][scaleDegree][0]), (float) (xVal + (sideWidth1 * i)) + 10, yVal - (sideHeight * 0.4f) - 50);
				this.parent.text(("g: " + this.menu.getColors()[inputNum][scaleDegree][1]), (float) (xVal + (sideWidth1 * i)) + 10, yVal - (sideHeight * 0.4f) - 40);
				this.parent.text(("b: " + this.menu.getColors()[inputNum][scaleDegree][2]), (float) (xVal + (sideWidth1 * i)) + 10, yVal - (sideHeight * 0.4f) - 30);
			}
		} // for - i

	} // legend

	/**
	 * Draws the legend at the bottom of the screen.
	 * 
	 * @param goalHuePos	current position, be that note or threshold level, in this Module's menu.colorSelect
	 */
	public void okGoLegend(int goalHuePos, int inputNum)
	{
		if(this.rectWidths == null)
		{
			this.curNumInputs		= Math.max(this.curNumInputs, 1);
			this.setSquareValues();
		}

		this.parent.textSize(Math.max(24 - (this.curNumInputs * 2), 8));

		String[]	legendText	= this.getLegendText();

		//		float	scale	= 1;
		//		if(this.menu.getIsRunning())	{	scale	= this.menu.getScale();	}
		float	scale	= this.menu.getCurrentScale();

		float	sideWidth1	=(this.rectWidths[inputNum] * scale) / legendText.length;
		float	sideHeight	= this.rectHeights[inputNum] / 10; //this.rectWidths[inputNum] / 12;	// pretty arbitrary
		float	addToLastRect	= (this.rectWidths[inputNum] * scale) - (sideWidth1 * legendText.length);
		float	sideWidth2	= sideWidth1;

		this.parent.noStroke();

		int	scaleDegree;
		float	xVal;
		float	yVal;
		if(inputNum < 2)
		{
			xVal	= this.menu.mapCurrentXPos(this.xVals[inputNum]);
			yVal	= this.menu.mapCurrentYPos(this.yVals[inputNum] + (sideHeight * this.menu.getCurrentScale() * 1.5f));
		} else {
			xVal	= this.menu.mapCurrentXPos(this.xVals[inputNum]);
			yVal	= this.menu.mapCurrentYPos(this.yVals[inputNum] + this.rectHeights[inputNum]);
		}

		for (int i = 0; i < legendText.length; i++)
		{
			if(i == legendText.length - 1)
			{
				sideWidth2	= sideWidth1 + addToLastRect;
			}

			// colors is filled all the way and only picked at the desired notes:
			scaleDegree	= this.scaleDegrees[this.menu.getMajMinChrom()][i];
			this.parent.fill(this.menu.getColors()[inputNum][scaleDegree][0], this.menu.getColors()[inputNum][scaleDegree][1], this.menu.getColors()[inputNum][scaleDegree][2]);

			if (i == goalHuePos) {
				this.parent.rect(xVal + (sideWidth1 * i), yVal - (sideHeight * this.menu.getCurrentScale() * 1.5f), sideWidth2, (sideHeight * this.menu.getCurrentScale() * 1.5f));
			} else {
				this.parent.rect(xVal + (sideWidth1 * i), yVal - (sideHeight * this.menu.getCurrentScale()), sideWidth2, sideHeight * this.menu.getCurrentScale());
			}

			this.parent.fill(0);
			this.parent.text(legendText[i], (float) (xVal + (sideWidth1 * i) + (sideWidth1 * 0.3)), yVal - (sideHeight * 0.3f));
		} // for - i

	} // legend


	/**
	 * Default value is the current scale, but a Module can override this for its own use.
	 * 
	 * @return	String[] of text to display in each position of the legend;
	 */
	public String[] getLegendText()
	{	
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText


	public void setCurNumInputs(int newCurNumInputs)
	{
		this.curNumInputs	= newCurNumInputs;
	}

	public int getCurNumInputs()
	{
		return this.curNumInputs;
	}

	public int getTotalNumInputs()
	{
		return this.totalNumInputs;
	}

	public ModuleMenu getModuleMenu()
	{
		return this.menu;
	}


	/**
	 * Calculates the x and y values for the squares given the number of inputs.
	 */
	public void setSquareValues()
	{
		if(this.verticalBarsDemo)
		{
			int barWidth = this.parent.width/this.curNumInputs;

			this.xVals = new int[this.curNumInputs];
			this.yVals = new int[this.curNumInputs];
			this.rectWidths = new int[this.curNumInputs];
			this.rectHeights = new int[this.curNumInputs];

			for(int i = 0; i < this.curNumInputs; i++)
			{
				//gives value between 0 and 1 to be used as a percent
				//System.out.println(this.amplitude[i]);
				float amp = (float) Math.min(1, this.amplitude[i] / 500/*max amp*/);
				amp = (float) Math.max(amp, .1);

				this.xVals[i] = i * barWidth;
				//this.yVals[i] = (int) (val - (amp*val));
				this.yVals[i] = 0;
				this.rectWidths[i] = barWidth;
				//this.rectHeights[i] = (int) (amp * this.parent.height);
				this.rectHeights[i] = this.parent.height;
			}

		}
		else
		{
			// Rectangles are always the same height, so will be set in a loop every time:
			this.rectHeights	= new int[this.curNumInputs];

			// Setting xVals and yVals and width and height of rectangles:
			// Even number of inputs:
			if(this.curNumInputs % 2 == 0 && this.curNumInputs != 12)
			{
				this.rectWidths		= new int[this.curNumInputs];
				this.rectHeights	= new int[this.curNumInputs];
				for(int i = 0; i < this.rectWidths.length; i++)
				{
					this.rectWidths[i]	= this.parent.width / (this.curNumInputs / 2);
					this.rectHeights[i]	= this.parent.height / 2;
				} // for

				this.xVals	= new int[this.curNumInputs];
				this.yVals	= new int[this.curNumInputs];

				for(int i = 0; i < this.xVals.length; i++)
				{
					int xPos	= i % (this.curNumInputs / 2);
					int xVal	= xPos * (this.rectWidths[i]);
					xVals[i]	= xVal;
					System.out.println(i + ": xPos = " + xPos + "; xVal = " + xVal);
				} // for - xVals

				for(int i = 0; i < this.yVals.length; i++)
				{
					int	yPos	= i / (this.curNumInputs / 2);
					int	yVal	= yPos * this.rectHeights[i];
					yVals[i]	= yVal;
				} // for - yVals
			} // even number of inputs
			else if(this.curNumInputs == 1)
			{
				this.rectWidths		= new int[] {	this.parent.width	};
				this.rectHeights	= new int[]	{	this.parent.height	};

				this.xVals	= new int[] {	0	};
				this.yVals	= new int[] {	0	};
			} // 1
			else if(this.curNumInputs == 3)
			{
				this.rectWidths		= new int[] {	
						this.parent.width,
						(this.parent.width / 2), (this.parent.width / 2)
				};
				for(int i = 0; i < this.rectHeights.length; i++)
				{
					this.rectHeights[i]	= this.parent.height / 2;
				}

				this.xVals	= new int[] { 
						0,
						0,	(this.parent.width / 2)
				};
				this.yVals	= new int[] {
						0,
						(this.parent.height / 2), (this.parent.height / 2)
				};
			} // 3
			else if(this.curNumInputs == 5)
			{
				this.rectWidths	= new int[] {
						(this.parent.width / 2),	(this.parent.width / 2),
						(this.parent.width / 3), (this.parent.width / 3), (this.parent.width / 3)
				};
				for(int i = 0; i < this.rectHeights.length; i++)
				{
					this.rectHeights[i]	= this.parent.height / 2;
				}

				this.xVals	= new int[] {
						0,				(this.parent.width / 2),	
						0,	(this.parent.width / 3), ((this.parent.width / 3) * 2)
				};
				this.yVals	= new int[] {
						0,				0,
						(this.parent.height / 2), (this.parent.height / 2), (this.parent.height / 2)
				};
			} // 5
			else if(this.curNumInputs == 7)
			{
				this.rectWidths	= new int[] {
						(this.parent.width / 2),	(this.parent.width / 2),
						(this.parent.width / 2), (this.parent.width / 3), (this.parent.width / 3),
						(this.parent.width / 2),	(this.parent.width / 2)
				};
				for(int i = 0; i < this.rectHeights.length; i++)
				{
					this.rectHeights[i]	= this.parent.height / 3;
				}

				this.xVals	= new int[] {
						0,				(this.parent.width / 2),	
						0,	(this.parent.width / 3), ((this.parent.width / 3) * 2),
						0,				(this.parent.width / 2)
				};
				this.yVals	= new int[] {
						0,				0,
						(this.parent.height / 3), (this.parent.height / 3), (this.parent.height / 3),
						(this.parent.height / 3) * 2, (this.parent.height / 3) * 2, (this.parent.height / 3) * 2
				};
			} // 7
			else if(this.curNumInputs == 9)
			{
				this.rectWidths		= new int[this.curNumInputs];
				for(int i = 0; i < this.rectWidths.length; i++)
				{
					this.rectWidths[i]	= (this.parent.width / 3);
					this.rectHeights[i]	= (this.parent.height / 3);
				} // for

				this.xVals	= new int[] {
						0, this.parent.width/3, (this.parent.width/3) * 2,
						0, this.parent.width/3, (this.parent.width/3) * 2,
						0, this.parent.width/3, (this.parent.width/3) * 2
				};
				this.yVals	= new int[] {
						0, 0, 0,
						this.parent.height/3, this.parent.height/3, this.parent.height/3, 
						((this.parent.height / 3) * 2), ((this.parent.height / 3) * 2), ((this.parent.height / 3) * 2)
				};
			} // 9
			else if(this.curNumInputs == 11)
			{
				this.rectWidths		= new int[this.curNumInputs];
				for(int i = 0; i < this.rectWidths.length; i++)
				{
					if(i < 4 || i > 6)
					{
						this.rectWidths[i]	= (this.parent.width / 4);
					} else {
						// middle row has only 3:
						this.rectWidths[i]	= (this.parent.width / 3);
					}

					this.rectHeights[i]	= (this.parent.height / 3);
				} // for

				this.xVals	= new int[] {
						0, this.parent.width/4, this.parent.width/2, (this.parent.width/4) * 3,
						0, this.parent.width/3, (this.parent.width/3) * 2,
						0, this.parent.width/4, this.parent.width/2, (this.parent.width/4) * 3,
				};
				this.yVals	= new int[] {
						0, 0, 0, 0,
						this.parent.height/3, this.parent.height/3, this.parent.height/3, 
						((this.parent.height / 3) * 2), ((this.parent.height / 3) * 2), ((this.parent.height / 3) * 2), ((this.parent.height / 3) * 2)
				};
			} // 11
			else if(this.curNumInputs == 12)
			{
				this.rectWidths		= new int[this.curNumInputs];
				for(int i = 0; i < this.rectWidths.length; i++)
				{
					this.rectWidths[i]	= (this.parent.width / 4);
					this.rectHeights[i]	= (this.parent.height / 3);
				} // for

				this.xVals	= new int[] {
						0, this.parent.width/4, this.parent.width/2, (this.parent.width/4) * 3,
						0, this.parent.width/4, this.parent.width/2, (this.parent.width/4) * 3,
						0, this.parent.width/4, this.parent.width/2, (this.parent.width/4) * 3,
				};
				this.yVals	= new int[] {
						0, 0, 0, 0,
						this.parent.height/3, this.parent.height/3, this.parent.height/3, this.parent.height/3, 
						((this.parent.height / 3) * 2), ((this.parent.height / 3) * 2), ((this.parent.height / 3) * 2), ((this.parent.height / 3) * 2)
				};
			} // 12
		} // else - verticalBars

	} // set Square Vals

	/////////////// SciGirls Abstracted Methods /////////////////

	/**
	 * Returns the scale degree (0 to 11) of the note currently being sung
	 * into the given input line.
	 * 
	 * @param inputNum	number indicating which input to get the note from
	 * @return	the scale degree of the note currently being sung into that input
	 */
	public int getMusicalNote(int inputNum)
	{
		this.inputNumErrorCheck(inputNum, "getScaleDegree");

		return (int) ((this.input.getAdjustedFundAsMidiNote(inputNum) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12);
	} // getScaleDegree

	/**
	 * Returns the amplitude of the given input line.
	 * 
	 * @param inputNum	number indicating which input to get the amplitude from
	 * @return		the amplitude of the given input
	 */
	public float getVolume(int inputNum)
	{
		this.inputNumErrorCheck(inputNum, "getAmplitude");

		return this.input.getAmplitude(inputNum);
	} // getAmplitude

	/**
	 * Gets the current red value for the given input.
	 * 
	 * @param inputNum	input to get the red value for
	 * @return	red value for this input
	 */
	public int getRed(int inputNum)
	{
		this.inputNumErrorCheck(inputNum, "getRed");

		return	this.menu.getCurHue()[inputNum][0];
	}

	/**
	 * Gets the current green value for the given input.
	 * 
	 * @param inputNum	input to get the green value for
	 * @return	green value for this input
	 */
	public int getGreen(int inputNum)
	{
		this.inputNumErrorCheck(inputNum, "getGreen");

		return	this.menu.getCurHue()[inputNum][1];
	}

	/**
	 * Gets the current blue value for the given input.
	 * 
	 * @param inputNum	input to get the blue value for
	 * @return	blue value for this input
	 */
	public int getBlue(int inputNum)
	{
		this.inputNumErrorCheck(inputNum, "getBlue");

		return	this.menu.getCurHue()[inputNum][2];
	}

	/**
	 * Sets the given input's first color to the given color.
	 * 
	 * @param inputNum	input to change the color for
	 * @param red	red value for the color
	 * @param green	green value for the color
	 * @param blue	blue value for the color
	 */
	public void setColor0(int inputNum, int red, int green, int blue)
	{
		this.inputNumErrorCheck(inputNum, "setColor0");

		this.menu.setColor(0, new int[] { 
				Math.min(255, Math.max(0, red)),
				Math.min(255, Math.max(0, green)),
				Math.min(255, Math.max(0, blue))
		}, false);
	}

	/**
	 * Sets the given input's second color to the given color.
	 * 
	 * @param inputNum	input to change the color for
	 * @param red	red value for the color
	 * @param green	green value for the color
	 * @param blue	blue value for the color
	 */
	public void setColor1(int inputNum, int red, int green, int blue)
	{
		this.inputNumErrorCheck(inputNum, "setColor1");

		this.menu.setColor(1, new int[] { 
				Math.min(255, Math.max(0, red)),
				Math.min(255, Math.max(0, green)),
				Math.min(255, Math.max(0, blue))
		}, false);
	}

	/**
	 * Sets the given input's third color to the given color.
	 * 
	 * @param inputNum	input to change the color for
	 * @param red	red value for the color
	 * @param green	green value for the color
	 * @param blue	blue value for the color
	 */
	public void setColor2(int inputNum, int red, int green, int blue)
	{
		this.inputNumErrorCheck(inputNum, "setColor2");

		this.menu.setColor(2, new int[] { 
				Math.min(255, Math.max(0, red)),
				Math.min(255, Math.max(0, green)),
				Math.min(255, Math.max(0, blue))
		}, false);
	}

	/**
	 * Sets the given input's fourth color to the given color.
	 * 
	 * @param inputNum	input to change the color for
	 * @param red	red value for the color
	 * @param green	green value for the color
	 * @param blue	blue value for the color
	 */
	public void setColor3(int inputNum, int red, int green, int blue)
	{
		this.inputNumErrorCheck(inputNum, "setColor3");

		this.menu.setColor(3, new int[] { 
				Math.min(255, Math.max(0, red)),
				Math.min(255, Math.max(0, green)),
				Math.min(255, Math.max(0, blue))
		}, false);
	}

	/**
	 * Sets the given input's fifth color to the given color.
	 * 
	 * @param inputNum	input to change the color for
	 * @param red	red value for the color
	 * @param green	green value for the color
	 * @param blue	blue value for the color
	 */
	public void setColor4(int inputNum, int red, int green, int blue)
	{
		this.inputNumErrorCheck(inputNum, "setColor4");

		this.menu.setColor(4, new int[] { 
				Math.min(255, Math.max(0, red)),
				Math.min(255, Math.max(0, green)),
				Math.min(255, Math.max(0, blue))
		}, false);
	}

	/**
	 * Sets the given input's sixth color to the given color.
	 * 
	 * @param inputNum	input to change the color for
	 * @param red	red value for the color
	 * @param green	green value for the color
	 * @param blue	blue value for the color
	 */
	public void setColor5(int inputNum, int red, int green, int blue)
	{
		this.inputNumErrorCheck(inputNum, "setColor5");

		this.menu.setColor(5, new int[] { 
				Math.min(255, Math.max(0, red)),
				Math.min(255, Math.max(0, green)),
				Math.min(255, Math.max(0, blue))
		}, false);
	}

	/**
	 * Sets the given input's seventh color to the given color.
	 * 
	 * @param inputNum	input to change the color for
	 * @param red	red value for the color
	 * @param green	green value for the color
	 * @param blue	blue value for the color
	 */
	public void setColor6(int inputNum, int red, int green, int blue)
	{
		this.inputNumErrorCheck(inputNum, "setColor6");

		this.menu.setColor(6, new int[] { 
				Math.min(255, Math.max(0, red)),
				Math.min(255, Math.max(0, green)),
				Math.min(255, Math.max(0, blue))
		}, false);
	}

	/**
	 * Sets the given input's eigth color to the given color.
	 * 
	 * @param inputNum	input to change the color for
	 * @param red	red value for the color
	 * @param green	green value for the color
	 * @param blue	blue value for the color
	 */
	public void setColor7(int inputNum, int red, int green, int blue)
	{
		this.inputNumErrorCheck(inputNum, "setColor7");

		this.menu.setColor(6, new int[] { 
				Math.min(255, Math.max(0, red)),
				Math.min(255, Math.max(0, green)),
				Math.min(255, Math.max(0, blue))
		}, false);
	}

	/**
	 * Sets the given input's ninth color to the given color.
	 * 
	 * @param inputNum	input to change the color for
	 * @param red	red value for the color
	 * @param green	green value for the color
	 * @param blue	blue value for the color
	 */
	public void setColor8(int inputNum, int red, int green, int blue)
	{
		this.inputNumErrorCheck(inputNum, "setColor8");

		this.menu.setColor(8, new int[] { 
				Math.min(255, Math.max(0, red)),
				Math.min(255, Math.max(0, green)),
				Math.min(255, Math.max(0, blue))
		}, false);
	}

	/**
	 * Sets the given input's tenth color to the given color.
	 * 
	 * @param inputNum	input to change the color for
	 * @param red	red value for the color
	 * @param green	green value for the color
	 * @param blue	blue value for the color
	 */
	public void setColor9(int inputNum, int red, int green, int blue)
	{
		this.inputNumErrorCheck(inputNum, "setColor9");

		this.menu.setColor(9, new int[] { 
				Math.min(255, Math.max(0, red)),
				Math.min(255, Math.max(0, green)),
				Math.min(255, Math.max(0, blue))
		}, false);
	}

	/**
	 * Sets the given input's eleventh color to the given color.
	 * 
	 * @param inputNum	input to change the color for
	 * @param red	red value for the color
	 * @param green	green value for the color
	 * @param blue	blue value for the color
	 */
	public void setColor10(int inputNum, int red, int green, int blue)
	{
		this.inputNumErrorCheck(inputNum, "setColor10");

		this.menu.setColor(10, new int[] { 
				Math.min(255, Math.max(0, red)),
				Math.min(255, Math.max(0, green)),
				Math.min(255, Math.max(0, blue))
		}, false);
	}

	/**
	 * Sets the given input's twelfth color to the given color.
	 * 
	 * @param inputNum	input to change the color for
	 * @param red	red value for the color
	 * @param green	green value for the color
	 * @param blue	blue value for the color
	 */
	public void setColor11(int inputNum, int red, int green, int blue)
	{
		this.inputNumErrorCheck(inputNum, "setColor11");

		this.menu.setColor(11, new int[] { 
				Math.min(255, Math.max(0, red)),
				Math.min(255, Math.max(0, green)),
				Math.min(255, Math.max(0, blue))
		}, false);
	}

	/**
	 * Sets the first color to the given color for all inputs.
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void setColor0forAllInputs(int red, int green, int blue)
	{
		for(int i = 0; i < this.menu.colors.length; i++)
		{
			this.menu.setColor(0, new int[] { 
					Math.min(255, Math.max(0, red)),
					Math.min(255, Math.max(0, green)),
					Math.min(255, Math.max(0, blue))
			}, false);
		} // for - through inputs
		
		for(int i = 0; i < this.menu.colors.length; i++)
		{
			this.menu.setColor(1, new int[] { 
					Math.min(255, Math.max(0, red)),
					Math.min(255, Math.max(0, green)),
					Math.min(255, Math.max(0, blue))
			}, false);
		} // for - through inputs
	}

	/**
	 * Sets the ninth color to the given color for all inputs.
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void setColor1forAllInputs(int red, int green, int blue)
	{
		for(int i = 0; i < this.menu.colors.length; i++)
		{
			this.menu.setColor(2, new int[] { 
					Math.min(255, Math.max(0, red)),
					Math.min(255, Math.max(0, green)),
					Math.min(255, Math.max(0, blue))
			}, false);
		} // for - through inputs
		
		for(int i = 0; i < this.menu.colors.length; i++)
		{
			this.menu.setColor(3, new int[] { 
					Math.min(255, Math.max(0, red)),
					Math.min(255, Math.max(0, green)),
					Math.min(255, Math.max(0, blue))
			}, false);
		} // for - through inputs
	}

	/**
	 * Sets the third color to the given color for all inputs.
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void setColor2forAllInputs(int red, int green, int blue)
	{
		for(int i = 0; i < this.menu.colors.length; i++)
		{
			this.menu.setColor(4, new int[] { 
					Math.min(255, Math.max(0, red)),
					Math.min(255, Math.max(0, green)),
					Math.min(255, Math.max(0, blue))
			}, false);
		} // for - through inputs
	}

	/**
	 * Sets the fourth color to the given color for all inputs.
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void setColor3forAllInputs(int red, int green, int blue)
	{
		for(int i = 0; i < this.menu.colors.length; i++)
		{
			this.menu.setColor(5, new int[] { 
					Math.min(255, Math.max(0, red)),
					Math.min(255, Math.max(0, green)),
					Math.min(255, Math.max(0, blue))
			}, false);
		} // for - through inputs
		
		for(int i = 0; i < this.menu.colors.length; i++)
		{
			this.menu.setColor(6, new int[] { 
					Math.min(255, Math.max(0, red)),
					Math.min(255, Math.max(0, green)),
					Math.min(255, Math.max(0, blue))
			}, false);
		} // for - through inputs
	}

	/**
	 * Sets the fifth color to the given color for all inputs.
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void setColor4forAllInputs(int red, int green, int blue)
	{
		for(int i = 0; i < this.menu.colors.length; i++)
		{
			this.menu.setColor(7, new int[] { 
					Math.min(255, Math.max(0, red)),
					Math.min(255, Math.max(0, green)),
					Math.min(255, Math.max(0, blue))
			}, false);
		} // for - through inputs
		
		for(int i = 0; i < this.menu.colors.length; i++)
		{
			this.menu.setColor(8, new int[] { 
					Math.min(255, Math.max(0, red)),
					Math.min(255, Math.max(0, green)),
					Math.min(255, Math.max(0, blue))
			}, false);
		} // for - through inputs
	}

	/**
	 * Sets the sixth color to the given color for all inputs.
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void setColor5forAllInputs(int red, int green, int blue)
	{
		for(int i = 0; i < this.menu.colors.length; i++)
		{
			this.menu.setColor(9, new int[] { 
					Math.min(255, Math.max(0, red)),
					Math.min(255, Math.max(0, green)),
					Math.min(255, Math.max(0, blue))
			}, false);
		} // for - through inputs
		
		for(int i = 0; i < this.menu.colors.length; i++)
		{
			this.menu.setColor(10, new int[] { 
					Math.min(255, Math.max(0, red)),
					Math.min(255, Math.max(0, green)),
					Math.min(255, Math.max(0, blue))
			}, false);
		} // for - through inputs
	}

	/**
	 * Sets the seventh color to the given color for all inputs.
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void setColor6forAllInputs(int red, int green, int blue)
	{
		for(int i = 0; i < this.menu.colors.length; i++)
		{
			this.menu.setColor(11, new int[] { 
					Math.min(255, Math.max(0, red)),
					Math.min(255, Math.max(0, green)),
					Math.min(255, Math.max(0, blue))
			}, false);
		} // for - through inputs
	}

	/**
	 * Set the first color of the scale and makes a trichromatic spectrum
	 * between that, the 4th and the 8th colors of the scale.
	 * 
	 * @param inputNum	input to be affected
	 * @param red	red value of the color
	 * @param green	green value of the color
	 * @param blue	blue value of the color
	 */
	public void setTrichromColor1(int inputNum, int red, int green, int blue)
	{
		this.inputNumErrorCheck(inputNum, "setTrichromColor1");

		this.menu.trichromatic_ThreeRGB(new int[] { 
				Math.min(255, Math.max(0, red)),
				Math.min(255, Math.max(0, green)),
				Math.min(255, Math.max(0, blue))
		}, 
				this.menu.colors[inputNum][4], 
				this.menu.colors[inputNum][8],
				inputNum);
	}

	/**
	 * Set the 4th color of the scale and makes a trichromatic spectrum
	 * between the 1st, the 4th and the 8th colors of the scale.
	 * 
	 * @param inputNum	input to be affected
	 * @param red	red value of the color
	 * @param green	green value of the color
	 * @param blue	blue value of the color
	 */
	public void setTrichromColor2(int inputNum, int red, int green, int blue)
	{
		this.inputNumErrorCheck(inputNum, "setTrichromColor2");

		this.menu.trichromatic_ThreeRGB(
				this.menu.colors[inputNum][0],
				new int[] { 
						Math.min(255, Math.max(0, red)),
						Math.min(255, Math.max(0, green)),
						Math.min(255, Math.max(0, blue))
				}, 
				this.menu.colors[inputNum][8],
				inputNum);
	}

	/**
	 * Set the 8th color of the scale and makes a trichromatic spectrum
	 * between the 1st, the 4th and the 8th colors of the scale.
	 * 
	 * @param inputNum	input to be affected
	 * @param red	red value of the color
	 * @param green	green value of the color
	 * @param blue	blue value of the color
	 */
	public void setTrichromColor3(int inputNum, int red, int green, int blue)
	{
		this.inputNumErrorCheck(inputNum, "setTrichromColor3");

		this.menu.trichromatic_ThreeRGB(
				this.menu.colors[inputNum][0], 
				this.menu.colors[inputNum][4],
				new int[] { 
						Math.min(255, Math.max(0, red)),
						Math.min(255, Math.max(0, green)),
						Math.min(255, Math.max(0, blue))
				},
				inputNum);
	}

	/**
	 * Set the 1st color of the scale and makes a trichromatic spectrum
	 * between the 1st, the 4th and the 8th colors of the scale.
	 * 
	 * @param red	red value of the color
	 * @param green	green value of the color
	 * @param blue	blue value of the color
	 */
	public void setTrichromColor1forAllInputs(int red, int green, int blue)
	{
		for(int i = 0; i < this.menu.colors.length; i++)
		{
			this.menu.trichromatic_ThreeRGB(
					new int[] { 
							Math.min(255, Math.max(0, red)),
							Math.min(255, Math.max(0, green)),
							Math.min(255, Math.max(0, blue))
					}, 
					this.menu.colors[i][4],
					this.menu.colors[i][8], 
					i);
		}
	}

	/**
	 * Set the 4th color of the scale and makes a trichromatic spectrum
	 * between the 1st, the 4th and the 8th colors of the scale.
	 * 
	 * @param red	red value of the color
	 * @param green	green value of the color
	 * @param blue	blue value of the color
	 */
	public void setTrichromColor2forAllInputs(int red, int green, int blue)
	{
		for(int i = 0; i < this.menu.colors.length; i++)
		{
			this.menu.trichromatic_ThreeRGB(
					this.menu.colors[i][0],
					new int[] { 
							Math.min(255, Math.max(0, red)),
							Math.min(255, Math.max(0, green)),
							Math.min(255, Math.max(0, blue))
					},
					this.menu.colors[i][8], 
					i);
		}
	}

	/**
	 * Set the 8th color of the scale and makes a trichromatic spectrum
	 * between the 1st, the 4th and the 8th colors of the scale.
	 * 
	 * @param red	red value of the color
	 * @param green	green value of the color
	 * @param blue	blue value of the color
	 */
	public void setTrichromColor3forAllInputs(int red, int green, int blue)
	{
		for(int i = 0; i < this.menu.colors.length; i++)
		{
			this.menu.trichromatic_ThreeRGB(
					this.menu.colors[i][0],
					this.menu.colors[i][4], 
					new int[] { 
							Math.min(255, Math.max(0, red)),
							Math.min(255, Math.max(0, green)),
							Math.min(255, Math.max(0, blue))
					},
					i);
		}
	}

	/**
	 * Set the first color of the scale and make a dichromatic spectrum
	 * between that and the last color of the scale.
	 * 
	 * @param inputNum	input to be affected
	 * @param red	red value of the color
	 * @param green	green value of the color
	 * @param blue	blue value of the color
	 */
	public void setDichromColor1(int inputNum, int red, int green, int blue)
	{
		this.inputNumErrorCheck(inputNum, "setDichromColor1");

		this.menu.dichromatic_TwoRGB(new int[] { 
				Math.min(255, Math.max(0, red)),
				Math.min(255, Math.max(0, green)),
				Math.min(255, Math.max(0, blue))
		}, this.menu.colors[inputNum][11], inputNum);
	}

	/**
	 * Set the last color of the scale and make a dichromatic spectrum
	 * between the first color and this one.
	 * 
	 * @param inputNum	input to be affected
	 * @param red	red value of the color
	 * @param green	green value of the color
	 * @param blue	blue value of the color
	 */
	public void setDichromColor2(int inputNum, int red, int green, int blue)
	{
		this.inputNumErrorCheck(inputNum, "setDichromColor2");

		this.menu.dichromatic_TwoRGB(this.menu.colors[inputNum][0], 
				new int[] { 
						Math.min(255, Math.max(0, red)),
						Math.min(255, Math.max(0, green)),
						Math.min(255, Math.max(0, blue))
		}, inputNum);
	}

	/**
	 * Set the first color of the scale and make a dichromatic spectrum
	 * between that and the last color of the scale.
	 * 
	 * @param inputNum	input to be affected
	 * @param red	red value of the color
	 * @param green	green value of the color
	 * @param blue	blue value of the color
	 */
	public void setDichromColor1forAllInputs(int red, int green, int blue)
	{
		for(int i = 0; i < this.menu.colors.length; i++)
		{
			this.menu.dichromatic_TwoRGB(new int[] { 
					Math.min(255, Math.max(0, red)),
					Math.min(255, Math.max(0, green)),
					Math.min(255, Math.max(0, blue))
			}, this.menu.colors[i][11], i);
		}
	}

	/**
	 * Set the last color of the scale and make a dichromatic spectrum
	 * between the first color and this one.
	 * 
	 * @param inputNum	input to be affected
	 * @param red	red value of the color
	 * @param green	green value of the color
	 * @param blue	blue value of the color
	 */
	public void setDichromColor2forAllInputs(int red, int green, int blue)
	{
		for(int i = 0; i < this.menu.colors.length; i++)
		{
			this.menu.dichromatic_TwoRGB(this.menu.colors[i][0], 
					new int[] { 
							Math.min(255, Math.max(0, red)),
							Math.min(255, Math.max(0, green)),
							Math.min(255, Math.max(0, blue))
			}, i);
		}
	}

	public void setColor(int scaleDegree, int inputNum)
	{
		this.inputNumErrorCheck(inputNum, "setColor");

		this.menu.fadeColor(scaleDegree, inputNum);
	} // setColor

	/**
	 * Set the attack value for this particular input to the given value
	 * (that is, the time that it will take for a new color to come fully).
	 * 
	 * @param inputNum	The input whose attack value is being changed
	 * @param attackVal	The new attack value
	 */
	public void setAttack(int inputNum, int attackVal)
	{
		this.menu.setAttRelTranVal(0, inputNum, attackVal);
	} // setAttack

	/**
	 * Set the release value for this particular input to the given value
	 * (that is, the time that it will take for each color to fade away).
	 * 
	 * @param inputNum	The input whose release value is being changed
	 * @param releaseVal	The new release value
	 */
	public void setRelease(int inputNum, int releaseVal)
	{
		this.menu.setAttRelTranVal(1, inputNum, releaseVal);
	} // setRelease

	private void inputNumErrorCheck(int inputNum, String method)
	{
		if(inputNum < 0)
		{
			IllegalArgumentException iae = new IllegalArgumentException("Module." + method + ": parameter " + inputNum + " is less than 0; must be between 0 and " + this.totalNumInputs + ".");
			throw iae;
		} else if(inputNum > this.totalNumInputs) {
			IllegalArgumentException iae = new IllegalArgumentException("Module." + method + ": parameter " + inputNum + " is greater than " + this.totalNumInputs + "; must be between 0 and the total number of inputs.");
			throw iae;
		}
	}
	
	public PShape getSuperShape(float a, float m1, float n1, float n2, float n3)
	{
		float b = a;
		float m2 = m1;	

		float[] shape = new float[100];

		int i = 0;
		float r = 0;

		float increment = (float) (2*Math.PI/100);

		for(float theta = 0; theta < 2*Math.PI; theta += increment)
		{
			float part1 = (float) ((1 / a) * Math.cos(theta * m1 / 4));
			part1 = Math.abs(part1);
			part1 = (float) Math.pow(part1, n2);

			float part2 = (float) ((1 / b) * Math.sin(theta * m2 / 4));
			part2 = Math.abs(part2);
			part2 = (float) Math.pow(part2, n3);

			float part3 = (float) Math.pow(part1 + part2, 1 / n1);

			if (part3 == 0) {
				r = 0;
			}
			else { 
				r = (1 / part3); 
			}

			shape[i] = r * 100;
			
			i++;
		}//for()

		PShape pshape = this.parent.createShape();

		pshape.beginShape();

		float x;
		float y;

		i = 0;

		for(float theta = 0; theta < 2*Math.PI; theta += increment)
		{
			x = (float) (shape[i]*Math.cos(theta));
			y = (float) (shape[i]*Math.sin(theta));

			pshape.vertex(x, y);

			i++;

		}//for()

		return pshape;

		

	}




} // Module
