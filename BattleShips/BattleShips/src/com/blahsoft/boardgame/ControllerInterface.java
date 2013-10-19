package com.blahsoft.boardgame;

/* This interface is for translating actions originating from the user-interface (view) of a board game into 
 * manipulations of a model object which encapsulates the settings and state of a board game.
 * The methods below could potentially be used to govern both notifications to the user and state updates to the model*/

public interface ControllerInterface {

	/* ThisChanges whether the second player in the game is AI or human. */
	void setOpponentIsAI(boolean opponentIsAI);
	
	void setAIStrategy(String mode);
	
	/* Initialise a new game using the current settings. */
	void newGame();

	/* Request a position for a player to place a piece when setting up. */ 
	void requestPlacement(Location boardLocationClicked);
	
	/* Request a position on the board for a move. */
	void requestMove(Location location);
	

}
