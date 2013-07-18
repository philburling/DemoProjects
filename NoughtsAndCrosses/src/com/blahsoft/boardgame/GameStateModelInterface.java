package com.blahsoft.boardgame;

import java.util.ArrayList;

/*This interface can be used for any two-player turn based game.
 * Its purpose is to store the state of a game and expose methods for managing, reading and evaluating that state or potential changes to it.
 * If the game is to include more than two players, this class must be modified to have an array of opponent tokens
 */
public interface GameStateModelInterface extends Cloneable {
	
	/* The following three constants are used to represent the utility of the current state of a game for a specified player.*/
	public static final int WIN = 1;
	public static final int LOSS = -1;
	public static final int DRAWN = 0;
	public static final int GAME_UNFINISHED = -3;
	/* The following constants represent what a specific available move in this game would achieve if carried out */
	public static final int  BLOCKS_OPPONENT_WIN = -2; // This move prevents an opponent from winning in the next turn.
	public static final int WINNING_MOVE = 2; // Making this move will guarantee a win.

	public GameStateModelInterface clone();

	// Accessor and mutator functions...
	public ArrayList<Move> getAvailableMoves();
	public char getPlayerTurn(); // Find out who's turn it currently is.
	public int getUtility(char playerToken, char opponentToken);
	
	public void setPlayerTurn(char playerToken); // This can be used to set the starting player or to skip or grant an extra turns.
	public void nextTurn(); // Set the variable storing whose turn it is to the next player. This should be called after addMove.

	// Returns a new GameStateInterface object with a move added for the player with that token.
	public GameStateModelInterface addMove(Move move, char playerToken);

	/* This function assesses whether a particular move for a particular player would achieve a specified goal if carried out.
	 * For example. 'type' could be either BLOCKS_OPPONENT_WIN or WINNING_MOVE */ 
	public boolean isMoveType(Move move, int type, char playerToken, char opponentToken);
	


}
