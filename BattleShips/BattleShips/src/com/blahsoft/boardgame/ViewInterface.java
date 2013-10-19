package com.blahsoft.boardgame;

import com.blahsoft.designpatterns.ObserverInterface;


/* This interface should be used to implement a front-end for user interaction with a board-game. */
public interface ViewInterface extends ObserverInterface {

	/*
	 * Visual display updates
	 */
	
	void hideBoards(); // Use this to prevent a player from seeing their opponents board in full between turns. 

	void showBoards();

	void disableControls(); // Use this to disable a set of controls while a game is in progress.
	
	void enableControls(); 
	
	/* 
	 * Methods for communication to the user
	 */

	void displayWelcomeMessage();

	void displayInstructions();	

	void notifyPlayerSetUp(); // Used to alert a player it is their turn to set up their pieces.
	
	void notifyAlreadyPlacedHere(); // To be triggered if the user tries to place a piece in an occupied location.
	
	void notifySetUpComplete(); // To be triggered when the initial set-up phase of a game is complete.

	void notifyPlayerTurn(); // Used to tell the current user it is their turn. 
	
	void notifyAlreadyMovedHere();
	
	void fetchNextPlayer(); // Tell the player to get the next player.

	void notifyGameOver(String player); // Where 'player' is the name or ID of the  winner.

}