package com.blahsoft.battleships;

import java.util.ArrayList;
import java.util.Random;

import com.blahsoft.ai.AIStrategy;
import com.blahsoft.boardgame.Location;
import com.blahsoft.boardgame.Location2D;

/* This class implements a simple AI for a game of BattleShips where the board set-up and moves are generated randomly */
public class AIRandom implements AIStrategy {
	
	/* N.B. This method returns 'null' if no move is found */ 
	@Override
	public Location getMove(char[][] board) {
		
		// Get available moves
		ArrayList<Location2D> availableMoves = new ArrayList<Location2D>();
		for (int x=0; x<board.length; x++) {
			for (int y=0; y<board.length; y++) {
				if (board[x][y] != ModelInterface.HIT && board[x][y] != ModelInterface.MISS){
					availableMoves.add(new Location2D(x,y));
				}
			}
		}
		// Pick one of these moves at random, then return it.
		Random randomNumberGenerator = new Random(System.nanoTime());
		return availableMoves.get(randomNumberGenerator.nextInt(availableMoves.size()));
	}	
	
	@Override
	// N.B. This does not change any value of 'piecesToPlace that may be stored elsewhere.
	public void setUpBoard(char[][] board, int piecesToPlace) {
		
		// Get available positions
		ArrayList<Location2D> availableLocations = new ArrayList<Location2D>();
		for (int x=0; x<board.length; x++) {
			for (int y=0; y<board.length; y++) {
				if (board[x][y] != ModelInterface.SHIP){
					availableLocations.add(new Location2D(x,y));
				}
			}
		}
		Random randomNumberGenerator = new Random(System.nanoTime());

		while (piecesToPlace > 0) {
			if (availableLocations.size() <= 0) { // To prevent an infinite loop in the error case where pieces > locations
				return;
			}
			int moveIndex = randomNumberGenerator.nextInt(availableLocations.size());
			Location2D move = availableLocations.get(moveIndex);;
			board[move.getX()][move.getY()] = ModelInterface.SHIP;
			availableLocations.remove(moveIndex);
			piecesToPlace--;
		}
	}

	@Override
	public String toString() {
		return "random";
	}

}
