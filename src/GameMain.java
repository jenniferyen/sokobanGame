import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/*
 * This contains the javaSwing setup - frames, panels, buttons, etc. 
 */

public class GameMain implements Runnable {
	private static GameBoard board;
	private static int numMoves = 0;
	private final static JLabel moveLabel = new JLabel("Number of Moves: " + numMoves);
	
	// if the avatar is moved 
	public static void moved() {
		numMoves++;
		moveLabel.setText("Number of Moves: " + numMoves);
	}
	
	public static int getNumMoves() {
		return numMoves;
	}
	
	public static GameBoard getGameBoard() {
		return board;
	}
	
	public void run() {
		// title of game
		final JFrame frame = new JFrame("SOKOBAN");
		frame.setLocation(300, 300); // EXPERIMENT w this
		
		// status of game
		final JPanel status_panel = new JPanel();
		frame.add(status_panel, BorderLayout.SOUTH);
		final JLabel status = new JLabel("Running...");
		status_panel.add(status);
		
		// board
		board = new GameBoard(status);
		frame.add(board, BorderLayout.CENTER);
		
		final JPanel info_panel = new JPanel();
		frame.add(info_panel, BorderLayout.NORTH);
		
		// reset button
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        info_panel.add(reset);
        
//        // save game button
//        final JButton save = new JButton("Save Game");
//        save.addActionListener(new ActionListener() {
//        	public void actionPerformed(ActionEvent e) {
//        		try {
//					board.save();
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
//        	}
//        });
//        info_panel.add(save);
//        
//        // load game button
//        final JButton load = new JButton("Load Game");
//        load.addActionListener(new ActionListener() {
//        	public void actionPerformed(ActionEvent e) {
//        		try {
//					board.load();
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
//        	}
//        });
//        info_panel.add(load);
        
        // new game button
        final JButton newGame = new JButton("New Game");
        newGame.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
					numMoves = 0;
					board.newGame();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
        	}
        });
        info_panel.add(newGame);
        
        // game movements
        info_panel.add(moveLabel);
		
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        board.reset();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new GameMain());
	}
}
