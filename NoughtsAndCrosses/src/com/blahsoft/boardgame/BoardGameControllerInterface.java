package com.blahsoft.boardgame;

/* This interface is for translating actions originating from the user-interface of a board game into 
 * manipulations of a 'view-model' object containing game-options and an encapsulated game-state object */
public interface BoardGameControllerInterface {
	
	public void setOpponentMode(String mode); // Set whether an opponent is AI-controlled or controlled by another human.
	public void setStartingPlayer(char player);
	public void setAIStrategy(String mode);
	/* Initialise a new game using the current settings. */
	public void newGame();
	/* Start a game that has just been initialised. For example: This could be used to invoke the main thread. */
	public void startGame();
	/* Request a position on the board for a move. The player the move is handled by the state maintained by the game's model. */
	public void requestMove(Move location);

}
