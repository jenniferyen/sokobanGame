import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;


public class UnitTests {
	// using assertEquals, assertTrue, assertFalse

	/*
	 * HIGHSCORE TESTS (ScoreManager.java, ScoreComparator.java, HighScores.java)
	 */
	@Test
	public void SM_getScores_Name() {
		ArrayList<HighScores> scores = new ArrayList<HighScores>();
		HighScores highscore1 = new HighScores("Jen", 100);
		scores.add(highscore1);
		assertEquals("gets name associated with high score", "Jen", scores.get(0).getName());
	}

	@Test
	public void SM_getScores_Score() {
		ArrayList<HighScores> scores = new ArrayList<HighScores>();
		HighScores highscore1 = new HighScores("Jen", 100);
		scores.add(highscore1);
		assertEquals("gets score associated with high score", 100, scores.get(0).getScore());
	}

	@Test
	public void SM_addScore() {
		ScoreManager sm = new ScoreManager();
		sm.addScoreTesting("Jen", 100);
		sm.addScoreTesting("Steph", 99);
		assertEquals("if added, score at index 1 should be 99", 99, sm.getScoresTesting().get(1).getScore());
	}

	@Test
	public void SM_sort() {
		ScoreManager sm = new ScoreManager();
		sm.addScoreTesting("Jen", 100);
		sm.addScoreTesting("Steph", 99);
		sm.sort();
		assertEquals("index 0 should be lower score", 99, sm.getScoresTesting().get(0).getScore());
	}

	/*
	 * MODEL AND LEVEL TESTS (Model.java, Levels.java, Level.java)
	 */
	@Test
	public void L_replaceTile() {
		Level level = new Level();
		assertEquals("walls # -> W", "WWW", level.replaceTileTesting("###"));
		assertEquals("goals . -> G", "GGG", level.replaceTileTesting("..."));
		assertEquals("blocks $ -> B", "BBB", level.replaceTileTesting("$$$"));
		assertEquals("avatar @ -> A", "AAA", level.replaceTileTesting("@@@"));
		assertEquals("block + goal * -> X", "XXX", level.replaceTileTesting("***"));
		assertEquals("avatar + goal + -> Y", "YYY", level.replaceTileTesting("+++"));
		assertEquals("space   -> S", "SSS", level.replaceTileTesting("   "));
	}

	@Test
	public void M_model() {
		Level level = new Level();
		level.setInfo("test");
		Model model = new Model(level, true);
		assertEquals("level info should transfer to model's", "test", model.getLevelInfo());
	}

	/*
	 * GAMEBOARD TESTS (GameBoard.java)
	 */

}
