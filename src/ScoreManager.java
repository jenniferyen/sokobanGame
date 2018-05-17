import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.Collections;

/*
 * This class contains methods that manage everything related to highscores. 
 */
public class ScoreManager {
	
	// private fields
	private ArrayList<HighScores> scores;
	private static final String HIGHSCORE_FILE1 = "highscores/highscores1.txt";
	private static final String HIGHSCORE_FILE2 = "highscores/highscores2.txt";
	private static final String HIGHSCORE_FILE3 = "highscores/highscores3.txt";
	private static final String HIGHSCORE_FILE4 = "highscores/highscores4.txt";
	private static final String HIGHSCORE_FILE5 = "highscores/highscores5.txt";
	private static final String HIGHSCORE_FILE6 = "highscores/highscores6.txt";
	private static final String HIGHSCORE_FILE7 = "highscores/highscores7.txt";
	private static final String HIGHSCORE_FILE8 = "highscores/highscores8.txt";
	private static final String HIGHSCORE_FILE9 = "highscores/highscores9.txt";
	private static final String HIGHSCORE_FILE10 = "highscores/highscores10.txt";
	private static final String HIGHSCORE_FILE11 = "highscores/highscores11.txt";
	private static final String HIGHSCORE_FILE12 = "highscores/highscores12.txt";
	private static final String HIGHSCORE_FILE13 = "highscores/highscores13.txt";
	private static final String HIGHSCORE_FILE14 = "highscores/highscores14.txt";
	private static final String HIGHSCORE_FILE15 = "highscores/highscores15.txt";
	private static final String HIGHSCORE_FILE16 = "highscores/highscores16.txt";
	private static final String HIGHSCORE_FILE17 = "highscores/highscores17.txt";
	private static final String HIGHSCORE_FILE18 = "highscores/highscores18.txt";
	
	ObjectOutputStream os = null;
	ObjectInputStream is = null;
	
	// contains the ArrayList of high scores
	public ScoreManager() {
		scores = new ArrayList<HighScores>();
	}
	
	// gets, sorts, and returns high scores for a specified level
	public ArrayList<HighScores> getScores() {
		loadScore();
		sort();
		return scores;
	}
	
	// used in testing without having to loadScoreFile()
	public ArrayList<HighScores> getScoresTesting() {
		return scores;
	}
	
	// sort high scores using the comparator
	public void sort() {
		ScoreComparator comparator = new ScoreComparator();
		Collections.sort(scores, comparator);
	}
	
	// adds a high score the ArrayList
	public void addScore(String name, int score) {
		loadScore();
		scores.add(new HighScores(name, score));
		updateScore();
	}
	
	// used in testing without having to read from / write to  file
	public void addScoreTesting(String name, int score) {
		scores.add(new HighScores(name, score));
	}
	
	// reads high scores from specified file 
	@SuppressWarnings("unchecked")
	public void loadScore() {
		try {
			String fileToRead = getHighscoreFile();
			is = new ObjectInputStream(new FileInputStream(fileToRead));
			scores = (ArrayList<HighScores>) is.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// updates specified file with a new high score
	public void updateScore() {
		try {
			String fileToRead = getHighscoreFile();
			os = new ObjectOutputStream(new FileOutputStream(fileToRead));
			os.writeObject(scores);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.flush();
					os.close();
				}
			}  catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// returns list of high scores (5 max) for a specified level
	public String getHighscoreString() {
        String highscoreString = "";
        int max = 5;

        ArrayList<HighScores> scores;
        scores = getScores();

        int i = 0;
        int x = scores.size();
        if (x > max) {
            x = max;
        }
        while (i < x) {
            highscoreString += (i + 1) + ".\t" + scores.get(i).getName() + "\t\t" + scores.get(i).getScore() + "\n";
            i++;
        }
        return highscoreString;
	}
	
	// specifies which file to read from and write to
	public String getHighscoreFile() {
		String fileToReturn = "";
		if (GameBoard.getLevelString().equals("1")) {
			fileToReturn = HIGHSCORE_FILE1;
		}
		else if (GameBoard.getLevelString().equals("2")) {
			fileToReturn = HIGHSCORE_FILE2;
		}
		else if (GameBoard.getLevelString().equals("3")) {
			fileToReturn = HIGHSCORE_FILE3;
		}
		else if (GameBoard.getLevelString().equals("4")) {
			fileToReturn = HIGHSCORE_FILE4;
		}
		else if (GameBoard.getLevelString().equals("5")) {
			fileToReturn = HIGHSCORE_FILE5;
		}
		else if (GameBoard.getLevelString().equals("6")) {
			fileToReturn = HIGHSCORE_FILE6;
		}
		else if (GameBoard.getLevelString().equals("7")) {
			fileToReturn = HIGHSCORE_FILE7;
		}
		else if (GameBoard.getLevelString().equals("8")) {
			fileToReturn = HIGHSCORE_FILE8;
		}
		else if (GameBoard.getLevelString().equals("9")) {
			fileToReturn = HIGHSCORE_FILE9;
		}
		else if (GameBoard.getLevelString().equals("10")) {
			fileToReturn = HIGHSCORE_FILE10;
		}
		else if (GameBoard.getLevelString().equals("11")) {
			fileToReturn = HIGHSCORE_FILE11;
		}
		else if (GameBoard.getLevelString().equals("12")) {
			fileToReturn = HIGHSCORE_FILE12;
		}
		else if (GameBoard.getLevelString().equals("13")) {
			fileToReturn = HIGHSCORE_FILE13;
		}
		else if (GameBoard.getLevelString().equals("14")) {
			fileToReturn = HIGHSCORE_FILE14;
		}
		else if (GameBoard.getLevelString().equals("15")) {
			fileToReturn = HIGHSCORE_FILE15;
		}
		else if (GameBoard.getLevelString().equals("16")) {
			fileToReturn = HIGHSCORE_FILE16;
		}
		else if (GameBoard.getLevelString().equals("17")) {
			fileToReturn = HIGHSCORE_FILE17;
		}
		else if (GameBoard.getLevelString().equals("18")) {
			fileToReturn = HIGHSCORE_FILE18;
		}
		return fileToReturn;
	}
		
}
