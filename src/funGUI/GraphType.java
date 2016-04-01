package funGUI;

public enum GraphType {
	DOTPLOT(1),
	BOXPLOT(1),
	SCATTERPLOT(2),
	HISTOGRAM(1),
	BAR(1),
	COLUMN(1),
	PIE(1),
	LINE(2);
	
	int nDimensions = 0;
	
	public int d() {
		return(nDimensions);
	}
	
	GraphType(int num) {
		nDimensions = num;
	}
}
