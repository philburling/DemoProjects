package com.blahsoft.ai;

import com.blahsoft.boardgame.Location;

/* This interface is for getting appropriate moves in a two-player board game according to a particular AI strategy*/

public interface AIStrategy {
	
	/* Retrieve the move that would be made according to the AI strategy for a particular state of the board
	 * represented by a char array. Note that 'null' should be returned if, for whatever reason, no move is possible. */
	public abstract Location getMove(char[][] board);

	/* Use this method to implement a strategy for how the player sets up the board */
	public void setUpBoard(char[][] board, int piecesToPlace);
	
	/* This method should be overridden from Object to provide the name of the AI in lower-case */
	public abstract String toString();
}
