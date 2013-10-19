package com.blahsoft.boardgame;

import com.blahsoft.ai.AIStrategy;
import com.blahsoft.designpatterns.ObservableInterface;

/* Classes that implement this interface are responsible for hosting the game-settings of a board game  
 * and conducting the game according to them. The game flow-of-control is not conducted in a loop,
 * but instead event driven by requests to make a move in a particular location on the game board (these 
 * could originate from a user-interface). Whether this is granted and which player the move is for is 
 * determined by information from the GameStateModelInterface object and the game settings variables stored 
 * here. If AI is enabled, then a subsequent move will be made by the AI. An initial move will be made by 
 * the AI when the game is started if AI is enabled and is the starting player.
 */

public interface Model extends ObservableInterface {
	
	/* This class will store information on whether a particular player of the game is AI controlled or human controlled. */
	public static final int AI_PLAYER = 0;
	public static final int HUMAN_PLAYER = 1;
	
	int getOpponentMode(); // Determine if a player is either an AI_PLAYER or a HUMAN_PLAYER;
	String getAIStrategy(); // Find out the name of the AI strategy currently in use
	char getStartingPlayer(); // Get a char representing the starting player.
	boolean gameIsOver();
	public GameStateModelInterface getGameState(); // Get an object that wraps the state of the board and whose turn it is. 
	
	void setOpponentMode(int mode);
	void setAIStrategy(AIStrategy aiStrategy);
	void setStartingPlayer(char player);
	
	void resetBoard();
	void startGame();
	void requestMove(Move location);

}
