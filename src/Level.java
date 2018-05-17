	
/*
 * This class makes a level used in the game
 */

public class Level {
		
	public String name;
	public int width;
	public int height;
	public String info = "";
		
	// used in testing
	public void setInfo(String s) {
		this.info = s;
	}
	
	// used in testing
	public String getInfo() {
		return this.info;
	}
	
	// used in testing to see if replaceTile works
	public String replaceTileTesting(String line) {
		line = line.replace("#", "W"); // wall
		line = line.replace(".", "G"); // goal
		line = line.replace("$", "B"); // block
		line = line.replace("@", "A"); // avatar
		line = line.replace("*", "X"); // block + goal
		line = line.replace("+", "Y"); // avatar + goal
		line = line.replace(" ", "S"); // space
		return line;
	}
		
}