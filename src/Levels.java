import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

/*
 * This class organizes ALL of the levels using a Map<String, Level> where the 
 * String is the level #. It contains methods to load levels from a text file
 * get private fields, and replace characters in the text file with easier-to-use
 * symbols. 
 * I found the text file to read my levels from http://www.sourcecode.se/sokoban/levels
 */
public class Levels {
	
	// private fields
	private static Map<String, Level> levels = new HashMap<String, Level>();

	static {
		try {
			loadLevels();
		} catch (Exception e) {
			Logger.getLogger(Levels.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
		}
	}
	
	// loads information from the text file with all of the level info
	private static void loadLevels() throws Exception {
		FileReader fr = new FileReader("resources/Simple.txt");
		BufferedReader br = new BufferedReader(fr);

		String line = null;
		Level level = new Level();
		int blankLine = 0;
		while ((line = br.readLine()) != null) {
			if (line.trim().isEmpty()) { // accounts for white space
				blankLine++;
				continue;
			}
			if (blankLine >= 1) { 
				if (line.startsWith(";")) { // for example ; #1
					level.name = line.replace("; #", "");
					levels.put(level.name, level); // puts in map
					level = new Level();
				} else { 
					// updates level's fields
					level.info += replaceTile(line) + "\n";
					level.width = line.replaceAll("\\s+$", "").length() > level.width ? line.replaceAll("\\s+$", "").length() : level.width; 
					level.height++;
				}
			}
		}
		fr.close();
		br.close();
	}
	
	public static Level get(String name) {
		return levels.get(name);
	}
	
	public static Map<String, Level> getLevelMap() {
		return levels;
	}
	
	// replaces characters from file with easier-to-understand symbols
	private static String replaceTile(String line) {
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
