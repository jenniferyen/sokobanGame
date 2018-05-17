=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: Jennifer Yen
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

  1. Appropriately modeling state using 2D arrays: My gameboard is a char[][] where the characters can be A, S, X, Y, B, or G. A will draw an avatar. S will draw a normal tile. X will draw a block (means that the block is on a goal tile). Y will draw the avatar on a goal tile (draws goal tile than avatar). B will draw a block. G will draw a goal tile. To move, my move functions switch the position of the characters in the char[][] and redraws the board. 

  2. Appropriately modeling state using collections: I used an ArrayList<String, HighScores> to manage high scores. I also used a Map to map a String specifying the level to each Level object. 

  3. File I/O: using I/O to parse a file: I read my levels from a text file (see resources/Simple.txt), then converted it to easier-to-use characters, and drew my game board. I also implemented a save game function that saves the current board in a gameData.txt file and a load game function that loads the saved game. The save game and load game functions do save (write out to gameData.txt) and load the information (read from gameData.txt) but I am having trouble repainting the saved game. I wrote print functions in my save() and load(). 

  4. Using Unit on a testable component: I made JUnit tests for the high scores component of my game and also tried to test the Model and GameBoard classes. However, for the latter two and especially GameBoard.java, I had more difficulty because many of the methods were interconnected with one another. This informs me that I could have better separated the functionality of these classes. Some methods I tested included getScore(), addScore(), sort(), and replaceTile(). 


=========================
=: My Implementation :=
=========================

GameBoard.java: This class contains most of the functionality and state of the game with various methods/features as follows
1. instructions window that opens before you can play the game
2. reads graphics from the graphics folder containing different images
3. checks if keys are pressed and if so, moves avatar/blocks accordingly by rearranging the char[][] and repainting
4. checks if the level is completed and if so, notifies player and prompts player to input name to save the score. another JOptionPane will pop up listing the top 5 highest scores. 
5. reset() method for the reset button
6. save game and load game feature - a little buggy
7. paintComponent() and draw() that draws the graphics of the game
8. moveUp(), moveDown(), moveRight(), and moveLeft()
9. isComplete() to check if the level is complete (all blocks moved to goal tiles) 
	
GameMain.java: This class contains the javaSwing setup - frames, panels, buttons, etc. Essentially, builds the game. There is a frame. Inside the frame is the status panel, board, and info panel. The info panel contains the reset, save game, load game, new game, buttons and the label informing the player how many movements he/she has taken. The status panel says "running..." when the game is in action and "Level complete!" when the level is cleared.

HighScores.java: This class creates the HighScores object which stores a score (# of movements) and name. 

Level.java: This class creates a level used in the game. Its fields include name, width of level, height of level, and info. 

Levels.java: This class organizes ALL of the levels using a Map<String, Level> where the String is the level number. It contains methods to load levels from a text file, get private fields, and replace characters in the text file with easier-to-use symbols.

Model.java: This class contains the model created using a specified level. The model consists of a char[][] array with the easier-to-use symbols. 

ScoreComparator.java: This class creates a comparator that helps sort the list of high scores in ScoreManager. 

ScoreManager.java: This class contains methods that manage everything related to high scores as follows: 
1. getScores() to return the ArrayList of high scores for the specified level
2. sort() to sort the high scores using the Comparator
3. addScore() to add a new high score to the ArrayList
4. loadScore() to read from the specified high score file 
5. updateScore() to update the specified high score file with a new score
6. getHighscoreString() to return a list of the top 5 high scores for a specified level
7. get HighscoreFile() to return which file to read from and write to (there are 18 files - one for each level) 

UnitTests.java: Contains the JUnit tests for my game. 


Significant Challenges: 
I initially struggled with reading the images into the game and drawing it. Converting the text file into something that could be drawn to display the game board was definitely a challenge.
I also struggled with the save game/load game functionality. Currently, my save game does same my game board to a gameData.txt file but there are some technicalities with reloading the board.  


Design Evaluation / Encapsulation
My GameBoard.java is a very large class and there are ways to better separate the functionality of that classes into smaller classes. Something I played with was creating separate Objects for the Avatar, Blocks, and Tiles that have their own paintComponent() / draw() methods but as I tried that, I realized that drawing all the graphics in one class was easier because I also read all the graphics in the same class. However, if I could refactor GameBoard.java, I would separate the character/block movements from other methods in that class. 


========================
=: External Resources :=
========================

I found my game art from https://opengameart.org/content/sokoban-100-tiles. 
I found the text file to read my levels from http://www.sourcecode.se/sokoban/levels
