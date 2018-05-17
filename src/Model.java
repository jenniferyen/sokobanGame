
/*
 * This class contains the model created using a specified level. The model
 * consists of a char[][] array with the easier-to-use symbols. 
 */
public class Model{
	
	//private fields
	private String level = "";
	private char[][] levelMap;
	
	// used in testing
	public Model(Level level, boolean b) {
		this.level = level.info;
	}
	
	public Model(Level level) {
		this.level = level.info;
		levelMap = new char[level.width][level.height];
		fillMap();
	}
	
	public Model (Level level, String s) {
		this.level = level.info;
		levelMap = new char[level.width][level.height];
		// fillLoadMap();
	}
	
	public char[][] getLevelMap() {
		return levelMap;
	}
	
	// used in testing
	public String getLevelInfo() {
		return level;
	}
	
	// fills char[][] array with the necessary symbols
	private void fillMap() {
		String[] lines = level.split("\\n");
		int y = 0;
		for (String line : lines) {
			for (int x = 0; x < line.length(); x++) {
				char c = line.charAt(x);
				levelMap[x][y] = c;
			}
			y++;
		}
	}
	
//	// have to convert a String[] into a char[][]???
//	private void fillLoadMap() {
//		String[] lines = level.split("BREAK");
//		int y = 0;
//		for (String line : lines) {
//			for (int x = 0; x < line.length(); x++) {
//				char c = line.charAt(x);
//				levelMap[x][y] = c;
//			}
//			y++;
//		}
//	}
	
}
