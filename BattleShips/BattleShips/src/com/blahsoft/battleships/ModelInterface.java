package com.blahsoft.battleships;

import com.blahsoft.ai.AIStrategy;
import com.blahsoft.boardgame.Location;
import com.blahsoft.designpatterns.ObservableInterface;

/* Classes that implement this interface are responsible for storing the settings and game-state of a game of battleships. */
public interface ModelInterface extends ObservableInterface {
	
	/* These constants describe which phase the game is currently in */
	static final int SETTING_UP = 0;
	static final int IN_PROGRESS = 1;
	static final int GAME_OVER = 2;

	/* These constants represent the status of individual positions on a player's board. */
	static final char SHIP = 'S'; //i.e. A ship has been placed here.
	static final char HIT = 'H';
	static final char MISS = 'M';
	static final char UNMARKED = '\u0000';

	void initialiseGameState(); // This method should reset the game with any user-preference changes taken into account.
	void setUpAIBoard(Player opponent); //Use the AI module in the model to set up the board for this player.
	
	int getGridSize();	// Retrieve the width of the board (The board implemented should always be square).
	int getPhaseOfGame();
	Player getCurrentPlayer(); // Retrieves the player whose turn it is.
	String getAIStrategy(); // Find out the name of the AI strategy currently in use.
	boolean isOpponentAI(); // Use this to find out if the second player is set to be played by the computer or a human.

	void nextPlayer(); // Toggle whose turn it is.
	void setPhaseOfGame(int phaseOfGame);	
	void setOpponentIsAI(boolean opponentIsAI);
	void setAIStrategy(AIStrategy aiStrategy);

	void placePiece(Location location); // This is used to place a ship at the beginning of the game for the current player.
	void requestMove(Location location); // This is used by the current player to attack a square on the opponent's board.
	void requestAIMove(); // This causes the AI player to make a move.

}
