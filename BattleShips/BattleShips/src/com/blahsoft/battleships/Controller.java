package com.blahsoft.battleships;

import com.blahsoft.boardgame.ControllerInterface;
import com.blahsoft.boardgame.Location;
import com.blahsoft.boardgame.Location2D;
import com.blahsoft.boardgame.ViewInterface;

/* This class implements a controller for a board game.
 * It should be used to translate requests from a user-interface into updates on a model object and 
 * also corresponding notifications and changes to the user-interface.
 * It is particularly suited to BattleShips or a game analogous to it. */ 
public class Controller implements ControllerInterface {

	private ModelInterface model;
	private ViewInterface view;
	
	public Controller (ModelInterface model) {
		this.model = model;
		this.view = new View(this, model);
		view.displayWelcomeMessage();
	}
	
	@Override
	public void setOpponentIsAI(boolean opponentIsAI) {
		if (opponentIsAI) {
			model.setOpponentIsAI(true);			
		} else {
			model.setOpponentIsAI(false);
		}
	}
	
	@Override
	public void setAIStrategy(String mode) {		
		if (mode.equalsIgnoreCase("random")) {
			model.setAIStrategy(new AIRandom());	
		}
	}
	
	/* Initialise a new game with the current settings applied */
	@Override
	public void newGame() {
		model.initialiseGameState(); // N.B. This will change the phase to 'SETTING_UP'
		view.disableControls();
		view.notifyPlayerSetUp();
	}
	
	@Override
	/* A request from the GUI to add a counter in a particular board location before the start of the game.
	 * Which move this player is for is determined by the variable in the model representing whose turn it is.
	 * This method also updates the state of the game and the View object according to the success of these requests. */
	public void requestPlacement(Location location) {
		if (model.getPhaseOfGame() != ModelInterface.SETTING_UP) {
			return;
		} else {
			Location2D location2D = ((Location2D)location);
			Player currentPlayer = model.getCurrentPlayer();
			if (currentPlayer.getBoard()[location2D.getX()][location2D.getY()] == Model.SHIP) {
				view.notifyAlreadyPlacedHere();
				return;
			}
			if (currentPlayer.getPiecesToPlace() > 1) {	
				model.placePiece(location);
			} else {
				model.placePiece(location);
				view.notifySetUpComplete();
				view.hideBoards();
				Player opponent = currentPlayer.getOpponent();
				if(currentPlayer.getOpponent().isAI()) {
					view.showBoards();
					model.setUpAIBoard(opponent);
					model.setPhaseOfGame(ModelInterface.IN_PROGRESS);
				} else {
					model.nextPlayer();
					if (opponent.getPiecesToPlace() > 0) {
						view.notifyPlayerSetUp();
					} else {
						model.setPhaseOfGame(ModelInterface.IN_PROGRESS);
						view.notifyPlayerTurn();
					}
					view.showBoards();				
				}
			}
		}
	}

	/* A request from the GUI to make a move for the player whose turn it is.
	 * Which move this player is for is determined by the variable in the model representing whose turn it is. 
	 * This method also updates the state of the game and the View object according to the success of these requests. */
	@Override
	public void requestMove(Location location) {
		if (model.getPhaseOfGame() != ModelInterface.IN_PROGRESS) {
			return;
		} else {
			Location2D location2D = ((Location2D)location);
			Player currentPlayer = model.getCurrentPlayer();
			Player opponent = currentPlayer.getOpponent();
			char[][] opponentBoard = opponent.getBoard();
			if (opponentBoard[location2D.getX()][location2D.getY()] == Model.HIT ||
				opponentBoard[location2D.getX()][location2D.getY()] == Model.MISS) {
				view.notifyAlreadyMovedHere();
				return;
			}
			model.requestMove(location);
			if (opponent.getPiecesRemaining() == 0) {
				view.notifyGameOver(currentPlayer.getId());
				model.setPhaseOfGame(ModelInterface.GAME_OVER);
				view.enableControls();
			} else {			
				if(opponent.isAI()) {
						model.requestAIMove();
					if (currentPlayer.getPiecesRemaining() == 0) {
						view.notifyGameOver(opponent.getId());
						model.setPhaseOfGame(ModelInterface.GAME_OVER);
						view.enableControls();
					}
				} else {
					view.fetchNextPlayer();
					view.hideBoards();
					model.nextPlayer();
					view.notifyPlayerTurn();
					view.showBoards();
				}
			}
		}
	}	
}
