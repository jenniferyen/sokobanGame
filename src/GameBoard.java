import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//import java.io.BufferedReader;
//import java.io.BufferedWriter;
import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/*
 * This class contains most of the functionality and state of the game with various methods
 * to move the avatar, draw graphics, check if game is completed, etc. 
 * I found my game art from https://opengameart.org/content/sokoban-100-tiles. 
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {
	
	// private fields
	private Model model;
	private Level level;
	private static String levelString;
	private static char[][] levelMap;
	private BufferedImage images[] = new BufferedImage[5];
	private BufferedImage bg;
	private JLabel status;
	private boolean playing = true;
	private static ScoreManager sm = new ScoreManager();
	
    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 25;
	
    public GameBoard(JLabel status) {
        this.status = status;
        
    	// gets level and creates model based off of specified level
        levelString = JOptionPane.showInputDialog("Enter Level (1-18):");
    	level = Levels.get(levelString);
    	model = new Model(level);
    	
    	// instructions window
    	JOptionPane.showMessageDialog(this, "Welcome to Sokoban!\n"
		+ "Move the blocks to the goal tiles using the arrow keys.\n"
		+ "You win when all the blocks are moved to the goal tiles.\n"
		+ "You cannot move more than one block at a time. \n"
		+ "Try to win in as few movements as possible.");
    	
    	setFocusable(true);
    	
    	// reads graphics from file and returns buffered image
    	try {
    		images[0] = ImageIO.read(new File("graphics/Avatar.png"));
    		images[1] = ImageIO.read(new File("graphics/Block.png"));
    		images[2] = ImageIO.read(new File("graphics/GoalTile.png"));
    		images[3] = ImageIO.read(new File("graphics/NormalTile.png"));
    		images[4] = ImageIO.read(new File("graphics/Wall.png"));
    	} catch (IOException e) {
    		e.printStackTrace();
    	}	
    	bg = new BufferedImage(2000, 2000, BufferedImage.TYPE_INT_ARGB);
    	
    	// creates border around gameboard
    	setBorder(BorderFactory.createLineBorder(Color.BLACK));
    	
    	// timer that checks every 25 milliseconds
    	Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start();
        
        // keyListener to listen for keyPressed
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
               if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            	   	moveLeft();
            	   	GameMain.moved();
               } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            	   	moveRight();
            	   	GameMain.moved();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                	moveDown();
                	GameMain.moved();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                	moveUp();
                	GameMain.moved();
                }
            }
        });
    }
    
	// called by the timer
    void tick() {
    	if (playing) {
    		if (isCompleted()) {
    			// notifies user that level is complete
    			JOptionPane.showMessageDialog(this, "Congratulations! Level cleared!");
    			playing = false;
    			status.setText("Level Completed!");
    			// prompts user to enter name to save score
    			String name = JOptionPane.showInputDialog("Enter your name to save your score.");
    			sm.addScore(name, GameMain.getNumMoves());
    			JOptionPane.showMessageDialog(this, "Highscores: \n" + sm.getHighscoreString());
    			setFocusable(true);
    		}
    	}
    }
    
    // reset the game to its initial state
    public void reset() {
    	model = new Model(level);
    	repaint();
    	status.setText("Running...");
    	requestFocusInWindow();
    }

//    // FIX: Why doesn't the game work after I click the save button?
//	public void save() throws IOException {
//		StringBuilder builder = new StringBuilder();		
//		for (int j = 0; j < levelMap[0].length; j++) {
//			for (int i = 0; i < levelMap.length; i++) {
//				builder.append(levelMap[i][j] + "");
//			}
//			builder.append("BREAK");
//		}
//		BufferedWriter bw;
//		bw = new BufferedWriter(new FileWriter("gameData.txt"));
//		bw.write(builder.toString());
//		bw.close();
//		System.out.println("saved game to file");
//	}
//	
//	// FIX: Why doesn't the game work after I click the load button?
//	public void load() throws IOException {
//		FileReader fr = new FileReader("gameData.txt");
//		BufferedReader br = new BufferedReader(fr);
//		StringBuilder sb = new StringBuilder();
//		String line = br.readLine();
//		while (line != null) {
//			sb.append(line);
//			sb.append(System.lineSeparator());
//			line = br.readLine();
//		}
//		String everything = sb.toString();
//		
//		Level load = new Level();
//		load.setInfo(everything);
//		model = new Model(load, everything);
//		repaint();
//    	status.setText("Running...");
//    	requestFocusInWindow();
//		
//		System.out.println(everything);
//		fr.close();
//		br.close();
//		System.out.println("game loaded from file");
//	}
	
	// can choose a new game from the given levels
	public void newGame() throws IOException {
    	// gets level and creates model based off of specified level
        levelString = JOptionPane.showInputDialog("Enter Level (1-18):");
    	level = Levels.get(levelString);
    	model = new Model(level);
    	status.setText("Running...");
    	repaint(); // redraw
    	requestFocusInWindow();
    	setFocusable(true);
	}
    
    // repaint calls this every time
    @Override
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	
    	draw(bg.createGraphics());
    	g.drawImage(bg, 0, 0, null);
    }
    
    public void draw(Graphics2D g) {
    	g.setBackground(Color.GRAY);
    	g.clearRect(0, 0, getWidth(), getHeight());
    	g.translate(0, 25);
    	
    	levelMap = model.getLevelMap();
    	int tileSize = 64;
    	
    	/*
    	line = line.replace('#', 'W'); // wall
		line = line.replace('.', 'G'); // goal
		line = line.replace('$', 'B'); // block
		line = line.replace('@', 'A'); // avatar
		line = line.replace('*', 'X'); // block + goal
		line = line.replace('+', 'Y'); // avatar + goal
		line = line.replace(' ', 'S'); // space
    	 */
    	for (int x = 0; x < levelMap.length; x++) {
    		for (int y = 0; y < levelMap[0].length; y++) {
    			g.drawImage(images[3], x*tileSize, y*tileSize, null); // normal tiles
    			if (levelMap[x][y] == 'S') { // normal tiles
        			g.drawImage(images[3], x*tileSize, y*tileSize, null); 
    			}
    			if (levelMap[x][y] == 'W') { // wall
    				g.drawImage(images[4], x*tileSize, y*tileSize, null);
    			}
    			if (levelMap[x][y] == 'G') { // goal tiles
    				g.drawImage(images[2], x*tileSize, y*tileSize, null);
    			}
    			if (levelMap[x][y] == 'B') { // block
    				g.drawImage(images[1], x*tileSize, y*tileSize, null);
    			}
    			if (levelMap[x][y] == 'A') { // avatar
    		    	g.drawImage(images[0], x*tileSize, y*tileSize, null); 
    			}
    			if (levelMap[x][y] == 'X') { // block + goal (basically just block)
    				g.drawImage(images[1], x*tileSize, y*tileSize, null);
    			}
    			if (levelMap[x][y] == 'Y') { // avatar + goal (draws goal then avatar)
    				g.drawImage(images[2], x*tileSize, y*tileSize, null);
    		    	g.drawImage(images[0], x*tileSize, y*tileSize, null); 
    			}
    		}
    	}
    }
    

    public void moveUp() { 
    	for (int x = 0; x < levelMap.length; x++) {
    		for (int y = 0; y < levelMap[0].length; y++) {
    			// if avatar is trying to move into a space
    			if (levelMap[x][y] == 'A' && levelMap[x][y-1] == 'S') {
    				levelMap[x][y] = 'S';
    				levelMap[x][y-1] = 'A';
    				repaint();
    			}
    			// if avatar is trying to push a block into a space
    			if (levelMap[x][y] == 'A' && levelMap[x][y-1] == 'B' && levelMap[x][y-2] == 'S') {
    				levelMap[x][y] = 'S';
    				levelMap[x][y-1] = 'A';
    				levelMap[x][y-2] = 'B';
    				repaint();
    			}
    			// if avatar is trying to push a block into a goal tile
    			if (levelMap[x][y] == 'A' && levelMap[x][y-1] == 'B' && levelMap[x][y-2] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x][y-1] = 'A';
    				levelMap[x][y-2] = 'X';
    				repaint();
    			}
    			// if avatar is trying to push a block onto another goal tile
    			if (levelMap[x][y] == 'A' && levelMap[x][y-1] == 'X' && levelMap[x][y-2] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x][y-1] = 'Y';
    				levelMap[x][y-2] = 'X';
    				repaint();
    			}
    			// if avatar is trying to move to a goal tile 
    			if (levelMap[x][y] == 'A' && levelMap[x][y-1] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x][y-1] = 'Y';
    				repaint();
    			}
    			// if avatar is trying to move from a goal tile to another goal tile
    			if (levelMap[x][y] == 'Y' && levelMap[x][y-1] == 'G') {
    				levelMap[x][y] = 'G';
    				levelMap[x][y-1] = 'Y';
    				repaint();
    			}
    			// if avatar is trying to move from a goal tile to an empty space
    			if (levelMap[x][y] == 'Y' && levelMap[x][y-1] == 'S') {
    				levelMap[x][y] = 'G';
    				levelMap[x][y-1] = 'A';
    				repaint();
    			}
    			// if avatar is on a goal tile and trying to push a block onto another goal
    			if (levelMap[x][y] == 'Y' && levelMap[x][y-1] == 'X' && levelMap[x][y-2] == 'G') {
    				levelMap[x][y] = 'G';
    				levelMap[x][y-1] = 'Y';
    				levelMap[x][y-2] = 'X';
    				repaint();
    			}
    			// if avatar is on a goal and trying to push a block on a goal into an empty space 
    			if (levelMap[x][y] == 'Y' && levelMap[x][y-1] == 'X' && levelMap[x][y-2] == 'S') {
    				levelMap[x][y] = 'G';
    				levelMap[x][y-1] = 'Y';
    				levelMap[x][y-2] = 'B';
    				repaint();
    			}
    		}
    	}
    }
    
    public void moveDown() {
    	for (int x = 0; x < levelMap.length; x++) {
    		for (int y = 0; y < levelMap[0].length; y++) {
    			// if avatar is trying to move into a space
    			if (levelMap[x][y] == 'A' && levelMap[x][y+1] == 'S') {
    				levelMap[x][y] = 'S';
    				levelMap[x][y+1] = 'A';
    				repaint();
    				break;
    			}
    			// if avatar is trying to push a block into a space
    			if (levelMap[x][y] == 'A' && levelMap[x][y+1] == 'B' && levelMap[x][y+2] == 'S') {
    				levelMap[x][y] = 'S';
    				levelMap[x][y+1] = 'A';
    				levelMap[x][y+2] = 'B';
    				repaint();
    				break;
    			}
    			// if avatar is trying to push a block into a goal tile
    			if (levelMap[x][y] == 'A' && levelMap[x][y+1] == 'B' && levelMap[x][y+2] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x][y+1] = 'A';
    				levelMap[x][y+2] = 'X';
    				repaint();
    				break;
    			}
    			// if avatar is trying to push a block onto another goal tile
    			if (levelMap[x][y] == 'A' && levelMap[x][y+1] == 'X' && levelMap[x][y+2] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x][y+1] = 'Y';
    				levelMap[x][y+2] = 'X';
    				repaint();
    				break;
    			}
    			// if avatar is trying to move to a goal tile 
    			if (levelMap[x][y] == 'A' && levelMap[x][y+1] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x][y+1] = 'Y';
    				repaint();
    				break;
    			}
    			// if avatar is trying to move from a goal tile to another goal tile
    			if (levelMap[x][y] == 'Y' && levelMap[x][y+1] == 'G') {
    				levelMap[x][y] = 'G';
    				levelMap[x][y+1] = 'Y';
    				repaint();
    				break;
    			}
    			// if avatar is trying to move from a goal tile to an empty space
    			if (levelMap[x][y] == 'Y' && levelMap[x][y+1] == 'S') {
    				levelMap[x][y] = 'G';
    				levelMap[x][y+1] = 'A';
    				repaint();
    				break;
    			}
    			// if avatar is on a goal tile and trying to push a block onto another goal
    			if (levelMap[x][y] == 'Y' && levelMap[x][y+1] == 'X' && levelMap[x][y+2] == 'G') {
    				levelMap[x][y] = 'G';
    				levelMap[x][y+1] = 'Y';
    				levelMap[x][y+2] = 'X';
    				repaint();
    				break;
    			}
    			// if avatar is on a goal and trying to push a block on a goal into an empty space 
    			if (levelMap[x][y] == 'Y' && levelMap[x][y+1] == 'X' && levelMap[x][y+2] == 'S') {
    				levelMap[x][y] = 'G';
    				levelMap[x][y+1] = 'Y';
    				levelMap[x][y+2] = 'B';
    				repaint();
    				break;
    			}
    		}
    	}
    }
    
    public void moveRight() {
    	for (int x = 0; x < levelMap.length; x++) {
    		for (int y = 0; y < levelMap[0].length; y++) {
    			// if avatar is trying to move into a space
    			if (levelMap[x][y] == 'A' && levelMap[x+1][y] == 'S') {
    				levelMap[x][y] = 'S';
    				levelMap[x+1][y] = 'A';
    				repaint();
    				return;
    			} 
    			// if avatar is trying to push a block into a space
    			if (levelMap[x][y] == 'A' && levelMap[x+1][y] == 'B' && levelMap[x+2][y] == 'S') {
    				levelMap[x][y] = 'S';
    				levelMap[x+1][y] = 'A';
    				levelMap[x+2][y] = 'B';
    				repaint();
    				return;
    			}
    			// if avatar is trying to push a block into a goal tile
    			if (levelMap[x][y] == 'A' && levelMap[x+1][y] == 'B' && levelMap[x+2][y] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x+1][y] = 'A';
    				levelMap[x+2][y] = 'X';
    				repaint();
    				return;
    			}
    			// if avatar is trying to push a block onto another goal tile
    			if (levelMap[x][y] == 'A' && levelMap[x+1][y] == 'X' && levelMap[x+2][y] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x+1][y] = 'Y';
    				levelMap[x+2][y] = 'X';
    				repaint();
    				return;
    			}
    			// if avatar is trying to move to a goal tile 
    			if (levelMap[x][y] == 'A' && levelMap[x+1][y] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x+1][y] = 'Y';
    				repaint();
    				return;
    			}
    			// if avatar is trying to move from a goal tile to another goal tile
    			if (levelMap[x][y] == 'Y' && levelMap[x+1][y] == 'G') {
    				levelMap[x][y] = 'G';
    				levelMap[x+1][y] = 'Y';
    				repaint();
    				return;
    			}
    			// if avatar is trying to move from a goal tile to an empty space
    			if (levelMap[x][y] == 'Y' && levelMap[x+1][y] == 'S') {
    				levelMap[x][y] = 'G';
    				levelMap[x+1][y] = 'A';
    				repaint();
    				return;
    			}
    			// if avatar is on a goal tile and trying to push a block onto another goal
    			if (levelMap[x][y] == 'Y' && levelMap[x+1][y] == 'X' && levelMap[x+2][y] == 'G') {
    				levelMap[x][y] = 'G';
    				levelMap[x+1][y] = 'Y';
    				levelMap[x+2][y] = 'X';
    				repaint();
    				return;
    			}
    			// if avatar is on a goal and trying to push a block on a goal into an empty space 
    			if (levelMap[x][y] == 'Y' && levelMap[x+1][y] == 'X' && levelMap[x+2][y] == 'S') {
    				levelMap[x][y] = 'G';
    				levelMap[x+1][y] = 'Y';
    				levelMap[x+2][y] = 'B';
    				repaint();
    				return;
    			}
    		}
    	}
    }
    
    public void moveLeft() {
    	for (int x = 0; x < levelMap.length; x++) {
    		for (int y = 0; y < levelMap[0].length; y++) {
    			// if avatar is trying to move into a space
    			if (levelMap[x][y] == 'A' && levelMap[x-1][y] == 'S') {
    				levelMap[x][y] = 'S';
    				levelMap[x-1][y] = 'A';
    				repaint();
    			}
    			// if avatar is trying to push a block into a space
    			if (levelMap[x][y] == 'A' && levelMap[x-1][y] == 'B' && levelMap[x-2][y] == 'S') {
    				levelMap[x][y] = 'S';
    				levelMap[x-1][y] = 'A';
    				levelMap[x-2][y] = 'B';
    				repaint();
    			}
    			// if avatar is trying to push a block into a goal tile
    			if (levelMap[x][y] == 'A' && levelMap[x-1][y] == 'B' && levelMap[x-2][y] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x-1][y] = 'A';
    				levelMap[x-2][y] = 'X';
    				repaint();
    			}
    			// if avatar is trying to push a block onto another goal tile
    			if (levelMap[x][y] == 'A' && levelMap[x-1][y] == 'X' && levelMap[x-2][y] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x-1][y] = 'Y';
    				levelMap[x-2][y] = 'X';
    				repaint();
    			}
    			// if avatar is trying to move to a goal tile 
    			if (levelMap[x][y] == 'A' && levelMap[x-1][y] == 'G') {
    				levelMap[x][y] = 'S';
    				levelMap[x-1][y] = 'Y';
    				repaint();
    			}
    			// if avatar is trying to move from a goal tile to another goal tile
    			if (levelMap[x][y] == 'Y' && levelMap[x-1][y] == 'G') {
    				levelMap[x][y] = 'G';
    				levelMap[x-1][y] = 'Y';
    				repaint();
    			}
    			// if avatar is trying to move from a goal tile to an empty space
    			if (levelMap[x][y] == 'Y' && levelMap[x-1][y] == 'S') {
    				levelMap[x][y] = 'G';
    				levelMap[x-1][y] = 'A';
    				repaint();
    			}
    			// if avatar is on a goal tile and trying to push a block onto another goal
    			if (levelMap[x][y] == 'Y' && levelMap[x-1][y] == 'X' && levelMap[x-2][y] == 'G') {
    				levelMap[x][y] = 'G';
    				levelMap[x-1][y] = 'Y';
    				levelMap[x-2][y] = 'X';
    				repaint();
    				return;
    			}
    			// if avatar is on a goal and trying to push a block on a goal into an empty space 
    			if (levelMap[x][y] == 'Y' && levelMap[x-1][y] == 'X' && levelMap[x-2][y] == 'S') {
    				levelMap[x][y] = 'G';
    				levelMap[x-1][y] = 'Y';
    				levelMap[x-2][y] = 'B';
    				repaint();
    				return;
    			}
    		}
    	}
    }
   
    /*
     * HELPER METHODS
     */
    
    // checks if all blocks are pushed to goal tiles
    public boolean isCompleted() {
    	int emptyGoalCount = 0;
    	int avatarAndGoalCount = 0;
    	for (int x = 0; x < levelMap.length; x++) {
    		for (int y = 0; y < levelMap[0].length; y++) {
    			// if there is an empty goal, increment. you don't want empty goals!
                if (levelMap[x][y] == 'G') {
                	emptyGoalCount++;
                }
                // if the avatar is on a goal tile
                if (levelMap[x][y] == 'Y') {
                	avatarAndGoalCount++;
                }
            }
        }
    	if (emptyGoalCount == 0 && avatarAndGoalCount == 0) {
    		return true;
    	}
        return false;
    } 

    // gets level that is being played (used in ScoreManager.java)
	public static String getLevelString() {
		return levelString;
	}
    
}
